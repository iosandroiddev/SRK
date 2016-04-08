package com.sabrentkaro.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.jsonclasses.IObjectParseListener;
import com.android.jsonclasses.JSONObjectRequestResponse;
import com.android.volley.VolleyError;
import com.models.ProductModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.MiscUtils;
import com.utils.StaticData;

public class SearchActivity extends BaseActivity implements
		OnItemClickListener, IObjectParseListener {

	private GridView mGridView;
	private ArrayList<ProductModel> mProductsArray = new ArrayList<ProductModel>();
	private SearchAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_search);
		loadReferences();
		initProductsApi();

	}

	private void setAdapter() {
		mAdapter.addItems(mProductsArray);
		mGridView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void setImages() {
		for (int i = 0; i < mProductsArray.size(); i++) {
			ProductModel mObj = mProductsArray.get(i);
			if (mObj.getTitle().contains("Accessories")) {
				mObj.setImage(R.drawable.watch);
			} else if (mObj.getTitle().contains("Appliances")) {
				mObj.setImage(R.drawable.frigde);
			} else if (mObj.getTitle().contains("Automobiles")) {
				mObj.setImage(R.drawable.automobile);
			}  else if (mObj.getTitle().contains("Books & Media")) {
				mObj.setImage(R.drawable.books);
			} else if (mObj.getTitle().contains("Baby products")) {
				mObj.setImage(R.drawable.toysgames);
			} else if (mObj.getTitle().contains("Camera Equipment")) {
				mObj.setImage(R.drawable.digicamera);
			} else if (mObj.getTitle().contains("Festivals")) {
				mObj.setImage(R.drawable.camcorder);
			} else if (mObj.getTitle().contains("Furniture")) {
				mObj.setImage(R.drawable.sofas);
			} else if (mObj.getTitle().contains("Gaming")) {
				mObj.setImage(R.drawable.games);
			} else if (mObj.getTitle().contains("Kitchenware")) {
				mObj.setImage(R.drawable.oven);
			} else if (mObj.getTitle().contains("Laptops & Computers")) {
				mObj.setImage(R.drawable.laptop);
			} else if (mObj.getTitle().contains("Mobiles & Tablets")) {
				mObj.setImage(R.drawable.mobile_tablet);
			} else if (mObj.getTitle().contains("Musical Instruments")) {
				mObj.setImage(R.drawable.musical);
			} else if (mObj.getTitle().contains("Packages")) {
				mObj.setImage(R.drawable.suits);
			} else if (mObj.getTitle().contains("Sports & Fitness")) {
				mObj.setImage(R.drawable.sports);
			} else if (mObj.getTitle().contains("Toys & Games")) {
				mObj.setImage(R.drawable.games);
			} else if (mObj.getTitle().contains("TV & Audio Video")) {
				mObj.setImage(R.drawable.tv);
			} else if (mObj.getTitle().contains("Vacation Homes")) {
				mObj.setImage(R.drawable.house);
			} else {
				mObj.setImage(R.drawable.suits);
			}
			mProductsArray.set(i, mObj);
		}
	}

	private void loadReferences() {
		mGridView = (GridView) findViewById(R.id.gridView);
		mGridView.setOnItemClickListener(this);
		mAdapter = new SearchAdapter(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent = new Intent(this, SearchResultsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedCategory", mAdapter.getItem(position)
				.getTitle());
		mIntent.putExtras(mBundle);
		startActivity(mIntent);

	}

	private void initProductsApi() {
		showProgressLayout();
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
		hideProgressLayout();

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
		hideProgressLayout();
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
					setImages();
					setAdapter();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
