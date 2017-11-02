/*
* Activity que exibe os tópicos em que 
* o usuário participou e/ou criou.
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
import android.support.v4.widget.SwipeRefreshLayout;


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


public class MostrarMeusTopicosActivity extends AppCompatActivity {
    
    
    private ListView modeList;
    private CustomAdapter modeAdapter;
    private ArrayList<TpcInfo> tpcs = new ArrayList<TpcInfo>();
    private boolean isBusy = false;
    private Document doc;
    private String meusTopicosPageUrl = "";
    private View SeletorPagView;
    private int lastPageNumber = 1;
    private int actualPageNumber = 1;
    private AlertDialog PagePicker;
    private Menu optionsMenu = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_meustopicos);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.meustopicos_toolbar);
        setSupportActionBar(myToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        getSupportActionBar().setTitle("Meus Tópicos");
        
        modeAdapter = new CustomAdapter(this,  android.R.layout.simple_list_item_1, tpcs, true);
        modeList = (ListView)findViewById(R.id.ListaDeMeusTopicos);
        
        meusTopicosPageUrl = getIntent().getStringExtra("MEUSTOPICOS_PAGE_URL");
        
        //Constrói caixa de dialogo para selecionar paginas
        SeletorPagView = getLayoutInflater().inflate(R.layout.seletor_de_paginas, null);
        PagePicker = (new AlertDialog.Builder(this, 0)).setView(SeletorPagView).create();
        
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange);
        
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        
            @Override
            public void onRefresh() {
                RefreshMeusTopicos();
            }
        });
        
        RefreshMeusTopicos();
        
    }
    
    public void RefreshMeusTopicos() {
        if(isBusy)
            return;
        
        modeList.setAdapter(null);
        mSwipeRefreshLayout.setRefreshing(true);
        modeList.setEnabled(false);
        BaixarTopicos baixarTpcs = new BaixarTopicos(actualPageNumber);
        baixarTpcs.execute();
    }
    
    
    public void PagDialogClickListener(View v) {
        PagePicker.dismiss();
        
        if(isBusy)
            return;
        
        if (v.getId() == R.id.pag_dialog_go) {
            NumberPicker np =  (NumberPicker)SeletorPagView.findViewById(R.id.numberPicker1);
            
            if (np.getValue() == actualPageNumber)
                return;
            
            actualPageNumber = np.getValue();
            RefreshMeusTopicos();
        }
    }
    
    public void AtualizarInterface() {
        getSupportActionBar().setSubtitle("Pág.: " + actualPageNumber + " / " + lastPageNumber);
        
        NumberPicker np =  (NumberPicker)SeletorPagView.findViewById(R.id.numberPicker1);
        
        np.setMinValue(1);
        np.setMaxValue(lastPageNumber);
        np.setValue(actualPageNumber);
        
        if(optionsMenu == null)
            return;
        
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
    public boolean onPrepareOptionsMenu (Menu menu) {
        if(optionsMenu == null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.opcoes_meus_topicos, menu);
            optionsMenu = menu;
        }
        return true;
    }
    
    private void ListarTopicos(String pagina) {
        
        if (meusTopicosPageUrl.equals("")) {
            return;
        }
        
        
        try {
            
            URL urlMyTopics = new URL("http://forum.jogos.uol.com.br" + meusTopicosPageUrl + "page=" + pagina);
            
            HttpURLConnection conMyTopics = (HttpURLConnection)urlMyTopics.openConnection();
            
            String ck = new String("");
            
            for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
                ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
            }
            
            //Setando Request Headers
            conMyTopics.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conMyTopics.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conMyTopics.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
            conMyTopics.setRequestProperty("Connection", "keep-alive");
            conMyTopics.setRequestProperty("Cookie", ck);
            conMyTopics.setRequestProperty("Host", "forum.jogos.uol.com.br");
            conMyTopics.setRequestProperty("Referer", "http://forum.jogos.uol.com.br/userMessages.jbb");
            conMyTopics.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conMyTopics.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

            conMyTopics.getContent();


            Reader reader = null;
            StringWriter writer = null;
            InputStream gzis = new GZIPInputStream(conMyTopics.getInputStream());
            reader = new InputStreamReader(gzis, "UTF-8");
            writer = new StringWriter();

            char[] buffer = new char[1024];

            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            
            String response = writer.toString();

            doc = Jsoup.parse(response);
            
            Elements tpcsRow = doc.select("table[id=topics] tbody tr");
            Elements pageCount = doc.select("img[class=master-sprite sprite-last]");
            Elements actualPage = doc.select("span[class=actualPage]");
            
            lastPageNumber = 1;
            actualPageNumber = 1;
            
            if(actualPage.size() == 0)
                return;
                
            if(pageCount.size() == 0)
                lastPageNumber = actualPageNumber = Integer.parseInt(actualPage.get(0).text().replaceAll("[\\D]", ""));
            else {
                actualPageNumber = Integer.parseInt(actualPage.get(0).text().replaceAll("[\\D]", ""));
                lastPageNumber = Integer.parseInt(pageCount.get(0).parent().attr("href").split("\\&page\\=")[1]);
            }
            
            tpcs.clear();

            for(int count = 0; count < tpcsRow.size(); count++) {
                
                TpcInfo tpc = new TpcInfo(tpcsRow.get(count).child(2).child(0).text(), 
                            tpcsRow.get(count).child(1).child(0).child(0).attr("title"), 
                            "", 
                            tpcsRow.get(count).child(3).text().replaceAll("\\x09", ""), 
                            tpcsRow.get(count).child(1).child(0).child(0).attr("href"),
                            tpcsRow.get(count).child(4).text().split("Por:")[0].replaceAll("\\x09", ""), 
                            tpcsRow.get(count).child(4).text().split("Por:")[1].replaceAll("\\x09", ""));
                

                tpcs.add(tpc);

            }
        }catch (IOException e) {
                e.printStackTrace();
        }
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if(isBusy)
            return false;
        
        switch (item.getItemId()) {
            
            case R.id.action_navegar_pag:
                PagePicker.show();
                return true;

                
            case R.id.action_proximo:
                if(actualPageNumber < lastPageNumber)
                    actualPageNumber++;
                
                RefreshMeusTopicos();
                return true;
                
            case R.id.action_anterior:
                if(actualPageNumber > 0)
                    actualPageNumber--;
                
                RefreshMeusTopicos();
                return true;
            

            default:
                return super.onOptionsItemSelected(item);
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
    
    //Abre o topico
    public void AbrirTopico(int Idx) {
        
        String TpcURL = tpcs.get(Idx).getLink();
        String TpcTitulo = tpcs.get(Idx).getTitulo();
        
        Intent intent = new Intent(this, MostrarTopicoActivity.class);
        intent.putExtra("TOPICO_URL", TpcURL);
        intent.putExtra("TOPICO_TITULO", TpcTitulo);
        
        String ck = new String("");
        
        for (HttpCookie cookie : CookieForum.getCookieManager().getCookieStore().getCookies()) {
            
            ck = new String(ck + cookie.getName() + "=" + cookie.getValue() + ";");
        }
        
        
        startActivity(intent);

        return;
    }
    
    private class BaixarTopicos extends AsyncTask<Void, Void, Void> {
        
        private String pagina;
        
        @Override
        protected void onPreExecute() {
            isBusy = true;
            super.onPreExecute();
        }
        
        BaixarTopicos(int p) {
            pagina = Integer.toString(p);
        }
 
        @Override
        protected Void doInBackground(Void... params) {
           ListarTopicos(pagina);
           return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            
            isBusy = false;
            AtualizarListaDeTopicos();
            AtualizarInterface();
        }
    }
}