import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class IntentBroadcastReceiver extends BroadcastReceiver {
	public boolean running = false;
	public Vector<String> ResultStringVector = new Vector<String>();
	public Vector<String> ResultNameVector = new Vector<String>();
	public Vector<String> ResultArrayNameVector = new Vector<String>();
	public Vector<String[]> ResultStringArrayVector = new Vector<String[]>();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		running = false;
		ResultStringVector.removeAllElements();
		ResultStringArrayVector.removeAllElements();
		String[] extras = intent.getStringArrayExtra("STRING_VECTOR");
		for (int i = 0; i < extras.length; i++) {
			ResultStringVector.add(extras[i]);
			Log.i("IBRcv.onReceive()","result:"+extras[i]);
		}
		Log.i("IBRcv.onReceive()","ResultStringVector.size():"+ResultStringVector.size());
		
		String arraypattern = intent.getStringExtra("ARRAY_VECTOR_PATTERN"); //"1:1" {"lan","card"}
		Log.i("IBRcv.onReceive()","arraypattern:"+arraypattern);
		if(!arraypattern.equals("null")){
			String[] patterns = arraypattern.split(":");
			String[] allarray = intent.getStringArrayExtra("ARRAY_VECTOR");
			
			StringBuffer allmsg = new StringBuffer();
			for (int j = 0; j < allarray.length; j++) {
				allmsg.append("["+j+"]");
				allmsg.append(allarray[j]);
				allmsg.append("\n");
			}
			Log.i("IBRcv.onReceive()","allarray/\n"+allmsg.toString());
			
			int allcount = 0;
			for (int i = 0; i < patterns.length; i++) {
				int count = Integer.parseInt(patterns[i]);
				String[] arraytemp = new String[count];
				for (int j = 0; j < arraytemp.length; j++)
					arraytemp[j] = allarray[allcount++];
				ResultStringArrayVector.add(arraytemp);
			}
			Log.i("IBRcv.onReceive()","ResultStringArrayVector.size():"+ResultStringArrayVector.size());
		}
	}
}
