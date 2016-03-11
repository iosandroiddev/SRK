package com.sabrentkaro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {
	private WebView mWebView;
	private String loadText = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.web_layout);

		loadLayoutReferences();
		getDetails();
		showProgressLayout();

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			String mViewtoLoad = mBundle.getString("viewToLoad");
			if (mViewtoLoad.equalsIgnoreCase("terms")) {
				loadText = "file:///android_asset/terms.html";
				mWebView.loadUrl(loadText);
			} else if (mViewtoLoad.equalsIgnoreCase("aboutUs")) {
				loadText = "file:///android_asset/index.html";

			} else if (mViewtoLoad.equalsIgnoreCase("rentingPolicy")) {
				loadText = "file:///android_asset/RentingPolicy.html";
			} else if (mViewtoLoad.equalsIgnoreCase("privacyPolicy")) {
				loadText = "file:///android_asset/PrivacyPolicy.html";
			} else if (mViewtoLoad.equalsIgnoreCase("legalDisc")) {
				loadText = "file:///android_asset/LegalDisclaimer.html";
			} else if (mViewtoLoad.equalsIgnoreCase("listingPolicy")) {
				loadText = "file:///android_asset/ListingPolicy.html";
			}

			mWebView.loadUrl(loadText);
		}

		mWebView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				hideProgressLayout();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals(loadText)) {
					view.loadUrl(url);
				}
				return true;
			}
		});

		// StaticUtils.loadHtmlContent(mWebView, loadText, 15);
	}

	private void loadLayoutReferences() {
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.getSettings().setRenderPriority(
				WebSettings.RenderPriority.HIGH);
		mWebView.setFocusable(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setFocusableInTouchMode(true);

	}

}
