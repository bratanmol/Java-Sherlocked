package com.example.javasherlocked;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ControllerActivity extends Activity implements AnimationListener {

	/*
	 * Devlopement Notes
	 * 
	 * 1. We can't go from Stop state to start state directly in MEdia PLayer.
	 * First goto Prepare then start.
	 * 
	 * 2. To stop recreating activity each time the device rotates use config changes int he manifest.
	 */

	
	private Animation animTranslate;

	// Used to play long media files
	private MediaPlayer mp;

	// Used to play short sounds
	private SoundPool sp;
	int soundId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);

		// Initializing the widgets
		init();
		// mp.start();
	}

	private void init() {
		animTranslate = AnimationUtils
				.loadAnimation(this, R.anim.anim_tanslate);
		animTranslate.setAnimationListener(this);
		
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundId = sp.load(this, R.raw.gunshot, 1);
		
		

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mp = MediaPlayer.create(getApplicationContext(),
				R.raw.sherlock_bg_music);
		mp.start();
		mp.setVolume(0.4f,0.4f);
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		mp.stop();
		mp.release();
		super.onPause();
	}


	public void StartApp(View v) throws InterruptedException {
		sp.play(soundId, 1, 1, 0, 0, 1);
		TextView tvEnter = (TextView) this.findViewById(R.id.tvEnter);
		tvEnter.startAnimation(animTranslate);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.controller, menu);
		return true;
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		Intent options = new Intent(this, Options.class);
		startActivity(options);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}



}
