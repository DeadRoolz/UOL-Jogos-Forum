/* 
* Classe usada para armazenar dados
* de cada emoticon, tais informações
* serão utilizadas pelo gerenciador de layout
* da gridview que exibirá os emoticons 
*/


package com.gpsoft.uoljogosforum;

public class Emoticon {
    private String Url;
    private String Code;
    private int Id;
    
    Emoticon(String url, String code, int id) {
        Url = url;
        Code = code;
        Id = id;
    }
    
    public String getUrl() {
        return Url;
    }
    
    public String getCode() {
        return Code;
    }
    
    public int getId() {
        return Id;
    }
}