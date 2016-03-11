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
				mWebView.loadUrl("file:///android_asset/terms.html");
			} else if (mViewtoLoad.equalsIgnoreCase("aboutUs")) {
				mWebView.loadUrl("file:///android_asset/index.html");
			} else if (mViewtoLoad.equalsIgnoreCase("rentingPolicy")) {
				mWebView.loadUrl("file:///android_asset/RentingPolicy.html");
			} else if (mViewtoLoad.equalsIgnoreCase("privacyPolicy")) {
				mWebView.loadUrl("file:///android_asset/PrivacyPolicy.html");
			} else if (mViewtoLoad.equalsIgnoreCase("legalDisc")) {
				mWebView.loadUrl("file:///android_asset/LegalDisclaimer.html");
			} else if (mViewtoLoad.equalsIgnoreCase("listingPolicy")) {
				mWebView.loadUrl("file:///android_asset/ListingPolicy.html");
			}
		}

		mWebView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				hideProgressLayout();
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
