/*
* Gerenciador de layout da gridview que 
* exibe os subforums na tela inicial
*/


package com.gpsoft.uoljogosforum;


import android.widget.*;
import android.view.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

public class MainMenuAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private String[] ForumNomes = {"Noticias", "Nintendo", "PC", "Playstation", "Xbox", "Museu", "Vale Tudo"};

    public MainMenuAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return ForumNomes.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            v = inflater.inflate(R.layout.item_main_menu, null);
            TextView forumName = (TextView) v.findViewById(R.id.ForumDesc);
            forumName.setText(ForumNomes[position]);
            
        } else {
            v = (View) convertView;
        }

        return v;
    }
}