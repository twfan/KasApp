package wikdd.kasapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import wikdd.kasapp.helper.Config;
import wikdd.kasapp.helper.SqliteHelper;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipe_refresh;
    ListView list_kas;
    ArrayList <HashMap<String, String>> aruskas;

    RippleView ripEdit, ripHapus;

    public static String transaksi_id, tgl_dari, tgl_ke; //biar bisa di baca di activity lain

    SqliteHelper sqliteHelper;
    Cursor cursor;
    TextView txt_masuk, txt_keluar, txt_total;

    public static  boolean filter;
    String query_kas, query_total;
    public static TextView text_filter;


    //untuk edit
    public static String link, status, keterangan, jumlah, tanggal, tanggal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("contoh","misal");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//                    INI MENGGUNAKAN INTRO DULU SETELAH SPLASHSCREEN


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "RepstartActivity(new Intent(MainActivity.this, IntroActivity.class));lace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
//                startActivity(new Intent(MainActivity.this, AddActivity.class));
                startActivity(new Intent(MainActivity.this, TabActivity.class));
            }
        });

        swipe_refresh   = findViewById(R.id.swipe_refresh);
        list_kas        = findViewById(R.id.list_kas);
        txt_masuk       = findViewById(R.id.txt_masuk);
        txt_keluar      = findViewById(R.id.txt_keluar);
        txt_total       = findViewById(R.id.txt_total);
        text_filter     = findViewById(R.id.text_filter);
        aruskas         = new ArrayList<>();

        sqliteHelper    = new SqliteHelper(this);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query_kas = "SELECT *, strftime('%d/%m/%Y', tanggal) AS tanggal FROM transaksi ORDER BY transaksi_id DESC";
                query_total = "SELECT SUM(jumlah) AS total," + " (SELECT SUM(jumlah) FROM transaksi WHERE status='masuk') AS masuk," + " (SELECT SUM(jumlah) FROM transaksi WHERE status='keluar') AS keluar FROM transaksi";
                /*kasAdapter();*/
                link =  Config.HOST +"read.php";
                _readMYSQL();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        query_kas = "SELECT *, strftime('%d/%m/%Y', tanggal) AS tanggal FROM transaksi ORDER BY transaksi_id DESC";
        query_total = "SELECT SUM(jumlah) AS total,"
                + " (SELECT SUM(jumlah) FROM transaksi WHERE status='masuk') AS masuk,"
                + " (SELECT SUM(jumlah) FROM transaksi WHERE status='keluar') AS keluar FROM transaksi";
        if(filter){
            query_kas = "SELECT *, strftime('%d/%m/%Y', tanggal) AS tanggal FROM transaksi WHERE(tanggal>='"+tgl_dari+"') AND (tanggal<='"+tgl_ke+"') ORDER BY transaksi_id DESC";
            query_total =
                    "SELECT SUM(jumlah) AS total,"
                    + " (SELECT SUM(jumlah) FROM transaksi WHERE status='masuk' AND (tanggal>='"+tgl_dari+"') AND (tanggal<='"+tgl_ke+"')) AS masuk,"
                    + " (SELECT SUM(jumlah) FROM transaksi WHERE status='keluar' AND (tanggal>='"+tgl_dari+"') AND (tanggal<='"+tgl_ke+"')) AS keluar "
                            +"FROM transaksi"+
            " WHERE(tanggal>='"+tgl_dari+"') AND (tanggal<='"+tgl_ke+"')";
            link =  Config.HOST +"filter.php?dari=" +tgl_dari + "&ke=" +tgl_ke;
        }else{
            link =  Config.HOST +"read.php";
        }



        String str_filter = Boolean.toString(filter);
        Log.d("_FILTER", str_filter);

        /*kasAdapter();*/
        _readMYSQL();
    }

    private void _readMYSQL(){
        swipe_refresh.setRefreshing(false);
        aruskas.clear();
        list_kas.setAdapter(null);
        Log.d("_LINK", link);
        AndroidNetworking.post(link)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try{
                            NumberFormat rupiah = NumberFormat.getInstance(Locale.GERMANY);

                            txt_masuk.setText("Rp." + rupiah.format(response.getDouble("masuk")));
                            txt_keluar.setText( "Rp." + rupiah.format(response.getDouble("keluar")));
                            txt_total.setText( "Rp." + rupiah.format(response.getDouble("masuk") - response.getDouble("keluar")));

                            JSONArray jsonArray = response.getJSONArray("hasil");
                            for(int i=0;i< jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("transaksi_id", jsonObject.getString("transaksi_id"));
                                map.put("status",       jsonObject.getString("status"));
                                map.put("jumlah",       jsonObject.getString("jumlah"));
                                map.put("keterangan",   jsonObject.getString("keterangan"));
                                map.put("tanggal",      jsonObject.getString("tanggal"));
                                map.put("tanggal2",      jsonObject.getString("tanggal2"));
                                aruskas.add(map);
                            }
                            _readAdapter();


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

    private void _deleteMYSQL(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Yakin untuk menghapus data ini?");

        //POSITIVE BUTTON
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AndroidNetworking.post(Config.HOST + "delete.php")
                        .addBodyParameter("transaksi_id", transaksi_id)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                try{
                                    if(response.getString("response").equals("success")){
                                        Toast.makeText(MainActivity.this,"Data berhasil dihapus",Toast.LENGTH_LONG).show();
                                        _readMYSQL();
                                    }else
                                    {
                                        Toast.makeText(MainActivity.this , response.getString("response"), Toast.LENGTH_LONG).show();
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
        });
        //NEGATIVE BUTTON
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void _readAdapter(){

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_kas,
                new String[]{"transaksi_id","status","jumlah","keterangan","tanggal","tanggal2"},
                new int[]{R.id.txt_transaksi_id, R.id.txt_status, R.id.txt_jumlah, R.id.txt_keterangan, R.id.txt_tanggal, R.id.txt_tanggal2}
        );
        list_kas.setAdapter(simpleAdapter);

        list_kas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                transaksi_id    = ((TextView)view.findViewById(R.id.txt_transaksi_id)).getText().toString();
                status          = ((TextView)view.findViewById(R.id.txt_status)).getText().toString();
                jumlah          = ((TextView)view.findViewById(R.id.txt_jumlah)).getText().toString();
                keterangan      = ((TextView)view.findViewById(R.id.txt_keterangan)).getText().toString();
                tanggal         = ((TextView)view.findViewById(R.id.txt_tanggal)).getText().toString();
                tanggal2        = ((TextView)view.findViewById(R.id.txt_tanggal2)).getText().toString();
                //Log.e("Transaksi_id", transaksi_id);
                ListMenu();
            }
        });
    }

    private void kasAdapter(){

        aruskas.clear();
        list_kas.setAdapter(null);
        swipe_refresh.setRefreshing(false);

        SQLiteDatabase  db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery(query_kas ,null);
        cursor.moveToFirst();

        for(int i=0;i< cursor.getCount();i++)
        {
            cursor.moveToPosition(i);

            HashMap<String, String> map = new HashMap<>();
            map.put("transaksi_id", cursor.getString(0));
            map.put("status", cursor.getString(1));
            map.put("jumlah", cursor.getString(2));
            map.put("keterangan", cursor.getString(3));
           // map.put("tanggal", cursor.getString(4)); DI GANTI karena tanggal menambahkan index AS TGL di query
            map.put("tanggal", cursor.getString(5));
            aruskas.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_kas,
                new String[]{"transaksi_id","status","jumlah","keterangan","tanggal"},
                new int[]{R.id.txt_transaksi_id, R.id.txt_status, R.id.txt_jumlah, R.id.txt_keterangan, R.id.txt_tanggal}
        );
        list_kas.setAdapter(simpleAdapter);

        list_kas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                transaksi_id = ((TextView)view.findViewById(R.id.txt_transaksi_id)).getText().toString();
                Log.e("Transaksi_id", transaksi_id);
                ListMenu();
            }
        });

        kasTotal();
    }

    private void kasTotal(){
        NumberFormat rupiah = NumberFormat.getInstance(Locale.GERMANY);

        SQLiteDatabase  db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery( query_total , null);
        cursor.moveToFirst();
        txt_masuk.setText(rupiah.format(cursor.getDouble(1)));
        txt_keluar.setText(rupiah.format(cursor.getDouble(2)));
        txt_total.setText(rupiah.format(cursor.getDouble(1) - cursor.getDouble(2)));

        swipe_refresh.setRefreshing(false);
        if(!filter){
            text_filter.setVisibility(View.VISIBLE);
        }
        filter = false;
    }


    private void ListMenu(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        RippleView ripHapus = dialog.findViewById(R.id.ripHapus);
        RippleView ripEdit = dialog.findViewById(R.id.ripEdit);

        ripHapus.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                //Hapus();
                _deleteMYSQL();
            }
        });

        ripEdit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });

        dialog.show();
    }

    private void Hapus(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Yakin untuk menghapus data ini?");

        //POSITIVE BUTTON
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SQLiteDatabase db = sqliteHelper.getWritableDatabase();
                db.execSQL("DELETE FROM transaksi WHERE transaksi_id = '"+transaksi_id+"' ");
                Toast.makeText(getApplicationContext(),"Data berhasil dihapus",Toast.LENGTH_LONG).show();
                kasAdapter();
            }
        });

        //NEGATIVE BUTTON
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            //startActivity(new Intent(this, FilterActivity.class));
            _filterMysql();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void _filterMysql(){
        SmoothDateRangePickerFragment smoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(
                new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                    @Override
                    public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                               int yearStart, int monthStart,
                                               int dayStart, int yearEnd,
                                               int monthEnd, int dayEnd) {
                        // grab the date range, do what you want
                        tgl_dari = String.valueOf(yearStart) +"-" + String.valueOf(monthStart +1)+"-"+ String.valueOf(dayStart);
                        tgl_ke = String.valueOf(yearEnd) +"-" + String.valueOf(monthEnd +1)+"-"+ String.valueOf(dayEnd);

                        text_filter.setText(String.valueOf(dayStart) +"/" + String.valueOf(monthStart +1)+"/"+ String.valueOf(yearStart)+ " - " + String.valueOf(dayEnd) +"/" + String.valueOf(monthEnd +1)+"/"+ String.valueOf(yearEnd));
                        link =  Config.HOST +"filter.php?dari=" +tgl_dari + "&ke=" +tgl_ke;
                        _readMYSQL();
                        Log.d("_DATE", "tanggal dari = " +tgl_dari+" tanggal ke = "+tgl_ke);
                    }
                });

        smoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");
    }
}


