import java.util.*;

public class DuplicateCleaner {

    public static ElementParentResources getListWithoutDuplicates(ArrayList<ElementParentResources> resources) {

        HashSet<ElementChildString> allStrings = new HashSet<ElementChildString>();
        HashSet<ElementChildParentStringArrays> allArrayStrings = new HashSet<ElementChildParentStringArrays>();
        HashSet<ElementChildParentPlurals> allPlurals = new HashSet<ElementChildParentPlurals>();

        for (ElementParentResources parentResources : resources) {
            if (parentResources.getStrings() != null) {
                allStrings.addAll(parentResources.getStrings());
            }

            if (parentResources.getStringArrays() != null) {
                allArrayStrings.addAll(parentResources.getStringArrays());
            }

            if (parentResources.getPlurals() != null) {
               allPlurals.addAll(parentResources.getPlurals());
            }
        }

        System.out.println("All strings without duplicates: "+allStrings.size());
        System.out.println(allStrings);

        ArrayList<ElementChildString> allSortedStrings = new ArrayList<ElementChildString>();
        allSortedStrings.addAll(allStrings);

        Collections.sort(allSortedStrings, new Comparator<ElementChildString>() {
            @Override
            public int compare(ElementChildString o1, ElementChildString o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        System.out.println();
        System.out.println("All strings without duplicates: "+allStrings.size()+" , sorted by name:");
        System.out.println(allSortedStrings);

        System.out.println("All array strings "+allArrayStrings.size());
        System.out.println("All plurals : "+allPlurals.size());

        ElementParentResources allResources = new ElementParentResources(allSortedStrings,
                new ArrayList<ElementChildParentStringArrays>(allArrayStrings),
                new ArrayList<ElementChildParentPlurals>(allPlurals));

        return allResources;
    }
}
