package ac.at.uibk.dps.nexa.core.context.datatype;

public class BinaryContextData extends ContextData {

  private final byte[] data;

  private BinaryContextData() {
    data = null;
  }

  public BinaryContextData(byte[] data) {
    this.data = data;
  }

  @Override
  public byte[] value() {
    return data;
  }

  @Override
  public ContextDataType type() {
    return ContextDataType.BINARY;
  }

}


