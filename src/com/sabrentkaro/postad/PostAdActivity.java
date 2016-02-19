package com.sabrentkaro.postad;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.models.CategoryModel;
import com.models.CityModel;
import com.models.PostAdModel;
import com.models.ProductModel;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.UploadPicture;

public class PostAdActivity extends BaseActivity implements
		OnRatingBarChangeListener {

	private TextView mbtnProductCategory, mbtnSubProductCategory,
			mbtnUploadPhotos, mbtnSelectControl, mbtnSelectType,
			mbtnSelectCapacity, mbtnNext, mbtnClear;
	private EditText mEditTitle, mEditShortDesc, mEditInstructions, mEditStuff,
			mEditPurchasedCost, mEditDailyCost, mEditWeeklyCost,
			mEditMonthlyCost, mEditQuantity, mEditSecurityDeposit;

	private ArrayList<ProductModel> mProductsArray = new ArrayList<ProductModel>();
	private ArrayList<CategoryModel> mCateogoryMappingsArray = new ArrayList<CategoryModel>();
	private ArrayList<String> mCategoriesArray = new ArrayList<String>();
	private ArrayList<CityModel> mCityArray = new ArrayList<CityModel>();

	private LinearLayout mImgLayout;
	private ImageView mImgProduct;

	private PostAdModel mObjModel;

	final static int PICK_IMAGE = 1;
	final static int CAPTURE_IMAGE = 2;

	public static UploadPicture uploadPicture;
	private Bitmap mProfilePicBitmap;
	private RatingBar mRatingBar;
	private String mImageProfilePicPath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_ad_activity);
		getDatafromIntent();
		loadReferences();
		addClickListeners();
	}

	private void addClickListeners() {
		mbtnProductCategory.setOnClickListener(this);
		mbtnSubProductCategory.setOnClickListener(this);
		mbtnUploadPhotos.setOnClickListener(this);
		mbtnSelectControl.setOnClickListener(this);
		mbtnSelectType.setOnClickListener(this);
		mbtnSelectCapacity.setOnClickListener(this);
		mbtnNext.setOnClickListener(this);
		mbtnClear.setOnClickListener(this);

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

	}

	private void loadReferences() {

		mEditTitle = (EditText) findViewById(R.id.editTitleAd);
		mEditShortDesc = (EditText) findViewById(R.id.editShortDesc);
		mEditInstructions = (EditText) findViewById(R.id.editInstructions);
		mEditStuff = (EditText) findViewById(R.id.editStuff);
		mEditPurchasedCost = (EditText) findViewById(R.id.editPricePurchased);
		mEditDailyCost = (EditText) findViewById(R.id.editPricePerDayRent);
		mEditWeeklyCost = (EditText) findViewById(R.id.editPricePerWeekRent);
		mEditMonthlyCost = (EditText) findViewById(R.id.editPricePerMonthRent);
		mEditQuantity = (EditText) findViewById(R.id.editQuantity);
		mEditSecurityDeposit = (EditText) findViewById(R.id.editSecurityDeposit);

		mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
		mRatingBar.setOnRatingBarChangeListener(this);

		mbtnProductCategory = (TextView) findViewById(R.id.btnSelectProductCategory);
		mbtnSubProductCategory = (TextView) findViewById(R.id.btnSelectSubProductCategory);
		mbtnUploadPhotos = (TextView) findViewById(R.id.btnUploadPhoto);
		mbtnSelectControl = (TextView) findViewById(R.id.btnSelectControl);
		mbtnSelectType = (TextView) findViewById(R.id.btnSelectType);
		mbtnSelectCapacity = (TextView) findViewById(R.id.btnSelectCapacity);
		mbtnNext = (TextView) findViewById(R.id.btnNext);
		mImgLayout = (LinearLayout) findViewById(R.id.imgLayout);
		mImgProduct = (ImageView) findViewById(R.id.imgProduct);
		mbtnClear = (TextView) findViewById(R.id.btnClear);

		StaticUtils.setEditTextHintFont(mEditTitle, this);
		StaticUtils.setEditTextHintFont(mEditShortDesc, this);
		StaticUtils.setEditTextHintFont(mEditInstructions, this);
		StaticUtils.setEditTextHintFont(mEditStuff, this);
		StaticUtils.setEditTextHintFont(mEditPurchasedCost, this);
		StaticUtils.setEditTextHintFont(mEditDailyCost, this);
		StaticUtils.setEditTextHintFont(mEditWeeklyCost, this);
		StaticUtils.setEditTextHintFont(mEditMonthlyCost, this);
		StaticUtils.setEditTextHintFont(mEditQuantity, this);
		StaticUtils.setEditTextHintFont(mEditSecurityDeposit, this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnSelectProductCategory:
			btnSelectProductCategoryClicked();
			break;
		case R.id.btnSelectSubProductCategory:
			btnSelectSubProductCategoryClicked();
			break;
		case R.id.btnNext:
			btnNextClicked();
			break;
		case R.id.btnUploadPhoto:
			btnUploadPhotoClicked();
			break;
		case R.id.btnSelectControl:
			btnSelectControlClicked();
			break;
		case R.id.btnSelectType:
			btnSelectTypeClicked();
			break;
		case R.id.btnSelectCapacity:
			btnSelectCapacityClicked();
			break;
		case R.id.btnClear:
			btnClearClicked();
			break;
		default:
			break;
		}
	}

	private void btnClearClicked() {
		mEditTitle.setText("");
		mEditShortDesc.setText("");
		mEditInstructions.setText("");
		mEditStuff.setText("");
		mEditPurchasedCost.setText("");
		mEditDailyCost.setText("");
		mEditWeeklyCost.setText("");
		mEditMonthlyCost.setText("");
		mEditQuantity.setText("");
		mEditSecurityDeposit.setText("");

		mbtnProductCategory.setText("Select Prodcut Category");
		mbtnSubProductCategory.setText("Select Sub Product Category");
		mbtnSelectControl.setText("Select Control");
		mbtnSelectType.setText("Select Type");
		mbtnSelectCapacity.setText("Select Capacity");

	}

	private void btnSelectCapacityClicked() {
		if (mObjModel.getCapacityValues() != null
				&& mObjModel.getCapacityValues().size() > 0) {
			int pos = -1;
			final String[] mCapcacityValues = new String[mObjModel
					.getCapacityValues().size()];
			for (int i = 0; i < mObjModel.getCapacityValues().size(); i++) {
				String mValue = mObjModel.getCapacityValues().get(i);
				if (mbtnSelectCapacity.getText().toString()
						.equalsIgnoreCase("Select Capacity")) {
					pos = -1;
				} else {
					if (mValue.equalsIgnoreCase(mbtnSelectCapacity.getText()
							.toString())) {
						pos = i;
					}
				}
				mCapcacityValues[i] = mValue;
			}
			if (mCapcacityValues != null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select Capacity");
				alert.setSingleChoiceItems(mCapcacityValues, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mbtnSelectCapacity
										.setText(mCapcacityValues[which]);
								dialog.dismiss();
							}

						});
				alert.show();
			}
		}
	}

	private void btnSelectTypeClicked() {
		if (mObjModel.getTypeValues() != null
				&& mObjModel.getTypeValues().size() > 0) {
			int pos = -1;
			final String[] mTypeValues = new String[mObjModel.getTypeValues()
					.size()];
			for (int i = 0; i < mObjModel.getTypeValues().size(); i++) {
				String mValue = mObjModel.getTypeValues().get(i);
				if (mbtnSelectType.getText().toString()
						.equalsIgnoreCase("Select Type")) {
					pos = -1;
				} else {
					if (mValue.equalsIgnoreCase(mbtnSelectType.getText()
							.toString())) {
						pos = i;
					}
				}
				mTypeValues[i] = mValue;
			}
			if (mTypeValues != null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select Type");
				alert.setSingleChoiceItems(mTypeValues, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mbtnSelectType.setText(mTypeValues[which]);
								dialog.dismiss();
							}

						});
				alert.show();
			}
		}
	}

	private void btnSelectControlClicked() {
		if (mObjModel.getControlValues() != null
				&& mObjModel.getControlValues().size() > 0) {
			int pos = -1;
			final String[] mControlValues = new String[mObjModel
					.getControlValues().size()];
			for (int i = 0; i < mObjModel.getControlValues().size(); i++) {
				String mValue = mObjModel.getControlValues().get(i);
				if (mbtnSelectControl.getText().toString()
						.equalsIgnoreCase("Select Control")) {
					pos = -1;
				} else {
					if (mValue.equalsIgnoreCase(mbtnSelectControl.getText()
							.toString())) {
						pos = i;
					}
				}
				mControlValues[i] = mValue;
			}
			if (mControlValues != null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select Control");
				alert.setSingleChoiceItems(mControlValues, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mbtnSelectControl
										.setText(mControlValues[which]);
								dialog.dismiss();
							}

						});
				alert.show();
			}
		}
	}

	private void btnUploadPhotoClicked() {
		final String[] mArray = { "Camera", "Gallery", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setItems(mArray, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (mArray[which].toString().equalsIgnoreCase("Camera")) {
					StaticUtils.isProfilePic = true;
					initiateCameraActivity();
				} else if (mArray[which].toString().equalsIgnoreCase("Gallery")) {
					StaticUtils.isProfilePic = true;
					initiateGalleryActivity();
				} else {

				}
			}
		});

		builder.setCancelable(false);
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void btnNextClicked() {
		if (mbtnProductCategory.getText().toString()
				.equalsIgnoreCase("Select Category")) {
			showToast("Please Select Category");
		} else {
			if (mbtnSubProductCategory.getText().toString()
					.equalsIgnoreCase("Select Product Sub Category")) {
				showToast("Please Select Product Sub Category");
			} else {
				if (mEditTitle.getText().toString().length() == 0) {
					showToast("Please Enter Title");
				} else {
					if (mEditShortDesc.getText().toString().length() == 0) {
						showToast("Please Enter Short Description");
					} else {
						if (mEditInstructions.getText().toString().length() == 0) {
							showToast("Please Enter Instructions");
						} else {
							if (mEditStuff.getText().toString().length() == 0) {
								showToast("Please Enter Product Stuff");
							} else {
								if (mbtnSelectControl.getText().toString()
										.equalsIgnoreCase("Select Control")) {
									showToast("Please Select Control");
								} else {
									if (mbtnSelectType.getText().toString()
											.equalsIgnoreCase("Select Type")) {
										showToast("Please Select Type");
									} else {
										if (mbtnSelectCapacity
												.getText()
												.toString()
												.equalsIgnoreCase(
														"Select Capcacity")) {
											showToast("Please Select Capacity");
										} else {
											if (mEditPurchasedCost.getText()
													.toString().length() == 0) {
												showToast("Please Enter Purchased Cost");
											} else {
												if (mEditDailyCost.getText()
														.toString().length() == 0) {
													showToast("Please Enter Daily Cost");
												} else {
													if (mEditWeeklyCost
															.getText()
															.toString()
															.length() == 0) {
														showToast("Please Enter Weekly Cost");
													} else {
														if (mEditMonthlyCost
																.getText()
																.toString()
																.length() == 0) {
															showToast("Please Enter Monhtly Cost");
														} else {
															if (mEditQuantity
																	.getText()
																	.toString()
																	.length() == 0) {
																showToast("Please Enter Quantity");
															} else {
																if (mEditSecurityDeposit
																		.getText()
																		.toString()
																		.length() == 0) {
																	showToast("Please Enter Security Deposit");
																} else {
																	navigateToPostDocuments();
																}
															}
														}
													}
												}
											}
										}
									}
								}

							}
						}
					}
				}
			}
		}

	}

	private void navigateToPostDocuments() {
		Intent mIntent = new Intent(this, PostAdDocumentActivity.class);
		startActivity(mIntent);
	}

	private void btnSelectSubProductCategoryClicked() {
		if (mbtnProductCategory.getText().toString()
				.equalsIgnoreCase("Select Category")) {
			showToast("Please Select Category");
		} else {
			int pos = -1;
			if (mCateogoryMappingsArray != null) {
				final String[] mSubCategories = new String[mCateogoryMappingsArray
						.size()];
				for (int i = 0; i < mCateogoryMappingsArray.size(); i++) {
					CategoryModel mModel = mCateogoryMappingsArray.get(i);
					if (mbtnSubProductCategory.getText().toString()
							.equalsIgnoreCase("Select Product Sub Category")) {
						pos = -1;
					} else {
						if (mModel.getTitle().equalsIgnoreCase(
								mbtnSubProductCategory.getText().toString())) {
							pos = i;
						}
					}
					mSubCategories[i] = mModel.getTitle().toString();
				}
				if (mSubCategories != null) {
					AlertDialog.Builder alert = new AlertDialog.Builder(this);
					alert.setTitle("Select Sub Category");
					alert.setSingleChoiceItems(mSubCategories, pos,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mbtnSubProductCategory
											.setText(mSubCategories[which]);
									dialog.dismiss();
									initTemplateForCategoryApi();
								}

							});
					alert.show();
				}
			}
		}
	}

	private void initTemplateForCategoryApi() {
		String mCode = "";
		for (int i = 0; i < mCateogoryMappingsArray.size(); i++) {
			if (mCateogoryMappingsArray
					.get(i)
					.getTitle()
					.equalsIgnoreCase(
							mbtnSubProductCategory.getText().toString())) {
				mCode = mCateogoryMappingsArray.get(i).getCode();
				break;
			}
		}
		if (!mCode.isEmpty()) {
			JSONObject params = new JSONObject();
			try {
				params.put("Subscription", "");
				params.put("CategoryName", mCode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			showProgressLayout();
			JsonObjectRequest mObjReq = new JsonObjectRequest(
					ApiUtils.GETADTEMPLATEFORCATEGORY, params,
					new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							hideProgressLayout();
							responseForTemplateCategory(response);
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							hideProgressLayout();
						}
					});

			RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
			mQueue.add(mObjReq);
		}
	}

	private void responseForTemplateCategory(JSONObject response) {
		if (response != null) {
			mObjModel = new PostAdModel();
			JSONArray mFieldsArray = response.optJSONArray("Fields");
			if (mFieldsArray != null) {
				for (int i = 0; i < mFieldsArray.length(); i++) {
					JSONObject mFieldsObj = mFieldsArray.optJSONObject(i);
					if (mFieldsObj != null) {
						JSONArray mValuesArray = mFieldsObj
								.optJSONArray("Values");
						String mFieldName = mFieldsObj.optString("FieldName");
						if (mFieldName.equalsIgnoreCase("Brand")
								|| mFieldName.equalsIgnoreCase("Model")) {

						} else {
							ArrayList<String> mValues = new ArrayList<String>();
							if (mFieldName.equalsIgnoreCase("Text")) {

							} else {
								for (int j = 0; j < mValuesArray.length(); j++) {
									JSONObject mValueObj = mValuesArray
											.optJSONObject(j);
									if (mValueObj != null
											&& !mValueObj.optString("Value")
													.trim()
													.equalsIgnoreCase("")) {
										mValues.add(mValueObj
												.optString("Value"));
									}
								}

								if (mObjModel.getTypeValues() == null
										|| mObjModel.getTypeValues().isEmpty()) {
									mObjModel.setTypeValues(mValues);
									continue;
								}
								if (mObjModel.getControlValues() == null
										|| mObjModel.getControlValues()
												.isEmpty()) {
									mObjModel.setControlValues(mValues);
									continue;
								}

								if (mObjModel.getCapacityValues() == null
										|| mObjModel.getCapacityValues()
												.isEmpty()) {
									mObjModel.setCapacityValues(mValues);
									continue;
								}

							}
						}

					}
				}
			}
		}
	}

	private void btnSelectProductCategoryClicked() {
		int pos = -1;
		if (mProductsArray != null) {
			final String[] mCategories = new String[mProductsArray.size()];
			for (int i = 0; i < mProductsArray.size(); i++) {
				if (mbtnProductCategory.getText().toString()
						.equalsIgnoreCase("Select Category")) {
					pos = -1;
				} else {
					if (mProductsArray
							.get(i)
							.getTitle()
							.equalsIgnoreCase(
									mbtnProductCategory.getText().toString())) {
						pos = i;
					}
				}
				mCategories[i] = mProductsArray.get(i).getTitle();
			}
			if (mCategories != null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select Category");
				alert.setSingleChoiceItems(mCategories, pos,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mbtnProductCategory.setText(mCategories[which]);
								dialog.dismiss();
							}
						});
				alert.show();
			}
		}
	}

	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	protected void initiateCameraActivity() {
		if (isDeviceSupportCamera()) {
			uploadPicture = new UploadPicture(this);
			uploadPicture.dispatchTakePictureIntent(CAPTURE_IMAGE, this);
		} else {
			showToast("Camera feature not available");
		}
	}

	protected void initiateGalleryActivity() {
		Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
		galleryIntent.setType("image/*");
		startActivityForResult(galleryIntent, PICK_IMAGE);
	}

	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PICK_IMAGE:
				if (resultCode == Activity.RESULT_OK) {
					Uri uri = data.getData();
					if (uri != null) {
						try {
							mImgProduct.setImageURI(uri);
							mImgLayout.setVisibility(View.VISIBLE);
						} catch (Exception e) {
							e.getStackTrace();
						}
					} else {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							ClipData clipData = data.getClipData();
							if (clipData != null) {
								for (int i = 0; i < clipData.getItemCount(); i++) {
									try {
										mImgProduct.setImageURI(clipData
												.getItemAt(i).getUri());
										mImgLayout.setVisibility(View.VISIBLE);
									} catch (Exception e) {
										e.getStackTrace();
									}
								}
							}
						}
					}
				}
				break;
			case CAPTURE_IMAGE:
				if (resultCode == Activity.RESULT_OK) {
					try {
						mImageProfilePicPath = uploadPicture.mCurrentPhotoPath;
						mProfilePicBitmap = uploadPicture.setPic();
						mImgProduct.setImageBitmap(mProfilePicBitmap);
						mImgLayout.setVisibility(View.VISIBLE);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {

	}
}
