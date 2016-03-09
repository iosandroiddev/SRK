package com.sabrentkaro.login;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;
import com.models.FbUserInfo;
import com.models.GPlusUserInfo;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.HomeActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.sabrentkaro.postad.PostAdDocumentActivity;
import com.sabrentkaro.search.RentDatesActivity;
import com.utils.ApiUtils;
import com.utils.GetSocialDetails;
import com.utils.GetSocialDetails.IFbLoginCallBack;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class LoginActivity extends BaseActivity implements IFbLoginCallBack,
		ConnectionCallbacks, OnConnectionFailedListener {

	private EditText mEditEmail, mEditPassword;
	private TextView mbtnLogin, mbtnRegister, mbtnForgotPassword, mbtnFacebok,
			mbtnGoogle;
	private String selectedProductAdId, mPrice, mMonthPrice, mWeekPrice,
			mProductDescription, mQuantity;
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
	private HashMap<String, String> controlLayouts = new HashMap<String, String>();
	private String fbAccessToken = "";
	private String fbId = "";
	private String gPlusId = "";
	private FbUserInfo mFbUserInfo;
	private GPlusUserInfo mGPlusUserInfo;

	private boolean hasBundle = false;
	private boolean btnGoogleClicked = false;
	private boolean mSignInClicked;
	private boolean mIntentInProgress;

	GoogleApiClient mGoogleApiClient;
	private ConnectionResult mConnectionResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_login);
		getDetails();
		loadLayoutReferences();
		hideSoftKeyboard();
		initGoogleApiClient();

	}

	private void initGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null && mBundle.containsKey("isPostAd")) {
				hasBundle = true;
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
				controlLayouts = (HashMap<String, String>) mBundle
						.getSerializable("controlLayouts");
			} else {
				hasBundle = true;
				selectedProductAdId = mBundle.getString("selectedAdId");
				mPrice = mBundle.getString("productPrice");
				mMonthPrice = mBundle.getString("productPriceMonth");
				;
				mWeekPrice = mBundle.getString("productPriceweek");
				mProductDescription = mBundle.getString("productDescription");
				mQuantity = mBundle.getString("quantity");
			}
		}
	}

	private void loadLayoutReferences() {
		mEditEmail = (EditText) findViewById(R.id.emailId);
		mEditPassword = (EditText) findViewById(R.id.password);
		mbtnLogin = (TextView) findViewById(R.id.btnLoginEmail);
		mbtnRegister = (TextView) findViewById(R.id.btnSignUp);
		mbtnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);
		StaticUtils.setEditTextHintFont(mEditEmail, this);
		StaticUtils.setEditTextHintFont(mEditPassword, this);
		mbtnForgotPassword.setOnClickListener(this);

		mbtnFacebok = (TextView) findViewById(R.id.btnFb);
		mbtnGoogle = (TextView) findViewById(R.id.btnGoogle);

		mbtnLogin.setOnClickListener(this);
		mbtnRegister.setOnClickListener(this);
		mbtnFacebok.setOnClickListener(this);
		mbtnGoogle.setOnClickListener(this);
		// mEditEmail.setText("harathithippaluru@sabrentkaro.com");
		// mEditPassword.setText("harathi@raj1");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnLoginEmail:
			btnLoginUserClicked();
			break;
		case R.id.btnSignUp:
			btnRegisterClicked();
			break;
		case R.id.btnForgotPassword:
			showForgotPasswordPopup();
			break;
		case R.id.btnFb:
			btnFacebookClicked();
			break;
		case R.id.btnGoogle:
			btnGoogleClicked();
			break;
		default:
			break;
		}
	}

	private void btnGoogleClicked() {
		// btnGoogleClicked = true;
		// if (!mGoogleApiClient.isConnecting()) {
		// checkSigningError();
		// mSignInClicked = true;
		// }
		showToast("Will be available in Next Release");

	}

	private void checkSigningError() {
		if (mConnectionResult != null && mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, 10);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		} else {
			if (mGPlusUserInfo != null) {
				responseForGPlus();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private void btnFacebookClicked() {
		btnGoogleClicked = false;
		// loginViaFb();
		showToast("Will be available in Next Release");
	}

	private void btnRegisterClicked() {
		Intent mIntent = new Intent(this, RegisterActivity.class);
		if (hasBundle) {
			if (selectedProductAdId == null
					|| selectedProductAdId.length() == 0) {
				Bundle mBundle = new Bundle();
				mBundle.putString("category", mCategory);
				mBundle.putString("subCategory", mSubCategory);
				mBundle.putString("adTitle", mAdTitle);
				mBundle.putString("productDescription", mProductDesc);
				mBundle.putString("productCondition", "");
				mBundle.putString("userInstructions", mUserInstructions);
				mBundle.putString("additionalStuff", mAdditionalStuff);
				mBundle.putString("productPurchasedPrice",
						mProductPurchasedPrice);
				mBundle.putString("dailyCost", mDailyCost);
				mBundle.putString("productAdId", mProductAdId);
				mBundle.putString("weekCost", mWeekCost);
				mBundle.putString("monthlyCost", mMonthCost);
				mBundle.putString("productCondition", mtxtRating);
				mBundle.putString("filePath", mFilePath);
				mBundle.putString("quantity", mQuantity);
				mBundle.putString("securityDeposit", mSecurityDeposit);
				mBundle.putString("productConditionName", mtxtCondName);
				mBundle.putSerializable("controlLayouts", controlLayouts);
				mIntent.putExtras(mBundle);
			} else {
				Bundle mBundle = new Bundle();
				mBundle.putString("selectedAdId", selectedProductAdId);
				mBundle.putString("productPrice", mPrice);
				mBundle.putString("quantity", mQuantity);
				mBundle.putString("productPriceMonth", mMonthPrice);
				mBundle.putString("productPriceWeek", mWeekPrice);
				mBundle.putString("productDescription", mProductDescription);
				mIntent.putExtras(mBundle);
			}
		} else {
		}
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
			initLoginApi(false, false);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	private void loginViaFb() {
		GetSocialDetails mSocial = new GetSocialDetails(this);
		mSocial.getAndPostFaceBookUserDetails();
	}

	private void initLoginApi(boolean fbLogin, boolean gplusLogin) {
		showProgressLayout();
		JSONObject mLoginParams = new JSONObject();
		try {
			mLoginParams.put("Email", mEditEmail.getText().toString());
			mLoginParams.put("IsClaims", false);
			if (fbLogin) {
				mLoginParams.put("Provider", "FB");
				mLoginParams.put("ProviderKey", fbId);
			}
			if (gplusLogin) {
				mLoginParams.put("Provider", "Google");
				mLoginParams.put("ProviderKey", gPlusId);
			}
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
			if (fbLogin || gplusLogin) {

			} else {
				mLogin.put("Verifications", mPassword);
			}
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

							if (hasBundle) {
								if (selectedProductAdId == null
										|| selectedProductAdId.length() == 0) {
									navigateToPostAdDocuments();
								} else {
									navigateToRentDates();
								}
							} else {
								navigateToHome();
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

	private void navigateToHome() {
		Intent mIntent = new Intent(this, HomeActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
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
		mBundle.putSerializable("controlLayouts", controlLayouts);
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
		mBundle.putString("productPriceMonth", mMonthPrice);
		mBundle.putString("productPriceWeek", mWeekPrice);
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
				showToast("Message was sent to your mobile");
			} else {
				showToast(response.optString("Information"));
			}
		}
	}

	@Override
	public void onFbLoginSucsess(String accessToken, JSONObject mUserInfo) {
		responseForFB(accessToken, mUserInfo);
	}

	private void responseForFB(String accessToken, JSONObject mUserInfo) {
		fbAccessToken = accessToken;
		if (mUserInfo != null) {
			fbId = mUserInfo.optString("id");
			String email = mUserInfo.optString("email");
			String firstName = mUserInfo.optString("first_name");
			String lastName = mUserInfo.optString("last_name");
			String gender = mUserInfo.optString("gender");
			String name = mUserInfo.optString("name");

			mFbUserInfo = new FbUserInfo();
			mFbUserInfo.setId(fbId);
			mFbUserInfo.setEmail(email);
			mFbUserInfo.setFirstName(firstName);
			mFbUserInfo.setLastName(lastName);
			mFbUserInfo.setGender(gender);
			mFbUserInfo.setName(name);
			mFbUserInfo.setAccessToken(fbAccessToken);
			mEditEmail.setText(email);
			initLoginApi(true, false);

		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		hideProgressLayout();
		responseForGPlus();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 10) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}
			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	private void responseForGPlus() {
		Person signedInUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		mGPlusUserInfo = new GPlusUserInfo();
		if (signedInUser != null) {

			if (signedInUser.hasId()) {
				String userId = signedInUser.getId();
				mGPlusUserInfo.setId(userId);
				gPlusId = userId;
			}
			if (signedInUser.hasDisplayName()) {
				String userName = signedInUser.getDisplayName();
				mGPlusUserInfo.setUserName(userName);
			}

			if (signedInUser.hasTagline()) {
				String tagLine = signedInUser.getTagline();
				mGPlusUserInfo.setTagLine(tagLine);
			}

			if (signedInUser.hasAboutMe()) {
				String aboutMe = signedInUser.getAboutMe();
				mGPlusUserInfo.setAboutMe(aboutMe);
			}

			if (signedInUser.hasBirthday()) {
				String birthday = signedInUser.getBirthday();
				mGPlusUserInfo.setBirthday(birthday);
			}

			if (signedInUser.hasCurrentLocation()) {
				String userLocation = signedInUser.getCurrentLocation();
				mGPlusUserInfo.setLocation(userLocation);
			}

			String userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
			mGPlusUserInfo.setEmail(userEmail);

			if (signedInUser.hasImage()) {
				String userProfilePicUrl = signedInUser.getImage().getUrl();
				int profilePicRequestSize = 250;
				userProfilePicUrl = userProfilePicUrl.substring(0,
						userProfilePicUrl.length() - 2) + profilePicRequestSize;
				mGPlusUserInfo.setProfilePic(userProfilePicUrl);
			}

			if (btnGoogleClicked) {
				mEditEmail.setText(userEmail);
				initLoginApi(false, true);
			}

		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					11).show();
			return;
		}
		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				checkSigningError();
			}
		}

	}
}
