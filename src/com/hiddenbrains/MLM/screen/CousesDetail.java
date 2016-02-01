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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

public class CousesDetail extends Activity implements OnClickListener, Runnable
{
	private ImageButton back;
	
	private TextView name,date;
//	description;
	private WebView webview;
	private String Url="";
	private Bundle bundle;
	private boolean exception = false;	
	private String course_name,course_date,course_desc,no_class;
	private ArrayList<String> class_name = new ArrayList<String>();
	private ArrayList<String> class_date = new ArrayList<String>();
	private ArrayList<String> duration = new ArrayList<String>();
	private  ProgressDialog pd;
	private String course_id,message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_detail);
		
		back = (ImageButton) findViewById(R.id.btn_back);
		back.setOnClickListener(this);
		
		try
		{
			bundle = getIntent().getExtras();
			course_id = bundle.getString("course_id");
			no_class = bundle.getString("no_class");
			
		}catch (Exception e) 
		{
			e.getMessage();
		}
		
		
		name = (TextView) findViewById(R.id.course);
		date = (TextView) findViewById(R.id.date_time);
//		description = (TextView) findViewById(R.id.description);
		webview = (WebView) findViewById(R.id.description);
		
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

	private void setData() 
	{
		try
        {
		
//        	Url = "http://sli.hospitalu.com/iphone/Directories.asmx/CourseDetail_a?course_id="+course_id;
//        	Url = "http://184.164.156.55/webservice/MLM/Directories.asmx/CourseDetail?course_id="+course_id;
        	Url = getString(R.string.url)+"/Directories.asmx/CourseDetail?course_id="+course_id;
        	 
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
        			JSONObject jo1 = (JSONObject) ja.get(0);
	        		course_name = jo1.getString("course_name");
	        		course_date = jo1.getString("course_date");
	        		course_desc = jo1.getString("course_desc");
	        		
        			for(int i=0;i<ja.length();i++)
	        		{
		        		JSONObject jo = (JSONObject) ja.get(i);
		        		class_name.add(jo.getString("class_name"));
		        		class_date.add(jo.getString("class_date"));
		        		duration.add(jo.getString("duration"));
		        		
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

        	}
        }catch (Exception e) 
        {
			e.printStackTrace();
			runOnUiThread(nocoonection);
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
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		 	dialog.show();
	 	}
		
	}

	@Override
	public void run() 
	{
		setData();
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
//				Spannable sp = (Spannable) Html.fromHtml(course_desc);
	    		
	    		name.setText(course_name);
	    		date.setText(course_date);
//	    		description.setText(sp);
	    		final String mimeType = "text/html";
		    	final String encoding = "utf-8";
	    		webview.loadDataWithBaseURL(course_desc, course_desc, mimeType, encoding, null);
	    		
	    		int x = Integer.parseInt(no_class);
	    		if(x==0)
	    		{
	    			TableLayout table1 = (TableLayout) findViewById(R.id.table_head);
	    			TextView text =  (TextView) findViewById(R.id.class_text);
	    			text.setText("No Class found.");
	    			table1.setVisibility(View.INVISIBLE);
	    		}
	    		else
	    		{
		    		TableLayout table = (TableLayout) findViewById(R.id.table_body);
		    		LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    		
		    		table.removeAllViews();
					
		    		for(int i=0;i<class_name.size();i++)
		    		{
		    			View rowview = linflater.inflate(R.layout.table_layout, null);
		    			
		    			TextView sno,cls,time,due; 
		    			
		    			sno = (TextView) rowview.findViewById(R.id.sno);
		    			cls = (TextView) rowview.findViewById(R.id.class_no);
						time = (TextView) rowview.findViewById(R.id.time);
						due = (TextView) rowview.findViewById(R.id.duration);
						
						String no = String.valueOf(i+1);
						sno.setText(no);
						cls.setText(class_name.get(i));
						time.setText(class_date.get(i));
						due.setText(duration.get(i)+" Hours");
						
						table.addView(rowview);
		    		}
	    		}
	    		pd.dismiss();
			}catch (Exception e) 
			{
				e.getMessage();
			}
		}
	};
}