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
				mObj.setImage(R.drawable.suits);
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
