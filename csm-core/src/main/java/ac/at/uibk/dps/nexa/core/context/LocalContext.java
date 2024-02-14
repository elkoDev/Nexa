package ac.at.uibk.dps.nexa.core.context;

import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;
import ac.at.uibk.dps.nexa.core.error.CsmRuntimeException;
import java.util.HashMap;
import java.util.Map;

public class LocalContext implements IContext {

  private final Map<String, ContextData> context = new HashMap<>();

  /**
   * Returns the value of the context variable with the given name.
   *
   * @param variableName the name of the variable to get the value of.
   * @return the value of the variable, or null if the variable does not exist.
   */
  @Override
  public ContextData get(String variableName) {
    if (!context.containsKey(variableName)) {
      return null;
    }
    return context.get(variableName);
  }

  /**
   * Creates a new context variable with the given name and value.
   *
   * @param variableName the name of the variable to create.
   * @param value        the value of the variable.
   * @return true if the variable was successfully created, false otherwise.
   */
  @Override
  public boolean create(String variableName, ContextData value) {
    if (context.containsKey(variableName)) {
      return false;
    }
    context.put(variableName, value);
    return true;
  }

  /**
   * Assigns the given value to the context variable with the given name.
   *
   * @param variableName the name of the variable to assign the value to.
   * @param value        the value to assign to the variable.
   * @return true if the value was successfully assigned, false otherwise.
   */
  @Override
  public boolean assign(String variableName, ContextData value) {
    if (!context.containsKey(variableName)) {
      return false;
    }
    context.put(variableName, value);
    return true;
  }

  /**
   * Locks the context variable with the given name.
   *
   * @param variableName the name of the variable to lock.
   * @return true if the variable was successfully locked, false otherwise.
   */
  @Override
  public boolean lock(String variableName) {
    throw new CsmRuntimeException("Locking is not supported in the LocalContext.");
  }

  /**
   * Unlocks the context variable with the given name.
   *
   * @param variableName the name of the variable to unlock.
   * @return true if the variable was successfully unlocked, false otherwise.
   */
  @Override
  public boolean unlock(String variableName) {
    throw new CsmRuntimeException("Unlocking is not supported in the LocalContext.");
  }

  /**
   * Deletes the context variable from the context.
   *
   * @param variableName the name of the variable to delete.
   * @return true if the variable was successfully deleted, false otherwise.
   */
  @Override
  public boolean delete(String variableName) {
    if (!context.containsKey(variableName)) {
      return false;
    }
    context.remove(variableName);
    return true;
  }

}
