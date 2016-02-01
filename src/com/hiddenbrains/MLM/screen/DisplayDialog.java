package com.hiddenbrains.MLM.screen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;


public class DisplayDialog extends Dialog implements android.view.View.OnClickListener 
{
	boolean valid;
	private Context context;
	private ImageButton cancel,submit;
	private EditText email;
	public DisplayDialog(Context c) 
	{
		super(c);
		context = c;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.dialog);
		
		email = (EditText) findViewById(R.id.email);
		
		cancel = (ImageButton) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(this);
		
		submit = (ImageButton) findViewById(R.id.btn_submit);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		int id= v.getId();
		
		if(id==R.id.btn_cancel)
		{
			dismiss();
		}
		else if(id==R.id.btn_submit)
		{
			String cs = email.getText().toString();
			
			sendEmail(cs);	
			
		}
	}
	private void sendEmail(String cs) 
	{
		String email = cs;
		
		if(email.equals(""))
		{
			alert("Enter Email");
		}
		else if(!email.equals(""))
		{
			valid = emailValidator(email);
		}
		if(valid)
		{
//			String Login_Url = "http://184.164.156.55/webservice/MLM/User.asmx/ForgotPassword?email="+email;
			String Login_Url ="http://mlm.hospitalu.com/iphone/User.asmx/ForgotPassword?email="+email;
		 
			try
	        {
	        	URL url = new URL(Login_Url);
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
	        		String message = OBJECT.getString("message");
	        		
	        		alert(message);
	        		if(success.equalsIgnoreCase("1"))
	        		{
	        			dismiss();
	        		}
	        		
	        	}
	        	else
	        	{ 
	        		dismiss();
	        	}
	        }
			catch (Exception e) 
			{
			   e.printStackTrace();
			}
		}

	}
	
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
	private boolean emailValidator(String s)
	{
		String cs = s;
		String message ="";
		cs.trim();
		boolean valid = true;
		if(cs.equals(""))
		{
			message= "Email Address should not be empty.";
			alert(message);
		    valid = false;
		}
		
		else 
		{	
			try
			{
		    char c=cs.charAt(0);
			if(Character.isDigit(c)|| c=='.' || c=='_') //For First Letter Non-Digit and not a dot,underscore operator 
			{
				message= "Email Address should start with alphabet.";
				alert(message);
				valid = false;
			}
			else if(cs.indexOf('@')==-1)
			{
				message= "Email Address should contain '@' symbol";
				alert(message);
				valid = false;
			}
			else if(cs.indexOf('.')==-1)
			{
				message= "Email Address should contain '.' symbol";
				alert(message);
				valid = false;
			}
			else  
			{												//For Last Letter not a dot,underscore operator 
				int length = cs.indexOf('@');
				char last = cs.charAt(length-1);
				if(last=='_'||last=='.')
				{
					message= "Email Address should not end with  a dot or underscore.";
					alert(message);
					valid = false;
				}
				else
				{ 
					
					//Regular Expression like .com
					String str="^[a-z_.A-Z0-9]{2,40}+@[a-z]+?\\.[a-z]{2,6}$";
					
					//Regular Expression like .co.in
					String str1="^[a-z_.A-Z0-9]{2,40}+@[a-z]+?\\.[a-z]{2,6}\\.[a-z]{2}$";
				    
					Pattern p=Pattern.compile(str);
					Matcher m=p.matcher(cs);
					Pattern p1=Pattern.compile(str1);
					Matcher m2=p1.matcher(cs);
					
					if(!(m.find() || m2.find()))
					{
						message= "Enter a valid domain name.";
						alert(message);			 
						valid = false;
					}
					else
					{
						valid = true;
					}
				}
			}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return valid;
	}
	private void alert(String string) 
	{
		String message = string;
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Message");
		dialog.setIcon(R.drawable.mlm);
		dialog.setMessage(message);
	 	dialog.setPositiveButton("Ok", null);
	 	dialog.show();
	}
}
