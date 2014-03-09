package com.socket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Perdio extends Activity {
	private Button volver;
	private TextView score;
	private String puntaje;
	private Bundle recibir;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perdio);
		volver = (Button) findViewById(R.id.buttonVolver);
		score = (TextView) findViewById(R.id.textViewScore3);
		recibir = this.getIntent().getExtras();
		puntaje = (String) recibir.getString("score");
		score.setText("Score: " + puntaje);
		volver.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"Aqui va el codigo del nuevo intent",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}