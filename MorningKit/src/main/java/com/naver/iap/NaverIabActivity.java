package com.naver.iap;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.nhn.android.appstore.iap.payment.NIAPActivity;
import com.nhn.android.appstore.iap.result.NIAPResult;
import com.nhn.android.appstore.iap.util.AppstoreSecurity;
import com.yooiistudios.morningkit.common.log.MNLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * activity for using naver appstore in-app purchase
 * @author NAVER
 */
public class NaverIabActivity extends NIAPActivity {
	public static final String KEY_ACTION = "KEY_ACTION";
	public static final String KEY_PRODUCT_KEY = "KEY_PRODUCT_KEY";
	public static final String KEY_PRODUCT_LIST = "KEY_PRODUCT_LIST";
	public static final String ACTION_PURCHASE = "ACTION_PURCHASE";
	public static final String ACTION_QUERY_PURCHASE = "ACTION_QUERY_PURCHASE";
	
	private static final String TAG = "NaverIabActivity";
	private static final String APP_CODE = "FVZZ376761401774425141";
	private static final String IAP_KEY_TEST = "dktHVjkNr5";
	private static final String IAP_KEY_RELEASE = "j2Q6gOjsCr";
	
	private String mPaymentSeq;
	private ArrayList<NaverIabInventoryItem> mInventoryList;
	private ProgressDialog mDialog;
	private String mAction;
	private String mProductKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);

//		mDialog = ProgressDialog.show(this, "", getString(R.string.common_loading), true);
//		DialogManager.getInstance().putDialogInQueue(this, mDialog);
		
		mPaymentSeq = null;

		/**
		 * naver appstore iap initialization
		 */
        initialize(APP_CODE, IAP_KEY_TEST);
//		initialize(APP_CODE, IAP_KEY_RELEASE);
		
		mAction = getIntent().getStringExtra(KEY_ACTION);
		mProductKey = getIntent().getStringExtra(KEY_PRODUCT_KEY);
		
		if (mAction == null) {
			throw new IllegalArgumentException("action not specified.");
		}
		else if (mAction.equals(ACTION_PURCHASE) && mProductKey == null) {
			finishWithErrorMessage("error occurred while purchasing item");
			return;
//			throw new IllegalArgumentException("product key not specified.");
		}

		ArrayList<String> productCodes = new ArrayList<String>();
		for (NaverIabProductInfo product : NaverIabHelper.getInstance(this).getProductList()) {
			productCodes.add(product.getKey());
		}
		/**
		 * request list of product details
		 * parameter : product code list
		 */
		requestProductInfos(productCodes);
		
		
		//purchase
//		requestPayment("1000007243", 100, "extra value");
		
		//check
//		ArrayList<String> productCodes = new ArrayList<String>();
//		productCodes.add("1000007243");
//		requestProductInfos(productCodes);
	}
	
	private void finishWithErrorMessage(String message) {
		if (message != null) {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
		setResult(RESULT_CANCELED);
		finish();
	}
	private void finishGracefully() {
		setResult(RESULT_OK, getIntent());
		finish();
	}

	@Override
	public void onPaymentCompleted(NIAPResult result) {
		printResult("call onPaymentCompleted", result);
		if (verifySignature(result) && mPaymentSeq != null) {
			String paymentSeq = null;
			try {
				JSONObject resultObj = new JSONObject(result.getResult());
				paymentSeq = resultObj.getJSONObject("receipt").getString("paymentSeq");

				if (paymentSeq.equalsIgnoreCase(mPaymentSeq)) {
					finishGracefully();
				}
				else {
					finishWithErrorMessage("error occurred while purchasing item");
				}
			} catch(JSONException e) {
				e.printStackTrace();
				finishWithErrorMessage("error occurred while purchasing item");
			}
		} else {
			finishWithErrorMessage("error occurred while purchasing item");
		}
	}

	@Override
	public void onReceivedProductInfos(NIAPResult result) {
		printResult("call onReceivedProductInfos", result);
		try {
			JSONObject resultObj = new JSONObject(result.getResult());
			JSONArray validArr = resultObj.getJSONArray("valid");
			
			mInventoryList = new ArrayList<NaverIabInventoryItem>();
			for (int i = 0; i < validArr.length(); i++) {
				JSONObject product = validArr.getJSONObject(i);
				String code = product.getString("productCode");
				String price = product.getString("productPrice");
				
				mInventoryList.add(new NaverIabInventoryItem(code, price));
			}

			if (mAction.equals(ACTION_PURCHASE)) {
				String price = "-1";
				for (NaverIabInventoryItem item : mInventoryList) {
					if (item.getKey().equalsIgnoreCase(mProductKey)) {
						price = item.getPrice();
						break;
					}
				}
				requestPayment(mProductKey, Integer.parseInt(price), "extra value");
			}
			else if (mAction.equals(ACTION_QUERY_PURCHASE)) {
				requestAllProductLicenses();
			}
		} catch (Exception e) {
			e.printStackTrace();
			finishWithErrorMessage("error occured while getting product list.");
		}
	}

	@Override
	public void onReceivedPaymentSeq(NIAPResult result) {
		printResult("call onReceivedPaymentSeq", result);
		
		String res = result.getResult();
		try {
			JSONObject resultJSON = new JSONObject(res);

			mPaymentSeq = resultJSON.getString("paymentSeq");
		} catch (Exception e) {
			e.printStackTrace();
			finishWithErrorMessage("error occurred while communicating with" +
					" server");
		}
	}

	//useless
	@Override
	public void onReceivedReceipt(NIAPResult result) {
		printResult("call onReceivedReceipt", result);
		verifySignature(result);
	}

	@Override
	public void onPaymentCanceled(final NIAPResult result) {
		printResult("call onPaymentCanceled", result);
		finishWithErrorMessage(null);
	}
	
	@Override
	public void onReceivedLicenses(NIAPResult result) {
		printResult("call onReceivedLicenses", result);
		if (verifySignature(result)) {
			String resultStr = result.getResult();
			try {
				JSONObject resultObj = new JSONObject(resultStr);
				
				JSONArray licenseList =
						new JSONArray(resultObj.getString("licenses"));

//				ArrayList<String> list = new ArrayList<String>();
				for (int i = 0; i < licenseList.length(); i++) {
					JSONObject licenseObj = licenseList.getJSONObject(i);
					
					if (licenseObj.getString("licenseStatusType").equalsIgnoreCase("APPROVED")) {
//						list.add(NaverIabHelper.getInstance(this).getProduct(licenseObj.getString("productCode")));
						NaverIabInventoryItem item;
						String key = licenseObj.getString("productCode");
						for (NaverIabInventoryItem invItem : mInventoryList) {
							if (invItem.getKey().equalsIgnoreCase(key)) {
								invItem.setIsAvailable(true);
								break;
							}
						}
//						list.add(licenseObj.getString("productCode"));
					}
				}
				getIntent().putParcelableArrayListExtra(KEY_PRODUCT_LIST, mInventoryList);
//				getIntent().putStringArrayListExtra(KEY_PRODUCT_LIST, list);
				
				finishGracefully();
			} catch (JSONException e) {
				e.printStackTrace();
				finishWithErrorMessage("error occurred while communicating with"
										+ " server");
			}
		} else {
			finishWithErrorMessage("error occurred while communicating with" +
					" server");
		}
	}

	// signature verification
	private boolean verifySignature(NIAPResult result) {
		String signedData = result.getResult();
		JSONObject jsonExtraValue;
		String signature;
		try {
			jsonExtraValue = new JSONObject(result.getExtraValue());
			signature = jsonExtraValue.getString("signature");
		} catch (JSONException e) {
			MNLog.e(TAG, "error has occred while parsing json!");
			return false;
		}
		
		if (TextUtils.isEmpty(signature)) {
            MNLog.e(TAG, "empty signature data");
			return false;
		}
		
		return AppstoreSecurity.verify(NaverIabHelper.getInstance(this)._getKey(), signedData, signature);
		
//		if (isValidSignature) {
//			Toast.makeText(NaverIabActivity.this, "signature verifying success", Toast.LENGTH_SHORT).show();
//		} else {
//			Toast.makeText(NaverIabActivity.this, "signature verifying failure", Toast.LENGTH_SHORT).show();
//		}
	}

	@Override
	public void onError(final NIAPResult result) {
		printErrorResult(result);
		
		setResult(RESULT_CANCELED);
		finish();
//		finishWithErrorMessage("error occurred while purchasing item");
	}

//	/**
//	 * setting app layout
//	 */
//	private void settingView() {
//		Button getProductInfosButton = (Button)findViewById(R.id.btProductInfos);
//		Button getReceiptButton = (Button)findViewById(R.id.btReceipt);
//		Button paymentButton = (Button)findViewById(R.id.btPayment);
//		Button getAllLicensesButton = (Button)findViewById(R.id.btAllLicenses);
//		Button getLicensesButton = (Button)findViewById(R.id.btLicenses);
//		Button getLicensesHistoryButton = (Button)findViewById(R.id.btLicensesHistory);
//		Button getLocalStoredPurchasesButton = (Button)findViewById(R.id.btLocalStoredPurchases);
//
//		getProductInfosButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ArrayList<String> productCodes = new ArrayList<String>();
//				productCodes.add("1000007243");
////				productCodes.add("1000002568");
//				/**
//				 * request list of product details
//				 * parameter : product code list
//				 */
//				requestProductInfos(productCodes);
//			}
//		});
//
//		getReceiptButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				/**
//				 * request payment receipt details
//				 * parameter : payment sequence of purchased product
//				 */
//				requestReceipt("1000007243");
//			}
//		});
//
//		paymentButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				/**
//				 * request in-app purchase
//				 * parameters : product code, product price, specified extra data
//				 */
//				requestPayment("1000007243", 100, "extra value");
//			}
//		});
//		
//		getAllLicensesButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				/**
//				 * user's licenses information of all permanence and period products
//				 */
//				requestAllProductLicenses();
//			}
//		});
//		
//		getLicensesButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				/**
//				 * user's license information of permanence and period products
//				 * parameter : product codes to find details
//				 */
//				ArrayList<String> productCodes = new ArrayList<String>();
//				productCodes.add("1000007243");
//				requestProductLicenses(productCodes);
//			}
//		});
//		
//		getLicensesHistoryButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				/**
//				 * all licenses history of permanence and period products
//				 */
//				requestProductLicensesHistory();
//			}
//		});
//		
//		getLocalStoredPurchasesButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			purchasesHelper.getLocalStoredPurchases(new LocalStoredPurchasesListener() {
//				@Override
//				public void onRetrivePurchasedItem(List<PurchasedItem> purchasedItemList) {
//					if (purchasedItemList.size() == 0) {
//						Toast.makeText(PaymentActivity.this, "there is no stored purchase details in local storage", Toast.LENGTH_SHORT).show();
//						return;
//					} else {
//						Toast.makeText(PaymentActivity.this, "total count : " + purchasedItemList.size(), Toast.LENGTH_SHORT).show();
//						Log.i(TAG, "details : " + purchasedItemList);
//						for (PurchasedItem purchasedItem : purchasedItemList) {
//							boolean isSuccessSigning = AppstoreSecurity.verify(PUBLIC_KEY, purchasedItem.getPurchaseResult(), purchasedItem.getSignature());  
//							Toast.makeText(PaymentActivity.this, "payment sequence : " + purchasedItem.getPaymentSeq() + ", signing success : " + isSuccessSigning, Toast.LENGTH_SHORT).show();
//							// TODO : write codes to check that items were not given out to the user.
//							// purchasedItem.removeFromLocal();	// remove from local storage
//						}
//					}
//				}
//
//				@Override
//				public void onError(String code, String message) {
//					Toast.makeText(PaymentActivity.this, "Error!! code : " + code + ", message : " + message, Toast.LENGTH_SHORT).show();
//				}
//			});
//			}
//		});
//	}

	/**
	 * print success result to logcat console and Toast message
	 * @param message 메시지
	 * @param result 반환값
	 */
	private void printResult(final String message, final NIAPResult result) {
		if (result != null) {
			String resultMessage = "[ " + message + " ]";
			String requestDesc = "Request Type : " + result.getRequestType().getDesc();
			String resultDetail = "Result Detail : " + result.getResult();
			String resultExtraValue = "Extra Value : " + result.getExtraValue();
			
			MNLog.i(TAG, resultMessage + "\n" + requestDesc + "\n" + resultDetail + "\n" + resultExtraValue);
//			if (ConfigurationManager.SignatureConfig.getInstance(this).isDebugBuild()) {
//            if (MNLog.isDebug) {
//                Toast.makeText(NaverIabActivity.this, resultMessage + "\n" + requestDesc + "\n" + resultDetail + "\n" + resultExtraValue, Toast.LENGTH_SHORT).show();
//			}
		}
	}

	/**
	 * print error result to logcat console and Toast message
	 * @param result 반환되는 값
	 */
	private void printErrorResult(final NIAPResult result) {
		String requestDesc = "";
		String errorCode = "";
		String errorMessage = "";
		if (result != null) {
			requestDesc = result.getRequestType().getDesc();
			try {
				JSONObject resultJson = new JSONObject(result.getResult());
				errorCode = resultJson.getString("code");
				errorMessage = resultJson.getString("message");
			} catch (Exception e) {
				errorCode = "ERS999";
				errorMessage = "unknown error has occured";
			}
//			if (ConfigurationManager.SignatureConfig.getInstance(this).isDebugBuild()) {
//				Toast.makeText(NaverIabActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//			}
		}
		MNLog.i(TAG, "Request Type : " + requestDesc + ",\nError Code : " + errorCode + ",\nError Description " + errorMessage);
	}
	
	@Override
	public void finish() {
		super.finish();

		overridePendingTransition(0, 0);
//		DialogManager.getInstance().dismissDialog(this, mDialog);
	}
}
