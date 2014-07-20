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
public class LocationJetty extends HttpServlet {
    //private String proofOfLife = null;
    private Context androidContext;
    IntentBroadcastReceiver IBReceiver;
    /* ------------------------------------------------------------ */
    public void init(ServletConfig config) throws ServletException
    {
    	super.init(config);
    	//to demonstrate it is possible
        Object o = config.getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
        @SuppressWarnings("unused")
		ContentResolver resolver = (android.content.ContentResolver)o;
        androidContext = (android.content.Context)config.getServletContext().getAttribute("org.mortbay.ijetty.context");
       // proofOfLife = androidContext.getApplicationInfo().packageName;
        //locationManager = (LocationManager) androidContext.getSystemService(Context.LOCATION_SERVICE);
        //loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
    }

    /* ------------------------------------------------------------ */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }

    /* ------------------------------------------------------------ */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        ServletOutputStream out = response.getOutputStream();
        String[][] locs = getLocation();
        out.println("<html>");
        out.println("<h1>Your Current Position Is</h1>");
        out.println("<h1>Latitude:"+locs[1][0]+"</h1>");
        out.println("<h1>Longitude:"+locs[1][1]+"</h1>");
        out.println("<h1>Provider:"+locs[0][0]+"</h1>");
        //out.println("Brought to you by: "+proofOfLife);
        out.println("</html>");
        out.flush();
    }
    
    private String[][] getLocation(){
    	IBReceiver = new IntentBroadcastReceiver();
    	Intent intent = new Intent("com.prach.mashup.SMA");
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	
    	String[] msg = {"com.prach.mashup.GPS","RESULT:PROVIDER","RESULTS:COOR","EXTRA:MODE","PASSIVE","EXTRA:TYPE","null"};
    	IBReceiver.ResultNameVector.add("PROVIDER");
    	IBReceiver.ResultArrayNameVector.add("COOR");
    	intent.putExtra("MSG", msg);
    	
    	IntentFilter IFfinished = new IntentFilter("com.prach.mashup.FINISHED");
    	androidContext.registerReceiver(IBReceiver,IFfinished,null,null);
    	IBReceiver.running = true;
    	androidContext.startActivity(intent);
    	
    	Log.i("LocationJetty.getLocation()","call intent:com.prach.mashup.SMA");
    	for (int i = 0; i < msg.length; i++)
    		Log.i("LocationJetty.getLocation()","msg["+i+"]:"+msg[i]);
		
    	while(IBReceiver.running){}
    	
    	androidContext.unregisterReceiver(IBReceiver);
    	
    	int count_resultname = IBReceiver.ResultNameVector.size();
    	int count_resultarrayname = IBReceiver.ResultArrayNameVector.size();
    	int allcount = count_resultarrayname + count_resultname;
    	
    	Log.i("LocationJetty.getLocation()","count_resultname:"+count_resultname);
    	Log.i("LocationJetty.getLocation()","count_resultarrayname:"+count_resultarrayname);
    	Log.i("LocationJetty.getLocation()","allcount:"+allcount);
    	
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
    	
        	/*for (int i = 0; i < resultstrings.length; i++)
        		resultstrings[i] = "null";*/
    	
    	
    	/*Location loc;
    	LocationManager locMan;
    	String locPro;
    	List<String> proList;
  				
    	//Get the location manager from the server
    	locMan = (LocationManager) androidContext.getSystemService(Context.LOCATION_SERVICE);

     	proList = locMan.getProviders(true);
    	
    	//Just grab the first member of the list. It's name will be "gps"
    	locPro = proList.get(0);
    	loc = locMan.getLastKnownLocation(locPro);

    	float Lat =  (float)loc.getLatitude();
    	float Lon =  (float)loc.getLongitude();

    	return new String[] {""+Lat,""+Lon};*/
    }
    
}
