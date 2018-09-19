package in.arula.myorder.networkutil;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ConnectionUtils {

	private static DefaultHttpClient client= new DefaultHttpClient();

	public static String exceute(String url, String methodType,String postdata) throws CustomException {
		String result = null;

		// postdata = encryption;

		HttpPost post = new HttpPost(url);
		Log.v("111111...", "AAA..." + url);
		post.setHeader("Content-Type", "text/plain");
		try {
			post.setEntity(new StringEntity(postdata));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			HttpResponse httpresponse;
			httpresponse = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()));
			String line = "";
			StringBuilder stringbuilder = new StringBuilder();
			System.out.println("Printing the webservice response:");
			while ((line = rd.readLine()) != null) {
				stringbuilder.append(line);
				System.out.println(line);
			}
			result = stringbuilder.toString();

		} catch (IOException ex) {
			// error occurred while reading the response from the server
			throw new CustomException(
					"Error Occured when reading the response from Server");
		} catch (Exception e) {
			// unknown error occured while receiving responce from the server
			throw new CustomException(
					"Error Occured when reading the response from Server");
		} finally {

		}
		return result;

	}

}
