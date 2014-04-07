package com.redmine.ui;


import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.redmine.R;
import com.redmine.data.Acount;
import com.redmine.database.AcountDatabase;
import com.redmine.database.AcountDatabaseHelper;
import com.redmine.dialog.AlertDialogFragment;
import com.redmine.dialog.ProgressDialogFragment;
import com.redmine.presenter.LoginPresenter;
import com.redmine.service.LoginService;

public class LoginActivity extends Activity
	implements OnClickListener, ILoginView{

	private String TAG = LoginActivity.class.getName();
	ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
	
	private EditText mServerURLEditText;
	private EditText mLoginEditText;
	private EditText mPasswordEditText;
	private AcountDatabase mDBHelper 
						= AcountDatabaseHelper.getInstance(this);
	
	
	private ProgressDialogFragment mProgressDialog;
	private LoginPresenter mAcountPresenter;
	
	private static class LoginResponseHandler extends Handler {
		private final WeakReference<LoginActivity> mRefLoginActivity;
		private LoginPresenter mAcountPresenter;

		public LoginResponseHandler(LoginPresenter acountPresenter, LoginActivity activity) {
			mAcountPresenter = acountPresenter;
			mRefLoginActivity = new WeakReference<LoginActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			LoginActivity activity = mRefLoginActivity.get();
			if(activity == null)
				return;
			
			activity.hidenDlg();
			switch (msg.what) {
			case LoginService.LOGIN_SUCCESS:
				String apiKey = (String)msg.obj;
				mAcountPresenter.saveAcount();
				mAcountPresenter.saveAPIKey(apiKey);
				mAcountPresenter.moveToNextView();
				break;
			case LoginService.LOGIN_ACOUNT_ERROR:
				mAcountPresenter.showErrDlg("アクセスエラー", "ログイン名もしくはパスワード名が間違っています");
				break;
			case LoginService.LOGIN_SERVER_ERROR:
				mAcountPresenter.showErrDlg("アクセスエラー", "サーバーにアクセスできません。");
				break;
			default:
				break;
			}
		}
	}
	
	private boolean mLoginMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acount_registry);

		((Button) findViewById(R.id.ticketListButton)).setOnClickListener(this);
		mServerURLEditText = (EditText)findViewById(R.id.serverEditText);
		mLoginEditText = (EditText)findViewById(R.id.userEditText);
		mPasswordEditText = (EditText)findViewById(R.id.passwordEditText);
		
		mAcountPresenter = new LoginPresenter(this, mDBHelper);
		Intent intent = getIntent();
		mLoginMode = intent.getBooleanExtra("LoginMode", false);
	}

	
	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");

		super.onResume();
		mAcountPresenter.loadAcount();
		if(mLoginMode) {
			login();
			mLoginMode = false;
		}
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");

		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
 
		return true;
	}

	@Override
	public void onClick(View v) {
		login();
	}

	
	private void login() {
		showDlg();
		Acount acount = mAcountPresenter.getAcount();
		LoginResponseHandler handler = new LoginResponseHandler(mAcountPresenter, this);

		mExecutorService.execute(new LoginService(handler, acount));
	}

	private void showDlg() {
		mProgressDialog = ProgressDialogFragment.getInstance("ネットワークに接続中", "ログイン情報取得中");
		mProgressDialog.show(getFragmentManager(), "progress");
	}
	
	private void hidenDlg() {
		if( null !=mProgressDialog &&
				null != mProgressDialog.getDialog() ) {
			mProgressDialog.getDialog().dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		mAcountPresenter.saveAcount();
		mAcountPresenter.saveAcountDB();
		mExecutorService.shutdown();
	}

	@Override
	public void setServerURL(String server) {
		mServerURLEditText.setText(server);
	}

	@Override
	public void setName(String name) {
		mLoginEditText.setText(name);
	}

	@Override
	public void setPassword(String password) {
		mPasswordEditText.setText(password);
	}

	@Override
	public String getServerURL() {
		return mServerURLEditText.getText().toString();
	}
	
	@Override
	public String getName() {
		return mLoginEditText.getText().toString();
	}

	@Override
	public String getPassword() {
		return mPasswordEditText.getText().toString();
	}

	@Override
	public void moveToTicketListView() {
		Intent nextIntent = new Intent();
		nextIntent.setClass(getApplicationContext(), com.redmine.ui.MenuActivity.class);

		startActivity(nextIntent);
	}

	@Override
	public void showErrDlg(String title, String msg) {
		AlertDialogFragment  fm = AlertDialogFragment.getInstance(title, msg);
		fm.show(getFragmentManager(), "alert");
	}
}
