import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class ElementChildParentPlurals {

    @Attribute
    private String name;

    @ElementList(entry=MergerUtilityStaticVariables.Resource.ATRIBUTE_ITEM, inline=true)
    private List<ElementChildItem> pluralItems;

    public String getName() {
        return name;
    }

    public List<ElementChildItem> getPluralItems() {
        return pluralItems;
    }

    public ElementChildParentPlurals() {}

    public ElementChildParentPlurals(String name) {
        this.name = name;
    }

    public ElementChildParentPlurals(List<ElementChildItem> pluralItems) {
        this.pluralItems = pluralItems;
    }

    @Override
    public boolean equals(Object obj) {
        ElementChildParentPlurals inputPlural = (ElementChildParentPlurals) obj;
        if (name.equals(inputPlural.getName())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPluralItems(List<ElementChildItem> pluralItems) {
        this.pluralItems = pluralItems;
    }

    public void addPluralItem(ElementChildItem item) {
        if (pluralItems == null) pluralItems = new ArrayList<ElementChildItem>();

        pluralItems.add(item);
    }

}
