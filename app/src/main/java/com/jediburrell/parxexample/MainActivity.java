package com.jediburrell.parxexample;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
							"    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
							"    android:layout_width=\"match_parent\"\n" +
							"    android:layout_height=\"match_parent\">\n" +
							"\n" +
							"    <LinearLayout\n" +
							"        android:layout_width=\"match_parent\"\n" +
							"        android:layout_height=\"wrap_content\"\n" +
							"		 android:orientation=\"vertical\">" +
							"\n" +
							"        <Button\n" +
							"            android:id=\"@+id/with\"\n" +
							"            android:layout_width=\"match_parent\"\n" +
							"            android:layout_height=\"wrap_content\"\n" +
							"            android:text=\"Load with Parx\"/>\n" +
							"\n" +
							"        <Button\n" +
							"            android:id=\"@+id/without\"\n" +
							"            android:layout_width=\"match_parent\"\n" +
							"            android:layout_height=\"wrap_content\"\n" +
							"            android:text=\"Load without Parx\"/>\n" +
							"\n" +
							"    </LinearLayout>\n" +
							"\n" +
							"    <ImageView\n" +
							"        android:layout_width=\"wrap_content\"\n" +
							"        android:layout_height=\"50dp\"\n" +
							"        android:src=\"@drawable/parx_load\"\n" +
							"        android:layout_alignParentBottom=\"true\"\n" +
							"        android:layout_centerHorizontal=\"true\"\n" +
							"		 android:padding=\"16dp\"/>" +
							"\n" +
							"</RelativeLayout>\n")
			);

			long timeAfter = System.currentTimeMillis()-time;
			((Button)findViewById(parx.getIds().get("with"))).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MainActivity.this, WithParx.class);
					startActivity(i);
				}
			});
			((Button)findViewById(parx.getIds().get("without"))).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MainActivity.this, WithoutParx.class);
					startActivity(i);
				}
			});
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
	}
}
