package net.fabiocigliano.sms.spam_filter.model;

import java.util.HashMap;
import java.util.Vector;

import net.fabiocigliano.sms.spam_filter.R;
import net.fabiocigliano.testsync.interfaces.ListableBase;
import android.content.Context;
import android.text.format.DateUtils;
import android.widget.SimpleAdapter;

public class SmsMessage extends ListableBase {
	
	private final static String TBLNAME = "SmsMessage";
	
	/*
	 * 09-22 22:29:46.031: INFO/System.out(10854): resolve:
	 * _id=296
	 * thread_id=42
	 * address=fabiociglia
	 * person=null
	 * date=1316722612534
	 * protocol=0
	 * read=1
	 * status=-1
	 * type=1
	 * reply_path_present=0
	 * subject=null
	 * body=ciao
	 * service_center=+46764650901
	 * locked=0
	 * error_code=0
	 * seen=1 
	 */
	
	public SmsMessage(Context ctx) {
		super(ctx, TBLNAME);
	}

	@Override protected void init() {
		addField("_id", 0, true);
		addField("thread_id", 0);
		addField("address", "");
		addField("person", "");
		addField("date", 0L);
		addField("protocol", 0);
		addField("read", 0);
		addField("status", -1);
		addField("type", 1);
		addField("reply_path_present", 0);
		addField("subject", "");
		addField("body", "");
		addField("service_center", "");
		addField("locked", 0);
		addField("error_code", 0);
		addField("seen", 0);
	}
	
	public void init2() {
		
	}
	
	public static SmsMessage getExample(Context ctx) {
		SmsMessage toRet = new SmsMessage(ctx);
		toRet.addField("_id", 296+(int)(Math.random()*100), true);
		toRet.addField("thread_id", 42);
		toRet.addField("address", "fabiociglia");
		toRet.addField("person", "");
		toRet.addField("date", 1316722612534L);
		toRet.addField("protocol", 0);
		toRet.addField("read", 1);
		toRet.addField("status", -1);
		toRet.addField("type", 1);
		toRet.addField("reply_path_present", 0);
		toRet.addField("subject", "");
		toRet.addField("body", "ciao "+Math.random());
		toRet.addField("service_center", "+46764650901");
		toRet.addField("locked", 0);
		toRet.addField("error_code", 0);
		toRet.addField("seen", 1);
		return toRet;
	}
	
	@Override public HashMap<String, String> getMap() {
		HashMap<String, String> toRet = new HashMap<String, String>();
		toRet.put("ROW1", getFieldString("address"));
		toRet.put("ROW2", getFieldString("body"));
		toRet.put("ROW3", 
				DateUtils.formatDateTime(getContext(), getFieldLong("date"), DateUtils.FORMAT_ABBREV_ALL)
			);
		return toRet;
	}
	
	public SimpleAdapter getListAdapter(Vector<ListableBase> items) {
		Vector<HashMap<String, String>> data = getListData(items);
		SimpleAdapter adapter = new SimpleAdapter(
				getContext(),
				data,
				R.layout.message_list_item,
				new String[]{"ROW1","ROW2","ROW3"},
				new int[]{android.R.id.title, android.R.id.text1, android.R.id.text2}
		);
		return adapter;
	}
}
