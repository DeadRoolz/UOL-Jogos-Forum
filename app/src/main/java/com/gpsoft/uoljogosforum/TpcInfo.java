/*
* Classe usada para armazenar informações dos tópicos
*/

package com.gpsoft.uoljogosforum;

public class TpcInfo {
    
    private String Autor;
    private String Titulo;
    private String Estralas;
    private String NumRespostas;
    private String Link;
    private String DataUltimaMsg;
    private String AutorUltmMsg;
    
    public TpcInfo(String autor, String titulo, String estralas, String numrespostas, String link, String dataultimamsg, String autorultmmsg) {
        Autor = autor;
        Titulo = titulo;
        Estralas = estralas;
        NumRespostas = numrespostas;
        Link = link;
        DataUltimaMsg = dataultimamsg;
        AutorUltmMsg = autorultmmsg;
    }
    
    public String getAutor() {
        return Autor;
    }
    
    
    public String getTitulo() {
        return Titulo;
    }
    
    public String getEstrelas() {
        return Estralas;
    }
    
    public String getId() {
        return Link.substring(Link.lastIndexOf("_t_") + 3);
    }
    
    public String getDataUltimaMsg() {
        return DataUltimaMsg;
    }
    
    public String getNumRespostas() {
        return NumRespostas;
    }
    
    public String getLink() {
        return Link;
    }
    
    public String getAutorUtlmMsg() {
        return AutorUltmMsg;
    }
}
