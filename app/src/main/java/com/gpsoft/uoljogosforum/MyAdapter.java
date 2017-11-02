/*
* Adapter utilizada para gerenciar as views dos posts
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.support.v7.widget.*;
import android.view.*;
import android.content.Context;
import android.content.Intent;
import android.widget.*;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Html;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.text.method.LinkMovementMethod;


//java imports
import java.util.*;

//jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//glide imports
import com.bumptech.glide.Glide;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<PostsInfo> mDataset;
    private String TpcTitle;
    private EditText edText;
    private boolean isEditing = false;
    private ProgressBar Progress;
    private String editingPostId = "";

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
    public MyAdapter(ArrayList<PostsInfo> myDataset, String tpctitle, EditText ed, ProgressBar progress) {
        mDataset = myDataset;
        TpcTitle = tpctitle;
        edText = ed;
        Progress = progress;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView v = (CardView)LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.list_itemrespostas_, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        
        TextView txtPost = (TextView) holder.mPostView.findViewById(R.id.Post);
        TextView txtAuthor = (TextView) holder.mPostView.findViewById(R.id.txtAutor);
        TextView txtCadastro = (TextView) holder.mPostView.findViewById(R.id.txtCadastro);
        TextView txtDataPostagem = (TextView) holder.mPostView.findViewById(R.id.txtDataPostagem);
        TextView txtNumMsgs = (TextView) holder.mPostView.findViewById(R.id.txtNumMensagens);
        TextView txtAssinatura = (TextView) holder.mPostView.findViewById(R.id.txtAssinatura);
        TextView txtRate = (TextView) holder.mPostView.findViewById(R.id.txtRating);
        TextView NumVotos = (TextView) holder.mPostView.findViewById(R.id.NumVotos);
		ImageView Avatar = (ImageView) holder.mPostView.findViewById(R.id.Avatar);
        RelativeLayout DataPostContainer = (RelativeLayout) holder.mPostView.findViewById(R.id.DataPostContainer);
        LinearLayout UserInteractContainer = (LinearLayout) holder.mPostView.findViewById(R.id.UserInteractContainer);
        LinearLayout PostContainer = (LinearLayout) holder.mPostView.findViewById(R.id.PostContainer);
        LinearLayout MainContainer = (LinearLayout) holder.mPostView.findViewById(R.id.main_container);
        LinearLayout AssinaturaContainer = (LinearLayout) holder.mPostView.findViewById(R.id.AssinaturaContainer);
        LinearLayout RatingTopicContainer = (LinearLayout) holder.mPostView.findViewById(R.id.RatingTopicContainer);
        ImageView Thumb_Up = (ImageView) holder.mPostView.findViewById(R.id.imageView_thumb_up);
        ImageView Thumb_Down = (ImageView) holder.mPostView.findViewById(R.id.imageView_thumb_down);
        ImageView SendMP_Button = (ImageView) holder.mPostView.findViewById(R.id.imageView_MP);
        ImageView Quote_Button = (ImageView) holder.mPostView.findViewById(R.id.imageView_quote);
        ImageView Edit_Post = (ImageView) holder.mPostView.findViewById(R.id.imageView_Edit);
        
        ImageView Star1 = (ImageView) holder.mPostView.findViewById(R.id.imageView_Estrela1);
		ImageView Star2 = (ImageView) holder.mPostView.findViewById(R.id.imageView_Estrela2);
		ImageView Star3 = (ImageView) holder.mPostView.findViewById(R.id.imageView_Estrela3);
		ImageView Star4 = (ImageView) holder.mPostView.findViewById(R.id.imageView_Estrela4);
		ImageView Star5 = (ImageView) holder.mPostView.findViewById(R.id.imageView_Estrela5);
        
		
		PostsInfo it = mDataset.get(position);
        
        if(it.getCount() == 1) {
            
            Thumb_Up.setVisibility(View.GONE);
            Thumb_Down.setVisibility(View.GONE);
            RatingTopicContainer.setVisibility(View.VISIBLE);
            txtRate.setVisibility(View.GONE);

            
            if (it.getNumVotos() != null) {
                NumVotos.setText(it.getNumVotos());
            }
            
            if(it.getRating().equals("0.0")) 
            {
                Star1.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star2.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("0.5")) {
                Star1.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("1.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("1.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("2.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("2.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("3.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_border_black_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("3.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_half_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("4.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
            
            if(it.getRating().equals("4.5")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_half_red_24dp);
            }
            
            if(it.getRating().equals("5.0")) {
                Star1.setImageResource(R.drawable.ic_star_red_24dp);
                Star2.setImageResource(R.drawable.ic_star_red_24dp);
                Star3.setImageResource(R.drawable.ic_star_red_24dp);
                Star4.setImageResource(R.drawable.ic_star_red_24dp);
                Star5.setImageResource(R.drawable.ic_star_red_24dp);
            }
            
        }
        else {
            
            
            Thumb_Up.setVisibility(View.VISIBLE);
            Thumb_Down.setVisibility(View.VISIBLE);
            
            Thumb_Up.setOnClickListener(new onThumbClicked(it.getId(), it.getTpcLink(), true, holder.mPostView.getContext()));
            Thumb_Down.setOnClickListener(new onThumbClicked(it.getId(), it.getTpcLink(), true, holder.mPostView.getContext()));
            
            RatingTopicContainer.setVisibility(View.GONE);
            txtRate.setVisibility(View.VISIBLE);
            
            if (it.getRating().indexOf('-') == -1)
                txtRate.setTextColor(Color.parseColor("#39e600"));
            if (it.getRating().indexOf('+') == -1)
                txtRate.setTextColor(Color.parseColor("#ff0000"));
            if (it.getRating().equals("0"))
                txtRate.setTextColor(Color.parseColor("#000000"));
            
            txtRate.setText(it.getRating());
        }
        
        if(it.getAutor().getNickName().equals(Usuario.getNickName())) {
            Edit_Post.setVisibility(View.VISIBLE);
            Edit_Post.setOnClickListener(new onEditClicked(it.getId(), it.getTpcLink(), edText, Edit_Post, holder.mPostView.getContext()));
            
            if(it.getId().equals(editingPostId)) {
                Edit_Post.setImageResource(R.drawable.ic_create_blue_24dp);
            }else {
                Edit_Post.setImageResource(R.drawable.ic_create_black_24dp);
            }
        }
        else {
            Edit_Post.setVisibility(View.GONE);
        }
        
        SendMP_Button.setOnClickListener(new onSendMPClicked(it.getAutor().getNickName(), holder.mPostView.getContext()));
        
        Quote_Button.setOnClickListener(new onQuoteClicked(it.getId(), it.getTpcLink(), edText, holder.mPostView.getContext()));
        
        
		txtPost.setText((Spannable)Html.fromHtml(it.getPost(), new ImageGetter(holder.mPostView.getContext(), txtPost), new TagHandler(holder.mPostView.getContext(), txtPost, it.getYouTubeVideo())));
        txtPost.setMovementMethod( LinkMovementMethod.getInstance() );
        txtAuthor.setText(it.getAutor().getNickName());
        txtDataPostagem.setText(it.getDataCriacaoTpc());
        txtNumMsgs.setText(it.getAutor().getNumMsgs());
        txtCadastro.setText(it.getAutor().getDataCadastro());
        
        if(it.getAutor().getAssinatura().equals("")) {
            AssinaturaContainer.setVisibility(View.GONE);
        }
        else {
            AssinaturaContainer.setVisibility(View.VISIBLE);
            txtAssinatura.setText((Spannable)Html.fromHtml(it.getAutor().getAssinatura(), new ImageGetter(holder.mPostView.getContext(), txtPost), null));
            txtAssinatura.setMovementMethod( LinkMovementMethod.getInstance() );
        }
        
        
        Glide.with(holder.mPostView.getContext()).load(it.getAutorAvatarLink()).into(Avatar);

    }
    
    public boolean isEditModeEnabled() {
        return isEditing;
    }
    
    public String getEditingPostId() {
        return editingPostId;
    }
    
    private class onQuoteClicked implements View.OnClickListener {
        private String postId;
        private String TpcLink;
        private EditText ediText;
        private Context mContext;
        
        onQuoteClicked(String id, String tpclink, EditText edi, Context c) {
            postId = id;
            TpcLink = tpclink;
            mContext = c;
            ediText = edi;
        }
        
        @Override
        public void onClick(View v) {
            PostFunctions.ObterPost(postId, TpcLink, ediText, mContext, true, Progress);
        }
    }
    
    private class onEditClicked implements View.OnClickListener {
        private String postId;
        private String TpcLink;
        private EditText ediText;
        private Context mContext;
        private ImageView editButton;
        
        onEditClicked(String id, String tpclink, EditText edi, ImageView editbutton, Context c) {
            postId = id;
            TpcLink = tpclink;
            mContext = c;
            ediText = edi;
            editButton = editbutton;
        }
        
        @Override
        public void onClick(View v) {
            if(isEditing)
                isEditing = false;
            else
                isEditing = true;
            
            if(isEditing) {
                editingPostId = postId;
                editButton.setImageResource(R.drawable.ic_create_blue_24dp);
                PostFunctions.ObterPost(postId, TpcLink, ediText, mContext, false, Progress);
            }
            else {
                editingPostId = "";
                editButton.setImageResource(R.drawable.ic_create_black_24dp);
                ediText.setText("");
            }
                
        }
    }
    
    private class onThumbClicked implements View.OnClickListener {
        private String postId;
        private String TpcLink;
        private boolean UpDown;
        private Context mContext;
        
        onThumbClicked(String id, String tpclink, boolean updown, Context c) {
            postId = id;
            TpcLink = tpclink;
            UpDown = updown;
            mContext = c;
        }
        
        @Override
        public void onClick(View v) {
            PostFunctions.AvaliarPost(postId, TpcLink, UpDown ? "7" : "6", mContext, Progress);
        }
    }
    
    private class onSendMPClicked implements View.OnClickListener {
        private String Dest;
        private String Titulo;
        private Context mContext;
        
        onSendMPClicked(String user, Context c) {
            Dest = user;
            mContext = c;
        }
        
        @Override
        public void onClick(View v) {
            AbrirEnviarMP(Dest, mContext);
        }
    }
    
    private void AbrirEnviarMP(String dest, Context c) {
        
        String Dest = dest;
        String Titulo = "Re: " + TpcTitle;
        
        Intent intent = new Intent(c, EnviarMPActivity.class);
        intent.putExtra("DESTINATARIO", Dest);
        intent.putExtra("ASSUNTO", Titulo);
        
        
        c.startActivity(intent);

        return;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}