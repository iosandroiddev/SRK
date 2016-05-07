package com.sabrentkaro.postad;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.CategoryModel;
import com.models.PostAdModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.HomeActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.PostAdSaver;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.PhotoUpload;
import com.utils.PhotoUpload.IImageUpload;
import com.utils.PostAd;
import com.utils.PostAd.IPostAd;
import com.utils.SquareImageView;
import com.utils.StaticUtils;
import com.utils.StorageClass;
import com.utils.UtilNetwork;

public class PostAdPreview extends BaseActivity implements IImageUpload,
		IPostAd {

	private TextView mtxtCategory, mtxtSubCategory, mtxtTitle, mtxtDesc,
			mtxtRating, mtxtInstructions, mtxtStuff, mtxtQuanity,
			mtxtPurchasedCost, mtxtDailyCost, mtxtWeekCost, mtxtMonthCost,
			mtxtSecurityDeposit;
	private ArrayList<PostAdModel> mArrayFields = new ArrayList<PostAdModel>();

	private TextView mbtnSubmit, mbtnBack;
	private String filePath, absFilePath;
	private LinearLayout mSelectLayout;
	private boolean uploadCoverImage = false;
	private HashMap<String, String> controlLayouts = new HashMap<String, String>();
	private String mCategory = "";
	private String mSubCategory = "";
	private String mAdTitle = "";
	private String mProductDesc = "";
	private String mProductCondition = "";
	private String mProductPurchasedPrice = "";
	private String mUserInstructions = "";
	private String mAdditionalStuff = "";
	private String mDailyCost = "";
	private String mMonthCost = "";
	private String mProductAdId = "";
	private String mSecurityDeposit = "";
	private String mFilePath = "";
	private String mWeekCost = "";
	private String mQuantity = "";
	private String mRating = "";
	private boolean imagesCompleted = false;
	private String mAadthrNumber = "";
	private String mDrvingNumber = "";
	private String mMobileNubmer = "";
	private String mAadharName = "";
	private String mPinCode = "";
	private String mPanCard = "";
	private String mState = "";
	private String mCity = "";
	private String mAddress = "";
	private String mCode = "";
	private HorizontalScrollView mScrollimages;
	private ArrayList<Uri> mUriArray = new ArrayList<Uri>();
	private String mtxtCondName;
	private TextView mtxtAddress;
	private TextView mtxtState;
	private TextView mtxtCity;
	private TextView mtxtMobile;
	private TextView mtxtPincode;
	private int currentImageUploadCount = 0;
	private TextView mtxtUserAddress;
	private TextView mtxtUserState;
	private TextView mtxtUserCity;
	private TextView mtxtUserMobile;
	private TextView mtxtUserPincode;

	private String mPinCodeUser = "";
	private String mStateUser = "";
	private String mCityUser = "";
	private String mAddressUser = "";
	private String mMobileNubmerUser = "";

	private boolean showCurrentAdrees = false;

	private LinearLayout mLayoutCurrentAddress;

	private String mAuthHeader;
	int currentImageCount = 0;
	private JSONArray mItemsMediaArrayResponse;
	private LinearLayout mLayoutAttachments;
	private String mPricingArrayString;
	private String minimumRentalPeriod;
	private TextView mtxtFields;
	private String mDrvingState;
	private boolean isDrivingLicenseSelected;
	private boolean isAadharCardSelected;
	private boolean isPanCardSelected;
	private LinearLayout mMinRentalPeriod;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_ad_preview);
		loadLayoutReferences();
		getDetails();
		setDetails();
	}

	@SuppressWarnings("deprecation")
	private void setDetails() {
		mtxtCategory.setText(mCategory);
		mtxtSubCategory.setText(mSubCategory);
		mtxtTitle.setText(mAdTitle);
		mtxtDesc.setText(mProductDesc);
		mtxtStuff.setText(mAdditionalStuff);
		mtxtInstructions.setText(mUserInstructions);

		loadAttachments();

		mtxtPurchasedCost.setText(getString(R.string.rupeeone) + " "
				+ mProductPurchasedPrice);
		if (mDailyCost.length() == 0) {
			mtxtDailyCost.setVisibility(View.GONE);
		} else {
			mtxtDailyCost.setVisibility(View.VISIBLE);
		}
		if (mMonthCost.length() == 0) {
			mtxtMonthCost.setVisibility(View.GONE);
		} else {
			mtxtMonthCost.setVisibility(View.VISIBLE);
		}
		if (mWeekCost.length() == 0) {
			mtxtWeekCost.setVisibility(View.GONE);
		} else {
			mtxtWeekCost.setVisibility(View.VISIBLE);
		}

		String dailyCost = "<font color='black'>" + "Daily Cost:"
				+ " </font> <font color='#EC016D'>"
				+ getString(R.string.rupeeone) + " " + mDailyCost + "</font>";
		String monthCost = "<font color='black'>" + "Monthly Cost:"
				+ " </font> <font color='#EC016D'>"
				+ getString(R.string.rupeeone) + " " + mMonthCost + "</font>";
		String weekCost = "<font color='black'>" + "Weekly Cost:"
				+ " </font> <font color='#EC016D'>"
				+ getString(R.string.rupeeone) + " " + mWeekCost + "</font>";

		mtxtDailyCost.setText(Html.fromHtml(dailyCost),
				TextView.BufferType.SPANNABLE);
		mtxtMonthCost.setText(Html.fromHtml(monthCost),
				TextView.BufferType.SPANNABLE);
		mtxtQuanity.setText(mQuantity);
		mtxtWeekCost.setText(Html.fromHtml(weekCost),
				TextView.BufferType.SPANNABLE);
		mtxtSecurityDeposit.setText(getString(R.string.rupeeone) + " "
				+ mSecurityDeposit);
		mtxtRating.setText(mtxtCondName);

		mtxtDailyCost.setSelected(true);
		mtxtMonthCost.setSelected(true);
		mtxtWeekCost.setSelected(true);
		// mtxtAddress.setText("Address:" + " " + mAddress);
		// mtxtCity.setText("City:" + " " + mCity);
		// mtxtState.setText("State:" + " " + mState);
		// mtxtPincode.setText("Pincode:" + " " + mPinCode);
		// mtxtMobile.setText("Mobile Number:" + " " + mMobileNubmer);

		// mtxtUserAddress.setText("Address:" + " " + mAddressUser);
		// mtxtUserCity.setText("City:" + " " + mCityUser);
		// mtxtUserState.setText("State:" + " " + mStateUser);
		// mtxtUserPincode.setText("Pincode:" + " " + mPinCodeUser);
		// mtxtUserMobile.setText("Mobile Number:" + " " + mMobileNubmerUser);

		mtxtCategory.setTextColor(getResources().getColor(R.color.pink));
		mtxtSubCategory.setTextColor(getResources().getColor(R.color.pink));
		mtxtTitle.setTextColor(getResources().getColor(R.color.pink));
		mtxtDesc.setTextColor(getResources().getColor(R.color.pink));
		mtxtStuff.setTextColor(getResources().getColor(R.color.pink));
		mtxtInstructions.setTextColor(getResources().getColor(R.color.pink));
		mtxtDailyCost.setTextColor(getResources().getColor(R.color.pink));
		mtxtMonthCost.setTextColor(getResources().getColor(R.color.pink));
		mtxtQuanity.setTextColor(getResources().getColor(R.color.pink));
		mtxtWeekCost.setTextColor(getResources().getColor(R.color.pink));
		mtxtSecurityDeposit.setTextColor(getResources().getColor(R.color.pink));
		mtxtPurchasedCost.setTextColor(getResources().getColor(R.color.pink));
		mtxtRating.setTextColor(getResources().getColor(R.color.pink));

		String address = "<font color='black'>Address: </font> <font color='#EC016D'>"
				+ mAddress + "</font>";
		mtxtAddress.setText(Html.fromHtml(address),
				TextView.BufferType.SPANNABLE);

		String city = "<font color='black'>City: </font> <font color='#EC016D'>"
				+ mCity + "</font>";
		mtxtCity.setText(Html.fromHtml(city), TextView.BufferType.SPANNABLE);

		String state = "<font color='black'>State: </font> <font color='#EC016D'>"
				+ mState + "</font>";
		mtxtState.setText(Html.fromHtml(state), TextView.BufferType.SPANNABLE);

		String pib = "<font color='black'>Pincode: </font> <font color='#EC016D'>"
				+ mPinCode + "</font>";
		mtxtPincode.setText(Html.fromHtml(pib), TextView.BufferType.SPANNABLE);

		String mob = "<font color='black'>Mobile Number: </font> <font color='#EC016D'>"
				+ mMobileNubmer + "</font>";
		mtxtMobile.setText(Html.fromHtml(mob), TextView.BufferType.SPANNABLE);

		String addressUser = "<font color='black'>Address: </font> <font color='#EC016D'>"
				+ mAddressUser + "</font>";
		mtxtUserAddress.setText(Html.fromHtml(addressUser),
				TextView.BufferType.SPANNABLE);

		String cityUser = "<font color='black'>City: </font> <font color='#EC016D'>"
				+ mCityUser + "</font>";
		mtxtUserCity.setText(Html.fromHtml(cityUser),
				TextView.BufferType.SPANNABLE);

		String stateUser = "<font color='black'>State: </font> <font color='#EC016D'>"
				+ mStateUser + "</font>";
		mtxtUserState.setText(Html.fromHtml(stateUser),
				TextView.BufferType.SPANNABLE);

		String pinUser = "<font color='black'>Pincode: </font> <font color='#EC016D'>"
				+ mPinCodeUser + "</font>";
		mtxtUserPincode.setText(Html.fromHtml(pinUser),
				TextView.BufferType.SPANNABLE);

		String mobUser = "<font color='black'>Mobile Number: </font> <font color='#EC016D'>"
				+ mMobileNubmerUser + "</font>";
		mtxtUserMobile.setText(Html.fromHtml(mobUser),
				TextView.BufferType.SPANNABLE);

		if (showCurrentAdrees) {
			mLayoutCurrentAddress.setVisibility(View.VISIBLE);
		} else {
			mLayoutCurrentAddress.setVisibility(View.GONE);
		}

		loadControlLaoyuts();

		loadMinRental();
	}

	@SuppressLint("NewApi")
	private void loadControlLaoyuts() {
		mSelectLayout.removeAllViews();
		String mttitle = "";
		if (controlLayouts != null) {
			for (Map.Entry<String, String> entry : controlLayouts.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				final TextView mtxtView = (TextView) LayoutInflater.from(this)
						.inflate(R.layout.showcontrol, null);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						new LayoutParams(LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
				params.setMargins(20, 10, 10, 10);
				mtxtView.setPadding(20, 10, 10, 10);
				mtxtView.setLayoutParams(params);
				if (mttitle.length() == 0)
					mttitle = key;
				else
					mttitle = mttitle + ", " + key;
				String mtxt = "<font color='black'>" + key
						+ " : </font> <font color='#EC016D'>" + value
						+ "</font>";
				mtxtView.setText(Html.fromHtml(mtxt),
						TextView.BufferType.SPANNABLE);
				mSelectLayout.addView(mtxtView);
			}
		}
		if (mttitle.length() == 0) {
			mtxtFields.setVisibility(View.GONE);
		} else {
			mtxtFields.setVisibility(View.VISIBLE);
			mtxtFields.setText(mttitle);
		}
		StaticUtils.expandCollapse(mSelectLayout, true);
	}

	@SuppressLint("NewApi")
	private void loadMinRental() {
		mMinRentalPeriod.removeAllViews();
		String mttitle = "";
		try {
			JSONArray mPricingArray = new JSONArray(mPricingArrayString);
			if (mPricingArray != null) {
				for (int i = 0; i < mPricingArray.length(); i++) {
					JSONObject mObj = mPricingArray.optJSONObject(i);
					if (mObj != null) {
						String key = mObj.optString("UnitTitle");
						String value = mObj.optString("Price");
						final TextView mtxtView = (TextView) LayoutInflater
								.from(this).inflate(R.layout.showcontrol, null);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								new LayoutParams(LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT));
						params.setMargins(20, 10, 10, 10);
						mtxtView.setPadding(20, 10, 10, 10);
						mtxtView.setLayoutParams(params);

						if (key.equalsIgnoreCase("Per WeekDay")) {
							key = "Per Day";
						} else if (key.equalsIgnoreCase("Per 3 Days")) {
							key = "Per Day (Min. 3 Days rent)";
						} else if (key.equalsIgnoreCase("Per Week")) {
							key = "Per Week";
						} else if (key.equalsIgnoreCase("Per Month")) {
							key = "Per Month (Min. Month rent)";
						} else if (key.equalsIgnoreCase("Per 3 Months")) {
							key = "Per Month (Min. 3 Months rent)";
						} else if (key.equalsIgnoreCase("Per 6 Months")) {
							key = "Per Month (Min. 6 Months rent)";
						} else if (key.equalsIgnoreCase("Per 9 Months")) {
							key = "Per Month (Min. 9 Months rent)";
						} else if (key.equalsIgnoreCase("Per 12 Months")) {
							key = "Per Month (Min. 12 Months rent)";
						} else {
						}

						if (mttitle.length() == 0)
							mttitle = key;
						else
							mttitle = mttitle + ", " + key;

						if (value == null || value.length() == 0) {

						} else {
							String mtxt = "<font color='black'>" + key
									+ " : </font> <font color='#EC016D'>"
									+ value + "</font>";
							mtxtView.setText(Html.fromHtml(mtxt),
									TextView.BufferType.SPANNABLE);
							mMinRentalPeriod.addView(mtxtView);
						}

					}
				}
			}
			StaticUtils.expandCollapse(mMinRentalPeriod, true);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void getDetails() {
		mAuthHeader = StorageClass.getInstance(this).getAuthHeader();
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
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
				mRating = mBundle.getString("productCondition");

				mAddress = mBundle.getString("address");
				mCity = mBundle.getString("city");
				mState = mBundle.getString("stateValue");
				mPinCode = mBundle.getString("pincode");
				mPanCard = mBundle.getString("panCard");
				mDrvingNumber = mBundle.getString("drivinglicense");
				mDrvingState = mBundle.getString("drivingState");
				mAadharName = mBundle.getString("aadharCardName");
				mAadthrNumber = mBundle.getString("aadharCardNumber");
				mMobileNubmer = mBundle.getString("mobileNumber");
				mtxtCondName = mBundle.getString("productConditionName");
				controlLayouts = (HashMap<String, String>) mBundle
						.getSerializable("controlLayouts");
				mPricingArrayString = mBundle.getString("pricingArray");
				minimumRentalPeriod = mBundle.getString("minimumRentalPeriod");

				isPanCardSelected = mBundle.getBoolean("isPanCardSelected");
				isAadharCardSelected = mBundle.getBoolean("isAadharSelected");
				isDrivingLicenseSelected = mBundle
						.getBoolean("drivingLicenseSelected");

				InternalApp mApp = (InternalApp) getApplication();
				mArrayFields = mApp.getArray();
				if (mBundle.getString("displayCurrent").equalsIgnoreCase(
						"false")) {
					showCurrentAdrees = false;
				} else {
					showCurrentAdrees = true;
				}

				mAddressUser = mBundle.getString("addressUser");
				mCityUser = mBundle.getString("cityUser");
				mStateUser = mBundle.getString("stateValueUser");
				mPinCodeUser = mBundle.getString("pincodeUser");
				mMobileNubmerUser = mBundle.getString("mobileNumberUser");

			}
		}
		InternalApp mApp = (InternalApp) getApplication();
		ArrayList<CategoryModel> mArray = mApp.getCategoryMappingArray();
		if (mArray != null && mArray.size() > 0) {
			for (int i = 0; i < mArray.size(); i++) {
				CategoryModel mModel = mArray.get(i);
				if (mSubCategory.equalsIgnoreCase(mModel.getTitle())) {
					mCode = mModel.getCode();
				}
			}
		}
	}

	private void loadLayoutReferences() {
		mtxtCategory = (TextView) findViewById(R.id.productCategory);
		mtxtSubCategory = (TextView) findViewById(R.id.subProductCategory);
		mtxtTitle = (TextView) findViewById(R.id.productTitle);
		mtxtDesc = (TextView) findViewById(R.id.productDesc);
		mtxtRating = (TextView) findViewById(R.id.rating);
		mtxtInstructions = (TextView) findViewById(R.id.instructions);
		mtxtStuff = (TextView) findViewById(R.id.stuff);
		mtxtQuanity = (TextView) findViewById(R.id.quantity);
		mtxtPurchasedCost = (TextView) findViewById(R.id.purchasedCost);
		mtxtDailyCost = (TextView) findViewById(R.id.dailyCost);
		mtxtWeekCost = (TextView) findViewById(R.id.weekCost);
		mtxtMonthCost = (TextView) findViewById(R.id.monthCost);
		mtxtSecurityDeposit = (TextView) findViewById(R.id.securityDeposit);
		mbtnSubmit = (TextView) findViewById(R.id.btnSubmit);
		mbtnBack = (TextView) findViewById(R.id.btnEdit);
		mtxtFields = (TextView) findViewById(R.id.txtFields);
		mbtnSubmit.setOnClickListener(this);
		mbtnBack.setOnClickListener(this);
		mSelectLayout = (LinearLayout) findViewById(R.id.layoutControlTypeCapacity);
		mMinRentalPeriod = (LinearLayout) findViewById(R.id.layoutMinRentPeriod);
		mScrollimages = (HorizontalScrollView) findViewById(R.id.scrollImages);
		mLayoutAttachments = (LinearLayout) findViewById(R.id.layoutAttachments);

		mLayoutCurrentAddress = (LinearLayout) findViewById(R.id.layoutCurrentAddress);

		mtxtAddress = (TextView) findViewById(R.id.address);
		mtxtState = (TextView) findViewById(R.id.state);
		mtxtCity = (TextView) findViewById(R.id.city);
		mtxtMobile = (TextView) findViewById(R.id.mobile);
		mtxtPincode = (TextView) findViewById(R.id.pincode);

		mtxtUserAddress = (TextView) findViewById(R.id.addressUser);
		mtxtUserState = (TextView) findViewById(R.id.stateUser);
		mtxtUserCity = (TextView) findViewById(R.id.cityUser);
		mtxtUserMobile = (TextView) findViewById(R.id.mobileUser);
		mtxtUserPincode = (TextView) findViewById(R.id.pincodeUser);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnSubmit:
			btnSubmitClicked();
			break;
		case R.id.btnEdit:
			btnEditClicked();
			break;
		default:
			break;
		}
	}

	private void btnEditClicked() {
		PostAdSaver.getInstance(this).setEditing(true);
		setResult(Activity.RESULT_OK);
		finish();
	}

	private void btnSubmitClicked() {
		if (!UtilNetwork.isOnline(this)) {
			showAlertForNoInternetConnection();
		} else {
			showProgressLayout();

			InternalApp mApp = (InternalApp) getApplication();
			if (mApp.getUriArray() != null && mApp.getUriArray().size() > 0) {
				if (currentImageUploadCount < mApp.getUriArray().size()) {
					startPhotUpload(currentImageUploadCount, mApp);
				}
			}

		}
	}

	private void startPhotUpload(int pos, InternalApp mApp) {
		PhotoUpload mUpload = new PhotoUpload(this,
				mApp.getUriArray().get(pos), mProductAdId, this);
		mUpload.startExexcution();
	}

	private void initPostAdApi() {
		JSONObject params = new JSONObject();
		try {
			params.put("Id", mProductAdId);
			params.put("Title", mAdTitle);
			params.put("Status", "null");
			params.put("Description", mProductDesc);
			params.put("IsLogisticsShared", false);
			params.put("IsInsuredByOwner", false);
			params.put("InsuranceCost", "null");
			params.put("LogisticsCost", "null");
			params.put("SecurityDeposit", mSecurityDeposit);
			params.put("History", "null");
			params.put("CreatedDate", new Date().getTime());
			params.put("Pricing", "null");
			params.put("Business", "null");
			params.put("PricePerDay", mDailyCost);
			int period = Integer.parseInt(minimumRentalPeriod);
			params.put("MinimumRentalPeriod", period);
			params.put("Tags", new JSONArray());

			JSONObject mAdaptParams = new JSONObject();
			mAdaptParams.put("Code", "");
			mAdaptParams.put("ExpiryDate", "null");
			mAdaptParams.put("GeneratedDate", "null");
			mAdaptParams.put("ConsumedDate", "null");
			mAdaptParams.put("OtpStatus", "null");
			params.put("AdOtp", mAdaptParams);

			JSONObject mAddressParams = new JSONObject();
			if (showCurrentAdrees) {
				mAddressParams.put("AddressLine1", mAddressUser);
				mAddressParams.put("AddressLine2", "");
				mAddressParams.put("City", mCityUser);
				mAddressParams.put("State", mStateUser);
				mAddressParams.put("Postcode", mPinCodeUser);
				mAddressParams.put("ContactNumber", mMobileNubmerUser);
				params.put("AdAddress", mAddressParams);
			} else {
				mAddressParams.put("AddressLine1", mAddress);
				mAddressParams.put("AddressLine2", "");
				mAddressParams.put("City", mCity);
				mAddressParams.put("State", mState);
				mAddressParams.put("Postcode", mPinCode);
				mAddressParams.put("ContactNumber", mMobileNubmer);
				params.put("AdAddress", mAddressParams);
			}

			JSONArray mOwnersArray = new JSONArray();
			JSONObject mOwnersObject = new JSONObject();
			JSONObject mLocationObject = new JSONObject();
			mLocationObject.put("Longitude", " ");
			mLocationObject.put("Latitude", " ");
			JSONArray mAddressArray = new JSONArray();
			mAddressArray.put(mAddressParams);
			mOwnersObject.put("Addresses", mAddressArray);
			mOwnersObject.put("Location", mLocationObject);
			mOwnersObject.put("AddressType", "null");
			mOwnersObject.put("Role", "null");
			mOwnersObject.put("MiddleName", "null");
			mOwnersObject.put("DateOfBirth", "null");
			mOwnersObject.put("LastName", " ");
			mOwnersObject.put("IsBusiness", "null");
			mOwnersObject.put("IsPrimary", "null");
			mOwnersArray.put(mOwnersObject);
			params.put("Owners", mOwnersArray);

			JSONObject mProdcutsObj = new JSONObject();
			JSONObject mProdcutCategoryObj = new JSONObject();
			mProdcutCategoryObj.put("Code", mCode);
			mProdcutCategoryObj.put("Title", mSubCategory);

			JSONObject mProdcutConditionObj = new JSONObject();
			mProdcutConditionObj.put("Code", mRating);
			mProdcutConditionObj.put("Title", mtxtCondName);
			mProdcutsObj.put("ProductCondition", mProdcutConditionObj);
			mProdcutsObj.put("Id", mProductAdId);
			mProdcutsObj.put("Title", mAdTitle);
			mProdcutsObj.put("Description", mProductDesc);
			mProdcutsObj.put("Quantity", mQuantity);
			mProdcutsObj.put("PriceWhenPurchased", mProductPurchasedPrice);
			mProdcutsObj.put("ProductCategory", mProdcutCategoryObj);

			if (mDailyCost.length() == 0 || mDailyCost.equalsIgnoreCase("0")) {
				if (mWeekCost.length() == 0 || mWeekCost.equalsIgnoreCase("0")) {
					if (mMonthCost.length() == 0
							|| mMonthCost.equalsIgnoreCase("0")) {

					} else {
						int weekCost = Integer.parseInt(mMonthCost);
						int Value = weekCost / 30;
						mDailyCost = String.valueOf(Math.round(Value));
					}
				} else {
					int weekCost = Integer.parseInt(mWeekCost);
					int Value = weekCost / 7;
					mDailyCost = String.valueOf(Math.round(Value));
				}
			}

			JSONArray mPricingArray = new JSONArray(mPricingArrayString);
			mProdcutsObj.put("Pricing", mPricingArray);

			JSONArray mItemMediaArray = new JSONArray();
			JSONObject mItemMediaObj = new JSONObject();
			mItemMediaObj.put("FilePath", filePath);
			mItemMediaObj.put("FileAbsolutePath", absFilePath);
			mItemMediaObj.put("FileName", "null");
			mItemMediaObj.put("IsThumbNail", "null");
			mItemMediaObj.put("IsCoverImage", uploadCoverImage);
			mItemMediaObj.put("AdItemId", mProductAdId);
			mItemMediaObj.put("Size", "null");
			mItemMediaArray.put(mItemMediaObj);
			if (mItemsMediaArrayResponse != null)
				mProdcutsObj.put("ItemMedia", mItemsMediaArrayResponse);
			else
				mProdcutsObj.put("ItemMedia", mItemMediaArray);

			JSONArray mItemDeatilsArray = new JSONArray();
			if (controlLayouts != null) {
				for (Map.Entry<String, String> entry : controlLayouts
						.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					JSONObject mItemDeatilsObj = new JSONObject();
					String type = "";
					if (mArrayFields != null) {
						for (int k = 0; k < mArrayFields.size(); k++) {
							PostAdModel mObjModel = mArrayFields.get(k);
							if (mObjModel != null) {
								if (mObjModel.getFieldTitle().equalsIgnoreCase(
										key)) {
									if (mObjModel.getValues() == null) {
										if (mObjModel.getValues().length() == 0)
											type = "text";
										else
											type = "select";
									} else {
										type = "text";
									}
								}
							}
						}
					}
					mItemDeatilsObj.put("Title", key);
					mItemDeatilsObj.put("Type", type);
					mItemDeatilsObj.put("Value", value);
					mItemDeatilsArray.put(mItemDeatilsObj);
				}
			}
			mProdcutsObj.put("ItemDetails", mItemDeatilsArray.toString());
			JSONArray mProdcutsArray = new JSONArray();
			mProdcutsArray.put(mProdcutsObj);

			JSONArray mServiceInputs = new JSONArray();
			JSONObject mServicesObject = new JSONObject();
			JSONObject mserviceField = new JSONObject();

			if (isPanCardSelected) {
				mserviceField.put("panId", mPanCard);
			} else if (isAadharCardSelected) {
				mserviceField.put("strAadhaar", mAadthrNumber);
				mserviceField.put("strAadhaarName", mAadharName);
			} else if (isDrivingLicenseSelected) {
				mserviceField.put("driverlicenseid", mDrvingNumber);
				mserviceField.put("state", mDrvingState);
			} else {
				if (mPanCard.length() == 0) {
					if (mAadthrNumber.length() == 0) {
						if (mDrvingNumber.length() == 0) {
							mserviceField.put("panId", mPanCard);
						} else {
							mserviceField.put("driverlicenseid", mDrvingNumber);
							mserviceField.put("state", mDrvingState);
						}
					} else {
						mserviceField.put("strAadhaar", mAadthrNumber);
						mserviceField.put("strAadhaarName", mAadharName);
					}
				} else {
					mserviceField.put("panId", mPanCard);
				}
			}

			JSONObject mserviceProvider = new JSONObject();
			if (isPanCardSelected) {
				mserviceProvider.put("Code", "PAN");
				mserviceProvider.put("Title", "null");
			} else if (isAadharCardSelected) {
				mserviceProvider.put("Code", "AADHAAR");
				mserviceProvider.put("Title", "null");
			} else if (isDrivingLicenseSelected) {
				mserviceProvider.put("Code", "DL");
				mserviceProvider.put("Title", "null");
			} else {
				if (mPanCard.length() == 0) {
					if (mAadthrNumber.length() == 0) {
						if (mDrvingNumber.length() == 0) {
							mserviceProvider.put("Code", "PAN");
							mserviceProvider.put("Title", "null");
						} else {
							mserviceProvider.put("Code", "DL");
							mserviceProvider.put("Title", "null");
						}
					} else {
						mserviceProvider.put("Code", "AADHAAR");
						mserviceProvider.put("Title", "null");
					}
				} else {
					mserviceProvider.put("Code", "PAN");
					mserviceProvider.put("Title", "null");
				}
			}
			mServicesObject.put("TpFieldJson", mserviceField.toString());
			mServicesObject.put("AdId", mProductAdId);
			mServicesObject.put("TpProviderService", mserviceProvider);
			mServiceInputs.put(mServicesObject);

			JSONArray mCalenders = new JSONArray();
			JSONObject mCalendarsObj = new JSONObject();
			mCalendarsObj.put("DateFrom", "1900-01-01T00:00:00");
			mCalendarsObj.put("DateTo", "1900-01-01T00:00:00");
			mCalendarsObj.put("IsBlocked", false);
			mCalendarsObj.put("RentalValuePerDay", mDailyCost);
			mCalendarsObj.put("IsOpenCalendar", true);
			mCalendarsObj.put("IsOutForRent", false);
			mCalendarsObj.put("IsActive", true);
			mCalenders.put(mCalendarsObj);

			JSONArray mArrayAdSettings = new JSONArray();
			JSONObject mAdSetObj = new JSONObject();
			JSONObject mProducSpec = new JSONObject();
			mProducSpec.put("Code", "RULES");
			mProducSpec.put("Title", "Rules of usage");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", mUserInstructions);
			mAdSetObj.put("Id", "2");
			mArrayAdSettings.put(mAdSetObj);

			mAdSetObj = new JSONObject();
			mProducSpec = new JSONObject();
			mProducSpec.put("Code", "INTHEBOX");
			mProducSpec.put("Title", "In the box");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", mAdditionalStuff);
			mAdSetObj.put("Id", "1");
			mArrayAdSettings.put(mAdSetObj);

			mAdSetObj = new JSONObject();
			mProducSpec = new JSONObject();
			mProducSpec.put("Code", "location");
			mProducSpec.put("Title", "Location Preferences");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", "Hyderabad");
			mAdSetObj.put("Id", "null");
			mAdSetObj.put("id", "5");
			mArrayAdSettings.put(mAdSetObj);

			mAdSetObj = new JSONObject();
			mProducSpec = new JSONObject();
			mProducSpec.put("Code", "INSURANCE");
			mProducSpec.put("Title", "Insurance Preferences");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", true);
			mAdSetObj.put("Id", "4");
			mArrayAdSettings.put(mAdSetObj);

			mAdSetObj = new JSONObject();
			mProducSpec = new JSONObject();
			mProducSpec.put("Code", "LOGISTICS");
			mProducSpec.put("Title", "Logistics Preferences");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", "SabRentKaro shipping services");
			mAdSetObj.put("Id", "3");
			mArrayAdSettings.put(mAdSetObj);

			mAdSetObj = new JSONObject();
			mProducSpec = new JSONObject();
			mProducSpec.put("Title", "Calendar  Preferences");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", "Open Calendar");
			mAdSetObj.put("Id", "6");
			mArrayAdSettings.put(mAdSetObj);

			params.put("AdSettings", mArrayAdSettings);
			params.put("AdCalendars", mCalenders);
			params.put("AdTpServiceInputs", mServiceInputs);
			params.put("Products", mProdcutsArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(ApiUtils.POSTANAD,
				params, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						showToast("Success");
						showAlert();
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						showToast("Failure");
					}
				}) {

			public String getBodyContentType() {
				return "application/json; charset=" + getParamsEncoding();
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("x-auth", mAuthHeader);
				map.put("Accept", "application/json");
				map.put("Content-Type", "application/json; charset=UTF-8");
				return map;
			}

		};

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);
	}

	private void showAlert() {
		new AlertDialog.Builder(this)
				.setTitle("Success")
				.setMessage("Product Posted Successfully!")
				.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog.cancel();
						navigateToHome();
					}
				})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
	}

	private void navigateToHome() {
		InternalApp application = (InternalApp) getApplication();
		application.setUriArray(new ArrayList<Uri>());
		Intent mIntent = new Intent(this, HomeActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
	}

	private void callPostAdApi(String ent) {
		PostAd mObj = new PostAd(this, ent, this);
		mObj.startExexcution();
	}

	@Override
	public void onImageUpload(String message) {
		if (message.equalsIgnoreCase("Image uploaded successfully.")) {
			InternalApp mApplication = (InternalApp) getApplication();
			JSONArray mResponseArray = mApplication.getPhotoUpload();
			if (mResponseArray != null && mResponseArray.length() > 0) {
				currentImageCount++;
				mItemsMediaArrayResponse = mResponseArray;
				JSONObject mObj = mResponseArray.optJSONObject(0);
				if (mObj != null) {
					filePath = mObj.optString("Filepath");
					absFilePath = mObj.optString("FileAbsolutePath");
					uploadCoverImage = mObj.optBoolean("IsCoverImage");
				}
				if (currentImageCount == mUriArray.size()) {
					initPostAdApi();
				} else {
					currentImageUploadCount = currentImageUploadCount + 1;
					startPhotUpload(currentImageUploadCount, mApplication);
				}
			}
		} else {
			showToast(message);
			hideProgressLayout();
		}

	}

	@Override
	public void onAdPosted(String message) {
		showToast(message);
		hideProgressLayout();
	}

	@SuppressLint("NewApi")
	private void loadAttachments() {
		mLayoutAttachments.removeAllViews();
		mUriArray = ((InternalApp) getApplication()).getUriArray();
		if (mUriArray != null) {
			for (int i = 0; i < mUriArray.size(); i++) {
				Uri mUri = mUriArray.get(i);
				if (mUri != null) {
					final LinearLayout mPhotoView = (LinearLayout) LayoutInflater
							.from(this).inflate(R.layout.layoutphoto, null);
					final SquareImageView mImageView = (SquareImageView) mPhotoView
							.findViewById(R.id.imgProduct);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							new LayoutParams(300, 300));
					params.setMargins(10, 10, 10, 10);
					mImageView.setLayoutParams(params);

					String mime = getMimeType(mUri);
					int xy = (int) (200 * getResources().getDimension(
							R.dimen.one_dp));
					Point size = new Point(xy, xy);
					if (mime != null && mime.startsWith("image")) {
						Bitmap bmp = getResizedBitmap(mUri, size);
						if (bmp == null) {
							mImageView.setImageURI(mUri);
						} else {
							mImageView.setImageBitmap(bmp);
						}
					}
					mImageView.setTag(mUri);
					mLayoutAttachments.addView(mPhotoView, 300, 300);
				}
			}
		}
		StaticUtils.expandCollapse(mScrollimages, true);
		// StaticUtils.expandCollapse(mLayoutAttachments, true);
	}

	private Bitmap getBitmap(Uri uri) {
		InputStream in = null;
		try {
			final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
			in = getContentResolver().openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth
					+ ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = getContentResolver().openInputStream(uri);
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = b.getHeight();
				int width = b.getWidth();
				Log.d("", "1th scale operation dimenions - width: " + width
						+ ", height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
						(int) y, true);
				b.recycle();
				b = scaledBitmap;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			Log.d("",
					"bitmap size - width: " + b.getWidth() + ", height: "
							+ b.getHeight());
			return b;
		} catch (IOException e) {
			Log.e("", e.getMessage(), e);
			return null;
		}
	}

	private String getMimeType(Uri file) {
		String contentType;
		String ext;
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		if ("file".equalsIgnoreCase(file.getScheme())) {
			String filePath = file.getPath();
			ext = filePath.substring(filePath.lastIndexOf(".") + 1);
			contentType = mime.getMimeTypeFromExtension(ext);
		} else {
			contentType = getContentResolver().getType(file);
			// ext = mime.getExtensionFromMimeType(contentType);
		}
		return contentType;
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor == null)
			return null;
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String s = cursor.getString(column_index);
		cursor.close();
		return s;
	}

	private Bitmap getResizedBitmap(Uri file, Point size) {
		// First decode with inJustDecodeBounds=true to check dimensions
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if ("file".equalsIgnoreCase(file.getScheme())) {
			BitmapFactory.decodeFile(file.getPath(), options);
		} else {
			InputStream stream = null;
			try {
				stream = getContentResolver().openInputStream(file);
			} catch (IOException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
			if (stream != null) {
				BitmapFactory.decodeStream(stream, null, options);
			}
		}
		Bitmap bmp = null;
		if (options.outHeight > 0 && options.outWidth > 0) {
			// Calculate inSampleSize
			options.inSampleSize = StaticUtils.calculateInSampleSize(options,
					size.x, size.y);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			try {
				if ("file".equalsIgnoreCase(file.getScheme())) {
					bmp = BitmapFactory.decodeFile(file.getPath(), options);
				} else {
					InputStream stream = null;
					try {
						stream = getContentResolver().openInputStream(file);
					} catch (IOException e) {
						Log.e(this.getClass().getName(), e.getMessage());
					}
					if (stream != null) {
						bmp = BitmapFactory.decodeStream(stream, null, options);
					}
				}
			} catch (OutOfMemoryError oome) {
				Log.e("Out Of Memory Error",
						oome.getMessage() == null ? "OutOfMemory error: size "
								+ size.x + "x" + size.y : oome.getMessage());
			}
		}
		return bmp;
	}

}