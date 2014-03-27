package com.redmine.http;

import java.util.*;

public class RequestURLFactory {
	
	public enum RequestType{
		USERLIST,
		TICKETLIST,
		TICKETDETAIL,
	}
	
	private static final HashMap<RequestType, String> map =
			new HashMap<RequestURLFactory.RequestType, String>();
	static {
		map.put(RequestType.USERLIST, "/users.xml");
		map.put(RequestType.TICKETLIST, "/issues.xml");
		map.put(RequestType.TICKETDETAIL, "/issues/");
	}
	
	public RequestURLFactory() {
	}
	
	public String getURL(RequestType type, String topUrl,
					HashMap<String, String> params) {

		String result = topUrl;

		String key = params.get("key");
		String id = null;
		
		switch (type) {
		case USERLIST:
		case TICKETLIST:
			result += map.get(type) + "?key=" + key;
			break;
		case TICKETDETAIL:
			id  = params.get("id");
			result += map.get(type) + id + ".xml" + "?key=" + key
			       + "&include=journals";
			break;
		default:
			break;
		}
		
		return result;
	}
}
