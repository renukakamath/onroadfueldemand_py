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

public class User_view_request extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView lv1;
    String[] booking_id,driver,phone,vehicle,regnum,type_name,rate,nooflitter,amount,date,bstatus,val;
    SharedPreferences sh;
    public static String booking_ids,bstatuss,amounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_request);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        lv1 = (ListView) findViewById(R.id.lvrequest);
        lv1.setOnItemClickListener(this);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) User_view_request.this;
        String q = "/user_view_request?login_id=" +sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }


    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("user_view_request")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
                if(status.equalsIgnoreCase("success")){

                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    booking_id=new String[ja1.length()];
                    driver=new String[ja1.length()];
                    phone=new String[ja1.length()];
                    vehicle=new String[ja1.length()];
                    regnum=new String[ja1.length()];
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
                        driver[i]=ja1.getJSONObject(i).getString("driver");
                        phone[i]=ja1.getJSONObject(i).getString("phone");
                        vehicle[i]=ja1.getJSONObject(i).getString("vehicle");
                        regnum[i]=ja1.getJSONObject(i).getString("regnum");
                        type_name[i]=ja1.getJSONObject(i).getString("type_name");
                        rate[i]=ja1.getJSONObject(i).getString("rate");
                        nooflitter[i]=ja1.getJSONObject(i).getString("nooflitter");
                        amount[i]=ja1.getJSONObject(i).getString("amount");

                        date[i]=ja1.getJSONObject(i).getString("date_time");

                        bstatus[i]=ja1.getJSONObject(i).getString("status");

//                        Toast.makeText(getApplicationContext(),val[i], Toast.LENGTH_SHORT).show();
                        val[i]="Driver Name : "+driver[i]+"\nVehicle : "+vehicle[i]+"\nReg.No : "+regnum[i]+"\nType Name : "+type_name[i]+"\nRate : "+rate[i]+"\nNo.of Litter : "+nooflitter[i]+"\nTotal Amount : "+amount[i]+"\nDate : "+date[i]+"\nStatus : "+bstatus[i];


                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,val);
                    lv1.setAdapter(ar);



                }

                else {
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();

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
        bstatuss = bstatus[arg2];
        amounts=amount[arg2];

        if (bstatuss.equalsIgnoreCase("Accept")) {


            final CharSequence[] items = {"Payment", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(User_view_request.this);
            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Payment")) {

                    startActivity(new Intent(getApplicationContext(),User_make_payment.class));

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }

            });
            builder.show();
//	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //startActivityForResult(i, GALLERY_CODE);
        }

        if (bstatuss.equalsIgnoreCase("Payment Received")) {


            final CharSequence[] items = {"Review", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(User_view_request.this);
            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Review")) {

                        startActivity(new Intent(getApplicationContext(),User_review_rating.class));

                    } else if (items[item].equals("Cancel")) {
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
        Intent b=new Intent(getApplicationContext(),Users_home.class);
        startActivity(b);
    }




}
