package ac.at.uibk.dps.nexa.core.context.datatype;

import ac.at.uibk.dps.nexa.core.context.ContextDataDeserializer;
import ac.at.uibk.dps.nexa.core.context.ContextDataSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ContextDataSerializer.class)
@JsonDeserialize(using = ContextDataDeserializer.class)
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


