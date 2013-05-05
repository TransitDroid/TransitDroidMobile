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
import org.shipp.activity.PrincipalActivity.RequestMethod;

import android.os.AsyncTask;
import android.util.Log;

public class RegisterRestClient extends AsyncTask<HttpUriRequest, Void, String>
{
	RequestMethod method;
	private String username;
	private String password;
	
	public RegisterRestClient(PrincipalActivity.RequestMethod method, String un, String pw){
		this.method = method;
		this.username = un;
		this.password = pw;
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
		HttpResponse response = pushRegister();
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

private HttpResponse pushRegister() {
	HttpResponse result = null;
    HttpClient hc = new DefaultHttpClient();
    String message;
    String url = "http://transitdroid.net/GO/rest/account/device/register/";

    HttpPost p = new HttpPost(url);
    JSONObject object = new JSONObject();
    try {

    	object.put("phoneId", "784930214321");
		object.put("username", "psmelser");
		object.put("password", "transitdroid");

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
