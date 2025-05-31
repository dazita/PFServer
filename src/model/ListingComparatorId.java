package model;

import java.util.Comparator;

public class ListingComparatorId implements Comparator<Listing> {

    @Override
    public int compare(Listing o1, Listing o2) {
        return o1.getId() - o2.getId();
    }
    
}