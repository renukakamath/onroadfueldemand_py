package com.example.onroadfuel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User_make_payment extends AppCompatActivity  implements JsonResponse {


    EditText t1,ed_card_num, ed_cvv, ed_exp_date, ed_amount;
    Button bt_pay;
    String amount, card_no, cvv, exp_date;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_make_payment);



        t1=(EditText)findViewById(R.id.ed_amount);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ed_card_num = findViewById(R.id.ed_card_num);
        ed_cvv = findViewById(R.id.ed_cvv);
        ed_exp_date = findViewById(R.id.ed_exp_date);
        ed_amount = findViewById(R.id.ed_amount);
        bt_pay = findViewById(R.id.bt_pay);

        ed_exp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();
            }
        });

        bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = ed_amount.getText().toString();
                card_no = ed_card_num.getText().toString();
                cvv = ed_cvv.getText().toString();
                exp_date = ed_exp_date.getText().toString();

                if (card_no.length() != 16) {
                    ed_card_num.setError("Valid card number");
                    ed_card_num.setFocusable(true);
                } else if (cvv.length() != 3) {
                    ed_cvv.setError("Valid cvv");
                    ed_cvv.setFocusable(true);
                } else if (exp_date.equalsIgnoreCase("")) {
                    ed_exp_date.setError("Expiry date");
                    ed_exp_date.setFocusable(true);
                } else {


                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) User_make_payment.this;
                    String q = "/user_payment?booking_id=" + User_view_request.booking_ids;
                    q = q.replace(" ", "%20");
                    JR.execute(q);
                }
            }
        });



//        ed_exp_date.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                exp_date = ed_exp_date.getText().toString();
//
////                String input = keyboard.next();
//                setMaxDate(System.currentTimeMillis());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
////                simpleDateFormat.setLenient(false);
//                Date expiry = (Date) simpleDateFormat.parse(exp_date);
//                Date date =  (Date) simpleDateFormat.parse(exp_date);
//                boolean expired = expiry.before(new Date());
//                if (expired == true)
//                {
//                    System.out.println("This card has already expired");
//                }
//
//
//               else if (exp_date.length() == 2 && !exp_date.contains("/")) {
//                    ed_exp_date.setText(exp_date + "/");
//                    ed_exp_date.setSelection(ed_exp_date.getText().length());
//                }
//            }
//        });



    }

    private int year, month, day;
    private void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
//        Toast.makeText(getApplicationContext(),"sdf : "+sdf,Toast.LENGTH_LONG).show();
        DatePickerDialog datePickerDialog = new DatePickerDialog(User_make_payment.this,
                new DatePickerDialog.OnDateSetListener() {



                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String tempDob = year + "-" + (monthOfYear + 1);
//                        try {
                        Toast.makeText(getApplicationContext(),"tempDo"+tempDob+"",Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"monthOfYear : "+year,Toast.LENGTH_LONG).show();


                            ed_exp_date.setText(tempDob);
//                        } catch (Exception e) {

//                        }
                    }
                }, year, month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub

        try {
            String status=jo.getString("status");
            if(status.equalsIgnoreCase("success"))
            {
                Toast.makeText(getApplicationContext()," Payment Success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),User_view_request.class));
            }
            else
            {
                Toast.makeText(getApplicationContext(), " failed",Toast.LENGTH_LONG ).show();
            }
        } catch (Exception e){
            // TODO: handle exception
        }

    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),User_view_request.class);
        startActivity(b);
    }
}