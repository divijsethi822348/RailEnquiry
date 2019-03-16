package com.example.railenquiry.Activities;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.railenquiry.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {

    EditText etnum,etcd,etdate;
    Button change,check;
    String trnnum="",code="",date="";
    String position="";
    TextView place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        etnum=findViewById(R.id.etnum);
        etcd=findViewById(R.id.etcd);
        etdate=findViewById(R.id.etdate);
        change=findViewById(R.id.chdt);
        place=findViewById(R.id.position);
        etdate.setText(giveDate());

        check=findViewById(R.id.check_status);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day,month,year;

                final Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(Main2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(dayOfMonth<10 ){
                            if(month<10){
                                etdate.setText("0"+dayOfMonth+"-"+"0"+(month+1)+"-"+year);
                            }else{
                                etdate.setText("0"+dayOfMonth+"-"+(month+1)+"-"+year);
                            }

                        }else if(month<10){
                            if(dayOfMonth>10){
                                etdate.setText(dayOfMonth+"-"+"0"+(month+1)+"-"+year);
                            }else{
                                etdate.setText("0"+dayOfMonth+"-"+"0"+(month+1)+"-"+year);
                            }
                        }
                        else{
                            etdate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                        }


                    }
                } ,year,month,day);
                datePickerDialog.show();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trnnum=etnum.getText().toString();
                code=etcd.getText().toString();
                date=etdate.getText().toString();
                new Status().execute();
            }
        });






    }

    public String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(cal.getTime());
    }


    public class Status extends AsyncTask<String,String,String> {
        String json="";
        StringBuilder stringBuilder=new StringBuilder();
        JSONObject jsonObject;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... strings) {


            try {
                String url1="https://api.railwayapi.com/v2/live/train/"+trnnum+"/station/"+code+"/date/"+date+"/apikey/aeto1a6riv/";
                URL url=new URL(url1);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

                String data;
                while ((data=bufferedReader.readLine())!=null){
                    stringBuilder.append(data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            json=stringBuilder.toString();


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                jsonObject =new JSONObject(json);

                position=jsonObject.getString("position");



            } catch (JSONException e) {
                e.printStackTrace();
            }
            place.setText(position);


        }
    }



}
