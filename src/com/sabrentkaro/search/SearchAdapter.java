package com.sabrentkaro.search;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.models.ProductModel;
import com.sabrentkaro.R;

public class SearchAdapter extends BaseAdapter {
	private ArrayList<ProductModel> mArrayProducts = new ArrayList<ProductModel>();
	private AQuery mQuery;
	private Context mContext;

	


	public SearchAdapter(Context context) {
		this.mContext = context;
		mQuery = new AQuery(mContext);
	}

	
	@Override
	public int getCount() {
		return mArrayProducts.size();
	}

	public void addItems(ArrayList<ProductModel> mArrayList) {
		for (int i = 0; i < mArrayList.size(); i++) {
			this.mArrayProducts.add(mArrayList.get(i));
		}
		notifyDataSetChanged();
	}

	public void clearList() {
		if (mArrayProducts != null) {
			mArrayProducts.clear();
		} else {
			mArrayProducts = new ArrayList<ProductModel>();
		}
	}

	@Override
	public ProductModel getItem(int position) {
		return mArrayProducts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class Holder {
		TextView mItemName;
		ImageView mItemImg;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.search_item, null);
			mHolder.mItemName = (TextView) convertView
					.findViewById(R.id.item_name);
			mHolder.mItemImg = (ImageView) convertView
					.findViewById(R.id.itemImg);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		final ProductModel mDataHolder = getItem(position);
		mHolder.mItemName.setText(mDataHolder.getTitle());
		mQuery.id(mHolder.mItemImg).image(mDataHolder.getImage());
		
		

		return convertView;
	}

	
}