/* 
* Classe que armazena o cookie do forum
*/


package com.gpsoft.uoljogosforum;

//java imports
import java.net.*;


public final class CookieForum {
    private final static CookieManager cm = new CookieManager();
    
    public static CookieManager getCookieManager() {
        return cm;
    }
}