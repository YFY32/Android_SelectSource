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

public class Exdialog extends ListActivity { // ListActivity自带List控件
    private List<Map<String, Object>> mData;
    private String mDir = "/sdcard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              /*Intent intent = this.getIntent();
              Bundle bl = intent.getExtras();
              String title = bl.getString("explorer_title");      // 接收标题内容
              Uri uri = intent.getData();    // 接收起始目录
              mDir = uri.getPath();  // 设置起始目录
*/              
             String title = "选择文件";      // 接收标题内容
             setTitle(title);
             
              mData = getData();  // 向链表mData填充目录的数据
              MyAdapter adapter = new MyAdapter(this);
              setListAdapter(adapter);                 // 设置MyAdapter类为ListView控件提供数据

              WindowManager m = getWindowManager();
              Display d = m.getDefaultDisplay();
              LayoutParams p = getWindow().getAttributes();
              p.height = (int) (d.getHeight() * 0.8);
              p.width = (int) (d.getWidth() * 0.95);
              getWindow().setAttributes(p);         // 设置对话框为固定大小，不因进出目录变化
    }

    private List<Map<String, Object>> getData() {       // 将目录数据填充到链表中
              List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
              Map<String, Object> map = null;
              File f = new File(mDir);                 // 打开当前目录
              File[] files = f.listFiles();       // 获取当前目录中文件列表

              if (!mDir.equals("/sdcard")) {        // 不充许进入/sdcard上层目录
                       map = new HashMap<String, Object>();        // 加返回上层目录项
                       map.put("title", "/返回上一级");
                       map.put("info", f.getParent());
                       map.put("img", R.drawable.aborted_icon);
                       list.add(map);
              }
              
              if (files != null) { // 将目录中文件填加到列表中
                       for (int i = 0; i < files.length; i++) {
                                map = new HashMap<String, Object>();
                                map.put("title", files[i].getName());
                                map.put("info", files[i].getPath());
                                if (files[i].isDirectory())         // 按不同类型显示不同图标
                                          map.put("img", R.drawable.aborted_icon);
                                else
                                          map.put("img", R.drawable.approval_icon);
                                list.add(map);
                       }
              }
              
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if (((String) o1.get("title")).equals("/返回上一级")) {
					return -1;
				}
				if (((String) o2.get("title")).equals("/返回上一级")) {
					return 1;
				}
				return (((String) o1.get("title")).toLowerCase())
						.compareTo(((String) o2.get("title")).toLowerCase());
			}
		});
              
              return list;
    }

    // 响应用户点击列表项的事件
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
              Log.d("MyListView4-click", (String) mData.get(position).get("info"));
              if ((Integer) mData.get(position).get("img") == R.drawable.aborted_icon) {
                       mDir = (String) mData.get(position).get("info"); 
                       mData = getData();     //点击目录时进入子目录
                       MyAdapter adapter = new MyAdapter(this);
                       setListAdapter(adapter);
              } else {       // 点击文件时关闭文件管理器，并将选取结果返回
                       finishWithResult((String) mData.get(position).get("info"));
              }
    }

    public final class ViewHolder {       // 定义每个列表项所含内容
              public ImageView img; // 显示图片ID
              public TextView title;     // 文件目录名
              public TextView info;     // 文件目录描述
    }

    public class MyAdapter extends BaseAdapter { // 实现列表内容适配器
              private LayoutInflater mInflater;

              public MyAdapter(Context context) {
                       this.mInflater = LayoutInflater.from(context);
              }

              public int getCount() {  // 获取列表项个数
                       return mData.size();
              }

              public Object getItem(int arg0) {
                       return null;
              }

              public long getItemId(int arg0) {
                       return 0;
              }

              // 设置每个列表项的显示
              public View getView(int position, View convertView, ViewGroup parent) {
                       ViewHolder holder = null;
                       if (convertView == null) {
                                holder = new ViewHolder();
                                convertView = mInflater.inflate(R.layout.listview_item, null); // 设置列表项的布局
                                holder.img = (ImageView) convertView.findViewById(R.id.img);
                                holder.title = (TextView) convertView.findViewById(R.id.title);
                                holder.info = (TextView) convertView.findViewById(R.id.info);
                                convertView.setTag(holder);
                       } else {
                                holder = (ViewHolder) convertView.getTag();
                       }
                       holder.img.setBackgroundResource((Integer) mData.get(position).get( "img"));     // 根据位置position设置具体内容
                       holder.title.setText((String) mData.get(position).get("title"));
                       holder.info.setText((String) mData.get(position).get("info"));
                       return convertView;
              }
    }

    private void finishWithResult(String path) {
              Bundle conData = new Bundle();
              conData.putString("results", "Thanks Thanks");
              Intent intent = new Intent();   // 以intent的方式将结果返回调用类
              intent.putExtras(conData);
              intent.putExtra("FilePath", path);
              Uri startDir = Uri.fromFile(new File(path));
              intent.setDataAndType(startDir, "vnd.android.cursor.dir/lysesoft.andexplorer.file");
              setResult(RESULT_OK, intent);
              finish();
    }
};
