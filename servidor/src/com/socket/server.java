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
	final int PUERTO = 5560;// Puerto que utilizara el servidor utilizar este
							// mismo en el cliente
	String IP_client, IP_clientAfter;
	Mensaje_data mdata = null;
	ObjectOutputStream oos;
	String TimeStamp,palabra="hola";
	int NumPal, NumCat;
	String jugadores[][] = new String [100][2];
	String Diccionario[][] = new String [3][6];

	server() {

		try {
			System.out.println("************ SERVER ****************");
		
			// creamos server socket
			skServidor = new ServerSocket(PUERTO);
			System.out.println("Escuchando el puerto " + PUERTO);
			System.out.println("En Espera....");
			TimeStamp = new java.util.Date().toString();
						
            while(true){
			
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
                    		NumCat =(int)(Math.random()*3 );
                    		GenerarDiccionario();
                    		Snd_txt_Msg((String)Diccionario[NumCat][NumPal],(String)Diccionario[NumCat][0]);
                    		rankingJugadores();
                    	}
                    	
                    }
					// cerramos cliente

					ois.close();
					skCliente.close();
					System.out.println("["+ TimeStamp+ "] Conexion cerrada, esperando actualización...");
					break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("[" + TimeStamp + "] Error ");
				}
			
			// Esperamos hasta la nueva conección con el cliente
			skCliente = skServidor.accept(); // esperamos al cliente
			// una vez q se conecto obtenemos la ip
			IP_clientAfter = skCliente.getInetAddress().toString();
			System.out.println("[" + TimeStamp + "] Conectado al cliente "
					+ "IP:" + IP_client);
			//comprobamos que sea la misma IP anterior para proceder a guardar, si no es así significa que el cliente
			//se está conectando por primera vez y el proceso a realizar es el de arriba.
			//es una prevención para un caso hipotético de varias conexiones a la vez
			try{
				if(IP_clientAfter.equals(IP_client)){
				
					
					//procedemos a revisar y guardar la información que envía el cliente
					// Manejamos flujo de Entrada
					ObjectInputStream ois = new ObjectInputStream(skCliente.getInputStream());
					// Cremos un Objeto con lo recibido del cliente
					Object aux = ois.readObject();// leemos objeto
	            
					if (aux instanceof Mensaje_data){
					
						mdata = (Mensaje_data) aux;
						//comparamos para saber si perdió y continuar
						if(mdata.tags == null || mdata.tags.equalsIgnoreCase("perdio")){
							ois.close();
							skCliente.close();
							continue;
						}else{
							//comparamos para saber si se hizo petición de ranking de jugadores
							if(mdata.tags.equalsIgnoreCase("ranking")){
								rankingJugadores();
								System.out.println("[" + TimeStamp + "]" + " Enviando ranking de jugadores...");
								ois.close();
								skCliente.close();
								continue;
							}
						}
						System.out.println("[" + TimeStamp + "]" + " guardando/actualizando nick y score...");
						guardarInfoJugador(mdata.tags, mdata.texto);				
	            	}
					//	cerramos la conexión
					ois.close();
					skCliente.close();
					System.out.println("["+ TimeStamp+ "] Datos guardados y actualizados, sigue superándote!!!");
				}
            }catch (EOFException e) {
				//e.printStackTrace();
				System.out.println("[" + TimeStamp + "] Desconexión inesperada...");
				return;
			}
			//prueba para revisar los jugadores guardados
			ordenarRanking();
			//fin prueba
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[" + TimeStamp + "] Error 1");
		}
		
	}
	
	//guardamos el nick y el puntaje en el arreglo de jugadores
	public void guardarInfoJugador(String nick, String puntaje){
		for(int i = 0; i < jugadores.length; i++){	//mientras menos que el límite que puede almacenar el servidor
			if(jugadores[i][0]== null){		//si está vacío el espacio guarde el jugador
				jugadores[i][0] = nick;
				jugadores[i][1] = puntaje;
				break;
			}else{	//si no está vacío
				if(jugadores[i][0].equalsIgnoreCase(nick)){		//si tiene el mismo nombre
					if(Integer.parseInt(jugadores[i][1]) < Integer.parseInt(puntaje)){	//guarde sólo si esl puntaje es mayor
						jugadores[i][1] = puntaje;
						break;
					}
				}
			}
		}
	}
	
	//muestra los jugadores es un método de prueba
	public void mostrarJugadores(){
		for(int i = 0; i < jugadores.length; i++){
			if(jugadores[i][0]!=null){
				System.out.println("Jugador N°: " + (i+1) +" " + jugadores[i][0] + " con puntaje " + jugadores[i][1]);
			}
		}
	}
	
	//retorna jugador, posición es la posición en la que está guardado en jugador, offset 0 si es para retornar el nick
	//offset 1 si es para retornar el puntaje
	//la función retorna NULL en caso de que se pida una posición o un offset fuera del rango
	//método para formar el mensaje_data para retornar el jugador completo
	public String retornarJugador(String posicion, String offset){
		if(Integer.parseInt(posicion) <= jugadores.length && (offset == "0" || offset == "0")){
			return jugadores[Integer.parseInt(posicion)][Integer.parseInt(offset)];
		}
		return null;
	}
	
	//método para retornar el mensaje_data con una posición ingresada
	//se puede usar el método Snd_txt_Msg en vez de este
	public Mensaje_data retornarMsgJugador(String posicion){
		Mensaje_data msgJugador = new Mensaje_data();
		msgJugador.tags = retornarJugador(posicion, "0");	//ingresa el nick como tags
		msgJugador.texto = retornarJugador(posicion, "1");	//ingresa el puntaje en el campo texto
		msgJugador.Action = -1;
		msgJugador.last_msg = false;
		return msgJugador;
	}
	
	//enviamor un String con los primeros 10 (si los hay) mejores jugadores con sus puntajes respectivos
	public void enviarRankingJugador(String posicion){
		ordenarRanking();
		Snd_txt_Msg(jugadores[Integer.parseInt(posicion)][1], jugadores[Integer.parseInt(posicion)][0]);
	}
	
	public void rankingJugadores(){
		ordenarRanking();
		
		String encabezado = "el top 10 de los mejores jugadores es \n";
		int i = 0;
		String enumeracion = "";
		if(jugadores[0][0]!=null){ 	//si está vacío
		do{
			enumeracion += "\n Jugador N°"+ (i+1) + ": " + jugadores[i][0] + " puntaje: " + jugadores[i][1];
			i++;
		}while(i < jugadores.length && jugadores[i][0]!=null);
		}
		Snd_txt_Msg(enumeracion, encabezado);
	}
	
	public void ordenarRanking(){
		
		boolean hayCambios = true;
		for(int i = 0; i < jugadores.length && hayCambios; i++){
			hayCambios = false;
			for(int j = 0; j < jugadores.length-1; j++){
				try{
					if(Integer.parseInt(jugadores[j][1]) < Integer.parseInt(jugadores[j + 1][1])){
						intercambia(j, j+1);
						hayCambios = true;
					}
				}catch(NumberFormatException e){
					break;
				}
			}
		}
		mostrarJugadores();
	}
	
	public void intercambia(int i, int j){
		String nickTemp, scoreTemp;
		nickTemp = jugadores[i][0];
		scoreTemp = jugadores[i][1];
		jugadores[i][0] = jugadores[j][0];
		jugadores[i][1] = jugadores[j][1];
		jugadores[j][0] = nickTemp;
		jugadores[j][1] = scoreTemp;
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
	// Con base en el cÃ³digo recibido se realiza la acciÃ³n 
	
	public static void main(String[] args) {
		new server();
	}
	
}	
