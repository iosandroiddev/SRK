package com.sabrentkaro.postad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.models.CityModel;
import com.models.PostAdModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.PostAdSaver;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class PostAdDocumentActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private EditText meditAdress, meditState, mEditPinCode, mEditPhone,
			meditPanCardNumber, mEditAadharCardName, mEditAadharCardNumber,
			meditUserAdress, meditUserCity, meditUserState, mEditUserPinCode,
			mEditUserPhone, mEditDrvingLicense, mEditDrvingState;;
	private TextView mbtnNext, mbtnSelectCity, mbtnSelectUserCity, mbtnClear;
	private LinearLayout mLayoutUserAddress;
	private CheckBox mCheckAddress;
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
	private String mQuantity;
	private String mtxtRating;
	private String mtxtCondName;
	private HashMap<String, String> controlLayouts = new HashMap<String, String>();

	private TextView mbtnPanCard, mbtnAadharCard, mbtnDrivingLicense;
	private boolean isPanCardSelected = false, isAadharCardSelected = false,
			isDrivingLicenseSelected = false;
	private LinearLayout mPanCardLayout, mAAadharCardLayout,
			mDrivingLicenseLayout;
	private String mPricingArray;
	private String minimumRentalPeriod;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_documents);
		loadReferences();
		getDetails();
		loadDetails();

	}

	@SuppressWarnings("unchecked")
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
				mtxtRating = mBundle.getString("productCondition");
				mtxtCondName = mBundle.getString("productConditionName");
				controlLayouts = (HashMap<String, String>) mBundle
						.getSerializable("controlLayouts");
				mPricingArray = mBundle.getString("pricingArray");
				minimumRentalPeriod = mBundle.getString("minimumRentalPeriod");

			}
		}
	}

	private void loadDetails() {
		PostAdSaver mSaver = PostAdSaver.getInstance(this);

		if (PostAdSaver.getInstance(this).isEditing()) {
			meditAdress.setText(mSaver.getAddress());
			mbtnSelectCity.setText(mSaver.getCity());
			meditState.setText(mSaver.getState());
			mEditPinCode.setText(mSaver.getPincode());
			mEditPhone.setText(mSaver.getMobNumber());
			mCheckAddress.setChecked(mSaver.isProductAddressChecked());
			meditPanCardNumber.setText(mSaver.getPanCard());

			mEditAadharCardName.setText(mSaver.getAadharname());
			mEditAadharCardNumber.setText(mSaver.getAadharNumber());
			if (mSaver.isProductAddressChecked()) {

			} else {
				meditUserAdress.setText(mSaver.getUserAddress());
				mbtnSelectUserCity.setText(mSaver.getUserCity());
				meditUserState.setText(mSaver.getUserState());
				mEditUserPinCode.setText(mSaver.getUserPincode());
				mEditUserPhone.setText(mSaver.getUserMobNumber());
				// StaticUtils.expandCollapse(mLayoutUserAddress, true);
			}

			if (mSaver.isPanCardSelected()) {
				isPanCardSelected = true;
				isAadharCardSelected = false;
				isDrivingLicenseSelected = false;
				mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_select, 0, 0, 0);
			} else {
				if (mSaver.isAaadharCardSelected()) {
					isAadharCardSelected = true;
					isPanCardSelected = false;
					isDrivingLicenseSelected = false;
					mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.btn_unselect, 0, 0, 0);
					mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.btn_select, 0, 0, 0);
					mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.btn_unselect, 0, 0, 0);
				} else {
					if (mSaver.isDrivingLicenseSelected()) {
						isAadharCardSelected = false;
						isPanCardSelected = false;
						isDrivingLicenseSelected = true;
						mbtnDrivingLicense
								.setCompoundDrawablesWithIntrinsicBounds(
										R.drawable.btn_select, 0, 0, 0);
						mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
								R.drawable.btn_unselect, 0, 0, 0);
						mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
								R.drawable.btn_unselect, 0, 0, 0);
					} else {

					}
				}
			}
		} else {
			meditAdress.setText(StorageClass.getInstance(this).getAddress());
			mbtnSelectCity
					.setText(StorageClass.getInstance(this).getUserCity());
			meditState.setText(StorageClass.getInstance(this).getUserState());
			mEditPinCode.setText(StorageClass.getInstance(this).getPinCode());
			mEditPhone
					.setText(StorageClass.getInstance(this).getMobileNumber());
		}

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

			String mArrayValues = StorageClass.getInstance(this)
					.getServiceValue();
			String[] mValues = mArrayValues.split(",");
			if (mValues != null && mValues.length >= 2) {
				mEditAadharCardNumber.setText(mValues[0]);
				mEditAadharCardName.setText(mValues[1]);
			} else {
				mEditAadharCardNumber.setText(StorageClass.getInstance(this)
						.getServiceValue());
			}

			isPanCardSelected = false;
			isAadharCardSelected = true;
			isDrivingLicenseSelected = false;
			mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
		} else if (StorageClass.getInstance(this).getServiceTitle()
				.contains("DL")) {
			String mArrayValues = StorageClass.getInstance(this)
					.getServiceValue();
			String[] mValues = mArrayValues.split(",");
			if (mValues != null && mValues.length >= 2) {
				mEditDrvingLicense.setText(mValues[0]);
				mEditDrvingState.setText(mValues[1]);
			} else {
				mEditDrvingLicense.setText(StorageClass.getInstance(this)
						.getServiceValue());
			}
			isPanCardSelected = false;
			isAadharCardSelected = false;
			isDrivingLicenseSelected = true;
			mbtnDrivingLicense.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
		}

	}

	private void loadReferences() {
		meditAdress = (EditText) findViewById(R.id.editAddress);
		mbtnSelectUserCity = (TextView) findViewById(R.id.btnSelectUserCity);
		meditState = (EditText) findViewById(R.id.editState);
		mEditPinCode = (EditText) findViewById(R.id.editPincode);
		mEditPhone = (EditText) findViewById(R.id.editPhone);

		meditUserAdress = (EditText) findViewById(R.id.editUserAddress);
		mbtnSelectCity = (TextView) findViewById(R.id.btnSelectCity);
		meditUserState = (EditText) findViewById(R.id.editUserState);
		mEditUserPinCode = (EditText) findViewById(R.id.editUserPincode);
		mEditUserPhone = (EditText) findViewById(R.id.editUserPhone);

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
		mbtnPanCard.setOnClickListener(this);
		mbtnAadharCard.setOnClickListener(this);
		mbtnDrivingLicense.setOnClickListener(this);
		mPanCardLayout.setVisibility(View.GONE);
		mAAadharCardLayout.setVisibility(View.GONE);
		mDrivingLicenseLayout.setVisibility(View.GONE);
		mLayoutUserAddress = (LinearLayout) findViewById(R.id.layoutUserAddress);
		mCheckAddress = (CheckBox) findViewById(R.id.checkAddress);
		mbtnNext = (TextView) findViewById(R.id.btnNext);
		mbtnClear = (TextView) findViewById(R.id.btnClear);
		mbtnNext.setOnClickListener(this);
		mbtnSelectCity.setOnClickListener(this);
		mbtnSelectUserCity.setOnClickListener(this);
		mbtnClear.setOnClickListener(this);

		StaticUtils.setEditTextHintFont(meditUserAdress, this);
		StaticUtils.setEditTextHintFont(meditUserState, this);
		StaticUtils.setEditTextHintFont(mEditUserPinCode, this);
		StaticUtils.setEditTextHintFont(mEditUserPhone, this);

		StaticUtils.setEditTextHintFont(meditAdress, this);
		StaticUtils.setEditTextHintFont(mEditPinCode, this);
		StaticUtils.setEditTextHintFont(meditState, this);
		StaticUtils.setEditTextHintFont(mEditPhone, this);
		StaticUtils.setEditTextHintFont(meditPanCardNumber, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardName, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardNumber, this);
		StaticUtils.setEditTextHintFont(mEditDrvingState, this);
		StaticUtils.setEditTextHintFont(mEditDrvingLicense, this);
		StaticUtils.setCheckBoxFont(mCheckAddress, this);

		mCheckAddress.setOnCheckedChangeListener(this);
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
		case R.id.btnNext:
			btnNextClicked();
			break;
		case R.id.btnClear:
			btnClearClicked();
			break;
		case R.id.btnSelectCity:
			showCityAlert(mbtnSelectCity);
			break;
		case R.id.btnSelectUserCity:
			showCityAlert(mbtnSelectUserCity);
			break;
		default:
			break;
		}
	}

	private void btnClearClicked() {
		meditAdress.setText("");
		meditState.setText("");
		mEditPinCode.setText("");
		mEditPhone.setText("");
		meditPanCardNumber.setText("");
		mEditAadharCardName.setText("");
		mEditAadharCardNumber.setText("");

		mbtnSelectCity.setText("Select City");
		mbtnSelectUserCity.setText("Select City");

		meditUserAdress.setText("");
		meditUserState.setText("");
		mEditUserPhone.setText("");
		mEditUserPinCode.setText("");

		mCheckAddress.setChecked(false);

	}

	private void btnNextClicked() {

		if (mCheckAddress.isChecked()) {
			if (meditAdress.getText().toString().length() == 0) {
				showToast("Please Enter Address");
			} else {
				if (mbtnSelectCity.getText().toString().contains("Select")) {
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
									showToast("Please Enter Phone Number");
								} else {
									if (mEditPhone.getText().toString()
											.length() != 10) {
										showToast("Please Enter Valid Phone Number");
									} else {
										if (!isPanCardSelected
												&& !isAadharCardSelected
												&& !isDrivingLicenseSelected) {
											showToast("Please Select Documents");
										} else if (isPanCardSelected) {
											if (TextUtils
													.isEmpty(meditPanCardNumber
															.getText()
															.toString())) {
												showToast("Please Enter PAN Card Number");
											} else {
												if (meditPanCardNumber
														.getText().toString()
														.length() != 10) {
													showToast("Please Enter Valid PAN Card Number");
												} else {
													// navigateToPostPreview();
													initSaveUserTpInformationApi();
												}
											}
										} else if (isAadharCardSelected) {
											if (TextUtils
													.isEmpty(mEditAadharCardName
															.getText()
															.toString())) {
												showToast("Please Enter AadharCard Name");
											} else if (TextUtils
													.isEmpty(mEditAadharCardNumber
															.getText()
															.toString())) {
												showToast("Please Enter AadharCard Number");
											} else {
												if (mEditAadharCardNumber
														.getText().toString()
														.length() != 12) {
													showToast("Please Enter Valid AadharCard Number");
												} else {
													// navigateToPostPreview();
													initSaveUserTpInformationApi();
												}
											}
										} else if (isDrivingLicenseSelected) {
											if (TextUtils
													.isEmpty(mEditDrvingLicense
															.getText()
															.toString())) {
												showToast("Please Enter Driving License Number");
											} else if (TextUtils
													.isEmpty(mEditDrvingState
															.getText()
															.toString())) {
												showToast("Please Enter Drving License State");
											} else {
												if (mEditDrvingLicense
														.getText().toString()
														.length() != 16) {
													showToast("Please Enter Valid Driving License Number");
												} else {
													// navigateToPostPreview();
													initSaveUserTpInformationApi();
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

		} else {
			if (meditUserAdress.getText().toString().length() == 0) {
				showToast("Please Enter Address");
			} else {
				if (mbtnSelectUserCity.getText().toString().contains("Select")) {
					showToast("Please Select City");
				} else {
					if (meditUserState.getText().toString().length() == 0) {
						showToast("Please Enter State");
					} else {
						if (mEditUserPinCode.getText().toString().length() == 0) {
							showToast("Please Enter Pincode");
						} else {
							if (mEditUserPinCode.getText().toString().length() != 6) {
								showToast("Please Enter Valid Pincode");
							} else {
								if (mEditUserPhone.getText().toString()
										.length() == 0) {
									showToast("Please Enter Phone Number");
								} else {
									if (mEditUserPhone.getText().toString()
											.length() != 10) {
										showToast("Please Enter Valid Phone Number");
									} else {
										if (!isPanCardSelected
												&& !isAadharCardSelected) {
											showToast("Please Select Documents");
										} else if (isPanCardSelected) {
											if (TextUtils
													.isEmpty(meditPanCardNumber
															.getText()
															.toString())) {
												showToast("Please Enter PAN Card Number");
											} else {
												if (meditPanCardNumber
														.getText().toString()
														.length() != 10) {
													showToast("Please Enter Valid PAN Card Number");
												} else {
													navigateToPostPreview();
												}
											}
										} else if (isAadharCardSelected) {
											if (TextUtils
													.isEmpty(mEditAadharCardName
															.getText()
															.toString())) {
												showToast("Please Enter AadharCard Name");
											} else if (TextUtils
													.isEmpty(mEditAadharCardNumber
															.getText()
															.toString())) {
												showToast("Please Enter AadharCard Number");
											} else {
												navigateToPostPreview();
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

	private void navigateToPostPreview() {

		if (isPanCardSelected) {
			StorageClass.getInstance(this).setServiceTitle("PAN");
			StorageClass.getInstance(this).setServiceValue(
					meditPanCardNumber.getText().toString());
		} else if (isAadharCardSelected) {
			StorageClass.getInstance(this).setServiceTitle("AADHAAR");
			StorageClass.getInstance(this).setServiceValue(
					mEditAadharCardNumber.getText().toString() + ","
							+ mEditAadharCardName.getText().toString());
		} else {
			StorageClass.getInstance(this).setServiceTitle("DL");
			StorageClass.getInstance(this).setServiceValue(
					mEditDrvingLicense.getText().toString() + ","
							+ mEditDrvingState.getText().toString());
		}

		PostAdSaver mAdSaver = PostAdSaver.getInstance(this);
		mAdSaver.setCity(mbtnSelectCity.getText().toString());
		mAdSaver.setAadharname(mEditAadharCardName.getText().toString());
		mAdSaver.setAadharNumber(mEditAadharCardNumber.getText().toString());
		mAdSaver.setAddress(meditAdress.getText().toString());
		mAdSaver.setState(meditState.getText().toString());
		mAdSaver.setPincode(mEditPinCode.getText().toString());
		mAdSaver.setMobNumber(mEditPhone.getText().toString());
		mAdSaver.setPanCard(meditPanCardNumber.getText().toString());

		mAdSaver.setUserCity(mbtnSelectUserCity.getText().toString());
		mAdSaver.setUserAddress(meditUserAdress.getText().toString());
		mAdSaver.setUserState(meditUserState.getText().toString());
		mAdSaver.setUserPincode(mEditUserPinCode.getText().toString());
		mAdSaver.setUserMobNumber(mEditUserPhone.getText().toString());

		mAdSaver.setPanCardSelected(isPanCardSelected);
		mAdSaver.setAaadharCardSelected(isAadharCardSelected);
		mAdSaver.setDrivingLicenseSelected(isDrivingLicenseSelected);
		mAdSaver.setProductAddressChecked(mCheckAddress.isChecked());

		Intent mIntent = new Intent(this, PostAdPreview.class);
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
		mBundle.putString("weekCost", mWeekCost);
		mBundle.putString("monthlyCost", mMonthCost);
		mBundle.putString("productCondition", mtxtRating);
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("securityDeposit", mSecurityDeposit);
		mBundle.putString("productConditionName", mtxtCondName);
		mBundle.putString("filePath", mFilePath);
		mBundle.putString("productAdId", mProductAdId);
		mBundle.putString("address", meditAdress.getText().toString());
		mBundle.putString("city", mbtnSelectCity.getText().toString());
		mBundle.putString("stateValue", meditState.getText().toString());
		mBundle.putString("pincode", mEditPinCode.getText().toString());
		mBundle.putSerializable("controlLayouts", controlLayouts);
		mBundle.putString("minimumRentalPeriod", minimumRentalPeriod);

		if (mCheckAddress.isChecked()) {
			mBundle.putString("displayCurrent", "false");
		} else {
			mBundle.putString("displayCurrent", "true");
		}

		mBundle.putString("addressUser", meditUserAdress.getText().toString());
		mBundle.putString("cityUser", mbtnSelectUserCity.getText().toString());
		mBundle.putString("stateValueUser", meditUserState.getText().toString());
		mBundle.putString("pincodeUser", mEditUserPinCode.getText().toString());
		mBundle.putString("mobileNumberUser", mEditUserPhone.getText()
				.toString());

		mBundle.putString("panCard", meditPanCardNumber.getText().toString());
		mBundle.putString("drivinglicense", mEditDrvingLicense.getText()
				.toString());
		mBundle.putString("aadharCardName", mEditAadharCardName.getText()
				.toString());
		mBundle.putString("aadharCardNumber", mEditAadharCardNumber.getText()
				.toString());
		mBundle.putString("drivingState", mEditDrvingState.getText().toString());

		mBundle.putString("mobileNumber", mEditPhone.getText().toString());
		mBundle.putString("pricingArray", mPricingArray.toString());

		mBundle.putBoolean("isPanCardSelected", isPanCardSelected);
		mBundle.putBoolean("isAadharSelected", isAadharCardSelected);
		mBundle.putBoolean("drivingLicenseSelected", isDrivingLicenseSelected);
		mIntent.putExtras(mBundle);
		startActivityForResult(mIntent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == Activity.RESULT_OK) {
				PostAdSaver.getInstance(this).setEditing(true);
				finish();
			} else {

			}
		}
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
		mPanCardLayout.invalidate();
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
			meditPanCardNumber.requestFocus();
			if (isAadharCardSelected) {
				isAadharCardSelected = false;
				mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mAAadharCardLayout, false);
			}
			if (isPanCardSelected) {
				isPanCardSelected = false;
				mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_unselect, 0, 0, 0);
				StaticUtils.expandCollapse(mPanCardLayout, false);
			}
		}
		mbtnDrivingLicense.invalidate();
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
		mAAadharCardLayout.invalidate();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			StaticUtils.expandCollapse(mLayoutUserAddress, false);
		} else {
			StaticUtils.expandCollapse(mLayoutUserAddress, true);
		}
	}

	private void showCityAlert(final TextView mtxtView) {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this)
				.getCityList();
		int pos = -1;
		if (mCityArray != null) {
			final String[] mCities = new String[mCityArray.size()];
			for (int i = 0; i < mCityArray.size(); i++) {
				if (mtxtView.getText().toString()
						.equalsIgnoreCase("Seletct City")) {
					pos = -1;
				} else {
					if (mCityArray.get(i).getName()
							.equalsIgnoreCase(mtxtView.getText().toString())) {
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
								setLocation();
							}
						});
				alert.show();
			}
		}
	}

	private void initSaveUserTpInformationApi() {
		final String mAuthHeader = StorageClass.getInstance(this)
				.getAuthHeader();
		showProgressLayout();

		JSONObject mParams = new JSONObject();
		try {
			if (isPanCardSelected) {
				mParams.put("ProviderServiceCode", "PAN");
				mParams.put("UserDetails", meditPanCardNumber.getText()
						.toString());
				mParams.put("UserId", "null");
			} else if (isAadharCardSelected) {
				mParams.put("ProviderServiceCode", "AADHAAR");
				mParams.put("UserDetails", mEditAadharCardNumber.getText()
						.toString()
						+ ","
						+ mEditAadharCardName.getText().toString());
				mParams.put("UserId", "null");
			} else {
				mParams.put("ProviderServiceCode", "DL");
				mParams.put("UserDetails", mEditDrvingLicense.getText()
						.toString()
						+ ","
						+ mEditDrvingState.getText().toString());
				mParams.put("UserId", "null");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		StringRequest mReq = new StringRequest(Method.POST,
				ApiUtils.SAVEUSERTPINFORMATION, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						hideProgressLayout();
						navigateToPostPreview();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						navigateToPostPreview();
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
		mQueue.add(mReq);
	}

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	//
	// PostAdSaver mAdSaver = PostAdSaver.getInstance(this);
	// mAdSaver.setCity(mbtnSelectCity.getText().toString());
	// mAdSaver.setAadharname(mEditAadharCardName.getText().toString());
	// mAdSaver.setAadharNumber(mEditAadharCardNumber.getText().toString());
	// mAdSaver.setAddress(meditAdress.getText().toString());
	// mAdSaver.setState(meditState.getText().toString());
	// mAdSaver.setPincode(mEditPinCode.getText().toString());
	// mAdSaver.setMobNumber(mEditPhone.getText().toString());
	// mAdSaver.setPanCard(meditPanCardNumber.getText().toString());
	//
	// mAdSaver.setUserCity(mbtnSelectUserCity.getText().toString());
	// mAdSaver.setUserAddress(meditUserAdress.getText().toString());
	// mAdSaver.setUserState(meditUserState.getText().toString());
	// mAdSaver.setUserPincode(mEditUserPinCode.getText().toString());
	// mAdSaver.setUserMobNumber(mEditUserPhone.getText().toString());
	// mAdSaver.setPanCardSelected(isPanCardSelected);
	// mAdSaver.setAaadharCardSelected(isAadharCardSelected);
	// mAdSaver.setProductAddressChecked(mCheckAddress.isChecked());
	//
	// mAdSaver.setEditing(true);
	// }

}
