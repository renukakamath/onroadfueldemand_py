package com.example.onroadfuel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Driver_update_stock extends AppCompatActivity implements JsonResponse {

    TextView t1;
    EditText e1;
    Button b1;
    String stock_id,stock;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_update_stock);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t1=(TextView)findViewById(R.id.tvstock);
        e1=(EditText)findViewById(R.id.etstock);
        b1=(Button)findViewById(R.id.btstock);

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Driver_update_stock.this;
        String q = "/driver_view_stock?vehicle_id="+Driver_view_assinged_vehicle.vehicle_ids;
        q=q.replace(" ","%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stock=e1.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) Driver_update_stock.this;
                String q = "/driver_update_stock?vehicle_id="+Driver_view_assinged_vehicle.vehicle_ids+"&stock="+stock;
                q=q.replace(" ","%20");
                JR.execute(q);
            }
        });
    }

    @Override
    public void response(JSONObject jo) {

        try {
            String method=jo.getString("method");
            Log.d("pearl",method);
            //Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();

            if(method.equalsIgnoreCase("driver_view_stock")) {
                String status = jo.getString("status");
                Log.d("pearl", status);
                //Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();

                if (status.equalsIgnoreCase("success")) {
//                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                   t1.setText(jo.getString("stock"));
                    stock = jo.getString("stock");

//                    SharedPreferences.Editor e = sh.edit();
//                    e.putString("log_id", logid);
//                    e.commit();



                } else {
//                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
//				Intent i=new Intent(getApplicationContext(),MainLogin.class);
                    startActivity(new Intent(getApplicationContext(), Driver_home.class));
                }

            }

            if(method.equalsIgnoreCase("driver_update_stock"))
            {
                String status=jo.getString("status");
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
                if(status.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getApplicationContext(),"Update Successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Driver_view_assinged_vehicle.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Update Failed", Toast.LENGTH_LONG).show();
                }
            }

        }catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}