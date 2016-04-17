package com.utils;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class PopUtils {

	public interface SpinnerCallBack {
		void onItemSelected(int pos);
	}

	public static void SingleChoice(String mTitle, final Context mContext,
			final TextView txtView, final String[] mStringArray,
			final SpinnerCallBack mCallBack) {
		int position = -1;
		try {
			position = (Integer) txtView.getTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle);
		builder.setSingleChoiceItems(mStringArray, position,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtView.setTag(which);
						txtView.setText(mStringArray[which]);
						mCallBack.onItemSelected(which);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void SingleChoice(String title, final Context act,
			final String[] array, final TextView txtView) {
		int position = -1;
		try {
			position = (Integer) txtView.getTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(title);
		builder.setSingleChoiceItems(array, position,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txtView.setText(array[which]);
						txtView.setTag(which);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void SingleChoiceForView(String title, final Context act,
			final String[] array, final ResultOK mResult, final TextView txtView) {
		int position = -1;
		try {
			position = (Integer) txtView.getTag();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(title);
		builder.setSingleChoiceItems(array, position,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						txtView.setText(array[which]);
						txtView.setTag(which);
						dialog.dismiss();
						if (mResult != null)
							mResult.btnOKClicked(array[which].toString());
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public interface ResultOK {
		public void btnOKClicked(String mString);
	}

	public interface ResultOKOne extends ResultOK {
		public void btnOKClicked(String mString, List<?> mResList);
	}

	public interface ResultMultipleOK {
		public void btnOKClicked(String mString, boolean[] checkedValues);
	}

	public void MultipleChoice(String title, final FragmentActivity act,
			final List<?> array, final ResultMultipleOK mResult, String items) {
		Builder builder = new AlertDialog.Builder(act);
		final boolean[] mCheckValues = new boolean[array.size()];
		String mDecideCheck = "";
		try {
			try {
				mDecideCheck = items;
			} catch (Exception e) {
				e.printStackTrace();
				mDecideCheck = "";
			}
			final String a[] = new String[array.size()];// array.toArray(new
														// String[array.size()]);
			for (int i = 0; i < array.size(); i++) {
				Object o = array.get(i);
				a[i] = o.toString();
				if (mDecideCheck.contains(a[i]))
					mCheckValues[i] = true;
				else
					mCheckValues[i] = false;
			}

			builder.setMultiChoiceItems(a, mCheckValues,
					new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							mCheckValues[which] = isChecked;
						}
					});

			builder.setNegativeButton("Cancel", null);
			builder.setPositiveButton("Set",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String mAllValues = "";
							int i = 0;
							for (boolean str : mCheckValues) {
								if (str)
									mAllValues += array.get(i) + ",";
								i++;
							}
							if (!mAllValues.equalsIgnoreCase("")) {
								mAllValues = mAllValues.substring(0,
										mAllValues.length() - 1);
								// txtView.setText(mAllValues);
								// txtView.setTag(mAllValues);
							}
							dialog.dismiss();

							if (mResult != null) {
								mResult.btnOKClicked(mAllValues, mCheckValues);
							}
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

			// Builder builder = new AlertDialog.Builder(act);
			// builder.setTitle(title);
			// builder.setSingleChoiceItems(a, position,
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// txtView.setText(a[which]);
			// txtView.setTag(which);
			// txtView.setTag(R.id.array, array);
			// dialog.dismiss();
			// }
			// });
			// builder.setNegativeButton("Cancel",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// }
			// });
			// AlertDialog alert = builder.create();
			// alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
