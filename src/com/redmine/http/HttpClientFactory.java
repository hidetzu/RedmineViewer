package com.redmine.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientFactory {
	private static HttpClient mHttpClient = new DefaultHttpClient();

	public static void setHttpClient(HttpClient httpClient) {
		mHttpClient = httpClient;
	}

	public static HttpClient createHttpClient() {
		return mHttpClient;
	}
}
