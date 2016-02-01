package com.hiddenbrains.MLM.screen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AudioClass extends Activity implements Runnable, OnClickListener, OnPreparedListener, MediaController.MediaPlayerControl
{
	private Bundle bundle;
	private String Url="";
	
	private SeekBar sb;
	
	private AudioManager am;
	private int volume;
	private MediaPlayer mp;
	private MediaController mediaController ;
	private ProgressDialog pd;
	private Thread thread;
	private TextView txt;
	private ImageButton back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_view);
		
	    try 
        { 
	    	txt =(TextView) findViewById(R.id.text);
	    	
	    	back = (ImageButton) findViewById(R.id.btn);
			back.setOnClickListener(this);
	    	
	    	sb = (SeekBar)findViewById(R.id.volume_sb);
	    	
		    am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		    volume = am.getStreamVolume(3);
		    sb.setProgress(volume);
		    sb.setMax(am.getStreamMaxVolume(3));
		    sb.setOnSeekBarChangeListener(VolumeChange);
		    mp = new MediaPlayer();
		    mp.setOnPreparedListener(this);
		    mediaController = new MediaController(this);
		    
		    bundle = getIntent().getExtras();
			Url = bundle.getString("url");
			String ss = bundle.getString("txt");
			txt.setText(ss);
			txt.setEllipsize(TruncateAt.MARQUEE);
			txt.setSelected(true);
			
			pd = ProgressDialog.show(this, "Please Wait", "Loading ....", true,true);
			pd.setIcon(R.drawable.mlm);
			thread = new Thread(this);
			thread.start();

        }
        
        catch (Exception e) 
        {
        	e.printStackTrace();
        }

	}
	private OnSeekBarChangeListener VolumeChange = new OnSeekBarChangeListener()
	{
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) 
		{
			int index = sb.getProgress();
			am.setStreamVolume(3, index, 1);
		}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) 
		{
			
		}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			
		}
	};

	@Override
	public void run() 
	{
		try
		{
			mp.setDataSource(Url);
        	mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        	mp.prepare();
			han.sendEmptyMessage(0);
			
		}catch (Exception e) 
		{
			e.getMessage();
		}
		
	}
	private Handler han=new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			try
			{
				mp.start();
				pd.dismiss();
				
			}catch (Exception e) 
			{
				e.getMessage();
			}
		}
	};
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		try
		{
			mp.stop();
			if(pd.isShowing())
			{
				pd.dismiss();
			}
			if(thread.isAlive())
			{
				thread.stop();
			}
			
		}catch (Exception e) 
		{
			e.getMessage();
		}
		
	}
	@Override
	public void onClick(View v) 
	{
		int id = v.getId();
		if(id==R.id.btn)
		{
			mp.stop();
			finish();
		}
		
	}
	public void start() {
	    mp.start();
	  }

	  public void pause() {
	    mp.pause();
	  }

	  public int getDuration() {
	    return mp.getDuration();
	  }

	  public int getCurrentPosition() {
	    return mp.getCurrentPosition();
	  }

	  public void seekTo(int i) {
	    mp.seekTo(i);
	  }

	  public boolean isPlaying() {
	    return mp.isPlaying();
	  }

	  public int getBufferPercentage() {
	    return 0;
	  }

	  public boolean canPause() {
	    return true;
	  }

	  public boolean canSeekBackward() {
	    return true;
	  }

	  public boolean canSeekForward() {
	    return true;
	  }
	@Override
	public void onPrepared(MediaPlayer arg0) 
	{
		    Log.d("xxx", "onPrepared");
		    mediaController.setMediaPlayer(this);
		    mediaController.setAnchorView(findViewById(R.id.relative));

		    han.post(new Runnable() 
		    {
		      public void run() 
		      {
		        mediaController.setEnabled(true);
		        mediaController.show();
		      }
		    });
	}
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    //the MediaController will hide after 3 seconds - tap the screen to make it appear again
	    mediaController.show();
	    return false;
	  }
	
}
