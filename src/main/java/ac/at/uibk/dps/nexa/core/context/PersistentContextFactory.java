package ac.at.uibk.dps.nexa.core.context;

import ac.at.uibk.dps.nexa.core.error.CsmRuntimeException;

public class PersistentContextFactory {

  private PersistentContextFactory() {
  }

  public static IContext build(String serverUrl) {
    if (serverUrl.contains("nats")) {
      return new PersistentNatsContext();
    }
    throw new CsmRuntimeException("Unsupported context type");
  }

}
