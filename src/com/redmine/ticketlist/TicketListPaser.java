package com.redmine.ticketlist;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;


public interface  TicketListPaser {
	public ArrayList<TicketItem> parse(String dataBody) throws XmlPullParserException, IOException;
}
