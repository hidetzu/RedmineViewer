package com.redmine;

import java.io.Serializable;

public class Acount implements Serializable{
	private static final long serialVersionUID = 183951749490571449L;

	private String mServerUrl;
	private String mUsername;
	private String mPassword;
	
	public Acount(String url, String username, String password) {
		mServerUrl = url;
		mUsername  = username;
		mPassword  = password;
	}
	
	public String getUsername() {
		return mUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	public String getServer() {
		return mServerUrl;
	}
}
