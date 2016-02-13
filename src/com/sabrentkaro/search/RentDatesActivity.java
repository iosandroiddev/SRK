package com.sabrentkaro.search;

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

public class RentDatesActivity extends BaseActivity {

	private TextView mtxtStartDate, mtxtEndDate, mbtnNext;
	private String selectedProductAdId, mPrice, mProductDescription, mQuantity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_rent_details);
		getDetails();
		loadLayoutReferences();
	}

	private void loadLayoutReferences() {
		mtxtStartDate = (TextView) findViewById(R.id.btnStartDate);
		mtxtEndDate = (TextView) findViewById(R.id.btnEndDate);
		mbtnNext = (TextView) findViewById(R.id.btnNext);

		mtxtStartDate.setOnClickListener(this);
		mtxtEndDate.setOnClickListener(this);
		mbtnNext.setOnClickListener(this);

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
				selectedProductAdId = mBundle.getString("selectedAdId");
				mPrice = mBundle.getString("productPrice");
				mProductDescription = mBundle.getString("productDescription");
				mQuantity = mBundle.getString("quantity");
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
			mTxtView.setText((mm+1) + "/" +dd + "/" + yy);
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

		default:
			break;
		}
	}

	private void btnNextClicked() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
				navigateToAdressDocuments();
			}
		}
	}

	private void navigateToAdressDocuments() {
		Intent intent = new Intent(this, AddressDocumentsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedStartDate", mtxtStartDate.getText()
				.toString());
		mBundle.putString("selectedEndDate", mtxtEndDate.getText().toString());
		mBundle.putString("selectedAdId", selectedProductAdId);
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("locationId", "1");
		mBundle.putString("productPrice", mPrice);
		mBundle.putString("productDescription", mProductDescription);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	private void btnStartDateClicked() {
		showDatePicker(mtxtStartDate);
	}

	private void btnEndDateClicked() {
		showDatePicker(mtxtEndDate);
	}
}
