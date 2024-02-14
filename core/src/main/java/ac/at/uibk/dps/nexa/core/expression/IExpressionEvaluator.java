package ac.at.uibk.dps.nexa.core.expression;

import ac.at.uibk.dps.nexa.core.context.IContext;
import java.util.List;

public interface IExpressionEvaluator {

  /**
   * Evaluates the given expression with the given extent.
   *
   * @param expression the expression to evaluate.
   * @param extent     the extent to use for evaluation.
   * @return the result of the evaluation.
   */
  Object evaluate(String expression, List<IContext> extent);

}
