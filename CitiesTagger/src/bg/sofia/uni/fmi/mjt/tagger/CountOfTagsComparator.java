package bg.sofia.uni.fmi.mjt.tagger;

import java.util.Comparator;

public class CountOfTagsComparator implements Comparator<City> {
    @Override
    public int compare(City o1, City o2) {
        return o2.getCountOfTags().compareTo(o1.getCountOfTags());
    }
}
