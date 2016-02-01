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

import com.hiddenbrains.MLM.Adapter.CoursesAdapter;
import com.hiddenbrains.MLM.lib.GlobalVar;
import com.hiddenbrains.MLM.lib.GoogleTracker;

public class Courses extends Activity implements OnClickListener, OnItemClickListener, Runnable
{
	private ImageButton home,search;
	private Intent intent;
	
	private CoursesAdapter list_adapter;
	private ListView list;
	private String Url="",ss=" ",course_class,course_title="",message;
	private ArrayList<String> course_id = new ArrayList<String>();
	private ArrayList<String> course_name = new ArrayList<String>();
	private ArrayList<String> course_date = new ArrayList<String>();
	private ArrayList<String> classname = new ArrayList<String>();
	private ArrayList<String> no_class = new ArrayList<String>();
	private boolean exception = false;	
	private Bundle bundle;
	private  ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courses);

		home = (ImageButton) findViewById(R.id.btn_home);
		search = (ImageButton) findViewById(R.id.search);
		home.setOnClickListener(this);
		search.setOnClickListener(this);
		try
		{
			bundle = getIntent().getExtras();
			course_class = bundle.getString("course_class");
			course_title = bundle.getString("course_title");
			ss = bundle.getString("search");
			course_class = course_class.replace(" ", "%20");
			course_title = course_title.replace(" ", "%20");
			
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
		if(id==R.id.btn_home)
		{
			startActivity(new Intent(Courses.this,Home.class));
			finish();
		}
		else if(id==R.id.search)
		{
			startActivity(new Intent(Courses.this,SearchCourse.class));
			
		}
	}

	private void listData() 
	{
		list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        
        try
        {
        	if(ss.equals("search"))
        	{ 
        		if(course_class.equals("0"))
        		{
//        			Url = "http://sli.hospitalu.com/iphone/Directories.asmx/CourseList?course_title="+course_title;
//        			Url = "http://184.164.156.55/webservice/MLM/Directories.asmx/CourseList?group_id="+Login.group_id+"&course_title="+course_title;
        			Url =getString(R.string.url)+"Directories.asmx/CourseList?group_id="+GlobalVar.vGroupID+"&course_title="+course_title;
        			
        		}
        		else
        		{
//        			Url = "http://sli.hospitalu.com/iphone/Directories.asmx/CourseList?course_class="+course_class;
//        			Url = "http://184.164.156.55/webservice/MLM/Directories.asmx/CourseList?group_id="+Login.group_id+"&course_class="+course_class;
        			Url =getString(R.string.url)+"Directories.asmx/CourseList?group_id="+GlobalVar.vGroupID+"&course_class="+course_class;
        			 
        		}
        		
        	}
        	else
        	{
//        		Url = "http://184.164.156.55/webservice/MLM/Directories.asmx/CourseList?group_id="+Login.group_id;
        		Url =getString(R.string.url)+"Directories.asmx/CourseList?group_id="+GlobalVar.vGroupID;
        		
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
        			runOnUiThread(nocoonection2);
        			exception = true;
        		}
        		else
	        	{
	        		for(int i=0;i<ja.length();i++)
	        		{
	        			JSONObject jo = (JSONObject) ja.get(i);
	        			course_id.add(jo.getString("course_id"));
	        			course_name.add(jo.getString("course_name"));
	        			course_date.add(jo.getString("course_date"));
	        			classname.add(jo.getString("classname"));
	        			no_class.add(jo.getString("no_class"));
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
	          list_adapter = new CoursesAdapter(Courses.this,course_name,course_date); 
        	}
        }catch (Exception e) 
        {
			e.printStackTrace();
			runOnUiThread(nocoonection);
    	    exception = true;
		}
		
	}			
	private Runnable nocoonection=new Runnable() 
	{
		@Override
		public void run() 
		{
			alert("Connection Error. Try again");
		}
	};
	private Runnable nocoonection2=new Runnable() 
	{
		@Override
		public void run() 
		{
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
			dialog.setIcon(R.drawable.mlm);
			dialog.setMessage(message);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		 	{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					startActivity(new Intent(Courses.this,Home.class));
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
		String label = course_name.get(arg2).toString();
		GoogleTracker.track(this, "Courses", label);
		
		
		
		
//		GoogleAnalyticsTracker tracker ;
//		tracker = GoogleAnalyticsTracker.getInstance();
//		tracker.startNewSession("UA-25672706-3",this); 
		
//		tracker.trackEvent(Login.info,"Courses",Login.user+" :- "+label,1);
//		tracker.trackEvent(Login.user,"Courses clicked",Login.info+label,1);
//		tracker.dispatch();
//		GoogleAnalyticsTracker Code End Here 
		
		String id = course_id.get(arg2).toString();
		String cls = no_class.get(arg2).toString();
		intent = new Intent(Courses.this,CousesDetail.class);
		intent.putExtra("course_id", id);
		intent.putExtra("no_class", cls);
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

