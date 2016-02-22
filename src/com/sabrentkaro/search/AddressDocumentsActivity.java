package com.sabrentkaro.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.R;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class AddressDocumentsActivity extends BaseActivity {

	private EditText meditAdress, meditCity, meditState, mEditPinCode,
			mEditPhone, meditPanCardNumber, mEditAadharCardName,
			mEditAadharCardNumber;
	private TextView mbtnPanCard, mbtnAadharCard, mbtnContinue;
	private boolean isPanCardSelected = false, isAadharCardSelected = false;
	private LinearLayout mPanCardLayout, mAAadharCardLayout;

	private String selectedProductAdId, mPrice, mProductDescription, mQuantity,
			mStartDate, mEndDate, mLocationId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_adress_documents);
		getDetails();
		loadReferences();
		loadDetails();
	}

	private void loadDetails() {
		meditAdress.setText(StorageClass.getInstance(this).getAddress());
		meditCity.setText(StorageClass.getInstance(this).getUserCity());
		meditState.setText(StorageClass.getInstance(this).getUserState());
		mEditPinCode.setText(StorageClass.getInstance(this).getPinCode());
		mEditPhone.setText(StorageClass.getInstance(this).getMobileNumber());
	}

	private void getDetails() {
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
			}
		}
	}

	private void loadReferences() {
		meditAdress = (EditText) findViewById(R.id.editAddress);
		meditCity = (EditText) findViewById(R.id.editCityTown);
		meditState = (EditText) findViewById(R.id.editState);
		mEditPinCode = (EditText) findViewById(R.id.editPincode);
		mEditPhone = (EditText) findViewById(R.id.editPhone);
		meditPanCardNumber = (EditText) findViewById(R.id.editPanCard);
		mEditAadharCardName = (EditText) findViewById(R.id.editAadharName);
		mEditAadharCardNumber = (EditText) findViewById(R.id.editAadharNumber);

		mbtnPanCard = (TextView) findViewById(R.id.btnPanCard);
		mbtnAadharCard = (TextView) findViewById(R.id.btnAadharCard);

		mPanCardLayout = (LinearLayout) findViewById(R.id.layoutPanCard);
		mAAadharCardLayout = (LinearLayout) findViewById(R.id.layoutAadharCard);
		mbtnContinue = (TextView) findViewById(R.id.btnContinue);
		mbtnPanCard.setOnClickListener(this);
		mbtnAadharCard.setOnClickListener(this);
		mbtnContinue.setOnClickListener(this);

		StaticUtils.setEditTextHintFont(meditAdress, this);
		StaticUtils.setEditTextHintFont(meditCity, this);
		StaticUtils.setEditTextHintFont(mEditPinCode, this);
		StaticUtils.setEditTextHintFont(meditState, this);
		StaticUtils.setEditTextHintFont(mEditPhone, this);
		StaticUtils.setEditTextHintFont(meditPanCardNumber, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardName, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardNumber, this);
		
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
		case R.id.btnContinue:
			btnContinueClicked();
			break;
		default:
			break;
		}
	}

	private void btnContinueClicked() {

		if (!isPanCardSelected && !isAadharCardSelected) {
			showToast("Please Select Documents");
		} else if (isPanCardSelected) {
			if (TextUtils.isEmpty(meditPanCardNumber.getText().toString())) {
				showToast("Please Enter Pan Card Number");
			} else {
				startOrdersActivity();
			}
		} else if (isAadharCardSelected) {
			if (TextUtils.isEmpty(mEditAadharCardName.getText().toString())) {
				showToast("Please Enter AadharCard Name");
			} else if (TextUtils.isEmpty(mEditAadharCardNumber.getText()
					.toString())) {
				showToast("Please Enter AadharCard Number");
			} else {
				startOrdersActivity();
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
		mBundle.putString("address", meditAdress.getText().toString());
		mBundle.putString("city", meditCity.getText().toString());
		mBundle.putString("state", meditState.getText().toString());
		mBundle.putString("pincode", mEditPinCode.getText().toString());
		mBundle.putString("mobile", mEditPhone.getText().toString());
		mBundle.putString("productDescription", mProductDescription);
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
			mEditAadharCardName.requestFocus();
			isAadharCardSelected = true;
			mbtnAadharCard.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.btn_select, 0, 0, 0);
			StaticUtils.expandCollapse(mAAadharCardLayout, true);

		}
	}

}
