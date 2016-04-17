package com.sabrentkaro.search;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.models.CategoryModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;

public class SubSearchActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView mListView;
	private ArrayList<String> mSubProductsArray = new ArrayList<String>();
	private ArrayList<CategoryModel> mCateogoryMappingsArray = new ArrayList<CategoryModel>();
	private SubSearchAdapter mAdapter;
	private String selectedCategory = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_sub_search);
		getDetails();
		loadReferences();
		setAdapter();

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			selectedCategory = mBundle.getString("selectedCategory");
		}
		if (mCateogoryMappingsArray != null
				&& mCateogoryMappingsArray.size() == 0) {
			InternalApp mApp = (InternalApp) getApplication();
			mCateogoryMappingsArray = mApp.getCategoryMappingArray();
		}

		if (mCateogoryMappingsArray != null) {
			for (int i = 0; i < mCateogoryMappingsArray.size(); i++) {
				CategoryModel mModel = mCateogoryMappingsArray.get(i);
				if (mModel.getCategory().equalsIgnoreCase(selectedCategory)) {
					mSubProductsArray.add(mModel.getTitle().toString());
				}
			}
		}
	}

	private void setAdapter() {
		mAdapter.addItems(mSubProductsArray);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void loadReferences() {
		mListView = (ListView) findViewById(R.id.listView);
		mListView.setOnItemClickListener(this);
		mAdapter = new SubSearchAdapter(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent mIntent = new Intent(this, SearchResultsActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("selectedCategory", mAdapter.getItem(position));
		mIntent.putExtras(mBundle);
		startActivity(mIntent);

	}

}
