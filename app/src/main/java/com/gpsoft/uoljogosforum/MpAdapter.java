/*
* Fornece a view de cada mensagem particular
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Html;


//java imports
import java.util.*;

//glide imports
import com.bumptech.glide.Glide;

public class MpAdapter extends RecyclerView.Adapter<MpAdapter.ViewHolder> {
    private ArrayList<MensagemPrivada> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mPostView;
        public ViewHolder(CardView v) {
            super(v);
            mPostView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MpAdapter(ArrayList<MensagemPrivada> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView v = (CardView)LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.list_item_mp_leitura, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        
        TextView txtPost = (TextView) holder.mPostView.findViewById(R.id.Mp_text);
        TextView txtAuthor = (TextView) holder.mPostView.findViewById(R.id.txtAutor);
        TextView txtCadastro = (TextView) holder.mPostView.findViewById(R.id.txtCadastro);
        TextView txtDataPostagem = (TextView) holder.mPostView.findViewById(R.id.txtDataEnvio);
        TextView txtNumMsgs = (TextView) holder.mPostView.findViewById(R.id.txtNumMensagens);
		ImageView Avatar = (ImageView) holder.mPostView.findViewById(R.id.Avatar);
        
		
		MensagemPrivada mp = mDataset.get(position);
        
        
        
		txtPost.setText((Spannable)Html.fromHtml(mp.getTexto(), new ImageGetter(holder.mPostView.getContext(), txtPost), null));
        txtAuthor.setText(mp.getAutor().getNickName());
        txtDataPostagem.setText(mp.getDataEnvio());
        txtNumMsgs.setText(mp.getAutor().getNumMsgs());
        txtCadastro.setText(mp.getAutor().getDataCadastro());
        Glide.with(holder.mPostView.getContext()).load(mp.getAutor().getAvatarURL()).into(Avatar);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}