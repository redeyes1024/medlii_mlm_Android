package com.hiddenbrains.MLM.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiddenbrains.MLM.screen.R;



public class CoursesAdapter extends BaseAdapter
{
	private Activity activity;
    private LayoutInflater inflater;
    private int size;
	
	private ArrayList<String> course_name = new ArrayList<String>();
	private ArrayList<String> course_date = new ArrayList<String>();

	public CoursesAdapter(Activity a, ArrayList<String> course_name1,ArrayList<String> course_date1) 
	{
		course_name = course_name1;
		course_date = course_date1;
		activity = a;
	    inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    size = course_name.size();
	}

	@Override
	public int getCount() 
	{
		return size;
	}

	@Override
	public Object getItem(int position) 
	{
		return position;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	
	public class DirectoryHolder
	{
		private TextView name;
		private TextView date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
		
		DirectoryHolder holder;
		
		if(v==null)
		 {
			
	    // SETTING THE SMALL XML FILE TO THIS VIEW 
			try
			{
				v= inflater.inflate(R.layout.courses_listview2, null);
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			
	    // Creating Holder Class Object 
			holder=new DirectoryHolder();
			
			try
			{ 
				holder.name = (TextView) v.findViewById(R.id.course_name);
				holder.date = (TextView) v.findViewById(R.id.course_date);
			}
			
			catch(Exception e)
			{
				e.getMessage();	
			}	
			
          // SETS Content  		 
			v.setTag(holder);
		}
		else
	//Assigning the default view or the view which u have setted before 
		{   
			 holder=(DirectoryHolder)v.getTag();
		}
		
		holder.name.setText(course_name.get(position).toString());
		holder.date.setText(course_date.get(position).toString());
		
	    return v;
	}

}
