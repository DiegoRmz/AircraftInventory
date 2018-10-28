package com.halo.aircraftinventory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.halo.aircraftinventory.helper.CheckNetworkStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
            preLogin();
        }else{
            Toast.makeText(this.getBaseContext(),"You need network for this, baka",Toast.LENGTH_LONG).show();
        }
    }

    public void preLogin(){
        String s = "{";

        EditText email = (EditText)findViewById(R.id.userText);
        EditText pswd  = (EditText)findViewById(R.id.password);

        String mail = email.getText().toString();
        String pssd = pswd.getText().toString();

        s+="\"email\":\""+mail+"\",\"pswd\":\""+pssd+"\"}";

        System.out.println(email);
        System.out.println(pssd);

        new LoginAsync().execute("http://192.168.1.76:3000/usr/login",s);
    }

    public void postLogin(String str){
        if(!str.equals("[]")){
            Toast.makeText(this.getBaseContext(),str,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AircraftMenu.class);
            startActivity(intent);
        }else{
            Toast.makeText(this.getBaseContext(),"If you don't have credentials, ask your boss",Toast.LENGTH_LONG).show();
        }
    }

    private class LoginAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            HttpURLConnection urlConnection = null;

            try{
                urlConnection = (HttpURLConnection) new URL(strings[0]).openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setDoOutput(true);

                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());


                dos.writeBytes(strings[1]);
                dos.flush();
                dos.close();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();

                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }

                System.out.println(data);

                if(!data.equals("[]")){
                    JSONArray jarr = new JSONArray(data);
                    JSONObject job = jarr.getJSONObject(0);
                    
                    data = "Welcome, "+job.get("userName").toString();
                }

            }catch (Exception e){
                e.printStackTrace();

                data = "[]";
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return data;
        }

        protected void onPostExecute(String s) {
            postLogin(s);
        }

    }
}
