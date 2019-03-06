package wikdd.kasapp.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import wikdd.kasapp.R;
import wikdd.kasapp.helper.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeluarFragment extends Fragment {

    EditText input_jumlah, input_keterangan;
    RippleView ripSimpan;

    public KeluarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_masuk, container, false);
        input_jumlah = (EditText) view.findViewById(R.id.input_jumlah);
        input_keterangan = (EditText) view.findViewById(R.id.input_keterangan);
        ripSimpan = (RippleView) view.findViewById(R.id.ripSimpan);


        ripSimpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (input_jumlah.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Jumlah angka harus diisi ", Toast.LENGTH_LONG).show();
                    input_jumlah.requestFocus();
                }else if(input_keterangan.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "keterangan harus diisi ", Toast.LENGTH_LONG).show();
                }else
                {
                    /*_createSQLite();*/
                    _createMysql();

                    /*Toast.makeText(getApplicationContext(), "tes", Toast.LENGTH_LONG).show();*/
                }

            }
        });


        return view;
    }


    private void _createMysql(){
        String varJumlah =  input_jumlah.getText().toString();
        String varKeterangan = input_keterangan.getText().toString();

        AndroidNetworking.post(Config.HOST + "create.php")
                .addBodyParameter("status", "keluar")
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
                                Toast.makeText(getActivity() , "Transaksi berhasil disimpan ", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }else
                            {
                                Toast.makeText(getActivity(), response.getString("response"), Toast.LENGTH_LONG).show();
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
