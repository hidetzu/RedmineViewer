package com.redmine.presenter;

import com.redmine.data.*;
import com.redmine.database.*;
import com.redmine.ui.*;

public class LoginPresenter {
	private AcountDatabase mDBHelper;
	private ILoginView mLoginView;
	private SharedDataManager mSharedDataManager
			= SharedDataManager.getInstance();
	
	public LoginPresenter(ILoginView view, AcountDatabase db) {
		mLoginView = view;
		mDBHelper=db;
	}
	
	public void loadAcount() {
		Acount tmp = mSharedDataManager.getAcount();
		if(tmp != null) {
			mLoginView.setServerURL(tmp.getServer());
			mLoginView.setName(tmp.getUsername());
			mLoginView.setPassword(tmp.getPassword());
		}
	}
	
	public void saveAPIKey(String apiKey) {
		mSharedDataManager.setAPIKey(apiKey);
	}
	
	public void saveAcount() {
		String server   = mLoginView.getServerURL();
		String username = mLoginView.getName();
		String password = mLoginView.getPassword();

		Acount acount = new Acount(server, username, password);
		mSharedDataManager.setAcount(acount);
	}
	
	public void saveAcountDB() {
		Acount acount = mSharedDataManager.getAcount();
		if( acount != null )
			mDBHelper.setAcount(0, acount.getServer(), acount.getUsername(), acount.getPassword());
	}
	
	
	public Acount getAcount() {
		Acount acount = new Acount(
				mLoginView.getServerURL(),
				mLoginView.getName(),
				mLoginView.getPassword());
		return acount;
	}
	
	public void moveToNextView() {
		mLoginView.moveToTicketListView();
	}

	public void showErrDlg(String title, String msg) {
		mLoginView.showErrDlg(title, msg);
	}
}
