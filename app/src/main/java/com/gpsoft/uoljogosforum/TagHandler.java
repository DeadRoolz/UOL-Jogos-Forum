/*
* trata as tags não reconhecidas pelo Html.fromHtml
* usado para tratar as tags de videos embutidos do youtube
* no post
*/

package com.gpsoft.uoljogosforum;

import android.text.Html;
import android.text.Layout;
import android.text.Editable;
import android.text.Spannable;
import android.content.Context;
import android.widget.*;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.URLSpan;


import org.xml.sax.XMLReader;


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class TagHandler implements Html.TagHandler {
    
    private Context mContext;
    private TextView mTextView;
    private YouTubeVideo YTVideo;
    
    TagHandler(Context c, TextView tv, YouTubeVideo ytvideo) {
        mContext = c;
        mTextView = tv;
        YTVideo = ytvideo;
    }
    
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if((tag.equals("embed")) && (opening)) {
            ImageGetter x = new ImageGetter(mContext, mTextView);
        
            if(YTVideo != null) {
                Drawable img = x.getDrawable(YTVideo.getThumbUrl());
                ImageSpan spanImg = new ImageSpan(img, ImageSpan.ALIGN_BASELINE );
                
                SpannableString stringEstilizada = new SpannableString(YTVideo.getTitulo() + "i");

                stringEstilizada.setSpan(
                    spanImg,
                    stringEstilizada.length()-1,
                    stringEstilizada.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
                
                //AlignmentSpan.Standard as = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
                stringEstilizada.setSpan(new URLSpan("https://www.youtube.com/watch?v=" + YTVideo.getId()), 0, YTVideo.getTitulo().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                
                output = output.append(stringEstilizada);
            }
        }
    }
    
}