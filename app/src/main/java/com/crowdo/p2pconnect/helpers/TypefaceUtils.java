package com.crowdo.p2pconnect.helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by cwdsg05 on 28/2/17.
 */

public class TypefaceUtils {

    public static Typeface getNothingYouCouldDoTypeFace(final Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/NothingYouCouldDo.ttf");
    }
}
