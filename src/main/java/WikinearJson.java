import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.util.Log;

@SuppressWarnings("serial")
public class WikinearJson extends HttpServlet {
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
        ServletOutputStream out = response.getOutputStream();
        out.print(getJSON(request));
        out.flush();
    }
	
	@SuppressWarnings("unchecked")
	public String getJSON(HttpServletRequest request)throws IOException{
		StringBuffer sb = new StringBuffer();
		sb.append("http://ws.geonames.org/findNearbyWikipediaJSON");
		boolean first = true;
		
		Enumeration paramNames = request.getParameterNames();
	    while(paramNames.hasMoreElements()) {
	    	if(first){
	    		sb.append("?");
	    		first=false;
	    	}else
	    		sb.append("&");
	    	String param = (String)paramNames.nextElement();
	    	sb.append(param);
	    	sb.append("=");
	    	sb.append(request.getParameter(param));
	    }
		Log.i("WikinearJson.getJSON()","url:"+sb.toString());
		
		URL url = null;
		try {
			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			Log.e("WikinearJson.getJSON()",e.toString());
		}
		
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		DataInputStream dis = new DataInputStream(urlConnection.getInputStream());
		//BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
		sb.delete(0, sb.length());
		String inputLine = null;
		while ((inputLine = dis.readLine()) != null)
			sb.append(inputLine);
		
		return sb.toString();
	}
}
