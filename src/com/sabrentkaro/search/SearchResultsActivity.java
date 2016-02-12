package com.sabrentkaro.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.SearchModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.sabrentkaro.search.SearchResultsAdapter.IRentClick;
import com.utils.ApiUtils;

public class SearchResultsActivity extends BaseActivity implements IRentClick {

	private ArrayList<SearchModel> mSearchResultsArray = new ArrayList<SearchModel>();
	private SearchResultsAdapter mAdapter;
	private String selectedCategory = "";
	private TextView mtxtProductTitle;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_search_results);

		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			selectedCategory = mBundle.getString("selectedCategory");
		}
		loadLayoutReferences();
		initSearchResultsApi();

	}

	private void loadLayoutReferences() {
		mtxtProductTitle = (TextView) findViewById(R.id.titleSearchProduct);
		mListView = (ListView) findViewById(R.id.listView);
		mtxtProductTitle.setText(selectedCategory);
		mAdapter = new SearchResultsAdapter(this);
	}

	private void initSearchResultsApi() {
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
			mObj.put("SearchText", "HYDERABAD Anywhere");
			mObj.put("SearchType", "location");
			mObj.put("SearchCondition", "AND");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		minputs.put(mObj);

		JSONObject mPagingInputs = new JSONObject();
		try {
			mPagingInputs.put("PageNumber", "1");
			mPagingInputs.put("PageSize", "25");
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
						mModel.setCategory(resultObj.optString("category"));
						mSearchResultsArray.add(mModel);
					}
				}
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
}
