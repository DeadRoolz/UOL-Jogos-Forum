/*
* Classe utilizada para executar 
* as funçoes server-side do fórum que manipulam 
* postagens dos usuários
*/


package com.gpsoft.uoljogosforum;

//android imports
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.view.View;


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


public final class PostFunctions {
    private final static String callCount = "1";
    private final static String page = "";
    private final static String scriptSessionId = "DA72E62362FF117450C55A65293C216469";
    private final static String c0_scriptName = "PostFunctions";
    private final static String batchId = "0";
    private static String SuccessMsg = new String("Sucesso!");
    private static String ResultMsg = new String();
    private static boolean isBusy = false;
    private static String methodName = "";
    private static EditText edText;
    private static boolean Quote;
    private static ProgressBar progress;
    private static String mPostId = "";
    
    
    private static void execute(String tpclink, PostFunctionsParameters functionParameters, String methodname, Context context) {
        
        if (isBusy)
            return;
        
        methodName = methodname;
        
        String jsessionid = new String("");
            
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
            
            if (cookie.getName().equals("JSESSIONID"))
                jsessionid = new String(cookie.getValue());

        }
        
        
        String parameters = "callCount=" + callCount 
                          + "&page=" + tpclink
                          + "&httpSessionId=" + jsessionid
                          + "&scriptSessionId=" + scriptSessionId
                          + "&c0-scriptName=" + c0_scriptName
                          + "&c0-methodName=" + methodName
                          + "&c0-id=" + "0"
                          + functionParameters.toString()
                          + "&batchId=" + batchId;
        
        String url = "http://forum.jogos.uol.com.br/dwr/call/plaincall/PostFunctions." + methodName + ".dwr";
        AsyncJob callFunction = new AsyncJob(url, parameters, context);
        
        callFunction.execute();
        
        return;
    }
    
    
    public static void ObterPost(String PostId, String TopicUrl, EditText ed, Context c, boolean quote, ProgressBar pr) {
        
        progress = pr;
        
        Quote = quote;
        
        PostFunctionsParameters params = new PostFunctionsParameters();
        
        edText = ed;
        
        params.add("string", PostId);
        
        execute(TopicUrl, params, "getPost", c);
    }
    
    public static void InserirPost(String TopicId, String TopicUrl, String post, Context c, ProgressBar pr) {
        
        progress = pr;
        
        PostFunctionsParameters params = new PostFunctionsParameters();
        
        params.add("number", TopicId);
        params.add("string", post);
        
        execute(TopicUrl, params, "insertPost", c);
    }
    
    public static void AvaliarTopico(String TopicId, String TopicUrl, String NumEstrelas, Context c, ProgressBar pr) {
        
        progress = pr;
        
        PostFunctionsParameters params = new PostFunctionsParameters();
        
        params.add("number", TopicId);
        params.add("string", NumEstrelas);
        mPostId = TopicId;
        
        execute(TopicUrl, params, "evaluateTopic", c);

    }
    
    public static void AvaliarPost(String TopicId, String TopicUrl, String NumEstrelas, Context c, ProgressBar pr) {
        
        progress = pr;
        
        PostFunctionsParameters params = new PostFunctionsParameters();
        
        params.add("string", TopicId);
        params.add("number", NumEstrelas);
        mPostId = TopicId;
        
        execute(TopicUrl, params, "evaluatePost", c);

    }
    
    private static String convertStandardJSONString(String data_json) {
        
        return data_json.replaceAll("\\\\\"", "\"").replaceAll("\\\\\\\\n", "\\\\n").replaceAll("\\\\\\\\/", "\\/").replaceAll("\\\\\\\\\"", "\\\\\"");
    }
    
    private static void callPostFunction(String FunctionUrl, String Parameters) {
        String ck = new String("");
            
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
            ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
        }
            
        ResultMsg = "";
        
        try {

            URL urlFunction = new URL(FunctionUrl);
            HttpURLConnection conFunction = (HttpURLConnection)urlFunction.openConnection();
            
            
            conFunction.setRequestProperty("Accept", "*/*");
            conFunction.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conFunction.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conFunction.setRequestProperty("Cache-Control", "max-age=0");
            conFunction.setRequestProperty("Connection", "keep-alive");
            conFunction.setRequestProperty("Cookie", ck);
            conFunction.setRequestProperty("Content-Type", "text/plain");
            conFunction.setRequestProperty("Host", "forum.jogos.uol.com.br");
            conFunction.setRequestProperty("Origin", "http://forum.jogos.uol.com.br");
            conFunction.setRequestProperty("Referer", "http://forum.jogos.uol.com.br");
            conFunction.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conFunction.setRequestMethod("POST");
            
            String urlParameters = Parameters;

            conFunction.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conFunction.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            conFunction.getContent();
            
            Reader reader = null;
            StringWriter writer = null;
            reader = new InputStreamReader(conFunction.getInputStream(), "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();
            
            
            if(FunctionUrl.equals("http://forum.jogos.uol.com.br/dwr/call/plaincall/PostFunctions.evaluatePost.dwr") || 
            FunctionUrl.equals("http://forum.jogos.uol.com.br/dwr/call/plaincall/PostFunctions.evaluateTopic.dwr")) {
                    
                ResultMsg = response.substring(response.lastIndexOf(",")+1, response.lastIndexOf(");")).replaceAll("\"", "");
                
            }
            else if(FunctionUrl.equals("http://forum.jogos.uol.com.br/dwr/call/plaincall/PostFunctions.getPost.dwr")) {
                String responseFunction = response.substring(response.indexOf("Callback")+8, response.length());
                String responsejson = responseFunction.substring(responseFunction.indexOf("{"), responseFunction.lastIndexOf("}") + 1);
                JSONObject json = new JSONObject(convertStandardJSONString(responsejson));
                
                if(Quote)
                    ResultMsg = new String("[quote=\"" + json.getString("userName") + "\"]" + json.getString("postMessage") + "[/quote]\n\n");
                else
                    ResultMsg = new String(json.getString("postMessage"));
            }
            else {
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
            }
            
            
        } catch (IOException e) {
            
        } catch (JSONException e) {
            
        }
    }
    
    private static class AsyncJob extends AsyncTask<Void, Void, Void> {
        private String Url;
        private String Parm;
        private Context mContext;
        
        
        AsyncJob(String url, String parameters, Context c) {
            Url = url;
            Parm = parameters;
            mContext = c;
        }
        
        @Override
        protected void onPreExecute() {
            
            isBusy = true;
            
            progress.setVisibility(View.VISIBLE);
            
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            callPostFunction(Url, Parm);
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            
            isBusy = false;
            
            progress.setVisibility(View.GONE);
            
            if(methodName.equals("getPost")) {
                if(Quote)
                    edText.append(ResultMsg);
                else
                    edText.setText(ResultMsg);
                return;
            }
            else if(methodName.equals("evaluateTopic") || methodName.equals("evaluatePost")) {
                if(ResultMsg.equals("|" + mPostId)) {
                    ResultMsg = "Seu voto foi computado!";
                }
            }
            
            Toast toast = Toast.makeText(mContext, ResultMsg, Toast.LENGTH_LONG);
            toast.show();
            
        }
    }
}