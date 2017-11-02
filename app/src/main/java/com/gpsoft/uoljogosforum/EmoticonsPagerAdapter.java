/*
* Gerenciador de layout da caixa de dialogo de bbcode e emoticons
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.content.Context;
import android.widget.*;
import android.view.*;
import android.support.v4.view.PagerAdapter;

public class EmoticonsPagerAdapter extends PagerAdapter {
    private final int NUM_PAGES = 2;
    private Context mContext;
    private EditText textEditor;
    private LayoutInflater inflater;
    private EmoticonsAdapter gridAdapter;
    
    EmoticonsPagerAdapter(Context c, EditText ti) {
        mContext = c;
        textEditor = ti;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gridAdapter = new EmoticonsAdapter(c);
    }
    
    @Override
    public int getCount() {
        return NUM_PAGES;
    }
    
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView((View)obj);
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = null;
        
        if(position == 0) {
            v = inflater.inflate(R.layout.bbcodes_layout, null);
        }
        if(position == 1) {
            v = inflater.inflate(R.layout.emoticon_grid_view, null);
            GridView gridview_emoticons = (GridView) v.findViewById(R.id.gridview_emoticons);
            gridview_emoticons.setAdapter(gridAdapter);
            gridview_emoticons.setOnItemClickListener(new onEmoticonClickListener());
        }
        
        
        container.addView(v);
        
        return v;
    }
    
    private class onEmoticonClickListener implements AdapterView.OnItemClickListener {
        
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            String code = ((Emoticon)(gridAdapter.getItem(position))).getCode();
            textEditor.append(" " + code + " ");
        }
    }
    
    private class onItemClicked implements View.OnClickListener {
        private EditText ti;
        
        onItemClicked (EditText t) {
            ti = t;
        }
        
        @Override
        public void onClick(View v) {
            ti.append("MAOE");
        }
    }
}