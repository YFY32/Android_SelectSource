package com.resoure.selectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.selectsourcedemo.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectLocalFileDialog extends Dialog {

	private Context context;
	private ListView dlg_list = null;
	private String str;
	private int position;
	private String filePath = "";
	
	private MyAdapter adapter;
	
	private List<Map<String, Object>> mData;
    private String mDir = "/sdcard";

	public SelectLocalFileDialog(Activity context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public SelectLocalFileDialog(Activity context, int theme, String str) {
		super(context, theme);
		this.context = context;
		this.str = str;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ���öԻ���ʹ�õĲ����ļ�
		this.setContentView(R.layout.selectdialog_layout);
		String title = "ѡ���ļ�";
		setTitle(title);
		dlg_list = (ListView) findViewById(R.id.dialog_Listview);
		adapter = new MyAdapter(context);
		mData = getData();  // ������mData���Ŀ¼������
		dlg_list.setAdapter(adapter);
		
		WindowManager m = ((Activity)(context)).getWindowManager();
        Display d = m.getDefaultDisplay();
        LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.8);
        p.width = (int) (d.getWidth() * 0.95);
        getWindow().setAttributes(p);         // ���öԻ���Ϊ�̶���С���������Ŀ¼�仯

		// ΪGridView���ü�����
		dlg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if ((Integer) mData.get(arg2).get("img") == R.drawable.aborted_icon) {
                    mDir = (String) mData.get(arg2).get("info"); 
                    mData = getData();     //���Ŀ¼ʱ������Ŀ¼
                    adapter = new MyAdapter(context);
                    dlg_list.setAdapter(adapter);
	           } else {       // ����ļ�ʱ�ر��ļ�������������ѡȡ�������
	                    filePath = (String) mData.get(arg2).get("info");
	                  //���item��Dialog��ʧ
	    				SelectLocalFileDialog.this.dismiss();
	           }
			}
		});
	}
	//���ص����λ��
	public int getPosition() {
		return position;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void doInOnback() {
		if (mData.get(0).get("title").equals("/������һ��")) {
            mDir = (String) mData.get(0).get("info"); 
            mData = getData();     //���Ŀ¼ʱ������Ŀ¼
            adapter = new MyAdapter(context);
            dlg_list.setAdapter(adapter);
       } else {       
            //���item��Dialog��ʧ
			SelectLocalFileDialog.this.dismiss();
       }
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		doInOnback();
	}

}