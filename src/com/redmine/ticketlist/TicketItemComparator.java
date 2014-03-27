package com.redmine.ticketlist;

import java.util.*;

public class TicketItemComparator implements Comparator<TicketItem> {

	@Override
	public int compare(TicketItem lhs, TicketItem rhs) {
		Calendar calneCalendar1 = lhs.getUpdateDate();
		Calendar calneCalendar2 = rhs.getUpdateDate();
		int diff = calneCalendar1.compareTo(calneCalendar2);
		
		if(diff==0) return 0;
		if(diff > 0) return -1;
		return 1;
	}

}
