/**
 * 
 */
package net.fabiocigliano.sms.spam_filter.activity;

import java.util.Vector;

import net.fabiocigliano.sms.spam_filter.lib.SSFlib;
import net.fabiocigliano.sms.spam_filter.model.SmsMessage;
import net.fabiocigliano.testsync.inputsource.InputSourceSQLite;
import net.fabiocigliano.testsync.interfaces.ListableBase;
import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author fabio
 *
 */
public class MessageList extends ListActivity {
	
	public static final String TAG = "message_list";
	public static final String NAME = "SMS List";
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SSFlib.checkDB(this);
        
        SmsMessage m = SmsMessage.getExample(this);
        
        InputSourceSQLite db = new InputSourceSQLite(this, SSFlib.DBNAME);
        db.store(m);
        
        Vector<ListableBase> list = db.list(m); //, "GROUP BY thread_id ORDER BY date DESC", true);
        setListAdapter(m.getListAdapter(list));
        
        db.close();
	}
	
}
