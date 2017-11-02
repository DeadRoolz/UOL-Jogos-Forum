/* 
* Adaptador que fornece a view dos containers dos itens
* da lista de mps
*/


package com.gpsoft.uoljogosforum;


//android imports
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Color;
import android.content.*;


//java imports
import java.util.ArrayList;

public class CustomAdapterMP extends ArrayAdapter<MensagemPrivada> {
    
    private Context mContext;
    private ArrayList<MensagemPrivada> mps;
    
    public CustomAdapterMP(Context context, int resource, ArrayList<MensagemPrivada> objects) {
        super(context, resource, objects);
		mContext = context;
        mps = objects;
    }
    
    public ArrayList<MensagemPrivada> getItems() {
        return mps;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View v = convertView;
        
        if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            v = inflater.inflate(R.layout.list_item_mps, null);

		}
        
        
        TextView txtAutor = (TextView) v.findViewById(R.id.Autor);
        TextView txtDataEnvio = (TextView) v.findViewById(R.id.data_envio);
        TextView txtDePara = (TextView) v.findViewById(R.id.de_para_tv);
        TextView txtTitulo = (TextView) v.findViewById(R.id.Titulo);
        CheckBox chkMarcado = (CheckBox) v.findViewById(R.id.checkbox);
        LinearLayout MainContainer = (LinearLayout)v.findViewById(R.id.Topic_Item_Container);
		
		MensagemPrivada it = mps.get(position);
        
        if(it.getinOut()) {
            txtDePara.setText("Para: ");
        }
        else {
            txtDePara.setText("De: ");
        }
        
        txtAutor.setText(it.getAutor().getNickName());
        txtDataEnvio.setText(it.getDataEnvio());
        txtTitulo.setText(it.getTitulo());
        
        txtTitulo.setOnClickListener(new onTitleClicked(position));
        
        chkMarcado.setChecked(mps.get(position).isChecked());
        
        chkMarcado.setOnClickListener(new onCheckBoxClicked(position));
        
        return v;
    }
    
    private class onCheckBoxClicked implements View.OnClickListener {
        private int p;
        
        onCheckBoxClicked(int pos) {
            p = pos;
        }
        
        @Override
        public void onClick(View v) {
            
            boolean checked = ((CheckBox) v).isChecked();
            
            mps.get(p).setChecked(checked);
        }
    }
    
    private class onTitleClicked implements View.OnClickListener {
        private int p;
        
        onTitleClicked(int pos) {
            p = pos;
        }
        
        @Override
        public void onClick(View v) {
            AbrirMP(p);
        }
    }
    
    
    private void AbrirMP(int Idx) {
        
        String MpURL = mps.get(Idx).getLink();
        String MpTitulo = mps.get(Idx).getTitulo();
        String MpDataEnvio = mps.get(Idx).getDataEnvio();
        boolean MpLido = mps.get(Idx).foiLida();
        
        Intent intent = new Intent(mContext, LerMpActivity.class);
        intent.putExtra("MP_URL", MpURL);
        intent.putExtra("MP_TITULO", MpTitulo);
        intent.putExtra("MP_DATAENVIO", MpDataEnvio);
        intent.putExtra("MP_FOILIDA", MpLido);
        intent.putExtra("MP_INOUT", mps.get(Idx).getinOut());
        intent.putExtra("MP_AUTOR", mps.get(Idx).getAutor().getNickName());
        
        mContext.startActivity(intent);

        return;
    }
}