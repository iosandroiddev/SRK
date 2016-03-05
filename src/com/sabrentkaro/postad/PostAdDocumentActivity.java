package com.sabrentkaro.postad;

import java.util.ArrayList;
import java.util.HashMap;

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

import com.models.CityModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.PostAdSaver;
import com.sabrentkaro.R;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class PostAdDocumentActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private EditText meditAdress, meditState, mEditPinCode, mEditPhone,
			meditPanCardNumber, mEditAadharCardName, mEditAadharCardNumber,
			meditUserAdress, meditUserCity, meditUserState, mEditUserPinCode,
			mEditUserPhone;
	private TextView mbtnPanCard, mbtnAadharCard, mbtnNext, mbtnSelectCity,
			mbtnSelectUserCity, mbtnClear;
	private boolean isPanCardSelected = false, isAadharCardSelected = false;
	private LinearLayout mPanCardLayout, mAAadharCardLayout,
			mLayoutUserAddress;
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
				mbtnPanCard.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.btn_select, 0, 0, 0);
			} else {
				if (mSaver.isAaadharCardSelected()) {
					isAadharCardSelected = true;
					isPanCardSelected = false;
					mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.btn_select, 0, 0, 0);
				} else {

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

		mbtnPanCard = (TextView) findViewById(R.id.btnPanCard);
		mbtnAadharCard = (TextView) findViewById(R.id.btnAadharCard);

		mPanCardLayout = (LinearLayout) findViewById(R.id.layoutPanCard);
		mPanCardLayout.setVisibility(View.GONE);
		mAAadharCardLayout = (LinearLayout) findViewById(R.id.layoutAadharCard);
		mAAadharCardLayout.setVisibility(View.GONE);
		mLayoutUserAddress = (LinearLayout) findViewById(R.id.layoutUserAddress);
		mCheckAddress = (CheckBox) findViewById(R.id.checkAddress);
		mbtnNext = (TextView) findViewById(R.id.btnNext);
		mbtnClear = (TextView) findViewById(R.id.btnClear);
		mbtnPanCard.setOnClickListener(this);
		mbtnAadharCard.setOnClickListener(this);
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
												if (mEditAadharCardNumber
														.getText().toString()
														.length() != 12) {
													showToast("Please Enter Valid AadharCard Number");
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
		mBundle.putString("aadharCardName", mEditAadharCardName.getText()
				.toString());
		mBundle.putString("aadharCardNumber", mEditAadharCardNumber.getText()
				.toString());
		mBundle.putString("mobileNumber", mEditPhone.getText().toString());
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
		}
		mPanCardLayout.invalidate();
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
