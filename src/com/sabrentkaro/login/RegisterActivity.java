package com.sabrentkaro.login;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.R;
import com.utils.StaticUtils;

public class RegisterActivity extends BaseActivity {

	private EditText mEditEmail, mEditPassword, mEditConfirmEmail,
			mEditDisplayName, mEditMobileNumber;
	private TextView mbtnLogin;
	private CheckBox mCheckTerms;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_regsiter);
		loadLayoutReferences();
		hideSoftKeyboard();
	}

	private void loadLayoutReferences() {
		mEditEmail = (EditText) findViewById(R.id.emailId);
		mEditConfirmEmail = (EditText) findViewById(R.id.confirmEmailId);
		mEditPassword = (EditText) findViewById(R.id.password);
		mEditDisplayName = (EditText) findViewById(R.id.displayName);
		mEditMobileNumber = (EditText) findViewById(R.id.mobileNumber);
		mCheckTerms = (CheckBox) findViewById(R.id.checkTerms);

		StaticUtils.setEditTextHintFont(mEditEmail, this);
		StaticUtils.setEditTextHintFont(mEditConfirmEmail, this);
		StaticUtils.setEditTextHintFont(mEditPassword, this);
		StaticUtils.setEditTextHintFont(mEditDisplayName, this);
		StaticUtils.setEditTextHintFont(mEditMobileNumber, this);
		StaticUtils.setCheckBoxFont(mCheckTerms, this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

}
