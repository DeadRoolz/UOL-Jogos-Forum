/*
* Activity para enviar mps(mensagens particulares)
*/


package com.gpsoft.uoljogosforum;


//android imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.widget.GridView;
import android.view.*;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.support.v4.view.ViewPager;
import android.widget.Button;


//java imports
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.zip.GZIPInputStream;

//jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//JSON imports
import org.json.JSONObject;
import org.json.JSONException;


public class EnviarMPActivity extends AppCompatActivity {
    
    
    private String SuccessMsg = new String("Sucesso!");
    private String ResultMsg = new String();
    private boolean isBusy = false;
    private String token;
    private Document doc;
    private AlertDialog EmoticonsPicker;
    private View SeletorEmoticonsView;
    private EmoticonsPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mp);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.enviar_mp_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        getSupportActionBar().setTitle("Enviar mensagem");
        
        String destinatario = getIntent().getStringExtra("DESTINATARIO");
        String assunto = getIntent().getStringExtra("ASSUNTO");
        
        ((EditText)(findViewById(R.id.criar_topico_titulo))).setText((assunto == null) ? "" : assunto);
        ((EditText)(findViewById(R.id.criar_topico_dest))).setText((destinatario == null) ? "" : destinatario);
        
        //Constroi caixa de dialogo para selecionar emoticons e bbcodes
        SeletorEmoticonsView = getLayoutInflater().inflate(R.layout.seletor_de_emoticons, null);
        mPager = (ViewPager) SeletorEmoticonsView.findViewById(R.id.pager);
        mPagerAdapter = new EmoticonsPagerAdapter(this, (EditText)findViewById (R.id.criar_topico_mensagem));
        mPager.setAdapter(mPagerAdapter);
        EmoticonsPicker = (new AlertDialog.Builder(this, 0)).setView(SeletorEmoticonsView).create();
        
        AsyncJob baixarToken = new AsyncJob(1);
        baixarToken.execute();
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_enviar_mp, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if(isBusy)
            return false;
        
        switch (item.getItemId()) {
            
            case R.id.action_enviar_mp:
                AsyncJob enviarMP = new AsyncJob(2);
                enviarMP.execute();
                return true;
                
            case R.id.action_insert_emoticon:
                EmoticonsPicker.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void BotaoFecharSeletorEmoticonsListener(View v) {
        EmoticonsPicker.dismiss();
    }
    
    public void OnBBCodeClickListener(View v) {
        
        EditText respostaEditText = (EditText)findViewById (R.id.criar_topico_mensagem);
        respostaEditText.append("[" + ((Button)v).getText() + "]" + "  " + "[/" + ((Button)v).getText() + "]");
    }
    
    private void EnviarMensagem(String titulo, String mensagem, String username) {
        
        String charset = "UTF-8";
        String requestURL = "http://forum.jogos.uol.com.br/send.jbb";
        String response;
        
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addFormField("token", token);
            multipart.addFormField("userId", "");
            multipart.addFormField("pm.text", "");
            multipart.addFormField("username", username);
            multipart.addFormField("pm.topic", titulo);
            multipart.addFormField("addbbcode20", "12");
            multipart.addFormField("addbbcode18", "#444444");
            multipart.addFormField("message", mensagem);
            
            response = multipart.finish();
            
            doc = Jsoup.parse(response);
            
            Elements parseError = doc.select("div[class=msg error-msg]");
            
            if(parseError.size() > 0)
                ResultMsg = parseError.get(0).text().replace("\\t", "");
            else if(response.indexOf("META HTTP-EQUIV=\"Refresh\"") != -1) {
                ResultMsg = "Usuário não logado!";
            }
            else
                ResultMsg = SuccessMsg;
            
            
            
        } catch (IOException ex) {
            
        }
    }
    
    private void getToken() {
        try {
            
            
            URL urlTopics = new URL("http://forum.jogos.uol.com.br/inbox.jbb");
            HttpURLConnection conMPs = (HttpURLConnection)urlTopics.openConnection();
            
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

            Elements parseToken = doc.select("input[name=token]");
            
            if(parseToken.size() > 0)
                token = parseToken.get(0).attr("value");
            else
                token = "";    
            
            
        }catch (IOException e) {
            
        }
    }
    
    private class AsyncJob extends AsyncTask<Void, Void, Void> {
        
        private String msg; //mensagem
        private String tit; //titulo
        private String user; //username
        private int tarefa;
        
        AsyncJob(int t) {
            tarefa = t;
        }
    
        @Override
        protected void onPreExecute() {
            
            isBusy = true;
            
            if(tarefa == 2) {
                msg = new String(((EditText)(findViewById(R.id.criar_topico_mensagem))).getText().toString());
                tit = new String(((EditText)(findViewById(R.id.criar_topico_titulo))).getText().toString());
                user = new String(((EditText)(findViewById(R.id.criar_topico_dest))).getText().toString());
            }
            
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            
            if(tarefa == 2)
                EnviarMensagem(tit, msg, user);
            
            if(tarefa == 1)
                getToken();
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            isBusy = false;
            
            if(tarefa == 2) {
                Toast toast = Toast.makeText(getApplicationContext(), ResultMsg, Toast.LENGTH_LONG);
                toast.show();
                
                if(ResultMsg.equals(SuccessMsg)) {
                    
                    EditText ed = (EditText) findViewById(R.id.criar_topico_mensagem);
                    ed.setText("");
                    
                    ed = (EditText) findViewById(R.id.criar_topico_titulo);
                    ed.setText("");
                    
                    setResult(AppCompatActivity.RESULT_OK);
                    finish(); //finaliza atividade e retorna para a atividade pai
                }
            }
        }
    }
}