package com.example.railenquiry.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.railenquiry.Adapters.RouteAdapter;
import com.example.railenquiry.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView trnname,trnnumber;
    EditText trnuser;
    String train="";
    String Name="",Number="";
    String nm="",nbr="";
    Button submit;
    RecyclerView recyclerView;
    String City="";
    RouteAdapter adapter;
    List<String> list=new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trnnumber=findViewById(R.id.trnnumber);
        trnname=findViewById(R.id.trnname);
        trnuser=findViewById(R.id.trnuser);
        recyclerView=findViewById(R.id.routerecycler);


        submit=findViewById(R.id.submit);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                train=trnuser.getText().toString();
                if (list !=null){
                    list.clear();
                }
                new Async().execute();



            }
        });



    }

    public void status(View view) {

        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);
    }


    public class Async extends AsyncTask<String,String,String>{
        String name,number;
        HttpURLConnection httpURLConnection;
        String json="";
        StringBuilder stringBuilder=new StringBuilder();
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {


            if(trnuser.getText()!=null){
                train.equals(trnuser.getText());
            }

            Log.d("train+ ",train);
//
//            trnupd.setEnabled(false);

            try {
                String url1="https://api.railwayapi.com/v2/name-number/train/"+train+"/apikey/aeto1a6riv/";
                URL url=new URL(url1);
                httpURLConnection= (HttpURLConnection) url.openConnection();

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

                jsonObject=new JSONObject(json);
                JSONObject jsonObject1=jsonObject.getJSONObject("train");

                name=jsonObject1.getString("name");

                number=jsonObject1.getString("number");




            } catch (JSONException e) {
                e.printStackTrace();
            }
            Name=name;
            Number=number;

            trnname.setText(Name);
            trnnumber.setText(Number);

            new Route().execute();


        }
    }
    public class Route extends AsyncTask<String,String,String>{

        String json="";
        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder=new StringBuilder();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {


            try {
                String url1="https://api.railwayapi.com/v2/route/train/"+Number+"/apikey/aeto1a6riv/";

                URL url=new URL(url1);
                httpURLConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();

                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String data="";
                while ((data=bufferedReader.readLine())!=null){
                    stringBuilder.append(data);
                }

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
                JSONObject jsonObject=new JSONObject(json);

                JSONArray jsonArray=jsonObject.getJSONArray("route");

                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i).getJSONObject("station");
                    City=jsonObject1.getString("name");
                    list.add(City);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            adapter=new RouteAdapter(list,getApplicationContext());
            recyclerView.setAdapter(adapter);


        }
    }


}
