/*
* ImageGetter usado pelo Html.fromHtml
* Classe que faz o download e exibe as imagens presentes nas postagens
*/


package com.gpsoft.uoljogosforum;


//android imports
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


//glide imports
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;


//java imports
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class ImageGetter implements Html.ImageGetter, Drawable.Callback {

    private final Context mContext;

    private final TextView mTextView;

    private final Set<ImageGetterViewTarget> mTargets;

    public static ImageGetter get(View view) {
        return (ImageGetter)view.getTag(R.id.drawable_callback_tag);
    }

    public void clear() {
        ImageGetter prev = get(mTextView);
        if (prev == null) return;

        for (ImageGetterViewTarget target : prev.mTargets) {
            Glide.clear(target);
        }
    }

    public ImageGetter(Context context, TextView textView) {
        this.mContext = context;
        this.mTextView = textView;
        
        //clear();
        mTargets = new HashSet<>();
        mTextView.setTag(R.id.drawable_callback_tag, this);
    }
    
    @Override
    public Drawable getDrawable(String url) {
        final UrlDrawable urlDrawable = new UrlDrawable();

        
		Glide.with(mContext)
				.load(url).placeholder(R.drawable.loader)
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(new ImageGetterViewTarget(mTextView, urlDrawable));
	

        return urlDrawable;

    }

    @Override
    public void invalidateDrawable(Drawable who) {
        mTextView.invalidate();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {

    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {

    }

    private class ImageGetterViewTarget extends ViewTarget<TextView, GlideDrawable> {

        private final UrlDrawable mDrawable;

        private ImageGetterViewTarget(TextView view, UrlDrawable drawable) {
            super(view);
            mTargets.add(this);
            this.mDrawable = drawable;
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            Rect rect;
            if (resource.getIntrinsicWidth() > 60) {
                float width;
                float height;
                if (resource.getIntrinsicWidth() >= getView().getWidth()) {
                    float downScale = (float) resource.getIntrinsicWidth() / getView().getWidth();
                    width = (float) resource.getIntrinsicWidth() / (float) downScale;
                    height = (float) resource.getIntrinsicHeight() / (float) downScale;
                } else {
                    float multiplier = (float) getView().getWidth() / resource.getIntrinsicWidth();
                    width = (float) resource.getIntrinsicWidth() * (float) (multiplier/4);
                    height = (float) resource.getIntrinsicHeight() * (float) (multiplier/4);
                }


                rect = new Rect(0, 0, Math.round(width), Math.round(height));
            } else {
                rect = new Rect(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
            }
            resource.setBounds(rect);

            mDrawable.setBounds(rect);
            mDrawable.setDrawable(resource);


            if (resource.isAnimated()) {
                
                if(get(getView()) == null) {
                    getView().setTag(R.id.drawable_callback_tag, null);
                }
                else {
                    mDrawable.setCallback(get(getView()));
                    resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                    resource.start();
                }
            } 
            
            //getView().invalidate();
            getView().setText(getView().getText());
        }

        private Request request;
        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void setRequest(Request request) {
            this.request = request;
        }
    }
}