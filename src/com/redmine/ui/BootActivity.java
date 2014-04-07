package com.redmine.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.redmine.R;
import com.redmine.database.AcountDatabaseHelper;
import com.redmine.presenter.BootPresenter;

public class BootActivity extends Activity
	implements IBootView {
	
	private BootPresenter mBootPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createDesktopShortCut(this);
		
		mBootPresenter = new BootPresenter(this, AcountDatabaseHelper.getInstance(this));
		mBootPresenter.moveToLoginView();
	}

	private void createDesktopShortCut(Context context) {
		Intent shortcutIntent = new Intent();
		shortcutIntent.setClassName("com.redmine.ui", getClass().getName());

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		sendBroadcast(intent);
	}
	
	@Override
	public void moveToLoginView(boolean autoLogin) {
		Intent intent = new Intent();
		intent.putExtra("LoginMode", autoLogin);
		intent.setClass(getApplicationContext(), com.redmine.ui.LoginActivity.class);
		startActivity(intent);
	}
}
