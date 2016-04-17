package com.sabrentkaro.postad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.R;

public class PostDatesActivity extends BaseActivity {

	private TextView mtxtStartDate, mtxtEndDate, mbtnPostMyAd, mbtnCancel;
	private String selectedProductAdId, mPrice, mProductDescription, mQuantity;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_dates_activity);
		getDetails();
		loadLayoutReferences();
	}

	private void loadLayoutReferences() {
		mtxtStartDate = (TextView) findViewById(R.id.btnStartDate);
		mtxtEndDate = (TextView) findViewById(R.id.btnEndDate);
		mbtnPostMyAd = (TextView) findViewById(R.id.btnPostMyAd);
		mbtnCancel = (TextView) findViewById(R.id.btnCancel);

		mtxtStartDate.setOnClickListener(this);
		mtxtEndDate.setOnClickListener(this);
		mbtnPostMyAd.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
				selectedProductAdId = mBundle.getString("selectedAdId");
				mPrice = mBundle.getString("productPrice");
				mProductDescription = mBundle.getString("productDescription");
				mQuantity = mBundle.getString("quantity");

				mCategory = mBundle.getString("category");
				mSubCategory = mBundle.getString("subCategory");
				mAdTitle = mBundle.getString("adTitle");
				mProductDesc = mBundle.getString("productDescription");
				mProductCondition = mBundle.getString("productCondition");
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

			}
		}
	}

	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		private TextView mTxtView;

		public SelectDateFragment(TextView mTxtView) {
			this.mTxtView = mTxtView;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			mTxtView.setText((mm + 1) + "/" + dd + "/" + yy);
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			dialog.dismiss();
			super.onCancel(dialog);
		}
	}

	private void showDatePicker(TextView mtxtView) {
		DialogFragment newFragment = new SelectDateFragment(mtxtView);
		newFragment.show(getSupportFragmentManager(), "DatePicker");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnStartDate:
			btnStartDateClicked();
			break;
		case R.id.btnEndDate:
			btnEndDateClicked();
			break;
		case R.id.btnNext:
			btnNextClicked();
			break;
		case R.id.btnCancel:
			btnCancelClicked();
			break;
		default:
			break;
		}
	}

	private void btnCancelClicked() {

	}

	private void btnNextClicked() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		try {
			startDate = simpleDateFormat.parse(mtxtStartDate.getText()
					.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = null;
		try {
			endDate = simpleDateFormat.parse(mtxtEndDate.getText().toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date today = new Date();
		if (startDate != null && endDate != null) {
			if (startDate.before(today) || endDate.before(today)) {
				showToast("Kindly ensure start date is greater than or equal to end date and start date is greater than system date");
			} else if (endDate.before(startDate)) {
				showToast("Kindly ensure start date is greater than or equal to end date and start date is greater than system date");
			} else {
			}
		} else {
			if (mtxtStartDate.getText().toString()
					.equalsIgnoreCase("Select Date")) {
				showToast("Please Select Start Date");
			} else {
				showToast("Please Select End Date");
			}

		}
	}

	private void btnStartDateClicked() {
		showDatePicker(mtxtStartDate);
	}

	private void btnEndDateClicked() {
		showDatePicker(mtxtEndDate);
	}
}
