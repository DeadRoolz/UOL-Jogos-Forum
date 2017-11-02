/*
* Armazena informações sobre o video do youtube
* embutido no post
*/

package com.gpsoft.uoljogosforum;


public class YouTubeVideo {
    private String videoId;
    private String Titulo;
    private String ThumbUrl;
    
    YouTubeVideo(String id, String titulo, String thumbUrl) {
        
        videoId = id;
        Titulo = titulo;
        ThumbUrl = thumbUrl;
        
    }
    
    public String getId() {
        return videoId;
    }
    
    public String getTitulo() {
        return Titulo;
    }
    
    public String getThumbUrl() {
        return ThumbUrl;
    }
}