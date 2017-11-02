/*
* Activity que lista as mps
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
import android.util.Log;
import android.app.AlertDialog;
import android.widget.ProgressBar;


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


public class MostrarMpsActivity extends AppCompatActivity {
    
    private CustomAdapterMP modeAdapter;
    private ArrayList<MensagemPrivada> mps = new ArrayList<MensagemPrivada>();
    private ListView modeList;
    private String MP_URL;
    private boolean isBusy = false;
    private Document doc;
    private Menu optionsMenu = null;
    private String token;
    private ProgressBar Progress;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_mps);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mp_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        getSupportActionBar().setTitle("Mensagens particulares");
        
        modeAdapter = new CustomAdapterMP(this,  android.R.layout.simple_list_item_1, mps);
        modeList = (ListView)findViewById(R.id.ListaDeMPs);
        
        getSupportActionBar().setSubtitle("Recebidas");
        MP_URL = "http://forum.jogos.uol.com.br/inbox.jbb";
        
        Progress = (ProgressBar) findViewById(R.id.progressBar);
        
        Refresh();
        
    }
    
    private void DeletarMps() {
        
        
        String charset = "UTF-8";
        String requestURL = MP_URL.equals("http://forum.jogos.uol.com.br/inbox.jbb") ? 
        "http://forum.jogos.uol.com.br/deleteSelectedInbox.jbb" :
        "http://forum.jogos.uol.com.br/deleteSelectedOutbox.jbb";
        
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addFormField("token", token);
            
            
            for (MensagemPrivada mp : modeAdapter.getItems()) {
                if(mp.isChecked()) {
                    multipart.addFormField("idPm", mp.getId());
                }
            }
            
            multipart.finish();
            
            
            
        } catch (IOException ex) {
            
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if(isBusy)
            return false;
        
        switch (item.getItemId()) {
            
            case R.id.action_goto_enviadas:
                MP_URL = "http://forum.jogos.uol.com.br/outbox.jbb";
                getSupportActionBar().setSubtitle("Enviadas");
                Refresh();
                return true;
                
            case R.id.action_goto_recebidas:
                getSupportActionBar().setSubtitle("Recebidas");
                MP_URL = "http://forum.jogos.uol.com.br/inbox.jbb";
                Refresh();
                return true;
                
                
            case R.id.action_deletar:
                modeList.setAdapter(null);
                modeList.setEnabled(false);
                AsyncJob deletarMPs = new AsyncJob(true);
                deletarMPs.execute();
                return true;
                
            case R.id.action_nova_mp:
                AbrirEnviarMPActivity();
                return true;    
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    private void AbrirEnviarMPActivity() {
        Intent intent = new Intent(this, EnviarMPActivity.class);
        startActivity(intent);
    }
    
    public void Refresh() {
        if(isBusy)
            return;
        
        modeList.setAdapter(null);
        modeList.setEnabled(false);
        AsyncJob listarMPs = new AsyncJob();
        listarMPs.execute();
    }
    
    
    
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        
        if(optionsMenu == null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.opcoes_mp, menu);
            optionsMenu = menu;
        }
        return true;
    }
    
    private void BaixarMPs() {
        
        mps.clear();
        
        
        try {
            
            
            URL urlTopics = new URL(MP_URL);
            HttpURLConnection conMPs = (HttpURLConnection)urlTopics.openConnection();
            
            //Setando Request Headers
            
            String ck = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
            }
            
            
            conMPs.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conMPs.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conMPs.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conMPs.setRequestProperty("Connection", "keep-alive");
            conMPs.setRequestProperty("Cookie", ck);
            conMPs.setRequestProperty("Cache-Control", "max-age=0");
            conMPs.setRequestProperty("Host", "forum.jogos.uol.com.br");
            conMPs.setRequestProperty("Referer", "http://forum.jogos.uol.com.br");
            conMPs.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conMPs.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conMPs.getContent();

            Reader reader = null;
            StringWriter writer = null;
            InputStream gzis = new GZIPInputStream(conMPs.getInputStream());
            reader = new InputStreamReader(gzis, "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();
            
            doc = Jsoup.parse(response);
            
        
            Elements mpsTable = doc.select("table[id=pmsList] tr");
            Elements parseToken = doc.select("input[name=token]");
            
            if(parseToken.size() > 0)
                token = parseToken.get(0).attr("value");
            else
                token = "";
            
            
            boolean inOut = MP_URL.equals("http://forum.jogos.uol.com.br/outbox.jbb") ? true : false;
            
            for (int count = 0; count < mpsTable.size(); count++) {
                
                if(count == 0)
                    continue;
                
                mps.add(new MensagemPrivada(new UserInfo(mpsTable.get(count).child(2).child(0).child(0).text(), null, null, null, ""), 
                                            mpsTable.get(count).child(1).child(0).child(0).text(), 
                                            mpsTable.get(count).child(1).child(0).child(0).attr("href"), 
                                            mpsTable.get(count).child(3).child(0).text(),
                                            null,
                                            mpsTable.get(count).child(0).child(0).attr("class").equals("master-sprite sprite-folder") ? true : false,
                                            inOut
                                            ));
                
                
            }
            
        }catch (IOException e) {
            
        }
        
    }
    
    private void AtualizarListaDeMPs() {
        modeList.setAdapter(modeAdapter);
        modeList.setEnabled(true);
    }
    
    
    private class AsyncJob extends AsyncTask<Void, Void, Void> {
        
        private boolean deleteMps; //tarefa (1) == baixarMPS
        
        AsyncJob(boolean del) {
            deleteMps = del;
        }
        
        AsyncJob() {
            deleteMps = false;
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Progress.setVisibility(View.VISIBLE);
            isBusy = true;
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            
            if(deleteMps)
                DeletarMps();
            
            BaixarMPs();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            isBusy = false;
            
            Progress.setVisibility(View.GONE);
            
            AtualizarListaDeMPs();
        }
    }
}