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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Settings extends Activity implements OnClickListener
{
	SharedPreferences savesetting;
	private ImageButton home,logout,on1,on2,off1,off2;
	private EditText old_pwd,new_pwd,con_pwd;
	private String alerts_email="On",Url;
	private int x=0;
	private boolean auto_login=false;
	private String user_id = Login.id;
	private RelativeLayout layout_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		home = (ImageButton) findViewById(R.id.btn_home);
		logout = (ImageButton) findViewById(R.id.btn_logout);
		layout_password = (RelativeLayout) findViewById(R.id.layout_password);
		on1 = (ImageButton) findViewById(R.id.btn_on1);
		on2 = (ImageButton) findViewById(R.id.btn_on2);
		off1 = (ImageButton) findViewById(R.id.btn_off1);
		off2 = (ImageButton) findViewById(R.id.btn_off2);
		
		home.setOnClickListener(this);
		logout.setOnClickListener(this);
		layout_password.setOnClickListener(this);
		on1.setOnClickListener(this);
		on2.setOnClickListener(this);
		off1.setOnClickListener(this);
		off2.setOnClickListener(this);
		
		old_pwd = (EditText) findViewById(R.id.old_password);
		new_pwd = (EditText) findViewById(R.id.new_password);
		con_pwd = (EditText) findViewById(R.id.re_new_password);
		
		SharedPreferences app_preferences1 =	PreferenceManager.getDefaultSharedPreferences(this);
		
		if(app_preferences1.getBoolean("auto_login", false))
		{
			
			off1.setVisibility(View.GONE);
			on1.setVisibility(View.VISIBLE);
		}
		else 
		{
			on1.setVisibility(View.GONE);
			off1.setVisibility(View.VISIBLE);
		}
		
		alertEmail();
		
	}
	private void alertEmail() 
	{
		try
        {
			if(x==1)
	    	{
//				Url = "http://sli.hospitalu.com/iphone/User.asmx/changepassword?user_id="+user_id+"&alerts_email="+alerts_email;
				Url = getString(R.string.url)+"User.asmx/changepassword?user_id="+user_id+"&alerts_email="+alerts_email;
			}
			else
			{
			
//				Url = "http://sli.hospitalu.com/iphone/User.asmx/SettingView?user_id="+user_id;
				Url = getString(R.string.url)+"User.asmx/SettingView?user_id="+user_id;
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
		        		String message = OBJECT.getString("message");
		        		
		        		if(success.equalsIgnoreCase("0"))
		        		{
//			        		alert(message);
		        		}
		        		else
			        	{	
		        			alerts_email = OBJECT.getString("alerts_email");
			        		if(alerts_email.equals("Off"))
			        		{
			        			on2.setVisibility(View.GONE);
			    				off2.setVisibility(View.VISIBLE);
			        		}
			        		else if(alerts_email.equals("On"))
			        		{
			        			off2.setVisibility(View.GONE);
			    				on2.setVisibility(View.VISIBLE);
			        		}
		        		}
		        	}
		        	else
		        	{
		        		alert("Connection Error. Try again");
		        	}
				
			}
		catch (Exception e) 
		{
		   e.printStackTrace();
		   alert("No Connections Available!");
		}
		
	}
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
			case R.id.btn_home:
				startActivity(new Intent(Settings.this,Home.class));
				finish();
				break;
			case R.id.btn_logout:
				startActivity(new Intent(Settings.this,Login.class));
				finish();
				break;
			case R.id.layout_password:
				ChangePassword change_pwd = new ChangePassword(Settings.this);
				change_pwd.show();
//				sendData();
				break;
			case R.id.btn_on1:
				on1.setVisibility(View.GONE);
				off1.setVisibility(View.VISIBLE);
				auto_login = false;
				saveData();
				break;
			case R.id.btn_on2:
				on2.setVisibility(View.GONE);
				off2.setVisibility(View.VISIBLE);
				alerts_email = "Off";
				x=1;
				alertEmail();
				break;
			case R.id.btn_off1:
				off1.setVisibility(View.GONE);
				on1.setVisibility(View.VISIBLE);
				auto_login = true;
				saveData();
				break;
			case R.id.btn_off2:
				off2.setVisibility(View.GONE);
				on2.setVisibility(View.VISIBLE);
				alerts_email = "On";
				x=1;
				alertEmail();
				break;
		}
		
	}
	private void saveData() 
	{
		SharedPreferences app_preferences =	PreferenceManager.getDefaultSharedPreferences(this);
		app_preferences.getString("username", "");
		app_preferences.getString("password", "");

		savesetting = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = savesetting.edit();
		editor.putBoolean("auto_login", auto_login);
		editor.commit();
		
	}
	private void sendData() 
	{
		String old_password,new_password,confirm_password;
		
		try
        {
			old_password = old_pwd.getText().toString();
			new_password = new_pwd.getText().toString();
			confirm_password = con_pwd.getText().toString();
			
			user_id = Login.id;
			
			if(old_password.equals(""))// || new_password.equals("")||confirm_password.equals(""))
	    	{
	    		alert("Old Password Should not be Blank.");
	    	}
			else if(new_password.equals(""))
	    	{
	    		alert("Please Enter New Password");
	    	}
			else if(new_password.length()<4)
			{
				String message= "Password Must Contain Min. 4 Characters.";
				alert(message);
			}
			else if(!confirm_password.equals(new_password))
	    	{
	    		alert("Password and Re-enter Password must be same.");
	    	}
			else
			{
//				String Login_Url = "http://sli.hospitalu.com/iphone/User.asmx/changepassword?user_id="+user_id+"&old_password="+old_password+"&new_password="+new_password+"&confirm_password="+confirm_password+"&alerts_email="+alerts_email;
				String Login_Url = getString(R.string.url)+"User.asmx/changepassword?user_id="+user_id+"&old_password="+old_password+"&new_password="+new_password+"&confirm_password="+confirm_password+"&alerts_email="+alerts_email;
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
		        			savesetting = PreferenceManager.getDefaultSharedPreferences(this);
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
	 		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Error");
			dialog.setMessage(message);
			dialog.setIcon(R.drawable.mlm);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		 	dialog.show();
	 	}
		if(message.equals("Password Updated Successfully."))
	 	{
	 		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Message");
			dialog.setMessage(message);
			dialog.setIcon(R.drawable.mlm);
		 	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					startActivity(new Intent(Settings.this,Login.class));
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

}
