/*
* Armazena informações sobre o usuário atualmente logado
*/


package com.gpsoft.uoljogosforum;

//android imports 
import android.graphics.drawable.Drawable;


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


public final class Usuario {
    private static String Nickname = "";
    private static String Email = "";
    private static String AvatarUrl = "";
    private static String MpPageUrl = "";
    private static String MyTopicsPageUrl = "";
    private static boolean Logado = false;
    private static String Senha = "";
    private static Document doc;
    private static String NumeroMps = "0";
    
    public static void setInfos(String nickname, String email, String avatarurl, String mppageurl, String mytopicspageurl, String senha) {
        Nickname = nickname;
        Email = email;
        AvatarUrl = avatarurl;
        MpPageUrl = mppageurl;
        MyTopicsPageUrl = mytopicspageurl;
        Senha = senha;
    }
    
    public static void setSenha(String senha) {
        Senha = senha;
    }
    
    public static void setNumMps(String nummps) {
        NumeroMps = nummps;
    }
    
    public static String getNumMps() {
        return NumeroMps;
    }
    
    public static String getSenha() {
        return Senha;
    }
    
    public static void setEmail(String email) {
        Email = email;
    }
    
    public static void setNickName(String nickname) {
        Nickname = nickname;
    }
    
    public static String getNickName() {
        return Nickname;
    }
    
    public static String getEmail() {
        return Email;
    }
    
    public static String getAvatarUrl() {
        return AvatarUrl;
    }
    
    public static void setAvatarUrl(String avatarurl) {
        AvatarUrl = avatarurl;
    }
    
    public static String getMpPageUrl() {
        return MpPageUrl;
    }
    
    public static String getMyTopicsPageUrl() {
        return MyTopicsPageUrl;
    }
    
    public static boolean isLogged() {
        return Logado;
    }
    
    public static boolean Logar(String email, String senha) {
        
        
        try {
            
            URL urlForum = new URL("http://forum.jogos.uol.com.br");
            HttpURLConnection con = (HttpURLConnection)urlForum.openConnection();

            URL urlLogin= new URL("https://acesso.uol.com.br/login.html?skin=forum-jogos");
            HttpURLConnection conLogin = (HttpURLConnection)urlLogin.openConnection();
            
            //Realizando login
            conLogin.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conLogin.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conLogin.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conLogin.setRequestProperty("Cache-Control", "max-age=0");
            conLogin.setRequestProperty("Connection", "keep-alive");
            conLogin.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conLogin.setRequestProperty("Host", "acesso.uol.com.br");
            conLogin.setRequestProperty("Origin", "https://acesso.uol.com.br");
            conLogin.setRequestProperty("Referer", "https://acesso.uol.com.br/login.html?skin=forum-jogos");
            conLogin.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conLogin.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conLogin.setRequestMethod("POST");
            String urlParameters = "user="+email+"&pass="+senha+"&dest=REDIR|http://forum.jogos.uol.com.br/";

            conLogin.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conLogin.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            
            conLogin.getContent();
            
            Reader reader = null;
            StringWriter writer = null;
            reader = new InputStreamReader(conLogin.getInputStream(), "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();
            
            if(response.indexOf("msg alert") != -1){
                
                return false;
            }
            
            //Document doc = Jsoup.parse(response);
            

        }catch (IOException e) {
                e.printStackTrace();
                return false;
        }
        
        BaixarUserInfo();
        setEmail(email);
        setSenha(senha);
        setLogado(true);
        
        return true;
        
    }
    
    public static void Deslogar() {
        
        setLogado(false);
        
        Nickname = "";
        Email = "";
        AvatarUrl = "";
        MpPageUrl = "";
        MyTopicsPageUrl = "";
        Logado = false;
        String Senha = "";
        CookieForum.getCookieManager().getCookieStore().removeAll();
    }
    
    public static void setLogado(boolean logado) {
        Logado = logado;
    }
    
    private static void BaixarUserInfo() {
        try {
            
            URL url = new URL("http://forum.jogos.uol.com.br/");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            
            String ck = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
            }
            
            //Setando Request Headers
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            con.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Cookie", ck);
            con.setRequestProperty("Host", "forum.jogos.uol.com.br");
            con.setRequestProperty("Referer", "http://forum.jogos.uol.com.br/");
            con.setRequestProperty("Upgrade-Insecure-Requests", "1");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            con.connect();
            
            Reader reader = null;
            StringWriter writer = null;
            InputStream gzis = new GZIPInputStream(con.getInputStream());
            reader = new InputStreamReader(gzis, "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();

            doc = Jsoup.parse(response);
            Elements UserProfileLink = doc.select("a[class=menu-profile menu-buttons]");
            
            url = new URL("http://forum.jogos.uol.com.br" + UserProfileLink.attr("href"));
            con = (HttpURLConnection)url.openConnection();
            
            ck = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
            }
            
            //Setando Request Headers
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            con.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Cookie", ck);
            con.setRequestProperty("Host", "forum.jogos.uol.com.br");
            con.setRequestProperty("Referer", "http://forum.jogos.uol.com.br/");
            con.setRequestProperty("Upgrade-Insecure-Requests", "1");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
            
            con.connect();
            
            reader = null;
            writer = null;
            gzis = new GZIPInputStream(con.getInputStream());
            reader = new InputStreamReader(gzis, "UTF-8");
            writer = new StringWriter();

            buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            response = writer.toString();

            doc = Jsoup.parse(response);
            
            Elements UserName = doc.select("h1[class=userName]");
            Elements AvatarUrl = doc.select("img[id=avatarImg]");
            
            Usuario.setNickName(UserName.get(0).text());
            Usuario.setAvatarUrl(AvatarUrl.attr("src"));

            
        }catch (IOException e) {
                e.printStackTrace();
        }
    }
}