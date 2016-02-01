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
import android.widget.ImageButton;
import android.widget.ListView;

import com.hiddenbrains.MLM.Adapter.AdapterForAll;
import com.hiddenbrains.MLM.lib.GlobalVar;

public class VideoCategories extends Activity implements OnClickListener, OnItemClickListener, Runnable
{
	private ImageButton home;
	
	private Intent intent;
	private AdapterForAll list_adapter;
	private ListView list;
	private String Url="",message;
	private ArrayList<String> video_cate_id = new ArrayList<String>();
	private ArrayList<String> video_cate_title = new ArrayList<String>();
	private boolean exception = false;
	private  ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_categories);
		
		home = (ImageButton) findViewById(R.id.btn_home);
		home.setOnClickListener(this);
		
		pd = ProgressDialog.show(this, "Please Wait", "Loading ....", true,true);
		pd.setIcon(R.drawable.mlm);
		Thread thread = new Thread(this);
		thread.start();
		
	}
	@Override
	public void onClick(View v) 
	{
		int id = v.getId();
		if(id==R.id.btn_home)
		{
			startActivity(new Intent(VideoCategories.this,Home.class));
			finish();
		}
	}
	
	private void listData() 
	{
		list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        
        try
        {
//        	Url = "http://sli.hospitalu.com/iphone/Video.asmx/VideoList";
        	Url = getString(R.string.url)+"Video.asmx/VideoList?group_id="+GlobalVar.vGroupID;
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
        			runOnUiThread(nocoonection2);
        			exception = true;
        		}
        		else
	        	{
	        		for(int i=0;i<ja.length();i++)
	        		{
	        			JSONObject jo = (JSONObject) ja.get(i);
	        			video_cate_id.add(jo.getString("video_cate_id"));
	        			video_cate_title.add(jo.getString("video_cate_title"));
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
	          list_adapter = new AdapterForAll(VideoCategories.this,video_cate_title); 
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
			alert("Connection Error. Try again");
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
		if(message.equals("Connection Error. Try again"))
	 	{
	 		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Error");
			dialog.setMessage(message);
			dialog.setIcon(R.drawable.mlm);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					startActivity(new Intent(VideoCategories.this,Home.class));
					finish();
				}
			});
		 	dialog.show();
	 	}
	 	else
	 	{
		 	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Message");
			dialog.setMessage(message);
			dialog.setIcon(R.drawable.mlm);
		 	dialog.setPositiveButton("Ok", null);
		 	dialog.show();
	 	}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		String id = video_cate_id.get(arg2).toString();
		String title = video_cate_title.get(arg2).toString();
		intent = new Intent(VideoCategories.this,ByContentVedio.class);
		intent.putExtra("video_cate_id", id);
		intent.putExtra("title", title);
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
