/**
 * 
 */
package net.fabiocigliano.sms.spam_filter.lib;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import net.fabiocigliano.sms.spam_filter.R;
import net.fabiocigliano.sms.spam_filter.model.SmsMessage;
import net.fabiocigliano.sms.spam_filter.model.SpamSender;
import net.fabiocigliano.testsync.inputsource.InputSourceSQLite;
import net.fabiocigliano.testsync.interfaces.ListableBase;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.widget.SimpleAdapter;

/**
 * @author fabio
 *
 */
public final class SSFlib {
	
	/** TAG used for Debug-Logging */
    public static final String TAG = "SMSSpamFilterReceiver";

    /** Debug-Logging */
    public static final Boolean DEBUG = true;
	
    /** Database name */
    public static final String DBNAME = "SMSSpamFilter";
    
	public static void checkDB(Context ctx) {
		// apro la connessione al db
		InputSourceSQLite db = new InputSourceSQLite(ctx, DBNAME);
		// aggiungo il tipo di dato SmsMessage
		db.list(new SmsMessage(ctx));
		// chiudo la connessione al db
		db.close();
    }
	
	public static void checkSmsList(Context ctx) {
		Cursor c = ctx.getContentResolver().query(Uri.parse("content://sms/inbox"), null,
        		"address=? and data/100=?",
        		new String[] {
        			"fabiociglia"
//        			,"1316723015000"
        		}, null);
        System.out.println("c.count: "+c.getCount());
        while(c.moveToNext()) {
	        
        	StringBuilder b = new StringBuilder();
        	for(String s : c.getColumnNames()) {
        		b.append(s+"="+c.getString(c.getColumnIndex(s))+", ");
        	}
	        System.out.println(
	        		"resolve: "+b.toString()
	        	);
        }
	}
	
	public static SimpleAdapter getSenderList(final Context ctx) {
		final Vector<HashMap<String, String>> toRet = new Vector<HashMap<String, String>>();
		HashMap<String, String> tmp;
		
		Cursor c = ctx.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"address","person"}, null, null, null);
		Vector<String> tmp2 = new Vector<String>();
        while(c.moveToNext()) {
        	String address = c.getString(c.getColumnIndex("address"));
	        String person = c.getString(c.getColumnIndex("person"));
	        
	        if( !tmp2.contains(address) ) {
		        tmp = SpamSender.getViewMap(ctx, address,person);
		        toRet.add(tmp);
		        tmp2.add(address);
	        }
        }
        
        Collections.sort(toRet,new Comparator<HashMap<String, String>>() {
			@Override public int compare(HashMap<String, String> object1, HashMap<String, String> object2) {
				return object1.get("ROW1").compareToIgnoreCase( object2.get("ROW1"));
			}
		});
        
        SimpleAdapter adapter = new SimpleAdapter(
        		ctx,
        		toRet,
        		R.layout.sender_list_item,
        		new String[]{"ROW1","ROW2","address"
//        			,"isSpamSender"
        			},
        		new int[]{android.R.id.text1, android.R.id.icon, android.R.id.text2
//        			, android.R.id.checkbox
        			}
        );
        adapter.setViewBinder(SpamSender.getViewBinder(ctx));
        return adapter;
	}
	
	@Deprecated
	public static Boolean isSpamSender(Context ctx, android.telephony.SmsMessage msg) {
		Boolean toRet = false;
		toRet = toRet || isSpamSender(ctx, msg.getOriginatingAddress());
		System.out.println("questo è il person? "+msg.getEmailFrom());
		return toRet;
	}
	
	public static Boolean isSpamSender(Context ctx, String address) {
		InputSourceSQLite db = new InputSourceSQLite(ctx, SSFlib.DBNAME);
		SpamSender ss = new SpamSender(ctx);
		ss = (SpamSender) db.getInstance(ss, address);
		db.close();
		return ss != null;
	}
	
	public static Boolean isSpamSender(Context ctx, Uri person) {
		InputSourceSQLite db = new InputSourceSQLite(ctx, SSFlib.DBNAME);
		SpamSender ss = new SpamSender(ctx);
		Vector<ListableBase> list = db.list(ss,"WHERE `person` = '"+person.toString().replace(People.CONTENT_URI.toString(), "")+"'", true);
		db.close();
		return list!=null && list.size()>0;
	}
	
	@Deprecated
	public static void addSpamSender(Context ctx, android.telephony.SmsMessage msg) {
		if( !isSpamSender(ctx, msg) ) {
			InputSourceSQLite db = new InputSourceSQLite(ctx, SSFlib.DBNAME);
			SpamSender ss = new SpamSender(ctx, msg.getOriginatingAddress());
			db.store(ss);
			db.close();
		}
	}
	
	public static void addSpamSender(Context ctx, String address) {
		if( !isSpamSender(ctx, address) ) {
			InputSourceSQLite db = new InputSourceSQLite(ctx, SSFlib.DBNAME);
			SpamSender ss = new SpamSender(ctx, address);
			db.store(ss);
			db.close();
		}
	}
}
