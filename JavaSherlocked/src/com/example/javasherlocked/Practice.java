package com.example.javasherlocked;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Practice extends Activity implements OnClickListener {
	
	

	//Strings for getting and putting data for various states and intents
	private static final String INDEX = "question_index";

	// String variables to hold question,correct answer,options and explanations...
	// Answer Text is created specially for sharing only
	private static String questionText,optionA,optionB,optionC,optionD,correctAnswer,explanation,answerText;
    
	// Place Holder for the current question's index
	private int currentIndex=0;
	
	final private int no_of_ques=2;
	// String array of tags associated with each question to archive them...
	private String[] tags;
	
	// ImageButtons for navigation
	private ImageButton bNext,bPrev;
	
	//Button for Explanation
	private Button bExplain;
	
	// UI Elements(TextViews) to hold question and options
	private TextView tvQuestionText,tvQuestionNo;
	
	private TextView tvOptionA,tvOptionB,tvOptionC,tvOptionD;
	
	// All the Questions Database
	private JSONArray Questions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice);
		
		if(savedInstanceState==null)
		{
			currentIndex=0;
		}else{
			currentIndex=savedInstanceState.getInt(INDEX);
		}
		init();
		new GrabQuestionTask().execute();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt(INDEX,currentIndex);
		super.onSaveInstanceState(outState);
	}
	
	

	private void init() {
		tags = new String[3];
		tvQuestionText = (TextView)this.findViewById(R.id.tvQuestionText);
		tvOptionA =(TextView)this.findViewById(R.id.tvOptionA);
		tvOptionB =(TextView)this.findViewById(R.id.tvOptionB);
		tvOptionC =(TextView)this.findViewById(R.id.tvOptionC);
		tvOptionD =(TextView)this.findViewById(R.id.tvOptionD);
		bPrev = (ImageButton)this.findViewById(R.id.bPrev);
		bNext = (ImageButton)this.findViewById(R.id.bNext);
		bExplain =(Button)this.findViewById(R.id.bExplain);
		tvQuestionNo =(TextView)this.findViewById(R.id.tvQuestionNo);
		
		//Setting up the click listeners for all the buttons and textviews
		
		tvOptionA.setOnClickListener(this);
		tvOptionB.setOnClickListener(this);
		tvOptionC.setOnClickListener(this);
		tvOptionD.setOnClickListener(this);
		bPrev.setOnClickListener(this);
		bNext.setOnClickListener(this);
		bExplain.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practice, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		
		case R.id.menu_item_share:
			try {
				shareQuestion();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
	public Intent shareQuestion() throws JSONException{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setType("text/plain");
		
		Log.e("Intent Share",questionText);
		intent.putExtra(Intent.EXTRA_TEXT,questionText+"\n" + answerText);
		startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
		return intent;
		
	}
	
	private class GrabQuestionTask extends AsyncTask<String,String,String>{
	

		@Override
		protected void onPostExecute(String result) {
			setUiElements();
			super.onPostExecute(result);
		}
		private void setUiElements() {
			tvQuestionNo.setText("Question " + (currentIndex+1));
			tvQuestionText.setText(questionText);
			tvOptionA.setText("A. "+optionA);
			tvOptionB.setText("B. "+optionB);
			tvOptionC.setText("C. "+optionC);
			tvOptionD.setText("D. "+optionD);
			answerText = "A. "+optionA +"\n"+"B. "+optionB +"\n"+"C. "+optionC +"\n"+"D. "+optionD;
		}

		

		@Override
		protected String doInBackground(String... params) {
			String jsonString =readQuestion();
	        
			try {
				parseJson(jsonString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}

		private void parseJson(String jsonString) throws JSONException  {
			
			JSONObject root=new JSONObject(jsonString);
			//Log.i("Json Root",root.toString());
			Questions =root.getJSONArray("Questions");
			//Log.i("JsonArrayQuestion",Questions.toString());
			JSONObject currentQuestionObject = Questions.getJSONObject(currentIndex);
			JSONObject question =currentQuestionObject.getJSONObject("Question "+currentIndex);
			//Log.i("Json Question1",question.toString());
			questionText = question.getString("Question Text");
		    correctAnswer = question.getString("Correct Answer");
		    optionA=question.getString("Option A");
		    optionB=question.getString("Option B");
		    optionC=question.getString("Option C");
		    optionD=question.getString("Option D");
		    explanation=question.getString("Explanation");
		    JSONArray Tag = question.getJSONArray("Tags");
		    bExplain.setText("Explain");
		    tags[0] = Tag.getString(0);
		    tags[1] = Tag.getString(1);
		    tags[2]= Tag.getString(2);
			
			
		}

		private String readQuestion() {
			
			InputStream in;
			StringBuilder out = new StringBuilder();
			try {
				AssetManager am = Practice.this.getAssets();
				in = am.open("Questions.json");
				
				 BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			       
			        String line;
			        while ((line = reader.readLine()) != null) {
			            out.append(line);
			        }
			       
			        reader.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return out.toString(); 
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.tvOptionA:
			checkAnswer("a");
			break;
		case R.id.tvOptionB:
			checkAnswer("b");
			break;
        case R.id.tvOptionC:
        	checkAnswer("c");
			break;
        case R.id.tvOptionD:
        	checkAnswer("d");
	        break;
        case R.id.bNext:
        	if(currentIndex+1<no_of_ques)
        	{
        		currentIndex = (currentIndex+1);
        		try {
    				getQuestion(currentIndex);
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        	
        	break;
        case R.id.bPrev:
        	if(currentIndex-1>=0){
        	currentIndex = currentIndex-1;
        	try {
				getQuestion(currentIndex);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	}
        	break;
        case R.id.bExplain:
        	Toast.makeText(getApplicationContext(),explanation,Toast.LENGTH_LONG).show();
        	break;
		}
	}

	private void checkAnswer(String optionSelected){
		if(correctAnswer.contentEquals(optionSelected)){
			Toast.makeText(getApplicationContext(), "Correct! ",Toast.LENGTH_SHORT).show();
			if(correctAnswer.contentEquals("b")&& tvOptionB.isPressed()){
				Log.e("The Mystery","It is still pressed");
				tvOptionB.setBackgroundColor(getResources().getColor(R.color.actionbar_background));
				tvOptionB.setPressed(false);
			}
			
		}else{
			Toast.makeText(getApplicationContext(), "Incorrect :( ",Toast.LENGTH_SHORT).show();
		}
	}
	private void changeUiElements(boolean tag) {
		tvQuestionNo.setText("Question " + (currentIndex+1));
		tvQuestionText.setText(questionText);
		tvOptionA.setText("A. "+optionA);
		tvOptionB.setText("B. "+optionB);
		tvOptionC.setText("C. "+optionC);
		tvOptionD.setText("D. "+optionD);
		answerText = "A. "+optionA +"\n"+"B. "+optionB +"\n"+"C. "+optionC +"\n"+"D. "+optionD;
		if(tag){
			Log.e("The Mystery","Inside Tag");
			bExplain.setText("Solution");
		}else{
			Log.e("The Mystery","False");
			bExplain.setText("Explain");
		}
	}
	private void getQuestion(int currentindex) throws JSONException {
		JSONObject currentQuestionObject = Questions.getJSONObject(currentIndex);
		JSONObject question =currentQuestionObject.getJSONObject("Question "+currentIndex);
		Log.i("Json Question1",question.toString());
		questionText = question.getString("Question Text");
	    correctAnswer = question.getString("Correct Answer");
	    optionA=question.getString("Option A");
	    optionB=question.getString("Option B");
	    optionC=question.getString("Option C");
	    optionD=question.getString("Option D");
	    explanation=question.getString("Explanation");
	    JSONArray Tag = question.getJSONArray("Tags");
	    tags[0] = Tag.getString(0);
	    tags[1] = Tag.getString(1);
	    tags[2]= Tag.getString(2);
	    Log.e("The Mystery",Tag.toString());
	    if(tags[0].contentEquals("Programming")){
	    changeUiElements(true);
	    }
	    else
	    {
	    	changeUiElements(false);
	    }
	}
	

}
