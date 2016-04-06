package com.sabrentkaro.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.StorageClass;

public class OrderDetailsActivity extends BaseActivity {

	private String selectedProductAdId, mPrice, mProductDescription, mQuantity,
			mStartDate, mEndDate, mLocationId, mMonthPrice, mWeekPrice,
			mAddress, mCity, mState, txtAddress, mAuthHeader;

	private TextView mtxtTotalCost, mtxtRentalPeriod, mtxtShippingAddress,
			mtxtDescription, mtxtQuantity, mtxtFromDate, mtxtToDate,
			mbtnContinue;

	private TextView mtxtProductRentalValue, mtxtPerDayCost, mtxtPerWeekCost,
			mtxtPerMonthCost, mtxtFacilitaionCharges, mtxtFaciCost,
			mtxtServiceTax, mtxtServiceTaxCost, mtxtLogistics,
			mtxtLogisticsCost;

	private String mProdRentValue, mServiceValue, mLogisticsValue,
			mFaciliValue;

	private String fromDate;
	private String toDate;
	private String mSecurityValue;

	private String mPincode;

	private String mMobile;

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
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnContinue) {
			btnContinueClicked();
		}
	}

	private void btnContinueClicked() {
		Intent mIntent = new Intent(this, PayUIntegration.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("amount", mtxtTotalCost.getText().toString());
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("startDate", mStartDate);
		mBundle.putString("endDate", mEndDate);
		mBundle.putString("address", txtAddress);
		mBundle.putString("productDescription", mProductDescription);
		mBundle.putString("invoicePhone", mMobile);
		mBundle.putString("logisticsCost", mtxtLogistics.getText().toString());
		mBundle.putString("facilitationCost", mtxtFacilitaionCharges.getText().toString());
		mBundle.putString("serviceTax", mtxtServiceTax.getText().toString());
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

}
