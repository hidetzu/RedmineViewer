package com.redmine.ticketlist;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class TicketListPaserImplimention implements TicketListPaser, TicketListPaserFactory {
	public ArrayList<TicketItem> parse(String dataBody) throws XmlPullParserException, IOException {
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

	@Override
	public TicketListPaser newInstance() {
		return new TicketListPaserImplimention();
	}
}