package com.mini.findmeapp.JoinGroup;

import com.mini.findmeapp.R;
import com.mini.findmeapp.AzureConnection.Groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PublicGroupsAdapter extends ArrayAdapter<Groups> {
	
	private final Context mContext;
	private final Groups[] mValues;

	public PublicGroupsAdapter(Context context, Groups[] groups) {
		super(context, R.layout.public_group_template, groups);
		
		mContext = context;
		mValues = groups;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View row = inflater.inflate(R.layout.public_group_template, parent, false);
		Groups group = mValues[position];
		
		TextView firstLine = (TextView)row.findViewById(R.id.firstLine);
		TextView secondLine = (TextView)row.findViewById(R.id.secondLine);
		
		firstLine.setText(group.name);
		secondLine.setText(group.description);
		
		return row;
	}

	

}
