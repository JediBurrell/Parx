package com.jediburrell.parx;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Created by JediB on 3/26/2017.
 */

public class Parx {

	String LOG_TAG = "Parx";

	Context ctx;
	LinkedList<View> views = new LinkedList<View>();
	Map<String, Integer> ids = new HashMap<String, Integer>();

	public Parx(Context ctx){
		this.ctx = ctx;
	}

	public Map<String, Integer> getIds(){

		return ids;
	}

	public View parx(String xml) throws XmlPullParserException, IOException {

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		xpp.setInput( new StringReader( xml ) );
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			xpp.getPrefix();
			if(eventType == XmlPullParser.START_DOCUMENT) {
				Log.d(LOG_TAG, "Parsing document");
			} else if(eventType == XmlPullParser.START_TAG) {
				Log.d(LOG_TAG, "Tag opened: " + xpp.getName());
				View v = parseTag(xpp);
				if(v!=null) {
					if (!views.isEmpty())
						((ViewGroup) views.getLast()).addView(v);
					views.add(v);
				}
			} else if(eventType == XmlPullParser.END_TAG) {
				if(views.size()>1)
					views.removeLast();
				Log.d(LOG_TAG,"Tag closed "+xpp.getName());
			}
			eventType = xpp.next();
		}
		System.out.println("End document");

		return views.getFirst();

	}

	private View parseTag(XmlPullParser xpp){

		String tag = xpp.getName();

		switch(tag){

			case "LinearLayout": {
				LinearLayout v = new LinearLayout(ctx);
				v = (LinearLayout) parseAttributes(v, xpp);
				return v;
			}
			case "RelativeLayout": {
				RelativeLayout v = new RelativeLayout(ctx);
				v = (RelativeLayout) parseAttributes(v, xpp);
				return v;
			}
			case "ImageView": {
				ImageView v = new ImageView(ctx);
				v = (ImageView) parseAttributes(v, xpp);
				return v;
			}
			case "TextView": {
				TextView v = new TextView(ctx);
				v = (TextView) parseAttributes(v, xpp);
				return v;
			}

		}

		return new View(ctx);

	}

	// Turn back now!
	private View parseAttributes(View v, XmlPullParser xpp){

		String a = ""+xpp.getAttributeValue(null, "id");

		if(a.length()>0&&!a.equals("null")){
			if(a.startsWith("@+id/")){
				v.setId(new Random().nextInt(999999)+ids.size());
				ids.put(a.replace("@+id/", ""), v.getId());
			}
		}

		a = ""+xpp.getAttributeValue(null, "background");
		Log.d(LOG_TAG, "background:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("@drawable/")){
				int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
						null, ctx.getPackageName());

				v.setBackground(ctx.getDrawable(drawable));
			}else if(a.contains("@color/")){
				int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
						null, ctx.getPackageName());

				v.setBackgroundColor(ctx.getResources().getColor(drawable));
			}else if(a.contains("#")){
				v.setBackgroundColor(Color.parseColor(a));
			}
		}

		a = ""+xpp.getAttributeValue(null, "src");
		Log.d(LOG_TAG, "src:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("@drawable/")){
				int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
						null, ctx.getPackageName());

				((ImageView)v).setImageDrawable(ctx.getDrawable(drawable));
			}else if(a.contains("//")){
				((ImageView)v).setImageURI(Uri.parse(a));
			}
		}

		a = ""+xpp.getAttributeValue(null, "layout_height");
		Log.d(LOG_TAG, "layout_height:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a=="match_parent"||a=="fill_parent"){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				newLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
				v.setLayoutParams(newLayoutParams);
			}else if(a=="wrap_content"){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				newLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
				v.setLayoutParams(newLayoutParams);
			}else if(a.endsWith("px")){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				ViewGroup.LayoutParams newerLP = new ViewGroup.LayoutParams(newLayoutParams.width,
						Integer.parseInt(a.replace("px", "")));
				v.setLayoutParams(newerLP);
			}else if(a.endsWith("dp")){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				ViewGroup.LayoutParams newerLP = new ViewGroup.LayoutParams(newLayoutParams.width,
						(int) (Integer.parseInt(a.replace("dp", "")) *
								Resources.getSystem().getDisplayMetrics().density));
				v.setLayoutParams(newerLP);
			}
		}

		a = ""+xpp.getAttributeValue(null, "layout_width");
		Log.d(LOG_TAG, "layout_width:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a=="match_parent"||a=="fill_parent"){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				newLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
				v.setLayoutParams(newLayoutParams);
			}else if(a=="wrap_content"){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				newLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
				v.setLayoutParams(newLayoutParams);
			}else if(a.endsWith("px")){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				ViewGroup.LayoutParams newerLP = new ViewGroup.LayoutParams(Integer.parseInt(a.replace("px", "")),
						newLayoutParams.height);
				v.setLayoutParams(newerLP);
			}else if(a.endsWith("dp")){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				ViewGroup.LayoutParams newerLP = new ViewGroup.LayoutParams(
						(int) (Integer.parseInt(a.replace("dp", "")) *
								Resources.getSystem().getDisplayMetrics().density), newLayoutParams.height);
				v.setLayoutParams(newerLP);
			}
		}

		a = ""+xpp.getAttributeValue(null, "text");
		Log.d(LOG_TAG, "text:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("@string/")){
				int string = ctx.getResources().getIdentifier(a.replace("@", ""),
						null, ctx.getPackageName());

				((TextView)v).setText(ctx.getResources().getString(string));
			}else{
				((TextView)v).setText(a);
			}
		}

		a = ""+xpp.getAttributeValue(null, "textSize");
		Log.d(LOG_TAG, "textSize:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("sp")){
				((TextView)v).setTextSize(Integer.parseInt(a.replace("sp", ""))*
						Resources.getSystem().getDisplayMetrics().scaledDensity);
			}else if(a.contains("dp")){
				((TextView)v).setTextSize(Integer.parseInt(a.replace("dp", ""))*
						Resources.getSystem().getDisplayMetrics().density);
			}else if(a.contains("px")){
				((TextView)v).setTextSize(Integer.parseInt(a.replace("px", "")));
			}
		}

		a = ""+xpp.getAttributeValue(null, "alignParentStart");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_START, true);

		a = ""+xpp.getAttributeValue(null, "alignParentEnd");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_END, true);

		a = ""+xpp.getAttributeValue(null, "alignParentBottom");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_BOTTOM, true);

		a = ""+xpp.getAttributeValue(null, "alignParentLeft");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_LEFT, true);

		a = ""+xpp.getAttributeValue(null, "alignParentRight");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_RIGHT, true);

		a = ""+xpp.getAttributeValue(null, "alignParentTop");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_TOP, true);

		a = ""+xpp.getAttributeValue(null, "centerInParent");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.CENTER_IN_PARENT, true);

		a = ""+xpp.getAttributeValue(null, "centerHorizontal");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.CENTER_HORIZONTAL, true);

		a = ""+xpp.getAttributeValue(null, "centerVertical");
		if(a=="true") addOrRemoveProperty(v, RelativeLayout.CENTER_VERTICAL, true);

		return v;
	}

	// Thanks Hiren Patel
	private void addOrRemoveProperty(View view, int property, boolean flag){
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		if(flag){
			layoutParams.addRule(property);
		}else {
			layoutParams.removeRule(property);
		}
		view.setLayoutParams(layoutParams);
	}

}
