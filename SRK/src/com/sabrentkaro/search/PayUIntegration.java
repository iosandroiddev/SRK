package com.sabrentkaro.search;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.sabrentkaro.HomeActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.utils.StorageClass;

public class PayUIntegration extends FragmentActivity {

	// private Button button;

	public static boolean isExpired = false;
	public static boolean isHostMisMatch = false;
	public static boolean isNotTrusted = false;

	private static final String TAG = "MainActivity";
	WebView webviewPayment;
	WebView mwebview;
	TextView txtview;
	private String amount;
	private String orderId;
	private String mtransactionId;
	private String mwvUrl = "";
	private String strTrustError = "";
	private String strMisMatchError = "";
	private String strExpiryError = "";

	private SSLContext sslContext;
	private final int timeoutConnection = 20000;
	private final int timeoutRead = 20000;
	private X509Certificate[] currentCertificateChain;
	public JSONArray mUCertificateArray;

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

		if (InternalApp.isProductionApi) {
			url_s.append("https://secure.payu.in/_payment");
		} else {
			url_s.append("https://test.payu.in/_payment");
		}

		Log.e(TAG, "call url " + url_s);

		// webviewPayment.loadUrl(url_s.toString());
		// String postData = "username=my_username&password=my_password";
		webviewPayment.postUrl(url_s.toString(),
				EncodingUtils.getBytes(getPostString(), "utf-8"));

		// webviewPayment.loadUrl("http://128.199.193.113/rakhi/payment/endpoint?order_id=aAbBcC45&amount=0.10&currency=USD");
		setWebViewClient();
		// webviewPayment.setWebViewClient(new WebViewClient() {
		// @Override
		// public void onPageFinished(WebView view, String url) {
		// super.onPageFinished(view, url);
		// if
		// (url.contains("https://www.payumoney.com/mobileapp/payumoney/success.php"))
		// {
		// showSuccessPage();
		// } else if (url
		// .contains("https://www.payumoney.com/mobileapp/payumoney/failure.php"))
		// {
		// showFailurePage();
		// }
		// }
		//
		// @Override
		// public void onReceivedSslError(WebView view,
		// SslErrorHandler handler, SslError error) {
		// handler.proceed();
		// }
		//
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return true;
		// }
		//
		// });
	}

	private void setWebViewClient() {
		webviewPayment.setWebViewClient(new SSLTolerentWebViewClient());
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
					Toast.makeText(
							PayUIntegration.this,
							"Status is txn is success " + " payment id is "
									+ paymentId, 8000).show();
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
		invoicePageData.putString("transacationID", mtransactionId);
		invoicePageData.putString("orderId", orderId);
		invoicePageData.putString("amount", mBundle.getString("amount"));
		invoicePageData.putString("quantity", mBundle.getString("quantity"));
		invoicePageData.putString("startDate", mBundle.getString("startDate"));
		invoicePageData.putString("endDate", mBundle.getString("endDate"));
		invoicePageData.putString("startDateStr",
				mBundle.getString("startDateStr"));
		invoicePageData
				.putString("endDateStr", mBundle.getString("endDateStr"));
		invoicePageData.putString("address", mBundle.getString("address"));
		invoicePageData.putString("facilitationCost",
				mBundle.getString("facilitationCost"));
		invoicePageData
				.putString("serviceTax", mBundle.getString("serviceTax"));
		invoicePageData.putString("logisticsCost",
				mBundle.getString("logisticsCost"));
		invoicePageData.putString("productDescription",
				mBundle.getString("productDescription"));
		invoicePageData.putString("invoicePhone",
				mBundle.getString("invoicePhone"));
		invoicePageData.putString("securityDeposit",
				mBundle.getString("securityDeposit"));
		invoicePageData.putString("securityDepositValue",
				mBundle.getString("securityDepositValue"));
		invoicePageData.putString("data", mBundle.getString("data"));
		invoicePageData.putString("addressResponse",
				mBundle.getString("addressResponse"));
		invoicePageData.putString("mItemDetailsArray",
				mBundle.getString("mItemDetailsArray"));
		invoicePageData.putString("productRentalValue",
				mBundle.getString("productRentalValue"));
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

		String key = "";
		String salt = "";
		if (InternalApp.isProductionApi) {
			key = "v8zzo2Bg";
			salt = "5UkdKIu29m";
		} else {
			key = "OygoFs";
			salt = "BV1QBwCv";
		}

		Random rand = new Random();
		String randomString = Integer.toString(rand.nextInt())
				+ (System.currentTimeMillis() / 1000L);

		mtransactionId = "";
		mtransactionId = hashCal("SHA-256", randomString).substring(0, 20);
		String amnnt = amount;
		String firstname = StorageClass.getInstance(this).getUserName();
		String email = StorageClass.getInstance(this).getUserEmail();
		String productInfo = "Product1";

		StringBuilder post = new StringBuilder();
		post.append("key=");
		post.append(key);
		post.append("&");
		post.append("txnid=");
		post.append(mtransactionId);
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
			checkSumStr.append(mtransactionId);
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

	public String hashCal(String type, String str) {
		byte[] hashSequence = str.getBytes();
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest algorithm = MessageDigest.getInstance(type);
			algorithm.reset();
			algorithm.update(hashSequence);
			byte messageDigest[] = algorithm.digest();

			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append("0");
				hexString.append(hex);
			}
		} catch (NoSuchAlgorithmException NSAE) {
		}
		return hexString.toString();
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

	private class SSLTolerentWebViewClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// mwvUrl = view.getUrl();
			new RetrieveFeedTask().startExecution(handler);
		}

		@SuppressLint("NewApi")
		@Override
		public void onReceivedClientCertRequest(WebView view,
				ClientCertRequest request) {
			super.onReceivedClientCertRequest(view, request);
		}

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mwvUrl = url;
		}

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

	}

	class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

		private SslErrorHandler handler;

		@Override
		protected Void doInBackground(String... params) {
			HttpURLConnection connection = checkAuthentication(mwvUrl, null);
			try {
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (handler != null) {
				if (isExpired) {
					String message = "";
					if (TextUtils.isEmpty(strExpiryError)) {
						message = "The secure connection failed because the server certificate has expired.";
					} else {
						message = strExpiryError;
					}
					new AlertDialog.Builder(PayUIntegration.this)
							.setMessage(message)
							.setCancelable(false)
							.setOnDismissListener(new OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
								}
							})
							.setNegativeButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
											handler.cancel();
											finishActivity();
										}
									}).create().show();
				} else if (isHostMisMatch) {
					String message = "";
					if (TextUtils.isEmpty(strMisMatchError)) {
						message = "The host name doesn\'t matched with certificate\'s common name.";
					} else {
						message = strMisMatchError;
					}
					new AlertDialog.Builder(PayUIntegration.this)
							.setMessage(message)
							.setCancelable(false)
							.setOnDismissListener(new OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
								}
							})
							.setNegativeButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
											handler.cancel();
											finishActivity();
										}
									}).create().show();
				} else if (isNotTrusted) {

					boolean isAlreadyTrustedByUser = false;
					boolean isAlreadyTrusted = false;

					String mArray = StorageClass.getInstance(
							PayUIntegration.this).getJSONArrayCertificates();
					JSONArray mJSONArray = null;
					try {
						mJSONArray = new JSONArray(mArray);
					} catch (JSONException e) {
						if (e != null && e.getMessage() != null) {
							// if (mListener != null) {
							// mListener.onFailure(e.getMessage());
							// if (mCurrentView != null)
							// removeCurrentView();
							// }
						}
						e.printStackTrace();
					}
					if (mJSONArray != null) {
						for (int i = 0; i < mJSONArray.length(); i++) {
							JSONObject mItem;
							try {
								mItem = mJSONArray.getJSONObject(i);
								byte[] sig = currentCertificateChain[0]
										.getSignature();
								try {
									String text = new String(sig, "UTF-8");
									if (mItem.getString("sigAlgName")
											.equalsIgnoreCase(text)) {
										isAlreadyTrustedByUser = true;
										break;
									}
								} catch (UnsupportedEncodingException e1) {
									e1.printStackTrace();
								}
							} catch (JSONException e) {
								// if (e != null && e.getMessage() != null) {
								// if (mListener != null) {
								// mListener.onFailure(e.getMessage());
								// if (mCurrentView != null)
								// removeCurrentView();
								// }
								// }
								e.printStackTrace();
							}
						}
					}

					if (mUCertificateArray != null) {
						for (int i = 0; i < mUCertificateArray.length(); i++) {
							JSONObject mItem;
							try {
								mItem = mUCertificateArray.getJSONObject(i);
								byte[] sig = currentCertificateChain[0]
										.getSignature();
								try {
									String text = new String(sig, "UTF-8");
									if (mItem.getString("sigAlgName")
											.equalsIgnoreCase(text)) {
										isAlreadyTrusted = true;
										break;
									}
								} catch (UnsupportedEncodingException e1) {
									e1.printStackTrace();
								}
							} catch (JSONException e) {
								// if (e != null && e.getMessage() != null) {
								// if (mListener != null) {
								// mListener.onFailure(e.getMessage());
								// if (mCurrentView != null)
								// removeCurrentView();
								// }
								// }
								e.printStackTrace();
							}
						}
					}

					if (isAlreadyTrustedByUser | isAlreadyTrusted) {
						handler.proceed();
					} else {
						String message = "";
						if (TextUtils.isEmpty(strTrustError)) {
							message = "The server couldn\'t be verified and its security certificate is not trusted. Do you want to continue?";
						} else {
							message = strTrustError;
						}
						String title = "";
						title = "Warning";
						String pButton = "";
						pButton = "Continue";
						new AlertDialog.Builder(PayUIntegration.this)
								.setTitle(title)
								.setMessage(message)
								.setCancelable(false)
								.setPositiveButton(pButton,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
												saveCertificate(
														currentCertificateChain,
														handler);
												// handler.proceed();
											}

										})
								.setNegativeButton(android.R.string.cancel,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
												handler.cancel();
												finishActivity();
											}
										}).create().show();
					}

				} else {
					handler.proceed();
				}

			}
		}

		public void startExecution(SslErrorHandler handler) {
			this.handler = handler;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				execute();
			}
		}

	}

	private void finishActivity() {
		finish();
	}

	private HttpURLConnection checkAuthentication(final String mUrl, String body) {

		final ArrayList<TrustManager> managers = new ArrayList<>();
		try {
			TrustManagerFactory factory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			factory.init((KeyStore) null);
			managers.addAll(Arrays.asList(factory.getTrustManagers()));
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}

		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				for (TrustManager tm : managers) {
					if (tm instanceof X509TrustManager) {
						((X509TrustManager) tm).checkClientTrusted(chain,
								authType);
					}
				}
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] chain,
					String authType) {

				for (X509Certificate cert : chain) {

					final String mCertificatinoType = cert.getType();
					Date afterDate = cert.getNotAfter();
					Date beforeDate = cert.getNotBefore();
					Date currentDate = new Date();

					try {
						cert.checkValidity(new Date());
					} catch (CertificateExpiredException e) {
						isExpired = true;
						e.printStackTrace();
					} catch (CertificateNotYetValidException e) {
						isExpired = true;
						e.printStackTrace();
					}

					if (afterDate.compareTo(currentDate)
							* currentDate.compareTo(beforeDate) > 0) {
						isExpired = false;
					} else {
						isExpired = true;
					}

					String dn = cert.getSubjectDN().getName();
					String CN = getValByAttributeTypeFromIssuerDN(dn, "CN=");

					String host = "";
					if (TextUtils.isEmpty(mUrl)) {

					} else {
						try {
							URL url = new URL(mUrl);
							host = url.getAuthority();
							if (host.contains(String.valueOf(url.getPort()))) {
								String toBeReplaced = ":" + url.getPort();
								host = host.replace(toBeReplaced, "");
							}
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}

					for (TrustManager tm : managers) {
						if (tm instanceof X509TrustManager) {
							try {
								((X509TrustManager) tm).checkServerTrusted(
										chain, authType);
							} catch (CertificateException e) {
								if (e.getMessage() != null
										&& e.getMessage()
												.contains(
														"Trust anchor for certification path not found.")) {
									isNotTrusted = true;
									setCurrentCertificate(chain);

								}
								e.printStackTrace();
							}
						}

					}

					if (isNotTrusted) {
						if (CN.startsWith("*.")) {
							String sunCN = CN.substring(2, CN.length());
							if (host.contains(sunCN)) {
								isHostMisMatch = false;
							} else {
								isHostMisMatch = true;
							}
						} else {
							if (CN.equalsIgnoreCase(host)) {
								isHostMisMatch = false;
							} else {
								isHostMisMatch = true;
							}
						}
					}

				}

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				ArrayList<X509Certificate> issuers = new ArrayList<>();
				for (TrustManager tm : managers) {
					if (tm instanceof X509TrustManager) {
						issuers.addAll(Arrays.asList(((X509TrustManager) tm)
								.getAcceptedIssuers()));
					}
				}
				return issuers.toArray(new X509Certificate[issuers.size()]);
			}

		};

		final HostnameVerifier defaultVerifier = HttpsURLConnection
				.getDefaultHostnameVerifier();
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// exception to accept iux2013-1.gdeinfor2.com
				// server
				return defaultVerifier.verify(hostname, session);
			}
		});

		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { trustManager }, null);
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}

		URL url = null;
		try {
			url = new URL(mUrl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			if (urlConnection instanceof HttpsURLConnection
					&& sslContext != null) {
				((HttpsURLConnection) urlConnection)
						.setSSLSocketFactory(sslContext.getSocketFactory());
			}

			urlConnection.setRequestMethod(HttpMethod.GET.name());
			urlConnection.setConnectTimeout(timeoutConnection);
			urlConnection.setReadTimeout(timeoutRead);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (body != null) {
			byte[] buffer = null;
			try {
				buffer = body.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			urlConnection.setFixedLengthStreamingMode(buffer.length);
			try {
				urlConnection.getOutputStream().write(buffer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return urlConnection;

	}

	public String getValByAttributeTypeFromIssuerDN(String dn,
			String attributeType) {
		String[] dnSplits = dn.split(",");
		for (String dnSplit : dnSplits) {
			if (dnSplit.contains(attributeType)) {
				String[] cnSplits = dnSplit.trim().split("=");
				if (cnSplits[1] != null) {
					return cnSplits[1].trim();
				}
			}
		}
		return "";
	}

	public enum HttpMethod {
		GET, PUT, POST, DELETE;
	}

	private void setCurrentCertificate(X509Certificate[] chain) {
		this.currentCertificateChain = chain;

	}

	public void saveCertificate(X509Certificate[] chain, SslErrorHandler handler) {

		String mArray = StorageClass.getInstance(PayUIntegration.this)
				.getJSONArrayCertificates();
		JSONArray mJSONArray = null;
		try {
			mJSONArray = new JSONArray(mArray);
		} catch (JSONException e) {
			// if (e != null && e.getMessage() != null) {
			// if (mListener != null) {
			// mListener.onFailure(e.getMessage());
			// if (mCurrentView != null)
			// removeCurrentView();
			// }
			// }
			e.printStackTrace();
		}

		try {
			mJSONArray = new JSONArray(mArray);
		} catch (JSONException e) {
			// if (e != null && e.getMessage() != null) {
			// if (mListener != null) {
			// mListener.onFailure(e.getMessage());
			// if (mCurrentView != null)
			// removeCurrentView();
			// }
			// }
			e.printStackTrace();
		}

		JSONObject mObj = new JSONObject();
		try {
			byte[] sig = chain[0].getSignature();
			String text = "";
			try {
				text = new String(sig, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Date afterDate = chain[0].getNotAfter();
			Date beforeDate = chain[0].getNotBefore();
			BigInteger mNim = chain[0].getSerialNumber();
			String mNum = mNim.toString();
			BigInteger bi = new BigInteger(mNum);
			String mSerialNumber = bi.toString(16);

			String subjectName = chain[0].getSubjectDN().getName();
			String issuerName = chain[0].getIssuerDN().getName();
			String mSubjectCommonName = getValByAttributeTypeFromIssuerDN(
					subjectName, "CN=");
			String mIssuerCommonName = getValByAttributeTypeFromIssuerDN(
					issuerName, "CN=");
			mObj.put("sigAlgName", text);
			mObj.put("serialNumber", mSerialNumber);
			mObj.put("afterDate", afterDate.toString());
			mObj.put("issuerName", issuerName);
			mObj.put("subjectName", subjectName);
			mObj.put("subjectCommonName", mSubjectCommonName);
			mObj.put("issuerCommonName", mIssuerCommonName);
			mObj.put("beforeDate", beforeDate.toString());
			try {
				mObj.put("SHA1", getThumbPrint(chain[0]));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (java.security.cert.CertificateEncodingException e) {
				e.printStackTrace();
			}
			try {
				mObj.put("MD5", getMD5(chain[0]));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (java.security.cert.CertificateEncodingException e) {
				e.printStackTrace();
			}
			if (mJSONArray == null)
				mJSONArray = new JSONArray();
			mJSONArray.put(mObj);
		} catch (JSONException e) {
			if (e != null && e.getMessage() != null) {
			}
			e.printStackTrace();
		}

		StorageClass.getInstance(PayUIntegration.this)
				.setJSONArrayCertificates(mJSONArray);

		handler.proceed();
	}

	public static String getThumbPrint(X509Certificate cert)
			throws NoSuchAlgorithmException, CertificateEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] der = null;
		try {
			der = cert.getEncoded();
		} catch (java.security.cert.CertificateEncodingException e) {
			e.printStackTrace();
		}
		md.update(der);
		byte[] digest = md.digest();
		return hexify(digest);

	}

	public static String getMD5(X509Certificate cert)
			throws NoSuchAlgorithmException, CertificateEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] der = null;
		try {
			der = cert.getEncoded();
		} catch (java.security.cert.CertificateEncodingException e) {
			e.printStackTrace();
		}
		md.update(der);
		byte[] digest = md.digest();
		return hexify(digest);

	}

	public static String hexify(byte bytes[]) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; ++i) {
			buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
			buf.append(hexDigits[bytes[i] & 0x0f]);
		}
		return buf.toString();
	}
}
