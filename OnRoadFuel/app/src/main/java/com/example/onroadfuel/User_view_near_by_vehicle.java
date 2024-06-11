package com.example.onroadfuel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_view_near_by_vehicle extends AppCompatActivity implements JsonResponse,OnItemClickListener {

    ListView lv1;
    String [] vehicle_id,driver_id,assign_id,type_id,fuel_type,rate,vehicle,regnum,dname,phone,email,latitude,longitude,val;
    public static String vehicle_ids,driver_ids,type_ids,assign_ids,lts,lgs,fuel_types,rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_near_by_vehicle);


        lv1=(ListView)findViewById(R.id.lvnear);
        lv1.setOnItemClickListener(this);

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) User_view_near_by_vehicle.this;
        String q = "/user_view_nearest_vehicle?lati="+LocationService.lati+"&logi="+LocationService.logi;
        q=q.replace(" ","%20");
        JR.execute(q);
    }




    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("user_view_nearest_vehicle")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
                if(status.equalsIgnoreCase("success")){

                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    vehicle_id=new String[ja1.length()];
                    driver_id=new String[ja1.length()];
                    assign_id=new String[ja1.length()];
                    type_id=new String[ja1.length()];
                    fuel_type=new String[ja1.length()];
                    rate=new String[ja1.length()];
                    vehicle=new String[ja1.length()];
                    regnum=new String[ja1.length()];
                    dname=new String[ja1.length()];
                    phone=new String[ja1.length()];
                    email=new String[ja1.length()];
                    latitude=new String[ja1.length()];
                    longitude=new String[ja1.length()];

                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {

                        vehicle_id[i]=ja1.getJSONObject(i).getString("vehicle_id");
                        driver_id[i]=ja1.getJSONObject(i).getString("driver_id");
                        assign_id[i]=ja1.getJSONObject(i).getString("assign_id");
                        type_id[i]=ja1.getJSONObject(i).getString("type_id");
                        fuel_type[i]=ja1.getJSONObject(i).getString("type_name");
                        rate[i]=ja1.getJSONObject(i).getString("rate");
                        vehicle[i]=ja1.getJSONObject(i).getString("vehicle");
                        regnum[i]=ja1.getJSONObject(i).getString("regnum");
                        dname[i]=ja1.getJSONObject(i).getString("dname");
                        phone[i]=ja1.getJSONObject(i).getString("phone");
                        email[i]=ja1.getJSONObject(i).getString("email");
                        latitude[i]=ja1.getJSONObject(i).getString("latitude");
                        longitude[i]=ja1.getJSONObject(i).getString("longitude");

//                        Toast.makeText(getApplicationContext(),val[i], Toast.LENGTH_SHORT).show();
                        val[i]="Fuel Type : "+fuel_type[i]+"\nVehicle : "+vehicle[i]+"\nReg.No : "+regnum[i]+"\nDriver Name : "+dname[i]+"\nPhone : "+phone[i]+"\nEmail : "+email[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,val);
                    lv1.setAdapter(ar);



                }

                else {
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();

                }
            }

//            if(method.equalsIgnoreCase("user_book"))
//            {
//                String status=jo.getString("status");
//                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
//                if(status.equalsIgnoreCase("success"))
//                {
//                    Toast.makeText(getApplicationContext(),"Booking Successfully!", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"Booking Failed", Toast.LENGTH_LONG).show();
//                }
//            }

        }catch (Exception e)
        {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        vehicle_ids=vehicle_id[arg2];
        driver_ids=driver_id[arg2];
        assign_ids=assign_id[arg2];
        type_ids=type_id[arg2];
        fuel_types=fuel_type[arg2];
        rates=rate[arg2];
        lts=latitude[arg2];
        lgs=longitude[arg2];

        final CharSequence[] items = {"View Map","Request","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(User_view_near_by_vehicle.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("View Map"))
                {
                    String url = "http://www.google.com/maps?saddr="+LocationService.lati+""+","+LocationService.logi+""+"&&daddr="+User_view_near_by_vehicle.lts+","+User_view_near_by_vehicle.lgs;
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(in);
                }

                else if (items[item].equals("Request"))
                {

//                    Toast.makeText(getApplicationContext(),"Request", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),User_send_fuel_request.class));
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
        Intent b=new Intent(getApplicationContext(),Users_home.class);
        startActivity(b);
    }




}
