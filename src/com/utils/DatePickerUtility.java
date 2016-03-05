package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

@SuppressLint("NewApi")
public class DatePickerUtility implements OnClickListener {
	private DatePicker datePicker;
	private TimePicker timePicker;

	private final int SET = 102, CANCEL = 103;

	private Button btn_set, btn_cancel;

	private Context activity;

	private IDatePickListener iCustomDateTimeListener = null;

	private Dialog dialog;

	private Calendar calendar_date;

	private int minYear, minMonth, minDay;

	private int minHr_24hrformat, minMin;

	private int maxHr_24hrformat = 23, maxMin = 59;

	private int hrs_24hrformat = -1, mins = -1;

	private Calendar minCal = null;
	private Calendar maxCal = null;

	private boolean is24HrFormat = false;

	public DatePickerUtility(Context a,
			IDatePickListener customDateTimeListener, boolean showTimePicker,
			boolean is24HrFormat) {
		activity = a;
		iCustomDateTimeListener = customDateTimeListener;

		this.showTimePicker = showTimePicker;
		this.is24HrFormat = is24HrFormat;

		dialog = new Dialog(activity);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (iCustomDateTimeListener != null)
					iCustomDateTimeListener.onDatePickerCancel();
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View dialogView = getDatePickerLayout();
		dialog.setContentView(dialogView);
	}

	public DatePickerUtility(FragmentActivity a,
			IDatePickListener customDateTimeListener) {
		activity = a;
		iCustomDateTimeListener = customDateTimeListener;
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View dialogView = getDatePickerLayout();
		dialog.setContentView(dialogView);
	}

	@SuppressWarnings("deprecation")
	public View getDatePickerLayout() {

		LinearLayout.LayoutParams linear_match_wrap = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams linear_wrap_wrap = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

		LinearLayout linear_main = new LinearLayout(activity);
		linear_main.setLayoutParams(linear_match_wrap);

		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			// linear_main.setBackgroundDrawable(activity.getResources()
			// .getDrawable(R.drawable.dialog_popup));
		} else {
			// linear_main.setBackground(activity.getResources().getDrawable(
			// R.drawable.dialog_popup));
		}

		linear_main.setOrientation(LinearLayout.VERTICAL);
		linear_main.setGravity(Gravity.CENTER);

		LinearLayout linear_child = new LinearLayout(activity);
		linear_child.setLayoutParams(linear_wrap_wrap);
		linear_child.setOrientation(LinearLayout.VERTICAL);

		datePicker = new DatePicker(activity);

		if (Build.VERSION.SDK_INT >= 11)
			datePicker.setCalendarViewShown(false);

		LinearLayout linear_bottom = new LinearLayout(activity);
		// linear_match_wrap.topMargin = 5;
		linear_bottom.setPadding(10, 15, 10, 15);
		linear_bottom.setLayoutParams(linear_match_wrap);

		btn_set = new Button(activity);
		button_params.setMargins(10, 0, 5, 0);
		btn_set.setLayoutParams(button_params);
		btn_set.setText(android.R.string.ok);
		btn_set.setId(SET);

		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			// btn_set.setBackgroundDrawable(activity.getResources().getDrawable(
			// R.drawable.normal_button_selector));
		} else {
			// btn_set.setBackground(activity.getResources().getDrawable(
			// R.drawable.normal_button_selector));
		}

		// btn_set.setTextColor(activity.getResources().getColor(color.white));
		btn_set.setOnClickListener(this);

		btn_cancel = new Button(activity);

		button_params.setMargins(5, 0, 10, 0);
		btn_cancel.setLayoutParams(button_params);
		btn_cancel.setText(android.R.string.cancel);
		btn_cancel.setId(CANCEL);

		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			// btn_cancel.setBackgroundDrawable(activity.getResources()
			// .getDrawable(R.drawable.normal_button_selector));
		} else {
			// btn_cancel.setBackground(activity.getResources().getDrawable(
			// R.drawable.normal_button_selector));
		}

		// btn_cancel
		// .setTextColor(activity.getResources().getColor(R.color.white));
		btn_cancel.setOnClickListener(this);

		linear_bottom.addView(btn_cancel);
		linear_bottom.addView(btn_set);

		linear_child.addView(datePicker);

		if (showTimePicker) {
			LinearLayout linear_bottom_timePicker = new LinearLayout(activity);
			linear_bottom_timePicker.setLayoutParams(linear_match_wrap);
			linear_bottom_timePicker.setGravity(Gravity.CENTER);
			timePicker = new TimePicker(activity);
			linear_bottom_timePicker.addView(timePicker);

			linear_child.addView(linear_bottom_timePicker);
		}

		linear_child.addView(linear_bottom);

		linear_main.addView(linear_child);

		ScrollView mScrollView = new ScrollView(activity);
		mScrollView.setLayoutParams(linear_match_wrap);
		mScrollView.addView(linear_main);
		return mScrollView;
	}

	public void setMinTime(int hrs_24HrFormat, int minutes) {
		if (hrs_24HrFormat >= 0 && hrs_24HrFormat < 24)
			this.minHr_24hrformat = hrs_24HrFormat;

		if (minutes >= 0 && minutes < 60)
			this.minMin = minutes;
	}

	public void setMaxTime(int hrs_24HrFormat, int minutes) {
		if (hrs_24HrFormat >= 0 && hrs_24HrFormat < 24)
			this.maxHr_24hrformat = hrs_24HrFormat;

		if (minutes >= 0 && minutes < 60)
			this.maxMin = minutes;

		this.maxHr_24hrformat = this.maxHr_24hrformat == 0 ? 23
				: this.maxHr_24hrformat;
		this.maxMin = this.maxMin == 0 ? 59 : this.maxMin;
	}

	public void setTime(int hrs_24HrFormat, int minutes) {
		if (hrs_24HrFormat >= 0 && hrs_24HrFormat < 24)
			this.hrs_24hrformat = hrs_24HrFormat;

		if (minutes >= 0 && minutes < 60)
			this.mins = minutes;
	}

	// public void setUpdateDate(int year, int month, int day) {
	// minYear = year;
	// minMonth = month;
	// minDay = day;
	//
	// if (Build.VERSION.SDK_INT >= 11) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(year, month, day);
	//
	// long minMillis = calendar.getTimeInMillis();
	// long maxMillis = datePicker.getMaxDate();
	//
	// if (maxMillis > minMillis) {
	// Calendar c = Calendar.getInstance();
	//
	// if (c.get(Calendar.YEAR) == year
	// && c.get(Calendar.DAY_OF_YEAR) == calendar
	// .get(Calendar.DAY_OF_YEAR)) {
	// c.set(year, month, day, 0, 0);
	// minMillis = c.getTimeInMillis();
	// }
	//
	// datePicker.updateDate(year, month, day);
	// } else {
	// calendar.setTimeInMillis(maxMillis);
	// year = calendar.get(Calendar.YEAR);
	// month = calendar.get(Calendar.MONTH);
	// day = calendar.get(Calendar.DATE);
	// datePicker.setMinDate(maxMillis);
	// }
	// } else {
	// // minDate = new Date(minYear, minMonth, minDay);
	// minCal = Calendar.getInstance();
	// minCal.set(minYear, minMonth, minDay);
	// }
	// }

	@TargetApi(11)
	public void setMinDate(int year, int month, int day) {
		minYear = year;
		minMonth = month;
		minDay = day;

		if (Build.VERSION.SDK_INT >= 11) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);

			long minMillis = calendar.getTimeInMillis();
			long maxMillis = datePicker.getMaxDate();

			if (maxMillis > minMillis) {
				Calendar c = Calendar.getInstance();

				if (c.get(Calendar.YEAR) == year
						&& c.get(Calendar.DAY_OF_YEAR) == calendar
								.get(Calendar.DAY_OF_YEAR)) {
					c.set(year, month, day, 0, 0);
					minMillis = c.getTimeInMillis();
				}

				datePicker.setMinDate(minMillis);
			} else {
				calendar.setTimeInMillis(maxMillis);
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
				day = calendar.get(Calendar.DAY_OF_MONTH);
				datePicker.setMinDate(maxMillis);
			}
		} else {
			// minDate = new Date(minYear, minMonth, minDay);
			minCal = Calendar.getInstance();
			minCal.set(minYear, minMonth, minDay);
		}
	}

	@TargetApi(11)
	public void setMaxDate(int year, int month, int day) {
		if (Build.VERSION.SDK_INT >= 11) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);

			long minMillis = datePicker.getMinDate();
			long maxMillis = calendar.getTimeInMillis();

			if (maxMillis < minMillis)
				maxMillis = minMillis;

			datePicker.setMaxDate(maxMillis);
		} else {
			// maxDate = new Date(year, month, day);
			maxCal = Calendar.getInstance();
			maxCal.set(year, month, day);
		}
	}

	public void showDialog() {
		if (!dialog.isShowing()) {
			if (calendar_date == null)
				calendar_date = Calendar.getInstance();
			int year = calendar_date.get(Calendar.YEAR);
			int month = calendar_date.get(Calendar.MONTH);
			int day = calendar_date.get(Calendar.DATE);

			if (minCal != null && maxCal != null) {
				if (maxCal.before(minCal) || minCal.after(maxCal))
					maxCal = (Calendar) minCal.clone();
			}
			if (minCal != null)
				datePicker.init(minYear, minMonth, minDay, dateChangedListener);
			else if (maxCal != null)
				datePicker.init(year, month, day, dateChangedListener);

			if (minYear > 0) {
				Calendar calendar_min = Calendar.getInstance();
				calendar_min.set(minYear, minMonth, minDay);

				if (calendar_min.after(calendar_date))
					datePicker.updateDate(minYear, minMonth, minDay);
				else
					datePicker.updateDate(year, month, day);
			} else
				datePicker.updateDate(year, month, day);

			if (timePicker != null) {
				timePicker.setIs24HourView(is24HrFormat);

				if (hrs_24hrformat == -1) {
					hrs_24hrformat = Calendar.getInstance().get(
							Calendar.HOUR_OF_DAY);
				}

				if (mins == -1) {
					mins = Calendar.getInstance().get(Calendar.MINUTE);
				}

				if (hrs_24hrformat > maxHr_24hrformat) {
					hrs_24hrformat = maxHr_24hrformat;
					mins = maxMin;
				} else if (hrs_24hrformat < minHr_24hrformat) {
					hrs_24hrformat = minHr_24hrformat;

					if (mins < minMin) {
						mins = minMin;
					}
				} else if (hrs_24hrformat == minHr_24hrformat) {
					if (mins < minMin) {
						mins = minMin;
					}
				} else if (hrs_24hrformat == maxHr_24hrformat) {
					if (mins > maxMin) {
						mins = maxMin;
					}
				}

				timePicker.setCurrentHour(hrs_24hrformat);
				timePicker.setCurrentMinute(mins);

				timePicker.setOnTimeChangedListener(timeChangedListener);
			}

			dialog.show();
		}
	}

	public void dismissDialog() {
		if (!dialog.isShowing())
			dialog.dismiss();
	}

	public void setDate(Calendar calendar) {
		if (calendar != null)
			calendar_date = calendar;
	}

	public void setDate(Date date) {
		if (date != null) {
			calendar_date = Calendar.getInstance();
			calendar_date.setTime(date);
		}
	}

	public void setDate(int year, int month, int day) {
		if (month < 12 && month >= 0 && day < 32 && day >= 0 && year > 100
				&& year < 3000) {
			calendar_date = Calendar.getInstance();
			calendar_date.set(year, month, day);
		}
	}

	// public void setDateUpdate(int year, int month, int day) {
	// if (month < 12 && month >= 0 && day < 32 && day >= 0 && year > 100
	// && year < 3000) {
	// calendar_date = Calendar.getInstance();
	// calendar_date.updateDate(year, month, day);
	// datePicker.updateDate(year, month, day);
	// }
	// }
	public interface IDatePickListener {
		public void onDateSet(Calendar calendarSelected, Date dateSelected,
				int year, String monthFullName, String monthShortName,
				int monthNumber, int date, String weekDayFullName,
				String weekDayShortName, int hour24, int hour12, int min,
				int sec, String AM_PM);

		public void onDatePickerCancel();
	}

	private int getHourIn12Format(int hour24) {
		int hourIn12Format = 0;

		if (hour24 == 0)
			hourIn12Format = 12;
		else if (hour24 <= 12)
			hourIn12Format = hour24;
		else
			hourIn12Format = hour24 - 12;

		return hourIn12Format;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case SET:
			if (dialog.isShowing())
				dialog.dismiss();
			if (iCustomDateTimeListener != null) {
				int month = datePicker.getMonth();
				int year = datePicker.getYear();
				int day = datePicker.getDayOfMonth();

				calendar_date.set(year, month, day);

				if (timePicker != null) {
					int hourOfDay = timePicker.getCurrentHour().intValue();
					int minute = timePicker.getCurrentMinute().intValue();

					calendar_date.set(year, month, day, hourOfDay, minute);
				}

				iCustomDateTimeListener.onDateSet(calendar_date, calendar_date
						.getTime(), calendar_date.get(Calendar.YEAR),
						getMonthFullName(calendar_date.get(Calendar.MONTH)),
						getMonthShortName(calendar_date.get(Calendar.MONTH)),
						calendar_date.get(Calendar.MONTH), calendar_date
								.get(Calendar.DAY_OF_MONTH),
						getWeekDayFullName(calendar_date
								.get(Calendar.DAY_OF_WEEK)),
						getWeekDayShortName(calendar_date
								.get(Calendar.DAY_OF_WEEK)), calendar_date
								.get(Calendar.HOUR_OF_DAY),
						getHourIn12Format(calendar_date
								.get(Calendar.HOUR_OF_DAY)), calendar_date
								.get(Calendar.MINUTE), calendar_date
								.get(Calendar.SECOND), getAMPM(calendar_date));
			}
			break;

		case CANCEL:
			if (dialog.isShowing())
				dialog.dismiss();
			if (iCustomDateTimeListener != null)
				iCustomDateTimeListener.onDatePickerCancel();
			break;
		}
	}

	private String getAMPM(Calendar calendar) {
		String ampm = (calendar.get(Calendar.AM_PM) == (Calendar.AM)) ? "AM"
				: "PM";
		return ampm;
	}

	/**
	 * @param monthNumber
	 *            Month Number starts with 0. For <b>January</b> it is <b>0</b>
	 *            and for <b>December</b> it is <b>11</b>.
	 * @return
	 */
	private String getMonthFullName(int monthNumber) {
		String monthName = "";

		if (monthNumber >= 0 && monthNumber < 12)
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, monthNumber, Calendar.DAY_OF_MONTH);
				// calendar.set(Calendar.MONTH, monthNumber);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
				simpleDateFormat.setCalendar(calendar);
				monthName = simpleDateFormat.format(calendar.getTime());
			} catch (Exception e) {
				if (e != null)
					e.printStackTrace();
			}

		return monthName;
	}

	/**
	 * @param monthNumber
	 *            Month Number starts with 0. For <b>January</b> it is <b>0</b>
	 *            and for <b>December</b> it is <b>11</b>.
	 * @return
	 */
	private String getMonthShortName(int monthNumber) {
		String monthName = "";

		if (monthNumber >= 0 && monthNumber < 12)
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, monthNumber, Calendar.DAY_OF_MONTH);
				// calendar.set(Calendar.MONTH, monthNumber);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
				simpleDateFormat.setCalendar(calendar);
				monthName = simpleDateFormat.format(calendar.getTime());
			} catch (Exception e) {
				if (e != null)
					e.printStackTrace();
			}
		return monthName;
	}

	/**
	 * @param weekDayNumber
	 *            Week Number starts with 1. For <b>Sunday</b> it is <b>1</b>
	 *            and for <b>Saturday</b> it is <b>7</b>.
	 * @return
	 */
	private String getWeekDayFullName(int weekDayNumber) {
		String weekName = "";

		if (weekDayNumber > 0 && weekDayNumber < 8) {
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
				simpleDateFormat.setCalendar(calendar);
				weekName = simpleDateFormat.format(calendar.getTime());
			} catch (Exception e) {
				if (e != null)
					e.printStackTrace();
			}
		}
		return weekName;
	}

	/**
	 * @param weekDayNumber
	 *            Week Number starts with 1. For <b>Sunday</b> it is <b>1</b>
	 *            and for <b>Saturday</b> it is <b>7</b>.
	 * @return
	 */
	private String getWeekDayShortName(int weekDayNumber) {
		String weekName = "";
		if (weekDayNumber > 0 && weekDayNumber < 8) {
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE");
				simpleDateFormat.setCalendar(calendar);
				weekName = simpleDateFormat.format(calendar.getTime());
			} catch (Exception e) {
				if (e != null)
					e.printStackTrace();
			}
		}
		return weekName;
	}

	public void resetData() {
		calendar_date = null;

		minDay = 0;
		minMonth = 0;
		minYear = 0;

		minCal = null;
		maxCal = null;

		minHr_24hrformat = 0;
		minMin = 0;
		maxHr_24hrformat = 23;
		maxMin = 59;

		hrs_24hrformat = -1;
		mins = -1;
	}

	private OnDateChangedListener dateChangedListener = new OnDateChangedListener() {
		@Override
		public void onDateChanged(DatePicker view, int year, int month, int day) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day);

				if (cal.before(minCal)) // if min
					datePicker.updateDate(minCal.get(Calendar.YEAR),
							minCal.get(Calendar.MONTH),
							minCal.get(Calendar.DAY_OF_MONTH));
				else if (cal.after(maxCal))
					datePicker.updateDate(maxCal.get(Calendar.YEAR),
							maxCal.get(Calendar.MONTH),
							maxCal.get(Calendar.DAY_OF_MONTH));

			} catch (Exception e) {
				if (e != null)
					e.printStackTrace();
			}
		}
	};

	private OnTimeChangedListener timeChangedListener = new OnTimeChangedListener() {
		@Override
		public void onTimeChanged(TimePicker picker, int hourOfDay, int minute) {
			try {
				if (showTimePicker) {
					int month = datePicker.getMonth();
					int year = datePicker.getYear();
					int day = datePicker.getDayOfMonth();

					Calendar selectedCal = Calendar.getInstance();
					selectedCal.set(year, month, day);

					Calendar todayCalendar = Calendar.getInstance();
					if (selectedCal.after(todayCalendar)) {
						hrs_24hrformat = picker.getCurrentHour();
						mins = picker.getCurrentMinute();
						return;
					}
				}

				boolean isHrValid = (hourOfDay <= maxHr_24hrformat && hourOfDay >= minHr_24hrformat);

				if (!isHrValid) {
					picker.setOnTimeChangedListener(null);
					picker.setCurrentHour(hrs_24hrformat);
					picker.setOnTimeChangedListener(timeChangedListener);
				}

				if ((hourOfDay == minHr_24hrformat)
						|| (hourOfDay == maxHr_24hrformat)) {
					if ((hourOfDay == minHr_24hrformat)) {
						if (minute < minMin) {
							minute = minMin;
						}
					} else {
						if (minute > maxMin) {
							minute = maxMin;
						}
					}

					picker.setOnTimeChangedListener(null);
					picker.setCurrentMinute(minute);
					picker.setOnTimeChangedListener(timeChangedListener);
				}

				hrs_24hrformat = picker.getCurrentHour();
				mins = picker.getCurrentMinute();
			} catch (Exception e) {

			}
		}
	};

	public static int getMonthNumberFromMonthString(String mDOBArray) {
		switch (mDOBArray) {
		case "Jan":
			return 0;
		case "Feb":
			return 1;
		case "Mar":
			return 2;
		case "Apr":
			return 3;
		case "May":
			return 4;
		case "Jun":
			return 5;
		case "Jul":
			return 6;
		case "Aug":
			return 7;
		case "Sep":
			return 8;
		case "Oct":
			return 9;
		case "Nov":
			return 10;
		case "Dec":
			return 11;
		default:
			return 0;
		}
	}

	private boolean showTimePicker = false;

	public void showDialog(int day1, int month1, int year1) {
		// TODO Auto-generated method stub

		if (!dialog.isShowing()) {
			if (calendar_date == null)
				calendar_date = Calendar.getInstance();
			// int year11 = calendar_date.get(Calendar.YEAR);
			// int month11 = calendar_date.get(Calendar.MONTH);
			// int day11 = calendar_date.get(Calendar.DATE);

			if (minCal != null && maxCal != null) {
				if (maxCal.before(minCal) || minCal.after(maxCal))
					maxCal = (Calendar) minCal.clone();
			}
			if (minCal != null)
				datePicker.init(minYear, minMonth, minDay, dateChangedListener);
			else if (maxCal != null)
				datePicker.init(year1, month1, day1, dateChangedListener);

			if (minYear > 0) {
				Calendar calendar_min = Calendar.getInstance();
				calendar_min.set(minYear, minMonth, minDay);

				if (calendar_min.after(calendar_date))
					datePicker.updateDate(minYear, minMonth, minDay);
				else
					datePicker.updateDate(year1, month1, day1);
			} else
				datePicker.updateDate(year1, month1, day1);

			if (timePicker != null) {
				timePicker.setIs24HourView(is24HrFormat);

				if (hrs_24hrformat == -1) {
					hrs_24hrformat = Calendar.getInstance().get(
							Calendar.HOUR_OF_DAY);
				}

				if (mins == -1) {
					mins = Calendar.getInstance().get(Calendar.MINUTE);
				}

				if (hrs_24hrformat > maxHr_24hrformat) {
					hrs_24hrformat = maxHr_24hrformat;
					mins = maxMin;
				} else if (hrs_24hrformat < minHr_24hrformat) {
					hrs_24hrformat = minHr_24hrformat;

					if (mins < minMin) {
						mins = minMin;
					}
				} else if (hrs_24hrformat == minHr_24hrformat) {
					if (mins < minMin) {
						mins = minMin;
					}
				} else if (hrs_24hrformat == maxHr_24hrformat) {
					if (mins > maxMin) {
						mins = maxMin;
					}
				}

				timePicker.setCurrentHour(hrs_24hrformat);
				timePicker.setCurrentMinute(mins);

				timePicker.setOnTimeChangedListener(timeChangedListener);
			}

			dialog.show();
		}

	}
}
