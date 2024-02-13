package ac.at.uibk.dps.nexa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import ac.at.uibk.dps.nexa.core.context.IContext;
import ac.at.uibk.dps.nexa.core.context.LocalContext;
import ac.at.uibk.dps.nexa.core.context.datatype.ByteArrayContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.StringContextData;
import ac.at.uibk.dps.nexa.core.expression.CelExpressionEvaluator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CelExpressionEvaluatorTest {
  public static List<IContext> extent = new ArrayList<>();
  public static CelExpressionEvaluator evaluator = new CelExpressionEvaluator();

  @BeforeAll
  static void setUp() {
    LocalContext localContext = new LocalContext();
    localContext.create("some_integer", new StringContextData("0"));
    localContext.create("some_float", new StringContextData("2.35"));
    localContext.create("some_boolean", new StringContextData("true"));
    localContext.create("some_string", new StringContextData("\"hello\""));
    localContext.create("some_list", new StringContextData("[1, 2, 3]"));
    localContext.create("some_object", new StringContextData("{\"some_value\": {\"a\": 7}}"));
    localContext.create("some_byte_array", new ByteArrayContextData(new byte[]{1, 2, 3, 4, 5}));

    extent.add(localContext);
  }

  @Test
  void testIntegerResolving(){
    Object result = evaluator.evaluate("some_integer", extent);
    assertInstanceOf(Long.class, result);
    assertEquals(0L, result);
  }

  @Test
  void testFloatResolving(){
    Object result = evaluator.evaluate("some_float", extent);
    assertInstanceOf(Double.class, result);
    assertEquals(2.35, result);
  }

  @Test
  void testBooleanResolving(){
    Object result = evaluator.evaluate("some_boolean", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testStringResolving(){
    Object result = evaluator.evaluate("some_string", extent);
    assertInstanceOf(String.class, result);
    assertEquals("hello", result);
  }

  @Test
  void testListResolving(){
    Object result = evaluator.evaluate("some_list", extent);
    assertInstanceOf(List.class, result);
    List<Integer> expected = List.of(1, 2, 3);
    assertEquals(expected.size(), ((List<?>) result).size());
    for (int i = 0; i < expected.size(); i++) {
      assertInstanceOf(Integer.class, ((List<?>) result).get(i));
      assertEquals(expected.get(i), ((List<?>) result).get(i));
    }
  }

  @Test
  void testObjectResolving(){
    Object result = evaluator.evaluate("some_object", extent);
    assertInstanceOf(Map.class, result);
    ((Map<String, Object>) result).forEach((key, value) -> {
      assertInstanceOf(String.class, key);
      assertInstanceOf(Map.class, value);
      ((Map<String, Object>) value).forEach((key2, value2) -> {
        assertInstanceOf(String.class, key2);
        assertInstanceOf(Integer.class, value2);
        assertEquals(7, value2);
      });
    });
  }

  @Test
  void testByteArrayResolving(){
    Object result = evaluator.evaluate("some_byte_array", extent);
    var expected = new byte[]{1, 2, 3, 4, 5};
    assertInstanceOf(byte[].class, result);
    assertEquals(expected.length, ((byte[]) result).length);
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], ((byte[]) result)[i]);
    }
  }

  @Test
  void testFalseIntegerExpression(){
    Object result = evaluator.evaluate("some_integer == 5", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(false, result);
  }

  @Test
  void testTrueIntegerExpression(){
    Object result = evaluator.evaluate("some_integer == 0", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testFalseFloatExpression(){
    Object result = evaluator.evaluate("some_float == 5.0", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(false, result);
  }

  @Test
  void testTrueFloatExpression(){
    Object result = evaluator.evaluate("some_float == 2.35", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testFalseBooleanExpression(){
    Object result = evaluator.evaluate("some_boolean == false", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(false, result);
  }

  @Test
  void testTrueBooleanExpression(){
    Object result = evaluator.evaluate("some_boolean == true", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testFalseStringExpression(){
    Object result = evaluator.evaluate("some_string == \"world\"", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(false, result);
  }

  @Test
  void testTrueStringExpression(){
    Object result = evaluator.evaluate("some_string == \"hello\"", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testFalseListExpression(){
    Object result = evaluator.evaluate("some_list == [1, 2, 3, 4]", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(false, result);
  }

  @Test
  void testTrueListExpression(){
    Object result = evaluator.evaluate("some_list == [1, 2, 3]", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testFalseObjectExpression(){
    Object result = evaluator.evaluate("some_object.some_value.a == 5", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(false, result);
  }

  @Test
  void testTrueObjectExpression(){
    Object result = evaluator.evaluate("some_object.some_value.a == 7", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testFalseListAllExpression(){
    Object result = evaluator.evaluate("some_list.all(i, i<3)", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(false, result);
  }

  @Test
  void testTrueListAllExpression(){
    Object result = evaluator.evaluate("some_list.all(i, i<7)", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

  @Test
  void testMixedExpression(){
    Object result = evaluator.evaluate("some_list.all(i, i<7) && some_integer == 0 && some_object.some_value.a == 7", extent);
    assertInstanceOf(Boolean.class, result);
    assertEquals(true, result);
  }

}
