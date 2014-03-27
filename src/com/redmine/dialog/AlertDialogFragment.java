package com.redmine.dialog;

import android.app.*;
import android.os.*;


public class AlertDialogFragment extends DialogFragment {
	private static AlertDialog mAlertDialog = null;
	
	public static AlertDialogFragment getInstance(String title, String message) {
		AlertDialogFragment instance = new AlertDialogFragment();
		
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("message", message);
		
		instance.setArguments(bundle);
		
		return instance;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(mAlertDialog != null)
			return mAlertDialog;
		
		String title = getArguments().getString("title");
		String message = getArguments().getString("message");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		
		mAlertDialog = builder.create();
		return mAlertDialog;
	}

	@Override
	public Dialog getDialog() {
		return mAlertDialog;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mAlertDialog = null;
	}
}
