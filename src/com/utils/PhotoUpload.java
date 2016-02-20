package com.utils;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.jsonclasses.AndroidMultipartEntity;
import com.android.jsonclasses.AndroidMultipartEntity.ProgressListener;
import com.sabrentkaro.InternalApp;

public class PhotoUpload extends AsyncTask<Void, Integer, String> {

	private String imagePath;
	private String productId;

	IImageUpload mUpload;
	private Context mContext;
	InternalApp mApplication;

	public interface IImageUpload {
		public void onImageUpload(String message);
	}

	public PhotoUpload(IImageUpload mUpoad, String imagePath, String productId,
			Context mContext) {
		this.imagePath = imagePath;
		this.productId = productId;
		this.mContext = mContext;
		mApplication = (InternalApp) mContext.getApplicationContext();
		mUpload = mUpoad;
	}

	public void startExexcution() {
		if (!(TextUtils.isEmpty(imagePath))) {
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
		return uploadFile();
	}

	@SuppressWarnings("deprecation")
	private String uploadFile() {
		String responseString = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(ApiUtils.UPLOADADCONTENT);
		try {
			AndroidMultipartEntity entity = new AndroidMultipartEntity(
					new ProgressListener() {

						@Override
						public void transferred(long num) {
						}
					});

			File sourceFile = new File(imagePath);

			entity.addPart("file-3", new FileBody(sourceFile));
			entity.addPart(
					"Content-type",
					new StringBody(
							"multipart/form-data; boundary=ARCFormBoundarymy3972j8gk6s9k9"));
			entity.addPart("adId", new StringBody(productId));
			entity.addPart("adItemId", new StringBody(productId));
			httppost.setEntity(entity);

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				try {
					responseString = EntityUtils.toString(r_entity);
					JSONArray jsonArray = new JSONArray(responseString);
					if (mApplication != null)
						mApplication.setPhotoUpload(jsonArray);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						Integer adItemId = c.getInt("AdItemId");
						if (adItemId != null && c.getBoolean("IsActive")) {
							responseString = "Image uploaded successfully.";
						} else {
							responseString = "Image have not been uploaded, Please try again.";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				responseString = "Error occurred! Http Status Code: "
						+ statusCode;
			}

		} catch (ClientProtocolException e) {
			responseString = e.toString();
		} catch (IOException e) {
			responseString = e.toString();
		}

		return responseString;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (mUpload != null)
			mUpload.onImageUpload(result);
	}
}
