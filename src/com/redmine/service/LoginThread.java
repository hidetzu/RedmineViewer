package com.redmine.service;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Handler;
import android.os.Message;

import com.redmine.http.GetRequest;
import com.redmine.http.PostRequest;

public class LoginThread extends Thread {
	private String mServer;
	private String mUsername;
	private String mPassword;
	private Handler mHandler;

	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_ACOUNT_ERROR = 1;
	public static final int LOGIN_SERVER_ERROR = 2;

	public LoginThread(Handler handler, String server, String username, String password) {
		mHandler = handler;
		mServer = server;
		mUsername = username;
		mPassword = password;
	}

	public void run() {
		String url = createURL("/login");
		HttpClient httpClient = new DefaultHttpClient();

		ArrayList <NameValuePair> params = new ArrayList <NameValuePair>();
		params.add( new BasicNameValuePair("username", mUsername));
		params.add( new BasicNameValuePair("password", mPassword));

		String ret = null;
		ret = do_post(httpClient, url, params);
		if( ret != null ) {
			String getUrl = createURL("/my/account");
			ret = do_get(httpClient, getUrl);
			if(ret == null ) {
				Message msg = mHandler.obtainMessage(LOGIN_SERVER_ERROR);
				mHandler.sendMessage(msg);
			} else {
				Document document = Jsoup.parse(ret);
				Elements api_key = document.select("pre[class=autoscroll]");
				if( api_key.text() == "") {
					Message msg = mHandler.obtainMessage(LOGIN_ACOUNT_ERROR);
					mHandler.sendMessage(msg);
				} else {
					Message msg = mHandler.obtainMessage(LOGIN_SUCCESS);
					msg.obj = api_key.text();
					mHandler.sendMessage(msg);
				}
			}
		} else {
			Message msg = mHandler.obtainMessage(LOGIN_SERVER_ERROR);
			mHandler.sendMessage(msg);
		}
	}

	private String createURL(String action) {
		return mServer + action;
	}
	
	public String do_post(HttpClient httpClient, String url, ArrayList <NameValuePair> params) {	
		PostRequest postRequest = new PostRequest(httpClient, url, params);
		return postRequest.execute();
	}
	
	public String do_get(HttpClient httpClient, String url) {
		GetRequest getRequest = new GetRequest(httpClient, url);
		return getRequest.execute();
	}
}
