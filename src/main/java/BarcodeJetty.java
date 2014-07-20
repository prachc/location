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
public class BarcodeJetty extends HttpServlet {
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
        //proofOfLife = androidContext.getApplicationInfo().packageName;
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
        String[][] bars = getBarcode();
        out.println("<html>");
        out.println("<h1>Barcode Scanner Result</h1>");
        out.println("<h1>Content:"+bars[0][0]+"</h1>");
        out.println("<h1>Format:"+bars[1][0]+"</h1>");
        out.println("</html>");
        out.flush();
    }
    
    private String[][] getBarcode(){
    	IBReceiver = new IntentBroadcastReceiver();
    	Intent intent = new Intent("com.prach.mashup.SMA");
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	
    	String[] msg = {
			"com.google.zxing.client.android.SCAN",
			"RESULT:SCAN_RESULT","RESULT:SCAN_RESULT_FORMAT"};
    	IBReceiver.ResultNameVector.add("SCAN_RESULT");
    	IBReceiver.ResultNameVector.add("SCAN_RESULT_FORMAT");
    	intent.putExtra("MSG", msg);
    	
    	IntentFilter IFfinished = new IntentFilter("com.prach.mashup.FINISHED");
    	androidContext.registerReceiver(IBReceiver,IFfinished,null,null);
    	IBReceiver.running = true;
    	androidContext.startActivity(intent);
    	
    	Log.i("BarcodeJetty.getBarcode()","call intent:com.prach.mashup.SMA");
    	for (int i = 0; i < msg.length; i++)
    		Log.i("BarcodeJetty.getBarcode()","msg["+i+"]:"+msg[i]);
		
    	while(IBReceiver.running){}
    	androidContext.unregisterReceiver(IBReceiver);
    	
    	int count_resultname = IBReceiver.ResultNameVector.size();
    	int count_resultarrayname = IBReceiver.ResultArrayNameVector.size();
    	int allcount = count_resultarrayname + count_resultname;
    	
    	Log.i("BarcodeJetty.getBarcode()","count_resultname:"+count_resultname);
    	Log.i("BarcodeJetty.getBarcode()","count_resultarrayname:"+count_resultarrayname);
    	Log.i("BarcodeJetty.getBarcode()","allcount:"+allcount);
    	    	
    	String[][] resultstrings = null;
    	
    	if(IBReceiver.ResultStringVector.get(0).equals("RESULT_OK")){
    		resultstrings = new String[allcount][];
    		
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
