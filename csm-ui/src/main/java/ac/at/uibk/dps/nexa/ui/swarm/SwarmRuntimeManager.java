package ac.at.uibk.dps.nexa.ui.swarm;

import ac.at.uibk.dps.nexa.core.error.CsmRuntimeException;
import ac.at.uibk.dps.nexa.ui.IRuntimeManager;
import ac.at.uibk.dps.nexa.ui.Runtime;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.EndpointSpec;
import com.github.dockerjava.api.model.EndpointVirtualIP;
import com.github.dockerjava.api.model.PortConfig;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.ServiceUpdateState;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient.Builder;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.bouncycastle.cms.CMSRuntimeException;

public class SwarmRuntimeManager implements IRuntimeManager {

  private static final String DEFAULT_DOCKER_HOST = "tcp://localhost:2375";
  private static final int CSM_RUNTIME_PORT = 0xABBA;
  private static final String CSM_RUNTIME_IMAGE = "csm-runtime";
  private static final Random RANDOM = new Random();

  private final DockerClient dockerClient;
  private final Map<String, SwarmRuntime> runtimes = new HashMap<>();

  public SwarmRuntimeManager() {
    this(DEFAULT_DOCKER_HOST);
  }

  public SwarmRuntimeManager(String dockerHost) {
    var config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(dockerHost)
        .build();

    var httpClient = new Builder()
        .dockerHost(config.getDockerHost())
        .maxConnections(100)
        .connectionTimeout(Duration.ofSeconds(30))
        .responseTimeout(Duration.ofSeconds(45))
        .build();

    dockerClient = DockerClientImpl.getInstance(config, httpClient);
  }

  private int getUnusedPort() {
    Set<Integer> usedPorts = runtimes.values().stream()
        .map(Runtime::getPort).collect(Collectors.toSet());
    int port;
    do {
      port = 49152 + RANDOM.nextInt(65535 - 49152 + 1);
    } while (usedPorts.contains(port));

    return port;
  }

  /**
   * Selects a runtime based on the given properties.
   *
   * @param properties the properties to use for selection.
   * @return the selected runtime.
   */
  @Override
  public Runtime selectRuntime(Map<String, String> properties) {
    // TODO: Implement runtime selection based on properties.
    return null;
  }

  /**
   * Creates a runtime based on the given properties.
   *
   * @param properties the properties to use for creation.
   * @return the created runtime.
   */
  @Override
  public Runtime createRuntime(Map<String, String> properties) {
    int publishedPort = getUnusedPort();
    String serviceName = "csm-runtime-" + publishedPort;

    var serviceSpec = new ServiceSpec()
        .withName(serviceName)
        .withEndpointSpec(new EndpointSpec()
            .withPorts(Collections.singletonList(new PortConfig()
                .withPublishedPort(publishedPort)
                .withTargetPort(CSM_RUNTIME_PORT)
            ))
        ).withTaskTemplate(
            new TaskSpec().withContainerSpec(new ContainerSpec()
                .withImage(CSM_RUNTIME_IMAGE))
        );

    var serviceId = dockerClient.createServiceCmd(serviceSpec).exec().getId();

    var maxRetries = 10;
    var retryDelayMillis = 1000;

    Service service = null;
    for (int i = 0; i < maxRetries; i++) {
      service = dockerClient.inspectServiceCmd(serviceId).exec();
      if (service == null) {
        throw new CsmRuntimeException(
            "Failed to inspect the runtime service after creation.");
      }
      var updateState = Objects.requireNonNull(service.getUpdateStatus()).getState();

      if (i == maxRetries - 1 && (updateState != ServiceUpdateState.COMPLETED)) {
        throw new CsmRuntimeException("Failed to create runtime.");
      } else if (updateState == ServiceUpdateState.COMPLETED) {
        break;
      }
      try {
        Thread.sleep(retryDelayMillis);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    EndpointVirtualIP[] virtualIPs;
    try {
      virtualIPs = service.getEndpoint().getVirtualIPs();
    } catch (NullPointerException e) {
      throw new CMSRuntimeException("Failed to fetch the virtual IP of the runtime service.");
    }
    assert virtualIPs != null;
    String ipAddress = Arrays.stream(virtualIPs).findFirst()
        .orElseThrow(() -> new CMSRuntimeException(
            "Failed to fetch the virtual IP of the runtime service."))
        .getAddr();

    var swarmRuntime = new SwarmRuntime(false, ipAddress, publishedPort, serviceId, serviceName);
    runtimes.put(serviceId, swarmRuntime);

    return swarmRuntime;
  }

  /**
   * Removes the given runtime from the manager.
   *
   * @param runtime the runtime to remove.
   */
  @Override
  public void removeRuntime(Runtime runtime) {
    var swarmRuntime = (SwarmRuntime) runtime;
    try {
      dockerClient.removeServiceCmd(swarmRuntime.getServiceId()).exec();
    } catch (NotFoundException e) {
      throw new CsmRuntimeException("Could not find the runtime service for removal.");
    }
    runtimes.remove(swarmRuntime.getServiceId());
  }

  /**
   * Cleans up and stops all managed runtimes.
   */
  @Override
  public void cleanUp() {
    runtimes.values().forEach(this::removeRuntime);
  }

}
