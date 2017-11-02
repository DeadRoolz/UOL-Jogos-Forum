/*
* Activity que exibe as respostas do tópico selecionado
*/

package com.gpsoft.uoljogosforum;

//android imports
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.support.v7.widget.*;
import android.os.AsyncTask;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spannable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.support.design.widget.BottomNavigationView;
import android.app.AlertDialog;
import android.widget.NumberPicker;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
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

public class MostrarTopicoActivity extends AppCompatActivity {
    
    private ListView ListViewRespostas;
    private TextView txtView_TituloDoTopico;
    private ArrayList<PostsInfo> posts;
    private ArrayList<PostsInfo> postsToAppend;
    private String SuccessMsg = new String("Sucesso!");
    private String ResultMsg = new String();
    private boolean isBusy = false;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int lastPageNumber = 1;
    private int actualPageNumber = 1;
    private AlertDialog PagePicker;
    private AlertDialog EmoticonsPicker;
    private AlertDialog AvaliarTopicoDialog;
    private View SeletorPagView;
    private View SeletorEmoticonsView;
    private View AvaliarTopicoView;
    private String TpcURL;
    private Menu optionsMenu;
    private ViewPager mPager;
    private EmoticonsPagerAdapter mPagerAdapter;
    private String NumEstrelas = "";
    private ProgressBar Progress;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_topico);

        Intent intent = getIntent();
        TpcURL = intent.getStringExtra("TOPICO_URL");
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.topico_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //Setando titulo do tópico
        getSupportActionBar().setTitle(intent.getStringExtra("TOPICO_TITULO"));
        
        
        Progress = (ProgressBar) findViewById(R.id.progressBar);
        
        //Referenciando ListView das Respostas
        mRecyclerView = (RecyclerView) findViewById(R.id.ListaDeRespostas);
        
        //ListViewRespostas.setOnScrollListener(new EndlessScrollListener());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        posts = new ArrayList<PostsInfo>();
        mAdapter = new MyAdapter(posts, getIntent().getStringExtra("TOPICO_TITULO"), (EditText)findViewById(R.id.resposta_topico), Progress);
        mRecyclerView.setAdapter(mAdapter);
        
        postsToAppend = new ArrayList<PostsInfo>();
        
        //Constroi caixa de dialogo para selecionar emoticons e bbcodes
        SeletorEmoticonsView = getLayoutInflater().inflate(R.layout.seletor_de_emoticons, null);
        mPager = (ViewPager) SeletorEmoticonsView.findViewById(R.id.pager);
        mPagerAdapter = new EmoticonsPagerAdapter(this, (EditText)findViewById (R.id.resposta_topico));
        mPager.setAdapter(mPagerAdapter);
        EmoticonsPicker = (new AlertDialog.Builder(this, 0)).setView(SeletorEmoticonsView).create();
        
        //Constroi caixa de dialogo para avaliar topico
        AvaliarTopicoView = getLayoutInflater().inflate(R.layout.avaliar_topico, null);
        AvaliarTopicoDialog = (new AlertDialog.Builder(this, 0)).setView(AvaliarTopicoView).create();
        
        
        //Constrói caixa de dialogo para selecionar paginas
        SeletorPagView = getLayoutInflater().inflate(R.layout.seletor_de_paginas, null);
        PagePicker = (new AlertDialog.Builder(this, 0)).setView(SeletorPagView).create();
        
        //setando onclick listener do botao enviar
        FloatingActionButton floatingActionButton =
    (FloatingActionButton) findViewById(R.id.botao_enviar_resp);
    
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BotaoEnviarRespostaListener();
        }
    });
        
        
        //baixa respostas do topico e adiciona ao listview
        carregarTopico(TpcURL, 1);
        
    }
    
    
    public void BotaoFecharSeletorEmoticonsListener(View v) {
        EmoticonsPicker.dismiss();
    }
    
    public void OnBBCodeClickListener(View v) {
        
        EditText respostaEditText = (EditText)findViewById (R.id.resposta_topico);
        respostaEditText.append("[" + ((Button)v).getText() + "]" + "  " + "[/" + ((Button)v).getText() + "]");
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
    }
    
    public void carregarTopico(String tpcURL, int page) {
        
        if(isBusy)
            return;
        
        Progress.setVisibility(View.VISIBLE);
        
        mRecyclerView.setAdapter(null);
        
        AsyncJob job = new AsyncJob(1, tpcURL, page);
        job.execute();
    }
    
    public void openEmoticonsDialog(View v) {
        EmoticonsPicker.show();
    }
    
    public void ratingContainerClickListener(View v) {
        
        AvaliarTopicoDialog.show();
    }
    
    public void BotaoFecharAvaliarTopicosListener(View v) {
        
        AvaliarTopicoDialog.dismiss();
    }
    
    public void EnviarAvaliacaoClickListener(View v) {
        
        
        if(NumEstrelas.equals("")) {
            TextView desc = (TextView)AvaliarTopicoDialog.findViewById(R.id.txtDesc);
            desc.setText("Marque o número de estrelas");
            return;
        }
        
        AvaliarTopicoDialog.dismiss();
        
        PostFunctions.AvaliarTopico(getIntent().getStringExtra("TOPICO_ID"),
                                  getIntent().getStringExtra("TOPICO_URL"),
                                  NumEstrelas,
                                  this, Progress);
    }
    
    public void EstrelaClickListener(View v) {
        
        ImageView star1 = (ImageView)AvaliarTopicoDialog.findViewById(R.id.imageView_Estrela1);
        ImageView star2 = (ImageView)AvaliarTopicoDialog.findViewById(R.id.imageView_Estrela2);
        ImageView star3 = (ImageView)AvaliarTopicoDialog.findViewById(R.id.imageView_Estrela3);
        ImageView star4 = (ImageView)AvaliarTopicoDialog.findViewById(R.id.imageView_Estrela4);
        ImageView star5 = (ImageView)AvaliarTopicoDialog.findViewById(R.id.imageView_Estrela5);
        
        TextView desc = (TextView)AvaliarTopicoDialog.findViewById(R.id.txtDesc);
        
        if(star1 == null ||
           star2 == null ||
           star3 == null ||
           star4 == null ||
           star5 == null)
                return;
        
        
        switch(v.getId()) {
            case R.id.imageView_Estrela1:
                star1.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star2.setImageResource(R.drawable.ic_star_border_black_36dp);
                star3.setImageResource(R.drawable.ic_star_border_black_36dp);
                star4.setImageResource(R.drawable.ic_star_border_black_36dp);
                star5.setImageResource(R.drawable.ic_star_border_black_36dp);
                desc.setText("Ruim");
                NumEstrelas = "5";
                break;
                
            case R.id.imageView_Estrela2:
                star1.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star2.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star3.setImageResource(R.drawable.ic_star_border_black_36dp);
                star4.setImageResource(R.drawable.ic_star_border_black_36dp);
                star5.setImageResource(R.drawable.ic_star_border_black_36dp);
                desc.setText("Regular");
                NumEstrelas = "4";
                break;
                
            case R.id.imageView_Estrela3:
                star1.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star2.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star3.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star4.setImageResource(R.drawable.ic_star_border_black_36dp);
                star5.setImageResource(R.drawable.ic_star_border_black_36dp);
                desc.setText("Bom");
                NumEstrelas = "3";
                break;
                
            case R.id.imageView_Estrela4:
                star1.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star2.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star3.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star4.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star5.setImageResource(R.drawable.ic_star_border_black_36dp);
                desc.setText("Ótimo");
                NumEstrelas = "2";
                break;
                
            case R.id.imageView_Estrela5:
                star1.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star2.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star3.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star4.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                star5.setImageResource(R.drawable.ic_star_border_black_full_36dp);
                desc.setText("Excelente");
                NumEstrelas = "1";
                break;
                
            default:
                star1.setImageResource(R.drawable.ic_star_border_black_36dp);
                star2.setImageResource(R.drawable.ic_star_border_black_36dp);
                star3.setImageResource(R.drawable.ic_star_border_black_36dp);
                star4.setImageResource(R.drawable.ic_star_border_black_36dp);
                star5.setImageResource(R.drawable.ic_star_border_black_36dp);
                desc.setText("");
                NumEstrelas = "";
        }
    }
/*    
    private class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 7;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
            if (loading) {
                loading = false;
                return;
            }
            
            if (!loading && ((totalItemCount - firstVisibleItem) <= (visibleThreshold))) {
                
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
*/    
    //Refresh lista de topicos
    public void RefreshTopico() {
        carregarTopico(TpcURL, actualPageNumber);
    }
    
    public void PagDialogClickListener(View v) {
        PagePicker.dismiss();
        
        if(isBusy)
            return;
        
        if (v.getId() == R.id.pag_dialog_go) {
            NumberPicker np =  (NumberPicker)SeletorPagView.findViewById(R.id.numberPicker1);
            
            if (np.getValue() == actualPageNumber)
                return;
            
            mRecyclerView.setAdapter(null);
            carregarTopico(TpcURL, np.getValue());
        }
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_topico, menu);
        optionsMenu = menu;
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if(isBusy)
            return false;
        
        switch (item.getItemId()) {
            
            case R.id.action_navegar_pag:
                PagePicker.show();
                return true;
                
            case R.id.action_refresh_topic:
                RefreshTopico();
                return true;
                
            case R.id.action_proximo:
                mRecyclerView.setAdapter(null);
                carregarTopico(TpcURL, actualPageNumber+1);
                return true;
                
            case R.id.action_anterior:
                mRecyclerView.setAdapter(null);
                carregarTopico(TpcURL, actualPageNumber-1);
                return true;
            

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void BotaoEnviarRespostaListener() {
        AsyncJob job = new AsyncJob(2, "", 0);
        job.execute();
    }
    
    
    private String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("\\\"", "\"");
        return data_json;
    }
    
    
    //Envia resposta ao tópico
    public void enviarResposta(String resp) {
        
        
        
        if ((resp.replaceAll("\\s","").equals("")) || (resp.length() < 2)) {
            ResultMsg = new String("Erro:" + "Mensagem Inválida/Caracteres insuficientes");
            return;
        }
        
        String ck = new String("");
            
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
            ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
        }
        
        try {

            String methodName = "";
            String batchId = "";
            String param0Type = "";
            String param0 = "";
            
            if(mAdapter.isEditModeEnabled()) {
                methodName = "editPost";
                batchId = "1";
                param0Type = "string";
                param0 = mAdapter.getEditingPostId();
            }
            else {
                methodName = "insertPost";
                batchId = "0";
                param0Type = "number";
                String tpcurl = getIntent().getStringExtra("TOPICO_URL");
                param0 = tpcurl.substring(tpcurl.indexOf("_t_") + 3);
            }
            
            URL urlInsertPost = new URL("http://forum.jogos.uol.com.br/dwr/call/plaincall/PostFunctions." + methodName + ".dwr");
            HttpURLConnection conInsertPost = (HttpURLConnection)urlInsertPost.openConnection();
            
            
            conInsertPost.setRequestProperty("Accept", "*/*");
            conInsertPost.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conInsertPost.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conInsertPost.setRequestProperty("Cache-Control", "max-age=0");
            conInsertPost.setRequestProperty("Connection", "keep-alive");
            conInsertPost.setRequestProperty("Cookie", ck);
            conInsertPost.setRequestProperty("Content-Type", "text/plain");
            conInsertPost.setRequestProperty("Host", "forum.jogos.uol.com.br");
            conInsertPost.setRequestProperty("Origin", "http://forum.jogos.uol.com.br");
            conInsertPost.setRequestProperty("Referer", "http://forum.jogos.uol.com.br");
            conInsertPost.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conInsertPost.setRequestMethod("POST");
            String jsessionid = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                
            if (cookie.getName().equals("JSESSIONID"))
                    jsessionid = new String(cookie.getValue());

            }
            
            
            String tpcurl = new String();
            String urlParameters = "callCount=1&page=" 
                                    + tpcurl 
                                    + "&httpSessionId=" 
                                    + jsessionid 
                                    + "&scriptSessionId=59C5049A0FA43D7DE4AB0BE8BEEE9F10669&c0-scriptName=PostFunctions&c0-methodName=" + methodName + "&c0-id=0&"
                                    + "c0-param0=" + param0Type + ":"
                                    + param0 
                                    + "&c0-param1=string:" 
                                    + URLEncoder.encode(resp.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B").replaceAll("\\*", "%2A"))
                                    + "&batchId=" + batchId;
            
            
            conInsertPost.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conInsertPost.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            conInsertPost.getContent();
            
            Reader reader = null;
            StringWriter writer = null;
            reader = new InputStreamReader(conInsertPost.getInputStream(), "UTF-8");
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
            // e.printStackTrace();
        } catch (JSONException e) {
            
        }
        
    }

    public void HideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), 0);
    }
    
    public void AtualizaListaRespostas(ArrayList<PostsInfo> posts, int tarefa) {
        
        mAdapter = new MyAdapter(posts, getIntent().getStringExtra("TOPICO_TITULO"), (EditText)findViewById(R.id.resposta_topico), Progress);        
        mRecyclerView.setAdapter(mAdapter);
        
    }
    
    private void BaixarRespostas(int pagina, String tpcLink, boolean append) {
        
        
        String ck = new String("");
            
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
            ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
        }
        
        try {
                
                URL urlTopics = new URL("http://forum.jogos.uol.com.br" + tpcLink + "?page=" + Integer.toString(pagina));
                HttpURLConnection conTopics = (HttpURLConnection)urlTopics.openConnection();
                
                //Setando Request Headers
                conTopics.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                conTopics.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
                conTopics.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
                conTopics.setRequestProperty("Connection", "keep-alive");
                conTopics.setRequestProperty("Cookie", ck);
                conTopics.setRequestProperty("Host", "forum.jogos.uol.com.br");
                conTopics.setRequestProperty("Referer", "http://forum.jogos.uol.com.br/vale-tudo_f_57/");
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

                Document doc = Jsoup.parse(response);

            
                Elements tpcsParseResps = doc.select("div[class=right post-buttons] div[class=texto]");
                Elements postCount = doc.select("div[class=left postCount] a");
                Elements TopicDate = doc.select("div[class=topic-date]");
                Elements leftPostUser = doc.select("div[class=left post-user]");
                Elements votos = doc.select("div[id=detalhes] div[class=autoClear caixa-titulo] div[class=left caixa-texto]");
                Elements leftPublishDate = doc.select("div[class=left publishDate]");
                Elements postsParseAutor = doc.select("p[class=userNickname] a"); //"li[class=votingResult] span"
                Elements postsParseRate = doc.select("li[class=votingResult]");
                Elements pageCount = doc.select("img[class=master-sprite sprite-last]");
                Elements actualPage = doc.select("span[class=actualPage]");
                Elements NumeroDeEstrelas = doc.select("div[id=detalhes]");
                Elements postContainer = doc.select("div[id=posts-container] > div");
                
                    
                
                if(postContainer.size() == 0)
                    return;
                
                
                lastPageNumber = 1;
                actualPageNumber = 1;
                
                if(pageCount.size() == 0)
                    lastPageNumber = actualPageNumber = Integer.parseInt(actualPage.get(0).text().replaceAll("[\\D]", ""));
                else {
                    actualPageNumber = Integer.parseInt(actualPage.get(0).text().replaceAll("[\\D]", ""));
                    lastPageNumber = Integer.parseInt(pageCount.get(0).parent().attr("href").split("\\?page\\=")[1]);
                }
                
                
            
                String imgURL = null;
                int firstSpace = 0; 
                
                
                postsToAppend.clear();
            
                for (int count = 0; count < postCount.size(); count++) {
                    
                    if (leftPostUser.get(count).getElementById("avatarImg") == null) {
                        imgURL = "http://forum.imguol.com//forum/themes/jogos/images/nopic.gif";
                    }
                    else {
                        imgURL = postContainer
                        .get(count).getElementsByAttributeValue("class", "left post-user")
                        .get(0).getElementsByAttributeValue("id", "avatarImg")
                        .get(0).attr("src");
                    }
                
                    while (imgURL.indexOf(' ') != -1) {
                        
                        firstSpace = imgURL.indexOf(' ');
                        imgURL = imgURL.substring(0, firstSpace) + "%20" + imgURL.substring(firstSpace+1, (imgURL.length()));
                    }
                    
                    String nickname = postContainer
                    .get(count).getElementsByAttributeValue("class", "left post-user")
                    .get(0).getElementsByAttributeValue("class", "userNickname")
                    .get(0).text();
                    
                    String datacadastro = postContainer.get(count)
                    .getElementsByAttributeValue("class", "left post-user")
                    .get(0).getElementsByAttributeValue("class", "descricao")
                    .get(0).getElementsByAttributeValue("class", "data-cadastro")
                    .get(0).text();
                    
                    String nummsgs = postContainer.get(count)
                    .getElementsByAttributeValue("class", "left post-user")
                    .get(0).getElementsByAttributeValue("class", "descricao")
                    .get(0).child(0).text();
                    
                    
                    String assinatura = "";
                    
                    Elements assinaturaDiv = postContainer.get(count)
                    .getElementsByAttributeValue("class", "right post-buttons").get(0)
                    .getElementsByAttributeValue("class", "post-assinatura");
                    
                    if(assinaturaDiv.size() > 0) {
                        
                        assinaturaDiv.get(0)
                        .getElementsByAttributeValue("class", "publishDate").get(0).remove();
                        
                        assinaturaDiv.get(0)
                        .getElementsByTag("br").get(0).remove();
                        
                        assinatura = assinaturaDiv.get(0).html();
                    }
                    
                    UserInfo user = new UserInfo(nickname, datacadastro,nummsgs,imgURL, assinatura);
                    
                    String post = postContainer.get(count)
                    .getElementsByAttributeValue("class", "right post-buttons").get(0)
                    .getElementsByAttributeValue("class", "texto").get(0).html();
                    
                    
                    //busca por videos do youtube embutidos no post e insere o endereço do video dentro da tag embed
                    Document postHtml = Jsoup.parse(post);
                    
                    Elements EmbededVideos = postHtml.select("embed");
                    
                    YouTubeVideo YTVideo = null;
                    
                    if(EmbededVideos.size() > 0) {
                        String embededUrl = EmbededVideos.get(0).attr("src");
                        
                        String videoId = embededUrl.replace("http://www.youtube.com/v/", "");
                        videoId = videoId.substring(0, videoId.indexOf('&'));
                        
                        URL urlYoutubeVideoInfo = new URL("https://www.googleapis.com/youtube/v3/videos?id=" + videoId + "&key=AIzaSyC-VMtltiNSIlecvDi3-jsm4jcB0mqX-Tc&part=snippet");
                        HttpURLConnection conYTInfoVideo = (HttpURLConnection)urlYoutubeVideoInfo.openConnection();
                        
                        conYTInfoVideo.getContent();
                        
                        reader = null;
                        writer = null;
                        reader = new InputStreamReader(conYTInfoVideo.getInputStream());
                        writer = new StringWriter();

                        buffer = new char[1024];

                        for (int length = 0; (length = reader.read(buffer)) > 0;) {
                            writer.write(buffer, 0, length);
                        }
                        
                        response = writer.toString();
                        
                        JSONObject json = new JSONObject(response);
                        
                        String videoTitle = json.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getString("title");
                        String thumbUrl = json.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                        
                        
                        YTVideo = new YouTubeVideo(videoId, videoTitle, thumbUrl);
                    }
                    
                    String postText = post;
                    
                    int pontoIndex = NumeroDeEstrelas.get(0).child(0).text().indexOf(".");
                    char parteInteira = NumeroDeEstrelas.get(0).child(0).text().charAt(pontoIndex-1);
                    char parteDecimal = NumeroDeEstrelas.get(0).child(0).text().charAt(pontoIndex+1);
                    
                    String postcount = postContainer.get(count)
                    .getElementsByAttributeValue("class", "right post-buttons").get(0)
                    .getElementsByAttributeValue("class", "left postCount").get(0).text().substring(1);
                    
                    String postDate = TopicDate.get(0).text();
                    String postrating = "";
                    String numvotos = null;
                    
                    if(postcount.equals("1")) {
                        postDate = TopicDate.get(0).text();
                        postrating = parteInteira + "." + parteDecimal;
                        numvotos = votos.get(0).text();
                    }
                    else {
                        postDate = postContainer.get(count)
                        .getElementsByAttributeValue("class", "right post-buttons").get(0)
                        .getElementsByAttributeValue("class", "autoClear post-buttons2").get(0)
                        .getElementsByAttributeValue("class", "left publishDate").get(0).text().split("Mensagem publicada em ")[1];
                        
                        postrating = postContainer.get(count)
                        .getElementsByAttributeValue("class", "right post-buttons").get(0)
                        .getElementsByAttributeValue("class", "autoClear post-buttons2").get(0)
                        .getElementsByAttributeValue("class", "right text-right").get(0)
                        .getElementsByAttributeValue("class", "votingResult").get(0).text();
                    }
                    
                    String postId = postContainer.get(count).attr("id");
                    
                    postsToAppend.add(new PostsInfo(user, 
                                    postText, 
                                    postrating,
                                    postcount, 
                                    postDate, 
                                    numvotos, 
                                    postId, 
                                    tpcLink,
                                    YTVideo));
                    
                    
            }

            } catch (IOException e) {
                
            } catch (JSONException e) {
                
            }
    }
    
    
    private class AsyncJob extends AsyncTask<Void, Void, Void> {
      
        private String tpcLink;
        private int tarefa;
        private int pagina;
        
        
        AsyncJob(int tar, String lnk, int pag) { //tarefa == 1 -> BaixarRespostas | tarefa == 2 -> ResponderTopico
            tpcLink = new String(lnk);
            pagina = pag;
            tarefa = tar;
        }
        
        @Override
        protected void onPreExecute() {
            if (tarefa == 2)
                HideKeyBoard();
            
            if (isBusy == true)
                tarefa = 0;
            
            Progress.setVisibility(View.VISIBLE);
            
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            
            
            if(tarefa == 1) {
                isBusy = true;
                BaixarRespostas(pagina, tpcLink, false);
            }
            
            if (tarefa == 2) {
                isBusy = true;
                enviarResposta(new String(((EditText)(findViewById(R.id.resposta_topico))).getText().toString()));
            }
            
            if (tarefa == 3) {
                isBusy = true;
                BaixarRespostas(pagina, tpcLink, true);
            }

            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            
            isBusy = false;
            
            Progress.setVisibility(View.GONE);
            
            EditText ed = (EditText) findViewById (R.id.resposta_topico);
            
            if ((tarefa == 1) || (tarefa == 3)) {
                ed.setText("");
                AtualizaListaRespostas(postsToAppend, tarefa);
                AtualizarInterface();
            }
            
            if (tarefa == 2) {
                Toast toast = Toast.makeText(getApplicationContext(), ResultMsg, Toast.LENGTH_LONG);
                toast.show();
                
                if(ResultMsg.equals(SuccessMsg)) {
                    ed.setText("");
                    String tpcurl = getIntent().getStringExtra("TOPICO_URL");
                    carregarTopico(tpcurl, lastPageNumber);
                }
            }
        }
    }
}