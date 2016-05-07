import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class ElementChildString {

    public enum System {IOS, ANDROID, BOTH}

    @Attribute(name=MergerUtilityStaticVariables.Resource.ATRIBUTE_NAME)
    private String name;

    @Attribute(required=false)
    private String translatable;

    private String text;

    private System typeOs;

    public ElementChildString() {}

    public ElementChildString(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public void setKey(String name) {
        this.name = name;
    }

    public void setTranslatable(String translatable) {
        this.translatable = translatable;
    }

    public void setTypeOs(System typeOs) {
        this.typeOs = typeOs;
    }

    @Text(required=false)
    public String getValue() {
        return text;
    }

    @Text(required=false)
    public void setValue(String text) {
        this.text = text;
    }

    public String getKey() {
        return name;
    }

    public String isTranslatable() {
        return translatable;
    }

    public System getTypeOs() {
        return typeOs;
    }


    @Override
    public String toString() {
        if (translatable == null) {
            return "String name: "+name+"; String text: "+text+"; String for OS: "+typeOs.toString()+"\n";

        }
        return "String name: "+name+"; String translatable: "+translatable+"; String text: "+text+"; String for OS: "+typeOs.toString()+"\n";
    }

    @Override
    public boolean equals(Object obj) {
        ElementChildString inputString = (ElementChildString) obj;

        boolean result = false;
        if((text == null) && (inputString.getValue() == null)) {
            result = (name.equals(inputString.getKey()));
        } else {
            result = ((name.equals(inputString.getKey())) && (text.equals(inputString.getValue())));
        }


        if (result == true) {
            if (typeOs != inputString.typeOs) {
                typeOs = System.BOTH;
                inputString.setTypeOs(System.BOTH);
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        if (text == null) {
            return name.hashCode();
        }
        return (name.hashCode()+text.hashCode());
    }
}
