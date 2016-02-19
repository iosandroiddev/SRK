package com.sabrentkaro;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.models.CityModel;
import com.sabrentkaro.login.LoginActivity;
import com.sabrentkaro.postad.PostAdActivity;
import com.sabrentkaro.search.SearchActivity;
import com.utils.StorageClass;
import com.utils.slidingmenu.SlidingMenu;
import com.utils.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity implements
		OnClickListener {

	private SlidingMenu mSlidingMenu;
	public FrameLayout mMiddleLayout;
	public ImageView mbtnMenu;
	public RelativeLayout mProgressLayout;
	public TextView mtxtLocation;
	private TextView mbtnLogin;
	private TextView mbtnPostAd, mbtnSearchProducts, mbtnHome;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSlidingMenu();
		getLayoutReferences();
		setClickListeners();
		setLocation();
	}

	private void setClickListeners() {
		mbtnMenu.setOnClickListener(this);
		mbtnLogin.setOnClickListener(this);
		mtxtLocation.setOnClickListener(this);
		mbtnPostAd.setOnClickListener(this);
		mbtnSearchProducts.setOnClickListener(this);
		mbtnHome.setOnClickListener(this);
	}

	private void getLayoutReferences() {
		mMiddleLayout = (FrameLayout) findViewById(R.id.mainContent);
		mbtnMenu = (ImageView) findViewById(R.id.btnMenu);
		mProgressLayout = (RelativeLayout) findViewById(R.id.layoutWait);
		mtxtLocation = (TextView) findViewById(R.id.txtLocation);
		mbtnLogin = (TextView) findViewById(R.id.btnLogin);
		mbtnPostAd = (TextView) findViewById(R.id.btnPostAd);
		mbtnSearchProducts = (TextView) findViewById(R.id.btnSearchProducts);
		mbtnHome = (TextView) findViewById(R.id.btnHome);
	}

	private void setSlidingMenu() {
		setBehindContentView(R.layout.sliding_left_menu);
		setSlidingActionBarEnabled(true);
		setContentView(R.layout.base_layout);
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setShadowWidthRes(R.dimen.slider_shadow_width);
		mSlidingMenu.setShadowDrawable(R.drawable.slider_shadow);
		mSlidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	public void addContentLayout(int id) {
		LayoutInflater mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(id, null);
		mMiddleLayout.addView(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMenu:
			btnMenuClicked();
			break;
		case R.id.btnLogin:
			btnLoginClicked();
			break;
		case R.id.txtLocation:
			btnLocationClicked();
			break;
		case R.id.btnPostAd:
			btnPostAdClicked();
			break;
		case R.id.btnSearchProducts:
			btnSearchProductsClicked();
			break;
		case R.id.btnHome:
			btnHomeClicked();
			break;
		default:
			break;
		}
	}

	private void btnPostAdClicked() {
		btnMenuClicked();
		Intent mIntent = new Intent(this, PostAdActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
	}

	private void btnSearchProductsClicked() {
		btnMenuClicked();
		Intent mIntent = new Intent(this, SearchActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
	}

	private void btnHomeClicked() {
		btnMenuClicked();
		Intent mIntent = new Intent(this, HomeActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
	}

	private void btnLocationClicked() {
		showCityAlert();
	}

	private void btnLoginClicked() {
		btnMenuClicked();
		Intent mIntent = new Intent(this, LoginActivity.class);
		startActivity(mIntent);
	}

	public void btnMenuClicked() {
		mSlidingMenu.toggle();
	}

	public void showProgressLayout() {
		mProgressLayout.setVisibility(View.VISIBLE);
	}

	public void hideProgressLayout() {
		mProgressLayout.setVisibility(View.GONE);
	}

	public void setLocation() {
		if (TextUtils.isEmpty(StorageClass.getInstance(this).getUserCity())) {
			mtxtLocation.setText("");
			mtxtLocation.setVisibility(View.GONE);
		} else {
			mtxtLocation.setText(StorageClass.getInstance(this).getUserCity());
			mtxtLocation.setVisibility(View.VISIBLE);
		}

	}

	public void showToast(String mString) {
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Trebuchet_MS.ttf");
		SpannableString efr = new SpannableString(mString);
		efr.setSpan(new TypefaceSpan(font), 0, efr.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		Toast.makeText(this, efr, Toast.LENGTH_SHORT).show();
	}

	public class TypefaceSpan extends MetricAffectingSpan {
		private Typeface mTypeface;

		public TypefaceSpan(Typeface typeface) {
			mTypeface = typeface;
		}

		@Override
		public void updateMeasureState(TextPaint p) {
			p.setTypeface(mTypeface);
			p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}

		@Override
		public void updateDrawState(TextPaint tp) {
			tp.setTypeface(mTypeface);
			tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
	}

	public void hideSoftKeyboard() {
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	private void showCityAlert() {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this)
				.getCityList();
		int pos = -1;
		if (mCityArray != null) {
			final String[] mCities = new String[mCityArray.size()];
			for (int i = 0; i < mCityArray.size(); i++) {
				if (TextUtils.isEmpty(StorageClass.getInstance(this)
						.getUserCity())) {
					pos = -1;
				} else {
					if (mCityArray
							.get(i)
							.getName()
							.equalsIgnoreCase(
									StorageClass.getInstance(this)
											.getUserCity())) {
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
								StorageClass.getInstance(BaseActivity.this)
										.setUserCity(mCities[which]);
								dialog.dismiss();
								setLocation();
							}
						});
				alert.show();
			}
		}

	}

}
