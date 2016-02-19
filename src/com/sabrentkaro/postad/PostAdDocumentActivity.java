package com.sabrentkaro.postad;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.R;
import com.utils.StaticUtils;

public class PostAdDocumentActivity extends BaseActivity {

	private EditText meditAdress, meditCity, meditState, mEditPinCode,
			mEditPhone, meditPanCardNumber, mEditAadharCardName,
			mEditAadharCardNumber;
	private TextView mbtnPanCard, mbtnAadharCard, mbtnNext;
	private boolean isPanCardSelected = false, isAadharCardSelected = false;
	private LinearLayout mPanCardLayout, mAAadharCardLayout;
	private CheckBox mCheckAddress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_documents);
		loadReferences();

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
		mCheckAddress = (CheckBox) findViewById(R.id.checkAddress);
		mbtnNext = (TextView) findViewById(R.id.btnNext);
		mbtnPanCard.setOnClickListener(this);
		mbtnAadharCard.setOnClickListener(this);
		mbtnNext.setOnClickListener(this);

		StaticUtils.setEditTextHintFont(meditAdress, this);
		StaticUtils.setEditTextHintFont(meditCity, this);
		StaticUtils.setEditTextHintFont(mEditPinCode, this);
		StaticUtils.setEditTextHintFont(meditState, this);
		StaticUtils.setEditTextHintFont(mEditPhone, this);
		StaticUtils.setEditTextHintFont(meditPanCardNumber, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardName, this);
		StaticUtils.setEditTextHintFont(mEditAadharCardNumber, this);
		StaticUtils.setCheckBoxFont(mCheckAddress, this);
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
		default:
			break;
		}
	}

	private void btnClearClicked() {
		meditAdress.setText("");
		meditCity.setText("");
		meditState.setText("");
		mEditPinCode.setText("");
		mEditPhone.setText("");
		meditPanCardNumber.setText("");
		mEditAadharCardName.setText("");
		mEditAadharCardNumber.setText("");
	}

	private void btnNextClicked() {

		if (meditAdress.getText().toString().length() == 0) {
			showToast("Please Enter Address");
		} else {
			if (meditCity.getText().toString().length() == 0) {
				showToast("Please Enter City");
			} else {
				if (meditState.getText().toString().length() == 0) {
					showToast("Please Enter State");
				} else {
					if (mEditPinCode.getText().toString().length() == 0) {
						showToast("Please Enter Pincode");
					} else {
						if (mEditPhone.getText().toString().length() == 0) {
							showToast("Please Enter Phone Number");
						} else {
							if (!isPanCardSelected && !isAadharCardSelected) {
								showToast("Please Select Documents");
							} else if (isPanCardSelected) {
								if (TextUtils.isEmpty(meditPanCardNumber
										.getText().toString())) {
									showToast("Please Enter Pan Card Number");
								} else {
									navigateToPostDates();
								}
							} else if (isAadharCardSelected) {
								if (TextUtils.isEmpty(mEditAadharCardName
										.getText().toString())) {
									showToast("Please Enter AadharCard Name");
								} else if (TextUtils
										.isEmpty(mEditAadharCardNumber
												.getText().toString())) {
									showToast("Please Enter AadharCard Number");
								} else {
									navigateToPostDates();
								}
							}
						}
					}

				}

			}

		}

	}

	private void navigateToPostDates() {
		Intent mIntent = new Intent(this, PostDatesActivity.class);
		startActivity(mIntent);
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
