package com.jediburrell.parx.viewparsers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import static com.jediburrell.parx.Parx.logging;
import static com.jediburrell.parx.Parx.LOG_TAG;

public class TextViewParser {

    public static void parseAttributes(Context ctx, View v, XmlPullParser xpp) {
        String a;

        a = "" + xpp.getAttributeValue(null, "text");
        if(logging) Log.d(LOG_TAG, "text:" + a);

        if(a.length() > 0 && !a.equals("null")) {
            if(a.contains("@string/")) {
                int string = ctx.getResources().getIdentifier(a.replace("@", ""),
                        "string", ctx.getPackageName());

                ((TextView) v).setText(ctx.getResources().getString(string));
            } else {
                ((TextView) v).setText(a);
            }
        }

        a = "" + xpp.getAttributeValue(null, "textSize");
        if(logging) Log.d(LOG_TAG, "textSize:" + a);

        if(a.length() > 0 && !a.equals("null")) {
            if(a.contains("sp")) {
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(a.replace("sp", "")));
            } else if(a.contains("dp")) {
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, Integer.parseInt(a.replace("dp", "")));
            } else if(a.contains("px")) {
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(a.replace("px", "")));
            }
        }

        a = "" + xpp.getAttributeValue(null, "textColor");
        if(logging) Log.d(LOG_TAG, "textColor:" + a);

        if(a.length() > 0 && !a.equals("null")) {
            if(a.contains("@color/")) {
                int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
                        "color", ctx.getPackageName());

                ((TextView) v).setTextColor(ctx.getResources().getColor(drawable));
            } else if(a.contains("#")) {
                ((TextView) v).setTextColor(Color.parseColor(a));
            }
        }

        a = "" + xpp.getAttributeValue(null, "hint");
        if(logging) Log.d(LOG_TAG, "hint:" + a);

        if(a.length() > 0 && !a.equals("null")) {
            if(a.contains("@string/")) {
                int string = ctx.getResources().getIdentifier(a.replace("@", ""),
                        "string", ctx.getPackageName());

                ((TextView) v).setHint(ctx.getResources().getString(string));
            } else {
                ((TextView) v).setHint(a);
            }
        }

        a = "" + xpp.getAttributeValue(null, "textColorHint");
        if(logging) Log.d(LOG_TAG, "textColorHint:" + a);

        if(a.length() > 0 && !a.equals("null")) {
            if(a.contains("@color/")) {
                int drawable = ctx.getResources().getIdentifier(a.replace("@", ""),
                        "color", ctx.getPackageName());

                ((TextView) v).setHintTextColor(ctx.getResources().getColor(drawable));
            } else if(a.contains("#")) {
                ((TextView) v).setHintTextColor(Color.parseColor(a));
            }
        }

    }

}
