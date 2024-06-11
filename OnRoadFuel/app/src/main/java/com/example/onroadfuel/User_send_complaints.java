package com.example.onroadfuel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_send_complaints extends AppCompatActivity implements JsonResponse {

    Button b1;
    EditText e1;
    ListView l1;
    public static String complaints;
    public static String[] complaint_id,complaint,reply,date,value;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_complaints);
        e1=(EditText)findViewById(R.id.etcmp);
        l1=(ListView)findViewById(R.id.lvcmp);
        b1=(Button)findViewById(R.id.btcmp);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                complaints=e1.getText().toString();
                if(complaints.equalsIgnoreCase(""))
                {
                    e1.setError("No value for Complaints");
                    e1.setFocusable(true);
                }
                else{
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse) User_send_complaints.this;
                    String q = "/user_send_complaints?loginid="+Login.logid+"&complaints="+complaints;
                    q=q.replace(" ","%20");
                    JR.execute(q);
                }
            }
        });
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) User_send_complaints.this;
        String q = "/user_view_complaints?loginid="+Login.logid;
        q=q.replace(" ","%20");
        JR.execute(q);
    }


    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try{
            String method=jo.getString("method");
            if(method.equalsIgnoreCase("user_send_complaints")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                //Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
                if(status.equalsIgnoreCase("success")){

                    Toast.makeText(getApplicationContext(), " SENT", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),User_send_complaints.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong!Try Again.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Users_home.class));
                }
            }
            if(method.equalsIgnoreCase("user_view_complaints")){
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    complaint_id=new String[ja1.length()];
                    complaint=new String[ja1.length()];
                    reply=new String[ja1.length()];
                    date=new String[ja1.length()];
                    value=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        complaint_id[i]=ja1.getJSONObject(i).getString("complaint_id");
                        complaint[i]=ja1.getJSONObject(i).getString("complaint");
                        reply[i]=ja1.getJSONObject(i).getString("reply");
                        date[i]=ja1.getJSONObject(i).getString("date");
                        value[i]="Complaint:  "+complaint[i]+"\nReply : "+reply[i]+"\nDate:  "+date[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,value);
                    l1.setAdapter(ar);
                    //startActivity(new Intent(getApplicationContext(),User_Post_Disease.class));
                }

                else

                {
                    Toast.makeText(getApplicationContext(), "No Complaints!!", Toast.LENGTH_LONG).show();

                }
            }

        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(), Users_home.class);
        startActivity(b);
    }

}
