package com.halo.aircraftinventory;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.halo.aircraftinventory.helper.CheckNetworkStatus;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class InsertAircraft extends AppCompatActivity {
    private AircraftDatabaseController fishDatabaseController;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_aircraft);


        fishDatabaseController = new AircraftDatabaseController(this.getBaseContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fishDatabaseController.close();


    }

    public void saveClicked(View view){
        Aircraft aircraft = new Aircraft();
        String msg = "Aircraft created correctly";

        EditText _model =(EditText)findViewById(R.id.model);
        aircraft.setModel(""+_model.getText());
        EditText _producer = (EditText)findViewById(R.id.producer);
        aircraft.setProducer(""+_producer.getText());
        EditText _status = (EditText)findViewById(R.id.status);
        aircraft.setCurrentStatus(""+_status.getText());
        EditText _pilot = (EditText)findViewById(R.id.pilot);
        aircraft.setPilot(""+_pilot.getText());

        if(this.fishDatabaseController.insert(aircraft) < 0){
            msg = "Error creating the aircraft";
        }

        Toast.makeText(this.getBaseContext(),msg,Toast.LENGTH_LONG).show();
    }

    public void callBackup(View view){
        if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
            prepareBackup();
        }else{
            Toast.makeText(this.getBaseContext(),"You need network for this, baka",Toast.LENGTH_LONG).show();
        }
    }

    private void prepareBackup(){
        ArrayList<ArrayList<String>> listOfLists = new ArrayList<>();
        Cursor cursor = fishDatabaseController.selectAircraft("",null);

        String s = "[";
        while (cursor.moveToNext()){
            ArrayList<String> lst = new ArrayList<>();

            int id = cursor.getInt(cursor.getColumnIndex(Aircraft.COLUMN_ID));
            String producer = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_PRODUCER));
            String model = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_MODEL));
            String status = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_STATUS));
            String pilot = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_PILOT));

            ArrayList<String> list = new ArrayList<>();


            s+="[\""+id+"\",\""+producer+"\",\""+model+"\",\""+status+"\",\""+pilot+"\"],";
        }

        s = s.substring(0,s.length()-1);
        s+="]";

        Log.d("Cnt", s);

         new BackupAsyncTask().execute("http://192.168.1.76:3000/aircraft/create-many",s);
    }

    public void afterExecute(String data){
        Toast.makeText(this.getBaseContext(),data,Toast.LENGTH_LONG).show();
    }

    private class BackupAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(InsertAircraft.this);
            pDialog.setMessage("Backing up full database. This could take a while");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";

            HttpURLConnection urlConnection = null;

            int statusCode = 0;

            try{
                urlConnection = (HttpURLConnection) new URL(strings[0]).openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setDoOutput(true);

                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());


                dos.writeBytes(strings[1]);
                dos.flush();
                dos.close();

               statusCode = urlConnection.getResponseCode();

                Log.d("Status code",""+statusCode);

                InputStream in = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();

                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }


                System.out.println(data);

                data = "Records stored correctly";
            }catch (Exception e){
                e.printStackTrace();

                data = "HTTP Status code: "+statusCode;
            }finally {
                pDialog.dismiss();

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            afterExecute(s);
        }
    }
}
