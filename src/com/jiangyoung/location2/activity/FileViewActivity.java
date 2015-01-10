package com.jiangyoung.location2.activity;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.jiangyoung.location2.R;
import com.jiangyoung.location2.resource.ContentList;
import com.jiangyoung.location2.resource.Dir;
import com.jiangyoung.location2.resource.File;
import com.jiangyoung.location2.resource.StatusID;
import com.jiangyoung.location2.utils.PullListXML;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * 1�����մ���ҳ�洫������token
 * 2��ͨ��token�����б���Ϣ
 * 
 * 3���յ����ص��б���Ϣ ����������ʽ��ʾ����
 * 
 */

public class FileViewActivity extends Activity {
	private TextView textview;
	private ListView listview;
	private Button btn_exit;
	private SimpleAdapter simpleAdapter;
	
	//����������
	private List<Map<String,Object>> list;
	private Map<String,Object> map;
	
	/**
	 * ProgressDialog
	 */
	private ProgressDialog mDialog;
	/**
	 * ��֤��
	 */
	private String token = "";
	/**
	 * ��ǰҪ�����Ŀ¼��
	 */
	private String requestDir = "";
	/**
	 * ��¼֮ǰ�����Ŀ¼��
	 */
	private String prevDir = "";
	/**
	 * ��ȡ�б�ĵ�ַ����
	 */
	private final String requestListPath = "http://1.jiangyounglocation.sinaapp.com/location.php?case=5";
	/**
	 * ��ȫ�˳�����
	 */
	private final String quitPath = "http://1.jiangyounglocation.sinaapp.com/location.php?case=6";
	
	/**
	 * ���ڽ����߳�ִ�з��ص���Ϣ��������Ӧ����
	 */
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case StatusID.GET_LIST_SUCC:
				mDialog.cancel();
				
				//��ȡ����Ȩ�޺� ��ʼ��ʱ ���Ʋ���ʱ��
				new Thread(){
					public void run() {
						try {
							Thread.sleep(StatusID.PERIOD_VALID);
							Message msg = Message.obtain();
							msg.what = StatusID.NEED_REVALID;
							handler.sendMessage(msg);
							
						} catch (InterruptedException e) {							
							e.printStackTrace();
						}
					};
				}.start();
				
				String res = msg.obj.toString();
				//���ַ���ת��Ϊ ������
				InputStream is = new ByteArrayInputStream(res.getBytes());
				
				ContentList contentList = new ContentList();
				
				try {
					contentList = PullListXML.getContentList(is);
					
					textview.setText("Ŀ¼"+contentList.getDirNum()+"�����ļ�"+contentList.getFileNum()+"��.");
					
					list = new ArrayList<Map<String,Object>>();	
					if(!requestDir.isEmpty()){
						map = new HashMap<String,Object>();						
						map.put("name", "..");
						map.put("info", "�ϼ�Ŀ¼");
						map.put("cate", "dir");
						list.add(map);
					}
					
					if(contentList.getDirNum()>0){
						
						List<Dir> dirs = new ArrayList<Dir>();
						dirs = contentList.getDirs();
						Dir dir = new Dir();
						for (int i = 0; i < contentList.getDirNum(); i++) {
							dir = dirs.get(i);
							map = new HashMap<String,Object>();
							map.put("name", dir.getName());
							map.put("info", "Ŀ¼ | "+dir.getFullName());
							map.put("cate", "dir");
							list.add(map);
						}
					}
					if(contentList.getFileNum()>0){
						List<File> files = new ArrayList<File>();
						files = contentList.getFiles();
						File file = new File();
						for (int i = 0; i < contentList.getFileNum(); i++) {
							file = files.get(i);
							map = new HashMap<String,Object>();
							map.put("name", file.getName());
							map.put("info", "�ļ�  |  "+(file.getLength()/1024/1024)+" M | "+file.getUploadTime());
							map.put("cate", "file");
							list.add(map);
						}
					}
					//����������
					simpleAdapter = new SimpleAdapter(FileViewActivity.this,list,
							android.R.layout.simple_list_item_2,
							new String[]{"name","info"},
							new int[]{android.R.id.text1,
							android.R.id.text2});					
					//�ѽ�����ɵ�xmlͨ�� ������ ����listview��
					listview.setAdapter(simpleAdapter);		
					//��listview��ӵ���¼�����
					OnItemClickListener(listview);
					
				} catch (Exception e) {
					myToast(getString(R.string.pull_xml_failed));
				}
			
				break;
			case StatusID.GET_LIST_FAILED:
				mDialog.cancel();
				myToast(getString(R.string.get_list_failed));
				break;
			case StatusID.ABANDON_TOKEN_SUCC:
				mDialog.cancel();
				FileViewActivity.this.finish();
				break;
			case StatusID.ABANDON_TOKEN_FAILED:
				mDialog.cancel();
				myToast( "����֮��Ĵ���");
				break;
			case StatusID.NEED_REVALID:
				FileViewActivity.this.finish();
				break;
			default:
				mDialog.cancel();
				myToast( "����֮��Ĵ���");
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_view);
		
		textview = (TextView)findViewById(R.id.textview);
		listview = (ListView)findViewById(R.id.listview);
		btn_exit = (Button)findViewById(R.id.safetyexit);
		
		btn_exit.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				abandonToken();
			}			
		});
		
		Intent intent_getToken = getIntent();
		token = intent_getToken.getStringExtra("token");
		
		requestListContent();
		
		
		
	}

	private void abandonToken() {
		mDialog = new ProgressDialog(FileViewActivity.this);
		mDialog.setTitle("��ȡ�б���Ϣ");
		mDialog.setMessage("���Ͼͺ�...");
		mDialog.show();
		new Thread(){			
			public void run(){
				try {
					String result = "";
			        //1��ȡ��һ�������
			        HttpClient client = new DefaultHttpClient();

			        //2.׼��Ҫ�������������
			        HttpPost httpPost = new HttpPost(quitPath);

		            //��ֵ��  NameValuePair
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            
		            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
		            //3.����POST��������ʵ��
		            httpPost.setEntity(entity);
		            //4.�������ݸ�������
		            HttpResponse resp = client.execute(httpPost);
		            int code = resp.getStatusLine().getStatusCode();
		            if(code==200){
		                result = EntityUtils.toString(resp.getEntity(),"utf-8");
		                Message msg = Message.obtain();
				        msg.obj = result;
						msg.what = StatusID.ABANDON_TOKEN_SUCC;
						handler.sendMessage(msg);
		            }else{
		            	Message msg = Message.obtain();
						msg.what = StatusID.ABANDON_TOKEN_FAILED;
						handler.sendMessage(msg);
		            }
				}catch (IOException e) {
					e.printStackTrace();
					Message msg = Message.obtain();
					msg.what = StatusID.GET_LIST_FAILED;
					handler.sendMessage(msg);
				}
			}
			
		}.start();			
	}

	private void requestListContent() {
		mDialog = new ProgressDialog(FileViewActivity.this);
		mDialog.setTitle("��ȡ�б���Ϣ");
		mDialog.setMessage("���Ͼͺ�...");
		mDialog.show();
		new Thread(){			
			public void run(){
				try {
					String result = "";
			        //1��ȡ��һ�������
			        HttpClient client = new DefaultHttpClient();

			        //2.׼��Ҫ�������������
			        HttpPost httpPost = new HttpPost(requestListPath);

		            //��ֵ��  NameValuePair
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            
		            params.add(new BasicNameValuePair("token",token));
		            params.add(new BasicNameValuePair("dir",requestDir));
		            
		            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
		            //3.����POST��������ʵ��
		            httpPost.setEntity(entity);
		            //4.�������ݸ�������
		            HttpResponse resp = client.execute(httpPost);
		            int code = resp.getStatusLine().getStatusCode();
		            if(code==200){
		                result = EntityUtils.toString(resp.getEntity(),"utf-8");
		                Message msg = Message.obtain();
				        msg.obj = result;
						msg.what = StatusID.GET_LIST_SUCC;
						handler.sendMessage(msg);
		            }else{
		            	Message msg = Message.obtain();
						msg.what = StatusID.GET_LIST_FAILED;
						handler.sendMessage(msg);
		            }
				}catch (IOException e) {
					e.printStackTrace();
					Message msg = Message.obtain();
					msg.what = StatusID.GET_LIST_FAILED;
					handler.sendMessage(msg);
				}
			}
			
		}.start();	
	}


	private void OnItemClickListener(ListView lv) {
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				//ȡ�ö�Ӧmap�е�ֵ �� {cate=file,info=xxx,name=pic}
				String itemInfoString = arg0.getAdapter().getItem(arg2).toString();
				//ͨ�� , ��ȡ������
				//arr[0] ="{cate=file"   arr[1] ="info=�ļ� | xxx"     arr[2] ="name=pic}"
				String[] itemInfoArr = itemInfoString.split(",");
				
				String cate = itemInfoArr[0].substring(itemInfoArr[0].indexOf("=")+1);
				
				if(cate.equals("dir")){
					
					//���ַ�����ȡ�� dirName  
					String dirName = itemInfoArr[2].substring(itemInfoArr[2].indexOf("=")+1,itemInfoArr[2].indexOf("}"));
					
					//  .. ��Ϊ�����ϲ�Ŀ¼     ��Ŀ¼ͬ����
					if(dirName.equals("..")){
						try{
							requestDir = prevDir.substring(0, prevDir.indexOf("/"));
						}catch(Exception e){
							requestDir = "";
						}
						
					}else{
						// eg: itemInfoArr[1] = "info=Ŀ¼ | dirName1/dirName2/";
						requestDir = itemInfoArr[1].substring(itemInfoArr[1].indexOf("|")+2);
						
						requestDir = requestDir.substring(0,requestDir.length()-1);
					}
					
					//myToast(requestDir);
					prevDir = requestDir;
					requestListContent();
				}else if(cate.equals("file")){
					//Intent toDownFile = new Intent();
					myToast("cooming soon.");
				}else{
					myToast("δʶ��");
				}
				
			}
		});
		
	}
	private void myToast(String msg){
		Toast.makeText(FileViewActivity.this, msg,Toast.LENGTH_SHORT).show();
	}
	private void myToast(String msg,int duration){
		Toast.makeText(FileViewActivity.this, msg,duration).show();
	}
	
}
