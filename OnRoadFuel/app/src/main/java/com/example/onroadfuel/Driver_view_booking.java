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

public class Driver_view_booking extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView lv1;
    String[] booking_id,vehicle_id,user_id,username,phone,lati,longi,type_name,rate,nooflitter,amount,date,bstatus,val;
    SharedPreferences sh;
    public static String booking_ids,vehicle_ids,lts,lgs,bstatuss,nooflitters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_booking);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        lv1 = (ListView) findViewById(R.id.lvrequest);
        lv1.setOnItemClickListener(this);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Driver_view_booking.this;
        String q = "/driver_view_request?login_id=" +sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("driver_view_request")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
                if(status.equalsIgnoreCase("success")){

                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    booking_id=new String[ja1.length()];
                    vehicle_id=new String[ja1.length()];
                    user_id=new String[ja1.length()];
                    phone=new String[ja1.length()];
                    username=new String[ja1.length()];
                    lati=new String[ja1.length()];
                    longi=new String[ja1.length()];
                    type_name=new String[ja1.length()];
                    rate=new String[ja1.length()];
                    nooflitter=new String[ja1.length()];
                    amount=new String[ja1.length()];
                    date=new String[ja1.length()];
                    bstatus=new String[ja1.length()];

                    val=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {

                        booking_id[i]=ja1.getJSONObject(i).getString("booking_id");
                        vehicle_id[i]=ja1.getJSONObject(i).getString("vehicle_id");
                        user_id[i]=ja1.getJSONObject(i).getString("user_id");
                        phone[i]=ja1.getJSONObject(i).getString("phone");
                        username[i]=ja1.getJSONObject(i).getString("username");
                        lati[i]=ja1.getJSONObject(i).getString("latitude");
                        longi[i]=ja1.getJSONObject(i).getString("longitude");
                        type_name[i]=ja1.getJSONObject(i).getString("type_name");
                        rate[i]=ja1.getJSONObject(i).getString("rate");
                        nooflitter[i]=ja1.getJSONObject(i).getString("nooflitter");
                        amount[i]=ja1.getJSONObject(i).getString("amount");
                        date[i]=ja1.getJSONObject(i).getString("date_time");

                        bstatus[i]=ja1.getJSONObject(i).getString("status");

//                        Toast.makeText(getApplicationContext(),val[i], Toast.LENGTH_SHORT).show();
                        val[i]="User Name : "+username[i]+"\nPhone : "+phone[i]+"\nType Name : "+type_name[i]+"\nRate : "+rate[i]+"\nNo.of Litter : "+nooflitter[i]+"\nTotal Amount : "+amount[i]+"\nDate : "+date[i]+"\nStatus : "+bstatus[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,val);
                    lv1.setAdapter(ar);



                }

                else {
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();

                }
            }
            if(method.equalsIgnoreCase("driver_accept_request"))
            {
                String status=jo.getString("status");
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
                if(status.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getApplicationContext(),"Accept Successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Driver_view_booking.class));
                }
                else{
                    Toast.makeText(getApplicationContext()," Failed", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Driver_view_booking.class));
                }
            }

            if(method.equalsIgnoreCase("driver_accept_payment"))
            {
                String status=jo.getString("status");
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
                if(status.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getApplicationContext(),"Accept Successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Driver_view_booking.class));
                }
                else{
                    Toast.makeText(getApplicationContext()," Failed", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),Driver_view_booking.class));
                }
            }



        }catch (Exception e)
        {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        booking_ids = booking_id[arg2];
        vehicle_ids=vehicle_id[arg2];
        bstatuss = bstatus[arg2];
        nooflitters=nooflitter[arg2];
        lts=lati[arg2];
        lgs=longi[arg2];

        if (bstatuss.equalsIgnoreCase("Pending")) {


            final CharSequence[] items = {"Accept","View Map", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Driver_view_booking.this);
            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Accept")) {

                        JsonReq JR = new JsonReq();
                        JR.json_response = (JsonResponse) Driver_view_booking.this;
                        String q = "/driver_accept_request?booking_ids=" +booking_ids;
                        q = q.replace(" ", "%20");
                        JR.execute(q);
                    }
                    else if (items[item].equals("View Map")) {

                        String url = "http://www.google.com/maps?saddr="+LocationService.lati+""+","+LocationService.logi+""+"&&daddr="+Driver_view_booking.lts+","+Driver_view_booking.lgs;
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(in);
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

        if (bstatuss.equalsIgnoreCase("Paid")) {


            final CharSequence[] items = {"Payment Received","Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Driver_view_booking.this);
            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Payment Received")) {

                        JsonReq JR = new JsonReq();
                        JR.json_response = (JsonResponse) Driver_view_booking.this;
                        String q = "/driver_accept_payment?booking_ids=" +booking_ids+"&vehicle_ids="+vehicle_ids+"&nooflitters="+nooflitters;
                        q = q.replace(" ", "%20");
                        JR.execute(q);
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

        if (bstatuss.equalsIgnoreCase("Payment Received")) {


            final CharSequence[] items = {"View Rating","Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Driver_view_booking.this);
            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("View Rating")) {

                        startActivity(new Intent(getApplicationContext(),Driver_view_ratings.class));
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

    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Driver_home.class);
        startActivity(b);
    }




}
