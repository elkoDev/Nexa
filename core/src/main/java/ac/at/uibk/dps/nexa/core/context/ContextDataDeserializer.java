package ac.at.uibk.dps.nexa.core.context;

import ac.at.uibk.dps.nexa.core.context.datatype.BinaryContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.JsonContextData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class ContextDataDeserializer extends JsonDeserializer<ContextData> {

  /**
   * @param jsonParser             the json parser
   * @param deserializationContext the deserialization context
   * @return the deserialized context data
   * @throws IOException if an error occurs during deserialization
   */
  @Override
  public ContextData deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);

    JsonNode typeNode = node.get("type");
    if (typeNode != null) {
      JsonParser typeParser = typeNode.traverse();
      typeParser.nextToken();
      String type = deserializationContext.readValue(typeParser, String.class);
      if (type.equals("JSON")) {
        JsonNode valueNode = node.get("value");
        if (valueNode != null) {
          JsonParser valueParser = valueNode.traverse();
          valueParser.nextToken();
          return new JsonContextData(deserializationContext.readValue(valueParser, String.class));
        }
      } else if (type.equals("BINARY")) {
        JsonNode valueNode = node.get("value");
        if (valueNode != null) {
          JsonParser valueParser = valueNode.traverse();
          valueParser.nextToken();
          return new BinaryContextData(deserializationContext.readValue(valueParser, byte[].class));
        }
      }
    }
    return null;
  }
}
