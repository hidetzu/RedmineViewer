package com.redmine.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Handler;
import android.os.Message;

import com.redmine.TicketDetailData;
import com.redmine.http.GetRequest;
import com.redmine.ticketdetail.Journal;

public class TicketDetailReadThread extends Thread{
	public static final int OK = 1;
	public static final int NG = 2;
	
	
	private GetRequest  mGet;
	private Handler     mHandler;

	public TicketDetailReadThread(GetRequest get, Handler handler) {
		mGet = get;
		this.mHandler = handler;
		this.mGet = get;
	}
	
	public void run() {
		String result = this.mGet.execute();
		if(result != null ) {
			try {
				TicketDetailData res = parseXml(result);
				if( res == null ) {
					/* エラー */
					Message msg = mHandler.obtainMessage(NG);
					mHandler.sendMessage(msg);
				} else {
					Message msg = mHandler.obtainMessage(OK);
					msg.obj = res;
					mHandler.sendMessage(msg);
				}
			} catch (XmlPullParserException e) {
				Message msg = mHandler.obtainMessage(NG);
				mHandler.sendMessage(msg);
			} catch (IOException e) {
				Message msg = mHandler.obtainMessage(NG);
				mHandler.sendMessage(msg);
			}
		} else {
			Message msg = mHandler.obtainMessage(NG);
			mHandler.sendMessage(msg);
		}

	}

	private TicketDetailData parseXml(String dataBody)
			throws XmlPullParserException, IOException {
		TicketDetailData result = null;

		//XMLパーサーを生成する
		final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		final XmlPullParser parser = factory.newPullParser();
		//XMLパーサに解析したい内容を設定する
		parser.setInput(new StringReader(dataBody));

		ArrayList<Journal> journals = null;
    	Journal journal = null;

		
		int eventType = parser.getEventType();
		while( eventType != XmlPullParser.END_DOCUMENT ) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if(parser.getName().equals("subject")){
						if(result == null) {
							result = new TicketDetailData();
						}
						result.setSubject(parser.nextText());
					}
					if(parser.getName().equals("description")) {
						result.setDescription(parser.nextText());
					}

					if(parser.getName().equals("journal")) {
						journal = new Journal();
					} else if( journal != null ) {
						if( parser.getName().equals("user")) {
							String user = parser.getAttributeValue(null, "name");
							journal.setUser(user);
						}
						if( parser.getName().equals("notes")) {
							journal.setNotes(parser.nextText());
						}
						if( parser.getName().equals("created_on")) {
							String tmp   =  parser.nextText();
							String date  =  tmp.split("T")[0];
							String year  =  date.split("-")[0];
							String month =  date.split("-")[1];
							String day   =  date.split("-")[2];
							String times =  tmp.split("T")[1].split("\\+")[0];
							String hourOfDay = times.split(":")[0];
							String minute    = times.split(":")[1];
							String second    = times.split(":")[2];

							journal.setUpdateDate(Integer.valueOf(year),
									Integer.valueOf(month),
									Integer.valueOf(day),
									Integer.valueOf(hourOfDay),
									Integer.valueOf(minute),
									Integer.valueOf(second));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if(parser.getName().equals("journal")) {
						if( journals == null ) {
							journals = new ArrayList<Journal>();
						}
						journals.add(journal);
						journal = null;
					}
					if(parser.getName().equals("journals")) {
						result.setJournalList(journals);
					}
					break;
				default:
					break;
			}
			
			eventType = parser.next();
		}
		
		return result;
	}
}
