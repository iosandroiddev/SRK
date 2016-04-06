package com.sabrentkaro.postad;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.jsonclasses.IArrayParseListener;
import com.android.jsonclasses.IObjectParseListener;
import com.android.jsonclasses.JSONObjectRequestResponse;
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
import com.sabrentkaro.login.LoginActivity;
import com.utils.ApiUtils;
import com.utils.MiscUtils;
import com.utils.SquareImageView;
import com.utils.StaticData;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class PostAdActivity extends BaseActivity implements
		IObjectParseListener, IArrayParseListener {

	private TextView mbtnProductCategory, mbtnSubProductCategory,
			mbtnUploadPhotos, mbtnNext, mbtnClear, mtxtFields;
	private EditText mEditTitle, mEditShortDesc, mEditInstructions, mEditStuff,
			mEditPurchasedCost, mEditDailyCost, mEditWeeklyCost,
			mEditMonthlyCost, mEditQuantity, mEditSecurityDeposit;

	private ArrayList<ProductModel> mProductsArray = new ArrayList<ProductModel>();
	private ArrayList<CategoryModel> mCateogoryMappingsArray = new ArrayList<CategoryModel>();
	private ArrayList<String> mCategoriesArray = new ArrayList<String>();
	private ArrayList<CityModel> mCityArray = new ArrayList<CityModel>();

	private ArrayList<PostAdModel> mArrayFields = new ArrayList<PostAdModel>();
	private LinearLayout mSelectLayout, mlayoutFields, mlayoutRents;

	private HashMap<String, String> mControlLayouts = new HashMap<String, String>();
	final static int CAMERA_SELECT_CODE = 1;
	private Bitmap mProfilePicBitmap;
	private RatingBar mRatingBar;
	private String mImageProfilePicPath = "";
	private String[] mStringSelectValues;
	private TextView mbtnSelectRating;
	private String rentalPeriod = "";
	private String productAdId;
	private String mtxtRating;
	private String productCode;
	private LinearLayout mLayoutAttachments;
	private LinearLayout mLayoutSubCat;

	private ArrayList<String> mImageArrayPaths = new ArrayList<String>();
	private ArrayList<File> mImageFileArray = new ArrayList<File>();

	private ArrayList<Uri> mUriArray = new ArrayList<Uri>();

	private HorizontalScrollView mScrollimages;
	private File cameraFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_ad_activity);
		getDatafromIntent();
		loadReferences();
		addClickListeners();
		initProdcutAdId();

	}

	private void initProdcutAdId() {
		showProgressLayout();
		JSONObjectRequestResponse mJsonRequestResponse = new JSONObjectRequestResponse(
				this);
		mJsonRequestResponse.setPostMethod(true);
		Bundle params = new Bundle();
		mJsonRequestResponse.getResponse(
				MiscUtils.encodeUrl(ApiUtils.INITIATEAD, params),
				StaticData.INITIATE_RESPONSE_CODE, this);
	}

	private void addClickListeners() {
		mbtnProductCategory.setOnClickListener(this);
		mbtnSubProductCategory.setOnClickListener(this);
		mbtnUploadPhotos.setOnClickListener(this);
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

		if (mProductsArray != null && mProductsArray.size() == 0) {
			InternalApp mApp = (InternalApp) getApplication();
			mProductsArray = mApp.getProductsArray();
		}

		if (mCategoriesArray != null && mCategoriesArray.size() == 0) {
			InternalApp mApp = (InternalApp) getApplication();
			mCategoriesArray = mApp.getCateogoriesArray();
		}
		if (mCateogoryMappingsArray != null
				&& mCateogoryMappingsArray.size() == 0) {
			InternalApp mApp = (InternalApp) getApplication();
			mCateogoryMappingsArray = mApp.getCategoryMappingArray();
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
		mLayoutSubCat = (LinearLayout) findViewById(R.id.layoutSubCat);
		mScrollimages = (HorizontalScrollView) findViewById(R.id.scrollImages);
		mLayoutAttachments = (LinearLayout) findViewById(R.id.layoutAttachments);
		mEditDailyCost.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					mEditWeeklyCost.requestFocus();
					return true;
				}
				return false;
			}
		});

		mEditWeeklyCost.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					mEditMonthlyCost.requestFocus();
					return true;
				}
				return false;
			}
		});

		mEditMonthlyCost
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_NEXT) {
							mEditQuantity.requestFocus();
							return true;
						}
						return false;
					}
				});

		mbtnSelectRating = (TextView) findViewById(R.id.btnSelectRating);
		mbtnSelectRating.setOnClickListener(this);

		mbtnProductCategory = (TextView) findViewById(R.id.btnSelectProductCategory);
		mbtnSubProductCategory = (TextView) findViewById(R.id.btnSelectSubProductCategory);
		mbtnUploadPhotos = (TextView) findViewById(R.id.btnUploadPhoto);
		mbtnNext = (TextView) findViewById(R.id.btnNext);
		// mImgProduct = (ImageView) findViewById(R.id.imgProduct);
		mbtnClear = (TextView) findViewById(R.id.btnClear);

		mSelectLayout = (LinearLayout) findViewById(R.id.selectControls);
		mlayoutFields = (LinearLayout) findViewById(R.id.layoutControlTypeCapacity);
		mtxtFields = (TextView) findViewById(R.id.txtFields);

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
		case R.id.btnClear:
			btnClearClicked();
			break;
		case R.id.btnSelectRating:
			btnSelectRatingClicked();
			break;
		default:
			break;
		}
	}

	private void btnUploadClicked() {
		showProgressLayout();
	}

	public String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
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
		mbtnSubProductCategory.setText("Select Product Name");

		StaticUtils.expandCollapse(mLayoutSubCat, false);
		StaticUtils.expandCollapse(mlayoutFields, false);
		clearAllFields();

	}

	private void btnUploadPhotoClicked() {
		if (mUriArray != null && mUriArray.size() < 5) {
			final String[] mArray = { "Captue Image", "Photo Gallery", "Cancel" };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setItems(mArray, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (mArray[which].toString().equalsIgnoreCase(
							"Captue Image")) {
						initiateCameraActivity();
					} else if (mArray[which].toString().equalsIgnoreCase(
							"Photo Gallery")) {
						initiateGalleryActivity();
					} else {

					}
				}
			});

			builder.setCancelable(false);
			AlertDialog alert = builder.create();
			alert.show();

		} else {
			showToast("You can select only 5 Images");
		}

	}

	private void btnNextClicked() {
		if (mbtnProductCategory.getText().toString()
				.equalsIgnoreCase("Select Product Category")) {
			showToast("Please Select Category");
		} else {
			if (mbtnSubProductCategory.getText().toString()
					.equalsIgnoreCase("Select Product Name")) {
				showToast("Please Select Product Name");
			} else {
				if (mEditTitle.getText().toString().length() == 0) {
					showToast("Please Enter Title");
				} else {
					if (mUriArray == null || mUriArray.size() == 0) {
						showToast("Please Select Photo");
					} else {
						if (mtxtRating == null || mtxtRating.length() == 0) {
							showToast("Please Select Rating");
						} else {
							if (!areFieldsValidated()) {

							} else {
								if (mEditPurchasedCost.getText().toString()
										.length() == 0) {
									showToast("Please Enter Purchased Cost");
								} else {
									if (mEditDailyCost.getText().toString()
											.length() == 0
											&& mEditWeeklyCost.getText()
													.toString().length() == 0
											&& mEditMonthlyCost.getText()
													.toString().length() == 0) {
										showToast("Please Enter Either Daily Cost, Weekly Cost or Monthly Cost");
									} else {
										if (mEditQuantity.getText().toString()
												.length() == 0) {
											showToast("Please Enter Quantity");
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

	private boolean areFieldsValidated() {
		String mtxtTitle = "";
		boolean isEditText = false;
		ArrayList<String> mFieldName = new ArrayList<String>();
		if (mSelectLayout != null && mSelectLayout.getChildCount() > 0) {
			for (int i = 0; i < mSelectLayout.getChildCount(); i++) {
				View mView = mSelectLayout.getChildAt(i);
				if (mView != null && mView instanceof EditText) {
					EditText mtxtView = (EditText) mView;
					String mTitle = mtxtView.getText().toString();
					if (mTitle.length() == 0) {
						isEditText = true;
						mtxtTitle = mtxtView.getHint().toString();
						mtxtView.requestFocus();
						break;
					}
				} else if (mView != null && mView instanceof TextView) {
					TextView mtxtView = (TextView) mView;
					String mTitle = mtxtView.getText().toString();
					if (mTitle.contains("Select")) {
						mtxtTitle = mTitle;
						isEditText = false;
						break;
					}
				}
			}
		}
		if (mtxtTitle == null || mtxtTitle.length() == 0) {
			return true;
		} else {
			if (isEditText) {
				showToast("Please " + mtxtTitle);
			} else {
				showToast("Please " + mtxtTitle);
			}
			return false;
		}
	}

	private void navigateToPostDocuments() {
		InternalApp mApp = (InternalApp) getApplication();
		mApp.setUriArray(mUriArray);
		mApp.setImageFilesArray(mImageFileArray);
		mApp.setArray(mArrayFields);
		if (TextUtils.isEmpty(StorageClass.getInstance(this).getUserName())) {
			startLoginActivity();
		} else {
			Intent mIntent = new Intent(this, PostAdDocumentActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putString("category", mbtnProductCategory.getText()
					.toString());
			mBundle.putString("subCategory", mbtnSubProductCategory.getText()
					.toString());
			mBundle.putString("adTitle", mEditTitle.getText().toString());
			mBundle.putString("productDescription", mEditShortDesc.getText()
					.toString());
			mBundle.putString("productCondition", mtxtRating);
			mBundle.putString("productConditionName", mbtnSelectRating
					.getText().toString());
			mBundle.putString("userInstructions", mEditInstructions.getText()
					.toString());
			mBundle.putString("additionalStuff", mEditStuff.getText()
					.toString());
			mBundle.putString("productPurchasedPrice", mEditPurchasedCost
					.getText().toString());
			mBundle.putString("dailyCost", mEditDailyCost.getText().toString());
			mBundle.putString("weekCost", mEditWeeklyCost.getText().toString());
			mBundle.putString("monthlyCost", mEditMonthlyCost.getText()
					.toString());
			mBundle.putString("quantity", mEditQuantity.getText().toString());
			mBundle.putString("securityDeposit", mEditSecurityDeposit.getText()
					.toString());
			mBundle.putString("filePath", mImageProfilePicPath);
			mBundle.putString("productAdId", productAdId);
			mBundle.putSerializable("controlLayouts", mControlLayouts);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
		}
	}

	private void startLoginActivity() {
		InternalApp mApp = (InternalApp) getApplication();
		mApp.setUriArray(mUriArray);
		mApp.setImageFilesArray(mImageFileArray);
		mApp.setArray(mArrayFields);
		Intent mIntent = new Intent(this, LoginActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("isPostAd", "yes");
		mBundle.putString("category", mbtnProductCategory.getText().toString());
		mBundle.putString("subCategory", mbtnSubProductCategory.getText()
				.toString());
		mBundle.putString("adTitle", mEditTitle.getText().toString());
		mBundle.putString("productDescription", mEditShortDesc.getText()
				.toString());
		mBundle.putString("productCondition", mtxtRating);
		mBundle.putString("productConditionName", mbtnSelectRating.getText()
				.toString());
		mBundle.putString("userInstructions", mEditInstructions.getText()
				.toString());
		mBundle.putString("additionalStuff", mEditStuff.getText().toString());
		mBundle.putString("productPurchasedPrice", mEditPurchasedCost.getText()
				.toString());
		mBundle.putString("dailyCost", mEditDailyCost.getText().toString());
		mBundle.putString("weekCost", mEditWeeklyCost.getText().toString());
		mBundle.putString("monthlyCost", mEditMonthlyCost.getText().toString());
		mBundle.putString("quantity", mEditQuantity.getText().toString());
		mBundle.putString("securityDeposit", mEditSecurityDeposit.getText()
				.toString());
		mBundle.putString("filePath", mImageProfilePicPath);
		mBundle.putString("productAdId", productAdId);
		mBundle.putSerializable("controlLayouts", mControlLayouts);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	int pos = -1;
	private ArrayList<String> mSubCategories = new ArrayList<String>();
	private JSONArray mFieldsArray;
	private Dialog mCustomDialog;

	private void btnSelectSubProductCategoryClicked() {
		showProgressLayout();
		if (mbtnProductCategory.getText().toString()
				.equalsIgnoreCase("Select Product Category")) {
			showToast("Please Select Product Category");
		} else {
			if (mSubCategories != null && mSubCategories.size() > 0) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						showaAlert(mSubCategories);
					}

				}, 200);
			}
		}
	}

	private void showaAlert(ArrayList<String> mSubCategories) {
		hideProgressLayout();
		int pos = -1;
		if (mSubCategories != null) {
			final String[] mSubCat = new String[mSubCategories.size()];
			for (int i = 0; i < mSubCategories.size(); i++) {
				mSubCat[i] = mSubCategories.get(i);
				if (mbtnSubProductCategory.getText().toString()
						.equalsIgnoreCase("Select Product Name")) {
					pos = -1;
				} else {
					if (mSubCat[i].equalsIgnoreCase(mbtnSubProductCategory
							.getText().toString())) {
						pos = i;
					}
				}
			}

			AlertDialog.Builder alert = new AlertDialog.Builder(
					PostAdActivity.this);
			alert.setTitle("Select Product Name");
			alert.setSingleChoiceItems(mSubCat, pos,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mbtnSubProductCategory.setText(mSubCat[which]);
							dialog.dismiss();
							initTemplateForCategoryApi();
							clearAllFields();
						}

					});
			alert.show();
		}
	}

	private void clearAllFields() {
		mEditDailyCost.setText("");
		mEditInstructions.setText("");
		mEditMonthlyCost.setText("");
		mEditPurchasedCost.setText("");
		mEditQuantity.setText("");
		mEditSecurityDeposit.setText("");
		mEditShortDesc.setText("");
		mEditStuff.setText("");
		mEditTitle.setText("");
		mEditWeeklyCost.setText("");
		mImageProfilePicPath = "";
		mbtnSelectRating.setText("Select Rating");
		mSelectLayout.removeAllViews();

		mLayoutAttachments.removeAllViews();
		mUriArray.clear();
		mImageArrayPaths.clear();
		StaticUtils.expandCollapse(mScrollimages, false);
		mImageFileArray.clear();
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
			mArrayFields = new ArrayList<PostAdModel>();
			mFieldsArray = response.optJSONArray("Fields");
			if (mFieldsArray != null) {
				for (int i = 0; i < mFieldsArray.length(); i++) {
					JSONObject mFieldsObj = mFieldsArray.optJSONObject(i);

					if (mFieldsObj != null) {
						PostAdModel mModel = new PostAdModel();
						mModel.setFieldName(mFieldsObj.optString("FieldName"));
						mModel.setFieldTitle(mFieldsObj.optString("FieldTitle"));
						mModel.setValues(mFieldsObj.optJSONArray("Values"));
						mArrayFields.add(mModel);
					}
				}
			}
		}

		loadControlsLayout();
	}

	@SuppressLint("NewApi")
	private void loadControlsLayout() {
		ArrayList<String> mFieldName = new ArrayList<String>();
		if (mArrayFields != null && mArrayFields.size() > 0) {
			mSelectLayout.removeAllViews();
			for (int i = 0; i < mArrayFields.size(); i++) {
				final PostAdModel mModel = mArrayFields.get(i);
				if (mModel != null) {
					if (mModel.getValues() == null
							|| mModel.getValues().length() == 0) {
						final EditText mEditTexxt = (EditText) LayoutInflater
								.from(this).inflate(R.layout.editselectcontrol,
										null);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								new LayoutParams(LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT));
						params.setMargins(10, 10, 10, 10);
						mEditTexxt.setLayoutParams(params);
						mEditTexxt.setHint("Enter " + mModel.getFieldTitle());
						mEditTexxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
						mFieldName.add(mModel.getFieldTitle());
						StaticUtils.setEditTextHintFont(mEditTexxt, this);
						mSelectLayout.addView(mEditTexxt);
					} else {
						final TextView mtxtView = (TextView) LayoutInflater
								.from(this).inflate(R.layout.selectcontrol,
										null);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								new LayoutParams(LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT));
						params.setMargins(10, 10, 10, 10);
						mtxtView.setLayoutParams(params);
						mtxtView.setText("Select " + mModel.getFieldTitle());
						mFieldName.add(mModel.getFieldTitle());
						mtxtView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// if (mtxtView.getText().toString()
								// .contains("Others")
								// || mtxtView.getText().toString()
								// .contains("Other")) {
								// showPopup(mtxtView);
								// } else {
								showSelectAlert(mModel, mtxtView);
								// }
							}
						});
						mSelectLayout.addView(mtxtView);
					}

				}
			}
		}

		if (mFieldName != null && mFieldName.size() > 0) {
			String mTitles = "";
			for (int i = 0; i < mFieldName.size(); i++) {
				if (mTitles.length() == 0) {
					mTitles = mFieldName.get(i);
				} else {
					mTitles = mTitles + ", " + mFieldName.get(i);
				}
			}
			mtxtFields.setText(mTitles);
		}

		// StaticUtils.expandCollapse(mSelectLayout, true);
		StaticUtils.expandCollapse(mlayoutFields, true);
	}

	private void showSelectAlert(final PostAdModel mModel,
			final TextView mtxtView) {

		ArrayList<String> mStringValues = new ArrayList<String>();
		if (mModel.getValues() != null) {
			JSONArray mValuesArray = mModel.getValues();
			for (int j = 0; j < mValuesArray.length(); j++) {
				JSONObject mValueObj = mValuesArray.optJSONObject(j);
				if (mValueObj != null
						&& !mValueObj.optString("Value").trim()
								.equalsIgnoreCase("")) {
					mStringValues.add(mValueObj.optString("Value"));
				}
			}

		}

		if (mStringValues != null && mStringValues.size() > 0) {
			mStringSelectValues = new String[mStringValues.size()];
			for (int i = 0; i < mStringValues.size(); i++) {
				mStringSelectValues[i] = mStringValues.get(i);
			}
		}
		int pos = -1;
		if (mtxtView.getText().toString().contains("Select")) {
			pos = -1;
		} else {
			for (int i = 0; i < mStringSelectValues.length; i++) {
				if (mStringSelectValues[i].equalsIgnoreCase(mtxtView.getText()
						.toString())) {
					pos = i;
				}
			}
		}

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Select " + mModel.getFieldTitle());
		alert.setSingleChoiceItems(mStringSelectValues, pos,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mStringSelectValues[which].contains("Others")
								|| mStringSelectValues[which].contains("Other")) {
							showPopup(mtxtView, mModel.getFieldTitle());
						} else {
							mtxtView.setText(mStringSelectValues[which]);
							mControlLayouts.put(mModel.getFieldTitle(),
									mStringSelectValues[which]);
						}
						dialog.dismiss();
					}

				});
		alert.show();
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
								setSubProductsArray();
							}
						});
				alert.show();
			}
		}
	}

	private void btnSelectRatingClicked() {
		final String[] mRartings = { "Like New", "Very Good", "Good",
				"Average", "Working" };
		if (mRartings != null) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Select Rating");
			int position = -1;
			for (int i = 0; i < mRartings.length; i++) {
				if (mbtnSelectRating.getText().toString()
						.equalsIgnoreCase("Select Rating")) {
					position = -1;
				} else {
					if (mRartings[i].equalsIgnoreCase(mbtnSelectRating
							.getText().toString())) {
						position = i;
					}
				}
			}
			alert.setSingleChoiceItems(mRartings, position,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mbtnSelectRating.setText(mRartings[which]);
							if (mRartings[which].equalsIgnoreCase("Like New")) {
								mtxtRating = "5";
							} else if (mRartings[which]
									.equalsIgnoreCase("Very Good")) {
								mtxtRating = "4";
							} else if (mRartings[which]
									.equalsIgnoreCase("Good")) {
								mtxtRating = "3";
							} else if (mRartings[which]
									.equalsIgnoreCase("Average")) {
								mtxtRating = "2";
							} else if (mRartings[which]
									.equalsIgnoreCase("Working")) {
								mtxtRating = "1";
							}
							dialog.dismiss();
						}
					});
			alert.show();
		}
	}

	private void setSubProductsArray() {
		mbtnSubProductCategory.setText("Select Product Name");
		clearAllFields();
		StaticUtils.expandCollapse(mlayoutFields, false);
		if (mCateogoryMappingsArray != null) {
			mSubCategories = new ArrayList<String>();
			for (int i = 0; i < mCateogoryMappingsArray.size(); i++) {
				CategoryModel mModel = mCateogoryMappingsArray.get(i);
				if (mbtnSubProductCategory.getText().toString()
						.equalsIgnoreCase("Select Product Name")) {
					pos = -1;
				} else {
					if (mModel.getTitle().equalsIgnoreCase(
							mbtnSubProductCategory.getText().toString())) {
						pos = i;
					}
				}
				if (mModel.getCategory().equalsIgnoreCase(
						mbtnProductCategory.getText().toString())) {
					mSubCategories.add(mModel.getTitle().toString());
				}
			}
		}
		StaticUtils.expandCollapse(mLayoutSubCat, true);
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
			cameraFile = new File(StaticUtils.getSabRentKaroCameraDir(),
					"IMAGE_" + System.currentTimeMillis() + ".jpg");
			Uri cameraImageUri = Uri.fromFile(cameraFile);
			Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			camerIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					cameraImageUri);
			startActivityForResult(camerIntent, CAMERA_SELECT_CODE);
		} else {
			showToast("Camera feature not available");
		}
	}

	@SuppressLint("InlinedApi")
	protected void initiateGalleryActivity() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(photoPickerIntent, CAMERA_SELECT_CODE);
	}

	@SuppressWarnings("unused")
	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CAMERA_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				if (cameraFile != null) {
					MediaScannerConnection
							.scanFile(
									this,
									new String[] { cameraFile.getPath() },
									new String[] { "image/jpeg" },
									new MediaScannerConnection.OnScanCompletedListener() {
										@Override
										public void onScanCompleted(
												String path, final Uri uri) {
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													if (uri != null) {
														mUriArray.add(uri);
													}
													mImageFileArray
															.add(cameraFile);
													loadAttachments();
												}
											});
										}
									});
					// }
				} else {
					Uri uri = data.getData();
					if (uri != null) {
						mUriArray.add(uri);
						mImageFileArray.add(new File(uri.getPath()));
					} else {
						boolean isMaxFile = false;
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							ClipData clipData = data.getClipData();

							if (clipData != null) {
								for (int i = 0; i < clipData.getItemCount(); i++) {
									mUriArray.add(clipData.getItemAt(i)
											.getUri());
									mImageFileArray.add(new File(clipData
											.getItemAt(i).getUri().toString()));
								}
							}
						}
					}
				}
			}
			break;
		}
		cameraFile = null;
		loadAttachments();
	}

	public String getPathfromUri(Uri mUri) {
		File myFile = new File(mUri.getPath());
		String mPath = myFile.getAbsolutePath();
		return mPath;
	}

	public String getRealPathFromURI(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void ErrorResponse(VolleyError error, int requestCode) {
		hideProgressLayout();
	}

	@Override
	public void SuccessResponse(JSONObject response, int requestCode) {
		hideProgressLayout();
		switch (requestCode) {
		case StaticData.INITIATE_RESPONSE_CODE:
			responseForInitiateProductAdId(response);
			break;

		default:
			break;
		}
	}

	private void responseForInitiateProductAdId(JSONObject response) {
		if (response != null) {
			productAdId = response.optString("Id");
		}
	}

	@Override
	public void SuccessResponse(JSONArray response, int requestCode) {

	}

	private void showPopup(final TextView mtxtView, final String title) {
		mCustomDialog = new Dialog(this);
		mCustomDialog.setCancelable(false);
		mCustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mCustomDialog.setContentView(R.layout.cutsom_popup);
		mCustomDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		TextView mbtnDone = (TextView) mCustomDialog.findViewById(R.id.btnDone);

		final EditText meditText = (EditText) mCustomDialog
				.findViewById(R.id.editText);
		StaticUtils.setEditTextHintFont(meditText, this);
		String mText = "";
		if (mtxtView.getText().toString().contains("Others - ")) {
			mText = mtxtView.getText().toString().replace("Others - ", "");
		}
		if (mText.length() == 0) {

		} else {
			meditText.setText(mText);
		}
		mCustomDialog.show();

		mbtnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(meditText.getText().toString())) {
					showToast("Please Enter");
				} else {
					mCustomDialog.dismiss();
					mtxtView.setText("Others - "
							+ meditText.getText().toString());
					mControlLayouts.put(title, meditText.getText().toString());
					if (areFieldsValidated()) {
						mEditPurchasedCost.requestFocus();
					} else {
						hideKeyboard(meditText);
					}
				}
			}
		});
	}

	protected void hideKeyboard(EditText meditText) {
		if (meditText != null && getWindow() != null
				&& getWindow().getDecorView() != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(meditText.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(getWindow().getDecorView()
					.getWindowToken(), 0);
		}
	}

	@SuppressLint("NewApi")
	private void loadAttachments() {
		mLayoutAttachments.removeAllViews();
		if (mUriArray != null) {
			for (int i = 0; i < mUriArray.size(); i++) {
				Uri mUri = mUriArray.get(i);
				if (mUri != null) {
					final LinearLayout mPhotoView = (LinearLayout) LayoutInflater
							.from(this).inflate(R.layout.layoutphoto, null);
					final SquareImageView mImageView = (SquareImageView) mPhotoView
							.findViewById(R.id.imgProduct);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							new LayoutParams(300, 300));
					params.setMargins(10, 10, 10, 10);
					mImageView.setLayoutParams(params);
					mImageView.setImageURI(mUri);
					int xy = (int) (200 * getResources().getDimension(
							R.dimen.one_dp));
					Point size = new Point(xy, xy);
					String mime = getMimeType(mUri);
					if (mime != null && mime.startsWith("image")) {
						Bitmap bmp = getResizedBitmap(mUri, size);
						if (bmp == null) {
						} else {
							mImageView.setImageBitmap(bmp);
						}
					}
					mImageView.setTag(mUri);
					mImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showAlert(mImageView);
						}
					});
					mLayoutAttachments.addView(mPhotoView, 300, 300);
				}
			}
		}
		StaticUtils.expandCollapse(mScrollimages, true);
		// StaticUtils.expandCollapse(mLayoutAttachments, true);
	}

	private void showAlert(final SquareImageView mImageView) {
		new AlertDialog.Builder(this)
				.setTitle("Warning")
				.setMessage("Do you want to delete the Image ?")
				.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog.cancel();
					}
				})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						})
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								Uri mUri = (Uri) mImageView.getTag();
								if (mUri != null) {
									if (mUriArray != null
											&& mUriArray.size() > 0) {
										if (mUriArray.contains(mUri)) {
											mUriArray.remove(mUri);
											loadAttachments();
										}
										if (mImageFileArray.contains(new File(
												mUri.toString()))) {
											mImageFileArray.remove(new File(
													mUri.toString()));
										}
									}
								}
							}
						}).create().show();

	}

	private Bitmap getBitmap(Uri uri) {
		InputStream in = null;
		try {
			final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
			in = getContentResolver().openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth
					+ ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = getContentResolver().openInputStream(uri);
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = b.getHeight();
				int width = b.getWidth();
				Log.d("", "1th scale operation dimenions - width: " + width
						+ ", height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
						(int) y, true);
				b.recycle();
				b = scaledBitmap;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			Log.d("",
					"bitmap size - width: " + b.getWidth() + ", height: "
							+ b.getHeight());
			return b;
		} catch (IOException e) {
			Log.e("", e.getMessage(), e);
			return null;
		}
	}

	private String getMimeType(Uri file) {
		String contentType;
		String ext;
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		if ("file".equalsIgnoreCase(file.getScheme())) {
			String filePath = file.getPath();
			ext = filePath.substring(filePath.lastIndexOf(".") + 1);
			contentType = mime.getMimeTypeFromExtension(ext);
		} else {
			contentType = getContentResolver().getType(file);
			// ext = mime.getExtensionFromMimeType(contentType);
		}
		return contentType;
	}

	private Bitmap getResizedBitmap(Uri file, Point size) {
		// First decode with inJustDecodeBounds=true to check dimensions
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if ("file".equalsIgnoreCase(file.getScheme())) {
			BitmapFactory.decodeFile(file.getPath(), options);
		} else {
			InputStream stream = null;
			try {
				stream = getContentResolver().openInputStream(file);
			} catch (IOException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
			if (stream != null) {
				BitmapFactory.decodeStream(stream, null, options);
			}
		}
		Bitmap bmp = null;
		if (options.outHeight > 0 && options.outWidth > 0) {
			// Calculate inSampleSize
			options.inSampleSize = StaticUtils.calculateInSampleSize(options,
					size.x, size.y);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			try {
				if ("file".equalsIgnoreCase(file.getScheme())) {
					bmp = BitmapFactory.decodeFile(file.getPath(), options);
				} else {
					InputStream stream = null;
					try {
						stream = getContentResolver().openInputStream(file);
					} catch (IOException e) {
						Log.e(this.getClass().getName(), e.getMessage());
					}
					if (stream != null) {
						bmp = BitmapFactory.decodeStream(stream, null, options);
					}
				}
			} catch (OutOfMemoryError oome) {
				Log.e("Out Of Memory Error",
						oome.getMessage() == null ? "OutOfMemory error: size "
								+ size.x + "x" + size.y : oome.getMessage());
			}
		}
		return bmp;
	}
}
