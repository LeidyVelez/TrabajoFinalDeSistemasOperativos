package com.socket;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import com.socket.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/* cliente*/
public class Sockettest extends Activity {
	/** Called when the activity is first created. */

	private Button btconect;
	private EditText ipinput;
	Socket miCliente;

	private boolean connected = false;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Mensaje_data mdata;
	String palabra, labels;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		

		ipinput = (EditText) findViewById(R.id.ipinput);
		btconect = (Button) findViewById(R.id.btcnt);
				
		//Al clickear en conectar
		btconect.setOnClickListener(new OnClickListener() {
			@Override
			// conectar
			public void onClick(View v) {
			//Nos conectamos y obtenemos el estado de la conexion
				boolean conectstatus = Connect();
				//si nos pudimos conectar
				if (conectstatus) {//mostramos mensaje 
					
					Toast.makeText(getApplicationContext(), "Usted se ha conectado",
							Toast.LENGTH_SHORT).show();
					//juego j=new juego();
					
					
					Snd_txt_Msg("hola");

		              
					try{
		
						// Manejamos flujo de Entrada
						ObjectInputStream ois = new ObjectInputStream(
								miCliente.getInputStream());
						// Cremos un Objeto con lo recibido del cliente
						Object aux = ois.readObject();// leemos objeto
	                    
						if (aux instanceof Mensaje_data)
	                    {
	                    	mdata = (Mensaje_data) aux;
	                    	
	                    	 palabra = mdata.texto;
	                    	 labels = mdata.tags;
	                    }
					
					}
					catch(Exception exc){}
					
					
					Intent i = new Intent(Sockettest.this, juego.class );
			        i.putExtra("word", palabra);
			        i.putExtra("lab", labels);
			        startActivity(i);
			 
				} else {//error al conectarse 
					Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión",
							Toast.LENGTH_SHORT).show();
			   }
				
				
				
				
				
				
			}
		});		
		/************/
	}
	
	
	
	//Conectamos
	public boolean Connect() {
		//Obtengo datos ingresados en campos
		String IP = ipinput.getText().toString();
		int PORT = 5556;

		try {//creamos sockets con los valores anteriores
			miCliente = new Socket(IP, PORT);
			//si nos conectamos
			if (miCliente.isConnected() == true) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			//Si hubo algun error mostrmos error
			
			Log.e("Error connect()", "" + e);
			return false;
		}
	}

	//Metodo de desconexion
	public void Disconnect() {
		try {
			//Prepramos mensaje de desconexion
			Mensaje_data msgact = new Mensaje_data();
			msgact.texto = "";
			msgact.Action = -1;
			msgact.last_msg = true;
			//avisamos al server que cierre el canal
			boolean val_acc = Snd_Msg(msgact);

			if (!val_acc) {//hubo un error
				
				Log.e("Disconnect() -> ", "!ERROR!");

			} else {//ok nos desconectamos
				
				Log.e("Disconnect() -> ", "!ok!");
				//cerramos socket	
				miCliente.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	//Enviamos mensaje de accion segun el boton q presionamos
	public void Snd_Action(int bt) {
		Mensaje_data msgact = new Mensaje_data();
		//no hay texto
		msgact.texto = "";
		//seteo en el valor action el numero de accion
		msgact.Action = bt;
		//no es el ultimo msg
		msgact.last_msg = false;
		//mando msg
		boolean val_acc = Snd_Msg(msgact);
		//error al enviar
		if (!val_acc) {
			
			Log.e("Snd_Action() -> ", "!ERROR!");

		}
		
	}

	//Envio mensaje de texto 
	public void Snd_txt_Msg(String txt) {

		Mensaje_data mensaje = new Mensaje_data();
		//seteo en texto el parametro  recibido por txt
		mensaje.texto = txt;
		//action -1 no es mensaje de accion
		mensaje.Action = -1;
		//no es el ultimo msg
		mensaje.last_msg = false;
		//mando msg
		boolean val_acc = Snd_Msg(mensaje);
		//error al enviar
		if (!val_acc) {
			
			Log.e("Snd_txt_Msg() -> ", "!ERROR!");
		}
	
	}
	
	/*Metodo para enviar mensaje por socket
	 *recibe como parmetro un objeto Mensaje_data
	 *retorna boolean segun si se pudo establecer o no la conexion
	 */
	public boolean Snd_Msg(Mensaje_data msg) {

		try {
			//Accedo a flujo de salida
			oos = new ObjectOutputStream(miCliente.getOutputStream());
			//creo objeto mensaje
			Mensaje_data mensaje = new Mensaje_data();

			if (miCliente.isConnected())// si la conexion continua
			{
				//lo asocio al mensaje recibido
				mensaje = msg;
				//Envio mensaje por flujo
				oos.writeObject(mensaje);
				//envio ok
				return true;

			} else {//en caso de que no halla conexion al enviar el msg
				
				return false;
			}

		} catch (IOException e) {// hubo algun error
			Log.e("Snd_Msg() ERROR -> ", "" + e);

			return false;
		}
	}
}
