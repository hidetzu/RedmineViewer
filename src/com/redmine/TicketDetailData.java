package com.redmine;

import java.util.*;

import com.redmine.ticketdetail.*;

public class TicketDetailData {
	private String  mSubject;
	private String  mDescription;
	private ArrayList<Journal> mJournalList;

	public TicketDetailData() {
	}

	public String getSubject() {
		return mSubject;
	}
	
	public void setSubject(String subject) {
		mSubject = subject;
	}
	
	public String getDesscription() {
		return mDescription;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}
	
	public void setJournalList(ArrayList<Journal> list) {
		mJournalList = list;
	}
	
	public ArrayList<Journal> getJournalList() {
		return mJournalList;
	}
}
