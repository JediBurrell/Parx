package com.jediburrell.parx;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Created by JediB on 3/26/2017.
 */

public class Parx {

	private String LOG_TAG = "Parx";

	private Context ctx;
	private LinkedList<View> views = new LinkedList<View>();
	private Map<String, Integer> ids = new HashMap<String, Integer>();

	private ImageLoader customImageLoader = null;

	private boolean logging = true;

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
				if(logging) Log.d(LOG_TAG, "Parsing document");
			} else if(eventType == XmlPullParser.START_TAG) {
				if(logging) Log.d(LOG_TAG, "Tag opened: " + xpp.getName());
				View v = null;
				try { v = parseTag(xpp); }
				catch (Exception e)
					{ e.printStackTrace(); }

				if(v!=null) {
					if (!views.isEmpty())
						((ViewGroup) views.getLast()).addView(v.getRootView());
					views.add(v);
				}
			} else if(eventType == XmlPullParser.END_TAG) {
				if(views.size()>1)
					views.removeLast();
				if(logging) Log.d(LOG_TAG,"Tag closed "+xpp.getName());
			}
			eventType = xpp.next();
		}
		System.out.println("End document");

		return views.getFirst();

	}

	@SuppressWarnings("unused")
	public void setCustomImageLoader(ImageLoader imageLoader){
		this.customImageLoader = imageLoader;
	}

	private View parseTag(XmlPullParser xpp) throws Exception {

		String tag = xpp.getName();

		Class classType = View.class;
		switch(tag){
			case "ScrollView": classType = ScrollView.class; break;
			case "LinearLayout": classType = LinearLayout.class; break;
			case "RelativeLayout": classType = RelativeLayout.class; break;
			case "ImageView": classType = ImageView.class; break;
			case "TextView": classType = TextView.class; break;
			case "Button": classType = Button.class; break;
		}

		View v = (View) classType.getConstructor(Context.class).newInstance(ctx);
		return parseAttributes(View.class.cast(v), xpp);
	}

	// Turn back now!
	private View parseAttributes(View v, XmlPullParser xpp){

		int WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT;
		int HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;

		String a = ""+xpp.getAttributeValue(null, "id");

		if(a.length()>0&&!a.equals("null")){
			if(a.startsWith("@+id/")){
				v.setId(new Random().nextInt(999999)+ids.size());
				ids.put(a.replace("@+id/", ""), v.getId());
			}
		}

		a = ""+xpp.getAttributeValue(null, "background");
		if(logging) Log.d(LOG_TAG, "background:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("@drawable/")){
				int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
						"drawable", ctx.getPackageName());

				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
					v.setBackground(ctx.getResources().getDrawable(drawable));
				else
					v.setBackgroundDrawable(ctx.getResources().getDrawable(drawable));
			}else if(a.contains("@color/")){
				int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
						"color", ctx.getPackageName());

				v.setBackgroundColor(ctx.getResources().getColor(drawable));
			}else if(a.contains("#")){
				v.setBackgroundColor(Color.parseColor(a));
			}
		}

		a = ""+xpp.getAttributeValue(null, "src");
		if(logging) Log.d(LOG_TAG, "src:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("@drawable/")){
				int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
						"drawable", ctx.getPackageName());

				((ImageView)v).setImageDrawable(ctx.getResources().getDrawable(drawable));
			}else if(a.contains("//")){
				if(customImageLoader==null)
					((ImageView)v).setImageURI(Uri.parse(a));
				else
					customImageLoader.onUri(a, v);
			}
		}

		a = ""+xpp.getAttributeValue(null, "layout_width");
		if(logging) Log.d(LOG_TAG, "layout_width:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.equals("match_parent")||a.equals("fill_parent")){
				WIDTH = ViewGroup.LayoutParams.MATCH_PARENT;
			}else if(a.equals("wrap_content")){
				WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT;
			}else if(a.endsWith("px")){
				ViewGroup.LayoutParams newLayoutParams = v.getLayoutParams();
				WIDTH = Integer.parseInt(a.replace("px", ""));
			}else if(a.endsWith("dp")){
				WIDTH = (int) (Integer.parseInt(a.replace("dp", "")) *
								Resources.getSystem().getDisplayMetrics().density);
			}
			if(logging) Log.d(LOG_TAG, "WIDTH SET TO: " + WIDTH);
		}

		a = ""+xpp.getAttributeValue(null, "layout_height");
		if(logging) Log.d(LOG_TAG, "layout_height:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.equals("match_parent")||a.equals("fill_parent")){
				HEIGHT = ViewGroup.LayoutParams.MATCH_PARENT;
			}else if(a.equals("wrap_content")){
				HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;
			}else if(a.endsWith("px")){
				HEIGHT = Integer.parseInt(a.replace("px", ""));
			}else if(a.endsWith("dp")){
				HEIGHT = (int) (Integer.parseInt(a.replace("dp", "")) *
						Resources.getSystem().getDisplayMetrics().density);
			}
		}

		v.setLayoutParams(new ViewGroup.LayoutParams(WIDTH, HEIGHT));

		a = ""+xpp.getAttributeValue(null, "padding");
		if(logging) Log.d(LOG_TAG, "padding:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.endsWith("px")){
				v.setPadding(Integer.parseInt(a.replace("px", "")),
						Integer.parseInt(a.replace("px", "")),
						Integer.parseInt(a.replace("px", "")),
						Integer.parseInt(a.replace("px", ""))
						);
			}else if(a.endsWith("dp")){
				v.setPadding((int) (Integer.parseInt(a.replace("dp", "")) * Resources.getSystem().getDisplayMetrics().density),
						(int) (Integer.parseInt(a.replace("dp", ""))
								* Resources.getSystem().getDisplayMetrics().density),
						(int) (Integer.parseInt(a.replace("dp", ""))
								* Resources.getSystem().getDisplayMetrics().density),
						(int) (Integer.parseInt(a.replace("dp", ""))
								* Resources.getSystem().getDisplayMetrics().density)
				);
			}
		}

		a = ""+xpp.getAttributeValue(null, "text");
		if(logging) Log.d(LOG_TAG, "text:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("@string/")){
				int string = ctx.getResources().getIdentifier(a.replace("@", ""),
						"string", ctx.getPackageName());

				((TextView)v).setText(ctx.getResources().getString(string));
			}else{
				((TextView)v).setText(a);
			}
		}

		a = ""+xpp.getAttributeValue(null, "textSize");
		if(logging) Log.d(LOG_TAG, "textSize:"+a);

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

		a = ""+xpp.getAttributeValue(null, "textColor");
		if(logging) Log.d(LOG_TAG, "textColor:"+a);

		if(a.length()>0&&!a.equals("null")){
			if(a.contains("@color/")){
				int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
						"color", ctx.getPackageName());

				((TextView)v).setTextColor(ctx.getResources().getColor(drawable));
			}else if(a.contains("#")){
				((TextView)v).setTextColor(Color.parseColor(a));
			}
		}

		a = ""+xpp.getAttributeValue(null, "alignParentStart");
		if(logging) Log.d(LOG_TAG, "alignParentStart:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_START, true);

		a = ""+xpp.getAttributeValue(null, "layout_alignParentEnd");
		if(logging) Log.d(LOG_TAG, "alignParentEnd:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_END, true);

		a = ""+xpp.getAttributeValue(null, "layout_alignParentBottom");
		if(logging) Log.d(LOG_TAG, "alignParentBottom:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_BOTTOM, true);

		a = ""+xpp.getAttributeValue(null, "layout_alignParentLeft");
		if(logging) Log.d(LOG_TAG, "alignParentLeft:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_LEFT, true);

		a = ""+xpp.getAttributeValue(null, "layout_alignParentRight");
		if(logging) Log.d(LOG_TAG, "alignParentRight:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_RIGHT, true);

		a = ""+xpp.getAttributeValue(null, "layout_alignParentTop");
		if(logging) Log.d(LOG_TAG, "alignParentTop:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.ALIGN_PARENT_TOP, true);

		a = ""+xpp.getAttributeValue(null, "layout_centerInParent");
		if(logging) Log.d(LOG_TAG, "centerInParent:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.CENTER_IN_PARENT, true);

		a = ""+xpp.getAttributeValue(null, "layout_centerHorizontal");
		if(logging) Log.d(LOG_TAG, "centerHorizontal:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.CENTER_HORIZONTAL, true);

		a = ""+xpp.getAttributeValue(null, "layout_centerVertical");
		if(logging) Log.d(LOG_TAG, "centerVertical:"+a);
		if(a.equals("true")) addOrRemoveProperty(v, RelativeLayout.CENTER_VERTICAL, true);

		a = ""+xpp.getAttributeValue(null, "orientation");
		if(logging) Log.d(LOG_TAG, "orientation:"+a);
		if(a.equals("vertical"))
			((LinearLayout)v).setOrientation(LinearLayout.VERTICAL);
		else if(a.equals("horizontal"))
			((LinearLayout)v).setOrientation(LinearLayout.HORIZONTAL);

		return v;
	}

	// Thanks Hiren Patel
	// Here's a hackish way to make it work.
	private void addOrRemoveProperty(View view, int property, boolean flag){
		RelativeLayout relativeLayout = null;
		RelativeLayout.LayoutParams layoutParams;
		if(view instanceof RelativeLayout){
			layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		}else if(view.getRootView() instanceof  RelativeLayout) {
			layoutParams = (RelativeLayout.LayoutParams) view.getRootView().getLayoutParams();
		} else {
				relativeLayout = new RelativeLayout(ctx);
				layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		}
		if(flag){
			layoutParams.addRule(property);
		}else {
			if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
				layoutParams.removeRule(property);
			else
				layoutParams.addRule(property, 0);
		}
		if(relativeLayout!=null) {
			relativeLayout.setLayoutParams(layoutParams);
			relativeLayout.addView(view);
		}else{
			view.getRootView().setLayoutParams(layoutParams);
		}
	}

	public abstract class ImageLoader {
		public abstract void onUri(String uri, View view);
	}

}
