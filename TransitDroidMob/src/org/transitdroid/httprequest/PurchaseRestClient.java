package org.transitdroid.httprequest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;
import org.shipp.activity.PrincipalActivity;
import org.shipp.activity.PrincipalActivity.PurchaseType;
import org.shipp.activity.PrincipalActivity.RequestMethod;

import android.os.AsyncTask;
import android.util.Log;

public class PurchaseRestClient extends AsyncTask<HttpUriRequest, Void, String>
{
	RequestMethod method;
	PurchaseType type;
	int quantity;
	
	public PurchaseRestClient(PrincipalActivity.RequestMethod method, PurchaseType type, int qty){
		this.method = method;
		this.type = type;
		this.quantity = qty;
	}
private String responseText;
private HttpResponse response;

public String getResonseText(){
	return responseText;
}
public HttpResponse getHttpResponse(){
	return response;
}

protected String getASCIIContentFromEntity(HttpEntity entity)
		throws IllegalStateException, IOException {
	InputStream in = entity.getContent();
	StringBuffer out = new StringBuffer();
	int n = 1;
	while (n > 0) {
		byte[] b = new byte[4096];
		n = in.read(b);
		if (n > 0)
			out.append(new String(b, 0, n));
	}
	return out.toString();
}

@Override
protected String doInBackground(HttpUriRequest... params) {
	
	HttpClient httpClient1 = new DefaultHttpClient();
	HttpContext localContext = new BasicHttpContext();
	String text = null;
	
	switch(this.method){
	
	case GET:
		HttpGet httpGet = new HttpGet(
				"http://transitdroid.net/GO/rest/account/logout/PaulSmelser");
		
		try {
			HttpResponse response = httpClient1.execute(httpGet,
					localContext);
			this.response = response;
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		responseText = text;
		
	case POST:
		HttpResponse response = pushPurchase();
		this.response = response;
		HttpEntity entity = response.getEntity();
		try {
			text = getASCIIContentFromEntity(entity);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			text = "inside";
			e.printStackTrace();
		} catch (IOException e) {
			text = "inside";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		responseText = text;
		
	}
	return text;
	
}

private HttpResponse pushPurchase() {
	
    switch (type)
    {
    case SINGLE:
    	return getSingle();
    case MONTHLY:
	    return getMonthly();
    case NIGHTLY:
    	return getNightly();
    case WEEKLY:
    	return getWeekly();
    case YEARLY:
    	return getYearly();
    	default:
    		return getMonthly();
	}
}

	private HttpResponse getMonthly(){
		HttpResponse result = null;
	    HttpClient hc = new DefaultHttpClient();
	    String message;
		String url = "http://transitdroid.net/GO/rest/purchase/monthly/";
		
	    HttpPost p = new HttpPost(url);
	    JSONObject object = new JSONObject();
	    try {
	    	object.put("mobileDeviceMAC", "784930214321");
			object.put("version", "1");
			object.put("month", "4");
			object.put("year", "2013");
	
	    } catch (Exception ex) {
	
	    }
	
	    try {
	    message = object.toString();
	
	
	    p.setEntity(new StringEntity(message, "UTF8"));
	    p.setHeader("Content-type", "application/json");
	        HttpResponse resp = hc.execute(p);
	        if (resp != null) {
	        	result = resp;
	            if (resp.getStatusLine().getStatusCode() == 204){}
	               // result = true;
	        }
	
	        Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	
	    }
	
	    return result;
	}
	
	private HttpResponse getSingle(){
		HttpResponse result = null;
	    HttpClient hc = new DefaultHttpClient();
	    String message;
		String url = "http://transitdroid.net/GO/rest/purchase/single/";
		
	    HttpPost p = new HttpPost(url);
	    JSONObject object = new JSONObject();
	    try {
	    	object.put("mobileDeviceMAC", "784930214321");
			object.put("version", "7");
			object.put("quantity", ""+quantity);
			object.put("date", "2013-03-27T09:00:00");
	
	    } catch (Exception ex) {
	
	    }
	
	    try {
	    message = object.toString();
	
	
	    p.setEntity(new StringEntity(message, "UTF8"));
	    p.setHeader("Content-type", "application/json");
	        HttpResponse resp = hc.execute(p);
	        if (resp != null) {
	        	result = resp;
	            if (resp.getStatusLine().getStatusCode() == 204){}
	               // result = true;
	        }
	
	        Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	
	    }
	
	    return result;
	}
	
	private HttpResponse getNightly(){
		HttpResponse result = null;
	    HttpClient hc = new DefaultHttpClient();
	    String message;
		String url = "http://transitdroid.net/GO/rest/purchase/nightly/";
		
	    HttpPost p = new HttpPost(url);
	    JSONObject object = new JSONObject();
	    try {
	    	object.put("mobileDeviceMAC", "784930214321");
			object.put("version", "6");
			object.put("date", "2013-03-27T09:00:00");
	
	    } catch (Exception ex) {
	
	    }
	
	    try {
	    message = object.toString();
	
	
	    p.setEntity(new StringEntity(message, "UTF8"));
	    p.setHeader("Content-type", "application/json");
	        HttpResponse resp = hc.execute(p);
	        if (resp != null) {
	        	result = resp;
	            if (resp.getStatusLine().getStatusCode() == 204){}
	               // result = true;
	        }
	
	        Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	
	    }
	
	    return result;
	}
	
	private HttpResponse getYearly(){
		HttpResponse result = null;
	    HttpClient hc = new DefaultHttpClient();
	    String message;
		String url = "http://transitdroid.net/GO/rest/purchase/yearly/";
		
	    HttpPost p = new HttpPost(url);
	    JSONObject object = new JSONObject();
	    try {
	    	object.put("mobileDeviceMAC", "784930214321");
			object.put("version", "6");
			object.put("date", "2013-03-27T09:00:00");
	
	    } catch (Exception ex) {
	
	    }
	
	    try {
	    message = object.toString();
	
	
	    p.setEntity(new StringEntity(message, "UTF8"));
	    p.setHeader("Content-type", "application/json");
	        HttpResponse resp = hc.execute(p);
	        if (resp != null) {
	        	result = resp;
	            if (resp.getStatusLine().getStatusCode() == 204){}
	               // result = true;
	        }
	
	        Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	
	    }
	
	    return result;
	}
	
	private HttpResponse getWeekly(){
		HttpResponse result = null;
	    HttpClient hc = new DefaultHttpClient();
	    String message;
		String url = "http://transitdroid.net/GO/rest/purchase/single/";
		
	    HttpPost p = new HttpPost(url);
	    JSONObject object = new JSONObject();
	    try {
	    	object.put("mobileDeviceMAC", "784930214321");
			object.put("version", "6");
			object.put("date", "2013-03-27T09:00:00");
	
	    } catch (Exception ex) {
	
	    }
	
	    try {
	    message = object.toString();
	
	
	    p.setEntity(new StringEntity(message, "UTF8"));
	    p.setHeader("Content-type", "application/json");
	        HttpResponse resp = hc.execute(p);
	        if (resp != null) {
	        	result = resp;
	            if (resp.getStatusLine().getStatusCode() == 204){}
	               // result = true;
	        }
	
	        Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	
	    }
	
	    return result;
	}
}
