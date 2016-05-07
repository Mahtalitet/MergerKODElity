import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name=MergerUtilityStaticVariables.Resource.RESOURCE_PARENT)
public class ElementParentResources {

    @ElementList(entry=MergerUtilityStaticVariables.Resource.RESOURCE_STRING, inline=true, required=false)
    private List<ElementChildString> strings;

    @ElementList(entry=MergerUtilityStaticVariables.Resource.RESOURCE_STRING_ARRAY, inline=true, required=false)
    private List<ElementChildParentStringArrays> stringArrays;

    @ElementList(entry=MergerUtilityStaticVariables.Resource.RESOURCE_PLURALS, inline=true, required=false)
    private List<ElementChildParentPlurals> plurals;

    public ElementParentResources() {}

    public ElementParentResources(List<ElementChildString> strings) {
        this.strings = strings;
    }

    public ElementParentResources(List<ElementChildString> strings, List<ElementChildParentStringArrays> stringArrays, List<ElementChildParentPlurals> plurals) {
        this.strings = strings;
        this.stringArrays = stringArrays;
        this.plurals = plurals;
    }

    public List<ElementChildString> getStrings() {
        return strings;
    }

    public List<ElementChildParentStringArrays> getStringArrays() {
        return stringArrays;
    }

    public List<ElementChildParentPlurals> getPlurals() {
        return plurals;
    }

    public void setStrings(List<ElementChildString> strings) {
        this.strings = strings;
    }

    public void setStringArrays(List<ElementChildParentStringArrays> stringArrays) {
        this.stringArrays = stringArrays;
    }

    public void setPlurals(List<ElementChildParentPlurals> plurals) {
        this.plurals = plurals;
    }

    public void addStringResource(ElementChildString string) {
        if (strings == null) strings = new ArrayList<ElementChildString>();
        strings.add(string);

    }

    public void addStringArrayResource(ElementChildParentStringArrays stringArray) {
        if (stringArrays == null) stringArrays = new ArrayList<ElementChildParentStringArrays>();
        stringArrays.add(stringArray);
    }

    public void addPluralResource(ElementChildParentPlurals plural) {
        if (plurals == null) plurals = new ArrayList<ElementChildParentPlurals>();
        plurals.add(plural);
    }

    public boolean isSomeResources() {
        if ((strings == null) && (stringArrays == null) && (plurals == null)) {
            return false;
        } else return true;
    }

}
