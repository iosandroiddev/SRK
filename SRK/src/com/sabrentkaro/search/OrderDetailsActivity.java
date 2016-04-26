package com.sabrentkaro.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.StorageClass;

public class OrderDetailsActivity extends BaseActivity {

	private String selectedProductAdId, mPrice, mProductDescription, mQuantity,
			mStartDate, mEndDate, mLocationId, mMonthPrice, mWeekPrice,
			mAddress, mCity, mState, txtAddress, mAuthHeader, mSecurityDeposit;

	private TextView mtxtTotalCost, mtxtRentalPeriod, mtxtShippingAddress,
			mtxtDescription, mtxtQuantity, mtxtFromDate, mtxtToDate,
			mbtnContinue;

	private TextView mtxtProductRentalValue, mtxtPerDayCost, mtxtPerWeekCost,
			mtxtPerMonthCost, mtxtFacilitaionCharges, mtxtFaciCost,
			mtxtServiceTax, mtxtServiceTaxCost, mtxtLogistics,
			mtxtLogisticsCost, mtxtSecurityDeposit;

	private boolean isPanCardSelected = false, isAadharCardSelected = false,
			isDrivingLicenseSelected = false;
	private String panCard = "", aadharCardname = "", aadharCardNumber = "",
			drivingLicense = "", drivingState = "";

	private String mProdRentValue, mServiceValue, mLogisticsValue,
			mFaciliValue;
	private String mStartDateStr = "";
	private String mStartEndStr = "";
	private String fromDate;
	private String toDate;
	private String mSecurityValue;

	private String mPincode;
	private LinearLayout mLayoutSecurityDeposit;
	private String mMobile;

	private String addressResponse = "";

	private String itemDetailsArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_order_details);
		getDetails();
		loadLayoutReferences();
		loadDetails();
		initOrderDetailsApi();
	}

	private void initOrderDetailsApi() {
		showProgressLayout();

		JSONObject mParams = new JSONObject();
		try {
			String mlocation = StorageClass.getInstance(this).getCity();
			String mLocValue = StorageClass.getInstance(this).getCityValue();
			mParams.put("AdId", selectedProductAdId);
			mParams.put("CouponCode", null);
			mParams.put("FromDate", mStartDate);
			mParams.put("ToDate", mEndDate);
			mParams.put("LocationId", mLocValue);
			mParams.put("Quantity", mQuantity);
			mParams.put("Location", mlocation);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonArrayRequest mRequest = new JsonArrayRequest(
				ApiUtils.CALCULATERENTALPRICING, mParams,
				new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						responseForRentPricingApi(response);
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						showToast(error.toString());
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
		mQueue.add(mRequest);
	}

	private void responseForRentPricingApi(JSONArray response) {
		hideProgressLayout();
		if (response != null) {
			for (int i = 0; i < response.length(); i++) {
				JSONObject mobj = response.optJSONObject(i);
				if (mobj != null) {
					JSONObject mObject = mobj.optJSONObject("PaymentCategory");
					if (mObject != null) {
						String mTitle = mObject.optString("Code");
						if (mTitle.equalsIgnoreCase("PRODRENTALVALUE")) {
							mProdRentValue = mobj.optString("Amount");
							mProdRentValue = String.valueOf(mProdRentValue)
									.split("\\.")[0];
						} else if (mTitle.equalsIgnoreCase("COMMISSION")) {
							mFaciliValue = mobj.optString("Amount");
							mFaciliValue = String.valueOf(mFaciliValue).split(
									"\\.")[0];
						} else if (mTitle.equalsIgnoreCase("SERVICETAX")) {
							mServiceValue = mobj.optString("Amount");
							mServiceValue = String.valueOf(mServiceValue)
									.split("\\.")[0];
						} else if (mTitle.equalsIgnoreCase("LOGISTICS")) {
							mLogisticsValue = mobj.optString("Amount");
							mLogisticsValue = String.valueOf(mLogisticsValue)
									.split("\\.")[0];
						} else if (mTitle.equalsIgnoreCase("SECURITY")) {
							mSecurityValue = mobj.optString("Amount");
							mSecurityValue = String.valueOf(mSecurityValue)
									.split("\\.")[0];
						}
					}
				}
			}
		}
		loadValues();
	}

	private void loadLayoutReferences() {
		mtxtTotalCost = (TextView) findViewById(R.id.txtTotalCost);
		mtxtRentalPeriod = (TextView) findViewById(R.id.txtRentalPeriod);
		mtxtShippingAddress = (TextView) findViewById(R.id.txtShippingAddress);
		mtxtDescription = (TextView) findViewById(R.id.txtDescription);
		mtxtQuantity = (TextView) findViewById(R.id.txtQuantity);
		mtxtFromDate = (TextView) findViewById(R.id.txtFromDate);
		mtxtToDate = (TextView) findViewById(R.id.txtToDate);
		mbtnContinue = (TextView) findViewById(R.id.btnContinue);

		mtxtProductRentalValue = (TextView) findViewById(R.id.txtTotalAmount);
		mtxtPerDayCost = (TextView) findViewById(R.id.txtPerDayCost);
		mtxtPerWeekCost = (TextView) findViewById(R.id.txtPerWeekCost);
		mtxtPerMonthCost = (TextView) findViewById(R.id.txtPerMonthCost);
		mtxtFacilitaionCharges = (TextView) findViewById(R.id.txtFaciliatationCharges);
		mtxtFaciCost = (TextView) findViewById(R.id.txtFecilitationCost);
		mtxtServiceTax = (TextView) findViewById(R.id.txtServiceTax);
		mtxtServiceTaxCost = (TextView) findViewById(R.id.txtServiceTaxCost);
		mtxtLogistics = (TextView) findViewById(R.id.txtLogisticsTax);
		mtxtLogisticsCost = (TextView) findViewById(R.id.txtLogisticsTaxCost);

		mtxtSecurityDeposit = (TextView) findViewById(R.id.txtSecurityDeposit);
		mLayoutSecurityDeposit = (LinearLayout) findViewById(R.id.layoutSecurityDeposit);
		mbtnContinue.setOnClickListener(this);
	}

	private void loadValues() {
		mtxtProductRentalValue.setText(getString(R.string.rupeeone) + " "
				+ mProdRentValue);
		mtxtFacilitaionCharges.setText(getString(R.string.rupeeone) + " "
				+ mFaciliValue);
		mtxtServiceTax.setText(getString(R.string.rupeeone) + " "
				+ mServiceValue);
		mtxtLogistics.setText(getString(R.string.rupeeone) + " "
				+ mLogisticsValue);

		int totalCost = Integer.parseInt(mProdRentValue)
				+ Integer.parseInt(mFaciliValue)
				+ Integer.parseInt(mServiceValue)
				+ Integer.parseInt(mLogisticsValue);

		if (mSecurityDeposit == null || mSecurityDeposit.equalsIgnoreCase("0")
				|| mSecurityDeposit.length() == 0) {
			mLayoutSecurityDeposit.setVisibility(View.GONE);
		} else {
			int value = Integer.parseInt(mSecurityDeposit);
			int quantity = Integer.parseInt(mQuantity);
			totalCost = totalCost + (quantity * value);
			if (quantity > 1) {
				mtxtSecurityDeposit.setText(getString(R.string.rupeeone) + " "
						+ String.valueOf((quantity * value)) + " for "
						+ quantity + " Quantities.");
			} else {
				mtxtSecurityDeposit.setText(mSecurityDeposit);
			}
			mLayoutSecurityDeposit.setVisibility(View.VISIBLE);
		}
		mtxtTotalCost.setText(String.valueOf(totalCost));
	}

	private void loadDetails() {
		Long rentalDays = 0L;
		int days = 0;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy");
			Date startDate = mSimpleDateFormat.parse(mStartDate);
			Date endDate = mSimpleDateFormat.parse(mEndDate);
			rentalDays = endDate.getTime() - startDate.getTime();
			days = (int) (rentalDays / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		mtxtRentalPeriod.setText(String.valueOf(days + 1));
		mtxtShippingAddress.setText(txtAddress);
		mtxtDescription.setText(mProductDescription);
		mtxtQuantity.setText(mQuantity);
		mtxtFromDate.setText(mStartDate);
		mtxtToDate.setText(mEndDate);
		mtxtPerDayCost.setText(mPrice);
		if (mMonthPrice == null || mMonthPrice.contains("null")
				|| mMonthPrice.length() == 0) {

		} else {
			mtxtPerMonthCost.setText(mMonthPrice);
		}
		if (mWeekPrice == null || mWeekPrice.contains("null")
				|| mWeekPrice.length() == 0) {

		} else {
			mtxtPerWeekCost.setText(mWeekPrice);
		}

	}

	private void getDetails() {
		mAuthHeader = StorageClass.getInstance(this).getAuthHeader();
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
				selectedProductAdId = mBundle.getString("selectedAdId");
				mPrice = mBundle.getString("productPrice");
				mProductDescription = mBundle.getString("productDescription");
				mQuantity = mBundle.getString("quantity");
				mLocationId = mBundle.getString("locationId");
				mStartDate = mBundle.getString("selectedStartDate");
				mEndDate = mBundle.getString("selectedEndDate");
				mAddress = mBundle.getString("address");
				mCity = mBundle.getString("city");
				mState = mBundle.getString("state");
				mPincode = mBundle.getString("pincode");
				mMobile = mBundle.getString("mobile");
				mMonthPrice = mBundle.getString("productPriceMonth");
				mWeekPrice = mBundle.getString("productPriceweek");
				txtAddress = mAddress + "\n" + mCity + "\n" + mState + "\n"
						+ mPincode + "\n" + mMobile;
				mSecurityDeposit = mBundle.getString("securitDeposit");

				mStartDateStr = mBundle.getString("startDate");
				mStartEndStr = mBundle.getString("endDate");
				addressResponse = mBundle.getString("mAddressJson");
				itemDetailsArray = mBundle.getString("mItemDetailsArray");

				isPanCardSelected = mBundle.getBoolean("pancardSelected");
				isAadharCardSelected = mBundle
						.getBoolean("isaadharCardSelected");
				isDrivingLicenseSelected = mBundle
						.getBoolean("drivinglicenseSelected");

				panCard = mBundle.getString("panId");
				aadharCardname = mBundle.getString("aadharName");
				aadharCardNumber = mBundle.getString("aadharId");
				drivingLicense = mBundle.getString("drvingLicense");

			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnContinue) {
			initiateRentalApi();
		}
	}

	private void initiateRentalApi() {
		showProgressLayout();
		String inputPattern = "MM/dd/yyyy";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy",
				Locale.US);

		Date date = null;
		String mSDate = null;
		String mEDate = null;
		try {
			date = inputFormat.parse(mStartDate);
			mSDate = outputFormat.format(date);
			date = inputFormat.parse(mEndDate);
			mEDate = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JSONObject params = new JSONObject();
		try {
			params.put("AdId", selectedProductAdId);
			params.put("UserLocationId", StorageClass.getInstance(this)
					.getCityValue());
			params.put("UserLocation", StorageClass.getInstance(this).getCity());
			params.put("Quantity", mQuantity);
			params.put("InitiatedDate", "null");
			params.put("StartDate", mSDate);
			params.put("EndDate", mEDate);
			params.put("RentalType", "Fresh");
			params.put("OrderNo", "null");
			params.put("RenterShippingAddress", "null");
			params.put("RenterBillingAddress", "null");
			params.put("CouponCode", "null");

			JSONArray mStepCount = new JSONArray();
			JSONObject mObjRentalCosts = new JSONObject();
			mObjRentalCosts.put("PaymentFrequency", "null");
			mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("Amount", "");
			mObjRentalCosts.put("IsStatutory", false);
			JSONObject mPaymentCategory = new JSONObject();
			mPaymentCategory.put("Title", "Product Rental Value");
			mPaymentCategory.put("Code", "PRODRENTALVALUE");
			mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			mStepCount.put(mObjRentalCosts);

			mObjRentalCosts = new JSONObject();
			mObjRentalCosts.put("PaymentFrequency", "null");
			mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("Amount", mLogisticsValue);
			mObjRentalCosts.put("IsStatutory", false);
			mPaymentCategory = new JSONObject();
			mPaymentCategory.put("Title", "Logistics Cost");
			mPaymentCategory.put("Code", "LOGISTICS");
			mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			mStepCount.put(mObjRentalCosts);

			mObjRentalCosts = new JSONObject();
			mObjRentalCosts.put("PaymentFrequency", "null");
			mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("Amount", mSecurityDeposit);
			mObjRentalCosts.put("IsStatutory", false);
			mPaymentCategory = new JSONObject();
			mPaymentCategory.put("Title", "Security Deposit");
			mPaymentCategory.put("Code", "SECURITY");
			mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			mStepCount.put(mObjRentalCosts);

			mObjRentalCosts = new JSONObject();
			mObjRentalCosts.put("PaymentFrequency", "null");
			mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("Amount", mFaciliValue);
			mObjRentalCosts.put("IsStatutory", false);
			mPaymentCategory = new JSONObject();
			mPaymentCategory.put("Title", "Facilitation Charges");
			mPaymentCategory.put("Code", "COMMISSION");
			mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			mStepCount.put(mObjRentalCosts);

			mObjRentalCosts = new JSONObject();
			mObjRentalCosts.put("PaymentFrequency", "null");
			mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			mObjRentalCosts.put("Amount", mServiceValue);
			mObjRentalCosts.put("IsStatutory", false);
			mPaymentCategory = new JSONObject();
			mPaymentCategory.put("Title", "Service Tax");
			mPaymentCategory.put("Code", "SERVICETAX");
			mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			mStepCount.put(mObjRentalCosts);
			params.put("StepInput", mStepCount);

			JSONArray mRentalTpServiceInputs = new JSONArray();
			if (isPanCardSelected) {
				JSONObject mRentalTpServiceInputsObj = new JSONObject();
				mRentalTpServiceInputsObj.put("AdId", selectedProductAdId);
				JSONObject mTpFieldJson = new JSONObject();
				mTpFieldJson.put("panId", panCard);
				mRentalTpServiceInputsObj.put("TpFieldJson",
						mTpFieldJson.toString());
				JSONObject mTpProviderService = new JSONObject();
				mTpProviderService.put("Title", "null");
				mTpProviderService.put("Code", "PAN");
				mRentalTpServiceInputsObj.put("TpProviderService",
						mTpProviderService);
				mRentalTpServiceInputsObj.put("RentalId", "0");
				mRentalTpServiceInputsObj.put("UserId", StorageClass
						.getInstance(this).getUserId());
				mRentalTpServiceInputsObj
						.put("Url",
								"http://evoke.jocatagrid.in/Evoke/webservices/ident/getPanInformation");
				mRentalTpServiceInputs.put(mRentalTpServiceInputsObj);
			} else if (isAadharCardSelected) {
				JSONObject mRentalTpServiceInputsObj = new JSONObject();
				mRentalTpServiceInputsObj.put("AdId", selectedProductAdId);
				JSONObject mTpFieldJson = new JSONObject();
				mTpFieldJson.put("strAadhaar", aadharCardNumber);
				mTpFieldJson.put("strAadhaarName", aadharCardname);
				mRentalTpServiceInputsObj.put("TpFieldJson",
						mTpFieldJson.toString());
				JSONObject mTpProviderService = new JSONObject();
				mTpProviderService.put("Title", "null");
				mTpProviderService.put("Code", "AADHAAR");
				mRentalTpServiceInputsObj.put("TpProviderService",
						mTpProviderService);
				mRentalTpServiceInputsObj.put("RentalId", "0");
				mRentalTpServiceInputsObj.put("UserId", StorageClass
						.getInstance(this).getUserId());
				mRentalTpServiceInputsObj
						.put("Url",
								"http://evoke.jocatagrid.in/Evoke/webservices/ident/getAadhaarAuthentication");
				mRentalTpServiceInputs.put(mRentalTpServiceInputsObj);
			} else {
				JSONObject mRentalTpServiceInputsObj = new JSONObject();
				mRentalTpServiceInputsObj.put("AdId", selectedProductAdId);
				JSONObject mTpFieldJson = new JSONObject();
				mTpFieldJson.put("driverlicenseid", drivingLicense);
				mTpFieldJson.put("state", drivingState);
				mRentalTpServiceInputsObj.put("TpFieldJson",
						mTpFieldJson.toString());
				JSONObject mTpProviderService = new JSONObject();
				mTpProviderService.put("Title", "null");
				mTpProviderService.put("Code", "DL");
				mRentalTpServiceInputsObj.put("TpProviderService",
						mTpProviderService);
				mRentalTpServiceInputsObj.put("RentalId", "0");
				mRentalTpServiceInputsObj.put("UserId", StorageClass
						.getInstance(this).getUserId());
				mRentalTpServiceInputsObj
						.put("Url",
								"http://evoke.jocatagrid.in/Evoke/webservices/ident/getDriversLicInfo");
				mRentalTpServiceInputs.put(mRentalTpServiceInputsObj);
			}
			params.put("RentalTpServiceInputs", mRentalTpServiceInputs);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.INITIATERENTAL, params, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						saveResponse(response);

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

	private void saveResponse(JSONObject response) {

		initServicerProvider(response);

	}

	private void initServicerProvider(final JSONObject dataResponse) {
		final String mAuthHeader = StorageClass.getInstance(this)
				.getAuthHeader();
		showProgressLayout();
		JSONObject mTpType = new JSONObject();
		try {
			mTpType.put("Code", "VERIFICATION");
			mTpType.put("Title", "null");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mTpProvider = new JSONObject();
		try {
			mTpProvider.put("Code", "JOCATA");
			mTpProvider.put("Title", "null");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mParams = new JSONObject();
		try {
			mParams.put("UserId", "null");
			mParams.put("TpType", mTpType);
			mParams.put("TpProvider", mTpProvider);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonArrayRequest mRequest = new JsonArrayRequest(
				ApiUtils.GETPROVIDERSERVICES, mParams,
				new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						hideProgressLayout();
						responseForProviderApi(response);
						if (dataResponse != null) {
							if (dataResponse.optBoolean("IsSuccess")) {
								if (dataResponse.optJSONObject("Data") != null) {
									JSONObject mResponseObj = dataResponse
											.optJSONObject("Data");
									btnContinueClicked(mResponseObj);
								}
							}
						}
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						showToast(error.toString());
						if (dataResponse != null) {
							if (dataResponse.optBoolean("IsSuccess")) {
								if (dataResponse.optJSONObject("Data") != null) {
									JSONObject mResponseObj = dataResponse
											.optJSONObject("Data");
									btnContinueClicked(mResponseObj);
								}
							}
						}
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
		mQueue.add(mRequest);
	}

	private void responseForProviderApi(JSONArray response) {
		if (response != null) {
			for (int i = 0; i < response.length(); i++) {
				JSONObject mObj = response.optJSONObject(i);
				if (mObj != null) {
					JSONArray mSpecificationsArray = mObj
							.optJSONArray("TpServiceInputSpecifications");
					if (mSpecificationsArray != null) {
						for (int j = 0; j < mSpecificationsArray.length(); j++) {
							JSONObject mObjSpecifications = mSpecificationsArray
									.optJSONObject(i);
							if (mObjSpecifications != null) {
								if (mObjSpecifications.optString("UserValues") != null) {
									StorageClass
											.getInstance(this)
											.setServiceTitle(
													mObjSpecifications
															.optString("ProviderServiceCode"));
									StorageClass
											.getInstance(this)
											.setServiceValue(
													mObjSpecifications
															.optString("UserValues"));
								}
							}
						}
					}
				}
			}
		}
	}

	private void btnContinueClicked(JSONObject mResponseObj) {

		if (isPanCardSelected) {
			StorageClass.getInstance(this).setServiceTitle("PAN");
			StorageClass.getInstance(this).setServiceValue(panCard);
		} else if (isAadharCardSelected) {
			StorageClass.getInstance(this).setServiceTitle("AADHAAR");
			StorageClass.getInstance(this).setServiceValue(
					aadharCardNumber + "," + aadharCardname);
		} else {
			StorageClass.getInstance(this).setServiceTitle("DL");
			StorageClass.getInstance(this).setServiceValue(
					drivingLicense + "," + drivingState);
		}

		Intent mIntent = new Intent(this, PayUIntegration.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("amount", mtxtTotalCost.getText().toString());
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("adId", selectedProductAdId);
		mBundle.putString("startDate", mStartDate);
		mBundle.putString("endDate", mEndDate);
		mBundle.putString("address", txtAddress);
		mBundle.putString("productDescription", mProductDescription);
		mBundle.putString("invoicePhone", mMobile);
		mBundle.putString("logisticsCost", mtxtLogistics.getText().toString());
		mBundle.putString("facilitationCost", mtxtFacilitaionCharges.getText()
				.toString());
		mBundle.putString("serviceTax", mtxtServiceTax.getText().toString());
		mBundle.putString("securityDeposit", mtxtSecurityDeposit.getText()
				.toString());
		mBundle.putString("securityDepositValue", mSecurityDeposit);
		mBundle.putString("startDateStr", mStartDateStr);
		mBundle.putString("endDateStr", mStartEndStr);
		mBundle.putString("data", mResponseObj.toString());
		mBundle.putString("addressResponse", addressResponse);
		mBundle.putString("mItemDetailsArray", itemDetailsArray);
		mBundle.putString("productRentalValue", mtxtProductRentalValue
				.getText().toString());
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

}
