package com.redmine.service;

import com.redmine.http.GetRequest;

import android.os.Handler;

public class ProjectListThread extends Thread{
	private Handler     mHandler;
	private GetRequest  mGet;

	public ProjectListThread(GetRequest get, Handler handler) {
		this.mGet = get;
		this.mHandler = handler;
	}
	
	public void run() {
		String result = this.mGet.execute();
		if(result != null ) {
		} else {
		}

	}
}
