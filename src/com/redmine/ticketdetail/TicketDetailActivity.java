package com.redmine.ticketdetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.redmine.data.Acount;
import com.redmine.R;
import com.redmine.data.SharedDataManager;
import com.redmine.TicketDetailData;
import com.redmine.http.GetRequest;
import com.redmine.http.PutRequest;
import com.redmine.http.RequestURLFactory;
import com.redmine.http.RequestURLFactory.RequestType;
import com.redmine.service.TicketDetailReadThread;

public class TicketDetailActivity extends Activity
		implements OnClickListener {

	private class UpdateTickeDetailHandler extends Handler {
		private JournalListAdapter mAdapter;
		private Context mContext;

		public UpdateTickeDetailHandler(JournalListAdapter ticketAdapter, Context context) {
			mAdapter = ticketAdapter;
			mContext = context;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TicketDetailReadThread.OK:
				TicketDetailData data = ((TicketDetailData)msg.obj);
				mSubject.setText(data.getSubject());
//				mDescription.setText(Html.fromHtml(data.getDesscription()));	
				mDescription.setText(data.getDesscription());	
				
				Iterator<Journal> i = data.getJournalList().iterator();
				while(i.hasNext()) {
					mAdapter.addList(i.next());
				}
				mAdapter.notifyDataSetChanged();

				break;
			case TicketDetailReadThread.NG:
				Toast toast = Toast.makeText(mContext, "サーバエラーが発生しました", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			default:
				break;
			}
		}
	}
	
	
	private TextView mSubject;
	private TextView mDescription;
	private ArrayList<Journal> mJournals    = null;
	private JournalListAdapter mListAdapter = null;
	private ListView mListView;
	private int mTicketId = 0;
	private String mApikey;
	private Acount mAcount;
	private UpdateTickeDetailHandler mHandler;
	private SharedDataManager mSharedDataManager = SharedDataManager.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_detail);

		
		mSubject = (TextView)findViewById(R.id.subject);
		mDescription = (TextView)findViewById(R.id.description);
		mListView = (ListView)findViewById(R.id.journals);
		((Button) findViewById(R.id.postButton)).setOnClickListener(this);

		Intent intent = getIntent();
		mApikey = mSharedDataManager.getAPIKey();
		Integer value = (Integer)intent.getIntExtra("TICKET_ID", 0);
		mTicketId = value;

    	mAcount = mSharedDataManager.getAcount();
    	
    	mJournals = new ArrayList<Journal>();
    	mListAdapter = new JournalListAdapter(this, mJournals);
    	mListView.setAdapter(mListAdapter);

    	mHandler = new UpdateTickeDetailHandler(mListAdapter, this);

		GetRequest getRequest = new GetRequest(new DefaultHttpClient(), createURL(value, mAcount.getServer(), mApikey));
		TicketDetailReadThread thread = new TicketDetailReadThread(getRequest, mHandler);
		thread.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_detail, menu);
		return true;
	}
   
    private String createURL(Integer id, String server, String key) {
    	
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put("id", Integer.toString(id));
    	params.put("key", key);
    	
    	return new RequestURLFactory()
    		.getURL(RequestType.TICKETDETAIL,
    				server,
    				params);  
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
	    //テキスト入力を受け付けるビューを作成します。
	    final EditText editView = new EditText(this);
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_info)
	        .setTitle("更新画面")
	        //setViewにてビューを設定します。
	        .setView(editView)
	        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							HttpClient httpClient = new DefaultHttpClient();
							String url = "http://redmine.hidenet.mydns.jp:10080/redmine/issues/"
									+ Integer.toString(mTicketId)+".xml";
							HashMap<String, String> header = new HashMap<String, String>();
							header.put("Content-Type", "application/xml");
							header.put("X-Redmine-API-Key", mApikey);
							String inputText = editView.getText().toString();
							String str = "<?xml version=1.0 encoding=UTF-8 ?><issue><notes>"+ inputText + "</notes></issue>";
							
							PutRequest putRequest = new PutRequest(httpClient, url, header, str);
							putRequest.execute();
							
						}
	            	});
	            	thread.start();
	            }
	        })
	        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            }
	        })
	        .show();    //テキスト入力を受け付けるビューを作成します。
	}    
}
