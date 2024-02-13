package ac.at.uibk.dps.nexa.core.context.datatype;

public class JsonContextData extends ContextData {

  private final String data;

  private JsonContextData() {
    data = null;
  }

  public JsonContextData(String data) {
    this.data = data;
  }

  @Override
  public String value() {
    return data;
  }

  @Override
  public ContextDataType type() {
    return ContextDataType.JSON;
  }

}
