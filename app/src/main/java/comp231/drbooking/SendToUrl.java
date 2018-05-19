package comp231.drbooking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendToUrl
{
    public String sendToUrl(String myUrl, String formData) throws IOException
    {
        String responseData = "";
        OutputStream connOutputStream = null;
        InputStream inputStream = null;
        HttpURLConnection conn=null;

        try {
            //alternate to below using HttpClient & HttpPost : https://stackoverflow.com/questions/4361601/reading-httppost-response?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

            URL url = new URL(myUrl);//alt+ent => surround w try/catch
            conn = (HttpURLConnection) url.openConnection();//alt+enter => add catch clause
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");//https://stackoverflow.com/questions/40574892/how-to-send-post-request-with-x-www-form-urlencoded-body?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.connect();

            //Write form-data to conn's request-body
            connOutputStream = conn.getOutputStream();
            OutputStreamWriter outputWriter = new OutputStreamWriter(connOutputStream, "UTF-8");
            BufferedWriter writer = new BufferedWriter(outputWriter);//for efficiency sake
            //
            writer.write(formData);//write to httpConn's req body
            writer.flush();
            writer.close();
            connOutputStream.close();
            //
            conn.connect();//POST req sent along w formData
            //
            //
            //
            inputStream = conn.getInputStream();//get response from server
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer sb = new StringBuffer();

            //read ea line one by one
            String line = "";
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            //aft all lines r read, close buffer
            responseData = sb.toString();
            br.close();

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //inputStream.close();//add 'throws IOException' at fn sign
            conn.disconnect();
        }
        return responseData;//JSON format string
    }
}
