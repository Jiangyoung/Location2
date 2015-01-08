package com.jiangyoung.location2.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiangyoung.location2.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FileViewActivity extends Activity {
	private TextView textview;
	private ListView listview;
	private SimpleAdapter simpleAdapter;
	
	private List<Map<String,Object>> list;
	private Map<String,Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_view);
		textview = (TextView)findViewById(R.id.textview);
		listview = (ListView)findViewById(R.id.listview);
		list = new ArrayList<Map<String,Object>>();
		
		map = new HashMap<String,Object>();
		map.put("name", "item1");
		map.put("fullname", "fullitem1");
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("name", "item2");
		map.put("fullname", "fullitem2");
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("name", "item3");
		map.put("fullname", "fullitem3");
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("name", "item4");
		map.put("fullname", "fullitem4");
		list.add(map);
		
		simpleAdapter = new SimpleAdapter(
				FileViewActivity.this,
				list,
				android.R.layout.simple_list_item_2,
				new String[]{"name","fullname"},
				new int[]{android.R.id.text1,
				android.R.id.text2});
		
		listview.setAdapter(simpleAdapter);
		
		OnItemClickListener(listview);
		
	}

	private void OnItemClickListener(ListView lv) {
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(FileViewActivity.this,""+arg2,Toast.LENGTH_SHORT).show();
				switch(arg2){
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				}
				
			}
		});
		
	}
}
