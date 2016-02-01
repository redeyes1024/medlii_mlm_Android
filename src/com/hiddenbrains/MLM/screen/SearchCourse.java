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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hiddenbrains.MLM.lib.GlobalVar;

public class SearchCourse extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener
{
	private ImageButton back,search;
	
	private Intent intent;
//	private Spinner class_name;
	public static TextView class_name;
	private EditText course_title;
	private RadioGroup rg;
	private RadioButton radio_class,radio_title;
	public static ArrayList<String> spinner_list = new ArrayList<String>();
	public static ArrayAdapter<String> spinner_adapter;
	private boolean exception = false;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) throws IllegalArgumentException
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_course);
		try{
			
			back = (ImageButton) findViewById(R.id.btn_back);
			search = (ImageButton) findViewById(R.id.btn_search);
			back.setOnClickListener(this);
			search.setOnClickListener(this);
			
//			class_name = (Spinner) findViewById(R.id.spinner_class);
			class_name = (TextView) findViewById(R.id.spinner_class);
			class_name.setOnClickListener(this);
			
			course_title = (EditText) findViewById(R.id.coursetitle);
			rg = (RadioGroup) findViewById(R.id.search_type);
			rg.setOnCheckedChangeListener(this);
			
			radio_class = (RadioButton) findViewById(R.id.class_radio);
			radio_title = (RadioButton) findViewById(R.id.course_radio);
			listData();
			
			
		}catch (Exception e) {
			e.getMessage();
		}
		
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
			if(radio_class.isChecked())
			{
//				String course_class = class_name.getSelectedItem().toString();
				String course_class = class_name.getText().toString();
				if(course_class.equals("Select Class"))
				{
					alert("Class Field Is Required");
					
				}
				else
				{
					intent = new Intent(SearchCourse.this,Courses.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("course_class", course_class);
					intent.putExtra("course_title", "0");
					intent.putExtra("search", "search");
					startActivity(intent);
					finish();
				}
			}
			else if(radio_title.isChecked())
			{
				String course_title = this.course_title.getText().toString();
				if(course_title.equals("Enter Course Title"))
				{
					alert("Course Field Is Required");
					
				}else
				{
					intent = new Intent(SearchCourse.this,Courses.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("course_title", course_title);
					intent.putExtra("course_class", "0");
					intent.putExtra("search", "search");
					startActivity(intent);
					finish();
				}
			}
		}
		if(id==R.id.spinner_class)
		{
			SelectClassDialog dis = new SelectClassDialog(SearchCourse.this);
			dis.show();
		}
		
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		if(radio_class.isChecked())
		{
			class_name.setVisibility(View.VISIBLE);
			course_title.setVisibility(View.GONE);
		}
		else if(radio_title.isChecked())
		{
			class_name.setVisibility(View.GONE);
			course_title.setVisibility(View.VISIBLE);
		}
		
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
	private void listData() 
	{
        try
        {
        	
//        	String Url = "http://sli.hospitalu.com/iphone/Directories.asmx/Dropdown_class";
        	String Url = getString(R.string.url)+"Directories.asmx/Dropdown_class?group_id="+GlobalVar.vGroupID;
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
        			alert(message);
        			exception = true;
        		}
        		else
	        	{
        			if(!spinner_list.isEmpty())
        			{
        				spinner_list.clear();
        			}
//        			spinner_list.add("Select Class");
	        		for(int i=0;i<ja.length();i++)
	        		{
	        			JSONObject jo = (JSONObject) ja.get(i);
	        			spinner_list.add(jo.getString("vClassname"));
	        		}
	        	}	
        	}
        	else
        	{
        		alert("No Connections Available!");
        		
        	    exception = true;
        	}
        	
        	if(!exception)
        	{ 
        		spinner_adapter = new ArrayAdapter<String>(this,R.layout.spinner_text, spinner_list);
//        		class_name.setAdapter(spinner_adapter);
//        		class_name.setPrompt("Select Class");
//        		class_name.setOnItemSelectedListener(this);
        		
        	}
        }catch (Exception e) 
        {
			e.printStackTrace();
			alert("No Connections Available!");
    	    exception = true;
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
//		spinner_list.add(0, "Select Class");
	}
	
}
