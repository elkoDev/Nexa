package ac.at.uibk.dps.nexa.core.error;

public class CsmRuntimeException extends RuntimeException {

  public CsmRuntimeException(String message) {
    super(message);
  }

  public CsmRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
