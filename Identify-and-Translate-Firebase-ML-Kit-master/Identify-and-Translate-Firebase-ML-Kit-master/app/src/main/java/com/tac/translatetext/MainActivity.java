package com.tac.translatetext;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


public class MainActivity extends AppCompatActivity {


    private TextView mSourceLang;
    private EditText mSourcetext;
    private Button mTranslateBtn;
    private TextView mTranslatedText;
    private String sourceText;
    private Spinner spinnerentrada;
    private Spinner spinnersalida;
    private TextView salida;
    private String[] categorias = {"Español", "Inglés", "Alemán", "Japonés", "Ruso", "Chino", "Italiano"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSourceLang = findViewById(R.id.sourceLang);
        mSourcetext = findViewById(R.id.sourceText);
        mTranslateBtn = findViewById(R.id.translate);
        mTranslatedText = findViewById(R.id.translatedText);
        mTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyLanguage();
            }
        });
        spinnerentrada = findViewById(R.id.spinnerentrada);
        spinnersalida = findViewById(R.id.spinnersalida);
        salida = findViewById(R.id.salidadetectado);

        spinnerentrada.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R
                .layout.simple_spinner_dropdown_item, categorias));
        spinnersalida.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R
                .layout.simple_spinner_dropdown_item, categorias));

    }

    private void identifyLanguage() {
        sourceText = mSourcetext.getText().toString();

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        mSourceLang.setText("Detecting..");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("und")){
                    Toast.makeText(getApplicationContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();
                }
                else {
                    getLanguageCode(s);
                }
            }
        });
    }

    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "es":
                langCode = FirebaseTranslateLanguage.ES;
                mSourceLang.setText(categorias[0].toString());
                spinnerentrada.setSelection(0);
                break;
            case "en":
                langCode = FirebaseTranslateLanguage.EN;
                mSourceLang.setText(categorias[1].toString());
                spinnerentrada.setSelection(1);
                break;
            case "de":
                langCode = FirebaseTranslateLanguage.DE;
                mSourceLang.setText(categorias[2].toString());
                spinnerentrada.setSelection(2);
                break;
            case "ja":
                langCode = FirebaseTranslateLanguage.JA;
                mSourceLang.setText(categorias[3].toString());
                spinnerentrada.setSelection(3);
                break;
            case "ru":
                langCode = FirebaseTranslateLanguage.RU;
                mSourceLang.setText(categorias[4].toString());
                spinnerentrada.setSelection(4);
                break;
            case "zh":
                langCode = FirebaseTranslateLanguage.ZH;
                mSourceLang.setText(categorias[5].toString());
                spinnerentrada.setSelection(5);
                break;
            case "it":
                langCode = FirebaseTranslateLanguage.IT;
                mSourceLang.setText(categorias[6].toString());
                spinnerentrada.setSelection(6);
                break;
            default:
                    langCode = 0;
        }

        translateText(langCode);
    }

    private void translateText(int langCode) {
        int segundo=0;
        if(spinnersalida.getSelectedItem().toString().equals(categorias[0].toString())){
            segundo = FirebaseTranslateLanguage.ES;
            salida.setText(categorias[0].toString());
        }
        if(spinnersalida.getSelectedItem().toString().equals(categorias[1].toString())){
            segundo = FirebaseTranslateLanguage.EN;
            salida.setText(categorias[1].toString());
        }
        if(spinnersalida.getSelectedItem().toString().equals(categorias[2].toString())){
            segundo = FirebaseTranslateLanguage.DE;
            salida.setText(categorias[2].toString());
        }
        if(spinnersalida.getSelectedItem().toString().equals(categorias[3].toString())){
            segundo = FirebaseTranslateLanguage.JA;
            salida.setText(categorias[3].toString());
        }
        if(spinnersalida.getSelectedItem().toString().equals(categorias[4].toString())){
            segundo = FirebaseTranslateLanguage.RU;
            salida.setText(categorias[4].toString());
        }
        if(spinnersalida.getSelectedItem().toString().equals(categorias[5].toString())){
            segundo = FirebaseTranslateLanguage.ZH;
            salida.setText(categorias[5].toString());
        }
        if(spinnersalida.getSelectedItem().toString().equals(categorias[6].toString())){
            segundo = FirebaseTranslateLanguage.IT;
            salida.setText(categorias[6].toString());
        }
        mTranslatedText.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(langCode)
                // to language
                .setTargetLanguage(segundo)
                .build();

        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .build();


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mTranslatedText.setText(s);
                    }
                });
            }
        });
    }


}
