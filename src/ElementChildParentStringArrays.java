import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class ElementChildParentStringArrays {

    @Attribute
    private String name;

    @ElementList(entry=MergerUtilityStaticVariables.Resource.ATRIBUTE_ITEM, inline=true)
    private List<ElementChildItem> arrayItems;

    public String getName() {
        return name;
    }

    public List<ElementChildItem> getArrayItems() {
        return arrayItems;
    }

    public ElementChildParentStringArrays() {}

    public ElementChildParentStringArrays(List<ElementChildItem> arrayItems) {
        this.arrayItems = arrayItems;
    }

    public ElementChildParentStringArrays(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        ElementChildParentStringArrays inputStringArray = (ElementChildParentStringArrays) obj;
        if (name.equals(inputStringArray.getName())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setArrayItems(List<ElementChildItem> arrayItems) {
        this.arrayItems = arrayItems;
    }

    public void addStringArrayItem(ElementChildItem item) {
        if (arrayItems == null) arrayItems = new ArrayList<ElementChildItem>();

        arrayItems.add(item);
    }

}
