package com.sabrentkaro.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.sabrentkaro.login.LoginActivity;
import com.squareup.picasso.Picasso;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class ProductDetailsActivity extends BaseActivity {

	private ImageView mImageProduct, mImageRating;
	private TextView mtxtProductName, mtxtCategory, mtxtLocation,
			mtxtDailyCost, mtxtMonthCost, mtxtWeekCost, mtxtSecurityDeposit,
			mtxtYearOfPurchase, mtxtMonthOfPurchase, mtxtQuantity,
			mtxtProductTitle, mtxtRating;
	private EditText mEditQuantity;
	private TextView mbtnRent;
	private String selectedProductAdId;

	private String mType, mModel, mBrand, mCapacity, mQuantity, mCategory,
			mProductCategory, mYearOfPurchase, mMonthOfPurchase, mDailyCost,
			locationValue = "", productConditionValue, mWeekCost, mMonthlyCost,
			mTonnage, mPTitle = "";
	private String mStrSecurityDeposit;
	private LinearLayout mLayoutWeekCost, mLayoutMonthCost, mLayoutDailyCost;
	private String mImageUrl;
	private LinearLayout mLayoutFiedlsValues;
	private ImageLoader mImageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_product_details);
		getDetails();
		loadLayoutReferences();
		initProductDetailsApi();
	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			selectedProductAdId = mBundle.getString("selectedProductAdId");
		}
	}

	private void initProductDetailsApi() {
		showProgressLayout();

		JSONObject params = new JSONObject();

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.LOADADDETAILS, params, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						responseForProductDetailsApi(response);
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						showToast(error.toString());
					}

				}) {

			@Override
			public byte[] getBody() {
				super.getBody();
				return selectedProductAdId.getBytes();
			}

		};

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);

	}

	private void loadLayoutReferences() {
		mtxtProductName = (TextView) findViewById(R.id.txtProductName);
		mtxtCategory = (TextView) findViewById(R.id.txtCategoryName);
		mtxtLocation = (TextView) findViewById(R.id.txtLocationName);
		mtxtDailyCost = (TextView) findViewById(R.id.txtDailyCost);
		mtxtWeekCost = (TextView) findViewById(R.id.txtWeekCost);
		mtxtMonthCost = (TextView) findViewById(R.id.txtMonthCost);
		mtxtSecurityDeposit = (TextView) findViewById(R.id.txtSecurityDeposit);
		mtxtYearOfPurchase = (TextView) findViewById(R.id.txtYearOfPurchase);
		mtxtMonthOfPurchase = (TextView) findViewById(R.id.txtMonthOfPurchase);
		mImageRating = (ImageView) findViewById(R.id.imgProductRating);
		mImageProduct = (ImageView) findViewById(R.id.itemProduct);
		mEditQuantity = (EditText) findViewById(R.id.editTextQuantity);
		mtxtQuantity = (TextView) findViewById(R.id.txtQuantity);
		mbtnRent = (TextView) findViewById(R.id.btnRentProduct);
		mLayoutMonthCost = (LinearLayout) findViewById(R.id.layoutMonthCost);
		mLayoutWeekCost = (LinearLayout) findViewById(R.id.layoutWeekCost);
		mLayoutDailyCost = (LinearLayout) findViewById(R.id.layoutDailyCost);
		mLayoutFiedlsValues = (LinearLayout) findViewById(R.id.rootFieldsValues);
		mtxtProductTitle = (TextView) findViewById(R.id.txtProductTitle);
		mtxtRating = (TextView) findViewById(R.id.txtRating);
		mLayoutFiedlsValues.setVisibility(View.GONE);
		mbtnRent.setOnClickListener(this);
		StaticUtils.setEditTextHintFont(mEditQuantity, this);
	}

	private void responseForProductDetailsApi(JSONObject response) {
		if (response != null) {
			mStrSecurityDeposit = response.optString("SecurityDeposit");
			mPTitle = response.optString("Title");
			Double mSD = response.optDouble("SecurityDeposit");
			mStrSecurityDeposit = String.valueOf((int) Math.round(mSD));
			JSONArray productsArray = response.optJSONArray("Products");
			if (productsArray != null) {
				for (int i = 0; i < productsArray.length(); i++) {
					JSONObject mObj = productsArray.optJSONObject(i);
					if (mObj != null) {
						String mitemStr = mObj.optString("ItemDetails");
						JSONArray mItemDetailsArray = null;
						try {
							mItemDetailsArray = new JSONArray(mitemStr);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (mItemDetailsArray != null) {
							loadProductValues(mItemDetailsArray);
						}
						mQuantity = mObj.optString("Quantity");
						mMonthOfPurchase = mObj.optString("MonthOfPurchase");
						mYearOfPurchase = mObj.optString("YearOfPurchase");
						JSONObject mCategoryObj = mObj
								.optJSONObject("Category");
						if (mCategoryObj != null) {
							mCategory = mCategoryObj.optString("Title");
						}

						JSONObject mProductCategoryObj = mObj
								.optJSONObject("ProductCategory");
						if (mProductCategoryObj != null) {
							mProductCategory = mProductCategoryObj
									.optString("Title");
						}

						productConditionValue = mObj.optJSONObject(
								"ProductCondition").optString("Code");

						JSONArray mMediaArray = mObj.optJSONArray("ItemMedia");
						if (mMediaArray != null) {
							for (int k = 0; k < mMediaArray.length(); k++) {
								JSONObject mMediaObj = mMediaArray
										.optJSONObject(k);
								if (mMediaObj != null) {
									mImageUrl = mMediaObj.optString("Filepath")
											.toString();
									mImageUrl = mImageUrl.toString().replace(
											"\\", "/");
								}
							}
						}

						JSONArray mPricingArray = mObj.optJSONArray("Pricing");
						String mPricePerday = "";
						String mPricePerWeek = "";
						String mPriceMonth = "";
						if (mPricingArray != null) {
							for (int l = 0; l < mPricingArray.length(); l++) {
								JSONObject mPriceObj = mPricingArray
										.optJSONObject(l);
								if (mPriceObj != null) {
									if (mPriceObj.optString("UnitCode")
											.equalsIgnoreCase("PerWeekDay")) {
										Double mPrice = mPriceObj
												.optDouble("Price");
										if (mPrice > 0) {
											mPricePerday = String
													.valueOf((int) Math
															.round(mPrice));
										}
									}
									if (mPriceObj.optString("UnitCode")
											.equalsIgnoreCase("PerWeek")) {
										Double mPrice = mPriceObj
												.optDouble("Price");
										if (mPrice > 0) {
											mPricePerWeek = String
													.valueOf((int) Math
															.round(mPrice));
										}
									}
									if (mPriceObj.optString("UnitCode")
											.equalsIgnoreCase("PerMonth")) {
										Double mPrice = mPriceObj
												.optDouble("Price");

										if (mPrice > 0) {
											mPriceMonth = String
													.valueOf((int) Math
															.round(mPrice));
										}
									}
								}
							}
						}
						if (TextUtils.isEmpty(mPricePerday)) {
							mDailyCost = "";
							if (TextUtils.isEmpty(mPricePerWeek)) {
								mWeekCost = "";
								if (TextUtils.isEmpty(mPriceMonth)) {
									mMonthlyCost = "";
								} else {
									mMonthlyCost = mPriceMonth;
								}
							} else {
								mWeekCost = mPricePerWeek;
							}
						} else {
							mDailyCost = mPricePerday;
						}

						JSONArray adSettingsArray = response
								.optJSONArray("AdSettings");
						if (adSettingsArray != null) {
							for (int m = 0; m < adSettingsArray.length(); m++) {
								JSONObject mAdSettingsObj = adSettingsArray
										.optJSONObject(m);
								if (mAdSettingsObj != null) {
									if (mAdSettingsObj
											.optJSONObject(
													"ProductCategorySpecification")
											.optString("Code").toString()
											.equalsIgnoreCase("LOCATION")) {
										if (locationValue.length() == 0)
											locationValue = mAdSettingsObj
													.optString("Value");
										else {
											locationValue = locationValue
													+ ", "
													+ mAdSettingsObj
															.optString("Value");
										}
									}
								}
							}
						}

					}
				}

			}
		}
		setData();
	}

	private void loadProductValues(JSONArray mItemDetailsArray) {
		for (int j = 0; j < mItemDetailsArray.length(); j++) {
			JSONObject mItemObj = mItemDetailsArray.optJSONObject(j);
			if (mItemObj != null) {
				View mView = LayoutInflater.from(this).inflate(
						R.layout.customproducts, null);

				TextView mtxtTitle = (TextView) mView
						.findViewById(R.id.txtPTitle);
				TextView mtxtValue = (TextView) mView
						.findViewById(R.id.txtPValue);

				mtxtTitle.setText(mItemObj.optString("Title"));
				mtxtValue.setText(mItemObj.optString("Value"));

				if (mItemObj.optString("title") == null
						|| mItemObj.optString("title").contains("null")
						|| mItemObj.optString("title").length() == 0) {

				} else {
					mLayoutFiedlsValues.addView(mView);
				}

				if (mItemObj.optString("Title").equalsIgnoreCase("Brand")) {
					mBrand = mItemObj.optString("Value");
				} else if (mItemObj.optString("Title")
						.equalsIgnoreCase("Model")) {
					mModel = mItemObj.optString("Value");
				} else if (mItemObj.optString("Title").equalsIgnoreCase("Type")) {
					mType = mItemObj.optString("Value");
				} else if (mItemObj.optString("Title").equalsIgnoreCase(
						"Storage Volume")) {
					mCapacity = mItemObj.optString("Value");
				} else if (mItemObj.optString("Title").equalsIgnoreCase(
						"Tonnage")) {
					mTonnage = mItemObj.optString("Value");
				}
			}
		}

		StaticUtils.expandCollapse(mLayoutFiedlsValues, true);
	}

	private void setData() {
		mtxtProductName.setText(mProductCategory);
		mtxtCategory.setText(mCategory);
		mtxtYearOfPurchase.setText(mYearOfPurchase);
		mtxtMonthOfPurchase.setText(mMonthOfPurchase);
		mtxtQuantity.setText(mQuantity);
		mtxtProductTitle.setText(mPTitle);
		mtxtLocation.setText(locationValue);
		mtxtLocation.setSelected(true);
		mtxtSecurityDeposit.setText(mStrSecurityDeposit);

		if (TextUtils.isEmpty(mDailyCost)) {
			mLayoutDailyCost.setVisibility(View.GONE);
		} else {
			mtxtDailyCost.setText(mDailyCost);
			mLayoutDailyCost.setVisibility(View.VISIBLE);
		}

		if (TextUtils.isEmpty(mWeekCost)) {
			mLayoutWeekCost.setVisibility(View.GONE);
		} else {
			mtxtWeekCost.setText(mWeekCost);
			int weekPrice = Integer.parseInt(mWeekCost);
			int dayPrice = weekPrice / 7;
			mDailyCost = String.valueOf(dayPrice);
			mLayoutWeekCost.setVisibility(View.VISIBLE);
		}

		if (TextUtils.isEmpty(mMonthlyCost)) {
			mLayoutMonthCost.setVisibility(View.GONE);
		} else {
			mtxtMonthCost.setText(mMonthlyCost);
			int monthPrice = Integer.parseInt(mMonthlyCost);
			int dayPrice = monthPrice / 30;
			mDailyCost = String.valueOf(dayPrice);
			mLayoutMonthCost.setVisibility(View.VISIBLE);
		}

		if (TextUtils.isEmpty(productConditionValue)
				|| productConditionValue.equalsIgnoreCase("0")) {
			mImageRating.setImageResource(R.drawable.zerorating);
			mtxtRating.setText("");
		} else {
			if (productConditionValue.equalsIgnoreCase("1")) {
				mImageRating.setImageResource(R.drawable.onerating);
				mtxtRating.setText("Working");
			} else if (productConditionValue.equalsIgnoreCase("2")) {
				mImageRating.setImageResource(R.drawable.tworating);
				mtxtRating.setText("Average");
			} else if (productConditionValue.equalsIgnoreCase("3")) {
				mImageRating.setImageResource(R.drawable.threerating);
				mtxtRating.setText("Good");
			} else if (productConditionValue.equalsIgnoreCase("4")) {
				mImageRating.setImageResource(R.drawable.fourrating);
				mtxtRating.setText("Very Good");
			} else {
				mImageRating.setImageResource(R.drawable.fiverating);
				mtxtRating.setText("Like New");
			}
		}

		Picasso.with(this).load(mImageUrl)
				.placeholder(R.drawable.default_loading)
				.error(R.drawable.default_loading).into(mImageProduct);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnRentProduct) {
			btnRentProductClicked();
		}
	}

	private void btnRentProductClicked() {
		String mQuanty = mEditQuantity.getText().toString();
		if (TextUtils.isEmpty(mQuanty)) {
			showToast("Please enter a value for quantity");
		} else {
			if (Integer.parseInt(mQuanty) > 0) {
				if (Integer.parseInt(mQuanty) > Integer.parseInt(mQuantity)) {
					showToast("Please enter a value for quantity as per the available quantity");
				} else {
					if (TextUtils.isEmpty(StorageClass.getInstance(this)
							.getUserName())) {
						startLoginActivity();
					} else {
						startSelectDatesActivity();
					}
				}
			} else {
				showToast("Please enter a value for quantity greater than 0 to proceed.");
			}

		}
	}

	private void startSelectDatesActivity() {
		Intent intent = new Intent(this, RentDatesActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedAdId", selectedProductAdId);
		mBundle.putString("productPrice", mDailyCost);
		mBundle.putString("productPriceMonth", mMonthlyCost);
		mBundle.putString("productPriceWeek", mWeekCost);
		mBundle.putString("quantity", mEditQuantity.getText().toString());
		mBundle.putString("productDescription", mBrand + " " + mProductCategory);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	private void startLoginActivity() {
		Intent mIntent = new Intent(this, LoginActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedAdId", selectedProductAdId);
		mBundle.putString("productPrice", mDailyCost);
		mBundle.putString("productPriceMonth", mMonthlyCost);
		mBundle.putString("productPriceWeek", mWeekCost);
		mBundle.putString("quantity", mEditQuantity.getText().toString());
		mBundle.putString("productDescription", mBrand + " " + mProductCategory);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

}
