package com.sabrentkaro.postad;

import android.os.Bundle;

import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.R;

public class PostAdActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.post_ad_activity);
	}
}
