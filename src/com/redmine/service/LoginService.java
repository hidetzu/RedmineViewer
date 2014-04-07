package com.redmine.service;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.Handler;
import com.redmine.data.Acount;
import com.redmine.http.GetRequest;
import com.redmine.http.HttpClientFactory;
import com.redmine.http.PostRequest;
import com.redmine.http.Request;

public class LoginService implements Runnable {
		private Handler mHandler;
		private Request mPost;
		private Request mGet;

		public static final int LOGIN_SUCCESS = 0;
		public static final int LOGIN_ACOUNT_ERROR = 1;
		public static final int LOGIN_SERVER_ERROR = 2;
		
		public LoginService(Handler handler, Acount acount) {
			mHandler = handler;
			mPost = createPostRequest(acount);
			mGet  = creatGetRequest(acount);
		}

		@Override
		public void run() {
			new LoginServiceImplimention(mHandler, mPost, mGet).execute();
		}

		private String createURL(String action, Acount acount) {
			return acount.getServer() + action;
		}

		private Request createPostRequest(Acount acount) {
			String url = createURL("/login", acount);
			ArrayList <NameValuePair> params = new ArrayList <NameValuePair>();
			params.add( new BasicNameValuePair("username", acount.getUsername()));
			params.add( new BasicNameValuePair("password", acount.getPassword()));
			
			HttpClient httpClient = HttpClientFactory.createHttpClient();
			return new PostRequest(httpClient, url, params);
		}

		private Request creatGetRequest(Acount acount) {
			String url = createURL("/my/account", acount);
			HttpClient httpClient = HttpClientFactory.createHttpClient();
			return new GetRequest(httpClient, url);
		}
}
