package com.chj.tencent.goo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class GooActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// È¥µôtitle
		super.onCreate(savedInstanceState);

		setContentView(new GooView(GooActivity.this));
	}

}
