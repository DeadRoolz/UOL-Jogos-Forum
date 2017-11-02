/*
* Classe que armazenas os parametros
* utilizados nas funções server-side do forum
*/


package com.gpsoft.uoljogosforum;

import java.util.ArrayList;
import java.net.URLEncoder;

public class PostFunctionsParameters {
    
    private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
    
    public void add(String t, String v) {
        parameters.add(new Parameter(t, v));
    }
    
    
    @Override
    public String toString() {
        String result = "";
        int paramCount = 0;
        
        for(Parameter p : parameters) {
            result = result + "&c0-param" + 
                     Integer.toString(paramCount) + 
                     "=" + p.getType() + ":" +
                     URLEncoder.encode(p.getValue().replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B").replaceAll("\\*", "%2A"));
            
            paramCount++;
        }
        return result;
    }
    
    private class Parameter {
        private String value;
        private String type;
        
        Parameter(String t, String v) {
            value = v;
            type = t;
        }
        
        public String getValue() {
            return value;
        }
    
        public String getType() {
            return type;
        }
    }
}