/*
* Activity principal que exibe a tela inicial(lista dos subforums)
*/


package com.gpsoft.uoljogosforum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MainMenuAdapter(this));
        
        
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                
                String ForumLink = null;
                
                switch(position) {
                    
                    case 0:
                        ForumLink = "http://forum.jogos.uol.com.br/noticias_f_56";
                        break;
                        
                    case 1:
                        ForumLink = "http://forum.jogos.uol.com.br/ds-wii-wii-u_f_39";
                        break;
                        
                    case 2:
                        ForumLink = "http://forum.jogos.uol.com.br/pc_f_40";
                        break;
                        
                    case 3:
                        ForumLink = "http://forum.jogos.uol.com.br/playstation-4-playstation-3-ps-vita_f_41";
                        break;
                        
                    case 4:
                        ForumLink = "http://forum.jogos.uol.com.br/xbox-one-xbox-360_f_43";
                        break;
                    
                    case 5:
                        ForumLink = "http://forum.jogos.uol.com.br/museu-do-videogame_f_44";
                        break;
                    
                    case 6:
                        ForumLink = "http://forum.jogos.uol.com.br/vale-tudo_f_57";
                        break;
                    
                    default:
                        return;
                        
                }
                
                AbrirForum(ForumLink, (String)((TextView)(v.findViewById(R.id.ForumDesc))).getText());
            }
        });
    }
    
    private void AbrirForum(String url, String TituloDoForum) {
        Intent intent = new Intent(this, MostrarForumActivity.class);
        intent.putExtra("FORUM_URL", url);
        intent.putExtra("FORUM_TITULO", TituloDoForum);
        startActivity(intent);
    }
}