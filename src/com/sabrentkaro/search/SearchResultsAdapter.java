package com.sabrentkaro.search;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
		mArraySearchResult = new ArrayList<SearchModel>();
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
		TextView mtxtTitle, mtxtPostedBy, mbtnRent, mtxtLocation, mtxtPrice;
		LinearLayout mRootFiedlsLayout;
		ImageView mItemImg;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.search_result_item, null);
			mHolder.mtxtTitle = (TextView) convertView
					.findViewById(R.id.txtTitle);
			mHolder.mtxtPostedBy = (TextView) convertView
					.findViewById(R.id.txtPostedBy);
			mHolder.mtxtLocation = (TextView) convertView
					.findViewById(R.id.txtLocation);
			mHolder.mtxtPrice = (TextView) convertView
					.findViewById(R.id.txtPricePerDay);
			mHolder.mItemImg = (ImageView) convertView
					.findViewById(R.id.itemProduct);
			mHolder.mbtnRent = (TextView) convertView
					.findViewById(R.id.btnRent);
			mHolder.mRootFiedlsLayout = (LinearLayout) convertView
					.findViewById(R.id.rootFields);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}

		SearchModel mModel = getItem(position);
		mHolder.mtxtTitle.setText(mModel.getAdTitle());
		mHolder.mtxtPostedBy.setText(mModel.getPostedBy());
		mHolder.mtxtLocation.setText(mModel.getLocation());
		mHolder.mtxtPrice.setText(mModel.getPricePerDay());
		mHolder.mtxtLocation.setSelected(true);

		Picasso.with(mContext).load(mModel.getCoverImagePath())
				.placeholder(R.drawable.default_loading)
				.error(R.drawable.default_loading).into(mHolder.mItemImg);

		mHolder.mbtnRent.setOnClickListener(new onRentClick(position));

		if (mModel.getItemsArray() == null
				|| mModel.getItemsArray().length() == 0) {
			mHolder.mRootFiedlsLayout.setVisibility(View.GONE);
		} else {
			mHolder.mRootFiedlsLayout.setVisibility(View.VISIBLE);
			mHolder.mRootFiedlsLayout.removeAllViews();
			for (int i = 0; i < mModel.getItemsArray().length(); i++) {
				final TextView mtxtView = (TextView) LayoutInflater.from(
						mContext).inflate(R.layout.itemtext, null);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						new LayoutParams(LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
				params.setMargins(2, 2, 2, 2);
				JSONObject mCatTonObj = mModel.getItemsArray().optJSONObject(i);
				mtxtView.setLayoutParams(params);
				mtxtView.setText(mCatTonObj.optString("title") + ": "
						+ mCatTonObj.optString("value"));
				mtxtView.setSelected(true);
				mtxtView.setEllipsize(TruncateAt.MARQUEE);
				mtxtView.setSingleLine(true);
				mHolder.mRootFiedlsLayout.addView(mtxtView);
			}

		}

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