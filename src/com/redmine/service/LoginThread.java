package com.redmine.service;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Handler;
import android.os.Message;

import com.redmine.http.HttpClientFactory;
import com.redmine.http.Request;

public class LoginThread extends Thread {
	private Handler mHandler;
	private Request mPost;
	private Request  mGet;

	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_ACOUNT_ERROR = 1;
	public static final int LOGIN_SERVER_ERROR = 2;

	public LoginThread(Handler handler, Request post, Request get) {
		mHandler = handler;
		mPost 	 = post;
		mGet 	 = get;
	}

	public void run() {
		String ret = null;
		ret = mPost.execute();
		if( ret != null ) {
			ret = mGet.execute();
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
}
