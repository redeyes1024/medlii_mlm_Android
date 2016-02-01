package com.hiddenbrains.MLM.screen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

public class EventDetail extends Activity implements OnClickListener, Runnable
{
	private ImageButton back;
	private TextView name,date;//description;
	private WebView webview;
	private String Url="";
	private Bundle bundle;
	private boolean exception = false;	
	private String event_name,event_date,event_desc,event_id,message;
	private  ProgressDialog pd;
	Vector<String> data = new Vector<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_detail);
		
		back = (ImageButton) findViewById(R.id.btn_back);
		back.setOnClickListener(this);
		
		try
		{
			bundle = getIntent().getExtras();
			event_id = bundle.getString("event_id");
		}catch (Exception e) 
		{
			e.getMessage();
		}
		
		
		name = (TextView) findViewById(R.id.event);
		date = (TextView) findViewById(R.id.date_time);
//		description = (TextView) findViewById(R.id.description);
		webview = (WebView) findViewById(R.id.description);
////		fowfnwg;wenf
//		webview = (WebView) findViewById(R.id.webkitWebView1);
//    	webview.setFocusableInTouchMode(false);
//    	webview.getSettings().setJavaScriptEnabled(true);
//    	webview.setVerticalScrollBarEnabled(false);
//    	webview.setHorizontalScrollBarEnabled(false);
//    	webview.addJavascriptInterface(this, "interface");
//    	webview.addJavascriptInterface(this, "contactSupport");
////    	webview.setClickable(false);
//    	webview.setFocusable(true);
////    	webview.setTouchDelegate(null);
//
//    	webview.setOnTouchListener(new View.OnTouchListener() {
//    	    @Override
//    	    public boolean onTouch(View v, MotionEvent event) {
//    	    	WebView.HitTestResult hr = ((WebView) v).getHitTestResult();
//				boolean values=false;
//				
//				try{
//				s = hr.getExtra();
//				String extra = "~LinkAction~sendemail~email";
//				String extra1 = "~LinkAction~phone~"+data.elementAt(4).toString();
//				String extra2 = "~LinkAction~openbrowser~"+data.elementAt(5).toString();
//				if(event.getAction()==1)
//				{
//					if(s.equals(extra))
//					{
//						try 
//						{
//					    	String str1=data.elementAt(6).toString();
//					    	Log.d("DS",s +" Checking! : "+str1);
//					    	Intent i = new Intent(android.content.Intent.ACTION_SEND);
//					    	i.setType("text/plain");
//					    	i.putExtra(android.content.Intent.EXTRA_EMAIL  , new String[]{str1});
//					    	i.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject of email");
//					    	i.putExtra(android.content.Intent.EXTRA_TEXT  , "body of email");
//					    	startActivity(android.content.Intent.createChooser(i, "Send mail..."));
////					    	flag = true;
//					    	values = true;
//					    	webview.invalidate();
//					    	webview.clearFocus();
////					    	webview.setNextFocusUpId(R.id.btn_back);
//					    	webview.computeScroll();
//					    	
//					    } 
//						catch (android.content.ActivityNotFoundException e) 
//				    	{
//				    	    e.getMessage();
//				    	}
//					}else if(s.equals(extra1))
//					{
//						try
//						{
//							
//					    	Intent callIntent = new Intent(Intent.ACTION_CALL);
//							String str="tel:"+data.elementAt(4).toString();
//						    callIntent.setData(Uri.parse(str));
//						    startActivity(callIntent);
//						    Log.d("DS",s +" Checking : "+str);
//						    values = true;
//						   
//						    webview.clearFocus();
//						    webview.invalidate();
////						    webview.setNextFocusDownId(R.id.btn_back);
//						    webview.computeScroll();
//					    }
//						catch(Exception e)
//						{
//				    		e.getMessage();
//				    	}
//					}else if(s.equals(extra2))
//					{
//						try
//						{
//							Intent webIntent = new Intent(Intent.ACTION_VIEW);
//							
//							String str=""+data.elementAt(5).toString();
//							
//							
//							webIntent.setData(Uri.parse(str));
//						    startActivity(webIntent);
//						    
//						    values = true;
//						    webview.clearFocus();
//						    webview.invalidate();
////						    webview.setNextFocusDownId(R.id.btn_back);
//						    webview.computeScroll();
//					    }
//						catch(Exception e)
//						{
//				    		e.getMessage();
//				    	}
//					}
//					else
//					{
//						values = true;
//					}
//				}
//				}catch (Exception e) {
//					values=true;
//				}
//				
//    	    	return values;
//    	    }
//    	});
//		
//		
//		
//		
//		efjiowenfpioweuhf
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
		
//        	Url = "http://sli.hospitalu.com/iphone/Directories.asmx/EventDetail?event_id="+event_id;
//        	Url = "http://184.164.156.55/webservice/MLM/Directories.asmx/EventDetail?event_id="+event_id;
			Url = getString(R.string.url)+"Directories.asmx/EventDetail?event_id="+event_id;
        	
        	
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
	        		JSONObject jo = (JSONObject) ja.get(0);
	        		event_name = jo.getString("event_name");
	        		event_date = jo.getString("event_date");
	        		event_desc = jo.getString("event_desc");
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
	 		dialog.setIcon(R.drawable.mlm);
			dialog.setTitle("Error");
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
		 	dialog.setIcon(R.drawable.mlm);
			dialog.setTitle("Message");
			dialog.setMessage(message);
		 	dialog.setPositiveButton("Ok", null);
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
//				Spannable sp = (Spannable) Html.fromHtml(event_desc);
	    		
	    		name.setText(event_name);
	    		date.setText(event_date);
//	    		description.setText(sp);
	    		
	    		final String mimeType = "text/html";
		    	final String encoding = "utf-8";
	    		webview.loadDataWithBaseURL(event_desc, event_desc, mimeType, encoding, null);
	        	
				pd.dismiss();
			}catch (Exception e) 
			{
				e.getMessage();
			}
			
		}
		
	};
}