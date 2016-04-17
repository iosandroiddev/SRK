package com.sabrentkaro;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;

import com.android.volley.RequestQueue;
import com.android.volley.examples.toolbox.MyVolley;
import com.android.volley.toolbox.Volley;
import com.models.CategoryModel;
import com.models.PostAdModel;
import com.models.ProductModel;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;

public class InternalApp extends Application {

	RequestQueue queue;
	private ArrayList<ProductModel> mProductsArray;
	private ArrayList<String> mCategoriesArray;
	private ArrayList<CategoryModel> mCateogoryMappingsArray;
	private Bitmap mBitmap;
	private JSONArray jsonArray;
	private ArrayList<Uri> mArrayBitmaps;

	private ArrayList<File> mFileImagesArray;
	private ArrayList<PostAdModel> mArrayFields;

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

	public void setImage(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	public Bitmap getImage() {
		return this.mBitmap;
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

	public void setPhotoUpload(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public JSONArray getPhotoUpload() {
		return this.jsonArray;
	}

	public void setUriArray(ArrayList<Uri> mArrayUri) {
		this.mArrayBitmaps = mArrayUri;

	}

	public ArrayList<Uri> getUriArray() {
		return this.mArrayBitmaps;
	}

	public void setImageFilesArray(ArrayList<File> mArrayUri) {
		this.mFileImagesArray = mArrayUri;

	}

	public ArrayList<File> getImageFilesArray() {
		return this.mFileImagesArray;
	}

	public void setArray(ArrayList<PostAdModel> mArrayFields) {
		this.mArrayFields = mArrayFields;
	}

	public ArrayList<PostAdModel> getArray() {
		return this.mArrayFields;
	}
}
