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

public class SubSearchAdapter extends BaseAdapter {
	private ArrayList<String> mArrayProducts = new ArrayList<String>();
	private AQuery mQuery;
	private Context mContext;

	public SubSearchAdapter(Context context) {
		this.mContext = context;
		mQuery = new AQuery(mContext);
	}

	@Override
	public int getCount() {
		return mArrayProducts.size();
	}

	public void addItems(ArrayList<String> mArrayList) {
		for (int i = 0; i < mArrayList.size(); i++) {
			this.mArrayProducts.add(mArrayList.get(i));
		}
		notifyDataSetChanged();
	}

	public void clearList() {
		if (mArrayProducts != null) {
			mArrayProducts.clear();
		} else {
			mArrayProducts = new ArrayList<String>();
		}
	}

	@Override
	public String getItem(int position) {
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
					R.layout.sub_search_item, null);
			mHolder.mItemName = (TextView) convertView
					.findViewById(R.id.item_name);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.mItemName.setText(getItem(position));
		mHolder.mItemName.setSelected(true);

		return convertView;
	}

}