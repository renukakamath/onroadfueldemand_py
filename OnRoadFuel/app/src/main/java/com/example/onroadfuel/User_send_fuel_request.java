package com.example.onroadfuel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class User_send_fuel_request extends AppCompatActivity implements  JsonResponse {

    TextView t1,t2,t3;
    EditText e1;
    Button b1;
    String ftype,frate,no_titter,tot_rate;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_fuel_request);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t1=(TextView)findViewById(R.id.tvfuel);
        t2=(TextView)findViewById(R.id.tvrate);
        t3=(TextView)findViewById(R.id.tvtotal);

        t1.setText(User_view_near_by_vehicle.fuel_types);
        t2.setText(User_view_near_by_vehicle.rates);

        e1=(EditText)findViewById(R.id.etltr);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(e1.getText().toString().equalsIgnoreCase("")){
//                    e1.setText("0");
                }
                else if(e1.getText().toString().equalsIgnoreCase(".")){
                    e1.setText("0");
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (e1.getText().toString().length() >= 1) {
                    double totalrate = Integer.parseInt(t2.getText().toString()) * (Integer.parseInt(e1.getText().toString()));
                    t3.setText(String.valueOf(totalrate));
                } else
                    t3.setText("0");
            }
        });

        b1=(Button)findViewById(R.id.btrequest);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                no_titter=e1.getText().toString();
                tot_rate=t3.getText().toString();



                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) User_send_fuel_request.this;
                String q = "/user_send_fuel_request?login_id="+sh.getString("log_id","")+"&driver="+User_view_near_by_vehicle.driver_ids+"&type_id="+User_view_near_by_vehicle.type_ids+"&no_titter="+no_titter+"&tot_rate="+tot_rate;
                q=q.replace(" ","%20");
                JR.execute(q);
            }
        });

    }


    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {
            String status=jo.getString("status");
            Log.d("pearl",status);


            if(status.equalsIgnoreCase("success")){





                Toast.makeText(getApplicationContext(), "REQUEST SEND SUCCESS", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),Users_home.class));

            }

            else
            {
                startActivity(new Intent(getApplicationContext(),User_view_near_by_vehicle.class));

                Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),User_view_near_by_vehicle.class);
        startActivity(b);
    }


}

