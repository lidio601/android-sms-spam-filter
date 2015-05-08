/**
 * 
 */
package net.fabiocigliano.sms.spam_filter.activity;

import java.util.HashMap;
import java.util.Vector;

import net.fabiocigliano.sms.spam_filter.R;
import net.fabiocigliano.sms.spam_filter.lib.SSFlib;
import net.fabiocigliano.sms.spam_filter.model.SpamSender;
import net.fabiocigliano.testsync.inputsource.InputSourceSQLite;
import net.fabiocigliano.testsync.interfaces.ListableBase;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author fabio
 *
 */
public class SenderList extends ListActivity {
	
	public static final String TAG = "sender_list";
	public static final String NAME = "Sender Black-List";
	
	private int cur_menu = R.string.menu_blacklist;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SSFlib.checkDB(this);
        
        cur_menu = R.string.menu_blacklist;
        
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	@Override protected void onResume() {
		super.onResume();
		aggiornaLista();
	}
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.menu_blacklist);
		menu.add(R.string.menu_mittenti);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getTitle() == getString(R.string.menu_blacklist) ) {
	        cur_menu = R.string.menu_blacklist;
	        aggiornaLista();
			return true;
		} else if( item.getTitle() == getString(R.string.menu_mittenti) ) {
	        cur_menu = R.string.menu_mittenti;
	        aggiornaLista();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == KeyEvent.KEYCODE_BACK && cur_menu != R.string.menu_blacklist ) {
	        cur_menu = R.string.menu_blacklist;
	        aggiornaLista();
	        return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void aggiornaLista() {
		if( cur_menu == R.string.menu_blacklist ) {
			SpamSender m = new SpamSender(this);
	        InputSourceSQLite db = new InputSourceSQLite(this, SSFlib.DBNAME);
	        Vector<ListableBase> list = db.list(m);
	        setListAdapter(m.getListAdapter(list));
	        db.close();
	        for(int i=0;i<list.size();i++) {
	        	getListView().setItemChecked(i, true);
	        }
		} else if( cur_menu == R.string.menu_mittenti ) {
			SimpleAdapter adapter = SSFlib.getSenderList(this);
	        setListAdapter(adapter);
	        for(int i=0;i<adapter.getCount();i++) {
	        	getListView().setItemChecked(i, SSFlib.isSpamSender(this, ((HashMap<String,String>)(adapter.getItem(i))).get("address") ));
	        }
		}
	}
}
