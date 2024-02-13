package ac.at.uibk.dps.nexa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cel.common.CelValidationException;
import dev.cel.common.CelVarDecl;
import dev.cel.common.types.SimpleType;
import dev.cel.compiler.CelCompilerFactory;
import dev.cel.parser.CelStandardMacro;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntimeFactory;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args)
      throws CelEvaluationException, CelValidationException, JsonProcessingException {
    var expression = "some_list.all(i, i<7) && some_integer == 5 && some_object.some_value.a == 7";

    var objectMapper = new ObjectMapper();
    var var1 = objectMapper.readValue("5", Object.class);
    var var2 = objectMapper.readValue("{\"some_value\": {\"a\": 7}}", Object.class);
    var var3 = objectMapper.readValue("[5,4]", Object.class);

    List<CelVarDecl> declarations = List.of(
        CelVarDecl.newVarDeclaration("some_list", SimpleType.DYN),
        CelVarDecl.newVarDeclaration("some_integer", SimpleType.DYN),
        CelVarDecl.newVarDeclaration("some_object", SimpleType.DYN)
    );
    var celCompiler = CelCompilerFactory.standardCelCompilerBuilder()
        .setStandardMacros(CelStandardMacro.STANDARD_MACROS)
        .addVarDeclarations(declarations).build();

    var celRuntime = CelRuntimeFactory.standardCelRuntimeBuilder().build();

    // Compile the expression into an Abstract Syntax Tree.
    var parseResult = celCompiler.parse(expression);
    var checkResult = celCompiler.check(parseResult.getAst());

    var ast = checkResult.getAst();

    // Plan an executable program instance.
    var program = celRuntime.createProgram(ast);

    // Evaluate the program with an input variable.
    Object result = program.eval(Map.of("some_integer", var1, "some_object", var2,  "some_list", var3));
    System.out.println(result);

  }
}
