package net.fabiocigliano.sms.spam_filter.model;


import java.util.HashMap;
import java.util.Vector;

import net.fabiocigliano.sms.spam_filter.R;
import net.fabiocigliano.sms.spam_filter.lib.SSFlib;
import net.fabiocigliano.testsync.inputsource.InputSourceSQLite;
import net.fabiocigliano.testsync.interfaces.ListableBase;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts.People;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class SpamSender extends ListableBase {
	
	private final static String TBLNAME = "SpamSender";
	
	/*
	 * _id=296
	 * address=fabiociglia
	 * person=null
	 */
	
	public SpamSender(Context ctx) {
		super(ctx, TBLNAME);
	}
	
	public SpamSender(Context ctx, String address) {
		super(ctx, TBLNAME);
		addField("address", address);
	}
	
	public SpamSender(Context ctx, String address, String person) {
		super(ctx, TBLNAME);
		addField("address", address);
		if( person != null && !person.equalsIgnoreCase("null") ) { 
			addField("person", person);
		}
	}

	@Override protected void init() {
		addField("address", "", true);
		addField("person", "");
	}
	
	public void init2() {
		
	}
	
	public static SpamSender getExample(Context ctx) {
		SpamSender toRet = new SpamSender(ctx);
		toRet.addField("address", "fabiociglia");
		toRet.addField("person", "");
		return toRet;
	}
	
	@Override public HashMap<String, String> getMap() {
//		HashMap<String, String> toRet = new HashMap<String, String>();
//		toRet.put("ROW1", getFieldString("address"));
//		toRet.put("ROW2", getFieldString("person")!=null&&getFieldString("person").length()>0 ? Uri.withAppendedPath(People.CONTENT_URI, getFieldString("person")).toString() : "default" );
//		toRet.put("address", getFieldString("address"));
//		toRet.put("isSpamSender", getFieldString("address")+"|"+getFieldString("person")+"|"+"true");
//		return toRet;
		return getViewMap( getContext(), getFieldString("address"), getFieldString("person") );
	}
	
	@SuppressWarnings("deprecation")
	public static HashMap<String, String> getViewMap(Context ctx, String address, String person) {
		HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put("address", null);
        tmp.put("person", person);
        tmp.put("ROW2", "default");
        if( person!=null && person.length() > 0 ) {
        	Uri myPerson = Uri.withAppendedPath(People.CONTENT_URI, person);
//	        	Bitmap bm = People.loadContactPhoto(ctx, myPerson, 0, null);
        	Cursor managedCursor = ctx.getContentResolver().query(People.CONTENT_URI, new String[]{ People.DISPLAY_NAME}, People._ID+"=? ", new String[]{ person }, null );
        	if(managedCursor!=null && managedCursor.moveToFirst()) {
        		tmp.put("ROW1", managedCursor.getString(managedCursor.getColumnIndex(People.DISPLAY_NAME)));
        		tmp.put("address", address);
//        		tmp.put("isSpamSender", address+"|"+person+"|"+SSFlib.isSpamSender(ctx, myPerson).toString() );
        	} else {
        		tmp.put("ROW1", address);
//        		tmp.put("isSpamSender", address+"|"+person+"|"+SSFlib.isSpamSender(ctx, address).toString() );
        	}
        	tmp.put("ROW2", myPerson.toString());
        } else {
        	tmp.put("ROW1", address);
//        	tmp.put("isSpamSender", address+"|"+person+"|"+SSFlib.isSpamSender(ctx, address).toString() );
        }
        return tmp;
	}
	
	public SimpleAdapter getListAdapter(Vector<ListableBase> items) {
		Vector<HashMap<String, String>> data = getListData(items);
		SimpleAdapter adapter = new SimpleAdapter(
				getContext(),
				data,
				R.layout.sender_list_item,
        		new String[]{"ROW1","ROW2","address"
//					,"isSpamSender"
					},
        		new int[]{android.R.id.text1, android.R.id.icon, android.R.id.text2
//					, android.R.id.checkbox
					}
		);
		adapter.setViewBinder(SpamSender.getViewBinder(getContext()));
		return adapter;
	}
	
	public static ViewBinder getViewBinder(final Context ctx) {
		return new SimpleAdapter.ViewBinder() {
			@Override public boolean setViewValue(View view, Object data, String textRepresentation) {
				if( view.getClass().equals(ImageView.class) ) {
					if( textRepresentation!=null && textRepresentation.equals("default") ) {
						((ImageView)view).setImageResource(R.drawable.userimage_back);
					} else {
						// FROM API LEVEL 5
//						InputStream input = null;
//						try {
//							//ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(textRepresentation));
//							Uri uri = Uri.parse(textRepresentation);
//							input = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(), uri);
//						} catch(Exception e) {
//							if(isDebug()) {
//								Log.e(TAG,"getViewBinder(Image)",e);
//							}
//							input = null;
//						}
//						if(input == null) {
//							((ImageView)view).setImageResource(R.drawable.userimage_back);
//						} else {
//							((ImageView)view).setImageDrawable(BitmapDrawable.createFromStream(input, textRepresentation));
//						}
						// API LEVEL <= 4
						Bitmap bm = People.loadContactPhoto(ctx, Uri.parse(textRepresentation), R.drawable.userimage_back, null);
						((ImageView)view).setImageBitmap(bm);
					}
					return true;
				}
//				else if( view.getClass().equals(CheckBox.class) ) {
//					final CheckBox tmp = (CheckBox) view;
//					if( textRepresentation.indexOf("|") >= 0 ) {
//						String[] parts = textRepresentation.split("\\|",3);
//						textRepresentation = parts[2];
//						final SpamSender ss = new SpamSender(ctx,parts[0],parts[1]);
//						tmp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//							@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//								if( isChecked ) {
//									System.out.println("store di "+ss.getFieldString("address"));
//									ss.store();
//								} else {
//									ss.delete();
//								}
//							}
//						});
//					}
//					tmp.setChecked( Boolean.parseBoolean(textRepresentation) );
//					return true;
//				}
				else {
					return false;
				}
			}
		};
	}
	
	public void delete() {
		InputSourceSQLite db = new InputSourceSQLite(getContext(), SSFlib.DBNAME);
		db.delete(this);
		db.rawSQL("DELETE FROM `"+TBLNAME+"` " +
				"WHERE `address` = '"+getFieldString("address")+"' OR" +
				"`person` = '"+getFieldString("person")+"'");
		db.close();
	}
	
	public void store() {
		InputSourceSQLite db = new InputSourceSQLite(getContext(), SSFlib.DBNAME);
		db.store(this);
		db.close();
	}
}
