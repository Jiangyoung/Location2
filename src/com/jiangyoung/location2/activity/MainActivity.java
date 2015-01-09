package com.jiangyoung.location2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.jiangyoung.location2.R;
import com.jiangyoung.location2.resource.*;
import com.jiangyoung.location2.utils.Locinfo;




public class MainActivity extends Activity {
	
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private String receivePath = "http://1.jiangyounglocation.sinaapp.com/location.php?case=1";
	private Locinfo locInfo;
	
	private ProgressDialog mDialog;
	
	private Button startCheck;

	private TextView textView1;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			
			case StatusID.GET_LOCINFO_FAILED:
				mDialog.cancel();
				Toast.makeText(MainActivity.this, getString(R.string.send_loc_success),Toast.LENGTH_SHORT).show();
				break;
			case StatusID.GET_LOCINFO_SUCC:
				
				mDialog.cancel();
				//停止定位
				mLocationClient.stop();
				
				mDialog.setTitle("获取权限信息");
				mDialog.setMessage("马上就好...");
				mDialog.show();
				
				textView1.setText(msg.obj.toString());
				sendLocInfo(receivePath);
				break;
			case StatusID.REQUEST_POWINFO_SUCC:
				mDialog.cancel();
				String reqResult = msg.obj.toString();
				try {
					JSONObject jsonObject = new JSONObject(reqResult);
					int isValid = jsonObject.getInt("isvalid");
					
					if(StatusID.POWER_ISVALID == isValid){
						Toast.makeText(MainActivity.this, getString(R.string.power_isvalid), Toast.LENGTH_SHORT).show();
						//取得token
						String token = jsonObject.getString("token");
						//跳转到FileList页面
						Intent toFileList = new Intent();
						toFileList.putExtra("token",token);
						toFileList.setClass(MainActivity.this, FileViewActivity.class);
						startActivity(toFileList);
						
					}else{
						Toast.makeText(MainActivity.this, getString(R.string.power_invalid), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					
					//Toast.makeText(MainActivity.this, getString(R.string.parse_json_failed), Toast.LENGTH_SHORT).show();
				}
				
				break;
			case StatusID.REQUEST_POWINFO_FAILED:
				mDialog.cancel();
				Toast.makeText(MainActivity.this, getString(R.string.send_loc_failed), Toast.LENGTH_SHORT).show();
				break;
			default:
				mDialog.cancel();
				Toast.makeText(MainActivity.this, "意料之外的错误", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
		
	    startCheck = (Button)findViewById(R.id.checkpower);

		//实例化坐标数据
	    locInfo = new Locinfo();
	    
	    textView1 = (TextView)findViewById(R.id.textView1);
	        
	    startCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "OnClick", Toast.LENGTH_SHORT).show();
				
				InitLocation();
				
				mDialog = new ProgressDialog(MainActivity.this);
				mDialog.setTitle("获取位置信息");
				mDialog.setMessage("马上就好...");
				mDialog.show();
				
				mLocationClient.start();				
								
				/*
				int res = mLocationClient.requestLocation();
				switch(res){
			    case 0:
			    	Toast.makeText(MainActivity.this, "0：正常发起了定位。", Toast.LENGTH_SHORT).show();
			    	break;
			    case 1:
			    	Toast.makeText(MainActivity.this, "1：服务没有启动。", Toast.LENGTH_SHORT).show();
			    	break;
			    case 2:
			    	Toast.makeText(MainActivity.this, "2：没有监听函数。", Toast.LENGTH_SHORT).show();
			    	break;
			    case 6:
			    	Toast.makeText(MainActivity.this, "6：请求间隔过短。 前后两次请求定位时间间隔不能小于1000ms。", Toast.LENGTH_SHORT).show();
			    	break;
			    default:
			    	Toast.makeText(MainActivity.this, "default", Toast.LENGTH_SHORT).show();
			    	
			    }
			    */
			}
		});
	    
	    
	    
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			locInfo.setTime(location.getTime());
			
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			locInfo.setErrorcode(location.getLocType());
			
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			locInfo.setLatitude(location.getLatitude());
			
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			locInfo.setLontitude(location.getLongitude());
			
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			locInfo.setRadius(location.getRadius());
			
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				locInfo.setSpeed(location.getSpeed());
				
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				locInfo.setSatellite(location.getSatelliteNumber());
				
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				locInfo.setAddr(location.getAddrStr());
			} 
			Message msg = Message.obtain();
			msg.what = StatusID.GET_LOCINFO_SUCC;
			msg.obj = sb;
			handler.sendMessage(msg);
		}
	}
	
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度
		int span=5000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		
		mLocationClient.setLocOption(option);
	}
	/**
	 * 向服务器发送位置信息
	 */
	private void sendLocInfo(final String path) {
		
		new Thread(){			
			public void run(){
				try {
					String result = "";
			        //1获取到一个浏览器
			        HttpClient client = new DefaultHttpClient();

			        //2.准备要请求的数据类型
			        HttpPost httpPost = new HttpPost(path);

		            //键值对  NameValuePair
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            
		            
		            params.add(new BasicNameValuePair("time",locInfo.getTime()));
		            params.add(new BasicNameValuePair("errorcode",""+locInfo.getErrorcode()));
		            params.add(new BasicNameValuePair("latitude",""+locInfo.getLatitude()));
		            params.add(new BasicNameValuePair("lontitude",""+locInfo.getLontitude()));
		            params.add(new BasicNameValuePair("radius",""+locInfo.getRadius()));
		            params.add(new BasicNameValuePair("speed",""+locInfo.getSpeed()));
		            params.add(new BasicNameValuePair("satellite",""+locInfo.getSatellite()));
		            params.add(new BasicNameValuePair("addr", locInfo.getAddr()));
		            
		            
		            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
		            //3.设置POST请求数据实体
		            httpPost.setEntity(entity);
		            //4.发送数据给服务器
		            HttpResponse resp = client.execute(httpPost);
		            int code = resp.getStatusLine().getStatusCode();
		            if(code==200){
		                result = EntityUtils.toString(resp.getEntity(),"utf-8");
		                Message msg = Message.obtain();
				        msg.obj = result;
						msg.what = StatusID.REQUEST_POWINFO_SUCC;
						handler.sendMessage(msg);
		            }else{
		            	Message msg = Message.obtain();
						msg.what = StatusID.REQUEST_POWINFO_FAILED;
						handler.sendMessage(msg);
		            }
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg = Message.obtain();
					msg.what = StatusID.REQUEST_POWINFO_FAILED;
					handler.sendMessage(msg);
				}
			}
			
		}.start();	
		
	};
		
}
