package com.socket;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.socket.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/* cliente*/
public class Sockettest extends Activity {
	/** Called when the activity is first created. */

	private Button btconect;
	private EditText ipinput;
	Socket miCliente;
	int Request_code = 11;
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
				eventoButtonConectar();
			}
		});		
		/************/
	}
	
	//Este método hace reutilizable el código que usa el evento clic del botón conectar
	public void eventoButtonConectar(){
		
		//Nos conectamos y obtenemos el estado de la conexion
		boolean conectstatus = Connect();
		//si nos pudimos conectar
		if (conectstatus) {//mostramos mensaje 
			
			Toast.makeText(getApplicationContext(), "Usted se ha conectado",
					Toast.LENGTH_SHORT).show();
			//juego j=new juego();
			
			
			Snd_txt_Msg("hola");

			ObjectInputStream ois;
			Object aux;
			
			try{

				// Manejamos flujo de Entrada
				 ois = new ObjectInputStream(
						miCliente.getInputStream());
				// Cremos un Objeto con lo recibido del cliente
				 aux = ois.readObject();// leemos objeto
                
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
	        //agregamos el ranking si es que lo hay
	        Mensaje_data infoRanking = new Mensaje_data();	        
	        infoRanking.tags = "ranking";
	        Snd_Msg(infoRanking);	//mandamos la solicitud de ranking
	        try{
	        	ois = new ObjectInputStream(
	        			miCliente.getInputStream());
	        	// Cremos un Objeto con lo recibido del cliente
	        	aux = ois.readObject();
	        	mdata = (Mensaje_data) aux;
	        	i.putExtra("ranking", mdata.texto);
	        	/*Toast.makeText(getApplicationContext(),"texto ranking" + mdata.texto ,
						Toast.LENGTH_SHORT).show();*/
	        }catch(Exception e){}
	        startActivityForResult(i, Request_code);
		} else {//error al conectarse 
			Toast.makeText(getApplicationContext(), "No se ha podido establecer conexiÃ³n",
					Toast.LENGTH_SHORT).show();
	   }
		
	}
	
	//método para capturar los datos recibidos desde la otra actividad creada
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Connect();
	    // TODO Auto-generated method stub
			
		//ahora vamos a guardar el nick y el puntaje en el servidor por medio de un Mensaje_data		
		Mensaje_data infoJugador = new Mensaje_data();
		infoJugador.texto = data.getStringExtra("score");	//se guarda el score en el campo texto
		infoJugador.tags = data.getStringExtra("nick");		//se guarda el nick en el campo tags
		//action -1 no es mensaje de accion
		infoJugador.Action = -1;
		//no es el ultimo msg
		infoJugador.last_msg = false;
		//mando el mensaje
		boolean val_acc = Snd_Msg(infoJugador);
		
		if (!val_acc) {
			//error al enviar
			Log.e("Snd_txt_Msg() -> ", "!ERROR!");
		}else{
			//si no  hay error al enviar entonces vuelve y juega en este punto
			eventoButtonConectar();
		}
	    /*if ((requestCode == Request_code) && (resultCode == RESULT_OK)){
	    	Toast.makeText(getApplicationContext(), "El nick es "+ data.getStringExtra("nick")+ " y" +
	        		" su puntaje es "+ data.getStringExtra("score"),
					Toast.LENGTH_SHORT).show();
	    }*/
	
	}
	
	
	
	//Conectamos
	public boolean Connect() {
		//Obtengo datos ingresados en campos
		String IP = ipinput.getText().toString();
		int PORT = 5560;

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
	/*
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
	*/

	/*//Enviamos mensaje de accion segun el boton q presionamos
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
		
	}*/

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
