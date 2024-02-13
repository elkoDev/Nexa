package ac.at.uibk.dps.nexa.core.expression;

import ac.at.uibk.dps.nexa.core.context.IContext;
import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;
import ac.at.uibk.dps.nexa.core.context.datatype.ContextDataType;
import ac.at.uibk.dps.nexa.core.error.CsmRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cel.common.CelValidationException;
import dev.cel.common.CelVarDecl;
import dev.cel.common.types.SimpleType;
import dev.cel.compiler.CelCompilerFactory;
import dev.cel.parser.CelStandardMacro;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import dev.cel.runtime.CelRuntimeFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class CelExpressionEvaluator implements IExpressionEvaluator {

  private static final CelRuntime CEL_RUNTIME = CelRuntimeFactory.standardCelRuntimeBuilder()
      .build();

  /**
   * Evaluates the given expression with the given extent.
   *
   * @param expression the expression to evaluate.
   * @param extent     the extent to use for evaluation.
   * @return the result of the evaluation.
   */
  @Override
  public Object evaluate(String expression, List<IContext> extent) {
    try {
      var identifiers = findIdentifiers(expression);
      var varDeclarations = createVarDeclarations(identifiers);

      var celCompiler = CelCompilerFactory.standardCelCompilerBuilder()
          .setStandardMacros(CelStandardMacro.STANDARD_MACROS)
          .addVarDeclarations(varDeclarations).build();

      var parseResult = celCompiler.parse(expression);
      var checkResult = celCompiler.check(parseResult.getAst());
      var ast = checkResult.getAst();

      var mapping = getMapping(identifiers, extent);
      var program = CEL_RUNTIME.createProgram(ast);

      return program.eval(mapping);
    } catch (CelValidationException | CelEvaluationException e) {
      throw new CsmRuntimeException("Could not evaluate the expression: " + expression, e);
    }
  }

  /**
   * Find all identifiers in the given abstract syntax tree.
   *
   * @param expression the expression to find the identifiers in.
   * @return a list of all identifiers in the abstract syntax tree.
   */
  private List<String> findIdentifiers(String expression) {
    List<String> identifiers = new ArrayList<>();

    var celCompiler = CelCompilerFactory.standardCelCompilerBuilder()
        .setStandardMacros(CelStandardMacro.STANDARD_MACROS)
        .build();

    try {
      celCompiler.compile(expression).getAst();
    } catch (CelValidationException e) {
      e.getErrors().forEach(cellIssue -> {
        var regex = "'(.*?)'";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(cellIssue.getMessage());
        while (matcher.find()) {
          var potentialIdentifier = matcher.group(1);
          if (!potentialIdentifier.isEmpty()) {
            identifiers.add(potentialIdentifier);
          }
        }
      });
    }

    return identifiers;
  }

  /**
   * Create a list of variable declarations from the given list of identifiers.
   *
   * @param identifiers the list of identifiers to create variable declarations from.
   * @return a list of variable declarations.
   */
  private List<CelVarDecl> createVarDeclarations(List<String> identifiers) {
    List<CelVarDecl> declarations = new ArrayList<>();
    for (String identifier : identifiers) {
      declarations.add(CelVarDecl.newVarDeclaration(identifier, SimpleType.DYN));
    }

    return declarations;
  }

  private Map<String, Object> getMapping(List<String> identifiers, List<IContext> extent) {
    Map<String, Object> mapping = new HashMap<>();

    for (String identifier : identifiers) {
      if (mapping.containsKey(identifier)) {
        continue;
      }
      for (IContext context : extent) {
        ContextData value = context.get(identifier);
        if (value == null) {
          continue;
        }
        ContextDataType type = value.type();
        if (Objects.requireNonNull(type) == ContextDataType.STRING) {
          mapping.put(identifier, getObjectFromJson((String) value.value()));
        } else if (type == ContextDataType.BYTE_ARRAY) {
          mapping.put(identifier, value.value());
        }
      }
    }

    return mapping;
  }

  /**
   * Get the object from the given JSON string.
   *
   * @param json the JSON string to get the object from.
   * @return the object from the given JSON string.
   */
  private Object getObjectFromJson(String json) {
    var objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(json, Object.class);
    } catch (Exception e) {
      throw new CsmRuntimeException("Could not parse the JSON: " + json, e);
    }
  }
}
