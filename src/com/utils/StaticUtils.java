package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("SimpleDateFormat")
public class StaticUtils {

	private static DateFormat formatter = SimpleDateFormat
			.getDateTimeInstance();

	public static boolean isProfilePic = false;

	public static <T extends ImageView> void setTabButtonState(T imageView,
			int selected, int normal) {
		Context ctx = imageView.getContext();
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] { android.R.attr.state_selected }, ctx
				.getResources().getDrawable(selected));
		states.addState(new int[] { android.R.attr.state_focused }, ctx
				.getResources().getDrawable(selected));
		states.addState(new int[] {}, ctx.getResources().getDrawable(normal));
		imageView.setImageDrawable(states);
	}

	@SuppressLint("NewApi")
	public static <T extends View> void setViewButtonState(T imageView,
			int selected, int normal) {
		Context ctx = imageView.getContext();
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] { android.R.attr.state_selected }, ctx
				.getResources().getDrawable(selected));
		states.addState(new int[] { android.R.attr.state_focused }, ctx
				.getResources().getDrawable(selected));
		states.addState(new int[] {}, ctx.getResources().getDrawable(normal));
		imageView.setBackground(states);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T get(View view, int resId) {

		SparseArray viewHolder = (SparseArray) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray();
			view.setTag(viewHolder);
		}
		View childView = (View) viewHolder.get(resId);

		if (childView == null) {
			childView = view.findViewById(resId);
			viewHolder.put(resId, childView);
		}
		return (T) childView;
	}

	@SuppressLint("NewApi")
	public static boolean isValidEmail(String emailString) {

		if (Build.VERSION.SDK_INT < 8) {
			if (emailString.toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")
					&& emailString.length() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			CharSequence inputStr = emailString;
			Pattern pattern = Patterns.EMAIL_ADDRESS;
			Matcher matcher = pattern.matcher(inputStr);
			return matcher.matches();
		}

	}

	public static String getFullDayName() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.get(Calendar.DAY_OF_WEEK);
		return String.format("%tA", c);
	}

	public static String getShortDayName(int day) {
		Calendar c = Calendar.getInstance();
		c.set(2011, 7, 1, 0, 0, 0);
		c.add(Calendar.DAY_OF_MONTH, day);
		return String.format("%ta", c);
	}

	public static void setEditTextHintFont(EditText mEditText,
			Context mContext, int index) {
		Typeface typeFace = null;
		String str = "";
		switch (index) {
		case 0:
			str = "fonts/Trebuchet_MS.ttf";
			break;
		case 1:
			str = "fonts/Trebuchet_MS.ttf";
			break;
		case 2:
			str = "fonts/Trebuchet_MS.ttf";
			break;
		case 3:
			str = "fonts/Trebuchet_MS.ttf";
			break;

		default:
			break;
		}
		typeFace = Typeface.createFromAsset(mContext.getAssets(), str);
		mEditText.setTypeface(typeFace);
	}

	public static void setEditTextHintFont(EditText mEditText, Context mContext) {
		Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/Trebuchet_MS.ttf");
		mEditText.setTypeface(typeFace);
	}

	public static void setRadioButtonHintFont(RadioButton mEditText,
			Context mContext) {
		Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/Trebuchet_MS.ttf");
		mEditText.setTypeface(typeFace);
	}

	public static void setCheckBoxFont(CheckBox mEditText, Context mContext) {
		Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/Trebuchet_MS.ttf");
		mEditText.setTypeface(typeFace);
	}

	public static void setTypeFace(TextView view, Context mContext) {
		Typeface t = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/Trebuchet_MS.ttf");
		view.setTypeface(t);
	}

	public static File bitmapToFile(Bitmap bitmap) {

		try {
			long current = System.currentTimeMillis();
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/image" + current + ".png");
			FileOutputStream fOut;
			fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getCameraPhotoOrientation(Context context, Uri imageUri,
			String imagePath) {
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_NORMAL:
				rotate = 0;
				break;
			}

			// Log.v("", "Exif orientation: " + orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	public static Bitmap setPic(String mFilePath, Context mContext) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mFilePath, bmOptions);

		/* Figure out which way needs to be reduced less */
		final int REQUIRED_SIZE = 140;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = bmOptions.outWidth, height_tmp = bmOptions.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scale;

		/* Decode the JPEG file into a Bitmap */
		try {
			Matrix matrix = new Matrix();
			matrix.setRotate(getCameraPhotoOrientation(mContext,
					Uri.fromFile(new File(mFilePath)), mFilePath));
			Bitmap bitmap = BitmapFactory.decodeFile(mFilePath, bmOptions);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, false);

			return bitmap;

			// mImageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void loadHtmlContent(WebView view, String appendText,
			float textSize) {
		view.setBackgroundColor(Color.TRANSPARENT);

		String color;
		color = "#000000";

		appendText = appendText.replace("\n", "<br>");// #363636
		String s = "<html><head><style type='text/css'>@font-face {font-family: MyFont;src: url('file:///android_asset/fonts/HelveticaNeueLTStd_35_Thin.otf')}body {margin:0px;color:"
				+ color
				+ ";font-family: MyFont;font-size: "
				+ textSize
				+ "px;text-align: justify;}</style></head><body>"
				+ appendText
				+ "</body></html>";
		view.loadDataWithBaseURL("", s, "text/html", "utf-8", null);
		view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		view.setFocusable(false);
		view.setFocusableInTouchMode(false);
	}

	@SuppressWarnings("deprecation")
	public static String encodeGETUrl(Bundle parameters) {
		StringBuilder sb = new StringBuilder();

		if (parameters != null && parameters.size() > 0) {
			boolean first = true;
			for (String key : parameters.keySet()) {
				if (key != null) {

					if (first) {
						first = false;
					} else {
						sb.append("&");
					}
					String value = "";
					Object object = parameters.get(key);
					if (object != null) {
						value = String.valueOf(object);
					}

					try {
						sb.append(URLEncoder.encode(key, "UTF-8") + "="
								+ URLEncoder.encode(value, "UTF-8"));
					} catch (Exception e) {
						sb.append(URLEncoder.encode(key) + "="
								+ URLEncoder.encode(value));
					}
				}
			}
		}
		return sb.toString();
	}

	public static String encodeUrl(String url, Bundle mParams) {
		Log.e("WSUrl", url + encodeGETUrl(mParams));
		return url + encodeGETUrl(mParams);
	}

	public static String formatDate(String messageTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
		@SuppressWarnings("unused")
		String dayVal = new String();
		try {
			Date messageDateandTime = formatter.parse(messageTime);
			String messageDate = df.format(messageDateandTime);
			return messageDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatDateWithoutYear(String messageTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df = new SimpleDateFormat("MMM dd HH:mm:ss");
		@SuppressWarnings("unused")
		String dayVal = new String();
		try {
			Date messageDateandTime = formatter.parse(messageTime);
			String messageDate = df.format(messageDateandTime);
			return messageDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Animation expandCollapse(final View v, final boolean expand) {
		return expandCollapse(v, expand, 500);
	}

	public static Animation expandCollapse(final View v, final boolean expand,
			final int duration) {
		int currentHeight = v.getLayoutParams().height;
		v.measure(
				MeasureSpec.makeMeasureSpec(
						((View) v.getParent()).getMeasuredWidth(),
						MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED));
		final int initialHeight = v.getMeasuredHeight();

		if ((expand && currentHeight == initialHeight)
				|| (!expand && currentHeight == 0))
			return null;

		if (expand)
			v.getLayoutParams().height = 0;
		else
			v.getLayoutParams().height = initialHeight;
		v.setVisibility(View.VISIBLE);

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				int newHeight = 0;
				if (expand)
					newHeight = (int) (initialHeight * interpolatedTime);
				else
					newHeight = (int) (initialHeight * (1 - interpolatedTime));
				v.getLayoutParams().height = newHeight;
				v.requestLayout();

				if (interpolatedTime == 1 && !expand)
					v.setVisibility(View.GONE);
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(duration);
		v.startAnimation(a);
		return a;
	}

	public static void showKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInputFromWindow(((Activity) activity)
				.getCurrentFocus().getApplicationWindowToken(),
				InputMethodManager.SHOW_FORCED, 0);
	}

	public static void hideKeyboard(OnEditorActionListener activity) {

		try {
			InputMethodManager inputManager = (InputMethodManager) ((Activity) activity)
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(((Activity) activity)
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			Log.e("KeyBoardUtil", e.toString(), e);
		}

	}

	public static Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
	}

	public static List<String> getListWithoutDuplicates(
			List<String> mListWithDuplicates) {
		List<String> mListWithoutDuplicates = new ArrayList<String>();
		for (int s = 0; s < mListWithDuplicates.size(); s++) {
			for (int m = s + 1; m < mListWithDuplicates.size(); m++) {
				if (mListWithDuplicates.get(s) != null
						&& mListWithDuplicates.get(s).equals(
								mListWithDuplicates.get(m))) {
					mListWithDuplicates.set(m, null);
				}
			}
		}
		for (int i = 0; i < mListWithDuplicates.size(); i++) {
			if (mListWithDuplicates.get(i) != null)
				mListWithoutDuplicates.add(mListWithDuplicates.get(i));
		}
		mListWithDuplicates.clear();
		return mListWithoutDuplicates;
	}

	public static float pixelsToSp(Context context, float px) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return px / scaledDensity;
	}

	public static boolean isEmptyText(String txt) {
		if (TextUtils.isEmpty(txt))
			return true;
		else
			return false;
	}

	public static void applyListAnimation(View mView, float initialTranslation) {
		// mView.setTranslationY(initialTranslation);
		// mView.animate().setInterpolator(new DecelerateInterpolator(1.0f))
		// .translationY(0f).setDuration(1000l).setListener(null);
	}

	@SuppressLint("NewApi")
	public static Bitmap BlurImage(Bitmap input, Context mContext) {
		try {
			RenderScript rsScript = RenderScript.create(mContext
					.getApplicationContext());
			Allocation alloc = Allocation.createFromBitmap(rsScript, input);

			ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript,
					Element.U8_4(rsScript));
			blur.setRadius(21);
			blur.setInput(alloc);

			Bitmap result = Bitmap.createBitmap(input.getWidth(),
					input.getHeight(), Bitmap.Config.ARGB_8888);
			Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);

			blur.forEach(outAlloc);
			outAlloc.copyTo(result);

			rsScript.destroy();
			return result;
		} catch (Exception e) {
			return input;
		}

	}

	@SuppressLint("DefaultLocale")
	public static void initShareIntent(Context mContext, String type,
			String mUrl) {
		boolean found = false;
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("image/jpeg");
		List<ResolveInfo> resInfo = mContext.getPackageManager()
				.queryIntentActivities(share, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				if (info.activityInfo.packageName.toLowerCase().contains(type)
						|| info.activityInfo.name.toLowerCase().contains(type)) {
					share.putExtra(Intent.EXTRA_SUBJECT, "subject");
					share.putExtra(Intent.EXTRA_TEXT, "your text");
					if (TextUtils.isEmpty(mUrl))
						share.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(new File(mUrl)));
					share.setPackage(info.activityInfo.packageName);
					found = true;
					break;
				}
			}
			if (!found)
				return;
			mContext.startActivity(Intent.createChooser(share, "Select"));
		}
	}

	public static boolean isNetworkAvailable(Context mContext) {
		boolean connection = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo net_info = cm.getActiveNetworkInfo();
				if (net_info != null && net_info.isConnected()) {
					connection = true;

					int type = net_info.getType();

					switch (type) {
					case ConnectivityManager.TYPE_MOBILE:

						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
		}

		return connection;
	}

	public static File getSabRentKaroCameraDir() {
		File cameraDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM
						+ File.separator + "SabRentKaro");
		cameraDir.mkdirs();
		return cameraDir;
	}

	public static String formatDate(Date date) {
		return formatter.format(date);
	}

}
