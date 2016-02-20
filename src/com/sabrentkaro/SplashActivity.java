package com.sabrentkaro;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.widget.Toast;

import com.android.jsonclasses.IArrayParseListener;
import com.android.jsonclasses.IObjectParseListener;
import com.android.jsonclasses.JSONArrayRequestResponse;
import com.android.jsonclasses.JSONObjectRequestResponse;
import com.android.volley.VolleyError;
import com.models.CategoryModel;
import com.models.CityModel;
import com.models.ProductModel;
import com.utils.ApiUtils;
import com.utils.MiscUtils;
import com.utils.StaticData;
import com.utils.StorageClass;

public class SplashActivity extends FragmentActivity implements
		IObjectParseListener, IArrayParseListener {

	private ArrayList<ProductModel> mProductsArray = new ArrayList<ProductModel>();
	private ArrayList<CategoryModel> mCateogoryMappingsArray = new ArrayList<CategoryModel>();
	private ArrayList<String> mCategoriesArray = new ArrayList<String>();
	private ArrayList<CityModel> mCityArray = new ArrayList<CityModel>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash);
		initProductsApi();

	}

	private void initiateHandler() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				navigateToMainActivity();
			}

		}, StaticData.SPLASH_ACTIVITY_DURATION);
	}

	private void navigateToMainActivity() {
		Intent mIntent = new Intent(SplashActivity.this, HomeActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("productsArray", mProductsArray);
		mBundle.putSerializable("categories", mCategoriesArray);
		mBundle.putSerializable("categoriesMapping", mCateogoryMappingsArray);
		mIntent.putExtras(mBundle);
		InternalApp mApp = (InternalApp) getApplication();
		if (mApp != null) {
			mApp.setProductsArray(mProductsArray);
			mApp.setCateogoriesArray(mCategoriesArray);
			mApp.setCategoryMappingArray(mCateogoryMappingsArray);
		}
		startActivity(mIntent);
		finish();
	}

	private void initCityListApi() {
		JSONArrayRequestResponse mJsonRequestResponse = new JSONArrayRequestResponse(
				this);
		mJsonRequestResponse.setPostMethod(false);
		Bundle params = new Bundle();
		mJsonRequestResponse.getResponse(
				MiscUtils.encodeUrl(ApiUtils.GETCITY, params),
				StaticData.GETCITY_RESPONSE_CODE, this);
	}

	private void initSubCategoriesApi() {
		JSONArrayRequestResponse mJsonRequestResponse = new JSONArrayRequestResponse(
				this);
		mJsonRequestResponse.setPostMethod(true);
		Bundle params = new Bundle();
		mJsonRequestResponse.getResponse(
				MiscUtils.encodeUrl(ApiUtils.GETCATEGORYMAPPINGS, params),
				StaticData.GETCATEGORYMAPPINGS_RESPONSE_CODE, this);
	}

	private void initProductsApi() {
		JSONObjectRequestResponse mJsonRequestResponse = new JSONObjectRequestResponse(
				this);
		mJsonRequestResponse.setPostMethod(true);
		Bundle params = new Bundle();
		mJsonRequestResponse.getResponse(
				MiscUtils.encodeUrl(ApiUtils.GETALLPRODUCTS, params),
				StaticData.GETALLPRODUCTS_RESPONSE_CODE, this);
	}

	@Override
	public void ErrorResponse(VolleyError error, int requestCode) {
		switch (requestCode) {
		case StaticData.GETALLPRODUCTS_RESPONSE_CODE:
			showToast("Something went Wrong at Initiating Products Api");
			finish();
			break;
		case StaticData.GETCATEGORYMAPPINGS_RESPONSE_CODE:
			showToast("Something went Wrong at Category Mappings  Api");
			finish();
			break;
		case StaticData.GETCITY_RESPONSE_CODE:
			showToast("Something went Wrong at Initiating Cities Api");
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void SuccessResponse(JSONObject response, int requestCode) {
		switch (requestCode) {
		case StaticData.GETALLPRODUCTS_RESPONSE_CODE:
			responseForAllProductsApi(response);
			break;
		default:
			break;
		}

	}

	private void responseForAllProductsApi(JSONObject response) {
		if (response != null && response.length() > 0) {
			JSONArray mGroupDataArray;
			try {
				mGroupDataArray = response.optJSONArray("GroupData");
				if (mGroupDataArray != null) {
					for (int i = 0; i < mGroupDataArray.length(); i++) {
						JSONObject mGroupDataObj = mGroupDataArray
								.getJSONObject(i);
						if (mGroupDataObj != null) {
							ProductModel mModel = new ProductModel();
							mModel.setTitle(mGroupDataObj.optString("Title"));
							mModel.setCode(mGroupDataObj.optString("Code"));
							mProductsArray.add(mModel);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		initSubCategoriesApi();
	}

	@Override
	public void SuccessResponse(JSONArray response, int requestCode) {
		switch (requestCode) {
		case StaticData.GETCATEGORYMAPPINGS_RESPONSE_CODE:
			responseForAllSubCategoriesApi(response);
			break;
		case StaticData.GETCITY_RESPONSE_CODE:
			responseForCityApi(response);
			break;
		default:
			break;
		}
	}

	private void responseForCityApi(JSONArray response) {
		if (response != null && response.length() > 0) {
			for (int i = 0; i < response.length(); i++) {
				try {
					JSONObject mCityObj = response.getJSONObject(i);
					CityModel mModel = new CityModel();
					mModel.setName(mCityObj.optString("Name"));
					mModel.setValue(mCityObj.optString("Value"));
					mCityArray.add(mModel);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		StorageClass.getInstance(this).setCityList(mCityArray);
		initiateHandler();
	}

	private void responseForAllSubCategoriesApi(JSONArray response) {
		if (response != null && response.length() > 0) {
			for (int i = 0; i < response.length(); i++) {
				try {
					JSONObject mCatObj = response.getJSONObject(i);
					CategoryModel mModel = new CategoryModel();
					mModel.setCode(mCatObj.optString("Code"));
					mModel.setCategory(mCatObj.optString("Category"));
					mModel.setTitle(mCatObj.optString("Title"));
					mCateogoryMappingsArray.add(mModel);
					
					if (!(mCategoriesArray.contains(mCatObj
							.optString("Category")))) {
						mCategoriesArray.add(mCatObj.optString("Category"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		initCityListApi();
	}

	private void showToast(String mString) {
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/Trebuchet_MS.ttf");
		SpannableString efr = new SpannableString(mString);
		efr.setSpan(new TypefaceSpan(font), 0, efr.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		Toast.makeText(this, efr, Toast.LENGTH_SHORT).show();
	}

	private class TypefaceSpan extends MetricAffectingSpan {
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

}
