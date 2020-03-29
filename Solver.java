import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import com.google.gson.*;

class Solver
{
	
	public static void main(String args[]) throws IOException
	{
		GetRequest();
	}

	public static void GetRequest() throws IOException
	{
		URL urlForGetRequest = new URL("http://gallows.hulu.com/play?code=xxx@abc.edu");
    	String readLine = null;
    	HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
    	conection.setRequestMethod("GET");
    	int responseCode = conection.getResponseCode();
    	if (responseCode == HttpURLConnection.HTTP_OK) {
        	BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
        	StringBuffer response = new StringBuffer();
        	while ((readLine = in.readLine()) != null) {
            	response.append(readLine);
        	} 
        	in.close();
        	Gson g = new Gson();
			HuluObject obj = g.fromJson(response.toString(), HuluObject.class);

			HangmanAI ai = new HangmanAI();
			
			String status = obj.getStatus();

			while(status.equals("ALIVE"))
			{
				char guess = ai.makeGuess(obj.getState());	
				String newURL = "http://gallows.hulu.com/play?code=xxx@abc.edu" + "&token="+obj.getToken()+"&guess="+guess;
				URL newURLForGet = new URL(newURL);
				String newLine = "";
				HttpURLConnection secConn = (HttpURLConnection) newURLForGet.openConnection();
    			secConn.setRequestMethod("GET");
    			int respCode = secConn.getResponseCode();
    			
    			if(respCode == HttpURLConnection.HTTP_OK) {
        			BufferedReader br = new BufferedReader(new InputStreamReader(secConn.getInputStream()));
        			StringBuffer newResp = new StringBuffer();
        			while ((newLine = br.readLine()) != null) {
            			newResp.append(newLine);
        			} 
        			in.close();
        			HuluObject temp = g.fromJson(newResp.toString(), HuluObject.class);

        			if(obj.getState().equals(temp.getState()))
        				ai.update(guess,false);
        			else
        				ai.update(guess,true);

        			status = temp.getStatus();
        			obj.setState(temp.getState());
        			obj.setGuesses(temp.getGuesses());
        			obj.setStatus(temp.getStatus());
				}
			}

			System.out.println("Prisoner is "+obj.getStatus());
    	}

	}
}
