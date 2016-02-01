package com.hiddenbrains.MLM.screen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hiddenbrains.MLM.Adapter.AdapterForAll;
import com.hiddenbrains.MLM.lib.GoogleTracker;

public class ByContentVedio extends Activity implements OnClickListener, OnItemClickListener, Runnable
{
	private ImageButton back,search;
	private Intent intent;
	private AdapterForAll list_adapter;
	private ListView list;
	private String Url="",title_s;
	private ArrayList<String> video_url = new ArrayList<String>();
	private ArrayList<String> video_title = new ArrayList<String>();
	private boolean exception = false;	
	private Bundle bundle;
	private TextView title;
	
	private EditText search_et;
	private String name_email;
	private int x=0;
	
	private  ProgressDialog pd;
	private String video_cate_id,message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.by_content);
		
		
		title = (TextView) findViewById(R.id.title);
		search_et = (EditText) findViewById(R.id.search);
		back = (ImageButton) findViewById(R.id.btn_back);
		back.setOnClickListener(this);
		search = (ImageButton) findViewById(R.id.btn_search);
		search.setOnClickListener(this);
		
		try
		{
			bundle = getIntent().getExtras();
			video_cate_id = bundle.getString("video_cate_id");
			title_s =  bundle.getString("title");
			title.setText(title_s);
			
		}catch (Exception e) {
			e.getMessage();
		}
		pd = ProgressDialog.show(this, "Please Wait", "Loading ....", true,true);
		pd.setIcon(R.drawable.mlm);
		Thread thread = new Thread(this);
		thread.start();
		
	}

	@Override
	public void onClick(View v) 
	{
		int id = v.getId();
		if(id==R.id.btn_back)
		{
			finish();
		}
		else if(id==R.id.btn_search)
		{
			name_email = search_et.getText().toString();
			x=1;
			pd = ProgressDialog.show(this, "Please Wait", "Loading ....", true,true);
			pd.setIcon(R.drawable.mlm);
			Thread thread = new Thread(this);
			thread.start();
		}
	}
	
	private void listData() 
	{
		
		list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        
        try
        {
        	if(x==1)
        	{
//        		Url = "http://sli.hospitalu.com/iphone/Video.asmx/VideoCategory?video_cate_id="+video_cate_id+"&keyword="+name_email;
//        		Url = "http://184.164.156.55/webservice/MLM/Video.asmx/VideoCategory?video_cate_id="+video_cate_id+"&keyword="+name_email;
        		Url =getString(R.string.url)+"Video.asmx/VideoCategory?video_cate_id="+video_cate_id+"&keyword="+name_email;
        		exception = false;	
        	}
        	else
        	{
//        		Url = "http://sli.hospitalu.com/iphone/Video.asmx/VideoCategory?video_cate_id="+video_cate_id;
        		Url =getString(R.string.url)+"Video.asmx/VideoCategory?video_cate_id="+video_cate_id;
//        		Url = "http://184.164.156.55/webservice/MLM/Video.asmx/VideoCategory?video_cate_id="+video_cate_id;
        	}
        	URL url = new URL(Url);
        	URLConnection urlc = url.openConnection();
        	HttpURLConnection huc = (HttpURLConnection)urlc;
        	huc.setRequestMethod("GET");
        	huc.connect();
        	int response = huc.getResponseCode();
        	if(response == HttpURLConnection.HTTP_OK)
        	{
        		InputStream is = huc.getInputStream();
        		String result = convertStreamToString(is);
        		
        		JSONTokener jt = new JSONTokener(result);
        		JSONArray ja = new JSONArray(jt);
        		
        		JSONObject OBJECT = (JSONObject) ja.get(0);
        		String success = OBJECT.getString("success");
        		message = OBJECT.getString("message");
        		
        		if(success.equalsIgnoreCase("0"))
        		{
        			if(!video_url.isEmpty())
        			{
        				video_url.clear();
        				video_title.clear();
        			}
        			runOnUiThread(nocoonection2);
        			exception = false;
        		}
        		else
	        	{
        			if(!video_url.isEmpty())
        			{
        				video_url.clear();
        				video_title.clear();
        			}
	        		for(int i=0;i<ja.length();i++)
	        		{
	        			JSONObject jo = (JSONObject) ja.get(i);
	        			video_url.add(jo.getString("video_url"));
	        			video_title.add(jo.getString("video_title"));
	        		}
	        	}	
        	}
        	else
        	{
        		runOnUiThread(nocoonection);
        	    exception = true;
        	}
        	
        	if(!exception)
        	{ 
	          list_adapter = new AdapterForAll(ByContentVedio.this,video_title); 
        	}
        }catch (Exception e) 
        {
			e.printStackTrace();
			runOnUiThread(nocoonection);
    	    exception = true;
		}
		
	}			
	private Runnable nocoonection=new Runnable() {
	
	@Override
	public void run() {
		alert("No Connections Available!");
	}
	};
	private Runnable nocoonection2=new Runnable() {
		
		@Override
		public void run() {
			alert(message);
		}
	};


	private  static String convertStreamToString(InputStream is) 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
 
        String line = null;
        try 
        {
            while ((line = reader.readLine()) != null) 
            {
                sb.append(line + "\n");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	private void alert(String string) 
	{
		String message = string;
		if(message.equals("No Connections Available!"))
	 	{
	 		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Error");
			dialog.setIcon(R.drawable.mlm);
			dialog.setMessage(message);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		 	dialog.show();
	 	}
	 	else
	 	{
		 	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Message");
			dialog.setIcon(R.drawable.mlm);
			dialog.setMessage(message);
		 	dialog.setPositiveButton("Ok", null);
		 	dialog.show();
	 	}
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
//		GoogleAnalyticsTracker Code Starts Here 
		String label = video_title.get(arg2).toString();
		String url = video_url.get(arg2).toString();
		
		GoogleTracker.track(this, "Video", label);
		
//		GoogleAnalyticsTracker tracker ;
//		tracker = GoogleAnalyticsTracker.getInstance();
//		tracker.startNewSession("UA-25672706-3",this); 
//		tracker.setCustomVar(4,"VideoCategories",Login.user+" :- "+label,1);
//		tracker.trackEvent(Login.user,"Video clicked",Login.info+label+"----"+url,1);
//		tracker.trackEvent(Login.info,"VideoCategories",Login.user+" :- "+label,1);
//		tracker.dispatch();
//		GoogleAnalyticsTracker Code End Here 
		
		
		intent = new Intent(ByContentVedio.this,VideoClass.class);
		intent.putExtra("url", url);
		startActivity(intent);
		
	}

	@Override
	public void run() 
	{
		listData();
		han.sendEmptyMessage(0);
		
	}
	private Handler han=new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			try
			{
				super.handleMessage(msg);
				list.setAdapter(list_adapter);
				pd.dismiss();
				
			}catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	};
}