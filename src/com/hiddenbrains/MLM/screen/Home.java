package com.hiddenbrains.MLM.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class Home extends Activity implements OnClickListener
{
	private ImageButton settings,directorys,events,courses,librarys,videos,audios;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		settings = (ImageButton) findViewById(R.id.btn_setting);
		directorys = (ImageButton) findViewById(R.id.tab_directory);
		events = (ImageButton) findViewById(R.id.tab_events);
		courses = (ImageButton) findViewById(R.id.tab_courses);
		librarys = (ImageButton) findViewById(R.id.tab_library);
		videos = (ImageButton) findViewById(R.id.tab_video);
		audios = (ImageButton) findViewById(R.id.tab_audio);
		
		settings.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(Home.this,Settings.class));
				finish();
				
			}
		});
		directorys.setOnClickListener(this);
		events.setOnClickListener(this);
		courses.setOnClickListener(this);
		librarys.setOnClickListener(this);
		videos.setOnClickListener(this);
		audios.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) 
	{
		String value = " ";
		switch(v.getId())
		{
//			case R.id.btn_setting:
//				startActivity(new Intent(Home.this,Settings.class));
//				finish();
//				break;
			case R.id.tab_directory:
				value = "Directory";
				startActivity(new Intent(Home.this,Directory.class));
				finish();
				break;
			case R.id.tab_events:
//				GoogleAnalyticsTracker tracker1 ;
//				tracker1 = GoogleAnalyticsTracker.getInstance();
//				tracker1.startNewSession("UA-25672706-3",this); 
//				tracker1.setCustomVar(3,Login.user,"Events/Messages",1);
//				tracker1.dispatch();
				value = "Events";
				startActivity(new Intent(Home.this,Events.class));
				finish();
				break;
			case R.id.tab_courses:
//				GoogleAnalyticsTracker tracker2 ;
//				tracker2 = GoogleAnalyticsTracker.getInstance();
//				tracker2.startNewSession("UA-25672706-3",this); 
//				tracker2.setCustomVar(3,Login.user,"Courses",1);
//				tracker2.dispatch();
				value = "Courses";
				startActivity(new Intent(Home.this,Courses.class));
				finish();
				break;
			case R.id.tab_library:
//				GoogleAnalyticsTracker tracker3 ;
//				tracker3 = GoogleAnalyticsTracker.getInstance();
//				tracker3.startNewSession("UA-25672706-3",this); 
//				tracker3.setCustomVar(3,Login.user,"Library",1);
//				tracker3.dispatch();
				value = "Library";
				startActivity(new Intent(Home.this,Library.class));
				finish();
				break;
			case R.id.tab_video:
//				GoogleAnalyticsTracker tracker4 ;
//				tracker4 = GoogleAnalyticsTracker.getInstance();
//				tracker4.startNewSession("UA-25672706-3",this); 
//				tracker4.setCustomVar(3,Login.user,"VideoCategories",1);
//				tracker4.dispatch();
				value = "VideoCategories";
				startActivity(new Intent(Home.this,VideoCategories.class));
				finish();
				break;
			case R.id.tab_audio:
//				GoogleAnalyticsTracker tracker5 ;
//				tracker5 = GoogleAnalyticsTracker.getInstance();
//				tracker5.startNewSession("UA-25672706-3",this); 
//				tracker5.setCustomVar(3,Login.user,"AudioCategories",1);
//				tracker5.dispatch();
				value = "AudioCategories";
				startActivity(new Intent(Home.this,AudioCategories.class));
				finish();
				break;
				
		}
//		GoogleAnalyticsTracker Code Starts Here 
//		GoogleAnalyticsTracker tracker ;
//		tracker = GoogleAnalyticsTracker.getInstance();
//		tracker.startNewSession("UA-25672706-3",this); 
//		tracker.setCustomVar(3,Login.user,value,2);
//		tracker.trackPageView("http://sli.hospitalu.com");
//		tracker.dispatch();
//GoogleAnalyticsTracker Code End Here 	
	}

}
