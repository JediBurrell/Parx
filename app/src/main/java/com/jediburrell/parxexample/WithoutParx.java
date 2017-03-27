package com.jediburrell.parxexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WithoutParx extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		long time = System.currentTimeMillis();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_without_parx);

		TextView textView = (TextView) findViewById(R.id.time);

		textView.setText(System.currentTimeMillis()-time+"ms");
	}
}
