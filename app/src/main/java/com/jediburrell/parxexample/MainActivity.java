package com.jediburrell.parxexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jediburrell.parx.Parx;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Parx parx = new Parx(this);
		ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
		try {
			long time = System.currentTimeMillis();
			viewGroup.addView(

					parx.parx("<RelativeLayout\n" +
							"	 xmlns:android=\"http://schemas.android.com/apk/res/android\"" +
							"    android:layout_width=\"match_parent\"\n" +
							"    android:layout_height=\"match_parent\">\n" +
							"\n" +
							"    <TextView\n" +
							"		 android:id=\"@+id/time\"" +
							"        android:layout_width=\"wrap_content\"\n" +
							"        android:layout_height=\"wrap_content\"\n" +
							"		 android:centerInParent=\"true\"" +
							"        android:text=\"Placeholder!\"\n/>" +
							"\n" +
							"</RelativeLayout>\n")
			);
			long timeAfter = System.currentTimeMillis()-time;
			((TextView)findViewById(parx.getIds().get("time"))).setText(timeAfter+"ms");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
