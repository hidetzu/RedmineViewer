package com.redmine;

public class SharedDataManager {
	private static SharedDataManager sInstance = new SharedDataManager();
	private String mApiKey;
	private Acount mAcount;
	
	private SharedDataManager() {
		mApiKey = "";
	}
	
	public static SharedDataManager getInstance() {
		return sInstance;
	}
	
	public void setAPIKey(String apiKey) {
		mApiKey = apiKey;
	}

	public String getAPIKey() {
		return mApiKey;
	}
	
	public void setAcount(Acount acount) {
		mAcount = acount;
	}
	
	public Acount getAcount() {
		return mAcount;
	}
}
