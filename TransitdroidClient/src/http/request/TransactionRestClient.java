package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
 
public class TransactionRestClient {
 
	public static void main(String[] args) {
 
	  try {
 
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(
			"http://transitdroid.net/GO/rest/transaction/process/");
		
		StringEntity input = new StringEntity("{\"keys\":[{\"key\":\"ssk3x+fxnQg=\",\"key\":\"U71bfTS/J3c=\",\"key\":\"29JDFiZn3CvQ=\",\"key\":\"syoZjiFUBB0=\"}], \"passcode\":\"8h0nAD6gTAI=\",\"phoneMac\":\"784930214321\"}");
		input.setContentType("application/json");
		postRequest.setEntity(input);
 
		HttpResponse response = httpClient.execute(postRequest);
 
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatusLine().getStatusCode());
		}
 
		BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));
 
		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
 
		httpClient.getConnectionManager().shutdown();
 
	  } catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	  } catch (Exception e) {
		  
			e.printStackTrace();
	 
		  }
 
	}
 
}