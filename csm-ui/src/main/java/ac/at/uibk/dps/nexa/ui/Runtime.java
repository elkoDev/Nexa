package ac.at.uibk.dps.nexa.ui;

import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class Runtime {

  private final boolean isDynamic;
  private final String ipAddress;
  private final int port;
  private final UUID id = UUID.randomUUID();


  protected Runtime(boolean isDynamic, String ipAddress, int port) {
    this.isDynamic = isDynamic;
    this.ipAddress = ipAddress;
    this.port = port;
  }

  public abstract RuntimeType getRuntimeType();

}
