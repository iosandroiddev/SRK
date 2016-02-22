package com.sabrentkaro.postad;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.ProductModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.PhotoUpload;
import com.utils.PhotoUpload.IImageUpload;
import com.utils.PostAd;
import com.utils.PostAd.IPostAd;
import com.utils.StorageClass;

public class PostAdPreview extends BaseActivity implements IImageUpload,
		IPostAd {

	private TextView mtxtCategory, mtxtSubCategory, mtxtTitle, mtxtDesc,
			mtxtRating, mtxtInstructions, mtxtStuff, mtxtQuanity,
			mtxtPurchasedCost, mtxtDailyCost, mtxtWeekCost, mtxtMonthCost,
			mtxtSecurityDeposit;

	private ImageView mImgProduct;

	private TextView mbtnSubmit, mbtnBack;
	private String filePath, absFilePath;

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

	private String mAadthrNumber = "";
	private String mMobileNubmer = "";
	private String mAadharName = "";
	private String mPinCode = "";
	private String mPanCard = "";
	private String mState = "";
	private String mCity = "";
	private String mAddress = "";
	private String mCode = "";

	private String mtxtCondName;

	private TextView mtxtAddress;

	private TextView mtxtState;

	private TextView mtxtCity;

	private TextView mtxtMobile;

	private TextView mtxtPincode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_ad_preview);
		loadLayoutReferences();
		getDetails();
		setDetails();
	}

	private void setDetails() {
		mtxtCategory.setText(mCategory);
		mtxtSubCategory.setText(mSubCategory);
		mtxtTitle.setText(mAdTitle);
		mtxtDesc.setText(mProductDesc);
		mtxtStuff.setText(mAdditionalStuff);
		mtxtInstructions.setText(mUserInstructions);

		mtxtPurchasedCost.setText(mProductPurchasedPrice);
		mtxtDailyCost.setText(mDailyCost);
		mtxtMonthCost.setText(mMonthCost);
		mtxtQuanity.setText(mQuantity);
		mtxtWeekCost.setText(mWeekCost);
		mtxtSecurityDeposit.setText(mSecurityDeposit);
		mtxtRating.setText(mtxtCondName);

		mtxtAddress.setText(mAddress);
		mtxtCity.setText(mCity);
		mtxtState.setText(mState);
		mtxtPincode.setText(mPinCode);
		mtxtMobile.setText(mMobileNubmer);

		InternalApp mApp = (InternalApp) getApplication();
		mImgProduct.setImageBitmap(mApp.getImage());
	}

	private void getDetails() {
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
				mAadharName = mBundle.getString("aadharCardName");
				mAadthrNumber = mBundle.getString("aadharCardNumber");
				mMobileNubmer = mBundle.getString("mobileNumber");
				mtxtCondName = mBundle.getString("productConditionName");

			}
		}
		InternalApp mApp = (InternalApp) getApplication();
		ArrayList<ProductModel> mArray = mApp.getProductsArray();
		if (mArray != null && mArray.size() > 0) {
			for (int i = 0; i < mArray.size(); i++) {
				ProductModel mModel = mArray.get(i);
				if (mCategory.equalsIgnoreCase(mModel.getTitle())) {
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
		mImgProduct = (ImageView) findViewById(R.id.imgProduct);
		mbtnSubmit = (TextView) findViewById(R.id.btnSubmit);
		mbtnBack = (TextView) findViewById(R.id.btnEdit);
		mbtnSubmit.setOnClickListener(this);
		mbtnBack.setOnClickListener(this);

		mtxtAddress = (TextView) findViewById(R.id.address);
		mtxtState = (TextView) findViewById(R.id.state);
		mtxtCity = (TextView) findViewById(R.id.city);
		mtxtMobile = (TextView) findViewById(R.id.mobile);
		mtxtPincode = (TextView) findViewById(R.id.pincode);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnSubmit:
			btnSubmitClicked();
			break;

		default:
			break;
		}
	}

	private void btnSubmitClicked() {
		showProgressLayout();
		PhotoUpload mUpload = new PhotoUpload(this, mFilePath, mProductAdId,
				this);
		mUpload.startExexcution();
	}

	private void initPostAdApi() {

		JSONObject params = new JSONObject();
		try {
			params.put("Id", mProductAdId);
			params.put("Title", mAdTitle);
			params.put("Description", mProductDesc);
			params.put("IsLogisticsShared", "true");
			params.put("IsInsuredByOwner", "false");
			params.put("InsuranceCost", "null");
			params.put("LogisticsCost", "null");
			params.put("SecurityDeposit", mSecurityDeposit);
			params.put("History", "null");
			params.put("CreatedDate", "1455962224412");
			params.put("Pricing", "null");
			params.put("Business", "null");
			params.put("MinimumRentalPeriod", "null");
			params.put("Tags", new JSONArray());

			JSONObject mAdaptParams = new JSONObject();
			mAdaptParams.put("Code", "");
			mAdaptParams.put("ExpiryDate", "null");
			mAdaptParams.put("GeneratedDate", "null");
			mAdaptParams.put("ConsumedDate", "null");
			mAdaptParams.put("OtpStatus", "null");
			params.put("AdOtp", mAdaptParams);

			JSONObject mAddressParams = new JSONObject();
			mAddressParams.put("AddressLine1", mAddress);
			mAddressParams.put("AddressLine2", "");
			mAddressParams.put("City", mCity);
			mAddressParams.put("State", mState);
			mAddressParams.put("Postcode", mPinCode);
			mAddressParams.put("ContactNumber", mMobileNubmer);
			params.put("AdAddress", mAddressParams);

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
			mProdcutsObj.put("Id", mAddressArray);
			mProdcutsObj.put("Title", mLocationObject);
			mProdcutsObj.put("Description", "null");
			mProdcutsObj.put("Quantity", "null");
			mProdcutsObj.put("PriceWhenPurchased", mProductPurchasedPrice);
			mProdcutsObj.put("ProductCategory", mProdcutCategoryObj);
			mProdcutsObj.put("LastName", " ");
			mProdcutsObj.put("IsBusiness", "null");
			mProdcutsObj.put("IsPrimary", "null");

			JSONArray mPricingArray = new JSONArray();
			JSONObject mPricingobject = new JSONObject();
			mPricingobject.put("Price", mDailyCost);
			mPricingobject.put("UnitCode", "PerWeekDay");
			mPricingobject.put("UnitTitle", "Per WeekDay");
			mPricingArray.put(mPricingobject);
			mPricingobject = new JSONObject();
			mPricingobject.put("Price", mWeekCost);
			mPricingobject.put("UnitCode", "PerWeek");
			mPricingobject.put("UnitTitle", "Per Week");
			mPricingArray.put(mPricingobject);
			mPricingobject = new JSONObject();
			mPricingobject.put("Price", mMonthCost);
			mPricingobject.put("UnitCode", "PerMonth");
			mPricingobject.put("UnitTitle", "Per Month");
			mPricingArray.put(mPricingobject);
			mProdcutsObj.put("Pricing", mPricingArray);

			JSONArray mItemMediaArray = new JSONArray();
			JSONObject mItemMediaObj = new JSONObject();
			mItemMediaObj.put("FilePath", filePath);
			mItemMediaObj.put("FileAbsolutePath", absFilePath);
			mItemMediaObj.put("FileName", "null");
			mItemMediaObj.put("IsThumbNail", "null");
			mItemMediaObj.put("IsCoverImage", "null");
			mItemMediaObj.put("AdItemId", mProductAdId);
			mItemMediaObj.put("Size", "null");
			mItemMediaArray.put(mItemMediaObj);
			mProdcutsObj.put("ItemMedia", mItemMediaArray);

			JSONArray mItemDeatilsArray = new JSONArray();
			JSONObject mItemDeatilsObj = new JSONObject();
			mItemDeatilsObj.put("Title", "Brand");
			mItemDeatilsObj.put("Type", "");
			mItemDeatilsObj.put("Value", "Brand");
			mItemDeatilsArray.put(mItemDeatilsObj);
			mItemDeatilsObj = new JSONObject();
			mItemDeatilsObj.put("Title", "Model");
			mItemDeatilsObj.put("Type", "");
			mItemDeatilsObj.put("Value", "Model");
			mItemDeatilsArray.put(mItemDeatilsObj);
			mItemDeatilsObj = new JSONObject();
			mItemDeatilsObj.put("Title", "");
			mItemDeatilsObj.put("Type", "");
			mItemDeatilsObj.put("Value", "");
			mItemDeatilsArray.put(mItemDeatilsObj);
			mItemDeatilsObj = new JSONObject();
			mItemDeatilsObj.put("Title", "");
			mItemDeatilsObj.put("Type", "");
			mItemDeatilsObj.put("Value", "");
			mItemDeatilsArray.put(mItemDeatilsObj);

			mProdcutsObj.put("ItemDetails",
					"" + String.valueOf(mItemDeatilsArray) + "");
			JSONArray mProdcutsArray = new JSONArray();
			mProdcutsArray.put(mProdcutsObj);

			JSONArray mServiceInputs = new JSONArray();
			JSONObject mServicesObject = new JSONObject();
			JSONObject mserviceField = new JSONObject();
			if (mPanCard.length() == 0) {
				mserviceField.put("panId", mPanCard);
			} else {
			}
			JSONObject mserviceProvider = new JSONObject();
			if (mPanCard.length() == 0) {
				mserviceProvider.put("Code", "PAN");
				mserviceProvider.put("Title", "null");
			} else {
			}
			mServicesObject.put("TpFieldJson", mserviceField);
			mServicesObject.put("TpProviderService", mserviceProvider);
			mServiceInputs.put(mServicesObject);

			JSONArray mCalenders = new JSONArray();
			JSONObject mCalendarsObj = new JSONObject();
			mCalendarsObj.put("DateFrom", "01/01/1900");
			mCalendarsObj.put("DateTo", "12/31/2999");
			mCalendarsObj.put("IsBlocked", "false");
			mCalendarsObj.put("RentalValuePerDay", mDailyCost);
			mCalendarsObj.put("IsOpenCalendar", "true");
			mCalenders.put(mCalendarsObj);

			JSONArray mArrayAdSettings = new JSONArray();
			JSONObject mAdSetObj = new JSONObject();
			JSONObject mProducSpec = new JSONObject();
			mProducSpec.put("Code", "RULES");
			mProducSpec.put("Title", "Rules of usage");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", "Instructions");
			mAdSetObj.put("Id", "2");
			mArrayAdSettings.put(mAdSetObj);

			mAdSetObj = new JSONObject();
			mProducSpec = new JSONObject();
			mProducSpec.put("Code", "INTHEBOX");
			mProducSpec.put("Title", "In the box");
			mAdSetObj.put("ProductCategorySpecification", mProducSpec);
			mAdSetObj.put("Value", "Stuff");
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
			mAdSetObj.put("Value", "true");
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
			mAdSetObj.put("Value", "true");
			mAdSetObj.put("Id", "6");
			mArrayAdSettings.put(mAdSetObj);

			params.put("AdSettings", mArrayAdSettings);
			params.put("AdCalendars", mCalenders);
			params.put("AdTpServiceInputs", mServiceInputs);
			params.put("Products", mProdcutsArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		String ent = "{\"Id\":\""
				+ mProductAdId
				+ "\",\"Status\":null,\"Title\":\""
				+ mAdTitle
				+ "\",\"Description\":\""
				+ mProductDesc
				+ "\","
				+ "\"IsLogisticsShared\":false,\"IsInsuredByOwner\":false,\"InsuranceCost\":null,\"LogisticsCost\":null,"
				+ "\"SecurityDeposit\":\""
				+ mSecurityDeposit
				+ "\",\"History\":null,\"CreatedDate\":"
				+ "01/01/1900"
				+ ",\"Owners\":[{\"IsPrimary\":null,"
				+ "\"IsBusiness\":null,\"LastName\":\" \",\"DateOfBirth\":null,\"MiddleName\":null,\"Location\":{\"Longitude\":\" "
				+ "\",\"Latitude\":\" \"},\"Role\":null,\"Addresses\":[{\"AddressLine1\":\""
				+ mAddress
				+ "\",\"AddressLine2\":\"\","
				+ "\"State\":\""
				+ mState
				+ "\",\"City\":\""
				+ mCity
				+ "\",\"Postcode\":\""
				+ mPinCode
				+ "\",\"ContactNumber\":\""
				+ mMobileNubmer
				+ "\"}],"
				+ "\"AddressType\":null}],\"Products\":[{\"Id\":\""
				+ mProductAdId
				+ "\",\"ProductCategory\":{\"Code\":\""
				+ mCode
				+ "\",\"Title\":\""
				+ mSubCategory
				+ "\"},"
				+ "\"Title\":\""
				+ mAdTitle
				+ "\",\"Description\":\""
				+ mProductDesc
				+ "\",\"Quantity\":\""
				+ mQuantity
				+ "\","
				+ "\"ProductCondition\":{\"Code\":\""
				+ mRating
				+ "\",\"Title\":\""
				+ mtxtCondName
				+ "\"},\"PriceWhenPurchased\":\""
				+ mProductPurchasedPrice
				+ "\","
				+ "\"ItemDetails\":\"[{\"Title\":\"Brand\",\"Type\":\"\",\"Value\":\""
				+ mProductDesc
				+ "\"},"
				+ "{\"Title\":\"Model\",\"Type\":\"\",\"Value\":\""
				+ mProductDesc
				+ "\"},{\"Title\":\"Control\","
				+ "\"Type\":\"select\",\"Value\":\"\"},{\"Title\":\"Type\",\"Type\":\"select\",\"Value\":\"\"},"
				+ "{\"Title\":\"Washing Capacity\",\"Type\":\"select\",\"Value\":\"\"}]\",\"ItemMedia\":[{\"Filepath\":"
				+ "\"http://www.allrental.co.in/BusinessServices/ImageStore/AC/818/A/818_71bca978-7de7-4fd5-8c5c-02f04cb8a3c2.png\","
				+ "\"FileAbsolutePath\":\"AC/818/A/818_71bca978-7de7-4fd5-8c5c-02f04cb8a3c2.png\",\"FileName\":null,\"IsThumbNail\":null,"
				+ "\"IsCoverImage\":null,\"AdItemId\":"
				+ mAdTitle
				+ ",\"Size\":null}],"
				+ "\"Pricing\":[{\"Price\":\""
				+ mDailyCost
				+ "\",\"UnitCode\":\"PerWeekDay\",\"UnitTitle\":\"Per WeekDay\"},"
				+ "{\"Price\":\""
				+ mWeekCost
				+ "\",\"UnitCode\":\"PerWeek\",\"UnitTitle\":\"Per Week\"},{\"Price\":\""
				+ mMonthCost
				+ "\",\"UnitCode\":"
				+ "\"PerMonth\",\"UnitTitle\":\"Per Month\"}]}],\"AdAddress\":{\"AddressLine1\":\""
				+ mAddress
				+ "\","
				+ "\"AddressLine2\":\"\",\"City\":\""
				+ mCity
				+ "\",\"State\":\""
				+ mState
				+ "\",\"Postcode\":\""
				+ mPinCode
				+ "\","
				+ "\"ContactNumber\":\""
				+ mMobileNubmer
				+ "\"},\"Pricing\":null,\"AdOtp\":{\"Code\":\"\",\"ExpiryDate\":null,\"GeneratedDate\":null,"
				+ "\"ConsumedDate\":null,\"OtpStatus\":null},\"Tags\":[],\"Business\":null,"
				+ "\"AdSettings\":[{\"ProductCategorySpecification\":{\"Code\":\"RULES\",\"Title\":\"Rules of usage\"},"
				+ "\"Value\":\""
				+ mProductDesc
				+ "\",\"Id\":\"2\"},{\"ProductCategorySpecification\":{\"Code\":\"INTHEBOX\",\"Title\":\"In the box\"},"
				+ "\"Value\":\""
				+ mProductDesc
				+ "\",\"Id\":\"1\"},"
				+ "{\"ProductCategorySpecification\":{\"Code\":\"location\",\"Title\":\"Location Preferences\"},\"Value\":\""
				+ StorageClass.getInstance(this).getUserCity()
				+ "\",\"Id\":\"5\"},"
				+ "{\"ProductCategorySpecification\":{\"Code\":\"LOGISTICS\",\"Title\":\"Logistics Preferences\"},"
				+ "\"Value\":\"\",\"Id\":3},"
				+ "{\"ProductCategorySpecification\":{\"Code\":\"INSURANCE\",\"Title\":\"Insurance Preferences\"},\"Value\":,\"Id\":4},"
				+ "{\"ProductCategorySpecification\":{\"Code\":\"CALENDAR\",\"Title\":\"Calendar Preferences\"},\"Value\":\"\",\"Id\":\"6\"}],"
				+ "\"AdCalendars\":[{\"DateFrom\":\""
				+ "01/01/1900"
				+ "\",\"DateTo\":\""
				+ "12/31/2999"
				+ "\",\"IsBlocked\":false,\"RentalValuePerDay\":\""
				+ mDailyCost
				+ "\",\"IsOpenCalendar\":}],"
				+ "\"AdTpServiceInputs\":[{\"AdId\":null,\"TpFieldJson\":\"{\"panId\":\""
				+ mPanCard
				+ "\"}\",\"TpProviderService\":{\"Code\":\"PAN\",\"Title\":null}}]}";

		callPostAdApi(ent);

		JsonObjectRequest mObjReq = new JsonObjectRequest(ApiUtils.POSTANAD,
				params, new Listener<JSONObject>() {

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
				JSONObject mObj = mResponseArray.optJSONObject(0);
				if (mObj != null) {
					filePath = mObj.optString("Filepath");
					absFilePath = mObj.optString("FileAbsolutePath");
					initPostAdApi();
				}
			}
		} else {

		}
	}

	@Override
	public void onAdPosted(String message) {
		showToast(message);
		hideProgressLayout();
	}

}