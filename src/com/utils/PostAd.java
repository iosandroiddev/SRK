package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.sabrentkaro.InternalApp;

public class PostAd extends AsyncTask<Void, Integer, String> {

	private String imagePath;
	private String productId;

	IPostAd mUpload;
	private Context mContext;
	InternalApp mApplication;
	private String body;

	public interface IPostAd {
		public void onAdPosted(String message);
	}

	public PostAd(IPostAd mUpoad, String body, Context mContext) {
		this.body = body;
		this.mContext = mContext;
		mApplication = (InternalApp) mContext.getApplicationContext();
		mUpload = mUpoad;
	}

	public void startExexcution() {
		if (!(TextUtils.isEmpty(body))) {
			execute();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
	}

	@Override
	protected String doInBackground(Void... params) {
		return postAd();
	}

	@SuppressWarnings("deprecation")
	private String postAd() {
		String mOutput = "";
		URL url;
		try {
			url = new URL(ApiUtils.POSTANAD);
			HttpURLConnection httpURLConnection;
			try {
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setRequestProperty("Content-Type",
						"application/json; charset=UTF-8");
				httpURLConnection.setRequestProperty("Accept",
						"application/json");
				httpURLConnection.setReadTimeout(100000);
				OutputStreamWriter writer;
				writer = new OutputStreamWriter(
						httpURLConnection.getOutputStream(), "UTF-8");
				writer.write(body);
				writer.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpURLConnection.getInputStream()));
				String line;
				StringBuilder output = new StringBuilder();
				while ((line = br.readLine()) != null) {
					output = output.append(line + "\n");
				}

				mOutput = output.toString();

				httpURLConnection.disconnect();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return mOutput;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (mUpload != null)
			mUpload.onAdPosted(result);
	}
}
