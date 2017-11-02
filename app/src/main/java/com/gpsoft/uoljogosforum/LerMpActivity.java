/*
* Activity para ler mps
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
import android.text.Spannable;
import android.text.Html;
import android.widget.TextView;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.support.v7.widget.*;


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

//Glide
import com.bumptech.glide.Glide;


public class LerMpActivity extends AppCompatActivity {
    
    private ArrayList<MensagemPrivada> mps = new ArrayList<MensagemPrivada>();
    private ListView modeList;
    private String MP_URL;
    private boolean isBusy = false;
    private Document doc;
    private String mpText;
    private String DataEnvio;
    private UserInfo Autor;
    private RecyclerView mRecyclerView;
    private MpAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Menu optionsMenu;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_mp);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.lermp_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        getSupportActionBar().setTitle(getIntent().getStringExtra("MP_TITULO"));
        MP_URL = getIntent().getStringExtra("MP_URL");
        
        
        //Referenciando RecyclerView das Mps
        mRecyclerView = (RecyclerView) findViewById(R.id.Mp_View);
        
        //ListViewRespostas.setOnScrollListener(new EndlessScrollListener());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MpAdapter(mps);
        
        
        Refresh();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_ler_mp, menu);
        
        optionsMenu = menu;
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if(isBusy)
            return false;
        
        switch (item.getItemId()) {
            
            case R.id.action_responder_mp:
                AbrirEnviarMPActivity();
                return true;
            

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void AbrirEnviarMPActivity() {
        Intent intent = new Intent(this, EnviarMPActivity.class);
        intent.putExtra("ASSUNTO", "RE: " + getIntent().getStringExtra("MP_TITULO")); 
        intent.putExtra("DESTINATARIO", getIntent().getStringExtra("MP_AUTOR"));
        
        startActivity(intent);
    }
    
    public void Refresh() {
        if(isBusy)
            return;
        
        mRecyclerView.setAdapter(null);
        
        AsyncJob baixarMP = new AsyncJob();
        baixarMP.execute();
    }
    
    private void BaixarMP() {

        try {
            
            mps.clear();
            
            URL url = new URL("http://forum.jogos.uol.com.br/" + MP_URL);
            HttpURLConnection conMP = (HttpURLConnection)url.openConnection();
            
            //Setando Request Headers
            
            String ck = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
            }
            
            
            conMP.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conMP.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conMP.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conMP.setRequestProperty("Connection", "keep-alive");
            conMP.setRequestProperty("Cookie", ck);
            conMP.setRequestProperty("Cache-Control", "max-age=0");
            conMP.setRequestProperty("Host", "forum.jogos.uol.com.br");
            conMP.setRequestProperty("Referer", "http://forum.jogos.uol.com.br");
            conMP.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conMP.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conMP.getContent();

            Reader reader = null;
            StringWriter writer = null;
            InputStream gzis = new GZIPInputStream(conMP.getInputStream());
            reader = new InputStreamReader(gzis, "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();
            
            doc = Jsoup.parse(response);
            
        
            Elements parsePostText = doc.select("div[class=texto]");
            Elements parseNickname = doc.select("p[class=userNickname] a");
            Elements parseNumMsgs = doc.select("p[class=descricao] b");
            Elements parseAvatarUrl = doc.select("img[id=avatarImg]");
            Elements parseDataCadastro = doc.select("span[class=data-cadastro] b");
            
            
            mpText = parsePostText.get(0).html();
            String AvatarImgUrl = null;
            
            if(parseAvatarUrl.size() < 2)
                AvatarImgUrl = "http://forum.imguol.com//forum/themes/jogos/images/nopic.gif";
            else
                AvatarImgUrl = parseAvatarUrl.get(1).attr("src");
            
            Autor = new UserInfo(parseNickname.get(0).text(),
                                parseDataCadastro.get(0).text(),
                                parseNumMsgs.get(0).text(),
                                AvatarImgUrl, 
                                "");
            
            
            mps.add(new MensagemPrivada(Autor, 
                                        getIntent().getStringExtra("MP_TITULO"), 
                                        getIntent().getStringExtra("MP_URL"),
                                        getIntent().getStringExtra("MP_DATAENVIO"),
                                        mpText,
                                        getIntent().getBooleanExtra("MP_FOILIDA", false),
                                        getIntent().getBooleanExtra("MP_INOUT", false)));
            
        }catch (IOException e) {
            
        }
        
    }
    
    public void AtualizarViewMp() {
        
        mAdapter = new MpAdapter(mps);
        mRecyclerView.setAdapter(mAdapter);
        
        if(MP_URL.indexOf("readIn.jbb") != -1)
            optionsMenu.findItem(R.id.action_responder_mp).setVisible(true);
        else
            optionsMenu.findItem(R.id.action_responder_mp).setVisible(false);
        
    }
    
    private class AsyncJob extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isBusy = true;
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            BaixarMP();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            isBusy = false;
            AtualizarViewMp();
        }
    }
}