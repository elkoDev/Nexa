package ac.at.uibk.dps.nexa.ui;

import java.util.Map;

public interface IRuntimeManager {

  /**
   * Selects a runtime based on the given properties.
   *
   * @param properties the properties to use for selection.
   * @return the selected runtime.
   */
  Runtime selectRuntime(Map<String, String> properties);

  /**
   * Creates a runtime based on the given properties.
   *
   * @param properties the properties to use for creation.
   * @return the created runtime.
   */
  Runtime createRuntime(Map<String, String> properties);

  /**
   * Removes the given runtime from the manager.
   *
   * @param runtime the runtime to remove.
   */
  void removeRuntime(Runtime runtime);

  /**
   * Cleans up and stops all managed runtimes.
   */
  void cleanUp();

}
