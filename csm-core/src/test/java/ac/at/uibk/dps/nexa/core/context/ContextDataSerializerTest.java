package ac.at.uibk.dps.nexa.core.context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ac.at.uibk.dps.nexa.core.context.datatype.BinaryContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.JsonContextData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ContextDataSerializerTest {

  private static ObjectMapper mapper;

  @BeforeAll
  static void setUpBeforeClass() {
    mapper = new ObjectMapper();
    var module = new SimpleModule();
    module.addSerializer(ContextData.class, new ContextDataSerializer());
    mapper.registerModule(module);
  }


  @Test
  void testSerializeJsonContextData() throws JsonProcessingException {
    ContextData data = new JsonContextData("{\"some_value\": {\"a\": 7}}");
    String json = mapper.writeValueAsString(data);
    assertEquals("{\"type\":\"JSON\",\"value\":\"{\\\"some_value\\\": {\\\"a\\\": 7}}\"}", json);
  }

  @Test
  void testSerializeBinaryContextData() throws JsonProcessingException {
    ContextData data = new BinaryContextData(new byte[]{1, 2, 3, 4, 5});
    String json = mapper.writeValueAsString(data);
    assertEquals("{\"type\":\"BINARY\",\"value\":\"AQIDBAU=\"}", json);
  }


}
