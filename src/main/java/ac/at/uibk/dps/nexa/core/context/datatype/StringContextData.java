package ac.at.uibk.dps.nexa.core.context.datatype;

public class StringContextData extends ContextData {

  private final String data;

  private StringContextData() {
    data = null;
  }

  public StringContextData(String data) {
    this.data = data;
  }

  @Override
  public String value() {
    return data;
  }

  @Override
  public ContextDataType type() {
    return ContextDataType.STRING;
  }

}
