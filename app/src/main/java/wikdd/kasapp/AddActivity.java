package wikdd.kasapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import wikdd.kasapp.helper.Config;
import wikdd.kasapp.helper.SqliteHelper;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.radio_status) RadioGroup radio_status;
    @BindView(R.id.input_jumlah) EditText input_jumlah;
    @BindView(R.id.input_keterangan) EditText input_keterangan;
    @BindView(R.id.ripSimpan) RippleView ripSimpan;

    String status;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        status = "";
        sqliteHelper = new SqliteHelper(this); // menjalankan sqlite


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


        ripSimpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(status.equals("")){
                    Toast.makeText(getApplicationContext(), "Status data harus diisi ", Toast.LENGTH_LONG).show();
                    radio_status.requestFocus();
                }else if (input_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Jumlah angka harus diisi ", Toast.LENGTH_LONG).show();
                    input_jumlah.requestFocus();
                }else if(input_keterangan.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "keterangan harus diisi ", Toast.LENGTH_LONG).show();
                }else
                {
                    /*_createSQLite();*/
                    _createMysql();

                    /*Toast.makeText(getApplicationContext(), "tes", Toast.LENGTH_LONG).show();*/
                }

            }
        });

        getSupportActionBar().setTitle("Tambah");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void _createSQLite(){

        String varJumlah =  input_jumlah.getText().toString();
        String varKeterangan = input_keterangan.getText().toString();
        SQLiteDatabase database =  sqliteHelper.getWritableDatabase();
        database.execSQL("INSERT INTO transaksi (status, jumlah, keterangan) VALUES ('"+ status +"','"+ varJumlah+"','"+varKeterangan+"')");
        Toast.makeText(getApplicationContext(), "Transaksi berhasil disimpan ", Toast.LENGTH_LONG).show();
        finish();

    }


    private void _createMysql(){
        String varJumlah =  input_jumlah.getText().toString();
        String varKeterangan = input_keterangan.getText().toString();

        AndroidNetworking.post(Config.HOST + "create.php")
                .addBodyParameter("status", status)
                .addBodyParameter("jumlah", varJumlah)
                .addBodyParameter("keterangan", varKeterangan)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try{
                            if(response.getString("response").equals("success")){
                                Toast.makeText(AddActivity.this , "Transaksi berhasil disimpan ", Toast.LENGTH_LONG).show();
                                finish();
                            }else
                            {
                                Toast.makeText(AddActivity.this , response.getString("response"), Toast.LENGTH_LONG).show();
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
