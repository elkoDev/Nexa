package ac.at.uibk.dps.nexa.core.context.datatype;

import ac.at.uibk.dps.nexa.core.context.ContextDataDeserializer;
import ac.at.uibk.dps.nexa.core.context.ContextDataSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ContextDataSerializer.class)
@JsonDeserialize(using = ContextDataDeserializer.class)
public class JsonContextData extends ContextData {

  private final String data;

  private JsonContextData() {
    data = null;
  }

  public JsonContextData(String data) {
    this.data = data;
  }

  @Override
  public String value() {
    return data;
  }

  @Override
  public ContextDataType type() {
    return ContextDataType.JSON;
  }

}
