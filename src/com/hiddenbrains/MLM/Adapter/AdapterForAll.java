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



public class AdapterForAll extends BaseAdapter
{
	private Activity activity;
    private LayoutInflater inflater;
    private int size;
	
	private ArrayList<String> data = new ArrayList<String>();
	
	public AdapterForAll(Activity a, ArrayList<String> name) 
	{
		data = name;
		activity = a;
	    inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    size = data.size();
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
	
	public class AdapterHolder
	{
		private TextView name;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
		
		AdapterHolder holder;
		
		if(v==null)
		 {
			
	    // SETTING THE SMALL XML FILE TO THIS VIEW 
			try
			{  
				v= inflater.inflate(R.layout.listview_common2, null);
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			
	    // Creating Holder Class Object 
			holder=new AdapterHolder();
			
			try
			{ 
				holder.name = (TextView) v.findViewById(R.id.data);
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
			 holder=(AdapterHolder)v.getTag();
		}
		
		holder.name.setText(data.get(position).toString());
		
	    return v;
	}

}
