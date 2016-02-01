package com.hiddenbrains.MLM.screen;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SelectClassDialog extends Dialog implements OnItemClickListener 
{
	private TextView txt;
	boolean valid;
	private ListView list;
	private Activity activity;
	private String test;
	
	public SelectClassDialog(Activity c) 
	{
		super(c);
		activity = c;
	}
	
	public SelectClassDialog(Registration registration, String s) 
	{
		super(registration);
		activity = registration;
		test = s;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.class_spinner);
		txt = (TextView) findViewById(R.id.textView1);
		list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        
        if(activity.getClass()==SearchCourse.class)
        {
        	txt.setText("Select Class");
        	list.setAdapter(SearchCourse.spinner_adapter);
        }
        else
        {
        	if(test.equals("group"))
        	{
        		txt.setText("Select Group");
        		list.setAdapter(Registration.spinner_adapter2);
        	}
        	else
        	{
        		txt.setText("Select Company");
        		list.setAdapter(Registration.spinner_adapter);
        	}
        	
//        	list.setAdapter(Registration.spinner_adapter);
        }
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		arg1.setBackgroundColor(Color.GRAY);
		if(activity.getClass()==SearchCourse.class)
        {
			SearchCourse.class_name.setText(SearchCourse.spinner_list.get(arg2).toString());
        }
        else
        {
        	/*if(test.equals("group"))
        	{
        		Registration.group.setText(Registration.spinner_list2.get(arg2).toString());
        		Registration.g_id_index = arg2;
        	}
        	else
        	{
        		Registration.company.setText(Registration.spinner_list.get(arg2).toString());
        		Registration.c_id_index = arg2;
        	}*/
//        	Registration.company.setText(Registration.spinner_list.get(arg2).toString());
        }
//		SearchCourse.class_name.setText(SearchCourse.spinner_list.get(arg2).toString());
		dismiss();
	}
	
}

