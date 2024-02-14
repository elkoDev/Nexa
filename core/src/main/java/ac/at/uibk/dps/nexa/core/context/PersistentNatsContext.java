package ac.at.uibk.dps.nexa.core.context;

import ac.at.uibk.dps.nexa.core.context.datatype.ContextData;
import ac.at.uibk.dps.nexa.core.error.CsmRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.nats.client.Connection;
import io.nats.client.Nats;

public class PersistentNatsContext implements IContext {

  private static final String BUCKET_NAME = "CSM_PERSISTENT_CONTEXT";
  private final String serverUrl;
  private final ObjectMapper mapper;

  public PersistentNatsContext(String serverUrl) {
    this.serverUrl = serverUrl;
    mapper = new ObjectMapper();
    var module = new SimpleModule();
    module.addSerializer(ContextData.class, new ContextDataSerializer());
    module.addDeserializer(ContextData.class, new ContextDataDeserializer());
    mapper.registerModule(module);
  }

  /**
   * Returns the value of the context variable with the given name.
   *
   * @param variableName the name of the variable to get the value of.
   * @return the value of the variable, or null if the variable does not exist.
   */
  @Override
  public ContextData get(String variableName) {
    try (Connection nc = Nats.connect(serverUrl)) {
      var kv = nc.keyValue(BUCKET_NAME);
      var value = kv.get(variableName);
      if (value == null) {
        return null;
      }
      return mapper.readValue(value.getValue(), ContextData.class);
    } catch (Exception e) {
      return null;
    }
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
    try (Connection nc = Nats.connect(serverUrl)) {
      var kv = nc.keyValue(BUCKET_NAME);
      var serializedValue = mapper.writeValueAsBytes(value);
      kv.create(variableName, serializedValue);
      return true;
    } catch (Exception e) {
      return false;
    }
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
    try (Connection nc = Nats.connect(serverUrl)) {
      var kv = nc.keyValue(BUCKET_NAME);
      var serializedValue = mapper.writeValueAsBytes(value);
      kv.put(variableName, serializedValue);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Locks the context variable with the given name.
   *
   * @param variableName the name of the variable to lock.
   * @return true if the variable was successfully locked, false otherwise.
   */
  @Override
  public boolean lock(String variableName) {
    throw new CsmRuntimeException("Locking is not supported in the PersistentNatsContext.");
  }

  /**
   * Unlocks the context variable with the given name.
   *
   * @param variableName the name of the variable to unlock.
   * @return true if the variable was successfully unlocked, false otherwise.
   */
  @Override
  public boolean unlock(String variableName) {
    throw new CsmRuntimeException("Unlocking is not supported in the PersistentNatsContext.");
  }

  /**
   * Deletes the context variable from the context.
   *
   * @param variableName the name of the variable to delete.
   * @return true if the variable was successfully deleted, false otherwise.
   */
  @Override
  public boolean delete(String variableName) {
    try (Connection nc = Nats.connect(serverUrl)) {
      var kv = nc.keyValue(BUCKET_NAME);
      kv.delete(variableName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
