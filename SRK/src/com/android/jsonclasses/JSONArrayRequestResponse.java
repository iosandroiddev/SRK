package com.android.jsonclasses;

import java.io.File;
import java.util.HashMap;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.examples.toolbox.MyVolley;
import com.android.volley.toolbox.JsonArrayRequest;

@SuppressWarnings("unused")
public class JSONArrayRequestResponse {

	public JSONArrayRequestResponse(Context cntx) {
		mContext = cntx;
	}

	private final Context mContext;
	private int reqCode;
	private IArrayParseListener listner;

	private boolean isFile = false;
	private String file_path = "", key = "";
	private HashMap<String, File> mAttachFileList = new HashMap<String, File>();

	public boolean isPostMethod = false;

	public void getResponse(String url, final int requestCode,
			IArrayParseListener mParseListener) {
		getResponse(url, requestCode, mParseListener, null);
	}

	public void getResponse(String url, final int requestCode,
			IArrayParseListener mParseListener, Bundle params) {
		this.listner = mParseListener;
		this.reqCode = requestCode;

		Response.Listener<JSONArray> sListener = new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				if (listner != null) {
					listner.SuccessResponse(response, reqCode);
				}
			}
		};

		Response.ErrorListener eListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (listner != null) {
					listner.ErrorResponse(error, reqCode);
				}
			}
		};

		if (!isFile) {
			if (isPostMethod) {
				JsonArrayRequest jsObjRequest = new JsonArrayRequest(
						Request.Method.POST, url, sListener, eListener);
				MyVolley.getRequestQueue().add(jsObjRequest);
			} else {
				JsonArrayRequest jsObjRequest = new JsonArrayRequest(
						Request.Method.GET, url, sListener, eListener);
				MyVolley.getRequestQueue().add(jsObjRequest);
			}
		} else {
			// if (file_path != null) {
			// if (file_path != null) {
			// String[] mary = file_path.split(",");
			// if (mary.length > 1) {
			// File mFile = new File(mary[0]);
			// ArrayMultipartRequest multipartRequest = new
			// ArrayMultipartRequest(
			// url, eListener, sListener, key, mary, mFile,
			// params, mAttachFileList);
			//
			// multipartRequest.setAttachFileList(mAttachFileList);
			// MyVolley.getRequestQueue().add(multipartRequest);
			//
			// } else {
			// File mFile = new File(mary[0]);
			// ArrayMultipartRequest multipartRequest = new
			// ArrayMultipartRequest(
			// url, eListener, sListener, key, mFile, params,
			// mAttachFileList);
			// multipartRequest.setAttachFileList(mAttachFileList);
			// MyVolley.getRequestQueue().add(multipartRequest);
			// }
			// } else {
			// throw new NullPointerException("File path is null");
			// }
			// } else {
			// throw new NullPointerException("File path is null");
			// }
		}
	}

	/**
	 * @return the isFile
	 */
	public boolean isFile() {
		return isFile;
	}

	public void setIsPost(boolean isPost) {
		this.isFile = isPost;
	}

	/**
	 * @param isFile
	 *            the File to set
	 */
	public void setFile(String param, String path) {
		if (path != null && param != null) {
			if (path.contains("null"))
				path = path.replace("null", "");
			key = param;
			file_path = path;
			this.isFile = true;
		}
	}

	public HashMap<String, File> getAttachFileList() {
		return mAttachFileList;
	}

	public void setAttachFileList(HashMap<String, File> mAttachFileList) {
		this.isFile = true;
		this.mAttachFileList = mAttachFileList;
	}

	public void setPostMethod(boolean isPost) {
		this.isPostMethod = isPost;
	}

}