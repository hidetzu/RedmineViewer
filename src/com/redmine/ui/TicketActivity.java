package com.redmine.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.redmine.R;
import com.redmine.data.Acount;
import com.redmine.data.SharedDataManager;
import com.redmine.http.RequestURLFactory;
import com.redmine.http.RequestURLFactory.RequestType;
import com.redmine.service.TicketListService;
import com.redmine.ticketlist.TicketItem;

public class TicketActivity extends Activity {	
	
	

	private static class UpdateTickeListHandler extends Handler {
		private TicketListAdapter mAdapter;
		private Context mContext;

		public UpdateTickeListHandler(TicketListAdapter ticketAdapter, Context context) {
			mAdapter = ticketAdapter;
			mContext = context;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TicketListService.TICKETLIST_OK:
				ArrayList<TicketItem> result = ((ArrayList<TicketItem>)msg.obj);
				Iterator<TicketItem> i = result.iterator();
				while(i.hasNext()) {
					mAdapter.addList(i.next());
				}
				mAdapter.notifyDataSetChanged();
				break;
			case TicketListService.TICKETLIST_NG:
				Toast toast = Toast.makeText(mContext, "サーバエラーが発生しました", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			default:
				break;
			}
		}

	}
	
	private ArrayList<TicketItem> mTicketList = null;
	private TicketListAdapter mTicketAdapter = null;
	private ListView mTicketListView;
	private String mApikey;
	private Acount mAcount;
	private UpdateTickeListHandler mHandler;
	private SharedDataManager mSharedDataManager = SharedDataManager.getInstance();
	ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket);

		mApikey = mSharedDataManager.getAPIKey();
    	mAcount = mSharedDataManager.getAcount();
		
		mTicketListView = (ListView)findViewById(R.id.ticketList);
		mTicketListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		        @Override
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {		              
		              TicketItem item = mTicketList.get(position);
		              Log.v("tag", String.format("onItemClick: subject=%s id=%d", item.getSubject(), item.getTicketID()));
    		        	moveToNextView(mApikey, item.getTicketID(), mAcount);
		              }
		        });	

    	
    	mTicketList = new ArrayList<TicketItem>();
    	mTicketAdapter = new TicketListAdapter(this, mTicketList);	
    	mTicketListView.setAdapter(mTicketAdapter);	
    	
    	mHandler = new UpdateTickeListHandler(mTicketAdapter, this);

    	mExecutorService.execute(new TicketListService(mHandler, mAcount, mApikey));
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket, menu);
		return true;
	}
    
    private String createURL(String server, String apikey) {	
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("key", apikey);
    	
    	return new RequestURLFactory()
    		.getURL(RequestType.TICKETLIST,
    				server,
    				params);    	
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mExecutorService.shutdown();
	}

	private void moveToNextView(String apiKey, int ticketId, Acount acount ) {
		Intent intent = new Intent();
		intent.putExtra("apikey", apiKey);
		intent.putExtra("TICKET_ID", ticketId);
		intent.putExtra("acount", acount);
		intent.setClass(getApplicationContext(), com.redmine.ticketdetail.TicketDetailActivity.class);
		startActivity(intent);
		
	}
}
