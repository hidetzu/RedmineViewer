package com.redmine.ui;

import java.util.*;

import com.redmine.R;
import com.redmine.ticketlist.TicketItem;
import com.redmine.util.StringHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TicketListAdapter extends ArrayAdapter<TicketItem> {
	private LayoutInflater mInflater;
	private TextView mSubject;
	private TextView mUpdate;
	private ArrayList<TicketItem> mTicketList;
	
	
	public TicketListAdapter(Context context, ArrayList<TicketItem> ticketList) {
		super(context, 0, ticketList);
		mTicketList = ticketList;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void addList(TicketItem item) {
		mTicketList.add(item);
	}
	
	 //一行ごとのビューを作成する。
	 public View getView(int position, View convertView,ViewGroup parent){
		 View view = convertView;
		 if(convertView == null){
			 view = mInflater.inflate(R.layout.ticketlayout, null);
		 }
		 
		 TicketItem item = this.getItem(position);
		 if( item != null ) {
			 String subject = item.getSubject().toString();
			 mSubject = (TextView)view.findViewById(R.id.title);
			 mSubject.setText(subject);
			 
			 Calendar calendar = item.getUpdateDate();
			 String update = StringHelper.convertToDateString(calendar);
 			 mUpdate = (TextView)view.findViewById(R.id.update);
			 mUpdate.setText(update);
		 }
		 
		 // アニメーションをロードする
		 Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.motion);
		 // ListViewのアイテム要素にロードしたアニメーションを実行する
		 view.startAnimation(anim);
		 return view;
	 }
}
