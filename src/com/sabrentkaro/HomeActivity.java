package com.sabrentkaro;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.adapters.HomeAdapter;
import com.models.CategoryModel;
import com.models.CityModel;
import com.models.ProductModel;
import com.sabrentkaro.postad.PostAdActivity;
import com.sabrentkaro.search.SearchActivity;
import com.sabrentkaro.search.SearchResultsActivity;
import com.utils.StorageClass;

public class HomeActivity extends BaseActivity implements OnItemClickListener {

	private GridView mGridView;
	private ArrayList<ProductModel> mProductsArray = new ArrayList<ProductModel>();
	private ArrayList<CategoryModel> mCateogoryMappingsArray = new ArrayList<CategoryModel>();
	private ArrayList<String> mCategoriesArray = new ArrayList<String>();
	private ArrayList<CityModel> mCityArray = new ArrayList<CityModel>();

	private HomeAdapter mAdapter;
	private TextView mbtnSearchProducts, mbtnPostAd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_main);
		loadAlert();
		getDatafromIntent();
		loadReferences();
		addClickListeners();
		setAdapter();
	}

	private void loadAlert() {
		if (TextUtils.isEmpty(StorageClass.getInstance(this).getUserCity())) {
			showCityAlert();
		} else {

		}
	}

	private void showCityAlert() {
		ArrayList<CityModel> mCityArray = StorageClass.getInstance(this)
				.getCityList();
		if (mCityArray != null) {
			final String[] mCities = new String[mCityArray.size()];
			for (int i = 0; i < mCityArray.size(); i++) {
				mCities[i] = mCityArray.get(i).getName();
			}
			if (mCities != null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select City");
				alert.setCancelable(false);
				alert.setSingleChoiceItems(mCities, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								StorageClass.getInstance(HomeActivity.this)
										.setUserCity(mCities[which]);
								dialog.dismiss();
								setLocation();
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
			mProductsArray = (ArrayList<ProductModel>) mBundle
					.getSerializable("productsArray");
			mCategoriesArray = (ArrayList<String>) mBundle
					.getSerializable("categories");
			mCateogoryMappingsArray = (ArrayList<CategoryModel>) mBundle
					.getSerializable("categoriesMapping");
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
		mbtnSearchProducts = (TextView) findViewById(R.id.btnSearch);
		mbtnPostAd = (TextView) findViewById(R.id.btnPost);
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
		Intent mIntent = new Intent(this, PostAdActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("productsArray", mProductsArray);
		mBundle.putSerializable("categories", mCategoriesArray);
		mBundle.putSerializable("categoriesMapping", mCateogoryMappingsArray);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

}
