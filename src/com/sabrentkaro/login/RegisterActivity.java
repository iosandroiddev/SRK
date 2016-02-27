package com.sabrentkaro.login;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.CityModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class RegisterActivity extends BaseActivity {

	private EditText mEditEmail, mEditPassword, mEditConfirmEmail,
			mEditDisplayName, mEditMobileNumber;
	private TextView mbtnGenerateOtp, mbtnSelectCity;
	private CheckBox mCheckTerms;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_regsiter);
		loadLayoutReferences();
		hideSoftKeyboard();
	}

	private void loadLayoutReferences() {
		mEditEmail = (EditText) findViewById(R.id.emailId);
		mEditConfirmEmail = (EditText) findViewById(R.id.confirmEmailId);
		mEditPassword = (EditText) findViewById(R.id.password);
		mEditDisplayName = (EditText) findViewById(R.id.displayName);
		mEditMobileNumber = (EditText) findViewById(R.id.mobileNumber);
		mCheckTerms = (CheckBox) findViewById(R.id.checkTerms);
		mbtnGenerateOtp = (TextView) findViewById(R.id.btnGenerateOtp);
		mbtnSelectCity = (TextView) findViewById(R.id.btnSelectCountry);
		mbtnGenerateOtp.setOnClickListener(this);
		mbtnSelectCity.setOnClickListener(this);
		StaticUtils.setEditTextHintFont(mEditEmail, this);
		StaticUtils.setEditTextHintFont(mEditConfirmEmail, this);
		StaticUtils.setEditTextHintFont(mEditPassword, this);
		StaticUtils.setEditTextHintFont(mEditDisplayName, this);
		StaticUtils.setEditTextHintFont(mEditMobileNumber, this);
		StaticUtils.setCheckBoxFont(mCheckTerms, this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnGenerateOtp) {
			btnGenerateOtpClicked();
		} else if (v.getId() == R.id.btnSelectCountry) {
			showCityAlert();
		}

	}

	private void btnGenerateOtpClicked() {
		if (TextUtils.isEmpty(mEditEmail.getText().toString())) {
			showToast("Please Enter Email");
		} else {
			if (!StaticUtils.isValidEmail(mEditEmail.getText().toString())) {
				showToast("Please Enter Valid Email");
			} else {
				if (TextUtils.isEmpty(mEditConfirmEmail.getText().toString())) {
					showToast("Please Confirm Email");
				} else {
					if (!StaticUtils.isValidEmail(mEditConfirmEmail.getText()
							.toString())) {
						showToast("Please Enter Valid Confirm Email");
					} else {
						if (!mEditConfirmEmail.getText().toString()
								.equals(mEditEmail.getText().toString())) {
							showToast("Email Mismatch");
						} else {
							if (TextUtils.isEmpty(mEditPassword.getText()
									.toString())) {
								showToast("Please Enter Password");
							} else {
								if (TextUtils.isEmpty(mEditDisplayName
										.getText().toString())) {
									showToast("Please Enter Display Name");
								} else {
									if (mbtnSelectCity.getText().toString()
											.equalsIgnoreCase("Select City")) {
										showToast("Please Select City");
									} else {
										if (TextUtils.isEmpty(mEditMobileNumber
												.getText().toString())) {
											showToast("Please Enter Mobile Number");
										} else {
											if (!mCheckTerms.isChecked()) {
												showToast("Please check Terms & Conditions");
											} else {
												initiateRegisterApi();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void initiateRegisterApi() {
		showProgressLayout();
		JSONObject mParams = new JSONObject();
		try {

			JSONObject mUserProfile = new JSONObject();
			mUserProfile.put("Name", mEditDisplayName.getText().toString());
			mUserProfile.put("Location", mbtnSelectCity.getText().toString());

			JSONObject mLogin = new JSONObject();
			mLogin.put("Email", mEditConfirmEmail.getText().toString());
			mLogin.put("MobileNumber", mEditMobileNumber.getText().toString());
			mLogin.put("IsClaims", "false");
			mLogin.put("UserTypeId", "1");

			JSONObject mUserProfileParams = new JSONObject();
			mUserProfileParams.put("UserProfile", mUserProfile);
			mUserProfileParams.put("Login", mLogin);
			mParams.put("User", mUserProfileParams);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.POSTREGISTERUSER, mParams, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						showToast("Success");
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						showToast("Failure");
					}
				});

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);

	}

	private void showCityAlert() {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this)
				.getCityList();
		int pos = -1;
		if (mCityArray != null) {
			final String[] mCities = new String[mCityArray.size()];
			for (int i = 0; i < mCityArray.size(); i++) {
				if (mbtnSelectCity.getText().toString()
						.equalsIgnoreCase("Select City")) {
					pos = -1;
				} else {
					if (mCityArray
							.get(i)
							.getName()
							.equalsIgnoreCase(
									mbtnSelectCity.getText().toString())) {
						pos = i;
					}
				}
				mCities[i] = mCityArray.get(i).getName();
			}
			if (mCities != null) {
				StorageClass.getInstance(this).getUserCity();
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select City");
				alert.setSingleChoiceItems(mCities, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								mbtnSelectCity.setText(mCities[which]);
							}
						});
				alert.show();
			}
		}
	}

}
