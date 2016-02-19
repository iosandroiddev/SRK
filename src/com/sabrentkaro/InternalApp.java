package com.sabrentkaro;

import java.util.ArrayList;

import android.app.Application;
import android.content.res.Configuration;

import com.android.volley.RequestQueue;
import com.android.volley.examples.toolbox.MyVolley;
import com.android.volley.toolbox.Volley;
import com.models.CategoryModel;
import com.models.ProductModel;

public class InternalApp extends Application {

	RequestQueue queue;
	private ArrayList<ProductModel> mProductsArray;
	private ArrayList<String> mCategoriesArray;
	private ArrayList<CategoryModel> mCateogoryMappingsArray;

	@Override
	public void onCreate() {
		super.onCreate();
		MyVolley.init(this);
		queue = Volley.newRequestQueue(this);
	}

	public RequestQueue getQueue() {
		return queue;
	}

	public boolean isTabletLayout() {
		int screenLayout = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenLayout != Configuration.SCREENLAYOUT_SIZE_SMALL
				&& screenLayout != Configuration.SCREENLAYOUT_SIZE_NORMAL;
	}

	public void setProductsArray(ArrayList<ProductModel> mProductsArray) {
		this.mProductsArray = mProductsArray;

	}

	public ArrayList<ProductModel> getProductsArray() {
		return this.mProductsArray;

	}

	public void setCateogoriesArray(ArrayList<String> mCatArray) {
		this.mCategoriesArray = mCatArray;

	}

	public ArrayList<String> getCateogoriesArray() {
		return this.mCategoriesArray;

	}

	public void setCategoryMappingArray(
			ArrayList<CategoryModel> mCateogoryMappingsArray) {
		this.mCateogoryMappingsArray = mCateogoryMappingsArray;

	}

	public ArrayList<CategoryModel> getCategoryMappingArray() {
		return this.mCateogoryMappingsArray;

	}

}
