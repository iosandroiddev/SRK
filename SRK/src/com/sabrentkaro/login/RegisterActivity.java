package com.sabrentkaro.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;
import com.models.CityModel;
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

public class RegisterActivity extends BaseActivity implements IFbLoginCallBack,
		ConnectionCallbacks, OnConnectionFailedListener {

	private EditText mEditEmail, mEditPassword, mEditDisplayName,
			mEditMobileNumber, mEditPanCardNumber;
	private TextView mbtnGenerateOtp, mbtnSelectCity, mbtnSelectUser,
			mbtnSignUpFb, mbtnSignupGplus, mbtnSingupEmail, mtxtMessage;
	private LinearLayout mlayoutCommonFields;
	private CheckBox mCheckTerms;
	private String deviceId;

	GoogleApiClient mGoogleApiClient;
	private ConnectionResult mConnectionResult;
	private boolean mSignInClicked;
	private boolean mIntentInProgress;
	private GPlusUserInfo mGPlusUserInfo = null;
	private boolean gplusClicked = false;
	private boolean fbClicked = false;
	private FbUserInfo mFbUserInfo;
	private GetSocialDetails mSocial;

	private String selectedProductAdId, mPrice, mMonthPrice, mWeekPrice,
			mProductDescription, mQuantity;

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
	private boolean hasBundle = false;
	private HashMap<String, String> controlLayouts = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_regsiter);
		getDeviceId();
		loadLayoutReferences();
		hideSoftKeyboard();
		// getAccountsRegisteredOnDevice();
		initGoogleApiClient();
		mbtnGenerateOtp.setVisibility(View.VISIBLE);
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

	private void getAccountsRegisteredOnDevice() {
		ArrayList<String> mPossibleEmails = new ArrayList<String>();
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(this).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				mPossibleEmails.add(account.name);
			}
		}

		String[] mUserAccounts = null;
		if (mPossibleEmails != null) {
			mUserAccounts = new String[mPossibleEmails.size()];
			for (int i = 0; i < mPossibleEmails.size(); i++) {
				mUserAccounts[i] = mPossibleEmails.get(i);
			}
		}

		if (mUserAccounts != null) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Select Account");
			alert.setSingleChoiceItems(mUserAccounts, -1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alert.show();
		}
	}

	private void getDeviceId() {
		deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	}

	private void loadLayoutReferences() {
		mEditEmail = (EditText) findViewById(R.id.emailId);
		mEditPassword = (EditText) findViewById(R.id.password);
		mEditDisplayName = (EditText) findViewById(R.id.displayName);
		mEditMobileNumber = (EditText) findViewById(R.id.mobileNumber);
		mCheckTerms = (CheckBox) findViewById(R.id.checkTerms);
		mbtnGenerateOtp = (TextView) findViewById(R.id.btnGenerateOtp);
		mbtnSelectCity = (TextView) findViewById(R.id.btnSelectCountry);
		mbtnSelectUser = (TextView) findViewById(R.id.btnSelectUserType);
		mEditPanCardNumber = (EditText) findViewById(R.id.editPanCard);

		mtxtMessage = (TextView) findViewById(R.id.txtMessage);

		mlayoutCommonFields = (LinearLayout) findViewById(R.id.commonLayout);

		mbtnSignUpFb = (TextView) findViewById(R.id.btnSignUpFb);
		mbtnSignupGplus = (TextView) findViewById(R.id.btnSignupGoogle);
		mbtnSingupEmail = (TextView) findViewById(R.id.btnSignUpEmail);

		mbtnGenerateOtp.setOnClickListener(this);
		mbtnSelectCity.setOnClickListener(this);
		mbtnSelectUser.setOnClickListener(this);

		mbtnSignUpFb.setOnClickListener(this);
		mbtnSignupGplus.setOnClickListener(this);
		mbtnSingupEmail.setOnClickListener(this);

		StaticUtils.setEditTextHintFont(mEditEmail, this);
		StaticUtils.setEditTextHintFont(mEditPanCardNumber, this);
		StaticUtils.setEditTextHintFont(mEditPassword, this);
		StaticUtils.setEditTextHintFont(mEditDisplayName, this);
		StaticUtils.setEditTextHintFont(mEditMobileNumber, this);
		StaticUtils.setCheckBoxFont(mCheckTerms, this);

		mbtnSelectCity.setText(StorageClass.getInstance(this).getCity());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnSignUpEmail:
			btnSignUpViaEmailClicked();
			break;
		case R.id.btnSignUpFb:
			btnSingUpViaFbClicked();
			break;
		case R.id.btnSignupGoogle:
			btnSingUpViaGPlusClicked();
			break;
		case R.id.btnGenerateOtp:
			btnGenerateOtpClicked();
			break;
		case R.id.btnSelectCountry:
			showCityAlert();
			break;
		case R.id.btnSelectUserType:
			btnSelectUserTypeClicked();
			break;
		default:
			break;
		}

	}

	private void btnSingUpViaGPlusClicked() {
		// StaticUtils.expandCollapse(mbtnSignUpFb, false);
		// StaticUtils.expandCollapse(mbtnSingupEmail, false);
		// gplusClicked = true;
		// btnGoogleClicked();
		showToast("Will be available in Next Release");
	}

	private void btnSingUpViaFbClicked() {
		// StaticUtils.expandCollapse(mbtnSignupGplus, false);
		// StaticUtils.expandCollapse(mbtnSingupEmail, false);
		showToast("Will be available in Next Release");
		// gplusClicked = false;
		// loginViaFb();
	}

	private void btnSignUpViaEmailClicked() {
		StaticUtils.expandCollapse(mbtnSignupGplus, false);
		StaticUtils.expandCollapse(mbtnSignUpFb, false);
		// gplusClicked = false;
		StaticUtils.expandCollapse(mlayoutCommonFields, true);
		mbtnGenerateOtp.setVisibility(View.VISIBLE);
	}

	private void btnSelectUserTypeClicked() {
		ArrayList<String> mUserTypeArray = new ArrayList<String>();
		mUserTypeArray.add("Individual");
		mUserTypeArray.add("Business");
		int pos = -1;
		if (mUserTypeArray != null) {
			final String[] mUserTypes = new String[mUserTypeArray.size()];
			for (int i = 0; i < mUserTypeArray.size(); i++) {
				if (mbtnSelectUser.getText().toString()
						.equalsIgnoreCase("Select User Type")) {
					pos = -1;
				} else {
					if (mUserTypeArray.get(i).equalsIgnoreCase(
							mbtnSelectUser.getText().toString())) {
						pos = i;
					}
				}
				mUserTypes[i] = mUserTypeArray.get(i).toString();
			}
			if (mUserTypes != null) {
				StorageClass.getInstance(this).getUserCity();
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select User Type");
				alert.setSingleChoiceItems(mUserTypes, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								mbtnSelectUser.setText(mUserTypes[which]);
								if (mUserTypes[which]
										.equalsIgnoreCase("Business")) {
									mEditPanCardNumber
											.setVisibility(View.VISIBLE);
								} else {
									if (mEditPanCardNumber.getVisibility() == View.VISIBLE) {
										mEditPanCardNumber
												.setVisibility(View.GONE);
									}
								}
								StaticUtils.expandCollapse(mlayoutCommonFields,
										true);
							}
						});
				alert.show();
			}
		}
	}

	private void btnGenerateOtpClicked() {
		if (mbtnSelectUser.getText().toString()
				.equalsIgnoreCase("Select User Type")) {
			showToast("Please Select User Type");
		} else {
			if (TextUtils.isEmpty(mEditEmail.getText().toString())) {
				showToast("Please Enter Email");
			} else {
				if (!StaticUtils.isValidEmail(mEditEmail.getText().toString())) {
					showToast("Please Enter Valid Email");
				} else {
					if (TextUtils.isEmpty(mEditPassword.getText().toString())) {
						showToast("Please Enter Password");
					} else {
						if (TextUtils.isEmpty(mEditDisplayName.getText()
								.toString())) {
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

									if (mbtnSelectUser.getText().toString()
											.equalsIgnoreCase("Individual")) {
										initiateRegisterApi();
									} else {
										if (TextUtils
												.isEmpty(mEditPanCardNumber
														.getText().toString())) {
											showToast("Please Enter PAN Card Number");
										} else {
											if (mEditPanCardNumber.getText()
													.toString().length() != 10) {
												showToast("Please Enter Valid PAN Card Number");
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
	}

	private void initiateRegisterApi() {
		showProgressLayout();
		JSONObject mParams = new JSONObject();
		try {

			JSONObject mUserProfile = new JSONObject();
			mUserProfile.put("Name", mEditDisplayName.getText().toString());
			mUserProfile.put("Location", mbtnSelectCity.getText().toString());

			JSONObject mLogin = new JSONObject();
			mLogin.put("Email", mEditEmail.getText().toString());
			mLogin.put("MobileNumber", mEditMobileNumber.getText().toString());
			// mLogin.put("IsClaims", "false");
			// if (fbClicked) {
			// mLogin.put("Provider", "FB");
			// mLogin.put("ProviderKey", mFbUserInfo.getId());
			// }
			// if (gplusClicked) {
			// mLogin.put("Provider", "Google");
			// mLogin.put("ProviderKey", mGPlusUserInfo.getId());
			// }
			if (mbtnSelectUser.getText().toString()
					.equalsIgnoreCase("Individual")) {
				mLogin.put("UserTypeId", "1");
			} else {
				mLogin.put("UserTypeId", "2");
				JSONObject mTax = new JSONObject();
				try {
					mTax.put("TaxPinNo", mEditPanCardNumber.getText()
							.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mUserProfile.put("Businessuser", mTax);
			}

			JSONObject mUserProfileParams = new JSONObject();
			mUserProfileParams.put("UserProfile", mUserProfile);
			mUserProfileParams.put("Login", mLogin);

			JSONObject mCurrentPassword = new JSONObject();
			try {
				mCurrentPassword.put("CurrentPassword", mEditPassword.getText()
						.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject mVerifications = new JSONObject();
			mVerifications.put("MobileAccessToken",
					StorageClass.getInstance(this).getAccessToken());
			mVerifications.put("Password", mCurrentPassword);
			try {
				mUserProfileParams.put("Verifications", mVerifications);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mParams.put("User", mUserProfileParams);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.POSTREGISTERUSERMOBILE, mParams,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						responseForRegisterApi(response);
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

	private void responseForRegisterApi(JSONObject response) {
		String userName = "";
		if (response != null) {
			String mInformation = response.optString("Information");
			if (mInformation == null || mInformation.contains("null")
					|| mInformation.length() == 0) {

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
					JSONArray mAddressArray = (JSONArray) mObjUser
							.optJSONArray("Addresses");
					if (mAddressArray != null) {
						JSONObject mObjUserAdress = (JSONObject) mAddressArray
								.opt(0);
						if (mObjUserAdress != null) {
							try {
								String addressLine = mObjUserAdress
										.getString("AddressLine1")
										+ " "
										+ mObjUserAdress
												.getString("AddressLine2");
								String city = mObjUserAdress.getString("City");
								String state = mObjUserAdress
										.getString("State");
								String country = mObjUserAdress
										.getString("Country");
								String pincode = mObjUserAdress
										.getString("PinCode");
								String mobileNumber = mObjUserAdress
										.getString("MobileNo");

								StorageClass.getInstance(this)
										.setUserCity(city);

								StorageClass.getInstance(this).setUserState(
										state);
								StorageClass.getInstance(this).setUserCountry(
										country);
								StorageClass.getInstance(this).setPinCode(
										pincode);
								StorageClass.getInstance(this).setMobileNumber(
										mobileNumber);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					StorageClass.getInstance(this).setUserName(userName);
					StorageClass.getInstance(this).setAuthHeader(
							authenticationHeader);
				}
				// if (hasBundle) {
				// if (selectedProductAdId == null
				// || selectedProductAdId.length() == 0) {
				// navigateToPostAdDocuments();
				// } else {
				// navigateToRentDates();
				// }
				// } else {
				// navigateToHome();
				// }
				showToast("Registered Successfully Done!");
				setResult(RESULT_OK);
				finish();

			} else {
				showAlert(mInformation);
			}
		}
	}

	private void showAlert(String message) {
		new AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage(message)
				.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog.cancel();
					}
				})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
	}

	private void initGoogleApiClient() {

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	private void btnGoogleClicked() {
		if (!mGoogleApiClient.isConnecting()) {
			checkSigningError();
			mSignInClicked = true;
		}

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
		// logoutFb();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
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

			if (gplusClicked) {
				fbClicked = false;
				StaticUtils.expandCollapse(mlayoutCommonFields, true);
				mEditEmail.setText(userEmail);
				mEditEmail.setEnabled(false);
				mEditDisplayName.setText(mGPlusUserInfo.getUserName());
				mbtnGenerateOtp.setVisibility(View.VISIBLE);
				StaticUtils.expandCollapse(mbtnSignupGplus, false);
				mtxtMessage
						.setText("You've successfully authenticated with Google. Please enter the details below to complete your one time registration with us");
				StaticUtils.expandCollapse(mtxtMessage, true);
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

	private void loginViaFb() {
		fbClicked = true;
		gplusClicked = false;
		mSocial = new GetSocialDetails(this);
		mSocial.getAndPostFaceBookUserDetails();
	}

	@Override
	public void onFbLoginSucsess(String accessToken, JSONObject mUserInfo) {
		responseForFB(accessToken, mUserInfo);
	}

	private void responseForFB(String accessToken, JSONObject mUserInfo) {
		if (mUserInfo != null) {
			String fbId = mUserInfo.optString("id");
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
			mFbUserInfo.setAccessToken(accessToken);

			StaticUtils.expandCollapse(mlayoutCommonFields, true);
			mEditEmail.setText(email);
			if (email != null && email.length() > 0)
				mEditEmail.setEnabled(false);
			mEditDisplayName.setText(name);
			mbtnGenerateOtp.setVisibility(View.VISIBLE);
			StaticUtils.expandCollapse(mbtnSignUpFb, false);
			mtxtMessage
					.setText("You've successfully authenticated with Facebook. Please enter the details below to complete your one time registration with us");
			StaticUtils.expandCollapse(mtxtMessage, true);

		}
	}

	private void logOutGPlus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}

	}

	private void logoutFb() {
		if (mSocial != null)
			mSocial.logoutFb();
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
}
