package com.redmine.dialog;

import android.app.*;
import android.os.Bundle;;

public class ProgressDialogFragment extends DialogFragment {
	private static ProgressDialog mProgressDialog = null;
	
	public static ProgressDialogFragment getInstance(String title, String message) {
		ProgressDialogFragment instance = new ProgressDialogFragment();
		
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("message", message);
		
		instance.setArguments(bundle);
		
		return instance;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(mProgressDialog != null)
			return mProgressDialog;
		
		String title = getArguments().getString("title");
		String message = getArguments().getString("message");
		
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		
		setCancelable(false);
		
		return mProgressDialog;
	}

	@Override
	public Dialog getDialog() {
		return mProgressDialog;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mProgressDialog = null;
	}
}
