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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hiddenbrains.MLM.Adapter.AdapterForAll;
import com.hiddenbrains.MLM.lib.GoogleTracker;

public class SafetyDocuments extends Activity implements OnClickListener, OnItemClickListener, Runnable
{
	private ImageButton back;
	
	private Intent intent;
	private AdapterForAll list_adapter;
	private ListView list;
	private String Url="",title_s;
	private ArrayList<String> doc_url = new ArrayList<String>();
	private ArrayList<String> doc_name = new ArrayList<String>();
	private boolean exception = false;	
	private Bundle bundle;
	private  ProgressDialog pd;
	private String lib_cate_id,message;
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safety_documents);
		
		title = (TextView) findViewById(R.id.title);
		back = (ImageButton) findViewById(R.id.btn_back);
		back.setOnClickListener(this);
		try
		{
			bundle = getIntent().getExtras();
			lib_cate_id = bundle.getString("lib_cate_id");
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
	}

	private void listData() 
	{
		
		list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        
        try
        {
//        	Url = "http://sli.hospitalu.com/iphone/Library.asmx/SafetyDocuments?lib_cate_id="+lib_cate_id;
        	Url = getString(R.string.url)+"Library.asmx/SafetyDocuments?lib_cate_id="+lib_cate_id;
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
	        			doc_url.add(jo.getString("doc_url"));
	        			doc_name.add(jo.getString("doc_name"));
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
	          list_adapter = new AdapterForAll(SafetyDocuments.this,doc_name); 
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
		String url = doc_url.get(arg2).toString();
		if(url.equals("no-image.png"))
		{
			alert("This PDF is not valid for streaming to this device.");
		}
		else
		{
			String label = doc_name.get(arg2).toString();
			GoogleTracker.track(this, "Library", label);
			
//			GoogleAnalyticsTracker Code Starts Here 
			
//			GoogleAnalyticsTracker tracker ;
//			tracker = GoogleAnalyticsTracker.getInstance();
//			tracker.startNewSession("UA-25672706-3",this); 
//			tracker.setCustomVar(4,"Library",Login.user+" :- "+url,1);
//			tracker.trackEvent(Login.user,"Library clicked",Login.info+label+"----"+url,1);
//			tracker.trackEvent(Login.info,"Library",Login.user+" :- "+label,1);
			
//			tracker.dispatch();
//			GoogleAnalyticsTracker Code End Here 	
			
//			intent = new Intent(SafetyDocuments.this,Pdf.class);
//			intent.putExtra("url", url);
//			startActivity(intent);
			
			Uri uri = Uri.parse("http://docs.google.com/viewer?url="+url);
			intent = new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
		}
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

