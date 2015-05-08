package net.fabiocigliano.sms.spam_filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.fabiocigliano.sms.spam_filter.activity.MessageList;
import net.fabiocigliano.sms.spam_filter.activity.SenderList;
import net.fabiocigliano.sms.spam_filter.lib.SSFlib;
import net.fabiocigliano.utils.Utils;
import android.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SMSSpamFilterActivity extends TabActivity {
	
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TabHost host = getTabHost();
        TabSpec spec;
        
        spec = host.newTabSpec(MessageList.TAG);
        Intent mli = new Intent(SMSSpamFilterActivity.this, MessageList.class);
        spec.setContent(mli);
        spec.setIndicator(MessageList.NAME, getResources().getDrawable(R.drawable.ic_menu_info_details));
        host.addTab(spec);
        
        spec = host.newTabSpec(SenderList.TAG);
        Intent sli = new Intent(SMSSpamFilterActivity.this, SenderList.class);
        spec.setContent(sli);
        spec.setIndicator(SenderList.NAME, getResources().getDrawable(R.drawable.ic_menu_info_details));
        host.addTab(spec);
        
        setDefaultTab(SenderList.TAG);
        host.setCurrentTabByTag(SenderList.TAG);
        
    	try {
	        String myPath = "/data/data/"+getPackageName()+"/databases/"+SSFlib.DBNAME;
	        FileInputStream in = new FileInputStream(new File(myPath));
	        FileOutputStream out = new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"db.sqllite"));
	        Utils.filesystem.copyFile(in, out);
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
}