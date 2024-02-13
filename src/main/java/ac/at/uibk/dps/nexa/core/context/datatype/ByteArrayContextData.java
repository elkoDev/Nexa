package ac.at.uibk.dps.nexa.core.context.datatype;

public class ByteArrayContextData extends ContextData {

  private final byte[] data;

  private ByteArrayContextData() {
    data = null;
  }

  public ByteArrayContextData(byte[] data) {
    this.data = data;
  }

  @Override
  public byte[] value() {
    return data;
  }

  @Override
  public ContextDataType type() {
    return ContextDataType.BYTE_ARRAY;
  }

}


