package com.redmine.ticketdetail;

import java.util.*;

public class Journal {

	private String    mUser;
	private String    mNotes;
	private Calendar  mCalendar;
	
	public Journal() {
		mUser = "";
		mNotes = "";
	}

	public String getUser() {
		return mUser;
	}

	public void setUser(String user) {
		mUser = user;
	}

	public String getNotes() {
		return mNotes;
	}

	public void setNotes(String notes) {
		mNotes = notes;
	}

	public void setUpdateDate(int year, int month, int date, int hourOfDay, int minute, int second){
		mCalendar = Calendar.getInstance();
		mCalendar.set(year, month, date, hourOfDay, minute, second);
	}
	
	public Calendar getUpdateDate() {
		return mCalendar;
	}
	
}