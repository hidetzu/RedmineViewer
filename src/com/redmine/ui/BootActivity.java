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
		
		mBootPresenter = new BootPresenter(this, AcountDatabaseHelper.getInstance(this));
		createDesktopShorCut(this);
		mBootPresenter.moveToLoginView();
	}
	
	private void createDesktopShorCut(Context context) {
		Intent targetIntent = new Intent(Intent.ACTION_MAIN);
		targetIntent.setClassName(context, "com.redmine.ui.BootActivity");
		
		Intent i = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		i.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
		
		//2. アイコン画像とタイトルを設定
		Parcelable icon = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher);
		i.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		i.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));

		//3. 1～2で作成したIntentをBroadcastしてOSに依頼
		context.sendBroadcast(i);
	}
	
	@Override
	public void moveToLoginView(boolean autoLogin) {
		Intent intent = new Intent();
		intent.putExtra("LoginMode", autoLogin);
		intent.setClass(getApplicationContext(), com.redmine.ui.LoginActivity.class);
		startActivity(intent);
	}
}
