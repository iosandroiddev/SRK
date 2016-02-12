package com.sabrentkaro.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.R;
import com.utils.StaticUtils;

public class LoginActivity extends BaseActivity {

	private EditText mEditEmail, mEditPassword;
	private TextView mbtnLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_login);
		loadLayoutReferences();
		hideSoftKeyboard();
	}

	private void loadLayoutReferences() {
		mEditEmail = (EditText) findViewById(R.id.emailId);
		mEditPassword = (EditText) findViewById(R.id.password);
		mbtnLogin = (TextView) findViewById(R.id.btnLoginUser);

		StaticUtils.setEditTextHintFont(mEditEmail, this);
		StaticUtils.setEditTextHintFont(mEditPassword, this);

		mbtnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnLoginUser:
			btnLoginUserClicked();
			break;

		default:
			break;
		}
	}

	private void btnLoginUserClicked() {
		hideSoftKeyboard();
	}

}
