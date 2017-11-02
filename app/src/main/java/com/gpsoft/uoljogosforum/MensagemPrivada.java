/*
* Classe que armazenas informações sobre associated
* mensagens particulares
*/


package com.gpsoft.uoljogosforum;

import android.text.Spannable;

public class MensagemPrivada {
    
    private String Titulo;
    private String Link;
    private String Data;
    private boolean msgLida;
    private String Texto;
    private UserInfo Autor;
    private boolean inOut; //in == false | out == true
    private boolean Checked = false;
    
    public MensagemPrivada(UserInfo autor, String titulo, String link, String data, String texto, boolean msglida, boolean inout) {
        Autor = autor;
        Titulo = titulo;
        Link = link;
        Data = data;
        msgLida = msglida;
        Texto = texto;
        inOut = inout;
    }
    
    public UserInfo getAutor() {
        return Autor;
    }
    
    public void setChecked(boolean chkd) {
        Checked = chkd;
    }
    
    public boolean isChecked() {
        return Checked;
    }
    
    public String getId() {
        return Link.split("pm\\.id\\=")[1];
    }
    
    public String getTexto() {
        return Texto;
    }
    
    public String getTitulo() {
        return Titulo;
    }
    
    public String getLink() {
        return Link;
    }
    
    public boolean getinOut() {
        return inOut;
    }
    
    public String getDataEnvio() {
        return Data;
    }
    
    public boolean foiLida() {
        return msgLida;
    }
}
