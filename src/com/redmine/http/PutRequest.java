package com.redmine.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class PutRequest implements Request{
	private String mURL;
	private HttpClient mHttpClient;
	private HashMap<String, String> mHeader;
	private String mBody;
	
	public PutRequest(HttpClient httpClient, String url,
			HashMap<String, String> header,
			String body) {
		mHttpClient = httpClient;
		mURL = url;
		mHeader = header;
		mBody = body;
	}

	public String execute() {
		String ret = null;

		//タイムアウト設定
		HttpParams httpParams = mHttpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 500*1000);
		HttpConnectionParams.setSoTimeout(httpParams, 500*1000);

		HttpPut putRequest = new HttpPut(this.mURL);
		HttpResponse response;
		try {
			// ヘッダの生成
			for(Map.Entry<String, String> e : mHeader.entrySet() ) {
				putRequest.addHeader(e.getKey(), e.getValue());
			}

			//Bodyの生成
			ByteArrayEntity requestEntity = new ByteArrayEntity(mBody.getBytes());
			putRequest.setEntity(requestEntity);
			
			response = mHttpClient.execute(putRequest);
			Log.d("TicketDetail", Integer.toString(response.getStatusLine().getStatusCode()));

		} catch (ClientProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} finally {
			mHttpClient.getConnectionManager().shutdown();
		}
    	
		return ret;
	}
}
