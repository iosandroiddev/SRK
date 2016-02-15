package com.sabrentkaro.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.VoiceInteractor.Prompt;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.sabrentkaro.search.RentDatesActivity;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class LoginActivity extends BaseActivity {

	private EditText mEditEmail, mEditPassword;
	private TextView mbtnLogin, mbtnRegister;
	private String selectedProductAdId, mPrice, mProductDescription, mQuantity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_login);
		getDetails();
		loadLayoutReferences();
		hideSoftKeyboard();
	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
				selectedProductAdId = mBundle.getString("selectedAdId");
				mPrice = mBundle.getString("productPrice");
				mProductDescription = mBundle.getString("productDescription");
				mQuantity = mBundle.getString("quantity");
			}
		}
	}

	private void loadLayoutReferences() {
		mEditEmail = (EditText) findViewById(R.id.emailId);
		mEditPassword = (EditText) findViewById(R.id.password);
		mbtnLogin = (TextView) findViewById(R.id.btnLoginUser);
		mbtnRegister = (TextView) findViewById(R.id.btnRegister);
		StaticUtils.setEditTextHintFont(mEditEmail, this);
		StaticUtils.setEditTextHintFont(mEditPassword, this);

		mbtnLogin.setOnClickListener(this);
		mbtnRegister.setOnClickListener(this);

		// mEditEmail.setText("harathithippaluru@sabrentkaro.com");
		// mEditPassword.setText("harathi@raj1");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnLoginUser:
			btnLoginUserClicked();
			break;
		case R.id.btnRegister:
			btnRegisterClicked();
			break;
		default:
			break;
		}
	}

	private void btnRegisterClicked() {
		Intent mIntent = new Intent(this, RegisterActivity.class);
		startActivity(mIntent);
	}

	private void btnLoginUserClicked() {
		hideSoftKeyboard();
		if (TextUtils.isEmpty(mEditEmail.getText().toString())) {
			showToast("Please Enter Email Id");
		} else if (!(StaticUtils.isValidEmail(mEditEmail.getText().toString()))) {
			showToast("Please Enter Valid Email Id");
		} else if (TextUtils.isEmpty(mEditPassword.getText().toString())) {
			showToast("Please Enter Password");
		} else {
			initLoginApi();
		}
	}

	private void initLoginApi() {
		showProgressLayout();
		JSONObject mLoginParams = new JSONObject();
		try {
			mLoginParams.put("Email", mEditEmail.getText().toString());
			mLoginParams.put("IsClaims", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mCurrentPassword = new JSONObject();
		try {
			mCurrentPassword.put("CurrentPassword", mEditPassword.getText()
					.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mPassword = new JSONObject();
		try {
			mPassword.put("Password", mCurrentPassword);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mVerifications = new JSONObject();
		try {
			mVerifications.put("Verifications", mPassword);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mLogin = new JSONObject();
		try {
			mLogin.put("Login", mLoginParams);
			mLogin.put("Verifications", mPassword);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mParams = new JSONObject();
		try {
			mParams.put("User", mLogin);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.POSTUSERINFORMATION, mParams,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						responseForLoginApi(response);
						hideProgressLayout();
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
					}
				});

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);

	}

	private void responseForLoginApi(JSONObject response) {
		String userName = "";
		if (response != null) {
			JSONObject mObjUser = response.optJSONObject("User");
			if (mObjUser != null) {
				String authenticationHeader = mObjUser.optJSONObject(
						"UserTransactions").optString("AuthenticationHeader");
				JSONObject mObjUserProfile = mObjUser
						.optJSONObject("UserProfile");
				if (mObjUserProfile != null) {
					userName = mObjUserProfile.optString("Name");
				}
				JSONObject mObjUserAdress = (JSONObject) mObjUser.optJSONArray(
						"Addresses").opt(0);
				if (mObjUserAdress != null) {
					try {
						String addressLine = mObjUserAdress
								.getString("AddressLine1")
								+ " "
								+ mObjUserAdress.getString("AddressLine2");
						String city = mObjUserAdress.getString("City");
						String state = mObjUserAdress.getString("State");
						String country = mObjUserAdress.getString("Country");
						String pincode = mObjUserAdress.getString("PinCode");
						String mobileNumber = mObjUserAdress
								.getString("MobileNo");

						StorageClass.getInstance(this).setUserCity(city);

						StorageClass.getInstance(this).setUserState(state);
						StorageClass.getInstance(this).setUserCountry(country);
						StorageClass.getInstance(this).setPinCode(pincode);
						StorageClass.getInstance(this).setMobileNumber(
								mobileNumber);
						StorageClass.getInstance(this).setAddress(addressLine);
						navigateToRentDates();

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				StorageClass.getInstance(this).setUserName(userName);
				StorageClass.getInstance(this).setAuthHeader(
						authenticationHeader);
			}
		}
	}

	private void navigateToRentDates() {
		Intent intent = new Intent(this, RentDatesActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedAdId", selectedProductAdId);
		mBundle.putString("productPrice", mPrice);
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("productDescription", mProductDescription);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

}
