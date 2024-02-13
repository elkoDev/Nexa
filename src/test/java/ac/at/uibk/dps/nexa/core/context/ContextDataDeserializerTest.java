package ac.at.uibk.dps.nexa.core.context;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ContextDataDeserializerTest {

  private static ObjectMapper mapper;

  @BeforeAll
  static void setUpBeforeClass() {
    mapper = new ObjectMapper();
    var module = new SimpleModule();
    module.addDeserializer(ContextData.class, new ContextDataDeserializer());
    mapper.registerModule(module);
  }

  @Test
  void testDeserializeJsonContextData() throws Exception {
    String json = "{\"type\":\"JSON\",\"value\":\"{\\\"some_value\\\": {\\\"a\\\": 7}}\"}";
    ContextData data = mapper.readValue(json, ContextData.class);
    assertEquals("{\"some_value\": {\"a\": 7}}", data.value());
  }

  @Test
  void testDeserializeBinaryContextData() throws Exception {
    String json = "{\"type\":\"BINARY\",\"value\":\"AQIDBAU=\"}";
    ContextData data = mapper.readValue(json, ContextData.class);
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, (byte[]) data.value());
  }
}
