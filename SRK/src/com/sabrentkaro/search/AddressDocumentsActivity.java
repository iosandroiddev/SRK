package com.sabrentkaro.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.CityModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class AddressDocumentsActivity extends BaseActivity {

	private EditText meditAdress, meditState, mEditPinCode, mEditPhone,
			meditPanCardNumber, mEditAadharCardName, mEditAadharCardNumber,
			mEditDrvingLicense, mEditDrvingState;
	private TextView mbtnPanCard, mbtnAadharCard, mbtnContinue,
			mbtnDrivingLicense;
	private boolean isPanCardSelected = false, isAadharCardSelected = false,
			isDrivingLicenseSelected = false;
	private LinearLayout mPanCardLayout, mAAadharCardLayout,
			mDrivingLicenseLayout;
	private TextView mbtnSelectCity;
	private String selectedProductAdId, mPrice, mMonthPrice, mWeekPrice,
			mProductDescription, mQuantity, mStartDate, mEndDate, mLocationId,
			mSecurityDeposit;
	private String mStartDateStr = "";
	private String mStartEndStr = "";
	private JSONObject mAddressResponse;
	private String itemDetailsArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_adress_documents);
		initGetAddressListApi();
		getDetails();
		loadReferences();
		loadDetails();
	}

	private void initGetAddressListApi() {
		showProgressLayout();
		final String mAuthHeader = StorageClass.getInstance(this)
				.getAuthHeader();
		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.GETADDRESSLIST, new JSONObject(),
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						saveAddressResponse(response);
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

	private void saveAddressResponse(JSONObject response) {
		if (response != null) {
			mAddressResponse = response;
		}
	}

	private void loadDetails() {
		meditAdress.setText(StorageClass.getInstance(this).getAddress());
		mbtnSelectCity.setText(StorageClass.getInstance(this).getUserCity());
		meditState.setText(StorageClass.getInstance(this).getUserState());
		mEditPinCode.setText(StorageClass.getInstance(this).getPinCode());
		mEditPhone.setText(StorageClass.getInstance(this).getMobileNumber());

		if (StorageClass.getInstance(this).getServiceTitle().contains("PAN")) {
			meditPanCardNumber.setText(StorageClass.getInstance(this)
					.getServiceValue());
			isPanCardSelected = true;
			isAadharCardSelected = false;
			isDrivingLicenseSelected = false;
			mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
		} else if (StorageClass.getInstance(this).getServiceTitle()
				.contains("AADHAAR")) {
			mEditAadharCardNumber.setText(StorageClass.getInstance(this)
					.getServiceValue());
			isPanCardSelected = false;
			isAadharCardSelected = true;
			isDrivingLicenseSelected = false;
			mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
		} else if (StorageClass.getInstance(this).getServiceTitle()
				.contains("DL")) {
			mEditDrvingLicense.setText(StorageClass.getInstance(this)
					.getServiceValue());
			isPanCardSelected = false;
			isAadharCardSelected = false;
			isDrivingLicenseSelected = true;
			mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
		}

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
				selectedProductAdId = mBundle.getString("selectedAdId");
				mPrice = mBundle.getString("productPrice");
				mMonthPrice = mBundle.getString("productPriceMonth");
				mWeekPrice = mBundle.getString("productPriceweek");
				mProductDescription = mBundle.getString("productDescription");
				mQuantity = mBundle.getString("quantity");
				mLocationId = mBundle.getString("locationId");
				mStartDate = mBundle.getString("selectedStartDate");
				mEndDate = mBundle.getString("selectedEndDate");
				mSecurityDeposit = mBundle.getString("securitDeposit");
				mStartDateStr = mBundle.getString("startDate");
				mStartEndStr = mBundle.getString("endDate");
				itemDetailsArray = mBundle.getString("mItemDetailsArray");
			}
		}
	}

	private void loadReferences() {
		meditAdress = (EditText) findViewById(R.id.editAddress);
		mbtnSelectCity = (TextView) findViewById(R.id.btnSelectCity);
		meditState = (EditText) findViewById(R.id.editState);
		mEditPinCode = (EditText) findViewById(R.id.editPincode);
		mEditPhone = (EditText) findViewById(R.id.editPhone);
		meditPanCardNumber = (EditText) findViewById(R.id.editPanCard);
		mEditAadharCardName = (EditText) findViewById(R.id.editAadharName);
		mEditAadharCardNumber = (EditText) findViewById(R.id.editAadharNumber);

		mEditDrvingLicense = (EditText) findViewById(R.id.editDrivingLicense);
		mEditDrvingState = (EditText) findViewById(R.id.editDrivingState);

		mbtnPanCard = (TextView) findViewById(R.id.btnPanCard);
		mbtnAadharCard = (TextView) findViewById(R.id.btnAadharCard);
		mbtnDrivingLicense = (TextView) findViewById(R.id.btnDrivingLicense);

		mPanCardLayout = (LinearLayout) findViewById(R.id.layoutPanCard);
		mAAadharCardLayout = (LinearLayout) findViewById(R.id.layoutAadharCard);
		mDrivingLicenseLayout = (LinearLayout) findViewById(R.id.layoutDrivingLicense);
		mbtnContinue = (TextView) findViewById(R.id.btnContinue);
		mbtnPanCard.setOnClickListener(this);
		mbtnAadharCard.setOnClickListener(this);
		mbtnDrivingLicense.setOnClickListener(this);
		mbtnContinue.setOnClickListener(this);

		StaticUtils.setEditTextHintFont(meditAdress, this);
		StaticUtils.setEditTextHintFont(mEditPinCode, this);
		StaticUtils.setEditTextHintFont(meditState, this);
		StaticUtils.setEditTextHintFont(mEditPhone, this);
		StaticUtils.setEditTextHintFont(meditPanCardNumber, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardName, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardNumber, this);
		StaticUtils.setEditTextHintFont(mEditDrvingState, this);
		StaticUtils.setEditTextHintFont(mEditDrvingLicense, this);
		mbtnSelectCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCityAlert(mbtnSelectCity);
			}
		});

		mEditAadharCardName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mEditAadharCardNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnAadharCard:
			btnAadharCardClicked();
			break;
		case R.id.btnPanCard:
			btnPanCardClicked();
			break;
		case R.id.btnDrivingLicense:
			btnDrivingLicenseClicked();
			break;
		case R.id.btnContinue:
			btnContinueClicked();
			break;
		default:
			break;
		}
	}

	private void btnContinueClicked() {

		if (meditAdress.getText().toString().length() == 0) {
			showToast("Please Enter Address");
		} else {
			if (mbtnSelectCity.getText().toString()
					.equalsIgnoreCase("Select City")) {
				showToast("Please Select City");
			} else {
				if (meditState.getText().toString().length() == 0) {
					showToast("Please Enter State");
				} else {
					if (mEditPinCode.getText().toString().length() == 0) {
						showToast("Please Enter Pincode");
					} else {
						if (mEditPinCode.getText().toString().length() != 6) {
							showToast("Please Enter Valid Pincode");
						} else {
							if (mEditPhone.getText().toString().length() == 0) {
								showToast("Please Enter Mobile Number");
							} else {
								if (mEditPhone.getText().toString().length() != 10) {
									showToast("Please Enter Valid Mobile Number");
								} else {
									if (!isPanCardSelected
											&& !isAadharCardSelected
											&& !isDrivingLicenseSelected) {
										showToast("Please Select Documents");
									} else if (isPanCardSelected) {
										if (TextUtils
												.isEmpty(meditPanCardNumber
														.getText().toString())) {
											showToast("Please Enter PAN Card Number");
										} else {
											if (meditPanCardNumber.getText()
													.toString().length() != 10) {
												showToast("Please Enter  Valid PAN Card Number");
											} else {
												startOrdersActivity();
											}
										}
									} else if (isAadharCardSelected) {
										if (TextUtils
												.isEmpty(mEditAadharCardName
														.getText().toString())) {
											showToast("Please Enter AadharCard Name");
										} else if (TextUtils
												.isEmpty(mEditAadharCardNumber
														.getText().toString())) {
											showToast("Please Enter AadharCard Number");
										} else {
											if (mEditAadharCardNumber.getText()
													.toString().length() != 12) {
												showToast("Please Enter Valid AadharCard Number");
											} else {
												startOrdersActivity();
											}
										}
									} else if (isDrivingLicenseSelected) {
										if (TextUtils
												.isEmpty(mEditDrvingLicense
														.getText().toString())) {
											showToast("Please Enter Driving License Number");
										} else if (TextUtils
												.isEmpty(mEditDrvingState
														.getText().toString())) {
											showToast("Please Enter Drving License State");
										} else {
											if (mEditDrvingLicense.getText()
													.toString().length() != 16) {
												showToast("Please Enter Valid Driving License Number");
											} else {
												startOrdersActivity();
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

	private void startOrdersActivity() {
		Intent intent = new Intent(this, OrderDetailsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedStartDate", mStartDate);
		mBundle.putString("selectedEndDate", mEndDate);
		mBundle.putString("selectedAdId", selectedProductAdId);
		mBundle.putString("locationId", mLocationId);
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("productPrice", mPrice);
		mBundle.putString("productPriceMonth", mMonthPrice);
		mBundle.putString("productPriceWeek", mWeekPrice);
		mBundle.putString("address", meditAdress.getText().toString());
		mBundle.putString("city", mbtnSelectCity.getText().toString());
		mBundle.putString("state", meditState.getText().toString());
		mBundle.putString("pincode", mEditPinCode.getText().toString());
		mBundle.putString("mobile", mEditPhone.getText().toString());
		mBundle.putString("productDescription", mProductDescription);
		mBundle.putString("securitDeposit", mSecurityDeposit);
		mBundle.putString("startDate", mStartDateStr);
		mBundle.putString("endDate", mStartEndStr);
		mBundle.putString("mAddressJson", mAddressResponse.toString());
		mBundle.putString("mItemDetailsArray", itemDetailsArray);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	private void btnPanCardClicked() {
		if (isPanCardSelected) {
			isPanCardSelected = false;
			mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_unselect, 0, 0, 0);
			StaticUtils.expandCollapse(mPanCardLayout, false);
		} else {
			isPanCardSelected = true;
			mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
			StaticUtils.expandCollapse(mPanCardLayout, true);
			meditPanCardNumber.requestFocus();
			if (isAadharCardSelected) {
				isAadharCardSelected = false;
				mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mAAadharCardLayout, false);
			}
			if (isDrivingLicenseSelected) {
				isDrivingLicenseSelected = false;
				mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mDrivingLicenseLayout, false);
			}
		}
	}

	private void btnDrivingLicenseClicked() {
		if (isDrivingLicenseSelected) {
			isDrivingLicenseSelected = false;
			mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_unselect, 0, 0, 0);
			StaticUtils.expandCollapse(mDrivingLicenseLayout, false);
		} else {
			isDrivingLicenseSelected = true;
			mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
			StaticUtils.expandCollapse(mDrivingLicenseLayout, true);
			mEditDrvingLicense.requestFocus();
			if (isAadharCardSelected) {
				isAadharCardSelected = false;
				mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mAAadharCardLayout, false);
			}
			if (isPanCardSelected) {
				isPanCardSelected = false;
				mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mPanCardLayout, false);
			}
		}
	}

	private void btnAadharCardClicked() {
		if (isAadharCardSelected) {
			isAadharCardSelected = false;
			mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_unselect, 0, 0, 0);
			StaticUtils.expandCollapse(mAAadharCardLayout, false);
		} else {
			if (isPanCardSelected) {
				isPanCardSelected = false;
				mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mPanCardLayout, false);
			}
			if (isDrivingLicenseSelected) {
				isDrivingLicenseSelected = false;
				mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mDrivingLicenseLayout, false);
			}
			mEditAadharCardName.requestFocus();
			isAadharCardSelected = true;
			mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
			StaticUtils.expandCollapse(mAAadharCardLayout, true);

		}
	}

	private void showCityAlert(final TextView mtxtView) {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this)
				.getCityList();
		int pos = -1;
		if (mCityArray != null) {
			final String[] mCities = new String[mCityArray.size()];
			for (int i = 0; i < mCityArray.size(); i++) {
				if (TextUtils.isEmpty(StorageClass.getInstance(this)
						.getUserCity())) {
					pos = -1;
				} else {
					if (mCityArray
							.get(i)
							.getName()
							.equalsIgnoreCase(
									StorageClass.getInstance(this)
											.getUserCity())) {
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
								mtxtView.setText(mCities[which]);
								dialog.dismiss();
							}
						});
				alert.show();
			}
		}
	}

}
