package com.jiangyoung.location2.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import android.util.Base64;
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
import com.jiangyoung.location2.utils.Encryption;
import com.jiangyoung.location2.utils.MD5;



/*
 * 

 * 1、定位  获取位置信息
 * 2、将位置信息发送到服务器    
 *   位置信息数据+md5（位置信息数据+ANDROID_KEY）
 *   用于确实数据来源于知道ANDROID_KEY的地方
 *   
 *   服务器收到数据先认证 再 确认权限信息
 *   
 * 3、收到返回的 用于请求列表的 权限信息  
 *   若有权限操作则附带返回 用于获取列表的临时 token
 *   token 为服务器生成
 * 4、若有操作权限 跳转到显示列表的Activity 并将 token 传过去
 * 
 */


public class MainActivity extends Activity {
	
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	/**
	 * 上传位置信息地址链接
	 */
	private String receivePath = "http://1.jiangyounglocation.sinaapp.com/location.php?case=1";
	
	private Locinfo locInfo;
	
	private ProgressDialog mDialog;
	
	private Button startCheck;
	private TextView textView1;
	
	/**
	 * 接收从服务器返回的认证号
	 */
	private String tokenFromServer = "";
	/**
	 * 用于接收线程执行返回的信息并做出对应操作
	 */
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			
			case StatusID.GET_LOCINFO_FAILED:
				mDialog.cancel();
				myToast( getString(R.string.send_loc_success));
				break;
			case StatusID.GET_LOCINFO_SUCC:
				
				mDialog.cancel();
				//停止定位
				mLocationClient.stop();
				
				mDialog.setTitle("获取权限信息");
				mDialog.setMessage("马上就好...");
				mDialog.show();
				//把位置信息展示在屏幕上
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
						myToast( getString(R.string.power_isvalid));
						
						//取得token
						tokenFromServer = jsonObject.getString("token");

						//跳转到FileList页面
						Intent toFileList = new Intent();
						toFileList.putExtra("token",tokenFromServer);
						toFileList.setClass(MainActivity.this, FileViewActivity.class);
						startActivity(toFileList);

					}else{
						textView1.append("\n该位置无访问权限");;
						myToast( getString(R.string.power_invalid));
					}
				} catch (Exception e) {
					
					myToast( getString(R.string.parse_json_failed));
				}
				
				break;
			case StatusID.REQUEST_POWINFO_FAILED:
				mDialog.cancel();
				myToast( getString(R.string.send_loc_failed));
				break;
			default:
				mDialog.cancel();
				myToast( "意料之外的错误");
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
	    
	    
/*
 * 
 
	    //String cleartext = "abcdefghijklmnopqrstuvwxyz !@#$^^%&(**)_++_(*()*(&*^&^$$%%^^&/music/G.E.M. 邓紫棋 - A.I.N.Y.(live版).mp3<";
	    //String cleartext = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><contentList><dirNum>3</dirNum><fileNum>1</fileNum><dirs><dir><dirName>music</dirName><dirFullName>music/</dirFullName></dir><dir><dirName>pic</dirName><dirFullName>pic/</dirFullName></dir><dir><dirName>testDir1</dirName><dirFullName>testDir1/</dirFullName></dir></dirs><files><file><fileName>README.txt</fileName><fileFullName>README.txt</fileFullName><fileCDNUrl>http://jiangyounglocation-locdomain.stor.sinaapp.com/README.txt</fileCDNUrl><fileLength>14</fileLength><fileUploadTime>1420774306</fileUploadTime></file></files></contentList>";
	    String cleartext = "2";
	    Encryption encryption = new Encryption(StatusID.ANDROID_KEY,cleartext);
	    
	    String ciphertext = "";
		try {
			ciphertext = encryption.replaceEncrypt();
		} catch (Exception e) {
			textView1.setText(e.getMessage());
		}
	    
	    textView1.setText(ciphertext);
	    
	    encryption.setText(ciphertext);
	    
	    String result = "";
	    try{
	    	result = encryption.replaceDecrypt();
	    }catch(Exception e){
	    	result = e.getMessage();
	    }
	    
	    textView1.append("\n--------\n"+result);
*/
	    
	    startCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//myToast( "OnClick");
				
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
			    	myToast( "0：正常发起了定位。");
			    	break;
			    case 1:
			    	myToast( "1：服务没有启动。");
			    	break;
			    case 2:
			    	myToast( "2：没有监听函数。");
			    	break;
			    case 6:
			    	myToast( "6：请求间隔过短。 前后两次请求定位时间间隔不能小于1000ms。");
			    	break;
			    default:
			    	myToast( "default");
			    	
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
			
			sb.append("LocationInfo : \n");
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
		            
		            //md5 数据 + key 用于验证数据真实性
		            String verifyCode = MD5.md5(locInfo.getTime()
		            		+locInfo.getLatitude()
		            		+locInfo.getLontitude()
		            		+locInfo.getRadius()
		            		+StatusID.MY_KEY);
		            
		            params.add(new BasicNameValuePair("verifyCode",verifyCode));
		            
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
	
	private void myToast(String msg){
		Toast.makeText(MainActivity.this, msg,Toast.LENGTH_SHORT).show();
	}
	private void myToast(String msg,int duration){
		Toast.makeText(MainActivity.this, msg,duration).show();
	}
	
}
