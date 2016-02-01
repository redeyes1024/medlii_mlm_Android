package com.hiddenbrains.MLM.screen;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SearchEvent extends Activity implements OnClickListener, OnCheckedChangeListener 
{
	private Intent intent;
	private ImageButton back,search;
	private TextView date;
	private EditText event_title;
	private RadioGroup rg;
	private RadioButton radio_date,radio_title;
	
	private int mYear;
	private int mMonth;
	private int mDay;
	private int id=0;
	static final int DATE_DIALOG_ID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) throws IllegalArgumentException
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_event);
		date = (TextView) findViewById(R.id.spinner_date);
		event_title = (EditText) findViewById(R.id.event_title);
		rg = (RadioGroup) findViewById(R.id.search_type);
		rg.setOnCheckedChangeListener(this);
		back = (ImageButton) findViewById(R.id.btn_back);
		search = (ImageButton) findViewById(R.id.btn_search);
		
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		date.setOnClickListener(this);
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        radio_date = (RadioButton) findViewById(R.id.radio_date);
		radio_title = (RadioButton) findViewById(R.id.radio_event_title);
		
		
	}
	
	@Override
	public void onClick(View v) 
	{
		int id = v.getId();
		if(id==R.id.btn_back)
		{
			finish();
		}
		else if(id==R.id.spinner_date)
		{
			 showDialog(DATE_DIALOG_ID);
		}
		else if(id==R.id.btn_search)
		{
			if(radio_date.isChecked())
			{
				String event_date = date.getText().toString();
				if(event_date.equals("Select Date"))
				{
					alert("Date is Required!");
					
				}
				else
				{
					intent = new Intent(SearchEvent.this,Events.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("event_date", event_date);
					intent.putExtra("event_title", "0");
					intent.putExtra("search", "search");
					startActivity(intent);
					finish();
				}
			}
			else if(radio_title.isChecked())
			{
				String event_title = this.event_title.getText().toString();
				if(event_title.equals("Enter Event Title"))
				{
					alert("Event Title is Required!");
					
				}
				else
				{
					intent = new Intent(SearchEvent.this,Events.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("event_title", event_title);
					intent.putExtra("event_date", "0");
					intent.putExtra("search", "search");
					startActivity(intent);
					finish();
				}
			}
			
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) 
	{
	    switch (id) 
	    {
	    	case DATE_DIALOG_ID:
	    		return new DatePickerDialog(this,mDateSetListener,mYear, mMonth, mDay);
	    }
	    return null;
	}
	private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

         
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				 mYear = year;
	                mMonth = monthOfYear;
	                mDay = dayOfMonth;
	                updateDisplay(id);
			}
        };
	
	private void updateDisplay(int id) 
	{
		 // Month is 0 based so add 1
		if(mDay<10&&mMonth<10)
		{
			StringBuilder sb = new StringBuilder()
			.append(mYear).append("-").append("0")
	        .append(mMonth + 1).append("-")
	        .append("0").append(mDay).append("");
			
			 String s = sb.toString();
			 date.setText(s);
		}else if(mDay<10)
		{
			StringBuilder sb = new StringBuilder()
			.append(mYear).append("-")
	        .append(mMonth + 1).append("-")
	        .append("0").append(mDay).append("");
			
			 String s = sb.toString();
			 date.setText(s);
		}else if(mMonth<10)
		{
			StringBuilder sb = new StringBuilder()
			.append(mYear).append("-").append("0")
	        .append(mMonth + 1).append("-")
	        .append(mDay).append("");
			
			 String s = sb.toString();
			 date.setText(s);
		}
		else
		{
			StringBuilder sb = new StringBuilder()
			.append(mYear).append("-")
	        .append(mMonth + 1).append("-")
	        .append(mDay).append("");
			
			 String s = sb.toString();
			 date.setText(s);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		if(radio_date.isChecked())
		{
			date.setVisibility(View.VISIBLE);
			event_title.setVisibility(View.GONE);
		}
		else if(radio_title.isChecked())
		{
	        date.setVisibility(View.GONE);
	        event_title.setVisibility(View.VISIBLE);
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
}
