package wikdd.kasapp;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import wikdd.kasapp.helper.Config;
import wikdd.kasapp.helper.CurrentDate;
import wikdd.kasapp.helper.SqliteHelper;

import static wikdd.kasapp.helper.CurrentDate.month;
import static wikdd.kasapp.helper.CurrentDate.year;

public class EditActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_masuk, radio_keluar;
    EditText edit_jumlah, edit_keterangan, edit_tanggal;
    Button btn_simpan;
    RippleView ripSimpan;

    String status, tanggal;
    SqliteHelper sqliteHelper;
    Cursor cursor;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        sqliteHelper = new SqliteHelper(this); // menjalankan sqlite

        radio_status    = findViewById(R.id.radio_status);
        radio_masuk     = findViewById(R.id.radio_masuk);
        radio_keluar    = findViewById(R.id.radio_keluar);
        edit_jumlah     = findViewById(R.id.edit_jumlah);
        edit_keterangan = findViewById(R.id.edit_keterangan);
        edit_tanggal    = findViewById(R.id.edit_tanggal);
        btn_simpan      = findViewById(R.id.btn_simpan);
        ripSimpan       = findViewById(R.id.ripSimpan);

        _editMysql();

        /*SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT *, strftime('%d/%m/%Y', tanggal) AS tanggal FROM transaksi WHERE transaksi_id='"+ MainActivity.transaksi_id+"'",null);

        cursor.moveToFirst();
        status = cursor.getString(1);

        switch (status){
            case "masuk": radio_masuk.setChecked(true);
                break;
            case "keluar": radio_keluar.setChecked(true);
                break;
        }

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.radio_masuk: status = "masuk";
                        break;
                    case R.id.radio_keluar: status = "keluar";
                        break;
                }
            }
        });

        edit_jumlah.setText(cursor.getString(2));
        edit_keterangan.setText(cursor.getString(3));
        tanggal = cursor.getString(4);
        edit_tanggal.setText(cursor.getString(5));
        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");
                        tanggal = year +"-" + numberFormat.format(month+1)+"-"+ numberFormat.format(dayOfmonth);
                        Log.e("_tanggal",tanggal);
                        edit_tanggal.setText(numberFormat.format(dayOfmonth)+ "/"+numberFormat.format((month+1))+"/"+year);
                    }
                }, year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });

        ripSimpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(status.equals("")){
                    Toast.makeText(getApplicationContext(), "Status data harus diisi ", Toast.LENGTH_LONG).show();
                    radio_status.requestFocus();
                }else if (edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Jumlah angka harus diisi ", Toast.LENGTH_LONG).show();
                    edit_jumlah.requestFocus();
                }else if(edit_keterangan.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "keterangan harus diisi ", Toast.LENGTH_LONG).show();
                }else
                {
                    simpanEdit();
                    finish();
                }
            }
        });*/

        //Set title
        getSupportActionBar().setTitle("Edit");
        //Back Icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public  boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void _editSQLite(){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT *, strftime('%d/%m/%Y', tanggal) AS tanggal FROM transaksi WHERE transaksi_id='"+ MainActivity.transaksi_id+"'",null);

        cursor.moveToFirst();
        status = cursor.getString(1);

        switch (status){
            case "masuk": radio_masuk.setChecked(true);
                break;
            case "keluar": radio_keluar.setChecked(true);
                break;
        }

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.radio_masuk: status = "masuk";
                        break;
                    case R.id.radio_keluar: status = "keluar";
                        break;
                }
            }
        });

        edit_jumlah.setText(cursor.getString(2));
        edit_keterangan.setText(cursor.getString(3));
        tanggal = cursor.getString(4);
        edit_tanggal.setText(cursor.getString(5));
        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");
                        tanggal = year +"-" + numberFormat.format(month+1)+"-"+ numberFormat.format(dayOfmonth);
                        Log.e("_tanggal",tanggal);
                        edit_tanggal.setText(numberFormat.format(dayOfmonth)+ "/"+numberFormat.format((month+1))+"/"+year);
                    }
                }, year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });

        ripSimpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(status.equals("")){
                    Toast.makeText(getApplicationContext(), "Status data harus diisi ", Toast.LENGTH_LONG).show();
                    radio_status.requestFocus();
                }else if (edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Jumlah angka harus diisi ", Toast.LENGTH_LONG).show();
                    edit_jumlah.requestFocus();
                }else if(edit_keterangan.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "keterangan harus diisi ", Toast.LENGTH_LONG).show();
                }else
                {
                    simpanEdit();
                    finish();
                }
            }
        });
    }

    private void _editMysql(){

        status = MainActivity.status;

        switch (status){
            case "masuk": radio_masuk.setChecked(true);
                break;
            case "keluar": radio_keluar.setChecked(true);
                break;
        }

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.radio_masuk: status = "masuk";
                        break;
                    case R.id.radio_keluar: status = "keluar";
                        break;
                }
            }
        });

        edit_jumlah.setText(MainActivity.jumlah);
        edit_keterangan.setText(MainActivity.keterangan);
        tanggal = MainActivity.tanggal2;
        edit_tanggal.setText(MainActivity.tanggal);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");
                        tanggal = year +"-" + numberFormat.format(month+1)+"-"+ numberFormat.format(dayOfmonth);
                        Log.e("_tanggal",tanggal);
                        edit_tanggal.setText(numberFormat.format(dayOfmonth)+ "/"+numberFormat.format((month+1))+"/"+year);
                    }
                }, year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });

        ripSimpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(status.equals("")){
                    Toast.makeText(getApplicationContext(), "Status data harus diisi ", Toast.LENGTH_LONG).show();
                    radio_status.requestFocus();
                }else if (edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Jumlah angka harus diisi ", Toast.LENGTH_LONG).show();
                    edit_jumlah.requestFocus();
                }else if(edit_keterangan.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "keterangan harus diisi ", Toast.LENGTH_LONG).show();
                }else
                {
                    //simpanEdit();
                    AndroidNetworking.post(Config.HOST + "update.php")
                            .addBodyParameter("transaksi_id", MainActivity.transaksi_id)
                            .addBodyParameter("status", status)
                            .addBodyParameter("jumlah", edit_jumlah.getText().toString())
                            .addBodyParameter("keterangan", edit_keterangan.getText().toString())
                            .addBodyParameter("tanggal", tanggal)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    try{
                                        if(response.getString("response").equals("success")){
                                            Toast.makeText(EditActivity.this , "Perubahan data berhasil disimpan ", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else
                                        {
                                            Toast.makeText(EditActivity.this , response.getString("response"), Toast.LENGTH_LONG).show();
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });
                }
            }
        });
    }


    private void simpanEdit(){

        String varJumlah =  edit_jumlah.getText().toString();
        String varKeterangan = edit_keterangan.getText().toString();
        SQLiteDatabase database =  sqliteHelper.getWritableDatabase();
        database.execSQL("UPDATE transaksi SET status='"+status+"', jumlah='"+varJumlah+"', keterangan='"+varKeterangan+"', tanggal='"+tanggal+"' WHERE transaksi_id='"+MainActivity.transaksi_id +"'");
        Toast.makeText(getApplicationContext(), "Transaksi perubahan berhasil disimpan ", Toast.LENGTH_LONG).show();
        finish();

    }
}
