package com.sabrentkaro.search;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sabrentkaro.HomeActivity;
import com.sabrentkaro.R;
import com.utils.StorageClass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class PayUIntegration extends FragmentActivity {

	// private Button button;

	private static final String TAG = "MainActivity";
	WebView webviewPayment;
	WebView mwebview;
	TextView txtview;
	private String amount;
	private String orderId;

	/*
	 * protected void writeStatus(String str){ txtview.setText(str); }
	 */

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_layout);
		getDeatils();
		webviewPayment = (WebView) findViewById(R.id.webView);
		webviewPayment.getSettings().setJavaScriptEnabled(true);
		webviewPayment.getSettings().setDomStorageEnabled(true);
		webviewPayment.getSettings().setLoadWithOverviewMode(true);
		webviewPayment.getSettings().setUseWideViewPort(true);
		webviewPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webviewPayment.getSettings().setSupportMultipleWindows(true);
		webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(
				true);
		webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(),
				"PayUMoney");
		// webviewPayment.loadUrl("http://www.google.com");
		/*
		 * webviewPayment .loadUrl(
		 * "128.199.193.113/rakhi/payment/endpoint?order_id=aAbBcC45&amount=10"
		 * );
		 */

		// webviewPayment.loadUrl("http://timesofindia.com/");
		StringBuilder url_s = new StringBuilder();
		// http://merirakhi.com/processor/payment/endpoint?order_id=aAbBcC&amount=10&currency=USD
		url_s.append("https://test.payu.in/_payment");
		// url_s.append("https://secure.payu.in/_payment");
		Log.e(TAG, "call url " + url_s);

		// webviewPayment.loadUrl(url_s.toString());
		// String postData = "username=my_username&password=my_password";
		webviewPayment.postUrl(url_s.toString(),
				EncodingUtils.getBytes(getPostString(), "utf-8"));

		// webviewPayment.loadUrl("http://128.199.193.113/rakhi/payment/endpoint?order_id=aAbBcC45&amount=0.10&currency=USD");

		webviewPayment.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (url.contains("https://www.payumoney.com/mobileapp/payumoney/success.php")) {
					showSuccessPage();
				} else if (url
						.contains("https://www.payumoney.com/mobileapp/payumoney/failure.php")) {
					showFailurePage();
				}
			}

			@SuppressWarnings("unused")
			public void onReceivedSslError(WebView view) {
				Log.e("Error", "Exception caught!");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
	}

	private void getDeatils() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			amount = mBundle.getString("amount");
		}
	}

	private final class PayUJavaScriptInterface {
		PayUJavaScriptInterface() {
		}

		public void success(long id, final String paymentId) {
			PayUIntegration.this.orderId = paymentId;
			runOnUiThread(new Runnable() {
				public void run() {
					// Toast.makeText(PayUIntegration.this, "Status is txn is
					// success " + " payment id is " + paymentId,
					// 8000).show();
					// String
					// str="Status is txn is success "+" payment id is
					// "+paymentId;
					// new MainActivity().writeStatus(str);

					// TextView txtview;
					// txtview = (TextView) findViewById(R.id.textView1);
					// txtview.setText("Status is txn is success "
					// + " payment id is " + paymentId);

				}
			});
		}

		public void success() {
			// PayUIntegration.this.orderId = paymentId;
			runOnUiThread(new Runnable() {
				public void run() {
					// Toast.makeText(PayUIntegration.this, "Status is txn is
					// success " + " payment id is " + "", 8000)
					// .show();
					// String
					// str="Status is txn is success "+" payment id is
					// "+paymentId;
					// new MainActivity().writeStatus(str);

					// TextView txtview;
					// txtview = (TextView) findViewById(R.id.textView1);
					// txtview.setText("Status is txn is success "
					// + " payment id is " + paymentId);

				}
			});
		}

	}

	private void showSuccessPage() {
		// Toast.makeText(PayUIntegration.this, "Transaction Success",
		// Toast.LENGTH_LONG).show();
		navigateToInvoice();
	}

	public void navigateToInvoice() {
		Bundle mBundle = getIntent().getExtras();
		Intent mIntent = new Intent(this, InvoiceDetailsActivity.class);
		Bundle invoicePageData = new Bundle();
		invoicePageData.putString("orderId", orderId);
		invoicePageData.putString("amount", mBundle.getString("amount"));
		invoicePageData.putString("quantity", mBundle.getString("quantity"));
		invoicePageData.putString("startDate", mBundle.getString("startDate"));
		invoicePageData.putString("endDate", mBundle.getString("endDate"));
		invoicePageData.putString("address", mBundle.getString("address"));
		invoicePageData.putString("productDescription",
				mBundle.getString("productDescription"));
		invoicePageData.putString("invoicePhone",
				mBundle.getString("invoicePhone"));
		mIntent.putExtras(invoicePageData);
		startActivity(mIntent);
	}

	public void navigateToHome() {
		Intent mIntent = new Intent(this, HomeActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
		finish();
	}

	private void showFailurePage() {
		// Toast.makeText(PayUIntegration.this, "Transaction Failure",
		// Toast.LENGTH_LONG).show();
		finish();
	}

	private String getPostString() {
		String key = "OygoFs";
		// String key = "v8zzo2Bg";
		String salt = "BV1QBwCv";
		// String salt = "5UkdKIu29m";
		String txnid = "TXN_1";
		String amnnt = amount;
		String firstname = StorageClass.getInstance(this).getUserName();
		String email = StorageClass.getInstance(this).getUserEmail();
		String productInfo = "Product1";

		StringBuilder post = new StringBuilder();
		post.append("key=");
		post.append(key);
		post.append("&");
		post.append("txnid=");
		post.append(txnid);
		post.append("&");
		post.append("amount=");
		post.append(amnnt);
		post.append("&");
		post.append("productinfo=");
		post.append(productInfo);
		post.append("&");
		post.append("firstname=");
		post.append(firstname);
		post.append("&");
		post.append("email=");
		post.append(email);
		post.append("&");
		post.append("phone=");
		post.append(StorageClass.getInstance(this).getMobileNumber());
		post.append("&");
		post.append("surl=");
		post.append("https://www.payumoney.com/mobileapp/payumoney/success.php");
		post.append("&");
		post.append("furl=");
		post.append("https://www.payumoney.com/mobileapp/payumoney/failure.php");
		post.append("&");

		StringBuilder checkSumStr = new StringBuilder();
		/*
		 * =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|
		 * udf4 |udf5||||||salt)
		 */
		MessageDigest digest = null;
		String hash;
		try {
			digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");

			checkSumStr.append(key);
			checkSumStr.append("|");
			checkSumStr.append(txnid);
			checkSumStr.append("|");
			checkSumStr.append(amount);
			checkSumStr.append("|");
			checkSumStr.append(productInfo);
			checkSumStr.append("|");
			checkSumStr.append(firstname);
			checkSumStr.append("|");
			checkSumStr.append(email);
			checkSumStr.append("|||||||||||");
			checkSumStr.append(salt);

			digest.update(checkSumStr.toString().getBytes());

			hash = bytesToHexString(digest.digest());
			post.append("hash=");
			post.append(hash);
			post.append("&");
			Log.i(TAG, "SHA result is " + hash);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		post.append("service_provider=");
		post.append("payu_paisa");
		return post.toString();
	}

	private JSONObject getProductInfo() {
		try {
			// create payment part object
			JSONObject productInfo = new JSONObject();

			JSONObject jsonPaymentPart = new JSONObject();
			jsonPaymentPart.put("name", "TapFood");
			jsonPaymentPart.put("description", "Lunchcombo");
			jsonPaymentPart.put("value", "500");
			jsonPaymentPart.put("isRequired", "true");
			jsonPaymentPart.put("settlementEvent", "EmailConfirmation");

			// create payment part array
			JSONArray jsonPaymentPartsArr = new JSONArray();
			jsonPaymentPartsArr.put(jsonPaymentPart);

			// paymentIdentifiers
			JSONObject jsonPaymentIdent = new JSONObject();
			jsonPaymentIdent.put("field", "CompletionDate");
			jsonPaymentIdent.put("value", "31/10/2012");

			// create payment part array
			JSONArray jsonPaymentIdentArr = new JSONArray();
			jsonPaymentIdentArr.put(jsonPaymentIdent);

			productInfo.put("paymentParts", jsonPaymentPartsArr);
			productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

			Log.e(TAG, "product Info = " + productInfo.toString());
			return productInfo;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

}
