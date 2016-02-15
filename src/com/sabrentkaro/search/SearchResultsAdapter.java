package com.sabrentkaro.search;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.models.SearchModel;
import com.sabrentkaro.R;
import com.squareup.picasso.Picasso;

public class SearchResultsAdapter extends BaseAdapter {
	private ArrayList<SearchModel> mArraySearchResult = new ArrayList<SearchModel>();
	private AQuery mQuery;
	private Context mContext;

	public SearchResultsAdapter(Context context) {
		this.mContext = context;
		mQuery = new AQuery(mContext);
	}

	public interface IRentClick {
		public void onRentButtonClicked(int pos);
	}

	IRentClick mRentClick;

	public void setCallback(IRentClick mRent) {
		this.mRentClick = mRent;
	}

	@Override
	public int getCount() {
		return mArraySearchResult.size();
	}

	public void addItems(ArrayList<SearchModel> mArrayList) {
		for (int i = 0; i < mArrayList.size(); i++) {
			this.mArraySearchResult.add(mArrayList.get(i));
		}
		notifyDataSetChanged();
	}

	public void clearList() {
		if (mArraySearchResult != null) {
			mArraySearchResult.clear();
		} else {
			mArraySearchResult = new ArrayList<SearchModel>();
		}
	}

	@Override
	public SearchModel getItem(int position) {
		return mArraySearchResult.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class Holder {
		TextView mtxtBrand, mtxtCategory, mtxtLocation, mtxtType, mtxtCapacity,
				mtxtPrice, mbtnRent, mtxtTonnage;
		LinearLayout mLayoutCapacity, mLayoutTonnage;
		ImageView mItemImg;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.search_result_item, null);
			mHolder.mtxtBrand = (TextView) convertView
					.findViewById(R.id.txtBrand);
			mHolder.mtxtCategory = (TextView) convertView
					.findViewById(R.id.txtCategory);
			mHolder.mtxtLocation = (TextView) convertView
					.findViewById(R.id.txtLocation);
			mHolder.mtxtType = (TextView) convertView
					.findViewById(R.id.txtType);
			mHolder.mtxtCapacity = (TextView) convertView
					.findViewById(R.id.txtCapacity);
			mHolder.mtxtTonnage = (TextView) convertView
					.findViewById(R.id.txtTonnage);
			mHolder.mtxtPrice = (TextView) convertView
					.findViewById(R.id.txtPrice);
			mHolder.mItemImg = (ImageView) convertView
					.findViewById(R.id.itemProduct);
			mHolder.mLayoutTonnage = (LinearLayout) convertView
					.findViewById(R.id.layoutTonange);
			mHolder.mLayoutCapacity = (LinearLayout) convertView
					.findViewById(R.id.layoutCapacity);
			mHolder.mbtnRent = (TextView) convertView
					.findViewById(R.id.btnRent);

			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}

		SearchModel mModel = getItem(position);
		mHolder.mtxtBrand.setText(mModel.getBrand());
		mHolder.mtxtPrice.setText(mModel.getPricePerDay());

		mHolder.mtxtType.setText(mModel.getProductCategory());
		mHolder.mtxtCategory.setText(mModel.getCategory());
		mHolder.mtxtLocation.setText(mModel.getLocation());
		mHolder.mtxtLocation.setSelected(true);
		mHolder.mtxtType.setSelected(true);

		if (mModel.getCapacity() == null || mModel.getCapacity().length() == 0) {
			mHolder.mLayoutCapacity.setVisibility(View.GONE);
			mHolder.mtxtCapacity.setText(mModel.getCapacity());
		} else {
			mHolder.mLayoutCapacity.setVisibility(View.VISIBLE);
			mHolder.mtxtCapacity.setText(mModel.getCapacity());
		}

		if (mModel.getTonnage() == null || mModel.getTonnage().length() == 0) {
			mHolder.mLayoutTonnage.setVisibility(View.GONE);
			mHolder.mtxtTonnage.setText(mModel.getTonnage());
		} else {
			mHolder.mLayoutTonnage.setVisibility(View.VISIBLE);
			mHolder.mtxtTonnage.setText(mModel.getTonnage());
		}

		Picasso.with(mContext).load(mModel.getCoverImagePath())
				.placeholder(R.drawable.default_loading)
				.error(R.drawable.default_loading).into(mHolder.mItemImg);

		mHolder.mbtnRent.setOnClickListener(new onRentClick(position));

		return convertView;
	}

	class onRentClick implements OnClickListener {

		private int pos;

		onRentClick(int pos) {
			this.pos = pos;

		}

		@Override
		public void onClick(View v) {
			if (mRentClick != null)
				mRentClick.onRentButtonClicked(pos);
		}
	}
}