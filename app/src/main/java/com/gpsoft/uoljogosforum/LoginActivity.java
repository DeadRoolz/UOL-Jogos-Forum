/*
* Activity para fazer login
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.CheckBox;
import android.os.AsyncTask;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.util.Log;
import android.support.design.widget.Snackbar;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.ProgressBar;

//java imports
import java.util.ArrayList;
import java.net.*;
import java.io.*;
import java.util.zip.GZIPInputStream;

//jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//Glide
import com.bumptech.glide.Glide;

public class LoginActivity extends AppCompatActivity {
    
    private boolean LoginSuccess = false;
    private BancoDeDados mDbHelper;
    private ArrayList<UserSalvo> LoginsSalvos = new ArrayList<UserSalvo>();
    private ProgressBar Progress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_login_layout);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        Progress = (ProgressBar) findViewById(R.id.progressBar);
        
        mDbHelper = new BancoDeDados(this);
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        AsyncJobDB LerUsuariosSalvos = new AsyncJobDB();
        LerUsuariosSalvos.execute();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_login, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            
            case R.id.action_gerperfis:
                AbrirGerenciarPefisActivity();
                return true;
            

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void AbrirGerenciarPefisActivity() {
        Intent intent = new Intent(this, GerenciarPerfisActivity.class);
        startActivity(intent);
    }
    
    public void BotaoEnviarListener(View v) {
        AsyncJobLogar logar = new AsyncJobLogar(
        ((EditText)(findViewById(R.id.email_address))).getText().toString(), 
        ((EditText)(findViewById(R.id.senha))).getText().toString());
        
        logar.execute();
    }
    
    private void getUsuariosSalvosBD() {
        
        String[] projection = {"nickname", "email", "senha", "avatarurl"};
        
        SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
        
        Cursor c = dbReader.query("usuarios", projection, null, null, null, null, "nickname", null);
        
        LoginsSalvos.clear();
        
        while(c.moveToNext()) {
            
            LoginsSalvos.add(new UserSalvo(
            c.getString(c.getColumnIndexOrThrow("nickname")),
            c.getString(c.getColumnIndexOrThrow("senha")),
            c.getString(c.getColumnIndexOrThrow("avatarurl")),
            c.getString(c.getColumnIndexOrThrow("email"))
            ));
            
        }
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
    
    private void AtualizaViewLoginsSalvos() {
        
        LinearLayout LoginsSalvosContainer = (LinearLayout) findViewById(R.id.logins_salvos_container);
        LoginsSalvosContainer.removeAllViews();
        
        LayoutInflater inflater = getLayoutInflater();
        
        LinearLayout v;
        
        ImageView StoredUserAvatar;
        TextView StoredUserName;
        
        for(UserSalvo user : LoginsSalvos) {
            v = (LinearLayout)inflater.inflate(R.layout.layout_user_saved_login, null);
            
            StoredUserAvatar = (ImageView)v.findViewById(R.id.avatar_stored_login);
            StoredUserName = (TextView)v.findViewById(R.id.name_stored_login);
            
            StoredUserName.setText(user.getNickName());
            Glide.with(this).load(user.getAvatarURL()).into(StoredUserAvatar);
            
            v.setOnClickListener(new AvatarClickListener(user.getEmail(), user.getSenha()));
            
            LoginsSalvosContainer.addView(v);
        }
        
    }
    
    private class AvatarClickListener implements View.OnClickListener {
        
        private String SenhaToLogin;
        private String EmailToLogin;
        
        AvatarClickListener(String email, String senha) {
            SenhaToLogin = senha;
            EmailToLogin = email;
        }
        
        @Override
        public void onClick(View v) {
            AsyncJobLogar logar = new AsyncJobLogar(EmailToLogin, SenhaToLogin);
            logar.execute();
        }
    }
    
    private class AsyncJobDB extends AsyncTask<Void, Void, Void> {
        
        
        @Override
        protected void onPreExecute() {
            Progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            getUsuariosSalvosBD();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            Progress.setVisibility(View.GONE);
            AtualizaViewLoginsSalvos();
        }
    }
    
    
    private class AsyncJobLogar extends AsyncTask<Void, Void, Void> {
        
        private String email;
        private String pass;
        
        @Override
        protected void onPreExecute() {
            Progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        
        AsyncJobLogar(String e, String p) {
            email = e;
            pass = p;
        }
 
        @Override
        protected Void doInBackground(Void... params) {
        
            LoginSuccess = Usuario.Logar(email, pass);
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            
            Progress.setVisibility(View.GONE);
            
            if (LoginSuccess == true) {
                setResult(AppCompatActivity.RESULT_OK);
                finish();
            }
            else {
                Snackbar.make(findViewById(R.id.login_main_container), R.string.login_failed_message, Snackbar.LENGTH_LONG).show();
            }
            
            return;
        }
    }
    
}