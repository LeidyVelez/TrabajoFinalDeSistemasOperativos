package com.socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Gano extends Activity {
	private Button enviar, ranking;
	private TextView score;
	private EditText nick;
	private String puntaje;
	private String nickEnviar;
	private Bundle recibir;
	private AlertDialog alert;
	private String rankingString;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gano);
		enviar = (Button) findViewById(R.id.buttonEnviar);
		ranking = (Button) findViewById(R.id.buttonRanking);
		recibir = this.getIntent().getExtras();
		rankingString = (String) recibir.getString("ranking");
		if(rankingString == null || rankingString.equalsIgnoreCase("")) 
			rankingString = "No hay registros disponibles! ";
		//ventana emergente
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(rankingString)
	            .setTitle("Top de los mejores!!")
	            .setCancelable(false)
	            .setNeutralButton("Listo!",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();
	                            }
	                        });
	    alert = builder.create();
	    //ventana emergente		
		score = (TextView) findViewById(R.id.textViewScore2);
		nick = (EditText) findViewById(R.id.editText1);		
		puntaje = (String) recibir.getString("score");		
		score.setText("Score: " + puntaje);
		enviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				nickEnviar = nick.getText().toString();
				Toast.makeText(getApplicationContext(),nickEnviar,Toast.LENGTH_SHORT).show();
				//retornamos el nick del jugador y su puntaje! al juego
				Intent intent = new Intent();
			    intent.putExtra("nick", nickEnviar);
			    intent.putExtra("score", puntaje);
			    //el resultado de esta actividad será el nick 
			    setResult(RESULT_OK, intent);
			    finish();
			}
		});
		
		//evento del ranking del botón
		ranking.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				Intent intent = new Intent();
			    intent.putExtra("nick", "ranking");
			    intent.putExtra("score", puntaje);
			    //el resultado de esta actividad será el nick 
			    setResult(RESULT_OK, intent);
			    */
			    alert.show();
			    //finish();
			    /*Toast.makeText(getApplicationContext(),
						"El top de los mejores 10 puntajes son:\n",
						Toast.LENGTH_LONG).show();*/
			}
		});
		//cierra evento
	}
}