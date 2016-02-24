package com.sabrentkaro.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jsonclasses.JSONObjectRequestResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.sabrentkaro.postad.PostAdDocumentActivity;
import com.sabrentkaro.search.RentDatesActivity;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class LoginActivity extends BaseActivity {

	private EditText mEditEmail, mEditPassword;
	private TextView mbtnLogin, mbtnRegister, mbtnForgotPassword;
	private String selectedProductAdId, mPrice, mProductDescription, mQuantity;
	private Dialog mForgotPasswordDialog;

	private String mCategory;
	private String mSubCategory;
	private String mAdTitle;
	private String mProductDesc;
	private String mProductCondition;
	private String mProductPurchasedPrice;
	private String mUserInstructions;
	private String mAdditionalStuff;
	private String mDailyCost;
	private String mMonthCost;
	private String mProductAdId;
	private String mSecurityDeposit;
	private String mFilePath;
	private String mWeekCost;
	private String mtxtRating;
	private String mtxtCondName;

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
			if (mBundle != null && mBundle.containsKey("isPostAd")) {
				mCategory = mBundle.getString("category");
				mSubCategory = mBundle.getString("subCategory");
				mAdTitle = mBundle.getString("adTitle");
				mProductDesc = mBundle.getString("productDescription");
				mAdditionalStuff = mBundle.getString("additionalStuff");
				mUserInstructions = mBundle.getString("userInstructions");
				mProductPurchasedPrice = mBundle
						.getString("productPurchasedPrice");
				mDailyCost = mBundle.getString("dailyCost");
				mMonthCost = mBundle.getString("monthlyCost");
				mWeekCost = mBundle.getString("weekCost");
				mQuantity = mBundle.getString("quantity");
				mFilePath = mBundle.getString("filePath");
				mSecurityDeposit = mBundle.getString("securityDeposit");
				mProductAdId = mBundle.getString("productAdId");
				mtxtRating = mBundle.getString("productCondition");
				mtxtCondName = mBundle.getString("productConditionName");
			} else {
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
		mbtnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);
		StaticUtils.setEditTextHintFont(mEditEmail, this);
		StaticUtils.setEditTextHintFont(mEditPassword, this);
		mbtnForgotPassword.setOnClickListener(this);

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
		case R.id.btnForgotPassword:
			showForgotPasswordPopup();
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
						showToast("Something went wrong. Please try again Later");
						hideProgressLayout();
					}
				});

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);

	}

	private void responseForLoginApi(JSONObject response) {
		String userName = "";
		if (response != null) {
			if (response.optString("Information") == null
					|| response.optString("Information").equalsIgnoreCase(
							"null")
					|| response.optString("Information").length() == 0) {
				JSONObject mObjUser = response.optJSONObject("User");
				if (mObjUser != null) {
					String authenticationHeader = mObjUser.optJSONObject(
							"UserTransactions").optString(
							"AuthenticationHeader");
					JSONObject mObjUserProfile = mObjUser
							.optJSONObject("UserProfile");
					if (mObjUserProfile != null) {
						userName = mObjUserProfile.optString("Name");
					}
					JSONObject mObjUserAdress = (JSONObject) mObjUser
							.optJSONArray("Addresses").opt(0);
					if (mObjUserAdress != null) {
						try {
							String addressLine = mObjUserAdress
									.getString("AddressLine1")
									+ " "
									+ mObjUserAdress.getString("AddressLine2");
							String city = mObjUserAdress.getString("City");
							String state = mObjUserAdress.getString("State");
							String country = mObjUserAdress
									.getString("Country");
							String pincode = mObjUserAdress
									.getString("PinCode");
							String mobileNumber = mObjUserAdress
									.getString("MobileNo");

							StorageClass.getInstance(this).setUserCity(city);

							StorageClass.getInstance(this).setUserState(state);
							StorageClass.getInstance(this).setUserCountry(
									country);
							StorageClass.getInstance(this).setPinCode(pincode);
							StorageClass.getInstance(this).setMobileNumber(
									mobileNumber);
							showToast("User Logged In Successfully!");
							StorageClass.getInstance(this).setAddress(
									addressLine);
							if (selectedProductAdId == null
									|| selectedProductAdId.length() == 0) {
								navigateToPostAdDocuments();
							} else {
								navigateToRentDates();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					StorageClass.getInstance(this).setUserName(userName);
					StorageClass.getInstance(this).setAuthHeader(
							authenticationHeader);
				} else {
					showToast(response.optString("Information"));
				}
			} else {
				showToast(response.optString("Information"));
			}
		}
	}

	private void navigateToPostAdDocuments() {
		Intent mIntent = new Intent(this, PostAdDocumentActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("category", mCategory);
		mBundle.putString("subCategory", mSubCategory);
		mBundle.putString("adTitle", mAdTitle);
		mBundle.putString("productDescription", mProductDesc);
		mBundle.putString("productCondition", "");
		mBundle.putString("userInstructions", mUserInstructions);
		mBundle.putString("additionalStuff", mAdditionalStuff);
		mBundle.putString("productPurchasedPrice", mProductPurchasedPrice);
		mBundle.putString("dailyCost", mDailyCost);
		mBundle.putString("productAdId", mProductAdId);
		mBundle.putString("weekCost", mWeekCost);
		mBundle.putString("monthlyCost", mMonthCost);
		mBundle.putString("productCondition", mtxtRating);
		mBundle.putString("filePath", mFilePath);
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("securityDeposit", mSecurityDeposit);
		mBundle.putString("productConditionName", mtxtCondName);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
		finish();
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
		finish();
	}

	private void showForgotPasswordPopup() {
		mForgotPasswordDialog = new Dialog(this);
		mForgotPasswordDialog.setCancelable(false);
		mForgotPasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mForgotPasswordDialog.setContentView(R.layout.forgot_password_popup);
		mForgotPasswordDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		TextView mbtnCancel = (TextView) mForgotPasswordDialog
				.findViewById(R.id.btn_cancel);
		TextView mbtnSubmit = (TextView) mForgotPasswordDialog
				.findViewById(R.id.btn_submit);

		final EditText meditFrgtEmail = (EditText) mForgotPasswordDialog
				.findViewById(R.id.editMobileNumber);
		StaticUtils.setEditTextHintFont(meditFrgtEmail, this);

		mForgotPasswordDialog.show();

		mbtnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mForgotPasswordDialog.dismiss();
			}
		});

		mbtnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(meditFrgtEmail.getText().toString())) {
					showToast("Please Enter Mobile Number");
				} else {
					initiateForgotPassswordApi(meditFrgtEmail.getText()
							.toString());
				}
			}
		});
	}

	private void initiateForgotPassswordApi(String string) {
		showProgressLayout();

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.GETEMAILFROMMOBILE + "" + string, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						responseForForgotPassAPi(response);
						if (mForgotPasswordDialog != null)
							mForgotPasswordDialog.dismiss();
						hideProgressLayout();
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("Something went wrong. Please try again Later");
						hideProgressLayout();
					}
				});

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);
	}

	private void responseForForgotPassAPi(JSONObject response) {
		if (response != null) {
			if (response.optString("Information") == null
					|| response.optString("Information").equalsIgnoreCase(
							"null")
					|| response.optString("Information").length() == 0) {
				showToast("UserName Sent to your respective Mobile Number");
			} else {
				showToast(response.optString("Information"));
			}
		}
	}
}
