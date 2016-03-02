package com.sabrentkaro.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.DatePickerUtility;
import com.utils.DatePickerUtility.IDatePickListener;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class RentDatesActivity extends BaseActivity implements
		IDatePickListener {

	private TextView mtxtStartDate, mtxtEndDate, mbtnNext;
	private String selectedProductAdId, mPrice, mProductDescription, mQuantity;
	private final SimpleDateFormat isoFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	private TextView mtxtDateField;
	private String mStartDateStr = "";
	private String mStartEndStr = "";

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

	private void showDateTimeDialog(final TextView mtxtValue) {
		this.mtxtDateField = mtxtValue;
		DatePickerUtility datePicker = new DatePickerUtility(this, this, false,
				true);
		datePicker.showDialog();
	}

	@Override
	public void onDateSet(Calendar calendarSelected, Date dateSelected,
			int year, String monthFullName, String monthShortName,
			int monthNumber, int date, String weekDayFullName,
			String weekDayShortName, int hour24, int hour12, int min, int sec,
			String AM_PM) {
		String mDateString = (monthNumber + 1) + "-" + date + "-" + year + " "
				+ hour24 + ":" + min + ":" + sec;
		String mConvertedString = parseDateToddMMyyyy(mDateString);
		if (mtxtDateField.getId() == R.id.btnStartDate) {
			mStartDateStr = mConvertedString;
		} else {
			mStartEndStr = mConvertedString;
		}
		mtxtDateField.setText((monthNumber + 1) + "/" + date + "/" + year);
	}

	public String parseDateToddMMyyyy(String time) {
		String inputPattern = "MM-dd-yyyy HH:mm:ss";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

		Date date = null;
		String str = null;
		try {
			date = inputFormat.parse(time);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
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
		if (mtxtStartDate.getText().toString().equalsIgnoreCase("Start Date")) {
			showToast("Please Select Start Date");
		} else if (mtxtEndDate.getText().toString()
				.equalsIgnoreCase("End Date")) {
			showToast("Please Select End Date");
		} else {
			if (startDate != null && endDate != null) {
				if (startDate.before(today) || endDate.before(today)) {
					showToast("Kindly ensure start date is greater than or equal to end date and start date is greater than system date");
				} else if (endDate.before(startDate)) {
					showToast("Kindly ensure start date is greater than or equal to end date and start date is greater than system date");
				} else {
					initiateAvailableDatesApi();
					// navigateToAdressDocuments();
				}
			} else {

			}
		}
	}

	private void initiateAvailableDatesApi() {
		final String mAuthHeader = StorageClass.getInstance(this)
				.getAuthHeader();
		showProgressLayout();
		JSONObject params = new JSONObject();
		try {
			params.put("FromDate", mStartDateStr);
			params.put("ToDate", mStartEndStr);
			params.put("Quantity", mQuantity);
			params.put("LocationId", "1");
			params.put("AdId", selectedProductAdId);
			params.put("Location", StorageClass.getInstance(this).getUserCity());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.GETADAVAILABILITYCALENDAR, params,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						showToast("Success");
						resposneForDatesApi(response);
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

	private void resposneForDatesApi(JSONObject response) {
		navigateToAdressDocuments();
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
		// showDatePicker(mtxtStartDate);
		showDateTimeDialog(mtxtStartDate);
	}

	private void btnEndDateClicked() {
		// showDatePicker(mtxtEndDate);
		showDateTimeDialog(mtxtEndDate);
	}

	@Override
	public void onDatePickerCancel() {

	}
}
