package com.sabrentkaro.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.HomeActivity;
import com.sabrentkaro.R;

public class InvoiceDetailsActivity extends BaseActivity {

	private String orderIdVal, invoiceAmountVal, rentalStartDateVal,
			rentalEndDateVal, invoiceQuantityVal, invoicePhoneVal,
			invoiceBrandVal, invoiceCategoryVal, billingAddressVal;
	private String faciliationCost = "", serviceCost = "", logisticsCost = "";
	private TextView orderId, invoiceAmount, rentalStartDate, rentalEndDate,
			rentalPeriod, invoiceQuantity, invoicePhone, invoiceBrand,
			invoiceCategory, billingAddress, doneBtn, mtxtLogisticCost,
			mtxtServiceCost, mtxtFacCost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_invoice_details);
		getDetails();
		loadLayoutReferences();
		loadDetails();
	}

	private void loadLayoutReferences() {
		orderId = (TextView) findViewById(R.id.invoiceOrderId);
		invoiceAmount = (TextView) findViewById(R.id.invoiceAmount);
		rentalStartDate = (TextView) findViewById(R.id.invoiceRentalStartDate);
		rentalEndDate = (TextView) findViewById(R.id.invoiceRentalEndDate);
		rentalPeriod = (TextView) findViewById(R.id.invoiceRentalPeriod);
		invoiceQuantity = (TextView) findViewById(R.id.invoiceQuantity);
		invoicePhone = (TextView) findViewById(R.id.invoicePhone);
		invoiceBrand = (TextView) findViewById(R.id.invoiceBrand);
		invoiceCategory = (TextView) findViewById(R.id.invoiceProductCategory);
		billingAddress = (TextView) findViewById(R.id.invoiceBillingAddress);
		doneBtn = (TextView) findViewById(R.id.btnDone);
		mtxtFacCost = (TextView) findViewById(R.id.invoiceFacilitaion);
		mtxtLogisticCost = (TextView) findViewById(R.id.invoiceLogistics);
		mtxtServiceCost = (TextView) findViewById(R.id.invoiceServiceTax);

		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				navigateToHome();
			}
		});
	}

	private void loadDetails() {
		Long rentalDays = 0L;
		int days = 0;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy");
			Date startDate = mSimpleDateFormat.parse(rentalStartDateVal);
			Date endDate = mSimpleDateFormat.parse(rentalEndDateVal);
			rentalDays = endDate.getTime() - startDate.getTime();
			days = (int) (rentalDays / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		rentalPeriod.setText(String.valueOf(days + 1));
		orderId.setText(orderIdVal);
		invoiceAmount.setText(invoiceAmountVal);
		rentalStartDate.setText(rentalStartDateVal);
		rentalEndDate.setText(rentalEndDateVal);
		invoiceQuantity.setText(invoiceQuantityVal);
		invoiceBrand.setText(invoiceBrandVal);
		invoiceCategory.setText(invoiceCategoryVal);
		billingAddress.setText(billingAddressVal);
		invoicePhone.setText(invoicePhoneVal);

		mtxtServiceCost.setText(serviceCost);
		mtxtLogisticCost.setText(logisticsCost);
		mtxtFacCost.setText(faciliationCost);

	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			if (mBundle != null) {
				orderIdVal = mBundle.getString("orderId");
				invoiceAmountVal = mBundle.getString("amount");
				rentalStartDateVal = mBundle.getString("startDate");
				rentalEndDateVal = mBundle.getString("endDate");
				invoiceQuantityVal = mBundle.getString("quantity");
				invoicePhoneVal = mBundle.getString("invoicePhone");
				invoiceBrandVal = mBundle.getString("productDescription");
				invoiceCategoryVal = mBundle.getString("productDescription");
				billingAddressVal = mBundle.getString("address");
				faciliationCost = mBundle.getString("facilitationCost");
				serviceCost = mBundle.getString("serviceTax");
				logisticsCost = mBundle.getString("logisticsCost");
			}
		}
	}

	private void navigateToHome() {
		Intent mIntent = new Intent(this, HomeActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(mIntent);
		finish();
	}

}
