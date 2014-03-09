package com.socket;

/* SERVIDOR  el servidor lo deja tal cual y se fija como 
 * 
 * */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import com.socket.Mensaje_data;



public class server {
	Socket skCliente;
	ServerSocket skServidor;
	String datareceived, substring1, substring2;
	final int PUERTO = 5556;// Puerto que utilizara el servidor utilizar este
							// mismo en el cliente
	String IP_client;
	Mensaje_data mdata = null;
	ObjectOutputStream oos;
	String TimeStamp,palabra="hola";
	int NumPal, NumCat;
	String Diccionario[][] = new String [3][6];

	server() {

		try {
			System.out.println("************ SERVER ****************");
		
			// creamos server socket
			skServidor = new ServerSocket(PUERTO);
			System.out.println("Escuchando el puerto " + PUERTO);
			System.out.println("En Espera....");
            while(true){
			TimeStamp = new java.util.Date().toString();

			try {
				// Creamos socket para manejar conexion con cliente
				skCliente = skServidor.accept(); // esperamos al cliente
				// una vez q se conecto obtenemos la ip
				IP_client = skCliente.getInetAddress().toString();
				System.out.println("[" + TimeStamp + "] Conectado al cliente "
						+ "IP:" + IP_client);
				
				
				
				while (true) {
					// Manejamos flujo de Entrada
					ObjectInputStream ois = new ObjectInputStream(
							skCliente.getInputStream());
					// Cremos un Objeto con lo recibido del cliente
					Object aux = ois.readObject();// leemos objeto
                    
					if (aux instanceof Mensaje_data)
                    {
                    	mdata = (Mensaje_data) aux;
                    	
                    	if (mdata.texto.equals("hola"))
                    	{
                    		//escoje una categoria y una palabra aleatoria y la manda al cliente
                    		NumPal =(int)(Math.random()*5 +1 );
                    		NumCat =(int)(Math.random()*2 );
                    		GenerarDiccionario();
                    		Snd_txt_Msg((String)Diccionario[NumCat][NumPal],(String)Diccionario[NumCat][0]);
                    	}
                    	
                    }
					// cerramos cliente

		                    skCliente.close();
							ois.close();
							System.out
									.println("["
											+ TimeStamp
											+ "] Last_msg detected Conexion cerrada, gracias vuelva pronto");
							break;

					}
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("[" + TimeStamp + "] Error ");
			}
           }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[" + TimeStamp + "] Error 1");
		}
		
	}
	
	public void Snd_txt_Msg(String txt, String label) {

		Mensaje_data mensaje = new Mensaje_data();
		//seteo en texto el parametro  recibido por txt
		mensaje.texto = txt;
		
		mensaje.tags = label;
		
		//action -1 no es mensaje de accion
		mensaje.Action = -1;
		//no es el ultimo msg
		mensaje.last_msg = false;
		//mando msg
		boolean val_acc = Snd_Msg(mensaje);
		//error al enviar
	}
	
	public boolean Snd_Msg(Mensaje_data msg) {

		try {
			//Accedo a flujo de salida
			oos = new ObjectOutputStream(skCliente.getOutputStream());
			//creo objeto mensaje
			Mensaje_data mensaje = new Mensaje_data();

			if (skCliente.isConnected())// si la conexion continua
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
			

			return false;
		}
	}
		
    public String[][] GenerarDiccionario()
    {   
    	Diccionario [0][0] = "PELICULAS";
    	 Diccionario[0][1] = "tron";
    	 Diccionario[0][2] = "seven";
    	 Diccionario[0][3] = "el padrino";
    	 Diccionario[0][4] = "la naranja mecanica";
    	 Diccionario[0][5] = "el club de la pelea";
    	   
    	Diccionario [1][0] =  "SERIES EN INGLES";
    	  Diccionario[1][1] = "game of thrones";
    	  Diccionario[1][2] = "the walking dead";
    	  Diccionario[1][3] = "prison break";
    	  Diccionario[1][4] = "spartacus";
    	  Diccionario[1][5] = "the big bang theory";
    	
    	
    	Diccionario [2][0] = "LIBROS";
    	  Diccionario[2][1] = "los reyes malditos";
    	  Diccionario[2][2] = "dracula";
    	  Diccionario[2][3] = "el ocaso de los dioses";
    	  Diccionario[2][4] = "caballo de troya";
    	  Diccionario[2][5] = "el lobo estepario";
    	
     	return Diccionario;
    	
    }
	// Con base en el código recibido se realiza la acción 
	
	public static void main(String[] args) {
		new server();
	}
	
}	
