package com.hiddenbrains.MLM.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Loading extends Activity
{
	private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        final int sleepTime = 2000;
        Thread thread = new Thread()
        {
        	int wait = 0;
        	
        	public void run()
        	{
        		super.run();
        		try
        		{
        			while(wait<sleepTime)
        			{
        				sleep(100);
        				wait +=100;
        			}
        		}
        		catch (Exception e) 
        		{
        				Log.i("Loading Screen", "Server Down");
        		}
        		finally
        		{		
        				intent = new Intent(Loading.this,Login.class);
						startActivity(intent);
						finish();
        		}
        	}
        };
       thread.start();
    }
    
}