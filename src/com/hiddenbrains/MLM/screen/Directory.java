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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.hiddenbrains.MLM.lib.GlobalVar;
import com.hiddenbrains.MLM.lib.GoogleTracker;

public class Directory extends Activity implements OnClickListener, OnItemClickListener, Runnable
{
	private ImageButton home,search;
	
//	private DirectoryAdapter list_adapter;
//	private ListView list;
	private String Url="";
	private ArrayList<String> member_name = new ArrayList<String>();
	private ArrayList<String> email = new ArrayList<String>();
	private ArrayList<String> contact_no = new ArrayList<String>();
	private ArrayList<String> vJobTitle = new ArrayList<String>();
	private boolean exception = false;	
	private EditText search_et;
	private String name_email,message;
	private int x=0;
	private  ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directory);
		
		search_et = (EditText) findViewById(R.id.search);
		home = (ImageButton) findViewById(R.id.btn_home);
		search = (ImageButton) findViewById(R.id.btn_search);
		home.setOnClickListener(this);
		search.setOnClickListener(this);
//		list = (ListView) findViewById(R.id.list);
//      list.setOnItemClickListener(this);
		
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
			startActivity(new Intent(Directory.this,Home.class));
			finish();
		}
		else if(id==R.id.btn_search)
		{
			String ss = search_et.getText().toString();
			name_email = ss.replace(" ", "%20");
		
			x=1;
			pd = ProgressDialog.show(this, "Please Wait", "Loading ....", true,true);
			pd.setIcon(R.drawable.mlm);
			Thread thread = new Thread(this);
			thread.start();
			
		}
	}
	
	private void listData() 
	{
        try
        {
        	if(x==1)
        	{
//        		Url = "http://sli.hospitalu.com/iphone/Directories.asmx/DirectoryList?keyword="+name_email;
//        		Url = "http://184.164.156.55/webservice/MLM/Directories.asmx/DirectoryList?group_id="+Login.group_id+"&keyword="+name_email;
        		Url = getString(R.string.url)+"Directories.asmx/DirectoryList?group_id="+GlobalVar.vGroupID+"&keyword="+name_email;
        		
        		exception = false;
        	}
        	else
        	{
//        		Url = "http://sli.hospitalu.com/iphone/Directories.asmx/DirectoryList";
//        		Url = "http://184.164.156.55/webservice/MLM/Directories.asmx/DirectoryList?group_id="+Login.group_id;
        		Url = getString(R.string.url)+"Directories.asmx/DirectoryList?group_id="+GlobalVar.vGroupID;
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
        			if(!member_name.isEmpty())
        			{
        				member_name.clear();
        				email.clear();
        				contact_no.clear();
        				vJobTitle.clear();
        			}
        			runOnUiThread(nocoonection2);
        			exception = false;
        		}
        		else
	        	{
        			if(!member_name.isEmpty())
        			{
        				member_name.clear();
        				email.clear();
        				contact_no.clear();
        				vJobTitle.clear();
        			}
        			member_name.clear();
	        		for(int i=0;i<ja.length();i++)
	        		{
	        			JSONObject jo = (JSONObject) ja.get(i);
	        			member_name.add(jo.getString("member_name"));
	        			email.add(jo.getString("email"));
	        			contact_no.add(jo.getString("contact_no"));
	        			vJobTitle.add(jo.getString("vJobTitle"));
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
//        		list_adapter = new DirectoryAdapter(Directory.this,member_name,email,contact_no); 
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
	 		AlertDialog.Builder dialog = new AlertDialog.Builder(Directory.this);
	 		dialog.setIcon(R.drawable.mlm);
			dialog.setTitle("Message");
			dialog.setMessage(message);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					startActivity(new Intent(Directory.this,Home.class));
					finish();
				}
			});
		 	dialog.show();
	 	}
	 	else
	 	{
	 		pd.dismiss();
		 	AlertDialog.Builder dialog = new AlertDialog.Builder(Directory.this);
		 	dialog.setIcon(R.drawable.mlm);
			dialog.setTitle("Message");
			dialog.setMessage(message);
		 	dialog.setPositiveButton("Ok", null);
		 	dialog.show();
	 	}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		
		
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
//				list.setAdapter(list_adapter);
//				pd.dismiss();
				LinearLayout l = (LinearLayout) findViewById(R.id.mylayout1);
				l.removeAllViews();
		  	    LayoutInflater linflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  	    for (int i = 0; i < member_name.size(); i++) 
		  	    {
		  	        View customView = linflater1.inflate(R.layout.directory_listview2,null);
		  	        TextView name = (TextView) customView.findViewById(R.id.member_name);
		  	        TextView email_id = (TextView) customView.findViewById(R.id.email);
		  	        TextView phone = (TextView) customView.findViewById(R.id.contact_no);
		  	        TextView job_title = (TextView) customView.findViewById(R.id.job_title);
		  	       
		  	        customView.setId(i);
		  	        email_id.setId(i);
		  	        phone.setId(i);
		  	        
		  	        name.setText(member_name.get(i).toString());
		  	        email_id.setText(email.get(i).toString());
		  	        phone.setText(contact_no.get(i).toString());
			  	    job_title.setText(vJobTitle.get(i).toString());
			  	    
			  	    if(i==0)
			  	    {
			  	    	customView.setBackgroundResource(R.drawable.strip_top_s4);
			  	    }
//			  	    if(i == (music_title.size()-1))
//			  	    {
//			  	    	customView.setBackgroundResource(R.layout.strip_bottom_s4);
//			  	    }
			  	    
			  	    email_id.setOnClickListener(new View.OnClickListener() 
			  	    {
			  	        @Override
			  	        public void onClick(View v) 
			  	        {
			  	        	int x = v.getId();
			  	        	String s_email = email.get(x).toString();
			  	        	sendEmail(s_email);
			  	        }

						private void sendEmail(String s_email) 
						{
							try
					    	{  
								/*GoogleTracker.track(Directory.this, "Directory", s_email);*/
								
								//GoogleAnalyticsTracker tracker ;
								//tracker = GoogleAnalyticsTracker.getInstance();
								//tracker.startNewSession("UA-25672706-3",Directory.this); 
								
								//tracker.trackEvent(Login.user,"Directory clicked",Login.info+s_email,1);
//								tracker.trackEvent(Login.info,"Directory",Login.user+" :-  Email--"+s_email,1);
								//tracker.dispatch();
								
								Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
							 
								emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{s_email}); 
								emailIntent.setType("plain/text");  
								startActivity(emailIntent);  
								
								/*startActivityForResult(
							            new Intent("android.intent.action.SEND", Uri.parse("EXTRA_EMAIL:"
							                + s_email)), 1);
*/
								/*
					    		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					    		
					            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{s_email});
					     
					            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
					     
					            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

					            emailIntent.setType("plain/text");
					        
					            startActivity(emailIntent);*/
					    	}
					        catch (Exception e) 
					        {
					        	e.printStackTrace();
							}
							
						}
			  	    });
			  	    phone.setOnClickListener(new View.OnClickListener() 
			  	    {
			  	        @Override
			  	        public void onClick(View v) 
			  	        {
			  	        	int x = v.getId();
			  	        	String s_phone = contact_no.get(x).toString();
			  	        	if (s_phone != null) 
			  	        	{
			  					if (!s_phone.equals("")) 
			  					{
			  						myPhoneCall(s_phone);
			  					}
			  				}
			  	        }

						private void myPhoneCall(String PhoneNumber) 
						{
							try 
							{
								/*
								 * GoogleAnalyticsTracker tracker ; tracker =
								 * GoogleAnalyticsTracker.getInstance();
								 * tracker.
								 * startNewSession("UA-25672706-3",Directory
								 * .this); tracker.trackEvent(GlobalVar.vUser,
								 * "Directory clicked"
								 * ,GlobalVar.vInfo+PhoneNumber,1);
								 * 
								 * //
								 * tracker.trackEvent(Login.info,"Directory",Login
								 * .user+" :-  Phone--"+PhoneNumber,1);
								 * tracker.dispatch();
								 */
								 startActivityForResult(
								            new Intent("android.intent.action.DIAL", Uri.parse("tel:"
								                + PhoneNumber)), 1);

								/*Intent callIntent = new Intent(Intent.DIAL);
								callIntent.setData(Uri.parse("tel:" + PhoneNumber));
								startActivity(callIntent);*/
								
							}
							catch (Exception e) 
							{
								e.printStackTrace();
							}
							
						}
			  	    });
			  	    
		  	        l.addView(customView);
		  	    }
				pd.dismiss();
			
			}catch (Exception e) 
			{
				e.getMessage();
			}
		}
	};
}
