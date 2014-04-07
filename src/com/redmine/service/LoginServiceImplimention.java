package com.redmine.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.redmine.http.Request;

import android.os.Handler;
import android.os.Message;

public class LoginServiceImplimention {
	private Handler mHandler;
	private Request mPost;
	private Request mGet;
	
	public LoginServiceImplimention(Handler handler, Request post, Request get) {
			mHandler = handler;
			mPost = post;
			mGet  = get;
	}
	
	public void execute() {
		String ret = null;
		ret = mPost.execute();
		if( ret != null ) {
			ret = mGet.execute();
			if(ret == null ) {
				Message msg = mHandler.obtainMessage(LoginService.LOGIN_SERVER_ERROR);
				mHandler.sendMessage(msg);
			} else {
				Document document = Jsoup.parse(ret);
				Elements api_key = document.select("pre[class=autoscroll]");
				if( api_key.text() == "") {
					Message msg = mHandler.obtainMessage(LoginService.LOGIN_ACOUNT_ERROR);
					mHandler.sendMessage(msg);
				} else {
					Message msg = mHandler.obtainMessage(LoginService.LOGIN_SUCCESS);
					msg.obj = api_key.text();
					mHandler.sendMessage(msg);
				}
			}
		} else {
			Message msg = mHandler.obtainMessage(LoginService.LOGIN_SERVER_ERROR);
			mHandler.sendMessage(msg);
		}
	}
}
