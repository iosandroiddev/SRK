package com.sabrentkaro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.adapters.HomeAdapter;
import com.android.jsonclasses.IArrayParseListener;
import com.android.jsonclasses.JSONArrayRequestResponse;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.CategoryModel;
import com.models.CityModel;
import com.models.ProductModel;
import com.sabrentkaro.postad.PostAdActivity;
import com.sabrentkaro.search.SearchActivity;
import com.sabrentkaro.search.SearchResultsActivity;
import com.utils.ApiUtils;
import com.utils.MiscUtils;
import com.utils.StaticData;
import com.utils.StorageClass;

public class HomeActivity extends BaseActivity implements OnItemClickListener, IArrayParseListener {

	private GridView mGridView;
	private ArrayList<ProductModel> mProductsArray = new ArrayList<ProductModel>();
	private ArrayList<CategoryModel> mCateogoryMappingsArray = new ArrayList<CategoryModel>();
	private ArrayList<String> mCategoriesArray = new ArrayList<String>();
	private ArrayList<CityModel> mCityArray = new ArrayList<CityModel>();

	private HomeAdapter mAdapter;
	private TextView mbtnSearchProducts, mbtnPostAd;
	private String deviceUdId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_main);
		loadAlert();
		getDatafromIntent();
		loadReferences();
		addClickListeners();
		setAdapter();
		if (StorageClass.getInstance(this).getAccessToken().length() == 0) {
			initDeviceEntryApi();
		} else {
			initSubCategoriesApi();
		}

	}

	private void initServicerProvider() {
		final String mAuthHeader = StorageClass.getInstance(this).getAuthHeader();
		showProgressLayout();
		JSONObject mTpType = new JSONObject();
		try {
			mTpType.put("Code", "VERIFICATION");
			mTpType.put("Title", "null");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mTpProvider = new JSONObject();
		try {
			mTpProvider.put("Code", "JOCATA");
			mTpProvider.put("Title", "null");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject mParams = new JSONObject();
		try {
			mParams.put("UserId", "null");
			mParams.put("TpType", mTpType);
			mParams.put("TpProvider", mTpProvider);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonArrayRequest mRequest = new JsonArrayRequest(ApiUtils.GETPROVIDERSERVICES, mParams,
				new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						hideProgressLayout();
						responseForProviderApi(response);
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
						showToast(error.toString());
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
		mQueue.add(mRequest);
	}

	private void responseForProviderApi(JSONArray response) {
		if (response != null) {
			for (int i = 0; i < response.length(); i++) {
				JSONObject mObj = response.optJSONObject(i);
				if (mObj != null) {
					JSONArray mSpecificationsArray = mObj.optJSONArray("TpServiceInputSpecifications");
					if (mSpecificationsArray != null) {
						for (int j = 0; j < mSpecificationsArray.length(); j++) {
							JSONObject mObjSpecifications = mSpecificationsArray.optJSONObject(i);
							if (mObjSpecifications != null) {
								if (mObjSpecifications.optString("UserValues") != null) {
									StorageClass.getInstance(this)
											.setServiceTitle(mObjSpecifications.optString("Title"));
									StorageClass.getInstance(this)
											.setServiceValue(mObjSpecifications.optString("UserValues"));
								}
							}
						}
					}
				}
			}
		}
	}

	private void initSubCategoriesApi() {
		showProgressLayout();
		JSONArrayRequestResponse mJsonRequestResponse = new JSONArrayRequestResponse(this);
		mJsonRequestResponse.setPostMethod(true);
		Bundle params = new Bundle();
		mJsonRequestResponse.getResponse(MiscUtils.encodeUrl(ApiUtils.GETCATEGORYMAPPINGS, params),
				StaticData.GETCATEGORYMAPPINGS_RESPONSE_CODE, this);
	}

	private void initDeviceEntryApi() {
		deviceUdId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		showProgressLayout();
		JSONObject params = new JSONObject();
		try {
			params.put("DeviceId", deviceUdId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest mObjReq = new JsonObjectRequest(ApiUtils.POSTDEVICEENTRY, params, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				responseForDeviceToken(response);
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				hideProgressLayout();
				showToast(error.toString());
			}

		}) {

		};

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);

	}

	private void responseForDeviceToken(JSONObject response) {
		if (response != null) {
			String token = response.optString("AccessToken");
			StorageClass.getInstance(this).setAccessToken(token);
		}
		initSubCategoriesApi();
	}

	private void loadAlert() {
		if (TextUtils.isEmpty(StorageClass.getInstance(this).getCity())) {
			showCityAlert();
		} else {

		}
	}

	private void showCityAlert() {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this).getCityList();
		if (mCityArray != null) {
			final String[] mCities = new String[mCityArray.size()];
			for (int i = 0; i < mCityArray.size(); i++) {
				mCities[i] = mCityArray.get(i).getName();
			}
			if (mCities != null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select City");
				alert.setCancelable(false);
				alert.setSingleChoiceItems(mCities, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StorageClass.getInstance(HomeActivity.this).setCity(mCities[which]);
						dialog.dismiss();
						setLocation();
						storeCityValue();
					}
				});
				alert.show();
			}
		}

	}

	private void addClickListeners() {
		mbtnSearchProducts.setOnClickListener(this);
		mbtnPostAd.setOnClickListener(this);
	}

	private void setAdapter() {
		mAdapter.addItems(mProductsArray);
		mGridView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	private void getDatafromIntent() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			mProductsArray = (ArrayList<ProductModel>) mBundle.getSerializable("productsArray");
			mCategoriesArray = (ArrayList<String>) mBundle.getSerializable("categories");
			mCateogoryMappingsArray = (ArrayList<CategoryModel>) mBundle.getSerializable("categoriesMapping");
		}

		if (mProductsArray != null && mProductsArray.size() == 0) {
			InternalApp mApp = (InternalApp) getApplication();
			mProductsArray = mApp.getProductsArray();
		}

		setImages();
		mAdapter = new HomeAdapter(this);
	}

	private void setImages() {
		for (int i = 0; i < mProductsArray.size(); i++) {
			ProductModel mObj = mProductsArray.get(i);
			switch (i) {
			case 0:
				mObj.setImage(R.drawable.frigde);
				break;
			case 1:
				mObj.setImage(R.drawable.sofas);
				break;
			case 2:
				mObj.setImage(R.drawable.digicamera);
				break;
			case 3:
				mObj.setImage(R.drawable.oven);
				break;
			case 4:
				mObj.setImage(R.drawable.mobile_tablet);
				break;
			case 5:
				mObj.setImage(R.drawable.laptop);
				break;
			case 6:
				mObj.setImage(R.drawable.tv);
				break;
			case 7:
				mObj.setImage(R.drawable.automobile);
				break;
			case 8:
				mObj.setImage(R.drawable.musical);
				break;
			case 9:
				mObj.setImage(R.drawable.sports);
				break;
			case 10:
				mObj.setImage(R.drawable.toysgames);
				break;
			case 11:
				mObj.setImage(R.drawable.games);
				break;
			case 12:
				mObj.setImage(R.drawable.books);
				break;
			case 13:
				mObj.setImage(R.drawable.watch);
				break;
			case 14:
				mObj.setImage(R.drawable.kidsfurniture);
				break;
			case 15:
				mObj.setImage(R.drawable.camcorder);
				break;
			case 16:
				mObj.setImage(R.drawable.house);
				break;
			default:
				mObj.setImage(R.drawable.suits);
				break;
			}
			mProductsArray.set(i, mObj);
		}
	}

	private void loadReferences() {
		mGridView = (GridView) findViewById(R.id.gridView);
		mGridView.setOnItemClickListener(this);
		mbtnSearchProducts = (TextView) findViewById(R.id.btnSearch);
		mbtnPostAd = (TextView) findViewById(R.id.btnPost);
		mbtnPostAd.setSelected(true);
		mbtnSearchProducts.setSelected(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent mIntent = new Intent(this, SearchResultsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedCategory", mAdapter.getItem(position).getTitle());
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnSearch:
			btnSearchProductsClicked();
			break;
		case R.id.btnPost:
			btnPostAdClicked();
			break;
		default:
			break;
		}
	}

	private void btnSearchProductsClicked() {
		Intent mIntent = new Intent(this, SearchActivity.class);
		startActivity(mIntent);
	}

	private void btnPostAdClicked() {
		PostAdSaver.getInstance(this).setEditing(false);
		Intent mIntent = new Intent(this, PostAdActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("productsArray", mProductsArray);
		mBundle.putSerializable("categories", mCategoriesArray);
		mBundle.putSerializable("categoriesMapping", mCateogoryMappingsArray);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	@Override
	public void ErrorResponse(VolleyError error, int requestCode) {
		switch (requestCode) {
		case StaticData.GETCATEGORYMAPPINGS_RESPONSE_CODE:
			showToast("Something went Wrong at Category Mappings  Api");
			finish();
			break;
		}
	}

	@Override
	public void SuccessResponse(JSONArray response, int requestCode) {

		switch (requestCode) {
		case StaticData.GETCATEGORYMAPPINGS_RESPONSE_CODE:
			responseForAllSubCategoriesApi(response);
			break;
		}
	}

	private void responseForAllSubCategoriesApi(JSONArray response) {
		hideProgressLayout();
		if (response != null && response.length() > 0) {
			for (int i = 0; i < response.length(); i++) {
				try {
					JSONObject mCatObj = response.getJSONObject(i);
					CategoryModel mModel = new CategoryModel();
					mModel.setCode(mCatObj.optString("Code"));
					mModel.setCategory(mCatObj.optString("Category"));
					mModel.setTitle(mCatObj.optString("Title"));
					mCateogoryMappingsArray.add(mModel);

					if (!(mCategoriesArray.contains(mCatObj.optString("Category")))) {
						mCategoriesArray.add(mCatObj.optString("Category"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		InternalApp mApp = (InternalApp) getApplication();
		if (mApp != null) {
			mApp.setCategoryMappingArray(mCateogoryMappingsArray);
		}
		if (StorageClass.getInstance(this).getAuthHeader().length() != 0)
			initServicerProvider();
	}

}
