package com.sabrentkaro.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.CityModel;
import com.models.SearchModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.sabrentkaro.search.SearchResultsAdapter.IRentClick;
import com.utils.ApiUtils;
import com.utils.StorageClass;

public class SearchResultsActivity extends BaseActivity implements IRentClick,
		OnScrollListener {

	private ArrayList<SearchModel> mSearchResultsArray = new ArrayList<SearchModel>();
	private SearchResultsAdapter mAdapter;
	private String selectedCategory = "";
	private TextView mtxtProductTitle;
	private ListView mListView;
	int index = 1;
	private int preLast = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_search_results);

		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			selectedCategory = mBundle.getString("selectedCategory");
		}
		loadLayoutReferences();
		initSearchResultsApi(index);

	}

	private void loadLayoutReferences() {
		mtxtProductTitle = (TextView) findViewById(R.id.titleSearchProduct);
		mListView = (ListView) findViewById(R.id.listView);
		mListView.setOnScrollListener(this);
		mtxtProductTitle.setText(selectedCategory);
		mAdapter = new SearchResultsAdapter(this);

		((TextView) (findViewById(R.id.txtLocation)))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						showCityAlert();
					}
				});
	}

	private void initSearchResultsApi(int index) {
		showProgressLayout();
		JSONObject params = new JSONObject();
		JSONArray minputs = new JSONArray();
		JSONObject mObj = new JSONObject();
		try {
			mObj.put("SearchText", selectedCategory);
			mObj.put("SearchType", "category");
			mObj.put("SearchCondition", "OR");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		minputs.put(mObj);
		mObj = new JSONObject();
		try {
			String mlocation = StorageClass.getInstance(this).getCity();
			mObj.put("SearchText", mlocation);
			mObj.put("SearchType", "location");
			mObj.put("SearchCondition", "AND");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		minputs.put(mObj);

		JSONObject mPagingInputs = new JSONObject();
		try {
			mPagingInputs.put("PageNumber", index);
			mPagingInputs.put("PageSize", "50");
			mPagingInputs.put("UserId", null);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray mSortKeys = new JSONArray();
		JSONObject mSortKeysObj = new JSONObject();
		try {
			mSortKeysObj.put("SearchText", "");
			mSortKeysObj.put("SearchType", "");
			mSortKeysObj.put("SearchCode", "");
			mSortKeysObj.put("pcSearchType", null);
			mSortKeysObj.put("SearchCondition", null);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mSortKeys.put(mSortKeysObj);

		try {
			params.put("Inputs", minputs);
			params.put("PagingInput", mPagingInputs);
			params.put("SortKeys", mSortKeys);
			params.put("SearchText", null);
			params.put("SearchType", "Category");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.FETCHSEARCHRESULTS, params,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						responseForResultsApi(response);
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
					}
				});

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);

	}

	private void responseForResultsApi(JSONObject response) {
		if (response != null) {
			JSONArray resultsArray = (JSONArray) response.opt("Results");
			if (resultsArray != null) {
				mSearchResultsArray = new ArrayList<SearchModel>();
				for (int i = 0; i < resultsArray.length(); i++) {
					JSONObject resultObj = resultsArray.optJSONObject(i);
					if (resultObj != null) {
						SearchModel mModel = new SearchModel();
						mModel.setAdId(resultObj.optString("adId"));
						mModel.setAdTitle(resultObj.optString("adTitle"));
						mModel.setAdDescription(resultObj
								.optString("adDescription"));
						mModel.setCoverImagePath(resultObj
								.optString("coverImagePath"));
						mModel.setPostedBy(resultObj.optString("postedByName"));
						mModel.setProductCategory(resultObj
								.optString("productcategory"));
						mModel.setLocation(resultObj.optString("location"));
						mModel.setBrand(resultObj.optString("brand"));
						mModel.setPricePerDay(resultObj
								.optString("pricePerDay"));
						mModel.setPricePerWeek(resultObj
								.optString("priceperWeek"));
						mModel.setPricePerMonth(resultObj
								.optString("priceperMonth"));
						mModel.setYearOfPurchase(resultObj
								.optString("Yearofpurchase"));
						mModel.setProductCondition(resultObj
								.optString("productcondition"));

						if (mModel.getPricePerDay() == null
								|| mModel.getPricePerDay()
										.equalsIgnoreCase("0")
								|| mModel.getPricePerDay().length() == 0) {
							if (mModel.getPricePerWeek() == null
									|| mModel.getPricePerWeek()
											.equalsIgnoreCase("0")
									|| mModel.getPricePerWeek().length() == 0) {
								if (mModel.getPricePerMonth() == null
										|| mModel.getPricePerMonth()
												.equalsIgnoreCase("0")
										|| mModel.getPricePerMonth().length() == 0) {
								} else {
									int monthPrice = Integer.parseInt(mModel
											.getPricePerMonth());
									int dayPrice = monthPrice / 30;
									mModel.setPricePerDay(String
											.valueOf(dayPrice));
								}
							} else {
								int weekPrice = Integer.parseInt(mModel
										.getPricePerWeek());
								int dayPrice = weekPrice / 7;
								mModel.setPricePerDay(String.valueOf(dayPrice));
							}
						} else {
						}

						mModel.setCategory(resultObj.optString("category"));

						JSONArray mCatTonArray = resultObj
								.optJSONArray("itemJsonobject");
						if (mCatTonArray != null) {
							mModel.setItemsArray(mCatTonArray);
							for (int k = 0; k < mCatTonArray.length(); k++) {
								JSONObject mCatTonObj = mCatTonArray
										.optJSONObject(k);
								if (mCatTonObj.optString("Title")
										.equalsIgnoreCase("Storage Volume")) {
									mModel.setCapacity(mCatTonObj
											.optString("Value"));
								}
								if (mCatTonObj.optString("Title")
										.equalsIgnoreCase("Tonnage")) {
									mModel.setTonnage(mCatTonObj
											.optString("Value"));
								}
							}
						}
						mSearchResultsArray.add(mModel);
					}
				}

			} else {
				hideProgressLayout();
			}
		}
		setAdapter();
	}

	private void setAdapter() {
		mAdapter.setCallback(this);
		mAdapter.addItems(mSearchResultsArray);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRentButtonClicked(int pos) {
		SearchModel mModel = mAdapter.getItem(pos);
		Intent mIntent = new Intent(this, ProductDetailsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedProductAdId", mModel.getAdId());
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
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
				StorageClass.getInstance(this).getCity();
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select City");
				alert.setSingleChoiceItems(mCities, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								StorageClass.getInstance(
										SearchResultsActivity.this).setCity(
										mCities[which]);
								dialog.dismiss();
								setLocation();
								index = 1;
								initSearchResultsApi(index);
							}
						});
				alert.show();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		final int lastItem = firstVisibleItem + visibleItemCount;
		if (lastItem == totalItemCount) {
			if (preLast != lastItem) {
				preLast = lastItem;
				index = index + 1;
				initSearchResultsApi(index);
			}
		}

	}
}
