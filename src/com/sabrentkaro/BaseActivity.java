package com.sabrentkaro;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.models.CityModel;
import com.sabrentkaro.login.LoginActivity;
import com.sabrentkaro.postad.PostAdActivity;
import com.sabrentkaro.search.SearchActivity;
import com.utils.StaticUtils;
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
	private TextView mbtnLogin, mtxtTitle;
	private TextView mbtnPostAd, mbtnSearchProducts, mbtnHome, mtxtUserName,
			mbtnTermsConditions, mbtnLegalDisc, mbtnPrivacyPolicy,
			mbtnListingPolicy, mbtnRentingPolicy, mbtnAboutUs, mbtnHelp;
	private LinearLayout mLoginLayout, mHelpLayout;
	private boolean isHelpClicked = false;
	private ProgressBar mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSlidingMenu();
		getLayoutReferences();
		setClickListeners();
		setLocation();
		hideOrShowLogin();
	}

	private void hideOrShowLogin() {
		if (TextUtils.isEmpty(StorageClass.getInstance(this).getUserName())) {
			mbtnLogin.setVisibility(View.VISIBLE);
			mLoginLayout.setVisibility(View.GONE);
		} else {
			mbtnLogin.setVisibility(View.GONE);
			mLoginLayout.setVisibility(View.VISIBLE);
			mtxtUserName.setText(StorageClass.getInstance(this).getUserName());
			mtxtUserName.setSelected(true);
		}
	}

	private void setClickListeners() {
		mbtnMenu.setOnClickListener(this);
		mbtnLogin.setOnClickListener(this);
		mtxtLocation.setOnClickListener(this);
		mbtnPostAd.setOnClickListener(this);
		mbtnSearchProducts.setOnClickListener(this);
		mbtnHome.setOnClickListener(this);
		mbtnTermsConditions.setOnClickListener(this);
		mbtnLegalDisc.setOnClickListener(this);
		mbtnRentingPolicy.setOnClickListener(this);
		mbtnListingPolicy.setOnClickListener(this);
		mbtnPrivacyPolicy.setOnClickListener(this);
		mbtnAboutUs.setOnClickListener(this);
		mbtnHelp.setOnClickListener(this);

	}

	private void getLayoutReferences() {
		mMiddleLayout = (FrameLayout) findViewById(R.id.mainContent);
		mbtnMenu = (ImageView) findViewById(R.id.btnMenu);
		mProgressLayout = (RelativeLayout) findViewById(R.id.layoutWait);
		mtxtLocation = (TextView) findViewById(R.id.txtLocation);
		mbtnLogin = (TextView) findViewById(R.id.btnLogin);
		mbtnPostAd = (TextView) findViewById(R.id.btnPostAd);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

		mHelpLayout = (LinearLayout) findViewById(R.id.helpLayout);
		mtxtTitle = (TextView) findViewById(R.id.txtTitle);
		mtxtTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				navigateToHome();
			}
		});

		mbtnSearchProducts = (TextView) findViewById(R.id.btnSearchProducts);
		mbtnHome = (TextView) findViewById(R.id.btnHome);
		mLoginLayout = (LinearLayout) findViewById(R.id.layouLogin);
		mtxtUserName = (TextView) findViewById(R.id.txtLoginUserName);

		mbtnTermsConditions = (TextView) findViewById(R.id.btnTermsOfUse);
		mbtnHelp = (TextView) findViewById(R.id.btnHelp);
		mbtnLegalDisc = (TextView) findViewById(R.id.btnLegalDisclaimer);
		mbtnListingPolicy = (TextView) findViewById(R.id.btnListingPolicy);
		mbtnPrivacyPolicy = (TextView) findViewById(R.id.btnPrivacyPolicy);
		mbtnRentingPolicy = (TextView) findViewById(R.id.btnRentingPolicy);
		mbtnAboutUs = (TextView) findViewById(R.id.btnAboutUs);

		mbtnTermsConditions.setSelected(true);
		mbtnLegalDisc.setSelected(true);
		mbtnListingPolicy.setSelected(true);
		mbtnPrivacyPolicy.setSelected(true);
		mbtnRentingPolicy.setSelected(true);
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
		case R.id.btnTermsOfUse:
			btnTermsConditionsClicked();
			break;
		case R.id.btnHelp:
			btnHelpClicked();
			break;
		case R.id.btnListingPolicy:
			btnListingPolicyClicked();
			break;
		case R.id.btnLegalDisclaimer:
			btnLegalDiscClicked();
			break;
		case R.id.btnPrivacyPolicy:
			btnPrivacyPolicyClicked();
			break;
		case R.id.btnRentingPolicy:
			btnRentingPolicyClicked();
			break;
		case R.id.btnAboutUs:
			btnAboutUsClicked();
			break;
		default:
			break;
		}
	}

	private void btnAboutUsClicked() {
		btnMenuClicked();
		Intent mIntent = new Intent(this, WebActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("viewToLoad", "aboutUs");
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	private void btnRentingPolicyClicked() {
		btnMenuClicked();
		btnHelpClicked();
		Intent mIntent = new Intent(this, WebActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("viewToLoad", "rentingPolicy");
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	private void btnPrivacyPolicyClicked() {
		btnMenuClicked();
		btnHelpClicked();
		Intent mIntent = new Intent(this, WebActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("viewToLoad", "privacyPolicy");
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	private void btnLegalDiscClicked() {
		btnMenuClicked();
		btnHelpClicked();
		Intent mIntent = new Intent(this, WebActivity.class);
		Bundle mBundle = new Bundle();
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mBundle.putString("viewToLoad", "legalDisc");
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	private void btnListingPolicyClicked() {
		btnMenuClicked();
		btnHelpClicked();
		Intent mIntent = new Intent(this, WebActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("viewToLoad", "listingPolicy");
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);

	}

	private void btnHelpClicked() {
		if (isHelpClicked) {
			isHelpClicked = false;
		} else {
			isHelpClicked = true;
		}
		StaticUtils.expandCollapse(mHelpLayout, isHelpClicked);
	}

	private void btnTermsConditionsClicked() {
		btnMenuClicked();
		btnHelpClicked();
		Intent mIntent = new Intent(this, WebActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("viewToLoad", "terms");
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	private void btnPostAdClicked() {
		btnMenuClicked();
		PostAdSaver.getInstance(this).setEditing(false);
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
		navigateToHome();
	}

	private void navigateToHome() {
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
		mProgressBar.getIndeterminateDrawable().setColorFilter(
				getResources().getColor(R.color.pink), PorterDuff.Mode.SRC_IN);
		mProgressLayout.setVisibility(View.VISIBLE);
	}

	public void hideProgressLayout() {
		mProgressLayout.setVisibility(View.GONE);
	}

	public void setLocation() {
		if (TextUtils.isEmpty(StorageClass.getInstance(this).getCity())) {
			mtxtLocation.setText("");
			mtxtLocation.setVisibility(View.GONE);
		} else {
			mtxtLocation.setText(StorageClass.getInstance(this).getCity());
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
		if (getCurrentFocus() != null && getWindow() != null
				&& getWindow().getDecorView() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getWindow()
					.getDecorView().getWindowToken(), 0);
		}
	}

	private void showCityAlert() {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this)
				.getCityList();
		int pos = -1;
		if (mCityArray != null) {
			final String[] mCities = new String[mCityArray.size()];
			for (int i = 0; i < mCityArray.size(); i++) {
				if (TextUtils.isEmpty(StorageClass.getInstance(this).getCity())) {
					pos = -1;
				} else {
					if (mCityArray
							.get(i)
							.getName()
							.equalsIgnoreCase(
									StorageClass.getInstance(this).getCity())) {
						pos = i;
					}
				}
				mCities[i] = mCityArray.get(i).getName();
			}
			if (mCities != null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select City");
				alert.setSingleChoiceItems(mCities, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								StorageClass.getInstance(BaseActivity.this)
										.setCity(mCities[which]);
								storeCityValue();
								dialog.dismiss();
								setLocation();
							}

						});
				alert.show();
			}
		}
	}

	public void storeCityValue() {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this)
				.getCityList();
		for (int i = 0; i < mCityArray.size(); i++) {
			if (mCityArray.get(i).getName()
					.equalsIgnoreCase(StorageClass.getInstance(this).getCity())) {
				StorageClass.getInstance(this).setCityValue(
						mCityArray.get(i).getValue());
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setLocation();
	}

}
