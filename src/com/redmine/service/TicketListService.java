package com.redmine.service;

import java.util.HashMap;

import org.apache.http.client.HttpClient;

import android.os.Handler;

import com.redmine.data.Acount;
import com.redmine.http.GetRequest;
import com.redmine.http.HttpClientFactory;
import com.redmine.http.Request;
import com.redmine.http.RequestURLFactory;
import com.redmine.http.RequestURLFactory.RequestType;

public class TicketListService implements Runnable {
	public static final int TICKETLIST_OK = 1;
	public static final int TICKETLIST_NG = 2;
	
	private Request     mGet;
	private Handler     mHandler;
	
	public TicketListService(Handler handler, Acount acount, String key) {
		mHandler = handler;
		mGet = creatGetRequest(acount.getServer(), key);
	}
	
    private String createURL(String server, String apikey) {	
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("key", apikey);
    	
    	return new RequestURLFactory()
    		.getURL(RequestType.TICKETLIST,
    				server,
    				params);    	
    }
	
	private Request creatGetRequest(String server, String key) {
		String url = createURL(server, key);
		HttpClient httpClient = HttpClientFactory.createHttpClient();
		return new GetRequest(httpClient, url);
	}

	@Override
	public void run() {
		new TicketListServiceImplimention(mHandler, mGet).execute();
	}
}
