/* 
* Activity que permite ao usuário criar tópicos
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
import android.widget.ProgressBar;


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


public class CriarTopicoActivity extends AppCompatActivity {
    
    
    private String SuccessMsg = new String("Sucesso!");
    private String ResultMsg = new String();
    private boolean isBusy = false;
    private AlertDialog EmoticonsPicker;
    private View SeletorEmoticonsView;
    private EmoticonsPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private ProgressBar Progress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_topico);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.criar_topico_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        getSupportActionBar().setTitle("Novo tópico - " + getIntent().getStringExtra("FORUM_TITULO"));
        
        //Constroi caixa de dialogo para selecionar emoticons e bbcodes
        SeletorEmoticonsView = getLayoutInflater().inflate(R.layout.seletor_de_emoticons, null);
        mPager = (ViewPager) SeletorEmoticonsView.findViewById(R.id.pager);
        mPagerAdapter = new EmoticonsPagerAdapter(this, (EditText)findViewById (R.id.criar_topico_mensagem));
        mPager.setAdapter(mPagerAdapter);
        EmoticonsPicker = (new AlertDialog.Builder(this, 0)).setView(SeletorEmoticonsView).create();
        
        Progress = (ProgressBar) findViewById(R.id.progressBar);
        
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_criar_topico, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if(isBusy)
            return false;
        
        switch (item.getItemId()) {
            
            case R.id.action_enviar_topico:
                AsyncJob criarTopico = new AsyncJob();
                criarTopico.execute();
                return true;
                
            case R.id.action_insert_emoticon:
                EmoticonsPicker.show();

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
    
    private String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("\\\"", "\"");
        return data_json;
    }
    
    private void PostarNovoTopico(String titulo, String mensagem) {
        
        
        if ((mensagem.replaceAll("\\s","").equals("")) || (mensagem.length() < 2)) {
            ResultMsg = new String("Erro:" + "Mensagem Inválida/Caracteres insuficientes");
            return;
        }
        
        if ((titulo.replaceAll("\\s","").equals("")) || (titulo.length() < 2)) {
            ResultMsg = new String("Erro:" + "Titulo Inválido/Caracteres insuficientes");
            return;
        }
        
        String ck = new String("");
            
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
            ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
        }
            
        
        try {

            URL urlInsertTopic = new URL("http://forum.jogos.uol.com.br/dwr/call/plaincall/PostFunctions.insertTopic.dwr");
            HttpURLConnection conInsertTopic = (HttpURLConnection)urlInsertTopic.openConnection();
            
            
            conInsertTopic.setRequestProperty("Accept", "*/*");
            conInsertTopic.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conInsertTopic.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conInsertTopic.setRequestProperty("Cache-Control", "max-age=0");
            conInsertTopic.setRequestProperty("Connection", "keep-alive");
            conInsertTopic.setRequestProperty("Cookie", ck);
            conInsertTopic.setRequestProperty("Content-Type", "text/plain");
            conInsertTopic.setRequestProperty("Host", "forum.jogos.uol.com.br");
            conInsertTopic.setRequestProperty("Origin", "http://forum.jogos.uol.com.br");
            conInsertTopic.setRequestProperty("Referer", "http://forum.jogos.uol.com.br");
            conInsertTopic.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conInsertTopic.setRequestMethod("POST");
            String jsessionid = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                
                if (cookie.getName().equals("JSESSIONID"))
                        jsessionid = new String(cookie.getValue());

            }
            
            String forumUrl = new String(getIntent().getStringExtra("FORUM_URL"));
            String urlParameters = "callCount=1&page=" 
                                    + forumUrl 
                                    + "&httpSessionId=" 
                                    + jsessionid 
                                    + "&scriptSessionId=59C5049A0FA43D7DE4AB0BE8BEEE9F10669&c0-scriptName=PostFunctions&c0-methodName=insertTopic&c0-id=0&c0-param0=number:" 
                                    + forumUrl.substring(forumUrl.indexOf("_f_") + 3) 
                                    + "&c0-param1=string:" 
                                    + URLEncoder.encode(titulo.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B").replaceAll("\\*", "%2A"))
                                    + "&c0-param2=string:" 
                                    + URLEncoder.encode(mensagem.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B").replaceAll("\\*", "%2A"))
                                    + "&batchId=1";

            conInsertTopic.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conInsertTopic.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            conInsertTopic.getContent();
            
            Reader reader = null;
            StringWriter writer = null;
            reader = new InputStreamReader(conInsertTopic.getInputStream(), "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();
            
            String responseFunction = response.substring(response.indexOf("Callback")+8, response.length());
            String responsejson = responseFunction.substring(responseFunction.indexOf("{"), responseFunction.indexOf("}") + 1);
            
            
            JSONObject json = new JSONObject(convertStandardJSONString(responsejson));
            
            if(json.getBoolean("error")) {
                ResultMsg = new String("Erro: " + json.getJSONArray("messages").getString(0));
                return;
            }
            else {
                ResultMsg = SuccessMsg;
                return;
            }
            
            
        } catch (IOException e) {
            
        } catch (JSONException e) {
            
        }
    }
    
    
    private class AsyncJob extends AsyncTask<Void, Void, Void> {
        
        private String msg; //mensagem
        private String tit; //titulo
    
        @Override
        protected void onPreExecute() {
            
            isBusy = true;
            
            Progress.setVisibility(View.VISIBLE);
            
            msg = new String(((EditText)(findViewById(R.id.criar_topico_mensagem))).getText().toString());
            tit = new String(((EditText)(findViewById(R.id.criar_topico_titulo))).getText().toString());
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            
            PostarNovoTopico(tit, msg);
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            isBusy = false;
            
            Progress.setVisibility(View.GONE);
            
            Toast toast = Toast.makeText(getApplicationContext(), ResultMsg, Toast.LENGTH_LONG);
            toast.show();
            
            if(ResultMsg.equals(SuccessMsg)) {
                
                EditText ed = (EditText) findViewById(R.id.criar_topico_mensagem);
                ed.setText("");
                
                ed = (EditText) findViewById(R.id.criar_topico_titulo);
                ed.setText("");
                
                setResult(AppCompatActivity.RESULT_OK);
                finish();
            }
        }
    }
}