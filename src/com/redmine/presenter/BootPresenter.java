package com.redmine.presenter;

import com.redmine.data.Acount;
import com.redmine.data.SharedDataManager;
import com.redmine.database.AcountDatabase;
import com.redmine.ui.IBootView;

public class BootPresenter {
	private IBootView mBootView;
	private AcountDatabase mDBHelper;
	private SharedDataManager mSharedDataManager
						= SharedDataManager.getInstance();
	

	public BootPresenter(IBootView view, AcountDatabase db) {
		mBootView = view;
		mDBHelper = db;
	}
	
	public void moveToLoginView() {
		Acount acount = getAcount();
		mSharedDataManager.setAcount(acount);
		mBootView.moveToLoginView((acount != null) ? true: false);
	}

	private Acount getAcount() {
		return mDBHelper.getAcount(0);
	}

}
