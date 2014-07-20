import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

@SuppressWarnings("serial")
public class LocationJson extends HttpServlet{
    //private String proofOfLife = null;
    private Context androidContext;
    //private Class IJetty;
    //public IntentLinkActivity ilActivity;
    IntentBroadcastReceiver IBReceiver;
    
    /* ------------------------------------------------------------ */
    public void init(ServletConfig config) throws ServletException{
    	super.init(config);
    	//to demonstrate it is possible
        //IJetty = (Class) config.getServletContext().getAttribute("org.mortbay.ijetty.IJetty");
    	@SuppressWarnings("unused")
    	ContentResolver resolver = (android.content.ContentResolver)config.getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");;
        androidContext = (android.content.Context)config.getServletContext().getAttribute("org.mortbay.ijetty.context");
        //proofOfLife = androidContext.getApplicationInfo().packageName;
        //androidContext.
        //locationManager = (LocationManager) androidContext.getSystemService(Context.LOCATION_SERVICE);
        //loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    /* ------------------------------------------------------------ */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

    /* ------------------------------------------------------------ */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //response.setContentType("application/json");
    	response.setContentType("text/html");
        ServletOutputStream out = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_OK);
        Log.i("LocationJson.doGet()","setStatus(200)");
        /*String[] locs = getLocation();
        out.print("{\"latitude\":\""+locs[0]+"\",\"longitude\":\""+locs[1]+"\"}");
        */
        out.print(getLocationJSON()[0][0]);
        out.flush();
    }
    
    
    
    private String[][] getLocationJSON(){
    	IBReceiver = new IntentBroadcastReceiver();
    	Intent intent = new Intent("com.prach.mashup.SMA");
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	
    	String[] msg = {"com.prach.mashup.GPS","RESULT:JSON_RESULT","EXTRA:MODE","PASSIVE","EXTRA:TYPE","JSON"};
    	IBReceiver.ResultNameVector.add("JSON_RESULT");
    	intent.putExtra("MSG", msg);
    	
    	IntentFilter IFfinished = new IntentFilter("com.prach.mashup.FINISHED");
    	androidContext.registerReceiver(IBReceiver,IFfinished,null,null);
    	IBReceiver.running = true;
    	androidContext.startActivity(intent);
    	
    	Log.i("LocationJson.getLocationJSON()","call intent:com.prach.mashup.SMA");
    	for (int i = 0; i < msg.length; i++)
    		Log.i("LocationJson.getLocationJSON()","msg["+i+"]:"+msg[i]);
		
    	while(IBReceiver.running){}
    	
    	androidContext.unregisterReceiver(IBReceiver);
    	
    	int count_resultname = IBReceiver.ResultNameVector.size();
    	int count_resultarrayname = IBReceiver.ResultArrayNameVector.size();
    	int allcount = count_resultarrayname + count_resultname;
    	
    	Log.i("LocationJson.getLocationJSON()","count_resultname:"+count_resultname);
    	Log.i("LocationJson.getLocationJSON()","count_resultarrayname:"+count_resultarrayname);
    	Log.i("LocationJson.getLocationJSON()","allcount:"+allcount);
    	
    	String[][] resultstrings = null;
    	
    	if(IBReceiver.ResultStringVector.get(0).equals("RESULT_OK")){
    		resultstrings = new String[allcount][];
        	//String[][] resultsarray = new String[count_resultarrayname][];
    		
    		for (int i = 0; i < count_resultname; i++){
				resultstrings[i] = new String[1];
				resultstrings[i][0] = IBReceiver.ResultStringVector.get(i+1);
    		}
    		for (int i = count_resultname; i < allcount; i++) {
				resultstrings[i] = IBReceiver.ResultStringArrayVector.get(i-count_resultname);
			}
        	
        }else if(IBReceiver.ResultStringVector.get(0).equals("RESULT_CANCELED")){
        	resultstrings = new String[allcount][];
        	for (int i = 0; i < count_resultname; i++){
				resultstrings[i] = new String[1];
				resultstrings[i][0] = "null";
    		}
    		for (int i = count_resultname; i < allcount; i++) {
    			resultstrings[i] = new String[1];
				resultstrings[i][0] = "null";
			}
        }else 
        	;
        
    	return resultstrings;
    }
    
}
