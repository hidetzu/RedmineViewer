package com.redmine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;
import android.os.Message;

import com.redmine.http.Request;
import com.redmine.ticketlist.TicketItem;
import com.redmine.ticketlist.TicketItemComparator;
import com.redmine.ticketlist.TicketListPaserFactory;

public class TicketListServiceImplimention {
	private Handler mHandler;
	private Request mRequest;
	
	public TicketListServiceImplimention(Handler handler, Request request) {
		mHandler = handler;
		mRequest = request;
	}

	public void execute() {
		String result = this.mRequest.execute();
		Message msg = null;
		if(result != null ) {
			ArrayList<TicketItem> list;
			try {
				list = parseXml(result);
				Collections.sort(list, new TicketItemComparator());
				msg = mHandler.obtainMessage(TicketListService.TICKETLIST_OK);
				msg.obj = list;
			} catch (XmlPullParserException e) {
				msg = mHandler.obtainMessage(TicketListService.TICKETLIST_NG);
			} catch (IOException e) {
				msg = mHandler.obtainMessage(TicketListService.TICKETLIST_NG);
			}
		} else {
			msg = mHandler.obtainMessage(TicketListService.TICKETLIST_NG);
		}
		mHandler.sendMessage(msg);
	}
	
	private ArrayList<TicketItem> parseXml(String dataBody) throws XmlPullParserException, IOException {
		TicketListPaserFactory factory = GTicketListPaserFactory.getFactory();
		return factory.newInstance().parse(dataBody);
	}
}
