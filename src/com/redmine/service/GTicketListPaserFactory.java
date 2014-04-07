package com.redmine.service;

import com.redmine.ticketlist.TicketListPaserFactory;
import com.redmine.ticketlist.TicketListPaserImplimention;

public class GTicketListPaserFactory {
	private static TicketListPaserFactory s_paserFactory
				= new TicketListPaserImplimention();

	public static void setFactory(TicketListPaserFactory factory) {
		s_paserFactory = factory;
	}
	
	public static TicketListPaserFactory getFactory() {
		return s_paserFactory;
	}
}
