package wikdd.kasapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.library.RippleView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import wikdd.kasapp.helper.CurrentDate;

import static wikdd.kasapp.helper.CurrentDate.year;

public class FilterActivity extends AppCompatActivity {

    EditText edit_ke,edit_dari;
    RippleView rip_filter;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        edit_ke = findViewById(R.id.edit_ke);
        edit_dari = findViewById(R.id.edit_dari);
        rip_filter = findViewById(R.id.rip_filter);

        //memberikan event ke edit_dari
        edit_dari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");
                        MainActivity.tgl_dari = year +"-" + numberFormat.format(month+1)+"-"+ numberFormat.format(dayOfmonth);
                        Log.e("_tgl_dari",MainActivity.tgl_dari);//cek value
                        edit_dari.setText(numberFormat.format(dayOfmonth)+ "/"+numberFormat.format((month+1))+"/"+year);

                    }
                }, year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });

        //memberikan event ke edit_ke
        edit_ke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");
                        MainActivity.tgl_ke = year +"-" + numberFormat.format(month+1)+"-"+ numberFormat.format(dayOfmonth);
                        Log.e("_tgl_ke",MainActivity.tgl_ke);//cek value
                        edit_ke.setText(numberFormat.format(dayOfmonth)+ "/"+numberFormat.format((month+1))+"/"+year);

                    }
                }, year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });


        rip_filter.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(edit_dari.getText().toString().equals("")||edit_ke.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Isi data dengan benar",Toast.LENGTH_SHORT).show();
                }else {
                    MainActivity.filter = true;
                    MainActivity.text_filter.setText(edit_dari.getText().toString() + " " + edit_ke.getText().toString());
                    MainActivity.text_filter.setVisibility(View.VISIBLE);
                    finish();

                }
            }
        });

        getSupportActionBar().setTitle("Filter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
