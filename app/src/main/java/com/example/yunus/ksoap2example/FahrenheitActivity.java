package com.example.yunus.ksoap2example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mehdi.sakout.fancybuttons.FancyButton;

public class FahrenheitActivity extends AppCompatActivity {

    @InjectView(R.id.fahrenheitDeger) public EditText fahrenheit;
    @InjectView(R.id.celsiusDeger) public TextView celcius;
    @InjectView(R.id.btn_cevir) public FancyButton ceviriYap;
    @InjectView(R.id.digerGec) public TextView diger;
    public String fahrenheitRequest;
    public ProgressDialog dialog;
    String TAG = "Response";
    SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fahrenheit);
        ButterKnife.inject(this);

        diger.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FahrenheitActivity.this,CelsiusActivity.class));
            }
        });

        ceviriYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                fahrenheitRequest=fahrenheit.getText().toString();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) //arka planda açılan ayrı bit thread ile ilgili işlemler burada
                    //saf sanal, implement zorunlu
                    {
                        String SOAP_ACTION= "https://www.w3schools.com/xml/FahrenheitToCelsius";
                        String METHOD_NAME = "FahrenheitToCelsius";
                        String NAMESPACE = "https://www.w3schools.com/xml/";
                        String URL = "https://www.w3schools.com/xml/tempconvert.asmx";
                        try
                        {
                            //Talep oluşturuluyor
                            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                            //Parametre ve argüman geçişi
                            Request.addProperty("Fahrenheit", fahrenheitRequest);
                            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            soapEnvelope.dotNet = true;
                            soapEnvelope.setOutputSoapObject(Request);
                            //WSDL
                            HttpTransportSE transport = new HttpTransportSE(URL);
                            //Fonksiyon çağırımı
                            transport.call(SOAP_ACTION, soapEnvelope);
                            resultString = (SoapPrimitive) soapEnvelope.getResponse();
                            Log.i(TAG, "Result Fahrenheit: " + resultString);
                        }
                        catch (Exception ex)
                        {
                            Log.e(TAG, "Error: " + ex.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPreExecute() {  //asenkron başlamadan önce
                        super.onPreExecute();
                        dialog = new ProgressDialog(FahrenheitActivity.this);
                        dialog.setMessage("Çevriliyor...");
                        dialog.show();

                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        dialog.dismiss();
                        celcius.setText(resultString.toString());
                    }
                }.execute();
            }
        });

    }
}
