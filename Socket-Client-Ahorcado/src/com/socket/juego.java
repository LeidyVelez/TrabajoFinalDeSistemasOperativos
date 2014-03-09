package com.socket;

//laj<shj<hflzsjdhfdsjfhv

import com.socket.R;
import com.socket.R.drawable;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class juego extends Activity {
	private Button enviar;
	private TextView texto , tag, score1, score2, score3;
	private EditText letraingr;
	private ImageView img;
	private int i=1, puntaje = 0;
    private boolean letra=true,ganar=false;
    private String ejemplo,palmostrar="", Ptag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		Bundle reicieveParams = getIntent().getExtras();
		 ejemplo = (String)reicieveParams.getString("word");
		  Ptag   =  (String)reicieveParams.getString("lab");
		img=(ImageView) findViewById(R.id.imgview);
		texto = (TextView) findViewById(R.id.textView1);
		tag = (TextView) findViewById(R.id.textView2);
		letraingr= (EditText) findViewById(R.id.editText1);
		enviar=(Button) findViewById(R.id.buttonenviar);
		score1 = (TextView) findViewById(R.id.textViewScore1);
		score1.setText("Score: " + puntaje);
		//recibir datos
		 generarpalabra();
		 texto.setText(palmostrar);
		 tag.setText(Ptag);
		enviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//si la letra que ingresa esta en la palabra o no
				String aux=letraingr.getText().toString();
				char[] caracter= aux.toCharArray();
				//comprobar si el caracter existe en la palabra
				letra=comprobarletra(caracter[0]);
				if(letra){

			       //ingresa la cadena que le envia el server
					palmostrar=formAhorcadito(caracter[0]);
					texto.setText(palmostrar);
					//mira aver si gano el juego
					gano(palmostrar);
				}
				else{
			    //si no esta aumenta la imagen
		        i++;
				String s = Integer.toString(i);
				int resID = getResources().getIdentifier("ahor"+s, "drawable", "com.socket");
				img.setImageResource(resID);
				letra=true;
				  //si esta es la ultima imagen
				  if(i==8){
					  //fin juego
					  setContentView(R.layout.perdio);
					  score3 = (TextView) findViewById(R.id.textViewScore3);
					  score3.setText("Score: " + puntaje);
				  }
				}
				letraingr.setText("");
				score1.setText("Score: " + puntaje);
			}
		});

	}


	//funcion para saber si dentro de la palabra esta el caracter
	public boolean comprobarletra(char caracter) {
		boolean encontrado=false;
		//CODIGO DE SERVER

           for(int i =0;i<ejemplo.length();i++){
        	   if(ejemplo.charAt(i)==caracter){
        		   encontrado=true;
        		 return encontrado;
        	   }
           }
		return encontrado;
	}
	//funcion para recuperar la cadena que se le mostrara al cliente
	public String formAhorcadito(char caracter) {
		 //CODIGOSERVER
        String palabra=palmostrar;
        String palaux="",palsalida="";


          for(int i=0;i<ejemplo.length();i++){
  			if(ejemplo.charAt(i)==caracter){
  				palaux=palaux+caracter+" ";
  			}else{
  				palaux=palaux+"_ ";
  			}
  		   }
          //genero la nueva palabra a mostrar
          for(int i=0;i<palaux.length();i++){
        	   //miro si ambas tienen en comun el caracter para ponerlo igual
    			if(palabra.charAt(i)==palaux.charAt(i)){
    				palsalida=palsalida+palabra.charAt(i);
    			}else{
    				//en caso de que alguna sea diferente coloco la que tenga el caracter diferente a "_"
    			  if(palabra.charAt(i)=='_'){
    				  palsalida=palsalida+palaux.charAt(i);
    			  }
    				  else{
    					  palsalida=palsalida+palabra.charAt(i);
    				  }
    			  }

          }
        //CODIGOSERVER fin
        palabra=palsalida;
		return palabra;
	}
	// si la cadena que se le muestra al cliente no tiene el simbolo "_" gana
	public void gano(String text) {
        //recorrer texto
		boolean gano=true;

		for(int i=0;i<text.length();i++){

			if(text.charAt(i)=='_'){
				gano=false;
			}
		}
		//si nunca leyo el caracter "_" la variable seguira verdadera por lo tanto gano
		if (gano){
			setContentView(R.layout.gano);
			score2 = (TextView) findViewById(R.id.textViewScore2);
			score2.setText("Score: " + puntaje);
		}
		return ;
	}
	public void generarpalabra() {
		//CODIGOSERVER
        int num=ejemplo.length();

        for (int i=0;i<num;i++){

        	if(ejemplo.substring(i,i+1).equals(" "))
        	{
        		 palmostrar=palmostrar+"  ";
        	}
        	else
        	{

        		 palmostrar=palmostrar+"_ ";
        	}



        }
		return ;
	}

}
