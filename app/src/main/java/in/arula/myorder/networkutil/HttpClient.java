package in.arula.myorder.networkutil;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * Class for communicating with the sever.
 * jonnalagadda.siva
 */

@SuppressLint("NewApi")
public class HttpClient {
    public static boolean offline = false;
    public static final String OFFLINE = "3";
    private static final String LOG_TAG = HttpClient.class.getName();
    private static final String HTTP_ACCEPT_LANGUAGE = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
    private static final int CONNECTION_TIMEOUT = 20000; // 20 seconds
    public static String HTTP_USER_AGENT = "wms";
    /**
     * Downloading data from server
     * @param url   -----> web service url
     * @param asBytes  ---> if the value is true return data in bytes form else string format
     * @param onComplete  ---> class to post the data to ui
     */
    public static void download(final String url, final boolean asBytes, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            onComplete.execute(false, null, "wms not connected");
            return;
        }

        final String encoded_url = encodeURL(url);
        Log.d(HttpClient.class.getName(), "######## encoded_url: " + encoded_url);

        // Not running on main thread so can use AndroidHttpClient.newInstance
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("wms");

        // allow redirects
        HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpGet getRequest = new org.apache.http.client.methods.HttpGet(encoded_url);

        InputStream inputStream = null;
        try {
        	 //trying gzip to make downloads fast.
            getRequest.addHeader("Accept-Encoding", "gzip");
            final org.apache.http.HttpResponse response = client.execute(getRequest);

            Header header = response.getFirstHeader("Content-Encoding");

            if(header != null && header.getValue().equalsIgnoreCase("gzip"))
            {
            	try
            	{
            		inputStream = response.getEntity().getContent();
            		inputStream = new GZIPInputStream(inputStream);

            		//making inputStream back to entity
            		BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            		basicHttpEntity.setContent(inputStream);
            		response.setEntity(basicHttpEntity);
            	}

            	catch(Exception e)
            	{
            		Log.e(HttpClient.class.getName(), "getting data into GZip: ("+encoded_url+")", e);
            	}
            }

            final int statusCode = response.getStatusLine().getStatusCode();
			Log.e(HttpClient.class.getName(), "inside...downloader.."+statusCode);

            if (statusCode != HttpStatus.SC_OK) {
                Log.e(HttpClient.class.getName(), "Error " + statusCode + " while retrieving data from " + encoded_url + "\n" + response.getStatusLine().getReasonPhrase());
                onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+encoded_url, "Error " + statusCode + " while retrieving data from " + encoded_url + "\n" + response.getStatusLine().getReasonPhrase());
                return;
            }

            /** If response came successfully **/
            onComplete.execute(true, asBytes?org.apache.http.util.EntityUtils.toByteArray(response.getEntity()):org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8"), null);
        } catch (Exception e) {
            Log.e(HttpClient.class.getName(), e);
            getRequest.abort();

            onComplete.execute(false, null, e.getMessage());
        } finally {
        	if(inputStream != null){
        		try{
        			inputStream.close();
        		}
        		catch(Exception e)
        		{
        			//ignore
        		}
        	}
            client.close();
        }
    }


    public static void get(final String url, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            onComplete.execute(false, OFFLINE, "wms not connected");
            return;
        }

        final String encoded_url = encodeURL(url);
        Log.d(LOG_TAG, "######## url: " + encoded_url);

        OkHttpClient client = new OkHttpClient();
        client.setFollowSslRedirects(true);
        client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        try {
            URL requestURL = new URL(encoded_url);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(requestURL)
                    .method("GET", null)
                    .header("User-Agent", HTTP_USER_AGENT)
                    .header("Accept-Language", HTTP_ACCEPT_LANGUAGE);
                      Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();
            final int statusCode = response.code();
            if (statusCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                onComplete.execute(false, statusCode + "|" + response.message() + "|" + encoded_url, "Error " + statusCode + " while retrieving data from " + encoded_url + "\nreason phrase: " + response.message());
                return;
            }
            onComplete.execute(true, response.body().string(), null);
        } catch (Exception e) {
           Log.e(LOG_TAG, "accessing: (" + encoded_url + ")", e);
            onComplete.execute(false, null, e.getMessage());
        } finally {
            client = null;
        }
    }



	/**
	 * Replacing empty spaces with %20
	 * @param url  ---> Input url
	 * @return  ----> encoded url
	 */
    private static String encodeURL(String url) {
    	 return url.trim().replaceAll(" ", "%20");
    }


    /**
     * If only header are required from server
     * @param url  ---> Input url
     * @param onComplete  ---> Interface for executing result
     */
    public static void get_header(final String url, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            onComplete.execute(false, OFFLINE, "wms not connected");
            return;
        }

        final String encoded_url = encodeURL(url);
        Log.d(HttpClient.class.getName(), "######## url: " + encoded_url);

        // Not running on main thread so can use AndroidHttpClient.newInstance
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("wms");

        // allow redirects
        HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpHead getRequest = new org.apache.http.client.methods.HttpHead(encoded_url);

        try {
            final org.apache.http.HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            onComplete.execute(HttpStatus.SC_OK==statusCode, response.getAllHeaders(), String.valueOf(statusCode));
        } catch (Exception e) {
            Log.e(HttpClient.class.getName(), "accessing: "+encoded_url, e);
            getRequest.abort();

            onComplete.execute(false, null, e.getMessage());
        } finally {
            client.close();
        }
    }

    /**
     * Dowloading the data with authentication
     * @param url
     * @param username
     * @param password
     * @param asBytes
     * @param onComplete
     */
    public static void downloadWithAuthentication(final String url, final String username, final String password, final boolean asBytes, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            onComplete.execute(false, null, "wms not connected");
            return;
        }

       Log.d(HttpClient.class.getName(), "######## url: " + url);

        // Not running on main thread so can use AndroidHttpClient.newInstance
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

        HttpParams params = new BasicHttpParams();
        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
        DefaultHttpClient client = new DefaultHttpClient(cm, params);
        BasicCredentialsProvider bcp = new BasicCredentialsProvider();
        // get ip/host and port from URL
        final Uri uri = Uri.parse(url);
        Log.d(HttpClient.class.getName(), uri.getHost() + " : " + uri.getPort());
        bcp.setCredentials(
                new AuthScope(uri.getHost(), uri.getPort()),
                new UsernamePasswordCredentials(username, password)
        );
        client.setCredentialsProvider(bcp);

        // allow redirects
//        HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpGet getRequest = new org.apache.http.client.methods.HttpGet(url);

        try {
            final org.apache.http.HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
               Log.e(HttpClient.class.getName(), "Error " + statusCode + " while retrieving data from " + url + "\n" + response.getStatusLine().getReasonPhrase());
                onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+url, "Error " + statusCode + " while retrieving data from " + url + "\n" + response.getStatusLine().getReasonPhrase());
                return;
            }

            // success
            onComplete.execute(true, asBytes?org.apache.http.util.EntityUtils.toByteArray(response.getEntity()):org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8"), null);
        } catch (Exception e) {
            Log.e(HttpClient.class.getName(), e);
            getRequest.abort();

            onComplete.execute(false, null, e.getMessage());
        } finally {
            client = null;
        }
    }

    public static void downloadWithAuthentication(final String[] urls, final String username, final String password, final boolean asBytes, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            onComplete.execute(false, null, "wms not connected");
            return;
        }

        String[] responses = new String[urls.length];

        // Not running on main thread so can use AndroidHttpClient.newInstance
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

        HttpParams params = new BasicHttpParams();
        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
        DefaultHttpClient client = new DefaultHttpClient(cm, params);
        BasicCredentialsProvider bcp = new BasicCredentialsProvider();
        // get ip/host and port from URL
        final Uri uri = Uri.parse(urls[0]);
      Log.d(HttpClient.class.getName(), uri.getHost() + " : " + uri.getPort());
        bcp.setCredentials(
                new AuthScope(uri.getHost(), uri.getPort()),
                new UsernamePasswordCredentials(username, password)
        );
        client.setCredentialsProvider(bcp);

        // allow redirects
//        HttpClientParams.setRedirecting(client.getParams(), true);

        for (int i=0; i<urls.length; i++) {
           Log.d(HttpClient.class.getName(), "######## url[" + i + "]: " + urls[i]);
            final org.apache.http.client.methods.HttpGet getRequest = new org.apache.http.client.methods.HttpGet(urls[i]);

            try {
                final org.apache.http.HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                   Log.e(HttpClient.class.getName(), "Error " + statusCode + " while retrieving data from " + urls[i] + "\n" + response.getStatusLine().getReasonPhrase());
                    onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+urls[i], "Error " + statusCode + " while retrieving data from " + urls[i] + "\n" + response.getStatusLine().getReasonPhrase());
                    return;
                }
                responses[i] = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (Exception e) {
                Log.e(HttpClient.class.getName(), e);
                getRequest.abort();
                onComplete.execute(false, null, e.getMessage());
            }
        }

        if (null != client) client = null;

        // success
        onComplete.execute(true, responses, null);
    }

    public static void postWithAuthentication(final String url, final String username, final String password, final String data, final String contentType, final boolean asBytes, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            onComplete.execute(false, null, "wms not connected");
            return;
        }

        Log.d(HttpClient.class.getName(), "######## url: " + url);

        // Not running on main thread so can use AndroidHttpClient.newInstance
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

        HttpParams params = new BasicHttpParams();
        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
        DefaultHttpClient client = new DefaultHttpClient(cm, params);
        BasicCredentialsProvider bcp = new BasicCredentialsProvider();
        // get ip/host and port from URL
        final Uri uri = Uri.parse(url);
       Log.d(HttpClient.class.getName(), uri.getHost() + " : " + uri.getPort());
        bcp.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(username, password));
        client.setCredentialsProvider(bcp);

        // allow redirects
        // HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpPost postRequest = new org.apache.http.client.methods.HttpPost(url);

        if (null != contentType) {
            postRequest.addHeader("Content-type","application/json");
        }

        try {
            postRequest.setEntity(new StringEntity(data, "UTF8"));
            final org.apache.http.HttpResponse response = client.execute(postRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+url, "Error " + statusCode + " while retrieving data from " + url + "\nreason phrase: " + response.getStatusLine().getReasonPhrase());
                return;
            }

            // success
            onComplete.execute(true, asBytes ? org.apache.http.util.EntityUtils.toByteArray(response.getEntity()) : org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8"), null);
        } catch (Exception e) {
            Log.e(HttpClient.class.getName(), e);
            postRequest.abort();

            onComplete.execute(false, null, e.getMessage());
        } finally {
            client = null;
        }
    }

    /**
     * Posting data to server
     * @param url  ----> webservice url
     * @param values  ----> Hashmap which contains posting data keys and posring data values.
     * @param onComplete
     */
    public static void postDataToServer(final String url, final java.util.Map<String, String> values, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "wms not connected");
            return;
        }

        // Not running on main thread so can use AndroidHttpClient.newInstance
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("wms");

        // enable redirects
        HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(url);

        try {
            final java.util.List<org.apache.http.NameValuePair> params = new java.util.ArrayList<org.apache.http.NameValuePair>();
            for (java.util.Map.Entry<String, String> entry : values.entrySet()) {
                params.add(new org.apache.http.message.BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            final org.apache.http.client.entity.UrlEncodedFormEntity entity = new org.apache.http.client.entity.UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);
            final org.apache.http.HttpResponse response = client.execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT) {
               Log.e(HttpClient.class.getName(), "Error " + statusCode + " while retrieving data from " + url + "\n" + response.getStatusLine().getReasonPhrase());
                if (null != onComplete) onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+url, "Error " + statusCode + " while posting data to " + url + "\n" + response.getStatusLine().getReasonPhrase());
                return;
            }
            if (statusCode != HttpStatus.SC_NO_CONTENT) {
                final String postResponse = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.d(HttpClient.class.getName(), "\n\npost response: \n" + postResponse);
                if (null != onComplete) onComplete.execute(true, postResponse, null);
            } else {
                if (null != onComplete) onComplete.execute(true, "HttpStatus: 204 No Content", null);
            }
        } catch(Exception e) {
            Log.e(HttpClient.class.getName(), e);
            post.abort();

            if (null != onComplete) onComplete.execute(false, null, e.getMessage());
        } finally {
            client.close();
        }
    }


    /**
     * Posting data to server
     * @param url  ----> webservice url
     * @param values  ----> Hashmap which contains posting data keys and posring data values.
     * @param onComplete
     */
    public static void postDataToServerjson(final String url, final java.util.Map<String, String> values, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "not connected");
            return;
        }

        // Not running on main thread so can use AndroidHttpClient.newInstance
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("wms");
        JSONObject json = new JSONObject(values);
        Log.d(HttpClient.class.getName(), "\n\npost request: \n" + json.toString());
        // enable redirects
        HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(url);
       Log.d(HttpClient.class.getName(), "######## url: " + url);


        try {
        	StringEntity se = new StringEntity(json.toString());
            //sets the post request as the resulting string
        	post.setEntity(se);
        	post.setHeader("Accept", "application/json");
        	post.setHeader("Content-type", "application/json");
            final org.apache.http.HttpResponse response = client.execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != org.apache.http.HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT) {
                Log.e(HttpClient.class.getName(), "Error " + statusCode + " while retrieving data from " + url + "\n" + response.getStatusLine().getReasonPhrase());
                if (null != onComplete) onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+url, "Error " + statusCode + " while posting data to " + url + "\n" + response.getStatusLine().getReasonPhrase());
                return;
            }
            if (statusCode != HttpStatus.SC_NO_CONTENT) {
                final String postResponse = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.d(HttpClient.class.getName(), "\n\npost response: \n" + postResponse);
                if (null != onComplete) onComplete.execute(true, postResponse, null);
            } else {
                if (null != onComplete) onComplete.execute(true, "HttpStatus: 204 No Content", null);
            }
        } catch(Exception e) {
            Log.e(HttpClient.class.getName(), e);
            post.abort();

            if (null != onComplete) onComplete.execute(false, null, e.getMessage());
        } finally {
            client.close();
        }
    }


    /**
     * Posting data to server
     * @param url  ----> webservice url
     * @param values  ----> Hashmap which contains posting data keys and posring data values.
     * @param onComplete
     */
    public static void postDataToServerStr(final String url, final String values, final ApplicationThread.OnComplete onComplete) {
        // check the connectivity mode
        if (offline) {
            if (null != onComplete) onComplete.execute(false, null, "not connected");
            return;
        }

        // Not running on main thread so can use AndroidHttpClient.newInstance
        final android.net.http.AndroidHttpClient client = android.net.http.AndroidHttpClient.newInstance("wms");

        Log.d(HttpClient.class.getName(), "\n\npost request: \n" + values);
        // enable redirects
        HttpClientParams.setRedirecting(client.getParams(), true);

        final org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(url);
       Log.d(HttpClient.class.getName(), "######## url: " + url);


        try {
        	StringEntity se = new StringEntity(values);
            //sets the post request as the resulting string
        	post.setEntity(se);
        	post.setHeader("Accept", "application/json");
        	post.setHeader("Content-type", "application/json");
            final org.apache.http.HttpResponse response = client.execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != org.apache.http.HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT) {
                Log.e(HttpClient.class.getName(), "Error " + statusCode + " while retrieving data from " + url + "\n" + response.getStatusLine().getReasonPhrase());
                if (null != onComplete) onComplete.execute(false, statusCode+"|"+response.getStatusLine().getReasonPhrase()+"|"+url, "Error " + statusCode + " while posting data to " + url + "\n" + response.getStatusLine().getReasonPhrase());
                return;
            }
            if (statusCode != HttpStatus.SC_NO_CONTENT) {
                final String postResponse = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.d(HttpClient.class.getName(), "\n\npost response: \n" + postResponse);
                if (null != onComplete) onComplete.execute(true, postResponse, null);
            } else {
                if (null != onComplete) onComplete.execute(true, "HttpStatus: 204 No Content", null);
            }
        } catch(Exception e) {
            Log.e(HttpClient.class.getName(), e);
            post.abort();

            if (null != onComplete) onComplete.execute(false, null, e.getMessage());
        } finally {
            client.close();
        }
    }


}
