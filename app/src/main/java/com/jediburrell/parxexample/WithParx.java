package com.jediburrell.parxexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jediburrell.parx.Parx;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WithParx extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parx parx = new Parx(this);
		try {
			long time = System.currentTimeMillis();
			setContentView(

					parx.parx("<RelativeLayout\n" +
							"	 xmlns:android=\"http://schemas.android.com/apk/res/android\"" +
							"    android:layout_width=\"match_parent\"\n" +
							"    android:layout_height=\"match_parent\">\n" +
							"\n" +
							"    <TextView\n" +
							"		 android:id=\"@+id/time\"" +
							"        android:layout_width=\"wrap_content\"\n" +
							"        android:layout_height=\"wrap_content\"\n" +
							"		 android:layout_centerInParent=\"true\"" +
							"        android:text=\"Placeholder!\"\n/>" +
							"\n" +
							"<ImageView" +
							"        android:layout_width=\"wrap_content\"\n" +
							"        android:layout_height=\"50dp\"\n" +
							"        android:src=\"@drawable/parx_load\"\n" +
							"        android:layout_alignParentBottom=\"true\"\n" +
							"        android:layout_centerHorizontal=\"true\"\n" +
							"		 android:padding=\"16dp\"/>" +
							"</RelativeLayout>\n")
			);
			long timeAfter = System.currentTimeMillis()-time;
			((TextView)findViewById(parx.getIds().get("time"))).setText(timeAfter+"ms");
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
	}
}
