package com.redmine.ticketdetail;

import java.util.*;

import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.redmine.*;
import com.redmine.util.StringHelper;

public class JournalListAdapter  extends ArrayAdapter<Journal> {
	private LayoutInflater mInflater;
	private ArrayList<Journal> mList;

	 public JournalListAdapter(Context context,ArrayList<Journal> mJournals) {
			super(context, 0, mJournals);
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mList = mJournals;
	}
	 
	 public void addList(Journal item) {
		 mList.add(item);
	 }
	 
	//一行ごとのビューを作成する。
	 public View getView(int position, View convertView,ViewGroup parent){
		 View view = convertView;
		 if(convertView == null){
			 view = mInflater.inflate(R.layout.journal, null);
		 }
		 
		 Journal item = this.getItem(position);
		 if( item != null ) {
			 ((TextView)view.findViewById(R.id.user)).setText(item.getUser());
//			 ((TextView)view.findViewById(R.id.notes)).setText(Html.fromHtml(item.getNotes()));
			 ((TextView)view.findViewById(R.id.notes)).setText(item.getNotes());

			 TextView updateText = ((TextView)view.findViewById(R.id.updateDate));
			 Calendar calendar = item.getUpdateDate();
			 String update = StringHelper.convertToDateString(calendar);
			 updateText.setText(update);
		 }
		 
		 return view;
	 }
}
