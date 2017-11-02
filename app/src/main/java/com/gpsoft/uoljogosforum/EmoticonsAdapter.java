/*
* Fornece a view dos emoticons
* que serão exibidos na gridview 
* da caixa de diálogo dos emoticons
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.widget.*;
import android.view.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;


//glide imports
import com.bumptech.glide.Glide;

public class EmoticonsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private EmoticonsList emoticons;

    public EmoticonsAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        emoticons = new EmoticonsList();
    }

    public int getCount() {
        return emoticons.size();
    }

    public Object getItem(int position) {
        return emoticons.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            v = inflater.inflate(R.layout.emoticon, null);
            ImageView img = (ImageView) v.findViewById(R.id.emoticon_img);
            
            Glide.with(parent.getContext()).load(emoticons.get(position).getId()).into(img);
            
        } else {
            v = (View) convertView;
            ImageView img = (ImageView) v.findViewById(R.id.emoticon_img);
            Glide.with(parent.getContext()).load(emoticons.get(position).getId()).into(img);
        }

        return v;
    }
}