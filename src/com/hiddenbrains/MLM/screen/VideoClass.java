package com.hiddenbrains.MLM.screen;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoClass extends Activity
{
	private Bundle bundle;
	private String Url;
	private VideoView videoView;
	private  ProgressDialog pd;
	private MediaController mediaController;
	private Uri video;
	private boolean error = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_view);
		clearLog();
		
		try
		{
	        bundle = getIntent().getExtras();
			Url = bundle.getString("url");
			
			pd = ProgressDialog.show(this, "Please Wait", "Loading ....", true,true);
			pd.setIcon(R.drawable.mlm);
			
			videoView = (VideoView) findViewById(R.id.videoView);
			mediaController = new MediaController(this);
			
			mediaController.setAnchorView(videoView);
	        videoView.setMediaController(mediaController);
	        
			video = Uri.parse(Url);
			
			
//			Thread thread = new Thread(this);
//			thread.start();
			videoView.setVideoURI(video);
			videoView.start();
			
			runOnUiThread(videoThread);
	
		}catch (Exception e) 
		{
			e.getMessage();
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		error = true;
        if(pd.isShowing())
        	pd.dismiss();
        if(videoView.isPlaying())
        	videoView.stopPlayback();
        clearLog();
		finish();
	}
	
	Runnable videoThread = new Runnable() 
	{
		@Override
		public void run() 
		{
			try
			{
				han.sendEmptyMessage(0);
				
			}catch (Exception e) 
			{
				e.getMessage();
			}
		}
	};
	
	private Handler han=new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			try
			{
				if(videoView.isPlaying())
				{
					pd.dismiss(); 
				}
				else
				{
					if(getLogCatDetails()) 
					{
						pd.dismiss(); 
//					    finish(); 
					} 
					else 
					{
						han.removeCallbacks(videoThread);
						han.post(videoThread);
					}
				}	
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	};
	
	private boolean getLogCatDetails() 
	{
//		boolean error = false;
		
		try 
		{
			Process process = Runtime.getRuntime().exec("logcat -d");

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			StringBuilder log = new StringBuilder();

			String line = "";

			while ((line = bufferedReader.readLine()) != null) 
			{
				log.append(line);
			}

            String screen[]= {"Error: 200,-32","Error: 1,-1","Error (1,-2147483648)","Error: 1,-4"};
            int index = -1; 
            for(int i=0;i<4;i++)
			{
            	index = log.indexOf(screen[i]);
				if(index != -1)
				{
					error = true;
					break;
				}	   
			}  
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return error;
	}

	private void clearLog()
	{
		try
		{
			Runtime.getRuntime().exec("logcat -c");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
