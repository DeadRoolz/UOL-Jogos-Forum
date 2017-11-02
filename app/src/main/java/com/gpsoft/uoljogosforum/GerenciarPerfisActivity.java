/*
* Activity para gerenciar os perfis salvos no dispositivo
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.widget.*;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.content.*;
import android.view.*;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.support.design.widget.Snackbar;


//jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//java imports
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.List;

//Glide
import com.bumptech.glide.Glide;


public class GerenciarPerfisActivity extends AppCompatActivity {
    
    private ArrayList<UserSalvo> perfis = new ArrayList<UserSalvo>();
    private boolean isBusy = false;
    private BancoDeDados mDbHelper;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_perfis);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.gp_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        getSupportActionBar().setTitle("Gerenciar perfis");
        
        mDbHelper = new BancoDeDados(this);
        
        Refresh();
        
    }
    
    private class UserSalvo {
        private String Senha;
        private String NickName;
        private String AvatarURL;
        private String Email;
        
        UserSalvo(String nick, String senha, String avatarurl, String email) {
            Senha = senha;
            NickName = nick;
            AvatarURL = avatarurl;
            Email = email;
        }
        
        public String getSenha() {
            return Senha;
        }
        
        public String getEmail() {
            return Email;
        }
        
        public String getNickName() {
            return NickName;
        }
        
        public String getAvatarURL() {
            return AvatarURL;
        }
    }
    
    private void DeletarPerfil() {
    }

    public void Refresh() {
        if(isBusy)
            return;
        
        AsyncJob listarPerfis = new AsyncJob();
        listarPerfis.execute();
    }
    
    
    private void BaixarPerfisDB() {
        
        perfis.clear();
        
        String[] projection = {"nickname", "email", "senha", "avatarurl"};
        
        SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
        
        Cursor c = dbReader.query("usuarios", projection, null, null, null, null, null, null);
        
        
        while(c.moveToNext()) {
            
            perfis.add(new UserSalvo(
            c.getString(c.getColumnIndexOrThrow("nickname")),
            c.getString(c.getColumnIndexOrThrow("senha")),
            c.getString(c.getColumnIndexOrThrow("avatarurl")),
            c.getString(c.getColumnIndexOrThrow("email"))
            ));
            
        }
        
    }
    
    private void AtualizarListaDePerfis() {
        LinearLayout PerfisContainer = (LinearLayout) findViewById(R.id.lista_perfis_container);
        
        LayoutInflater inflater = getLayoutInflater();
        
        PerfisContainer.removeAllViews();
        
        LinearLayout v;
        
        ImageView StoredUserAvatar;
        ImageView StoredUserDeleteButton;
        TextView StoredUserName;
        TextView StoredUserEmail;
        
        for(UserSalvo user : perfis) {
            v = (LinearLayout)inflater.inflate(R.layout.list_item_perfis, null);
            
            StoredUserAvatar = (ImageView)v.findViewById(R.id.avatar_stored_gp);
            StoredUserName = (TextView)v.findViewById(R.id.gp_Nickname);
            StoredUserEmail = (TextView)v.findViewById(R.id.gp_Email);
            StoredUserDeleteButton = (ImageView)v.findViewById(R.id.gp_delete_profile);
            
            StoredUserName.setText(user.getNickName());
            StoredUserEmail.setText(user.getEmail());
            StoredUserDeleteButton.setOnClickListener(new DeleteClickListener(user.getEmail()));
            
            Glide.with(this).load(user.getAvatarURL()).into(StoredUserAvatar);
            
            
            PerfisContainer.addView(v);
        }
    }
    
    private class DeleteClickListener implements View.OnClickListener {
        
        private String Email;
        
        DeleteClickListener(String email) {
            Email = email;
        }
        
        @Override
        public void onClick(View v) {
            AsyncJob deletarPerfil = new AsyncJob(Email);
            deletarPerfil.execute();
        }
    }
    
    private int DeletarPerfil(String email) {
        
        SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();
        
        return dbWriter.delete("usuarios", "email=?", new String[]{email});
    }
    
    private class AsyncJob extends AsyncTask<Void, Void, Void> {
        
        private String EmailToDelete = null;
        private int DeletedEntries = 0;
        
        AsyncJob(String emailtodelete) {
            EmailToDelete = emailtodelete;
        }
        
        AsyncJob() {
            EmailToDelete = null;
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isBusy = true;
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            
            if(EmailToDelete != null) {
                DeletedEntries = DeletarPerfil(EmailToDelete);
            }
            
            BaixarPerfisDB();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            isBusy = false;
            AtualizarListaDePerfis();
            
            if(EmailToDelete != null) {
                if(DeletedEntries == 0)
                    Snackbar.make(findViewById(R.id.gp_main_container), R.string.delete_profile_failed_message, Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(findViewById(R.id.gp_main_container), R.string.delete_profile_success_message, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}