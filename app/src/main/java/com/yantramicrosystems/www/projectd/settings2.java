package com.yantramicrosystems.www.projectd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yantramicrosystems.www.projectd.UserClass.Class_Keys;

import org.json.JSONObject;

import java.util.ArrayList;

public class settings2 extends AppCompatActivity {

    Class_Keys keys = null;

    TextView tvLegend;
    Spinner spGuage, spParameter;
    Button btSaveGuage;


    EditText etLabel, etRangeMin, etRangeMax, etAlarmMax, etAlarmMin;
    EditText etResetUnit,etRequiredUnit, etAddtionFactor, etMultiplicationFactor;

    final String Tag = "Log";


    final String EngineLoad = "Engine Load";
    final String EngineCoolantTemperature = "Engine Coolant Temperature";
    final String FuelPressure = "Fuel Pressure";
    final String EngineRPM = "Engine RPM";
    final String Speed = "Speed";
    final String IntakeAirTemperature = "Intake Air Temperature";
    final String RunTimesinceEngineStart = "Run Time Since Engine Start";
    final String EngineOilTemperature = "Engine Oil Temperature";
    final String FuelLevel = "Fuel Level";
    final String SoundLevel = "Sound";
    final String Acceleration = "Acceleration";
    final String LeanAngle = "Lean Angle";
    final String AmbientTemperature = "Ambient Temperature";
    final String ControlModuleVoltage = "Control Module Voltage";
    final String ThrottlePosition = "Throttle Position";

    final String PDRPM          = "PD-RPM";
    final String PDSupplyVolts  = "PD-Bat Volts";
    final String PDAmbTemp      = "PD-Amb temp";
    final String PDEngineTemp   = "PD-Engine temp";
    final String PDV1           = "PD-V1";
    final String PDV2           = "PD-V2";
    final String PDV3           = "PD-V3";


    final static String CUSTOM_PREFS = "CUSTOM_PREFS";

    String sG1CustomConfig,sG2CustomConfig,sG3CustomConfig,sG4CustomConfig,sG5CustomConfig,sG6CustomConfig,sG7CustomConfig,sG8CustomConfig,sG9CustomConfig;

    float fEngineLoad_Max,fEngineCoolantTemp_Max, fFuelPressure_Max, fEngineRPM_Max, fSpeed_Max;
    float fIntakeAirTemp_Max, fRunTimeSinceEngineStart_Max, fEngineOilTemp_Max, fFuelLevel_Max;

    String sRequiredUnit = null;
    
    int iRangeMin, iRangeMax, iAlarmMin, iAlarmMax;
    
    float fAdditionFactor = 1, fMultiplicationFactor = 0;
    
    int iSelectedGuage;

    SharedPreferences gaugeprefs;
    SharedPreferences.Editor editorCustomConfig;
    
    String sParameter,sRangeMin,sRangeMax,sAlarmMin,sAlarmMax,sDefaultUnit,sRequiredUnitonLoad,sAdditionalFactor,sMultiplicationFactor;
    String sLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        gaugeprefs = getSharedPreferences(settings2.CUSTOM_PREFS, MODE_PRIVATE);
        keys = new Class_Keys();

        viewsInitialization();

        populateGuagesSpinner();

        buttonClicks();

        populateParameterSpinner();

        loadGuageData();
        
        selectGuage();





    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGuageData();
    }

    public void viewsInitialization()
    {

        tvLegend = (TextView)findViewById(R.id.tvLegend);
        spGuage = (Spinner)findViewById(R.id.spSelectGuage);
        spParameter = (Spinner)findViewById(R.id.spSelectParameter);
        btSaveGuage = (Button)findViewById(R.id.btSaveGuage);

        etLabel = (EditText) findViewById(R.id.etLabel);
        etRangeMin = (EditText)findViewById(R.id.etRangeMin);
        etRangeMax = (EditText)findViewById(R.id.etRangeMax);
        etAlarmMax = (EditText)findViewById(R.id.etAlarmMax);
        etAlarmMin = (EditText)findViewById(R.id.etAlarmMin);
        etRequiredUnit = (EditText)findViewById(R.id.etRequiredUnit);
        etResetUnit = (EditText)findViewById(R.id.etDefaultUnit);
        etAddtionFactor = (EditText)findViewById(R.id.etAdditionFactor);
        etMultiplicationFactor = (EditText)findViewById(R.id.etMultiplicationFactor);


    }

    public void populateGuagesSpinner()
    {

        ArrayList<String> guagesArray = new ArrayList<String>();
        for(int i=1;i<10;i++)
        {
            guagesArray.add("Guage " + String.valueOf(i));
        }

        final ArrayAdapter<String> parameterArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, guagesArray);
        parameterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGuage.setAdapter(parameterArrayAdapter);
    }

    public void populateParameterSpinner()
    {

        ArrayList<String> parametersArray = new ArrayList<String>();

        parametersArray.add(EngineLoad);
        parametersArray.add(EngineCoolantTemperature);
        parametersArray.add(FuelPressure);
        parametersArray.add(EngineRPM);
        parametersArray.add(Speed);
        parametersArray.add(IntakeAirTemperature);
        parametersArray.add(RunTimesinceEngineStart);
        parametersArray.add(EngineOilTemperature);
        parametersArray.add(FuelLevel);
        parametersArray.add(AmbientTemperature);
        parametersArray.add(ThrottlePosition);
        parametersArray.add(ControlModuleVoltage);

        parametersArray.add(SoundLevel);
        parametersArray.add(Acceleration);
        parametersArray.add(LeanAngle);

        parametersArray.add(PDRPM);
        parametersArray.add(PDSupplyVolts);
        parametersArray.add(PDAmbTemp);
        parametersArray.add(PDEngineTemp);
        parametersArray.add(PDV1);
        parametersArray.add(PDV2);
        parametersArray.add(PDV3);


        final ArrayAdapter<String> parameterArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, parametersArray);
        parameterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spParameter.setAdapter(parameterArrayAdapter);
    }

    public void buttonClicks()
    {
        tvLegend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Legend.class));
            }
        });

        btSaveGuage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveCustomConfiguration();
            }
        });
    }

    public void selectGuage()
    {
        spGuage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iSelectedGuage  = spGuage.getSelectedItemPosition()+1;
                btSaveGuage.setText("Save Gauage "+String.valueOf(iSelectedGuage));
                Log.e(Tag,"Selected Guage: "+String.valueOf(iSelectedGuage));
                loadGuageData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    

    public String getValuesforSaveButton()
    {
        iSelectedGuage  = spGuage.getSelectedItemPosition()+1;
        iRangeMax = 0;       iRangeMin = 0;
        iAlarmMax = 0;       iAlarmMin = 0;
        sRequiredUnit = null;   fAdditionFactor = 0;     fMultiplicationFactor = 0;

        int iSelectedParameter = spParameter.getSelectedItemPosition()+1;
        Log.e(Tag,"iSelectedParameter: "+iSelectedParameter);
        switch (iSelectedParameter)
        {
            case 1: sDefaultUnit = keys.UnitEngineLoad;break;
            case 2: sDefaultUnit = keys.UnitEngineCoolantTemp;break;
            case 3: sDefaultUnit = keys.UnitFuelPressure;break;
            case 4: sDefaultUnit = keys.UnitEngineRPMUnit;break;
            case 5: sDefaultUnit = keys.UnitSpeed;break;
            case 6: sDefaultUnit = keys.UnitIntakeAirTemp;break;
            case 7: sDefaultUnit = keys.UnitRunTimeSinceEngineStart;break;
            case 8: sDefaultUnit = keys.UnitEngineOilTemp;break;
            case 9: sDefaultUnit = keys.UnitFuelLevel;break;
            case 10: sDefaultUnit = keys.UnitAmbientTemperature;break;
            case 11: sDefaultUnit = keys.UnitThrottlePosition;break;
            case 12: sDefaultUnit = keys.UnitControlModuleVoltage;break;
            case 13: sDefaultUnit = keys.UnitSound;break;
            case 14: sDefaultUnit = keys.UnitAcceleration;break;
            case 15: sDefaultUnit = keys.UnitEngineRPMUnit;break;
            case 16: sDefaultUnit = keys.UnitFuelLevel;break;
            case 17: sDefaultUnit = keys.UnitVoltage;break;
            case 18: sDefaultUnit = keys.UnitAmbientTemperature;break;
            case 19: sDefaultUnit = keys.UnitAmbientTemperature;break;
            case 20: sDefaultUnit = keys.UnitVoltage;break;
            case 21: sDefaultUnit = keys.UnitVoltage;break;
            case 22: sDefaultUnit = keys.UnitVoltage;break;
        }

        String sJSONString = null;

        sLabel = etLabel.getText().toString();
        String sRangeMin = etRangeMin.getText().toString();
        String sRangeMax = etRangeMax.getText().toString();
        String sAlarmMin = etAlarmMin.getText().toString();
        String sAlarmMax = etAlarmMax.getText().toString();
        String sAdditionFactor = etAddtionFactor.getText().toString();
        String sMultiplicationFactor = etMultiplicationFactor.getText().toString();

        if(sRangeMax.length()>0 && sRangeMin.length()>0)
        {
            iRangeMax = Integer.parseInt(sRangeMax);
            iRangeMin = Integer.parseInt(sRangeMin);
        }else{}

        if(sAlarmMax.length()>0 && sAlarmMin.length()>0)
        {
            iAlarmMin = Integer.parseInt(sAlarmMin);
            iAlarmMax = Integer.parseInt(sAlarmMax);
        }else{}

        sRequiredUnit = etRequiredUnit.getText().toString();
        if(TextUtils.isEmpty(sRequiredUnit))


        if(sAdditionFactor.length()>0)
            fAdditionFactor = Float.parseFloat(sAdditionFactor);

        if(sMultiplicationFactor.length()>0) {
            fMultiplicationFactor = Float.parseFloat(sMultiplicationFactor);
        }

          //  Toast.makeText(getApplicationContext(),"Invalid Multiplication Factor",Toast.LENGTH_SHORT).show();



        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(keys.GAUGE_NO,spGuage.getSelectedItemPosition()+1);
            jsonObject.put(keys.PARAMETER,spParameter.getSelectedItem().toString());
            switch (iSelectedGuage)
            {
                case 1: jsonObject.put(keys.CLOCKED_MAX,0);break;
                case 2: jsonObject.put(keys.CLOCKED_MAX,0);break;
                case 3: jsonObject.put(keys.CLOCKED_MAX,0);break;
                case 4: jsonObject.put(keys.CLOCKED_MAX,0);break;
                case 5: jsonObject.put(keys.CLOCKED_MAX,0);break;
                case 6: jsonObject.put(keys.CLOCKED_MAX,0);break;
                case 7: jsonObject.put(keys.CLOCKED_MAX,"NULL");break;
                case 8: jsonObject.put(keys.CLOCKED_MAX,"NULL");;break;
                case 9: jsonObject.put(keys.CLOCKED_MAX,"NULL");;break;
            }
            jsonObject.put(keys.LABEL,etLabel.getText().toString());
            jsonObject.put(keys.RANGE_MIN,etRangeMin.getText().toString());
            jsonObject.put(keys.RANGE_MAX,etRangeMax.getText().toString());
            jsonObject.put(keys.ALARM_MIN,etAlarmMin.getText().toString());
            jsonObject.put(keys.ALARM_MAX,etAlarmMax.getText().toString());
            jsonObject.put(keys.RESET_UNIT,sDefaultUnit);
            Log.e(Tag,"Default Unit: "+ sDefaultUnit);
            jsonObject.put(keys.REQUIRED_UNIT,etRequiredUnit.getText().toString());
            jsonObject.put(keys.ADDITION_FACTOR,etAddtionFactor.getText().toString());
            jsonObject.put(keys.MULTIPLICATION_FACTOR,etMultiplicationFactor.getText().toString());
            jsonObject.put(keys.GAUGE_TYPE,"0");
            jsonObject.put(keys.VALUE,"NULL");
            sJSONString = jsonObject.toString();
            Log.e(Tag, sJSONString);
        }
        catch (Exception e)
        {}

       return sJSONString;
    }

    public void saveCustomConfiguration()
    {
        iSelectedGuage  = spGuage.getSelectedItemPosition()+1;
        editorCustomConfig = getSharedPreferences(CUSTOM_PREFS,MODE_PRIVATE).edit();
        Log.e(Tag,"saveCustomConfiguration, slected gauge " + iSelectedGuage);
        switch (iSelectedGuage)
        {
            case 1: sG1CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G1_Key,sG1CustomConfig);break;
            case 2: sG2CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G2_Key,sG2CustomConfig);break;
            case 3: sG3CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G3_Key,sG3CustomConfig);break;
            case 4: sG4CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G4_Key,sG4CustomConfig);break;
            case 5: sG5CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G5_Key,sG5CustomConfig);break;
            case 6: sG6CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G6_Key,sG6CustomConfig);break;
            case 7: sG7CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G7_Key,sG7CustomConfig);break;
            case 8: sG8CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G8_Key,sG8CustomConfig);break;
            case 9: sG9CustomConfig = getValuesforSaveButton();editorCustomConfig.putString(keys.G9_Key,sG9CustomConfig);break;
        }

        editorCustomConfig.apply();

        Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_SHORT).show();



    }

    public void loadGuageData()
    {
        String sGuageConig = null;
        int selectedGuage = spGuage.getSelectedItemPosition() + 1;
        
        switch (selectedGuage)
        {
            case 1: sGuageConig = gaugeprefs.getString(keys.G1_Key,"NA");//spParameter.setSelection(3);
                    break;
            case 2: sGuageConig = gaugeprefs.getString(keys.G2_Key,"NA");//spParameter.setSelection(4);
                    break;
            case 3: sGuageConig = gaugeprefs.getString(keys.G3_Key,"NA");break;
            case 4: sGuageConig = gaugeprefs.getString(keys.G4_Key,"NA");break;
            case 5: sGuageConig = gaugeprefs.getString(keys.G5_Key,"NA");break;
            case 6: sGuageConig = gaugeprefs.getString(keys.G6_Key,"NA");break;
            case 7: sGuageConig = gaugeprefs.getString(keys.G7_Key,"NA");break;
            case 8: sGuageConig = gaugeprefs.getString(keys.G8_Key,"NA");break;
            case 9: sGuageConig = gaugeprefs.getString(keys.G9_Key,"NA");break;
        }
        
        if(sGuageConig.equals("NA"))
        {
            switch (selectedGuage)
            {
                case 1: sGuageConig = keys.G1ResetConfig ;//spParameter.setSelection(3);
                            break;
                case 2: sGuageConig = keys.G2ResetConfig;//spParameter.setSelection(4);
                            break;
                case 3: sGuageConig = keys.G3ResetConfig;break;
                case 4: sGuageConig = keys.G4ResetConfig;break;
                case 5: sGuageConig = keys.G5ResetConfig;break;
                case 6: sGuageConig = keys.G6ResetConfig;break;
                case 7: sGuageConig = keys.G7ResetConfig;break;
                case 8: sGuageConig = keys.G8ResetConfig;break;
                case 9: sGuageConig = keys.G9ResetConfig;break;
            }
        }
        
        JSONObject jsonobject = null;

        try
            {
                jsonobject = new JSONObject(sGuageConig);
                sParameter = jsonobject.getString(keys.PARAMETER);
                sLabel = jsonobject.getString(keys.LABEL);
                sRangeMin = jsonobject.getString(keys.RANGE_MIN);
                sRangeMax = jsonobject.getString(keys.RANGE_MAX);
                sAlarmMax = jsonobject.getString(keys.ALARM_MAX);
                sAlarmMin = jsonobject.getString(keys.ALARM_MIN);
                sDefaultUnit = jsonobject.getString(keys.RESET_UNIT);
                sRequiredUnitonLoad = jsonobject.getString(keys.REQUIRED_UNIT);
                sAdditionalFactor = jsonobject.getString(keys.ADDITION_FACTOR);
                sMultiplicationFactor = jsonobject.getString(keys.MULTIPLICATION_FACTOR);

                switch (sParameter)
                {
                    case EngineLoad:spParameter.setSelection(0);break;
                    case EngineCoolantTemperature:spParameter.setSelection(1);break;
                    case FuelPressure:spParameter.setSelection(2);break;
                    case EngineRPM:spParameter.setSelection(3);break;
                    case Speed:spParameter.setSelection(4);break;
                    case IntakeAirTemperature:spParameter.setSelection(5);break;
                    case RunTimesinceEngineStart:spParameter.setSelection(6);break;
                    case EngineOilTemperature:spParameter.setSelection(7);break;
                    case FuelLevel:spParameter.setSelection(8);break;
                    case AmbientTemperature:spParameter.setSelection(9);
                    case ThrottlePosition:spParameter.setSelection(10);
                    case ControlModuleVoltage:spParameter.setSelection(11);
                    case SoundLevel:spParameter.setSelection(12);break;
                    case Acceleration:spParameter.setSelection(13);break;
                    case LeanAngle:spParameter.setSelection(14);break;
                    case PDRPM:spParameter.setSelection(14);break;
                    case PDSupplyVolts:spParameter.setSelection(14);break;
                    case PDAmbTemp:spParameter.setSelection(14);break;
                    case PDEngineTemp:spParameter.setSelection(14);break;
                    case PDV1:spParameter.setSelection(14);break;
                    case PDV2:spParameter.setSelection(14);break;
                    case PDV3:spParameter.setSelection(14);break;


                }

                etLabel.setText(sLabel);
                etRangeMax.setText(sRangeMax);
                etRangeMin.setText(sRangeMin);
                etAlarmMax.setText(sAlarmMax);
                etAlarmMin.setText(sAlarmMin);
                etResetUnit.setText(sDefaultUnit);
                etRequiredUnit.setText(sRequiredUnitonLoad);
                etAddtionFactor.setText(sAdditionalFactor);
                etMultiplicationFactor.setText(sMultiplicationFactor);
                
            }
            catch (Exception e)
            {}

    }

}


