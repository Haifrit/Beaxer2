package com.example.jwolter.beaxernovcs;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Der Rest Client stellt über HttpURLConnection die Verbindung mit dem Server her. Sie gibt einen
 * String zurück der verarbeitet werden kann.
 */
public class RestClient extends AsyncTask<String, Void, String> {

    JSONArray outputJSON;
    Context context;

    public RestClient(JSONArray outputJSON, Context context) {
        this.outputJSON = outputJSON;
        this.context = context;
    }

    @Override
            protected String doInBackground(String... params) {
                try {
                    String url = context.getResources().getString(R.string.connectionUrl);
                    URL object=new URL(url);
                    HttpURLConnection con = (HttpURLConnection) object.openConnection();
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type",context.getResources().getString(R.string.sub_path));
                    con.setRequestProperty("Accept", context.getResources().getString(R.string.sub_path));
                    con.setRequestMethod("POST");

                    DataOutputStream output = null;

                    /* Send the request data. */
                    output = new DataOutputStream(con.getOutputStream());

                    //Testen ob toString Methode funktioniert
                    output.writeBytes(outputJSON.toString());
                    output.flush();
                    output.close();

                    /* Get response data. */
                    StringBuilder sb = new StringBuilder();
                    int HttpResult = con.getResponseCode();
                    if(HttpResult == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        return sb.toString();
                    }else{
                        return "Error";
                    }

        } catch (Exception ex){
            return "Error";
        }
    }

}
