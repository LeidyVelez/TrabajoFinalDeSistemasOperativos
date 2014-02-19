package com.socket;

import java.io.Serializable;

public class Mensaje_data implements Serializable{
	  
	private static final long serialVersionUID = 9178463713495654837L;

    public int Action, NumRandom;
    public String texto, tags;
    public boolean last_msg=false;
        
}
