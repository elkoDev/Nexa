package ac.at.uibk.dps.nexa.core.context;


import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;

public interface IContext {

  /**
   * Returns the value of the context variable with the given name.
   *
   * @param variableName the name of the variable to get the value of.
   * @return the value of the variable, or null if the variable does not exist.
   */
  ContextData get(String variableName);

  /**
   * Creates a new context variable with the given name and value.
   *
   * @param variableName the name of the variable to create.
   * @param value        the value of the variable.
   * @return true if the variable was successfully created, false otherwise.
   */
  boolean create(String variableName, ContextData value);

  /**
   * Assigns the given value to the context variable with the given name.
   *
   * @param variableName the name of the variable to assign the value to.
   * @param value        the value to assign to the variable.
   * @return true if the value was successfully assigned, false otherwise.
   */
  boolean assign(String variableName, ContextData value);

  /**
   * Locks the context variable with the given name.
   *
   * @param variableName the name of the variable to lock.
   * @return true if the variable was successfully locked, false otherwise.
   */
  boolean lock(String variableName);

  /**
   * Unlocks the context variable with the given name.
   *
   * @param variableName the name of the variable to unlock.
   * @return true if the variable was successfully unlocked, false otherwise.
   */
  boolean unlock(String variableName);

  /**
   * Deletes the context variable from the context.
   *
   * @param variableName the name of the variable to delete.
   * @return true if the variable was successfully deleted, false otherwise.
   */
  boolean delete(String variableName);

}
