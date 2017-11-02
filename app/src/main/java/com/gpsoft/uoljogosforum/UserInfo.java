/*
* Armazena informaçoes do usuário autor do post
*/

package com.gpsoft.uoljogosforum;

import android.text.Spannable;

public class UserInfo {
    
    private String DataCadastro;
    private String NumMsgs;
    private String NickName;
    private String AvatarURL;
    private String Assinatura;
    
    UserInfo(String nick, String datadacastro, String numsgs, String avatarurl, String assinatura) {
        DataCadastro = datadacastro;
        NumMsgs = numsgs;
        NickName = nick;
        AvatarURL = avatarurl;
        Assinatura = assinatura;
    }
    
    public String getDataCadastro() {
        return DataCadastro;
    }
    
    public String getNumMsgs() {
        return "Mensagens: " + NumMsgs;
    }
    
    public String getNickName() {
        return NickName;
    }
    
    public String getAssinatura() {
        return Assinatura;
    }
    
    public String getAvatarURL() {
        return AvatarURL;
    }
}