import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class ElementChildItem {

    @Attribute(required=false)
    private String quantity;

    public String text;

    public ElementChildItem() {}

    public ElementChildItem(String text) {
        this.text = text;
    }

    public ElementChildItem(String quantity, String text) {
        this.text = text;
        this.quantity = quantity;
    }

    @Text(required=false)
    public String getValue() {
        return text;
    }

    @Text(required=false)
    public void setValue(String text) {
        this.text = text;
    }

    public String getQuantity() {
        return quantity;
    }
}
