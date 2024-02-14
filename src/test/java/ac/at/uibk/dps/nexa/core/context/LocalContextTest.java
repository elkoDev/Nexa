package ac.at.uibk.dps.nexa.core.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ac.at.uibk.dps.nexa.core.context.datatype.BinaryContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.JsonContextData;
import ac.at.uibk.dps.nexa.core.error.CsmRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalContextTest {

  private final IContext context = new LocalContext();

  @BeforeEach
  void setUp() {
    context.delete("some_variable");
  }


  @Test
  void testGetAndCreateJson() {
    context.create("some_variable", new JsonContextData("{\"some_value\": {\"a\": 7}}"));
    var result = context.get("some_variable");
    assertInstanceOf(JsonContextData.class, result);
    assertEquals("{\"some_value\": {\"a\": 7}}", result.value());
  }

  @Test
  void testGetAndCreateBinary() {
    context.create("some_variable", new BinaryContextData(new byte[]{1, 2, 3}));
    var result = context.get("some_variable");
    assertInstanceOf(BinaryContextData.class, result);
    for (int i = 0; i < 3; i++) {
      assertEquals(i + 1, ((BinaryContextData) result).value()[i]);
    }
  }

  @Test
  void testGetNotExisting() {
    var result = context.get("some_variable");
    assertEquals(null, result);
  }

  @Test
  void testAssign() {
    context.create("some_variable", new JsonContextData("{\"some_value\": {\"a\": 7}}"));
    context.assign("some_variable", new JsonContextData("{\"some_value\": {\"a\": 8}}"));
    var result = context.get("some_variable");
    assertInstanceOf(JsonContextData.class, result);
    assertEquals("{\"some_value\": {\"a\": 8}}", result.value());
  }

  @Test
  void testLockNotImplemented() {
    assertThrows(CsmRuntimeException.class, () -> context.lock("variable"));
  }

  @Test
  void testUnlockNotImplemented() {
    assertThrows(CsmRuntimeException.class, () -> context.unlock("variable"));
  }

  @Test
  void testDelete() {
    context.create("some_variable", new JsonContextData("{\"some_value\": {\"a\": 7}}"));
    context.delete("some_variable");
    var result = context.get("some_variable");
    assertEquals(null, result);
  }
}
