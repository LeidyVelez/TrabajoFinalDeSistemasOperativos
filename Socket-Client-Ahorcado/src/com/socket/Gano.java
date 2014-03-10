package com.socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Gano extends Activity {
	private Button enviar;
	private TextView score;
	private EditText nick;
	private String puntaje, IP;
	String nickEnviar;
	private Bundle recibir;
	public int OK_RESULT_CODE = 13;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gano);
		enviar = (Button) findViewById(R.id.buttonEnviar);
		//ESTE ES EL SCORE QUE HAY QUE MANDAR AL SERVIDOR
		score = (TextView) findViewById(R.id.textViewScore2);
		nick = (EditText) findViewById(R.id.editText1);
		recibir = this.getIntent().getExtras();
		puntaje = (String) recibir.getString("score");
		IP = (String) recibir.getString("ip");
		score.setText("Score: " + puntaje);
	
		enviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//AQUI SE GUARDA EL NICK INGRESADO
				nickEnviar = nick.getText().toString();
				Toast.makeText(getApplicationContext(),nickEnviar,Toast.LENGTH_SHORT).show();
				//retornamos el nick del jugador y su puntaje! al juego
				Intent intent = new Intent();
			    intent.putExtra("nick", nickEnviar);
			    intent.putExtra("score", puntaje);
			    //el resultado de esta actividad será el nick y el puntaje con código de resultado'13'
			    setResult(RESULT_OK, intent);
			    finish();
				
			}
		});
	}
}