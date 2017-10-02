package com.regula.documentreader.demo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.regula.documentreader.api.DocumentReader;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class SettingsActivity extends Activity {
    private RadioGroup camerasGroup;
    private TextView horizontalAngleTv,verticalAngleTv;
    private SharedPreferences prefs;
    private HashMap<Integer,String> camerasHorAngle, camerasVerAngle;
    private CheckBox mrzCb, ocrCb, barcodeCb, documentTypeCb, locationCb, authenticityCb, imageQaCb,
    doLoggingCb;

    View.OnClickListener rbListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            horizontalAngleTv.setText(String.format(getString(R.string.camera_hor_angle), camerasHorAngle.get(v.getId())));
            verticalAngleTv.setText(String.format(getString(R.string.camera_ver_angle), camerasVerAngle.get(v.getId())));

            prefs.edit().putInt(MainActivity.SELECTED_CAMERA_ID, v.getId()).apply();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);

        camerasGroup = (RadioGroup) findViewById(R.id.camerasRG);
        horizontalAngleTv = (TextView) findViewById(R.id.horizontalAngleTv);
        verticalAngleTv = (TextView) findViewById(R.id.verticalAngleTv);

        camerasHorAngle = new HashMap<>();
        camerasVerAngle = new HashMap<>();

        mrzCb = (CheckBox) findViewById(R.id.mrzCb);
        ocrCb = (CheckBox) findViewById(R.id.ocrCb);
        barcodeCb = (CheckBox) findViewById(R.id.barcodeCb);
        documentTypeCb = (CheckBox) findViewById(R.id.docTypeCb);
        locationCb = (CheckBox) findViewById(R.id.locateCb);
        authenticityCb = (CheckBox) findViewById(R.id.authenticityCb);
        imageQaCb = (CheckBox) findViewById(R.id.imageQaCb);
        doLoggingCb = (CheckBox) findViewById(R.id.doLogCb);
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetCameraList();

        mrzCb.setChecked(DocumentReader.Instance().processParams.mrz);
        mrzCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.mrz = b;
            }
        });

        ocrCb.setChecked(DocumentReader.Instance().processParams.ocr);
        ocrCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.ocr = b;
            }
        });

        barcodeCb.setChecked(DocumentReader.Instance().processParams.barcode);
        barcodeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.barcode = b;
            }
        });

        documentTypeCb.setChecked(DocumentReader.Instance().processParams.doctype);
        documentTypeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.doctype = b;
            }
        });

        locationCb.setChecked(DocumentReader.Instance().processParams.locate);
        locationCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.locate = b;
            }
        });

        authenticityCb.setChecked(DocumentReader.Instance().processParams.authenticity);
        authenticityCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.authenticity = b;
            }
        });

        doLoggingCb.setChecked(DocumentReader.Instance().processParams.imageQA);
        doLoggingCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.logs = b;
            }
        });

        imageQaCb.setChecked(DocumentReader.Instance().processParams.imageQA);
        imageQaCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DocumentReader.Instance().processParams.imageQA = b;
            }
        });
    }

    private void GetCameraList() {
        camerasGroup.removeAllViews();
        int selectedCamera = prefs.getInt(MainActivity.SELECTED_CAMERA_ID, -1);
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Camera camera = Camera.open(i);
                Camera.Parameters parameters = camera.getParameters();
                camera.release();

                String horAngle = String.valueOf(parameters.getHorizontalViewAngle());
                String verAngle = String.valueOf(parameters.getVerticalViewAngle());

                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(i);
                rdbtn.setText(getString(R.string.camera) + " " + rdbtn.getId());
                rdbtn.setOnClickListener(rbListener);

                if (i == selectedCamera) {
                    rdbtn.setChecked(true);

                    horizontalAngleTv.setText(String.format(getString(R.string.camera_hor_angle), horAngle));
                    verticalAngleTv.setText(String.format(getString(R.string.camera_ver_angle), verAngle));
                }

                camerasGroup.addView(rdbtn);

                camerasVerAngle.put(i, verAngle);
                camerasHorAngle.put(i, horAngle);
            }
        }

        if(camerasGroup.getChildCount()==1 || selectedCamera==-1){
            RadioButton button = (RadioButton) camerasGroup.getChildAt(0);
            button.performClick();
        }
    }
}
