package com.example.selectsourcedemo;

import com.resoure.selectUtil.Exdialog;
import com.resoure.selectUtil.SelectLocalFileDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText uriEdit;
	private Button selectbtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		获取USB路径
//		Environment.getExternalUsbStorageDirectory().getPath();
		
		uriEdit = (EditText) findViewById(R.id.uri_edit);
		selectbtn = (Button) findViewById(R.id.select_btn);
		
		
		selectbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//第一种
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, Exdialog.class);
//				startActivityForResult(intent, 100);
				//第二种
				final SelectLocalFileDialog dlg= new SelectLocalFileDialog(MainActivity.this);
				dlg.show();
				//监听当Dialog消失后要触发的时间
				dlg.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						String path = dlg.getFilePath();
						uriEdit.setText(path);
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/*if (requestCode == 100) {
			String uri = data.getStringExtra("FileUri");
			uriEdit.setText(uri);
		}*/
		 String path = "";
         if (resultCode == RESULT_OK) {
                   if (requestCode == 100) {
                      path = data.getStringExtra("FilePath");
//                      String uriStr = "file://" + path;
                      uriEdit.setText(path);
                   }
         }
	}
	
}
