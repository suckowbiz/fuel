package biz.suckow.fuel.business.consumption.control;

import java.util.Comparator;

import biz.suckow.fuel.business.refueling.entity.Refueling;

public class RefuelingOldestFirstComparator implements Comparator<Refueling> {

    @Override
    public int compare(Refueling left, Refueling right) {
	if (right == null && left == null) {
	    return 0;
	} else if (left == null) {
	    return 1;
	} else if (right == null) {
	    return -1;
	}
	// compare vice versa to have older above newer
	return left.getDate().compareTo(right.getDate());
    }

}
