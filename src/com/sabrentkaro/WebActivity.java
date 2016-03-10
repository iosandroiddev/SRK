package com.sabrentkaro;

import com.utils.StaticUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

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

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			String mViewtoLoad = mBundle.getString("viewToLoad");
			if (mViewtoLoad.equalsIgnoreCase("terms")) {
				mWebView.loadUrl("http://allrental.co.in/documents/termsofuse");
			} else if (mViewtoLoad.equalsIgnoreCase("aboutUs")) {
				mWebView.loadUrl("http://allrental.co.in/documents/aboutus");
			} else if (mViewtoLoad.equalsIgnoreCase("rentingPolicy")) {
				mWebView.loadUrl("file:///android_asset/About Us.html");
			} else if (mViewtoLoad.equalsIgnoreCase("privacyPolicy")) {
				mWebView.loadUrl("http://allrental.co.in/documents/privacypolicy");
			} else if (mViewtoLoad.equalsIgnoreCase("legalDisc")) {
				mWebView.loadUrl("http://allrental.co.in/documents/legaldisclaimer");
			} else if (mViewtoLoad.equalsIgnoreCase("listingPolicy")) {
				mWebView.loadUrl("http://allrental.co.in/documents/rentingpolicy");
			}
		}

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
		mWebView.setFocusableInTouchMode(false);

	}

}
