package com.example.javasherlocked;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Options extends Activity {
	
	private Button bPractice;
	private Button bTests;
	private Button bSRIJ;
	private Button bContribute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		init();
	}

	private void init() {
		
		bPractice =(Button)this.findViewById(R.id.bPractice);
		bTests =(Button)this.findViewById(R.id.bTests);
		bSRIJ=(Button)this.findViewById(R.id.bSRIJ);
		bContribute=(Button)this.findViewById(R.id.bContribute);
		
	}
	
	public void Practice(View v){
		bPractice.setAlpha(100);
		Intent practice= new Intent(this,Practice.class);
    	startActivity(practice);
	}
	
	public void Tests(View v){
		bTests.setAlpha(100);
		Intent tests= new Intent(this,Tests.class);
    	startActivity(tests);
	}
	
	public void SRIJ(View v){
		bSRIJ.setAlpha(100);
		Intent srij= new Intent(this,SRIJ.class);
    	startActivity(srij);
		
	}
	
	
	public void Contribute(View v){
		bContribute.setAlpha(100);
		Intent contribute= new Intent(this,Contribute.class);
    	startActivity(contribute);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}

}
