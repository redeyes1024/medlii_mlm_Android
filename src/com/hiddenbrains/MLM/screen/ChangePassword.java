package com.hiddenbrains.MLM.screen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChangePassword extends Dialog implements OnClickListener
{
	SharedPreferences savesetting;
	boolean valid;
	private android.content.Context context;
	private ImageButton cancel,submit;
	private EditText old_pwd,new_pwd,con_pwd;
	public ChangePassword(Context c) 
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
		setContentView(R.layout.change_password);
		
		old_pwd = (EditText) findViewById(R.id.old_password);
		new_pwd = (EditText) findViewById(R.id.new_password);
		con_pwd = (EditText) findViewById(R.id.re_new_password);
		
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
			sendData();
		}
	}
	
	
	private void sendData() 
	{
		String old_password,new_password,confirm_password;
		
		try
        {
			old_password = old_pwd.getText().toString();
			new_password = new_pwd.getText().toString();
			confirm_password = con_pwd.getText().toString();
			
			String user_id = Login.id;
			
			if(old_password.equals(""))// || new_password.equals("")||confirm_password.equals(""))
	    	{
	    		alert("Old Password Should not be Blank.");
	    	}
			else if(new_password.equals(""))
	    	{
	    		alert("Please Enter New Password");
	    	}
			else if(new_password.length()<5)
			{
				String message= "Password Must Contain Min. 5 Characters.";
				alert(message);
			}
			else if(!confirm_password.equals(new_password))
	    	{
	    		alert("Password and Re-enter Password must be same.");
	    	}
			else
			{
//				String Login_Url = "http://184.164.156.55/webservice/MLM/User.asmx/changepassword?user_id="+user_id+"&old_password="+old_password+"&new_password="+new_password+"&confirm_password="+confirm_password+"&alerts_email= ";
				

				String Login_Url = "http://mlm.hospitalu.com/iphone/User.asmx/changepassword?user_id="+user_id+"&old_password="+old_password+"&new_password="+new_password+"&confirm_password="+confirm_password+"&alerts_email= ";
			
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
		        		if(success.equalsIgnoreCase("0"))
		        		{
			        		alert(message);
		        		}
		        		else
			        	{	
		        			savesetting = PreferenceManager.getDefaultSharedPreferences(context);
		        			SharedPreferences.Editor editor = savesetting.edit();
		        			editor.putString("password", confirm_password);
		        			editor.commit();
		        			alert(message);

		        		}
		        	}
		        	else
		        	{
		        		alert("Connections Error. Try again");
		        	}
		        }
				
			}
		catch (Exception e) 
		{
		   e.printStackTrace();
		   alert("No Connections Available!");
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
	private void alert(String string) 
	{
		String message = string;
		if(message.equals("No Connections Available!"))
	 	{
	 		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Error");
			dialog.setMessage(message);
			dialog.setIcon(R.drawable.mlm);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					finish();
					dismiss();
				}
			});
		 	dialog.show();
	 	}
		if(message.equals("Password Updated Successfully."))
	 	{
	 		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Message");
			dialog.setMessage(message);
			dialog.setIcon(R.drawable.mlm);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dismiss();
//					startActivity(new Intent(context.this,Login.class));
//        			finish();
				}
			});
		 	dialog.show();
	 	}
	 	else 
	 	{
		 	AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Message");
			dialog.setMessage(message);
			dialog.setIcon(R.drawable.mlm);
		 	dialog.setPositiveButton("Ok", null);
		 	dialog.show();
	 	}
	}

}