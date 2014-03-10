package com.socket;

import android.app.Activity;
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
	private String puntaje;
	String nickEnviar;
	private Bundle recibir;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gano);
		enviar = (Button) findViewById(R.id.buttonEnviar);
		score = (TextView) findViewById(R.id.textViewScore2);
		nick = (EditText) findViewById(R.id.editText1);
		recibir = this.getIntent().getExtras();
		puntaje = (String) recibir.getString("score");
		score.setText("Score: " + puntaje);
		enviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				nickEnviar = nick.getText().toString();
				Toast.makeText(getApplicationContext(),nickEnviar,Toast.LENGTH_SHORT).show();
			}
		});
	}
}