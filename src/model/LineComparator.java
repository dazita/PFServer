package model;

import java.util.Comparator;

public class LineComparator implements Comparator<Line>{

    @Override
    public int compare(Line o1, Line o2) {
        return o1.getName().compareTo(o2.getName());
    }
    
}
