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



public class DirectoryAdapter extends BaseAdapter
{
	private Activity activity;
    private LayoutInflater inflater;
    private int size;
	
	private ArrayList<String> member_name = new ArrayList<String>();
	private ArrayList<String> email = new ArrayList<String>();
	private ArrayList<String> contact_no = new ArrayList<String>();

	public DirectoryAdapter(Activity a,ArrayList<String> member_name2, ArrayList<String> email2,ArrayList<String> contact_no2) 
	{
		member_name = member_name2;
		email = email2;
		contact_no = contact_no2;
		activity = a;
	    inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    size = member_name.size();
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
		private TextView email_id;
		private TextView phone;
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
				v= inflater.inflate(R.layout.directory_listview2, null);
				
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			
	    // Creating Holder Class Object 
			holder=new DirectoryHolder();
			
			try
			{ 
				holder.name = (TextView) v.findViewById(R.id.member_name);
				holder.email_id = (TextView) v.findViewById(R.id.email);
				holder.phone = (TextView) v.findViewById(R.id.contact_no);
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
		
		holder.name.setText(member_name.get(position).toString());
		holder.email_id.setText(email.get(position).toString());
		holder.phone.setText(contact_no.get(position).toString());
		
	    return v;
	}

}
