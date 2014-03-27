package com.redmine.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Handler;
import android.os.Message;

import com.redmine.http.GetRequest;
import com.redmine.ticketlist.TicketItem;
import com.redmine.ticketlist.TicketItemComparator;

public class TicketListThread extends Thread{
	public static final int OK = 1;
	public static final int NG = 2;
	
	
	private GetRequest  mGet;
	private Handler     mHandler;

	public TicketListThread(GetRequest get, Handler handler) {
		mGet = get;
		this.mHandler = handler;
		this.mGet = get;
	}
	
	public void run() {
		String result = this.mGet.execute();
		if(result != null ) {
			ArrayList<TicketItem> list;
			try {
				list = parseXml(result);
				Collections.sort(list, new TicketItemComparator());
				Message msg = mHandler.obtainMessage(OK);
				msg.obj = list;
				mHandler.sendMessage(msg);
				
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

	private ArrayList<TicketItem> parseXml(String dataBody) throws XmlPullParserException, IOException {
		//XMLパーサーを生成する
		final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		final XmlPullParser parser = factory.newPullParser();
		//XMLパーサに解析したい内容を設定する
		parser.setInput(new StringReader(dataBody));

		ArrayList<TicketItem> result = new ArrayList<TicketItem>();
		TicketItem item = null;
		int eventType = parser.getEventType();
		while( eventType != XmlPullParser.END_DOCUMENT ) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if(parser.getName().equals("issue")) {
						item = new TicketItem();
					} else if( item != null) {
						if(parser.getName().equals("subject")){
							item.setSubject(parser.nextText());
						} else if(parser.getName().equals("id")) {
							item.setTicketID(Integer.valueOf(parser.nextText()));
						} else if(parser.getName().equals("updated_on")) {
							String tmp   =  parser.nextText();
							String date  =  tmp.split("T")[0];
							String year  =  date.split("-")[0];
							String month =  date.split("-")[1];
							String day   =  date.split("-")[2];
							String times =  tmp.split("T")[1].split("\\+")[0];
							String hourOfDay = times.split(":")[0];
							String minute    = times.split(":")[1];
							String second    = times.split(":")[2];

							item.setUpdateDate(Integer.valueOf(year),
									Integer.valueOf(month),
									Integer.valueOf(day),
									Integer.valueOf(hourOfDay),
									Integer.valueOf(minute),
									Integer.valueOf(second));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if(parser.getName().equals("issue")) {
						if( result == null ) {
							result = new ArrayList<TicketItem>();
						}
						result.add(item);
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
