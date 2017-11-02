/* 
* Adaptador que fornece a view dos containers dos itens
* da lista de tópico
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.CheckBox;
import android.content.*;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Color;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

//java imports
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<TpcInfo> {
    
    private Context mContext;
    private ArrayList<TpcInfo> tpcs_info;
	private LinearLayout lLay;
    private boolean topicType = false; //true = meustopicos | false = topicos
    private BancoDeDados mDbHelper;
    
    public CustomAdapter(Context context, int resource, ArrayList<TpcInfo> objects, boolean type) {
        super(context, resource, objects);
		mContext = context;
        tpcs_info = objects;
        topicType = type;
        mDbHelper = new BancoDeDados(context);
    }
    
    @Override
    public View getView(int position, View  convertView, ViewGroup parent) {
        
        View v = convertView;
        
        if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(!topicType)
                v = inflater.inflate(R.layout.list_item, null);
            else
                v = inflater.inflate(R.layout.list_item_meus_topicos, null);
		}
        
        
        TextView txtAutor = (TextView) v.findViewById(R.id.Autor);
        TextView txtDataUltimaMsg = (TextView) v.findViewById(R.id.DataUltimMsg);
        TextView txtAutorUltimaMsg = (TextView) v.findViewById(R.id.AutorUltimMsg);
        TextView txtTitulo = (TextView) v.findViewById(R.id.Titulo);
		TextView txtResps = (TextView) v.findViewById(R.id.NumResps);
		ImageView Star1 = (ImageView) v.findViewById(R.id.Estrela1);
        LinearLayout tituloContainer = (LinearLayout) v.findViewById(R.id.TituloContainer);
		ImageView Star2 = (ImageView) v.findViewById(R.id.Estrela2);
		ImageView Star3 = (ImageView) v.findViewById(R.id.Estrela3);
		ImageView Star4 = (ImageView) v.findViewById(R.id.Estrela4);
		ImageView Star5 = (ImageView) v.findViewById(R.id.Estrela5);
		
		TpcInfo it = tpcs_info.get(position);
        
		if(Star1 != null) {
            if(it.getEstrelas().equals("0.0")) 
            {
                Star1.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star2.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("0.5")) {
                Star1.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("1.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("1.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("2.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("2.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("3.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("3.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("4.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getEstrelas().equals("4.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_half_red_24dp);
            }
            
            if(it.getEstrelas().equals("5.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_red_24dp);
            }
        }
        
        txtAutor.setText(it.getAutor());
        txtDataUltimaMsg.setText(it.getDataUltimaMsg());
		txtResps.setText(it.getNumRespostas() + " Respostas");
        txtTitulo.setText(it.getTitulo());
        txtAutorUltimaMsg.setText(" | " + it.getAutorUtlmMsg());
        
        
        v.setOnClickListener(new onTitleClicked(position));
        
        return v;
    }
    
    
    private class onTitleClicked implements View.OnClickListener {
        private int position;
        
        onTitleClicked(int pos) {
            position = pos;
        }
        
        @Override
        public void onClick(View v) {
            AbrirTopico(position);
        }
    }
    
    private void AbrirTopico(int Idx) {
        
        String TpcURL = tpcs_info.get(Idx).getLink();
        String TpcTitulo = tpcs_info.get(Idx).getTitulo();
        String TpcId = tpcs_info.get(Idx).getId();
        
        Intent intent = new Intent(mContext, MostrarTopicoActivity.class);
        intent.putExtra("TOPICO_URL", TpcURL);
        intent.putExtra("TOPICO_TITULO", TpcTitulo);
        intent.putExtra("TOPICO_ID", TpcId);
        
        
        mContext.startActivity(intent);

        return;
    }
    
}