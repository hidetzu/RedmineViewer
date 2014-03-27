package com.redmine.ui;

import com.redmine.R;
import com.redmine.R.layout;
import com.redmine.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.widget.TextView;

public class AboutApplicationActivity extends Activity {
	private TextView mVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_application);
		mVersion = (TextView)findViewById(R.id.versionText);

		String versionName = "";
		PackageManager packageManager = this.getPackageManager();

		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			versionName = "不明";
		}

		mVersion.setText(versionName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_application, menu);
		return true;
	}

}
