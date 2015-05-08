package net.fabiocigliano.sms.spam_filter;


import net.fabiocigliano.sms.spam_filter.lib.SSFlib;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSSpamFilterReceiver extends BroadcastReceiver {
	
	private final static String idfield = "_id";
	
	private final static String t1 = "text1";
	private final static String t2 = "text2";
	private final static String t3 = "text3";
	private final static String t4 = "text4";
	private final static String t5 = "text5";
	
	
    
    /** Address that sends SMS */
    private static final String ADDRESS = "BYALERT";

    /** The Action fired by the Android-System when a SMS was received.
     * We are using the Default Package-Visibility */
    private static final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";
    
    private Context mContext = null;
    
    @Override public void onReceive(Context context, Intent intent) {
    	mContext = context;
    	// controllo il db
    	SSFlib.checkDB(mContext);
    	// al ricevimento di un sms
    	if (intent.getAction().equals(ACTION_RECEIVE_SMS)) {
    		
    		if(SSFlib.DEBUG)	Log.i(SSFlib.TAG,"You've received an SMS");
    		
	    	Bundle bundle = intent.getExtras();
	    	if(bundle!=null) {
	    		// controllo gli sms in arrivo
		    	Object PDUS[] = (Object[]) bundle.get("pdus");
		    	if(SSFlib.DEBUG)	Log.i(SSFlib.TAG,"PDUS.length: "+PDUS.length);
		    	SmsMessage m = null;
		    	for (int n = 0; n < PDUS.length; n++) {
		    		// ricevo il messaggio nella struttura dati appropriata
		    		m = SmsMessage.createFromPdu((byte[]) PDUS[n]);
		    		if(SSFlib.DEBUG)	Log.i(SSFlib.TAG,"Message "+n+" from: "+m.getOriginatingAddress());
		    		
//		    		Uri deleteUri = Uri.parse("content://sms");
//	    		    SmsMessage msg = (SmsMessage) m[n];
//	    		    System.out.println("Ho eleminato sms n: "+
//	    		    context.getContentResolver().delete(deleteUri, "address=? and date=?", new String[] {msg.getOriginatingAddress(), String.valueOf(msg.getTimestampMillis())}));
//	    		    System.out.println("deleteUri: "+deleteUri);
//	    		    System.out.println("query: "+"address=? and date=?");
//	    		    System.out.println("args: "+msg.getOriginatingAddress()+" , "+String.valueOf(msg.getTimestampMillis()));
	    		    
		    		if( !SSFlib.isSpamSender( context, m ) ) {
		    			continue;
		    		}
		    		
//		    		Log.i(TAG, n+": "+m[n].getOriginatingAddress());
//		    		Log.i(TAG, n+": "+m[n].getMessageBody());
//		    		Log.i(TAG, n+": "+m[n].getTimestampMillis());
//		    		Toast.makeText(context, "Msg from: \""+m[n].getOriginatingAddress()+"\"", Toast.LENGTH_LONG).show();
//		    		
//		    		registraMovimento(
//	    				Long.toString(m[n].getTimestampMillis()),	// _id
//	    				Long.toString(m[n].getTimestampMillis()/1000),	// date
//	    				"",
//	    				m[n].getOriginatingAddress(),	// sender
//	    				m[n].getMessageBody(),	//body
//	    				estraiPrezzo(m[n].getMessageBody()).toString(),	// price
//	    				Integer.toString(0)	// exported = false
//		    		);
		    		
		    		//smsMessage[n].getMessageBody(); //Ottengo il testo dell'ultimo messaggio
					//smsMessage[n].getOriginatingAddress(); //Ottengo il numero del mittente
					//Altre operazioni
		    	}
		    	
		    	// Notifica
//	    		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//	    		long[] pattern = new long[2];
//	    		pattern[0] = 500;
//	    		pattern[1] = 300;
//	    		try { v.vibrate(pattern, 2); } catch(Exception e) { e.printStackTrace(); }
	    	}
    	}
    }
}