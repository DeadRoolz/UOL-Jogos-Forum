/*
* Classe que armazena informações sobre os posts
* lidos no html 
*/

package com.gpsoft.uoljogosforum;

import 	android.graphics.drawable.Drawable;

public class PostsInfo {
    
    private String Post;
    private String Rating;
    private String postCount;
    private UserInfo Autor;
    private String DataCriacaoTpc;
    private String NumVotos;
    private String Id;
    private String TpcLink;
    private YouTubeVideo YTVideo;
    
    
    public PostsInfo(UserInfo autor, 
                    String post, 
                    String rating, 
                    String postcount, 
                    String datacriacaotpc, 
                    String numvotos, 
                    String id, 
                    String tpclink, 
                    YouTubeVideo ytvideo) 
    {
        Autor = autor;
        Post = post;
        Rating = rating;
        postCount = postcount;
        DataCriacaoTpc = datacriacaotpc;
        NumVotos = numvotos;
        Id = id;
        TpcLink = tpclink;
        YTVideo = ytvideo;
    }
    
    public UserInfo getAutor() {
        return Autor;
    }
    
    public String getNumVotos() {
        return NumVotos;
    }
    
    public String getId() {
        return Id;
    }
    
    public String getPost() {
        return Post;
    }
    
    public void setPost(String post) {
        Post = post;
    }
    
    public String getTpcLink() {
        return TpcLink;
    }
    
    public String getDataCriacaoTpc() {
        return DataCriacaoTpc;
    }
    
    public String getRating() {
        return Rating;
    }
    
    public String getAutorAvatarLink() {
        return Autor.getAvatarURL();
    }
    
    public int getCount() {
        return Integer.parseInt(postCount);
    }
    
    public YouTubeVideo getYouTubeVideo() {
        return YTVideo;
    }
}
