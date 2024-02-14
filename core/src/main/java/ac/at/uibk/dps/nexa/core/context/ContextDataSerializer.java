package ac.at.uibk.dps.nexa.core.context;

import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.ContextDataType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ContextDataSerializer extends StdSerializer<ContextData> {

  public ContextDataSerializer() {
    this(null);
  }

  public ContextDataSerializer(Class<ContextData> t) {
    super(t);
  }

  /**
   * @param contextData        the context data to serialize
   * @param jsonGenerator      the json generator
   * @param serializerProvider the serializer provider
   * @throws IOException if an error occurs during serialization
   */
  @Override
  public void serialize(ContextData contextData, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("type", contextData.type().name());
    jsonGenerator.writeFieldName("value");
    if (contextData.type() == ContextDataType.JSON) {
      jsonGenerator.writeString((String) contextData.value());
    } else {
      jsonGenerator.writeBinary((byte[]) contextData.value());
    }
    jsonGenerator.writeEndObject();
  }
}
