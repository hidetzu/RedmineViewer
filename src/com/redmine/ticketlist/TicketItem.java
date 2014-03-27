package com.redmine.ticketlist;

import java.util.*;

public class TicketItem {
	private CharSequence mSubject;
	private Calendar     mCalendar;
	private int          mId;
	
	public TicketItem() {
		mSubject = "";
	}
	
	public CharSequence getSubject() {
		return mSubject;
	}
	
	public void setSubject(CharSequence subject) {
		mSubject = subject;
	}
	
	public void setUpdateDate(int year, int month, int date, int hourOfDay, int minute, int second){
		mCalendar = Calendar.getInstance();
		mCalendar.set(year, month, date, hourOfDay, minute, second);
	}
	
	public Calendar getUpdateDate() {
		return mCalendar;
	}
	
	
	public int getTicketID() {
		return mId;
	}
	
	public void setTicketID(int id) {
		mId = id;
	}
}
