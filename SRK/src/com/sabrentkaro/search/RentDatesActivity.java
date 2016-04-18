package com.sabrentkaro.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
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
import com.utils.StorageClass;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

public class RentDatesActivity extends BaseActivity implements
		IDatePickListener, OnDateSetListener {

	private TextView mtxtStartDate, mtxtEndDate, mbtnNext;
	private String selectedProductAdId, mPrice, mProductDescription, mQuantity,
			mMonthPrice, mWeekPrice, mSecurityDeposit;
	private final SimpleDateFormat isoFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	private TextView mtxtDateField;
	private String mStartDateStr = "";
	private String mStartEndStr = "";
	private String mAuthHeader = "";
	private Calendar[] mAvaiableDatesCalendar;
	private String itemDetailsArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_rent_details);
		getDetails();
		loadLayoutReferences();
		initCalendarAvailablityApi();
	}

	private void initCalendarAvailablityApi() {
		mAuthHeader = StorageClass.getInstance(this).getAuthHeader();
		showProgressLayout();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(c.getTime());

		String endDate = strDate; // Start date
		try {
			c.setTime(sdf.parse(endDate));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		c.add(Calendar.DATE, 180); // number of days to add
		endDate = sdf.format(c.getTime());
		JSONObject params = new JSONObject();
		try {
			params.put("FromDate", strDate);
			params.put("ToDate", endDate);
			params.put("Quantity", mQuantity);
			params.put("AdId", selectedProductAdId);
			params.put("RenterId", StorageClass.getInstance(this).getUserId());
			params.put("LocationId", StorageClass.getInstance(this)
					.getCityValue());
			params.put("Location", StorageClass.getInstance(this).getCity());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.GETADAVAILABILITYCALENDAR, params,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						getAvaialbleDates(response);
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						showToast("Unable to Retrieve Availablity Dates for this product");
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

	private void showErrorAlert() {
		new AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage("No Available Dates for this product.")
				.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog.cancel();
						finish();
					}
				})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
	}

	private void getAvaialbleDates(JSONObject response) {
		if (response != null) {
			if (response.optJSONArray("AvailableDates") != null) {
				JSONArray mAvailableDatesArray = response
						.optJSONArray("AvailableDates");
				ArrayList<Calendar> calendarList = new ArrayList<Calendar>();
				for (int i = 0; i < mAvailableDatesArray.length(); i++) {
					String date = mAvailableDatesArray.optString(i);
					calendarList.add(prepareCalendar(date));
				}
				mAvaiableDatesCalendar = calendarList
						.toArray(new Calendar[calendarList.size()]);
			} else {
				showErrorAlert();
			}
		} else {
			showErrorAlert();
		}

	}

	public Calendar prepareCalendar(String parse) {
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
					.parse(parse);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
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
				mMonthPrice = mBundle.getString("productPriceMonth");
				mWeekPrice = mBundle.getString("productPriceweek");
				mProductDescription = mBundle.getString("productDescription");
				mQuantity = mBundle.getString("quantity");
				mSecurityDeposit = mBundle.getString("securitDeposit");
				itemDetailsArray = mBundle.getString("mItemDetailsArray");
			}
		}
	}

	private void showDateTimeDialog(final TextView mtxtValue) {
		this.mtxtDateField = mtxtValue;
		DatePickerUtility datePicker = new DatePickerUtility(this, this, false,
				true);
		Calendar cal = Calendar.getInstance();
		datePicker.setMinDate(cal.YEAR, cal.MONTH, cal.DAY_OF_MONTH);
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
					navigateToAdressDocuments();
				}
			} else {

			}
		}
	}

	private void navigateToAdressDocuments() {
		String mLocValue = StorageClass.getInstance(this).getCityValue();
		Intent intent = new Intent(this, AddressDocumentsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedStartDate", mtxtStartDate.getText()
				.toString());
		mBundle.putString("selectedEndDate", mtxtEndDate.getText().toString());
		mBundle.putString("selectedAdId", selectedProductAdId);
		mBundle.putString("quantity", mQuantity);
		mBundle.putString("locationId", mLocValue);
		mBundle.putString("productPrice", mPrice);
		mBundle.putString("productPriceMonth", mMonthPrice);
		mBundle.putString("productPriceWeek", mWeekPrice);
		mBundle.putString("securitDeposit", mSecurityDeposit);
		mBundle.putString("productDescription", mProductDescription);
		mBundle.putString("startDate", mStartDateStr);
		mBundle.putString("endDate", mStartEndStr);
		mBundle.putString("mItemDetailsArray", itemDetailsArray);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	private void btnStartDateClicked() {
		// showDatePicker(mtxtStartDate);
		// showDateTimeDialog(mtxtStartDate);
		if (mAvaiableDatesCalendar != null
				&& mAvaiableDatesCalendar.length != 0) {
			this.mtxtDateField = mtxtStartDate;
			Calendar now = Calendar.getInstance();
			DatePickerDialog dpd = DatePickerDialog.newInstance(
					RentDatesActivity.this, now.get(Calendar.YEAR),
					now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
			dpd.setThemeDark(false);
			dpd.vibrate(true);
			dpd.dismissOnPause(false);
			dpd.showYearPickerFirst(false);
			dpd.setSelectableDays(mAvaiableDatesCalendar);
			dpd.show(getFragmentManager(), "Datepickerdialog");
		} else {
			showErrorAlert();
		}

	}

	private void btnEndDateClicked() {
		// showDatePicker(mtxtEndDate);
		if (mAvaiableDatesCalendar != null
				&& mAvaiableDatesCalendar.length != 0) {
			this.mtxtDateField = mtxtEndDate;
			Calendar now = Calendar.getInstance();
			DatePickerDialog dpd = DatePickerDialog.newInstance(
					RentDatesActivity.this, now.get(Calendar.YEAR),
					now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
			dpd.setThemeDark(false);
			dpd.vibrate(true);
			dpd.dismissOnPause(false);
			dpd.showYearPickerFirst(false);
			dpd.setSelectableDays(mAvaiableDatesCalendar);
			dpd.show(getFragmentManager(), "Datepickerdialog");
		} else {
			showErrorAlert();
		}
	}

	@Override
	public void onDatePickerCancel() {

	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear,
			int dayOfMonth) {

		String mDateString = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year
				+ " " + "00" + ":" + "00" + ":" + "00";
		String mConvertedString = parseDateToddMMyyyy(mDateString);
		if (mtxtDateField.getId() == R.id.btnStartDate) {
			mStartDateStr = mConvertedString;
		} else {
			mStartEndStr = mConvertedString;
		}
		mtxtDateField
				.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

	}
}
