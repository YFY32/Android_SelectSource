package com.resoure.selectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.selectsourcedemo.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Exdialog extends ListActivity { // ListActivity�Դ�List�ؼ�
    private List<Map<String, Object>> mData;
    private String mDir = "/sdcard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              /*Intent intent = this.getIntent();
              Bundle bl = intent.getExtras();
              String title = bl.getString("explorer_title");      // ���ձ�������
              Uri uri = intent.getData();    // ������ʼĿ¼
              mDir = uri.getPath();  // ������ʼĿ¼
*/              
             String title = "ѡ���ļ�";      // ���ձ�������
             setTitle(title);
             
              mData = getData();  // ������mData���Ŀ¼������
              MyAdapter adapter = new MyAdapter(this);
              setListAdapter(adapter);                 // ����MyAdapter��ΪListView�ؼ��ṩ����

              WindowManager m = getWindowManager();
              Display d = m.getDefaultDisplay();
              LayoutParams p = getWindow().getAttributes();
              p.height = (int) (d.getHeight() * 0.8);
              p.width = (int) (d.getWidth() * 0.95);
              getWindow().setAttributes(p);         // ���öԻ���Ϊ�̶���С���������Ŀ¼�仯
    }

    private List<Map<String, Object>> getData() {       // ��Ŀ¼������䵽������
              List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
              Map<String, Object> map = null;
              File f = new File(mDir);                 // �򿪵�ǰĿ¼
              File[] files = f.listFiles();       // ��ȡ��ǰĿ¼���ļ��б�

              if (!mDir.equals("/sdcard")) {        // ���������/sdcard�ϲ�Ŀ¼
                       map = new HashMap<String, Object>();        // �ӷ����ϲ�Ŀ¼��
                       map.put("title", "/������һ��");
                       map.put("info", f.getParent());
                       map.put("img", R.drawable.aborted_icon);
                       list.add(map);
              }
              
              if (files != null) { // ��Ŀ¼���ļ���ӵ��б���
                       for (int i = 0; i < files.length; i++) {
                                map = new HashMap<String, Object>();
                                map.put("title", files[i].getName());
                                map.put("info", files[i].getPath());
                                if (files[i].isDirectory())         // ����ͬ������ʾ��ͬͼ��
                                          map.put("img", R.drawable.aborted_icon);
                                else
                                          map.put("img", R.drawable.approval_icon);
                                list.add(map);
                       }
              }
              
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if (((String) o1.get("title")).equals("/������һ��")) {
					return -1;
				}
				if (((String) o2.get("title")).equals("/������һ��")) {
					return 1;
				}
				return (((String) o1.get("title")).toLowerCase())
						.compareTo(((String) o2.get("title")).toLowerCase());
			}
		});
              
              return list;
    }

    // ��Ӧ�û�����б�����¼�
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
              Log.d("MyListView4-click", (String) mData.get(position).get("info"));
              if ((Integer) mData.get(position).get("img") == R.drawable.aborted_icon) {
                       mDir = (String) mData.get(position).get("info"); 
                       mData = getData();     //���Ŀ¼ʱ������Ŀ¼
                       MyAdapter adapter = new MyAdapter(this);
                       setListAdapter(adapter);
              } else {       // ����ļ�ʱ�ر��ļ�������������ѡȡ�������
                       finishWithResult((String) mData.get(position).get("info"));
              }
    }

    public final class ViewHolder {       // ����ÿ���б�����������
              public ImageView img; // ��ʾͼƬID
              public TextView title;     // �ļ�Ŀ¼��
              public TextView info;     // �ļ�Ŀ¼����
    }

    public class MyAdapter extends BaseAdapter { // ʵ���б�����������
              private LayoutInflater mInflater;

              public MyAdapter(Context context) {
                       this.mInflater = LayoutInflater.from(context);
              }

              public int getCount() {  // ��ȡ�б������
                       return mData.size();
              }

              public Object getItem(int arg0) {
                       return null;
              }

              public long getItemId(int arg0) {
                       return 0;
              }

              // ����ÿ���б������ʾ
              public View getView(int position, View convertView, ViewGroup parent) {
                       ViewHolder holder = null;
                       if (convertView == null) {
                                holder = new ViewHolder();
                                convertView = mInflater.inflate(R.layout.listview_item, null); // �����б���Ĳ���
                                holder.img = (ImageView) convertView.findViewById(R.id.img);
                                holder.title = (TextView) convertView.findViewById(R.id.title);
                                holder.info = (TextView) convertView.findViewById(R.id.info);
                                convertView.setTag(holder);
                       } else {
                                holder = (ViewHolder) convertView.getTag();
                       }
                       holder.img.setBackgroundResource((Integer) mData.get(position).get( "img"));     // ����λ��position���þ�������
                       holder.title.setText((String) mData.get(position).get("title"));
                       holder.info.setText((String) mData.get(position).get("info"));
                       return convertView;
              }
    }

    private void finishWithResult(String path) {
              Bundle conData = new Bundle();
              conData.putString("results", "Thanks Thanks");
              Intent intent = new Intent();   // ��intent�ķ�ʽ��������ص�����
              intent.putExtras(conData);
              intent.putExtra("FilePath", path);
              Uri startDir = Uri.fromFile(new File(path));
              intent.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");
              setResult(RESULT_OK, intent);
              finish();
    }
};
