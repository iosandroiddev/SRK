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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.internal.mj;
import com.models.PostAdModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.HomeActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.StorageClass;

public class InvoiceDetailsActivity extends BaseActivity {

	private String orderIdVal, invoiceAmountVal, rentalStartDateVal,
			rentalEndDateVal, invoiceQuantityVal, invoicePhoneVal,
			invoiceBrandVal, invoiceCategoryVal, billingAddressVal,
			productAdId = "";
	private String faciliationCost = "", serviceCost = "", logisticsCost = "",
			mSecurityDeposit = "", mSecurityDepositValue = "";
	private TextView orderId, invoiceAmount, rentalStartDate, rentalEndDate,
			rentalPeriod, invoiceQuantity, invoicePhone, invoiceBrand,
			invoiceCategory, billingAddress, doneBtn, mtxtLogisticCost,
			mtxtServiceCost, mtxtFacCost, mtxtSecurityDeposit;
	private LinearLayout mLayoutSecurityDeposit;
	private String mAuthHeader;
	private String mStartDateStr = "";
	private String mStartEndStr = "";
	private JSONObject mData = null;
	private JSONObject mAddressObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_invoice_details);
		getDetails();
		loadLayoutReferences();
		loadDetails();
	}

	private void loadLayoutReferences() {
		orderId = (TextView) findViewById(R.id.invoiceOrderId);
		invoiceAmount = (TextView) findViewById(R.id.invoiceAmount);
		rentalStartDate = (TextView) findViewById(R.id.invoiceRentalStartDate);
		rentalEndDate = (TextView) findViewById(R.id.invoiceRentalEndDate);
		rentalPeriod = (TextView) findViewById(R.id.invoiceRentalPeriod);
		invoiceQuantity = (TextView) findViewById(R.id.invoiceQuantity);
		invoicePhone = (TextView) findViewById(R.id.invoicePhone);
		invoiceBrand = (TextView) findViewById(R.id.invoiceBrand);
		invoiceCategory = (TextView) findViewById(R.id.invoiceProductCategory);
		billingAddress = (TextView) findViewById(R.id.invoiceBillingAddress);
		doneBtn = (TextView) findViewById(R.id.btnDone);
		mtxtFacCost = (TextView) findViewById(R.id.invoiceFacilitaion);
		mtxtLogisticCost = (TextView) findViewById(R.id.invoiceLogistics);
		mtxtServiceCost = (TextView) findViewById(R.id.invoiceServiceTax);
		mtxtSecurityDeposit = (TextView) findViewById(R.id.invoiceSecurityDeposit);
		mLayoutSecurityDeposit = (LinearLayout) findViewById(R.id.securityDepositLayout);

		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				initFinaliseRentalApi();
			}
		});
	}

	private void loadDetails() {
		Long rentalDays = 0L;
		int days = 0;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy");
			Date startDate = mSimpleDateFormat.parse(rentalStartDateVal);
			Date endDate = mSimpleDateFormat.parse(rentalEndDateVal);
			rentalDays = endDate.getTime() - startDate.getTime();
			days = (int) (rentalDays / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		rentalPeriod.setText(String.valueOf(days + 1));
		orderId.setText(orderIdVal);
		invoiceAmount.setText(invoiceAmountVal);
		rentalStartDate.setText(rentalStartDateVal);
		rentalEndDate.setText(rentalEndDateVal);
		invoiceQuantity.setText(invoiceQuantityVal);
		invoiceBrand.setText(invoiceBrandVal);
		invoiceCategory.setText(invoiceCategoryVal);
		billingAddress.setText(billingAddressVal);
		invoicePhone.setText(invoicePhoneVal);

		mtxtServiceCost.setText(serviceCost);
		mtxtLogisticCost.setText(logisticsCost);
		mtxtFacCost.setText(faciliationCost);

		if (mSecurityDepositValue == null
				|| mSecurityDepositValue.equalsIgnoreCase("0")
				|| mSecurityDepositValue.length() == 0) {
			mLayoutSecurityDeposit.setVisibility(View.GONE);
		} else {
			mtxtSecurityDeposit.setText(mSecurityDeposit);
			mLayoutSecurityDeposit.setVisibility(View.VISIBLE);
		}

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
				orderIdVal = mBundle.getString("orderId");
				invoiceAmountVal = mBundle.getString("amount");
				rentalStartDateVal = mBundle.getString("startDate");
				rentalEndDateVal = mBundle.getString("endDate");
				invoiceQuantityVal = mBundle.getString("quantity");
				invoicePhoneVal = mBundle.getString("invoicePhone");
				invoiceBrandVal = mBundle.getString("productDescription");
				invoiceCategoryVal = mBundle.getString("productDescription");
				billingAddressVal = mBundle.getString("address");
				faciliationCost = mBundle.getString("facilitationCost");
				serviceCost = mBundle.getString("serviceTax");
				logisticsCost = mBundle.getString("logisticsCost");
				mSecurityDepositValue = mBundle
						.getString("securityDepositValue");
				mSecurityDeposit = mBundle.getString("securityDeposit");
				productAdId = mBundle.getString("adId");
				mStartDateStr = mBundle.getString("startDate");
				mStartEndStr = mBundle.getString("endDate");
				String mdataStr = mBundle.getString("data");
				try {
					mData = new JSONObject(mdataStr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String mAddressStr = mBundle.getString("addressResponse");
				try {
					mAddressObject = new JSONObject(mAddressStr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initFinaliseRentalApi() {
		mAuthHeader = StorageClass.getInstance(this).getAuthHeader();
		// JSONObject params = new JSONObject();
		try {
			// params.put("Id", 0);
			// params.put("AdId", productAdId);
			// params.put("AdDescription", invoiceCategoryVal);
			// params.put("OrderNo", "");
			// params.put("Ad", "null");
			// params.put("RentalInitiatedDate", "");
			// params.put("RentalCycleStartDate", mStartDateStr);
			// params.put("RentalCycleEndDate", mStartEndStr);
			// params.put("NoofRentalDays", "");
			// params.put("CancellationDate", "0001-01-01T00:00:00");
			// params.put("CancellationReason", new Date().getTime());
			// params.put("RentalPayments", "null");
			// params.put("RentalInsurance", "null");
			// params.put("TransitTime", 0);
			// params.put("ForwardTransitTime", 0);
			// params.put("ReturnTransitTime", 0);
			// params.put("IsModerated", false);
			// params.put("IsVerified", true);
			// params.put("Quantity", "null");
			// params.put("RentalTriggerDate", "0001-01-01T00:00:00");
			// params.put("CouponCode", "null");
			// params.put("StatusLogs	", "null");
			// params.put("ChangesList", "null");
			// params.put("RentalFlags", "null");
			// params.put("RentalIssues", "null");
			// params.put("Claims", "null");
			// params.put("RentalPayout", "null");
			// params.put("PaymentReferenceNo", "null");
			// params.put("RentalLogisticsList", new JSONArray());
			// params.put("Notifications", new JSONArray());
			//
			JSONObject mRentalPayment = new JSONObject();
			mRentalPayment.put("PaymentAmount", invoiceAmountVal);
			mRentalPayment.put("TransactionAmount", 0);
			mRentalPayment.put("ServiceTaxAmount", 0);
			mRentalPayment.put("ProcessingFeeAmount", 0);
			mRentalPayment.put("PaymentReferenceNo", "");
			mRentalPayment.put("PaymentTransactionId", 0);
			mRentalPayment.put("PaymentMode", "null");
			mRentalPayment.put("PaymentComments", "null");
			mRentalPayment.put("PaymentLineItems", "null");
			mRentalPayment.put("RentalId", mData.opt("Id"));
			JSONObject mPaymentStatus = new JSONObject();
			mPaymentStatus.put("Title", "Success");
			mPaymentStatus.put("Code", "SUCCESS");
			mRentalPayment.put("PaymentStatus", mPaymentStatus);

			mData.put("RentalPayment", mRentalPayment);

			if (mAddressObject != null) {
				mAddressObject.put("AddressTypeId", 3);
				JSONObject mAddressType = new JSONObject();
				mAddressType.put("Title", "Shipping Address");
				mAddressType.put("Code", "AddressType");
				mAddressObject.put("AddressType", mAddressType);
			}

			mData.put("RenterBillingAddress", mAddressObject);
			mData.put("RenterShippingAddress", mAddressObject);

			//
			// JSONObject mRentalType = new JSONObject();
			// mRentalType.put("Title", "Fresh");
			// mRentalType.put("Code", "FRESH");
			// params.put("RentalType", mRentalType);
			//
			// JSONObject mRentalStatus = new JSONObject();
			// mRentalStatus.put("Title", "Initiated");
			// mRentalStatus.put("Code", "INITIATED");
			// params.put("RentalStatus", mRentalStatus);
			//
			// JSONArray mRentalTpServiceInputs = new JSONArray();
			// JSONObject mRentalTpServiceInputsObj = new JSONObject();
			// mRentalTpServiceInputsObj.put("RentalId", 0);
			// mRentalTpServiceInputsObj.put("UserId", "");
			// mRentalTpServiceInputsObj.put("Url", "");
			// JSONObject mTpFieldJson = new JSONObject();
			// mTpFieldJson.put("panId", "");
			// mRentalTpServiceInputsObj.put("TpFieldJson",
			// mTpFieldJson.toString());
			// JSONObject mTpProviderService = new JSONObject();
			// mTpProviderService.put("Title", "null");
			// mTpProviderService.put("Code", "PAN");
			// mRentalTpServiceInputsObj.put("TpProviderService",
			// mTpProviderService);
			// mRentalTpServiceInputs.put(mRentalTpServiceInputsObj);
			// params.put("RentalTpServiceInputs", mRentalTpServiceInputs);
			//
			// JSONObject mOwnerShippingAddress = new JSONObject();
			// mOwnerShippingAddress.put("AddressId", "");
			// mOwnerShippingAddress.put("DisplayName", "");
			// mOwnerShippingAddress.put("AddressLine1", "");
			// mOwnerShippingAddress.put("AddressLine2", "");
			// mOwnerShippingAddress.put("City", "");
			// mOwnerShippingAddress.put("State", "");
			// mOwnerShippingAddress.put("Country", "null");
			// mOwnerShippingAddress.put("IsDefaultShippingAddress", false);
			// mOwnerShippingAddress.put("PinCode", "");
			// mOwnerShippingAddress.put("MobileNo", "");
			// mOwnerShippingAddress.put("AddressTypeId", "null");
			// mOwnerShippingAddress.put("AddressType", "null");
			// mOwnerShippingAddress.put("IsAddMode", false);
			// mOwnerShippingAddress.put("IsActive", false);
			// mOwnerShippingAddress.put("DocumentRegister", "null");
			// mOwnerShippingAddress.put("IsVerified", false);
			// mOwnerShippingAddress.put("IsModerated", false);
			// mOwnerShippingAddress.put("LocationId", "");
			// params.put("OwnerShippingAddress", mOwnerShippingAddress);
			//
			// JSONObject mRenterShippingAddress = new JSONObject();
			// mRenterShippingAddress.put("AddressId", "");
			// mRenterShippingAddress.put("DisplayName", "");
			// mRenterShippingAddress.put("AddressLine1", "");
			// mRenterShippingAddress.put("AddressLine2", "");
			// mRenterShippingAddress.put("City", "");
			// mRenterShippingAddress.put("State", "");
			// mRenterShippingAddress.put("Country", "null");
			// mRenterShippingAddress.put("IsDefaultShippingAddress", false);
			// mRenterShippingAddress.put("PinCode", "");
			// mRenterShippingAddress.put("MobileNo", "");
			// mRenterShippingAddress.put("AddressTypeId", "null");
			// JSONObject mAddressType = new JSONObject();
			// mAddressType.put("Title", "Shipping Address");
			// mAddressType.put("Code", "AddressType");
			// mRenterShippingAddress.put("AddressType", mAddressType);
			// mRenterShippingAddress.put("IsAddMode", false);
			// mRenterShippingAddress.put("IsActive", false);
			// mRenterShippingAddress.put("DocumentRegister", "null");
			// mRenterShippingAddress.put("IsVerified", false);
			// mRenterShippingAddress.put("IsModerated", false);
			// mRenterShippingAddress.put("LocationId", "");
			// params.put("RenterShippingAddress", mRenterShippingAddress);
			//
			// JSONObject mRenterBillingAddress = new JSONObject();
			// mRenterBillingAddress.put("AddressId", "");
			// mRenterBillingAddress.put("DisplayName", "");
			// mRenterBillingAddress.put("AddressLine1", "");
			// mRenterBillingAddress.put("AddressLine2", "");
			// mRenterBillingAddress.put("City", "");
			// mRenterBillingAddress.put("State", "");
			// mRenterBillingAddress.put("Country", "null");
			// mRenterBillingAddress.put("IsDefaultShippingAddress", false);
			// mRenterBillingAddress.put("PinCode", "");
			// mRenterBillingAddress.put("MobileNo", "");
			// mRenterBillingAddress.put("AddressTypeId", "null");
			// JSONObject mRAddressType = new JSONObject();
			// mRAddressType.put("Title", "Shipping Address");
			// mRAddressType.put("Code", "AddressType");
			// mRenterBillingAddress.put("AddressType", mRAddressType);
			// mRenterBillingAddress.put("IsAddMode", false);
			// mRenterBillingAddress.put("IsActive", false);
			// mRenterBillingAddress.put("DocumentRegister", "null");
			// mRenterBillingAddress.put("IsVerified", false);
			// mRenterBillingAddress.put("IsModerated", false);
			// mRenterBillingAddress.put("LocationId", "");
			// params.put("RenterBillingAddress", mRenterBillingAddress);
			//
			// params.put("OwnerBillingAddress", "null");
			//
			// JSONObject mOwner = new JSONObject();
			// mOwner.put("UnRealMailCount", "null");
			// mOwner.put("UserProfile", "null");
			// mOwner.put("UserTransactions", "null");
			// mOwner.put("Verifications", "null");
			// mOwner.put("Addresses", "null");
			// mOwner.put("Documents", "null");
			// mOwner.put("Referral", "null");
			// mOwner.put("UserProfileImage", "null");
			// JSONObject mLoginOwner = new JSONObject();
			// mLoginOwner.put("LoginId", "");
			// mLoginOwner.put("EMail", "");
			// mLoginOwner.put("MobileNumber", "");
			// mLoginOwner.put("StageId", 0);
			// mLoginOwner.put("SubscriptionId", 0);
			// mLoginOwner.put("IsActive", false);
			// mLoginOwner.put("IsClaims", false);
			// mLoginOwner.put("ProviderKey", "null");
			// mLoginOwner.put("Provider", "null");
			// mLoginOwner.put("IsModified", false);
			// mLoginOwner.put("UserTypeId", 0);
			// mLoginOwner.put("RoleTypeId", 0);
			// mLoginOwner.put("UserStatusId", 0);
			// mLoginOwner.put("IsModerated", false);
			// mLoginOwner.put("IsVerified", false);
			// mLoginOwner.put("IsLocked", false);
			// mLoginOwner.put("Name", "");
			// mLoginOwner.put("ReferralUrl", "null");
			// mOwner.put("Login", mLoginOwner);
			// params.put("Owner", mOwner);
			//
			// JSONObject mRenter = new JSONObject();
			// mRenter.put("UnRealMailCount", "null");
			// mRenter.put("UserProfile", "null");
			// mRenter.put("UserTransactions", "null");
			// mRenter.put("Verifications", "null");
			// mRenter.put("Addresses", "null");
			// mRenter.put("Documents", new JSONArray());
			// mRenter.put("Referral", "null");
			// mRenter.put("UserProfileImage", "null");
			// JSONObject mRenterOwner = new JSONObject();
			// mRenterOwner.put("LoginId", "");
			// mRenterOwner.put("EMail", "");
			// mRenterOwner.put("MobileNumber", "");
			// mRenterOwner.put("StageId", 0);
			// mRenterOwner.put("SubscriptionId", 0);
			// mRenterOwner.put("IsActive", false);
			// mRenterOwner.put("IsClaims", false);
			// mRenterOwner.put("ProviderKey", "null");
			// mRenterOwner.put("Provider", "null");
			// mRenterOwner.put("IsModified", false);
			// mRenterOwner.put("UserTypeId", 0);
			// mRenterOwner.put("RoleTypeId", 0);
			// mRenterOwner.put("UserStatusId", 0);
			// mRenterOwner.put("IsModerated", false);
			// mRenterOwner.put("IsVerified", false);
			// mRenterOwner.put("IsLocked", false);
			// mRenterOwner.put("Name", "");
			// mRenterOwner.put("ReferralUrl", "null");
			// mRenter.put("Login", mRenterOwner);
			// params.put("Renter", mRenter);
			//
			// JSONArray mRentalCosts = new JSONArray();
			// JSONObject mObjRentalCosts = new JSONObject();
			// mObjRentalCosts.put("PaymentFrequency", "null");
			// mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("Amount", "");
			// mObjRentalCosts.put("IsStatutory", false);
			// JSONObject mPaymentCategory = new JSONObject();
			// mPaymentCategory.put("Title", "Product Rental Value");
			// mPaymentCategory.put("Code", "PRODRENTALVALUE");
			// mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			// mRentalCosts.put(mObjRentalCosts);
			//
			// mObjRentalCosts = new JSONObject();
			// mObjRentalCosts.put("PaymentFrequency", "null");
			// mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("Amount", logisticsCost);
			// mObjRentalCosts.put("IsStatutory", false);
			// mPaymentCategory = new JSONObject();
			// mPaymentCategory.put("Title", "Logistics Cost");
			// mPaymentCategory.put("Code", "LOGISTICS");
			// mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			// mRentalCosts.put(mObjRentalCosts);
			//
			// mObjRentalCosts = new JSONObject();
			// mObjRentalCosts.put("PaymentFrequency", "null");
			// mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("Amount", mSecurityDepositValue);
			// mObjRentalCosts.put("IsStatutory", false);
			// mPaymentCategory = new JSONObject();
			// mPaymentCategory.put("Title", "Security Deposit");
			// mPaymentCategory.put("Code", "SECURITY");
			// mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			// mRentalCosts.put(mObjRentalCosts);
			//
			// mObjRentalCosts = new JSONObject();
			// mObjRentalCosts.put("PaymentFrequency", "null");
			// mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("Amount", faciliationCost);
			// mObjRentalCosts.put("IsStatutory", false);
			// mPaymentCategory = new JSONObject();
			// mPaymentCategory.put("Title", "Facilitation Charges");
			// mPaymentCategory.put("Code", "COMMISSION");
			// mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			// mRentalCosts.put(mObjRentalCosts);
			//
			// mObjRentalCosts = new JSONObject();
			// mObjRentalCosts.put("PaymentFrequency", "null");
			// mObjRentalCosts.put("NextPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("LastPaymentDate", "0001-01-01T00:00:00");
			// mObjRentalCosts.put("Amount", serviceCost);
			// mObjRentalCosts.put("IsStatutory", false);
			// mPaymentCategory = new JSONObject();
			// mPaymentCategory.put("Title", "Service Tax");
			// mPaymentCategory.put("Code", "SERVICETAX");
			// mObjRentalCosts.put("PaymentCategory", mPaymentCategory);
			// mRentalCosts.put(mObjRentalCosts);
			// params.put("RentalCosts", mRentalCosts);
			//
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.FINALIZERENTAL, mData, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						showToast("Success");
						navigateToHome();
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

	private void navigateToHome() {
		Intent mIntent = new Intent(this, HomeActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
		finish();
	}

}
