package com.utils;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.facebook.Facebook;
import com.facebook.FacebookCallbackListner;
import com.facebook.FacebookUtilityWithCallback;
import com.sabrentkaro.R;

public class GetSocialDetails {
	private String twitter_consumer_key = "NYdNJQko7ojM67orTJldAA";// "nXz7zT5MgUpyVDZI3NbgGw";
	private String twitter_secret_key = "ahNV4LmWnAKKcUpaGJjVIPjJY36jFb4GnMx8WHrw";// "Nu9Yio2qQCZM7Fm6JI7OalkUflEf7EpNd3BXgL7v7D8";
	private FragmentActivity mactvity;

	public GetSocialDetails(FragmentActivity mactvity) {
		this.mactvity = mactvity;
		mLoginCallBack = (IFbLoginCallBack) mactvity;
	}

	public interface IFbLoginCallBack {
		public void onFbLoginSucsess(String accessToken, JSONObject mUserInfo);
	}

	IFbLoginCallBack mLoginCallBack;

	FacebookUtilityWithCallback mCallback;

	/**
	 * Start FaceBook
	 */
	public void getAndPostFaceBookUserDetails() {

		mCallback = new FacebookUtilityWithCallback(mactvity, "969051026512281");

		mCallback.facebooklogin(mCallFb);
		
	}

	/**
	 * Login Call Back
	 */
	public FacebookCallbackListner mCallFb = new FacebookCallbackListner() {

		@Override
		public void success(boolean success, Facebook mFaceBook) {
			if (success) {
				GetFbData mLocalTask = new GetFbData();
				mLocalTask.execute(mFaceBook);
			}
		}

		@Override
		public void response(String response) {

		}
	};

	/**
	 * ' Logout Once you Took Information
	 */
	public FacebookCallbackListner mLOgout = new FacebookCallbackListner() {

		@Override
		public void success(boolean success, Facebook mFaceBook) {

		}

		@Override
		public void response(String response) {

		}
	};

	/**
	 * Load FaceBook User Profile Data
	 * 
	 * @author hb
	 * 
	 */
	private class GetFbData extends AsyncTask<Facebook, Void, String> {

		ProgressDialog pd;
		Facebook mFb;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = ProgressDialog.show(mactvity, "Please wait",
					"Fecthing Information...");
			pd.setIcon(R.drawable.ic_launcher);
			pd.show();
		}

		@Override
		protected String doInBackground(Facebook... params) {
			String str = "";
			try {
				mFb = params[0];
				str = mFb.request("me");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			try {
				JSONObject mjsoObject = new JSONObject(result);
				String id = mjsoObject.getString("id");
				String accesstoken = mFb.getAccessToken();
				if (mLoginCallBack != null)
					mLoginCallBack.onFbLoginSucsess(accesstoken, mjsoObject);
			} catch (JSONException e) {
				e.printStackTrace();
				// DropDownaLERT.showAlertMessage(mactvity,
				// "Unable to login with Facebook. Please try again.");
			}
		}
	}
	
	
	public void logoutFb() {
		mCallback.facebookLogout(mLOgout);
	}

	public static final int FBCode = 1, TwitterCode = 3, Gplus = 5;

}
