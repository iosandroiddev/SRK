package com.android.jsonclasses;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.VolleyError;

public interface IArrayParseListener {
	int TIME_OUT = -1;

	// void ErrorResponse(VolleyError error, int requestCode, int errorcode);
	void ErrorResponse(VolleyError error, int requestCode);

	void SuccessResponse(JSONArray response, int requestCode);

}
