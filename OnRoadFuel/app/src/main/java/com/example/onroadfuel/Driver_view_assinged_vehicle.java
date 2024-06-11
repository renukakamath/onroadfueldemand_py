package com.example.onroadfuel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Driver_view_assinged_vehicle extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView lv1;
    String[] vehicle_id, assign_id, vehicle, regnum, capacity,stock, val;
    public static String vehicle_ids;

    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_assinged_vehicle);
        lv1 = (ListView) findViewById(R.id.lvveh);
        lv1.setOnItemClickListener(this);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Driver_view_assinged_vehicle.this;
        String q = "/driver_view_assigned_vehicle?login_id="+sh.getString("log_id","") ;
        q = q.replace(" ", "%20");
        JR.execute(q);
    }



    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {

            String method = jo.getString("method");
            if (method.equalsIgnoreCase("driver_view_assigned_vehicle")) {
                String status = jo.getString("status");
                Log.d("pearl", status);
                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                if (status.equalsIgnoreCase("success")) {

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    vehicle_id = new String[ja1.length()];
                    assign_id = new String[ja1.length()];
                    vehicle = new String[ja1.length()];
                    regnum = new String[ja1.length()];
                    capacity = new String[ja1.length()];
                    stock= new String[ja1.length()];
                    val = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {

                        vehicle_id[i] = ja1.getJSONObject(i).getString("vehicle_id");
                        assign_id[i] = ja1.getJSONObject(i).getString("assign_id");
                        vehicle[i] = ja1.getJSONObject(i).getString("vehicle");
                        regnum[i] = ja1.getJSONObject(i).getString("regnum");
                        capacity[i] = ja1.getJSONObject(i).getString("capacity");
                        stock[i] = ja1.getJSONObject(i).getString("stock");

                        Toast.makeText(getApplicationContext(), val[i], Toast.LENGTH_SHORT).show();
                        val[i] = "Vehicle : " + vehicle[i] + "\nRegistration Number : " + regnum[i] + "\nCapacity : " + capacity[i]+ "\nAvailable Stock : " + stock[i];


                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, val);
                    lv1.setAdapter(ar);

//                    Custimage clist=new Custimage(this,image1,image2,brand,model,colour,no_seat,description);
//                    lv1.setAdapter(clist);


                } else {
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();

                }
            }


        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        vehicle_ids=vehicle_id[arg2];

        final CharSequence[] items = {"Update Stock","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Driver_view_assinged_vehicle.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Update Stock"))
                {

//                    Toast.makeText(getApplicationContext(),"Request", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Driver_update_stock.class));
                }

                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
//	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, GALLERY_CODE);
    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Driver_home.class);
        startActivity(b);
    }




}

