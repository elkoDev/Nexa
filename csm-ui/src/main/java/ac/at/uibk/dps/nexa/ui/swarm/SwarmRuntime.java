package ac.at.uibk.dps.nexa.ui.swarm;

import ac.at.uibk.dps.nexa.ui.Runtime;
import ac.at.uibk.dps.nexa.ui.RuntimeType;
import lombok.Getter;

@Getter
public class SwarmRuntime extends Runtime {

  private final String serviceId;
  private final String serviceName;

  public SwarmRuntime(boolean isDynamic, String ipAddress, int port, String serviceId,
      String serviceName) {
    super(isDynamic, ipAddress, port);
    this.serviceId = serviceId;
    this.serviceName = serviceName;
  }

  @Override
  public RuntimeType getRuntimeType() {
    return RuntimeType.SWARM;
  }

}
