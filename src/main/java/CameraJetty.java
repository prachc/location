import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.util.IO;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

@SuppressWarnings("serial")
public class CameraJetty extends HttpServlet {
	// private String proofOfLife = null;
	private Context androidContext;
	IntentBroadcastReceiver IBReceiver;
	private static final String TAG = "CameraJetty";
	private ContentResolver resolver;

    private int __THUMB_WIDTH = 120;
    private int __THUMB_HEIGHT = 120;

	/* ------------------------------------------------------------ */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// to demonstrate it is possible
		Object o = config.getServletContext().getAttribute(
				"org.mortbay.ijetty.contentResolver");
		resolver = (android.content.ContentResolver) o;
		androidContext = (android.content.Context) config.getServletContext()
				.getAttribute("org.mortbay.ijetty.context");
		// proofOfLife = androidContext.getApplicationInfo().packageName;
		// locationManager = (LocationManager)
		// androidContext.getSystemService(Context.LOCATION_SERVICE);
		// loc =
		// locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	}

	/* ------------------------------------------------------------ */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/* ------------------------------------------------------------ */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		ServletOutputStream out = response.getOutputStream();
		takePicture();
		doGetFetchMedia(request,response,Uri.fromFile(new File("/sdcard/intent_image.jpg")));
		/*out.println("<html>");
		out.println("<h1>Barcode Scanner Result</h1>");
		//out.println("<h1>Content:" + bars[0][0] + "</h1>");
		//out.println("<h1>Format:" + bars[1][0] + "</h1>");
		out.println("</html>");*/
		out.flush();
	}

	public void takePicture() {
		IBReceiver = new IntentBroadcastReceiver();
		Intent intent = new Intent("com.prach.mashup.SMA");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		File out = new File(Environment.getExternalStorageDirectory(),
				"intent_image.jpg");
		Uri uri = Uri.fromFile(out);

		String[] msg = { MediaStore.ACTION_IMAGE_CAPTURE,
				"EXTRA:" + MediaStore.EXTRA_VIDEO_QUALITY, "1",
				"URI:" + MediaStore.EXTRA_OUTPUT, "INDEX=0" };

		Uri[] uris = { uri };
		intent.putExtra("MSG", msg);
		intent.putExtra("URIS", uris);

		IntentFilter IFfinished = new IntentFilter("com.prach.mashup.FINISHED");
		androidContext.registerReceiver(IBReceiver, IFfinished, null, null);
		IBReceiver.running = true;
		androidContext.startActivity(intent);

		Log.i("CameraJetty.getBarcode()", "call intent:com.prach.mashup.SMA");
		for (int i = 0; i < msg.length; i++)
			Log.i("BarcodeJetty.getBarcode()", "msg[" + i + "]:" + msg[i]);

		while (IBReceiver.running) {
		}
		androidContext.unregisterReceiver(IBReceiver);
	}

	public void doGetFetchMedia(HttpServletRequest request,
			HttpServletResponse response, Uri content) throws ServletException, IOException {
		try {
			//Uri content = Uri.withAppendedPath(contenturi, item);
			Log.i(TAG, "Exporting original media");
			Log.i(TAG, "uri="+content.toString());
			response.setContentType(resolver.getType(content));
			InputStream stream = resolver.openInputStream(content);
			OutputStream os = response.getOutputStream();

			response.setStatus(HttpServletResponse.SC_OK);
			IO.copy(stream, os);
			
		} catch (Exception e) {
			Log.w(TAG, "Failed to fetch media", e);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
