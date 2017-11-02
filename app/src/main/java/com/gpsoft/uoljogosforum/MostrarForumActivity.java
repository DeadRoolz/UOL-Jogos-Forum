/*
* Activity que exibe os tópicos do fórum
* selecionado na activity principal
*/


package com.gpsoft.uoljogosforum;


//android imports
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuInflater;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.FrameLayout;
import android.app.Activity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.util.ArraySet;
import android.os.AsyncTask;
import android.support.design.widget.*;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;
import android.widget.NumberPicker;

//jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//java imports
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.net.URL;

import com.bumptech.glide.Glide;

public class MostrarForumActivity extends AppCompatActivity {

    private int IdxPage = 0;
    private View lastLayout;
    private Document doc;
    private ListView modeList;
    private FrameLayout fLayout;
    private CustomAdapter modeAdapter;
    private ArrayList<TpcInfo> tpcs = new ArrayList<TpcInfo>();
    private AsyncJob ExecutarTarefaAsync;
    private FrameLayout RespsList;
    private FrameLayout PreviewList;
    private TabLayout tabLayout;
    private String cookie;
    private boolean logado = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isBusy = false;
    private boolean FillUserInfo = false;
    private FloatingActionButton NewTopicButton;
    private String Forum_URL;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private URI uri;
    private String meusTopicosPageUrl = "";
    private BancoDeDados mDbHelper;
    private int lastPageNumber = 1;
    private int actualPageNumber = 1;
    private AlertDialog PagePicker;
    private View SeletorPagView;
    private Menu optionsMenu;
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_forum);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        getSupportActionBar().setTitle(getIntent().getStringExtra("FORUM_TITULO"));
        Forum_URL = getIntent().getStringExtra("FORUM_URL");
        
        //Constrói caixa de dialogo para selecionar paginas
        SeletorPagView = getLayoutInflater().inflate(R.layout.seletor_de_paginas, null);
        PagePicker = (new AlertDialog.Builder(this, 0)).setView(SeletorPagView).create();
        
        NewTopicButton = (FloatingActionButton) findViewById(R.id.new_topic_button);
        
        NewTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click.
            }
        });
        
        
        modeAdapter = new CustomAdapter(this,  android.R.layout.simple_list_item_1, tpcs, false);
        modeList = (ListView)findViewById(R.id.ListaDeTopicos);
        
        
        
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange);
        
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        
            @Override
            public void onRefresh() {
                RefreshForum();
            }
        });
        
        
        //setando onclick listener do botao enviar
        FloatingActionButton floatingActionButton =
    (FloatingActionButton) findViewById(R.id.new_topic_button);
    
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AbrirCriarTopicoActivity();
        }
    });
        
        
        CookieForum.getCookieManager().setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(CookieForum.getCookieManager());
        
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        
        
        /*
        
        Implementar codigo para restaurar credenciais
        do usuario no banco de dados e usa-los para logar
        bem como resutarar os cookies
        
        */
        try {
            uri = new URI("http://forum.jogos.uol.com.br");
        }catch(Exception e) {
            
        }
        String ckName = null;
        String ckValue = null;
        
        for (String ck : sharedPref.getStringSet("Cookies", new HashSet<String>())) {
            ckName = ck.substring(0, ck.indexOf('='));
            if(ck.indexOf('=') == ck.length()-1)
                ckValue = "";
            else
                ckValue = ck.substring(ck.indexOf('=')+1);
                
            CookieForum.getCookieManager().getCookieStore().add(uri, new HttpCookie(ckName, ckValue));
        }
        
        Usuario.setAvatarUrl(sharedPref.getString("AvatarUrl", ""));
        Usuario.setEmail(sharedPref.getString("Email", ""));
        Usuario.setSenha(sharedPref.getString("Senha", ""));
        Usuario.setNickName(sharedPref.getString("NickName", ""));
        Usuario.setLogado(sharedPref.getBoolean("Logado", false));
        
        mDbHelper = new BancoDeDados(this);
        
        RefreshForum();
        
    }
    
    
    @Override
    public void onPause() {
        super.onPause();
        //nessa funcao - gravar num banco de dados do android, a classe Usuario e o cookie, pra manter o user logado
        
        Set<String> cookies = new HashSet<String>();
        
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                cookies.add(cookie.getName() + "=" + cookie.getValue());
        }
        
        editor.putStringSet("Cookies", cookies);
        editor.putString("AvatarUrl", Usuario.getAvatarUrl());
        editor.putString("Email", Usuario.getEmail());
        editor.putString("Senha", Usuario.getSenha());
        editor.putString("NickName", Usuario.getNickName());
        editor.putBoolean("Logado", Usuario.isLogged());
        editor.commit();
            
          
    }
    

    
    private void AbrirCriarTopicoActivity() {
        
        String ck = new String("");
        
        Intent intent = new Intent(this, CriarTopicoActivity.class);
        
        intent.putExtra("FORUM_TITULO", getSupportActionBar().getTitle());
        intent.putExtra("FORUM_URL", Forum_URL);
        
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
            
            ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
                
            if (cookie.getName().equals("JSESSIONID"))
                intent.putExtra("JSESSIONID", cookie.getValue());
        }
        
        
        startActivityForResult(intent, 4);
    }
    
    private void AbrirMeusTopicosActivity() {
        Intent intent = new Intent(this, MostrarMeusTopicosActivity.class);
        intent.putExtra("MEUSTOPICOS_PAGE_URL", meusTopicosPageUrl);
        startActivityForResult(intent, 6);
    }
    
    private static void FazerLogin() {
        
    }
    
    
    public void AtualizarInterface() {
        getSupportActionBar().setSubtitle("Pág.: " + actualPageNumber + " / " + lastPageNumber);
        
        NumberPicker np =  (NumberPicker)SeletorPagView.findViewById(R.id.numberPicker1);
        
        np.setMinValue(1);
        np.setMaxValue(lastPageNumber);
        np.setValue(actualPageNumber);
        
        if(actualPageNumber == lastPageNumber) {
            optionsMenu.findItem(R.id.action_proximo).setVisible(false);
            if(actualPageNumber == 1)
                optionsMenu.findItem(R.id.action_anterior).setVisible(false);
            else
                optionsMenu.findItem(R.id.action_anterior).setVisible(true);
        }
        else {
            
            if(actualPageNumber == 1)
                optionsMenu.findItem(R.id.action_anterior).setVisible(false);
            else
                optionsMenu.findItem(R.id.action_anterior).setVisible(true);
            
            optionsMenu.findItem(R.id.action_proximo).setVisible(true);
        }
        
        
    }
    
    //Refresh lista de topicos
    public void RefreshForum() {
        if(isBusy)
            return;
        
        modeList.setAdapter(null);
        mSwipeRefreshLayout.setRefreshing(true);
        modeList.setEnabled(false);
        AsyncJob listaTpcs = new AsyncJob(actualPageNumber);
        listaTpcs.execute();
    }
    
 
    //Abre o topico
    public void AbrirTopico(int Idx) {
        
        String TpcURL = tpcs.get(Idx).getLink();
        String TpcTitulo = tpcs.get(Idx).getTitulo();
        
        
        
        Intent intent = new Intent(this, MostrarTopicoActivity.class);
        intent.putExtra("TOPICO_URL", TpcURL);
        intent.putExtra("TOPICO_TITULO", TpcTitulo);
        
        startActivity(intent);

        return;
    }
    
    public void AbrirMpsActivity() {
        Intent intent = new Intent(this, MostrarMpsActivity.class);
        startActivityForResult(intent, 8);
    }
    
    public void AbrirLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 2);
    }
    
    public void AbrirSobreActivity() {
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
    }
    
    private void SalvarUsuarioBD() {
        AsyncJobDB salvarusuario = new AsyncJobDB();
        salvarusuario.execute();
    }
    
    private void AsyncSaveUser() {
        SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("nickname", Usuario.getNickName());
        values.put("email", Usuario.getEmail());
        values.put("senha", Usuario.getSenha());
        values.put("avatarurl", Usuario.getAvatarUrl());
        
        dbWriter.replace("usuarios", null, values);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if(requestCode == 2) {        
            if(resultCode == AppCompatActivity.RESULT_OK) {
                
                SalvarUsuarioBD();
                RefreshForum();
                UpdateUserNavView();
            }
        }
        
        if(requestCode == 4) {
            if(resultCode == AppCompatActivity.RESULT_OK) {
                RefreshForum();
            }
        }
    }
    
    private void Deslogar() {
        
        Usuario.Deslogar();
        
        MenuItem menuitem_logar = (MenuItem)optionsMenu.findItem(R.id.menu_logar);
        MenuItem menuitem_meustopicos = (MenuItem)optionsMenu.findItem(R.id.menu_meustopicos);
        MenuItem menuitem_mp = (MenuItem)optionsMenu.findItem(R.id.menu_mp);
        MenuItem menuitem_sair = (MenuItem)optionsMenu.findItem(R.id.menu_sair);
        
        menuitem_logar.setVisible(true);
        menuitem_meustopicos.setVisible(false);
        menuitem_mp.setVisible(false);
        menuitem_sair.setVisible(false);
        
        Snackbar.make(findViewById(R.id.forum_main_container), R.string.logout_success_message, Snackbar.LENGTH_LONG).show();
        
        RefreshForum();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_forum, menu);
        optionsMenu = menu;
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        
        switch (item.getItemId()) {
                
            case R.id.action_navegar_pag:
                PagePicker.show();
                return true;
                
            case R.id.action_proximo:
                carregarForum(actualPageNumber+1);
                return true;
                
            case R.id.action_anterior:
                carregarForum(actualPageNumber-1);
                return true;
                
            case R.id.menu_logar:
                AbrirLoginActivity();
                return true;
                
            case R.id.menu_meustopicos:
                AbrirMeusTopicosActivity();
                return true;
                
            case R.id.menu_mp:
                AbrirMpsActivity();
                return true;
                
            case R.id.menu_about:
                AbrirSobreActivity();
                return true;
                
            case R.id.menu_sair:
                Deslogar();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void carregarForum(int pagina) {
        if(isBusy)
            return;
        
        modeList.setAdapter(null);
        mSwipeRefreshLayout.setRefreshing(true);
        modeList.setEnabled(false);
        AsyncJob listaTpcs = new AsyncJob(pagina);
        listaTpcs.execute();
    }
    
    public void PagDialogClickListener(View v) {
        PagePicker.dismiss();
        
        if(isBusy)
            return;
        
        if (v.getId() == R.id.pag_dialog_go) {
            NumberPicker np =  (NumberPicker)SeletorPagView.findViewById(R.id.numberPicker1);
            
            if (np.getValue() == actualPageNumber)
                return;
            
            carregarForum(np.getValue());
        }
    }
    
    private void ListarTopicos(int pagina) {
        
        tpcs.clear();
        
        try {
            
            URL urlTopics = new URL(Forum_URL + "?page=" + Integer.toString(pagina));
            HttpURLConnection conTopics = (HttpURLConnection)urlTopics.openConnection();
            
            //Setando Request Headers
            
            String ck = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
            }
            
            
            conTopics.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conTopics.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conTopics.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conTopics.setRequestProperty("Connection", "keep-alive");
            conTopics.setRequestProperty("Cookie", ck);
            conTopics.setRequestProperty("Cache-Control", "max-age=0");
            conTopics.setRequestProperty("Host", "forum.jogos.uol.com.br");
            conTopics.setRequestProperty("Referer", "http://forum.jogos.uol.com.br");
            conTopics.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conTopics.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conTopics.getContent();
            

            Reader reader = null;
            StringWriter writer = null;
            InputStream gzis = new GZIPInputStream(conTopics.getInputStream());
            reader = new InputStreamReader(gzis, "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();
            
            doc = Jsoup.parse(response);
            
        
            Elements tpcsParse = doc.select("div[class=topicos] a");
            Elements tpcsParseAutor = doc.select("span[class=autor] a");
            Elements tpcsParseResps = doc.select("span[class=respostas]");
            Elements tpcsParseStars = doc.select("span[class=rating] img");
            Elements tpcsParseLastMsg = doc.select("span[class=lastmessage]");
            Elements ParseNumMps = doc.select("a[id=private-messages] span");
            Elements meusTopicosParseURL = doc.select("a[class=menu-topics menu-buttons]");
            Elements pageCount = doc.select("img[class=master-sprite sprite-last]");
            Elements actualPage = doc.select("span[class=actualPage]");
            
            if(tpcsParse.size() == 0)
                return;
            
            meusTopicosPageUrl = meusTopicosParseURL.size() > 0 ? meusTopicosParseURL.get(0).attr("href") : "";
            
            lastPageNumber = 1;
            actualPageNumber = 1;
            
            if(pageCount.size() == 0)
                lastPageNumber = actualPageNumber = Integer.parseInt(actualPage.get(0).text().replaceAll("[\\D]", ""));
            else {
                actualPageNumber = Integer.parseInt(actualPage.get(0).text().replaceAll("[\\D]", ""));
                lastPageNumber = Integer.parseInt(pageCount.get(0).parent().attr("href").split("\\?page\\=")[1]);
            }
            
            if(ParseNumMps.size() > 0) {
                Usuario.setNumMps(ParseNumMps.get(0).text());
            }
            else {
                Usuario.Deslogar();
                Usuario.setNumMps("0");
            }
            
            int countRating = 0;
            
            for (int count = 0; count < tpcsParse.size(); count++) {
                
                tpcs.add((TpcInfo)(new TpcInfo(tpcsParseAutor.get(count).text(), 
                tpcsParse.get(count).attr("title"), 
                tpcsParseStars.get(countRating).attr("title").substring((tpcsParseStars.get(countRating).attr("title").length()) - 3),
                tpcsParseResps.get(count).text(), 
                tpcsParse.get(count).attr("href"), tpcsParseLastMsg.get(count).text().split(" Por:")[0], tpcsParseLastMsg.get(count).text().split(" Por:")[1])));
                
                
                countRating+=5;
            }
            
            
        }catch (IOException e) {
            
        }
        
    }
    
    
    private void AtualizarListaDeTopicos() {
        modeList.setAdapter(modeAdapter);
        
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
    
            AbrirTopico(position);
        }

        });
        
        mSwipeRefreshLayout.setRefreshing(false);
        modeList.setEnabled(true);
    }
    
    
    private void UpdateUserNavView() {
        
        MenuItem menuitem_logar = (MenuItem)optionsMenu.findItem(R.id.menu_logar);
        MenuItem menuitem_meustopicos = (MenuItem)optionsMenu.findItem(R.id.menu_meustopicos);
        MenuItem menuitem_mp = (MenuItem)optionsMenu.findItem(R.id.menu_mp);
        MenuItem menuitem_sair = (MenuItem)optionsMenu.findItem(R.id.menu_sair);
        
        if(Usuario.isLogged() == true) {
            
            menuitem_logar.setVisible(false);
            menuitem_meustopicos.setVisible(true);
            menuitem_mp.setVisible(true);
            menuitem_sair.setVisible(true);
            menuitem_mp.setTitle("MPs " + Usuario.getNumMps());
        }
        else {
            
            menuitem_logar.setVisible(true);
            menuitem_meustopicos.setVisible(false);
            menuitem_mp.setVisible(false);
            menuitem_sair.setVisible(false);
            
        }
    }
    
    private class AsyncLogarEAtualizar extends AsyncTask<Void, Void, Void> {
        
        private String email;
        private String senha;
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = "";
            senha = "";
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            if((email == null) || (senha == null))
                return null;
            
            Usuario.Logar(email, senha);
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            RefreshForum();
            
        }
    }
    
    private class AsyncJobDB extends AsyncTask<Void, Void, Void> {
        
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            AsyncSaveUser();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
        }
    }
    
    private class AsyncJob extends AsyncTask<Void, Void, Void> {
        
        private int pagina;
        
        @Override
        protected void onPreExecute() {
            isBusy = true;
            super.onPreExecute();
        }
        
        AsyncJob(int p) {
            pagina = p;
        }
        
 
        @Override
        protected Void doInBackground(Void... params) {
            
            ListarTopicos(pagina);
            return null;
           
        }
        
        @Override
        protected void onPostExecute(Void result) {
            
            isBusy = false;
            
            UpdateUserNavView();
            AtualizarListaDeTopicos();
            AtualizarInterface();
        }
    }
}