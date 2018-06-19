package com.yantramicrosystems.www.projectd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yantramicrosystems.www.projectd.UserClass.ClassGeneralfuncations;
import com.yantramicrosystems.www.projectd.UserClass.ClassGetPermission;
import com.yantramicrosystems.www.projectd.UserClass.Class_GaugeData;
import com.yantramicrosystems.www.projectd.UserClass.Class_Keys;
import com.yantramicrosystems.www.projectd.UserClass.OBD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainScreen extends AppCompatActivity implements SensorEventListener {

    static final String Tag     = "TagProD";
    static final String G1      = "G1";
    static final String sensor  = "sensor";
    static final String BT      = "bluetooth";
    /**
     * Constatns Used in Switch Cases
     */
    final String ATZ = "ATZ";
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


    public static final String sResATZ = "ATZ";
    public static final String sResATE0 = "ATE0";
    public static final String sResENGINELOAD = "04";
    public static final String sResENGINECOOLANTTEMPERATURE = "05";
    public static final String sResFUELPRESSURE = "0A";
    public static final String sResENGINERPM = "0C";
    public static final String sResSPEED = "0D";
    public static final String sResINTAKEAIRTEMPERATURE = "0F";
    public static final String sResTHROTTLEPOSITION = "11";
    public static final String sResRUNTIMESINCEENGINESTART = "1F";
    public static final String sResFUELTANKLEVELINPUT = "2F";
    public static final String sResAMBIENTAIRTEMPERATURE = "46";
    public static final String sResENGINEOILTEMPERATURE = "5C";
    public static final String sResCONTROLMODULEVOLTAGE = "42";

    //location stuff
    LocationService myService;
    LocationManager locationManager;

    public static int LocationP = 0;

    public static ProgressDialog locate;
    static boolean GPRSstatus;
    public static long GPRSstartTime;
    public static long GPRSendTime;
    public static int GPRSp = 0;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            myService = binder.getService();
            GPRSstatus = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            GPRSstatus = false;
        }
    };


    /**
     * End Constatns Used in Switch Cases
     */



    SharedPreferences GaugePrefs;

    SharedPreferences BluetoothPrefs;
    static String sBluetoothPreferences;
    static String SPINNER_ITEM_POSITION = "SPINNER_ITEM_POSITION";
    static String DEVICE_ADDRESS = "DEVICE_ADDRESS";

    Handler mHandler;


    ClassGetPermission getPermission = null;
    ClassGeneralfuncations gf = null;
    OBD obd = null;
    Class_Keys keys = null;

    CustomGauge G3, G4, G5, G6;

    public Class_GaugeData G1_Data, G2_Data, G3_Data, G4_Data, G5_Data;
    public Class_GaugeData G6_Data, G7_Data, G8_Data, G9_Data;


    String sReceivedPacket = "";

    float fVal_PHONE_ACCELERATION = 0;
    float fVal_LEAN = 0;
    float fVal_SOUND = 0;

     float fVal_ENGINELOAD = 0;
     float fVal_ENGINECOOLANTTEMPERATURE = 0;
     float fVal_FUELPRESSURE = 0;
     float fVal_ENGINERPM = 0;
     float fVal_SPEED_OBD = 0;
     public static float fVal_SPEED_GPS = 0;
     public static float fValGPSDistance=0;
     public static String sValLocation="";
     float fVal_INTAKEAIRTEMPERATURE = 0;
     float fVal_THROTTLEPOSITION = 0;
     float fVal_RUNTIMESINCEENGINESTART = 0;
     float fVal_FUELTANKLEVELINPUT = 0;
     float fVal_AMBIENTAIRTEMPERATURE = 0;
     float fVal_ENGINEOILTEMPERATURE = 0;
     float fVal_CONTROLMODULEVOLTAGE = 0;
    float fVal_PDRPM = 0;
    float fVal_PDfSupplyVolts = 0;
    float fVal_PDfAmbientTemp = 0;
    float fVal_PDfEngineTemp = 0;
    float fVal_PDfV1 = 0;
    float fVal_PDfV2 = 0;
    float fVal_PDfV3 = 0;

    int ENABLE_MODULE_GPS           =0;
    int ENABLE_MODULE_SOUND         =0;
    int ENABLE_MODULE_ACCELARTION   =0;
    int ENABLE_MODULE_LEAN          =0;
    int ENABLE_MODULE_BLUETOOTH     =0;


    static int iFirstTimeOpen = 0;

    LinearLayout layout1, layout2, layout7;
    RelativeLayout layout3, layout4, layout5, layout6;

    ImageButton ibSettings;
    Spinner sp_paireddevices;
    Button btConnect;
    TextView tvG1Value, tvG1Unit, tvG1Max;
    TextView tvG2Value, tvG2Unit, tvG2Max;
    TextView tvG3Value, tvG3Max, tvG3Name;
    TextView tvG4Value, tvG4Max, tvG4Name;
    TextView tvG5Value, tvG5Max, tvG5Name;
    TextView tvG6Value, tvG6Max, tvG6Name;
    TextView tvG7Name, tvG7Value;
    TextView tvG8Name, tvG8Value;
    TextView tvG9Name, tvG9Value;
    public static TextView tvCurrentLocation;

    double G1Max = 0,G2Max = 0,G3Max = 0,G4Max = 0,G5Max = 0,G6Max = 0;
    int iG1Max,iG2Max,iG3Max,iG4Max,iG5Max,iG6Max;
    int G3Type = 0,G4Type = 0,G5Type = 0,G6Type=0;

    int iRPMmax = 0;

    final Context context = this;


    /**
     * GPS Components
     */
    private String provider;
    public static double myLatitude, myLongitude;
    public float fspeedGPS;
    /**
     * End of GPS Components
     */


    /**
     * Sound Measurement Components
     */
    private MediaRecorder mRecorder;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    int iSoundInDB = 0;
    /**
     * End of Sound Measurement Components
     */


    /**
     * Acceleration & Rotation Measurement Components
     */
    public static float acceleration;
    private SensorManager mSensorManager;
    private Sensor mSensor_LinearAcceleration;
    public static float x_Acceleration;
    public static float y_Acceleration;
    public static float z_Acceleration;
    final float[] mValuesMagnet      = new float[3];
    final float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];
    double dLeaninRad,dLeaninDeg;
    /**
     * End of Acceleration & Rotation Measurement Components
     */

    /**
     * -------Bluetooth Components-------
     */
    String[][] DevAddressString;
    public static String BTaddress = "";
    public static String devicename = "";
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectedThread mConnectedThread;
    Handler bt_handler;
    final int RECIEVE_MESSAGE = 1;
    StringBuilder sb = new StringBuilder();
    byte[] buffer;
    byte[] readBuf;
    String strIncom;
    int readbuflength;
    final int REQUEST_ENABLE_BT=1;
    final boolean BluetoothON = mBluetoothAdapter.isEnabled();
    final boolean BluetoothOFF = !mBluetoothAdapter.isEnabled();
    final boolean noBluetooth = (mBluetoothAdapter == null);
    int iSelectedDevicePosition;
    int iNoOfBluetoothSocket;
    int iTotal,iSuccess,iFail;
    String sOutStream = "";
    String sNewString = "";
    int iCounter = 0;
    int iTrue = 0;
    int iFalse = 0;
    int iBTConnectionState=0;
    private InputStream mmInStream ;
    private OutputStream mmOutStream;
    boolean ConnectedOnce=false;
    /**
     * -------End of Bluetooth Components-------
     */

    ArrayList<String> alPackets;
    int iSendCommandCntr=0;
    int iRequestCount = 0, iPacketCounter = 0;
    int iRejectedPackets = 0;
    int iTotalPackets = 0;

    private Thread AppKillThread = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Log.e(Tag,"On Create - start");

        resetValues();              //clear all global parameters
        initializeClassObjects();
        getUserPermissions();
        initializeViews();
        settingsButtonOnClick();
        resetMaxValues();
        handler();


        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        Log.e(Tag,"On Create - end");
    }


    //location stuff
    //This method leads you to the alert dialog box.
    void checkGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            showGPSDisabledAlertToUser();
        }
    }

    //This method configures the Alert Dialog box.
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    void bindGPRSService() {
        Log.e(Tag,"unbindGRPSService");
        if (GPRSstatus == true)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        bindService(i, sc, BIND_AUTO_CREATE);
        GPRSstatus = true;
        GPRSstartTime = System.currentTimeMillis();
    }

    void unbindGRPSService() {
        Log.e(Tag,"unbindGRPSService");
        if (GPRSstatus == false)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        unbindService(sc);
        GPRSstatus = false;
    }



    //endlocation stuff

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

                //Device is now connected
                btConnect.setText("Connected");
                iBTConnectionState = 1;

                if(!ConnectedOnce)
                {
                    ConnectedOnce=true;
                }


                resetDataProcessingComponents();

                alPackets = new ArrayList<String>();
                sendMessage(OBD.getATZ);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendMessage(OBD.getATE0);


            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected

                btConnect.setText("Connect");
                iBTConnectionState=0;
                Log.e(BT,"Bluetooth Disconnected");
                resetDataProcessingComponents();

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag, "ON Resume - start");

        resetDataProcessingComponents();
        MEMReadfromMemory();

        //display which sensors have to be enabled
        Log.e(Tag, "ON Resume Check sensor to be enabled");
        if(ENABLE_MODULE_LEAN==1) {
            Log.e(Tag, "ON Resume Sensor Enabled : Lean");
            setupRotationSensors();
            mHandler.postDelayed(interruptRoutineGetLeanValue, 1000);
        }
        if(ENABLE_MODULE_ACCELARTION==1) {
            Log.e(Tag, "ON Resume Sensor Enabled : Accelaration");
            mSensorManager.registerListener(this, mSensor_LinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);

        }
        if(ENABLE_MODULE_SOUND==1) {
            Log.e(Tag, "ON Resume Sensor Enabled : Sound");
            startRecorder();
            mHandler.postDelayed(interruptRoutineGetSoundValue, 1000);
        }
        if(ENABLE_MODULE_BLUETOOTH==1) {
            Log.e(Tag, "ON Resume Sensor Enabled : BLUETooth");
            startBluetooth();
        }
        if(ENABLE_MODULE_GPS==1) {
            Log.e(Tag, "ON Resume Sensor Enabled : GPS");


            //location stuff
            //The method below checks if Location is enabled on device or not. If not, then an alert dialog box appears with option
            //to enable gps.
            checkGps();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            assert locationManager != null;
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                return;
            }
            if (GPRSstatus == false)
                //Here, the Location Service gets bound and the GPS Speedometer gets Active.
                bindGPRSService();
            //endlocation

        }










        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        startInterruptRoutines();
        Log.e(Tag, "ON Resume - end");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag, "ON Pause -start");


       /* if(ENABLE_MODULE_LEAN==1) {
            Log.e(Tag, "ON Pause Sensor Disable : Lean");
            mHandler.removeCallbacks(interruptRoutineGetLeanValue);
        }
        if(ENABLE_MODULE_ACCELARTION==1) {
            Log.e(Tag, "ON Pause Sensor disable : Accelaration sensor ");
            mSensorManager.unregisterListener(this,mSensor_LinearAcceleration);
        }
        if(ENABLE_MODULE_SOUND==1) {
            Log.e(Tag, "ON Pause Sensor Disable : Sound");
            mHandler.removeCallbacks(interruptRoutineGetSoundValue);

        }
        if(ENABLE_MODULE_BLUETOOTH==1) {
            Log.e(Tag, "ON Pause Sensor Disable : BLUETooth");
            closeBluetooth();
        }
        if(ENABLE_MODULE_GPS==1) {
            Log.e(Tag, "ON Pause Sensor Disable : GPS");

            unbindGRPSService();


        }
*/
        stopRunnables();
        Log.e(Tag, "ON Pause - end");
    }


    @Override
    protected void onStart() {
        Log.e(Tag, "ON start - start");
        super.onStart();
        Log.e(Tag, "ON start - end");
    }

    public void startBluetooth()
  {
      //bluetooth code
      Log.e(Tag, "ON Resume : Bluetooth start");
      BTenableBluetooth();
      if(BluetoothON)
      {
          BTpopulateSpinner();
          BTSetupSpinnerAndConnectButtonEvent();
          Log.e(Tag, "Bluetooth connection status : " + String.valueOf(iBTConnectionState));

        /*  try {
              if (iBTConnectionState == 0)
                  BTconnecttoSelectedDevice(BTaddress );
          }
          catch (Exception e)
          {}*/
          Log.e(Tag, "ON Resume : Bluetooth end");
      }
      else {

          Log.e(Tag, "ON Resume : Bluetooth not turned ON");
      }
  }

    //inbuild sensor rading

    //Accelerometer
    @Override
    public void onSensorChanged(SensorEvent sensorevent)
    {
        if (sensorevent.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
            x_Acceleration = sensorevent.values[0];

            @SuppressLint("DefaultLocale") String ax = String.format("%.02f", x_Acceleration);
            x_Acceleration = Float.parseFloat(ax);
            //Log.e(sensor,"x Acceleration: "+String.valueOf(x_Acceleration));
            y_Acceleration = sensorevent.values[1];

            @SuppressLint("DefaultLocale") String ay = String.format("%.02f", y_Acceleration);
            y_Acceleration = Float.parseFloat(ay);
            //Log.e(sensor,"y Acceleration: "+String.valueOf(y_Acceleration));
            z_Acceleration = sensorevent.values[2];

            @SuppressLint("DefaultLocale") String az = String.format("%.02f", z_Acceleration);
            z_Acceleration = Float.parseFloat(az);

            if(x_Acceleration>y_Acceleration && x_Acceleration>z_Acceleration)
                acceleration = x_Acceleration;
            else if(y_Acceleration>x_Acceleration && y_Acceleration>z_Acceleration)
                acceleration = y_Acceleration;
            else if(z_Acceleration>x_Acceleration && z_Acceleration>y_Acceleration)
                acceleration = z_Acceleration;
        }

        switch (sensorevent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(sensorevent.values, 0, mValuesAccel, 0, 3);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(sensorevent.values, 0, mValuesMagnet, 0, 3);
                break;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    ;

    public int SoundGetValue()
    {

        int iGaugeValue=0;
        double dSoundInDB = soundDb(3.55);
        double fMaxVal = 90,fMinVal = 10,fValue = 0;

        int idBSound = (int)dSoundInDB;
        if( (idBSound>10) && (idBSound<120)) {

            iSoundInDB=idBSound;
            fValue = idBSound;

            double fDifference = fMaxVal - fMinVal;
            double fmulFactor = 100/fDifference;

            if (fValue<fMinVal)
                fValue=fMinVal;

            if(fValue>=fMaxVal)
                fValue=fMaxVal;

            double dParameterValue = ((fValue - fMinVal) * fmulFactor);
            iGaugeValue = (int) dParameterValue;
            //Log.e(Tag,"Sound: "+String.valueOf(iGaugeValue));
            dParameterValue = fValue;
            dParameterValue = (int)dParameterValue;

        }
        return iGaugeValue;
    }

    public double soundDb(double ampl)
    {
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }

    public double getAmplitude()
    {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;
    }

    public double getAmplitudeEMA()
    {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    public void startRecorder()
    {
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
        }

    }

    public void stopRecorder() {

        try{
            if(mRecorder == null) {
                Log.e(BT,"No Recorder");
                return;
            }
            else
            {
                mRecorder.stop();
                //Log.e(BT,"Recorder Stopped");
                mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
                //Log.e(BT,"Recorder Reset");
                mRecorder.release();
                //Log.e(BT,"Recorder Released");

            }
        }
        catch (RuntimeException stopException)
        {}

    }


    public void setupRotationSensors()
    {
        final SensorEventListener mEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
            public void onSensorChanged(SensorEvent event) {
                // Handle the events for which we registered
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                        break;
                }
            };
        };
        setListners(mSensorManager,mEventListener);
    }

    public float GetLeanFalue()
    {

        mSensorManager.getOrientation(mRotationMatrix,mValuesOrientation);
        //double lean = mValuesOrientation[1];

        SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
        SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
        //claculate lead angle
        dLeaninRad = mValuesOrientation[1];
        dLeaninRad = Math.abs(dLeaninRad);
        dLeaninDeg = Math.toDegrees(dLeaninRad);
        Float fLeaninDeg = (float) dLeaninDeg;
        String sLeaninDeg = String.format("%.1f", fLeaninDeg);
        fLeaninDeg = Float.parseFloat(sLeaninDeg);
       // Log.e(sensor,"Rotation: "+String.valueOf(fLeaninDeg));
        return fLeaninDeg;
    }

    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
    {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }




    //gauge stuff
    public void initializeClassObjects() {
        getPermission = new ClassGetPermission();
        gf = new ClassGeneralfuncations();
        obd = new OBD();
        keys = new Class_Keys();
        mHandler = new Handler();
        bt_handler = new Handler();


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mSensor_LinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }


        G1_Data = new Class_GaugeData();
        G2_Data = new Class_GaugeData();
        G3_Data = new Class_GaugeData();
        G4_Data = new Class_GaugeData();
        G5_Data = new Class_GaugeData();
        G6_Data = new Class_GaugeData();
        G7_Data = new Class_GaugeData();
        G8_Data = new Class_GaugeData();
        G9_Data = new Class_GaugeData();

        GaugePrefs = getSharedPreferences(settings2.CUSTOM_PREFS, MODE_PRIVATE);
    }


    public void getUserPermissions() {
        getPermission.GetUserPermissions(this);

    }

    /**
     *  Bluetooth Functions
     */

    public void BTenableBluetooth()
    {
        Log.e(Tag, "BTenableBluetooth-start");
        if ((!noBluetooth) && (!mBluetoothAdapter.isEnabled()))
        {
            Log.e(Tag, "enableBluetooth-Bluetooth not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Log.e(Tag, "BTenableBluetooth-return");
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {

        if ((requestCode == REQUEST_ENABLE_BT) && (resultCode == RESULT_OK))
        {
            boolean isEnabling = mBluetoothAdapter.enable();
            if (!isEnabling)
            {}
            else if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)
            {}
        }

        BTpopulateSpinner();
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private void BTpopulateSpinner() {

        Log.e(Tag, "BTpopulateSpinner-start");
        ArrayList<String> PairedDevicesArray = new ArrayList<String>();
        PairedDevicesArray.add("Select Device");
        Set<BluetoothDevice> PairedDevicesList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            PairedDevicesList = mBluetoothAdapter.getBondedDevices();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            if(mBluetoothAdapter.getBondedDevices()==null)
            {

            }
        }

        int iNoOfPariedDev = PairedDevicesList.size();
        int iDeviceCntr = 0;
        DevAddressString = new String[iNoOfPariedDev][2];

        for (BluetoothDevice device : PairedDevicesList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                DevAddressString[iDeviceCntr][0] = device.getAddress();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                DevAddressString[iDeviceCntr][1] = device.getName();
            }
            iDeviceCntr++;
            if (iDeviceCntr > iNoOfPariedDev)
                break;
        }

        for (int i = 0; i < iDeviceCntr; i++) {
            PairedDevicesArray.add(DevAddressString[i][1]);
        }

        final ArrayAdapter<String> PairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, PairedDevicesArray);
        PairedDevicesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_paireddevices.setAdapter(PairedDevicesArrayAdapter);

        try{
            BTaddress  = "";       //Defining a default device for avoiding crashes. Actual Device address may vary.
        }catch (Exception e)
        {}

        //load previous saved bluetooth from memroty
        BluetoothPrefs = getSharedPreferences(sBluetoothPreferences,MODE_PRIVATE);
        iSelectedDevicePosition = BluetoothPrefs.getInt(SPINNER_ITEM_POSITION,0);
        BTaddress  = BluetoothPrefs.getString(DEVICE_ADDRESS,"NA");

        if(iSelectedDevicePosition>0) {
            try{
                sp_paireddevices.setSelection(iSelectedDevicePosition);
            }
            catch (Exception e)
            {
                sp_paireddevices.setSelection(0);
            }
        }
        else
            sp_paireddevices.setSelection(0);

        Log.e(Tag, "BTpopulateSpinner- previous stored spinner position : " + String.valueOf(iSelectedDevicePosition));
        Log.e(Tag, "BTpopulateSpinner- previous stored BT Address       : " + BTaddress);
        Log.e(Tag, "BTpopulateSpinner-end");
    }

    private void BTSetupSpinnerAndConnectButtonEvent()
    {
        Log.e(Tag, "BTSetupSpinnerAndConnectButtonEvent-start");
        sp_paireddevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final SharedPreferences.Editor BluetoothPrefs_Editor = BluetoothPrefs.edit();
                Log.e(Tag, "BTSetupSpinnerAndConnectButtonEvent/onItemSelected-start");
                iSelectedDevicePosition = sp_paireddevices.getSelectedItemPosition();
                if(iSelectedDevicePosition==0)
                    BTaddress  = "";
                else {
                    BTaddress  = DevAddressString[iSelectedDevicePosition - 1][0];
                    BluetoothPrefs_Editor.putInt(SPINNER_ITEM_POSITION,iSelectedDevicePosition);
                    BluetoothPrefs_Editor.putString(DEVICE_ADDRESS,BTaddress );
                    BluetoothPrefs_Editor.apply();
                    iNoOfBluetoothSocket=0;
                }

                Log.e(Tag,"Device BTaddress : "+ BTaddress );

                Log.e(Tag, "BTSetupSpinnerAndConnectButtonEvent/onItemSelected-end");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(Tag, "BTSetupSpinnerAndConnectButtonEvent/ButtonClicked-start");


               if(iBTConnectionState==0)
               {
                   Toast.makeText(getApplicationContext(), "Trying to Connect to BT", Toast.LENGTH_SHORT).show();
                   btConnect.setText("Connecting...");
                   if(iSelectedDevicePosition>0 && !BTaddress .equals("NA"))
                   {
                       BTconnecttoSelectedDevice(BTaddress );
                   }
               }
               else if(iBTConnectionState==1)
               {
                   closeBluetooth();

               }
                Log.e(Tag, "BTSetupSpinnerAndConnectButtonEvent/ButtonClicked-end");

            }
        });

        Log.e(Tag, "selectAndConnectToDevice-end");
    }

    public void closeBluetooth()
    {
        iBTConnectionState=0;
        btConnect.setText("Connect");
        if (mmInStream != null) {
            try {mmInStream.close();} catch (Exception e) {}
            mmInStream = null;
        }

        if (mmOutStream != null) {
            try {mmOutStream.close();} catch (Exception e) {}
            mmOutStream = null;
        }

        if (btSocket != null) {
            try {btSocket.close();} catch (Exception e) {}
            btSocket = null;
        }
    }

    public void BTconnecttoSelectedDevice(String sDeviceAddress)
    {
        Log.e(Tag, "BTconnecttoSelectedDevice-start");
        try {

            closeBluetooth();

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.e(Tag, "Getting REMOTE DEVICE BTaddress ");
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(sDeviceAddress);
            Log.e(Tag, "Got REMOTE DEVICE BTaddress : " + sDeviceAddress);
            btSocket = createBluetoothSocket(device);

            iNoOfBluetoothSocket++;

            mBluetoothAdapter.cancelDiscovery();
            Log.e(Tag, "BT Discovery Cancelled");

            Log.e(Tag, "Trying to Connect to Device");
            btSocket.connect();
            Log.e(Tag, "BLUETOOTH CONNECTED");

            mConnectedThread = new ConnectedThread(btSocket);
            Log.e(Tag, "Connection Thread Created");
            mConnectedThread.start();
            Log.e(Tag, "Connection Thread Started");
            //mHandlerOBD.postDelayed(mRunnableOBD, 1000);


        }
        catch (IOException e)
        {
            Log.e(Tag, "Connection Failed");
            Toast.makeText(getApplicationContext(), "Couldn't Connect - Retry", Toast.LENGTH_SHORT).show();
            try {
                Log.e(Tag, "Trying to close Bluetooth Socket");
                btSocket.close();
                Log.e(Tag, "Bluetooth Socket Closed");
            } catch (IOException e2)
            {
                Log.e(Tag, "Could not Close bluetooth Socket");
            }
        }

        Log.e(Tag, "BTconnecttoSelectedDevice-end");
    }

    private class ConnectedThread extends Thread
    {
        public ConnectedThread(BluetoothSocket socket) {

            try {
              //  mmInStream = socket.wait();
                mmInStream = socket.getInputStream();
                mmOutStream = socket.getOutputStream();
            } catch (IOException e) {
            }

        }

        public void run() {

            buffer = new byte[2048];  // buffer store for the stream
            while (mmInStream != null) {
                try {

                    int bytes = mmInStream.available();
                    if(bytes>0)
                    {

                        mmInStream.read(buffer);
                        bt_handler.obtainMessage(RECIEVE_MESSAGE, bytes, 0, buffer.clone()).sendToTarget();
                        Log.e(Tag, "BT REC : " + bytes );
                        buffer[bytes] = '\0';
                    }
                    else {
                        SystemClock.sleep(400); // givem some time for bt handerl to make the packet
                    }

                } catch (IOException e) {
                    //Log.e(BT,"Catch of Incoming Stream");
                    break;
                }
            }



        }

        public void write(String s) {

            try {

                for(int i = 0; i<2048;i++)
                {
                    buffer[i] = '\0';
                }

                sOutStream = s;
                mmOutStream.write(s.getBytes());
                sOutStream = s;
                Log.e(BT,"Outstream: " + sOutStream);

            } catch (Exception e) {
                Log.e(BT,"Catch of Bluetooth Write");

            }
        }

    }

    @SuppressLint("HandlerLeak")
    public void handler()
    {

        bt_handler = new Handler()
        {

            public void handleMessage(android.os.Message msg)
            {
                //Log.e(BT,"In Handler");

                switch (msg.what) {

                    case RECIEVE_MESSAGE:

                        readBuf = (byte[]) msg.obj;
                        strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);
                        sNewString = sNewString + sb.toString();
                        sNewString = sNewString.replaceAll("\\s", "");

                        sb.delete(0,sb.length());

                        //here
                        int iStartIndex = sNewString.indexOf('{');
                        int iEndIndex = sNewString.indexOf('}');

                        Log.e(Tag,"Message : " + sNewString.length() + "  :  " + sNewString);
                  //      Log.e(Tag,"Start index : " + iStartIndex+"  end index : " + iEndIndex);


                        if( (iStartIndex>iEndIndex) && (iEndIndex>=0))
                        {
                            sNewString=sNewString.substring(iEndIndex+1);
                  //          Log.e(Tag,"updated packet 1: " + sNewString);
                            iStartIndex = sNewString.indexOf('{');
                            iEndIndex = sNewString.indexOf('}');
                        }

                        if( (iStartIndex>=0) &&(iEndIndex>0) && (iStartIndex<iEndIndex) ) {
                            sReceivedPacket = sNewString.substring(iStartIndex, iEndIndex + 1);

                  //          Log.e(Tag, "Packet : " + sReceivedPacket);

                            iEndIndex = sNewString.lastIndexOf('}');
                   //         Log.e(Tag,"last index : " + iEndIndex);
                             if(iEndIndex>10)
                                 sNewString=sNewString.substring(iEndIndex+1);

                  //          Log.e(Tag,"updated packet 2: " + sNewString);
                        }
                        if(sNewString.length()>100)
                            sNewString="";



                        break;
                }
            }
        };
    }

    public void processOBDData(String Parameter,String Value)
    {


        float f = 0;
        switch (Parameter)
        {

            case sResENGINELOAD:
                fVal_ENGINELOAD = OBD.calcEngineLoad(Value);
                f = fVal_ENGINELOAD;
                break;

            case sResENGINECOOLANTTEMPERATURE:
                fVal_ENGINECOOLANTTEMPERATURE = OBD.calcEngineCoolantTemperature(Value);
                f = fVal_ENGINECOOLANTTEMPERATURE;
                break;
            case sResFUELPRESSURE:
                fVal_FUELPRESSURE = OBD.calcFuelPressure(Value);
                f = fVal_FUELPRESSURE;
                break;
            case sResENGINERPM:
                fVal_ENGINERPM = OBD.calcEngineRPM(Value);
                f = fVal_ENGINERPM;
                break;
            case sResSPEED:
                fVal_SPEED_OBD = OBD.calcSpeed(Value);
                f = fVal_SPEED_OBD;
                break;
            case sResINTAKEAIRTEMPERATURE:
                fVal_INTAKEAIRTEMPERATURE = OBD.calcIntakeAirTemperature(Value);
                f = fVal_INTAKEAIRTEMPERATURE;
                break;
            case sResTHROTTLEPOSITION:
                fVal_THROTTLEPOSITION = OBD.calcThrottlePosition(Value);
                f = fVal_THROTTLEPOSITION;
                break;
            case sResRUNTIMESINCEENGINESTART:
                fVal_RUNTIMESINCEENGINESTART = OBD.calcRunTimeSinceEngineStart(Value);
                f = fVal_RUNTIMESINCEENGINESTART;
                break;
            case sResFUELTANKLEVELINPUT:
                fVal_FUELTANKLEVELINPUT = OBD.calcFuelTankLevelInput(Value);
                f = fVal_FUELTANKLEVELINPUT;
                break;
            case sResAMBIENTAIRTEMPERATURE:
                fVal_AMBIENTAIRTEMPERATURE = OBD.calcAmbientAirTemperature(Value);
                f = fVal_AMBIENTAIRTEMPERATURE;
                break;
            case sResENGINEOILTEMPERATURE:
                fVal_ENGINEOILTEMPERATURE = OBD.calcEngineOilTemperature(Value);
                f = fVal_ENGINEOILTEMPERATURE;
                break;
            case sResCONTROLMODULEVOLTAGE:
                fVal_CONTROLMODULEVOLTAGE = OBD.calcControlModuleVoltage(Value);
                f = fVal_CONTROLMODULEVOLTAGE;
                break;
        }

        Log.e(BT,"Processed Value: " + String.valueOf(f));

    }

    private void sendMessage(String message)
    {
        ConnectedThread ct;
        ct = mConnectedThread;
        try
        {
            mConnectedThread.write(message);
        }catch (Exception e)
        {
            Log.e(BT, "Catch of SendMessage");

        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e2) {
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }


    /**
     *  End of Bluetooth Functions
     */




    public void initializeViews() {
        ibSettings = findViewById(R.id.idbtSettings);
        tvG1Value =  findViewById(R.id.tvG1Value);
        tvG2Value =  findViewById(R.id.tvG2Value);
        tvG3Value =  findViewById(R.id.tvG3Value);
        tvG4Value =  findViewById(R.id.tvG4Value);
        tvG5Value =  findViewById(R.id.tvG5Value);
        tvG6Value =  findViewById(R.id.tvG6Value);
        tvG7Value =  findViewById(R.id.tvG7Value);
        tvG8Value =  findViewById(R.id.tvG8Value);
        tvG9Value =  findViewById(R.id.tvG9Value);

        tvG3Name =  findViewById(R.id.tvG3Name);
        tvG4Name =  findViewById(R.id.tvG4Name);
        tvG5Name =  findViewById(R.id.tvG5Name);
        tvG6Name =  findViewById(R.id.tvG6Name);
        tvG7Name =  findViewById(R.id.tvG7Name);
        tvG8Name =  findViewById(R.id.tvG8Name);
        tvG9Name =  findViewById(R.id.tvG9Name);

        tvG1Max =  findViewById(R.id.tvG1Max);
        tvG2Max =  findViewById(R.id.tvG2Max);
        tvG3Max =  findViewById(R.id.tvG3Max);
        tvG4Max =  findViewById(R.id.tvG4Max);
        tvG5Max =  findViewById(R.id.tvG5Max);
        tvG6Max =  findViewById(R.id.tvG6Max);

        tvG1Unit =  findViewById(R.id.tvG1Unit);
        tvG2Unit =  findViewById(R.id.tvG2Unit);

        tvCurrentLocation =  findViewById(R.id.tvCurrentLocation);

        G3 =  findViewById(R.id.idGauge3);
        G4 =  findViewById(R.id.idGauge4);
        G5 =  findViewById(R.id.idGauge5);
        G6 =  findViewById(R.id.idGauge6);

        layout1 =  findViewById(R.id.layout1);
        layout2 =  findViewById(R.id.layout2);
        layout3 =  findViewById(R.id.layout3);
        layout4 =  findViewById(R.id.layout4);
        layout5 =  findViewById(R.id.layout5);
        layout6 =  findViewById(R.id.layout6);
        layout7 =  findViewById(R.id.layout7);

        sp_paireddevices =  findViewById(R.id.id_spinner_pairedDevices);
        btConnect =  findViewById(R.id.btConnect);

    }

    public void settingsButtonOnClick() {
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), settings2.class));
            }
        });
    }

    public void MEMReadfromMemory() {
        Log.e(Tag, "MEMReadfromMemory - Start");
        GaugePrefs = getSharedPreferences(settings2.CUSTOM_PREFS, MODE_PRIVATE);
        String sTempG1CustomPref = GaugePrefs.getString(keys.G1_Key, "");
        if (sTempG1CustomPref.equals("") || sTempG1CustomPref.equals(null)) {
            iFirstTimeOpen = 1;
            Log.e(Tag, "App Opening for the First Time");
            MEMWriteDefaultConfiguration();

            MEMReadStoreDataFromMemory();
        } else {
            Log.e(Tag, "App has Data In Shared Preferences");
            MEMReadStoreDataFromMemory();
        }

        Log.e(Tag, "MEMReadfromMemory - end");

    }





   //memorty stuff

    public void MEMWriteDefaultConfiguration() {
        SharedPreferences.Editor editor = GaugePrefs.edit();
        editor.putString(keys.G1_Key, keys.G1ResetConfig);
        editor.putString(keys.G2_Key, keys.G2ResetConfig);
        editor.putString(keys.G3_Key, keys.G3ResetConfig);
        editor.putString(keys.G4_Key, keys.G4ResetConfig);
        editor.putString(keys.G5_Key, keys.G5ResetConfig);
        editor.putString(keys.G6_Key, keys.G6ResetConfig);
        editor.putString(keys.G7_Key, keys.G7ResetConfig);
        editor.putString(keys.G8_Key, keys.G8ResetConfig);
        editor.putString(keys.G9_Key, keys.G9ResetConfig);
        editor.apply();
        Log.e(Tag, "Defalut Configurations Saved to Shared Preferences");
    }
//readDataFromSharedPrefrences
    public void MEMReadStoreDataFromMemory() {
        Log.e(Tag, "MEMReadStoreDataFromMemory - start ");
        G1_Data.config = GaugePrefs.getString(keys.G1_Key, "NA");
        decodeG1Config();
        G2_Data.config = GaugePrefs.getString(keys.G2_Key, "NA");
        decodeG2Config();
        G3_Data.config = GaugePrefs.getString(keys.G3_Key, "NA");
        decodeG3Config();
        G4_Data.config = GaugePrefs.getString(keys.G4_Key, "NA");
        decodeG4Config();
        G5_Data.config = GaugePrefs.getString(keys.G5_Key, "NA");
        decodeG5Config();
        G6_Data.config = GaugePrefs.getString(keys.G6_Key, "NA");
        decodeG6Config();
        G7_Data.config = GaugePrefs.getString(keys.G7_Key, "NA"); //
        decodeG7Config();
        G8_Data.config = GaugePrefs.getString(keys.G8_Key, "NA"); //
        decodeG8Config();
        G9_Data.config = GaugePrefs.getString(keys.G9_Key, "NA"); //
        decodeG9Config();
        Log.e(Tag, "MEMReadStoreDataFromMemory - end");
    }

    public void decodeG1Config() {
        Log.e(Tag, "MEMdecodeG1Config G1- start");
        if (!G1_Data.config.equals("NA")) {
            JSONObject G1Json = null;
            //Log.e(Tag, "Reading From stored Data");
            try {
                G1Json = new JSONObject(G1_Data.config);
                G1_Data.guageNo = Integer.parseInt(G1Json.getString(keys.GAUGE_NO));
                //Log.e(Tag, "guage obtained");
                G1_Data.parameter = G1Json.getString(keys.PARAMETER);
                // Log.e(Tag, "parameter obtained");
                G1_Data.label = G1Json.getString(keys.LABEL);
                G1_Data.maxValue = Float.parseFloat(G1Json.getString(keys.CLOCKED_MAX));
                // Log.e(Tag, "max value obtained");
                G1_Data.rangeMin = Float.parseFloat(G1Json.getString(keys.RANGE_MIN));
                // Log.e(Tag, G1Json.getString(RANGE_MIN));
                //  Log.e(Tag, "range min obtained");
                G1_Data.rangeMax = Float.parseFloat(G1Json.getString(keys.RANGE_MAX));
                Log.e(G1, "range max obtained: "+String.valueOf(G1_Data.rangeMax));
                iRPMmax = (int)G1_Data.rangeMax;
                G1_Data.alarmMin = Float.parseFloat(G1Json.getString(keys.ALARM_MIN));
                // Log.e(Tag, "alarm min obtained");
                G1_Data.alarmMax = Float.parseFloat(G1Json.getString(keys.ALARM_MAX));
                // Log.e(Tag, "alarm max obtained");
                G1_Data.defaultUnit = G1Json.getString(keys.RESET_UNIT);
                //  Log.e(Tag, "default unit obtained: ");
                G1_Data.requiredUnit = G1Json.getString(keys.REQUIRED_UNIT);
                //  Log.e(Tag, "custom unit obtained");
                G1_Data.additionFactor = Float.parseFloat(G1Json.getString(keys.ADDITION_FACTOR));
                //  Log.e(Tag, "addition factor obtained");
                G1_Data.multiplicationFactor = Float.parseFloat(G1Json.getString(keys.MULTIPLICATION_FACTOR));
                // Log.e(Tag, "mulitpli factor obtained");
                G1_Data.value = 0;
                //  Log.e(Tag, "Data Decoding Success");

            } catch (JSONException e) {
                Log.e(Tag, "G1 Decoding Failed");
                e.printStackTrace();
            }
        }

        if (G1_Data.requiredUnit.equals("NULL"))
            tvG1Unit.setText(G1_Data.defaultUnit);
        else
            tvG1Unit.setText(G1_Data.requiredUnit);

        // Log.e(Tag, "Custom Unit: " + G1_Data.requiredUnit);

        //tvG1Value.setText(String.valueOf(Math.round(G1_Data.value)));
        tvG1Max.setText(String.valueOf(Math.round(G1_Data.maxValue)));
        // Log.e(Tag, "G1 DECODED");

        String sParaName = G1_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG1Config - Gauge no " + G1_Data.guageNo + ", param" +G1_Data.parameter + "label " + G1_Data.label);
    }

    public void decodeG2Config() {

        Log.e(Tag, "MEMdecodeG2Config- start");
        if (!G2_Data.config.equals("NA")) {
            JSONObject G2Json = null;
            try {
                G2Json = new JSONObject(G2_Data.config);
                G2_Data.guageNo = Integer.parseInt(G2Json.getString(keys.GAUGE_NO));
                G2_Data.parameter = G2Json.getString(keys.PARAMETER);
                G2_Data.label = G2Json.getString(keys.LABEL);
                G2_Data.maxValue = Float.parseFloat(G2Json.getString(keys.CLOCKED_MAX));
                G2_Data.rangeMin = Float.parseFloat(G2Json.getString(keys.RANGE_MIN));
                G2_Data.rangeMax = Float.parseFloat(G2Json.getString(keys.RANGE_MAX));
                G2_Data.alarmMin = Float.parseFloat(G2Json.getString(keys.ALARM_MIN));
                G2_Data.alarmMax = Float.parseFloat(G2Json.getString(keys.ALARM_MAX));
                G2_Data.defaultUnit = G2Json.getString(keys.RESET_UNIT);
                G2_Data.requiredUnit = G2Json.getString(keys.REQUIRED_UNIT);
                G2_Data.additionFactor = Float.parseFloat(G2Json.getString(keys.ADDITION_FACTOR));
                G2_Data.multiplicationFactor = Float.parseFloat(G2Json.getString(keys.MULTIPLICATION_FACTOR));
                G2_Data.value = 0;
                // Log.e(Tag, "G2 Data Decoding Success");

            } catch (JSONException e) {
                Log.e(Tag, "G2 Decoding Failed");
                e.printStackTrace();
            }
        }
        // Log.e(Tag, G2_Data.defaultUnit);

        if (G2_Data.requiredUnit.equals("NULL"))
            tvG2Unit.setText(G2_Data.defaultUnit);
        else
            tvG2Unit.setText(G2_Data.requiredUnit);

        tvG2Value.setText(String.valueOf(Math.round(G2_Data.value)));
        tvG2Max.setText(String.valueOf(Math.round(G2_Data.maxValue)));

        String sParaName = G2_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG2Config - Gauge no " + G2_Data.guageNo + ", param" +G2_Data.parameter + "label " + G2_Data.label + ", Max : " + G2_Data.rangeMax+ ", min : " + G2_Data.rangeMin);
    }

    public void decodeG3Config() {
        Log.e(Tag, "MEMdecodeG3Config - start");
        if (!G3_Data.config.equals("NA")) {
            JSONObject G3Json = null;
            try {
                G3Json = new JSONObject(G3_Data.config);
                Log.e(Tag,"G3 JSOn: "+ G3Json.toString());
                G3_Data.guageNo = Integer.parseInt(G3Json.getString(keys.GAUGE_NO));
                G3_Data.parameter = G3Json.getString(keys.PARAMETER);
                G3_Data.label = G3Json.getString(keys.LABEL);
                G3_Data.maxValue = Float.parseFloat(G3Json.getString(keys.CLOCKED_MAX));
                G3_Data.rangeMin = Float.parseFloat(G3Json.getString(keys.RANGE_MIN));
                G3_Data.rangeMax = Float.parseFloat(G3Json.getString(keys.RANGE_MAX));
                G3_Data.alarmMin = Float.parseFloat(G3Json.getString(keys.ALARM_MIN));
                G3_Data.alarmMax = Float.parseFloat(G3Json.getString(keys.ALARM_MAX));
                G3_Data.defaultUnit = G3Json.getString(keys.RESET_UNIT);

                G3_Data.requiredUnit = G3Json.getString(keys.REQUIRED_UNIT);
                G3_Data.additionFactor = Float.parseFloat(G3Json.getString(keys.ADDITION_FACTOR));
                G3_Data.multiplicationFactor = Float.parseFloat(G3Json.getString(keys.MULTIPLICATION_FACTOR));
                G3_Data.value = 0;
                //Log.e(Tag, "Data Decoding Success");
                Log.e(Tag, "Addition Factor: " + String.valueOf(G3_Data.additionFactor));
                Log.e(Tag, "Multi Factor: " + String.valueOf(G3_Data.multiplicationFactor));
                Log.e(Tag, "G3 DECODED");

            } catch (JSONException e) {
                Log.e(Tag, "G3 Decoding Failed");
                Log.e(Tag, "Decoded Def Unit: " + G3_Data.defaultUnit);
                e.printStackTrace();
            }
            String sParaName = G3_Data.parameter;
            if(sParaName.equals(Speed))
                ENABLE_MODULE_GPS=1;
            if(sParaName.equals(SoundLevel))
                ENABLE_MODULE_SOUND=1;
            if(sParaName.equals(Acceleration))
                ENABLE_MODULE_ACCELARTION=1;
            if(sParaName.equals(LeanAngle))
                ENABLE_MODULE_LEAN=1;
            if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
                ENABLE_MODULE_BLUETOOTH=1;

            Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG3Config - Gauge no " + G3_Data.guageNo + ", param" +G3_Data.parameter + "label " + G3_Data.label);
        }

        /*
        tvG3Name.setText(G3_Data.parameter);
        tvG3Value.setText(String.valueOf(Math.round(G3_Data.value)));
        tvG3Max.setText(String.valueOf(Math.round(G3_Data.maxValue)));
        */
        ;

    }

    public void decodeG4Config() {
        Log.e(Tag, "MEMdecodeG4Config- start");
        if (!G4_Data.config.equals("NA")) {
            JSONObject G4Json = null;
            try {
                G4Json = new JSONObject(G4_Data.config);
                G4_Data.guageNo = Integer.parseInt(G4Json.getString(keys.GAUGE_NO));
                G4_Data.parameter = G4Json.getString(keys.PARAMETER);
                G4_Data.label = G4Json.getString(keys.LABEL);
                G4_Data.maxValue = Float.parseFloat(G4Json.getString(keys.CLOCKED_MAX));
                G4_Data.rangeMin = Float.parseFloat(G4Json.getString(keys.RANGE_MIN));
                G4_Data.rangeMax = Float.parseFloat(G4Json.getString(keys.RANGE_MAX));
                G4_Data.alarmMin = Float.parseFloat(G4Json.getString(keys.ALARM_MIN));
                G4_Data.alarmMax = Float.parseFloat(G4Json.getString(keys.ALARM_MAX));
                G4_Data.defaultUnit = G4Json.getString(keys.RESET_UNIT);
                G4_Data.requiredUnit = G4Json.getString(keys.REQUIRED_UNIT);
                G4_Data.additionFactor = Float.parseFloat(G4Json.getString(keys.ADDITION_FACTOR));
                G4_Data.multiplicationFactor = Float.parseFloat(G4Json.getString(keys.MULTIPLICATION_FACTOR));
                G4_Data.value = 0;
                //Log.e(Tag, "Data Decoding Success");



            } catch (JSONException e) {
                Log.e(Tag, "G4 Decoding Failed");
                e.printStackTrace();
            }
        }

        String sParaName = G4_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG4Config - Gauge no " + G4_Data.guageNo + ", param" +G4_Data.parameter + "label " + G4_Data.label);



    }

    public void decodeG5Config() {
        Log.e(Tag, "MEMdecodeG5Config- start");
        if (!G5_Data.config.equals("NA")) {
            JSONObject G5Json = null;
            try {
                G5Json = new JSONObject(G5_Data.config);
                G5_Data.guageNo = Integer.parseInt(G5Json.getString(keys.GAUGE_NO));
                G5_Data.parameter = G5Json.getString(keys.PARAMETER);
                G5_Data.label = G5Json.getString(keys.LABEL);
                G5_Data.maxValue = Float.parseFloat(G5Json.getString(keys.CLOCKED_MAX));
                G5_Data.rangeMin = Float.parseFloat(G5Json.getString(keys.RANGE_MIN));
                G5_Data.rangeMax = Float.parseFloat(G5Json.getString(keys.RANGE_MAX));
                G5_Data.alarmMin = Float.parseFloat(G5Json.getString(keys.ALARM_MIN));
                G5_Data.alarmMax = Float.parseFloat(G5Json.getString(keys.ALARM_MAX));
                G5_Data.defaultUnit = G5Json.getString(keys.RESET_UNIT);
                G5_Data.requiredUnit = G5Json.getString(keys.REQUIRED_UNIT);
                G5_Data.additionFactor = Float.parseFloat(G5Json.getString(keys.ADDITION_FACTOR));
                G5_Data.multiplicationFactor = Float.parseFloat(G5Json.getString(keys.MULTIPLICATION_FACTOR));
                G5_Data.value = 0;
                // Log.e(Tag, "Data Decoding Success");

            } catch (JSONException e) {
                Log.e(Tag, "G5 Decoding Failed");
                e.printStackTrace();
            }
        }

        String sParaName = G5_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG5Config - Gauge no " + G5_Data.guageNo + ", param" +G5_Data.parameter + "label " + G5_Data.label);

    }

    public void decodeG6Config() {
        Log.e(Tag, "MEMdecodeG6Config- start");
        if (!G6_Data.config.equals("NA")) {
            JSONObject G6Json = null;
            try {
                G6Json = new JSONObject(G6_Data.config);
                G6_Data.guageNo = Integer.parseInt(G6Json.getString(keys.GAUGE_NO));
                G6_Data.parameter = G6Json.getString(keys.PARAMETER);
                G6_Data.label = G6Json.getString(keys.LABEL);
                G6_Data.maxValue = Float.parseFloat(G6Json.getString(keys.CLOCKED_MAX));
                G6_Data.rangeMin = Float.parseFloat(G6Json.getString(keys.RANGE_MIN));
                G6_Data.rangeMax = Float.parseFloat(G6Json.getString(keys.RANGE_MAX));
                G6_Data.alarmMin = Float.parseFloat(G6Json.getString(keys.ALARM_MIN));
                G6_Data.alarmMax = Float.parseFloat(G6Json.getString(keys.ALARM_MAX));
                G6_Data.defaultUnit = G6Json.getString(keys.RESET_UNIT);
                G6_Data.requiredUnit = G6Json.getString(keys.REQUIRED_UNIT);
                G6_Data.additionFactor = Float.parseFloat(G6Json.getString(keys.ADDITION_FACTOR));
                G6_Data.multiplicationFactor = Float.parseFloat(G6Json.getString(keys.MULTIPLICATION_FACTOR));
                G6_Data.value = 0;
                //Log.e(Tag, "Data Decoding Success");

            } catch (JSONException e) {
                Log.e(Tag, "G6 Decoding Failed");
                e.printStackTrace();
            }
        }

        String sParaName = G6_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG6Config - Gauge no " + G6_Data.guageNo + ", param" +G6_Data.parameter + "label " + G6_Data.label);
    }

    public void decodeG7Config() {
        Log.e(Tag, "MEMdecodeG7Config- start");
        if (!G7_Data.config.equals("NA"))
        {

            JSONObject G7Json = null;
            try {
                G7Json = new JSONObject(G7_Data.config);
                G7_Data.guageNo = Integer.parseInt(G7Json.getString(keys.GAUGE_NO));
                G7_Data.parameter = G7Json.getString(keys.PARAMETER);
                G7_Data.label = G7Json.getString(keys.LABEL);
            /**    G7_Data.maxValue = Float.parseFloat(G7Json.getString(keys.CLOCKED_MAX));
                G7_Data.rangeMin = Float.parseFloat(G7Json.getString(keys.RANGE_MIN));
                G7_Data.rangeMax = Float.parseFloat(G7Json.getString(keys.RANGE_MAX));
                G7_Data.alarmMin = Float.parseFloat(G7Json.getString(keys.ALARM_MIN));
                G7_Data.alarmMax = Float.parseFloat(G7Json.getString(keys.ALARM_MAX));*/
                G7_Data.defaultUnit = G7Json.getString(keys.RESET_UNIT);
                G7_Data.requiredUnit = G7Json.getString(keys.REQUIRED_UNIT);
                G7_Data.additionFactor = Float.parseFloat(G7Json.getString(keys.ADDITION_FACTOR));
                G7_Data.multiplicationFactor = Float.parseFloat(G7Json.getString(keys.MULTIPLICATION_FACTOR));
                G7_Data.value = 0;
                //Log.e(Tag, "Data Decoding Success");

            }
            catch (JSONException e)
            {
                Log.e(Tag, "G7 Decoding Failed");
                e.printStackTrace();
            }
        }

        String sParaName = G7_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG7Config - Gauge no " + G7_Data.guageNo + ", param" +G7_Data.parameter + "label " + G7_Data.label);
    }

    public void decodeG8Config() {
        Log.e(Tag, "MEMdecodeG8Config- start");
        if (!G8_Data.config.equals("NA"))
        {

            JSONObject G8Json = null;
            try {
                G8Json = new JSONObject(G8_Data.config);
                G8_Data.guageNo = Integer.parseInt(G8Json.getString(keys.GAUGE_NO));
                G8_Data.parameter = G8Json.getString(keys.PARAMETER);
                G8_Data.label = G8Json.getString(keys.LABEL);
                /**    G7_Data.maxValue = Float.parseFloat(G7Json.getString(keys.CLOCKED_MAX));
                 G7_Data.rangeMin = Float.parseFloat(G7Json.getString(keys.RANGE_MIN));
                 G7_Data.rangeMax = Float.parseFloat(G7Json.getString(keys.RANGE_MAX));
                 G7_Data.alarmMin = Float.parseFloat(G7Json.getString(keys.ALARM_MIN));
                 G7_Data.alarmMax = Float.parseFloat(G7Json.getString(keys.ALARM_MAX));*/
                G8_Data.defaultUnit = G8Json.getString(keys.RESET_UNIT);
                G8_Data.requiredUnit = G8Json.getString(keys.REQUIRED_UNIT);
                G8_Data.additionFactor = Float.parseFloat(G8Json.getString(keys.ADDITION_FACTOR));
                G8_Data.multiplicationFactor = Float.parseFloat(G8Json.getString(keys.MULTIPLICATION_FACTOR));
                G8_Data.value = 0;
                //Log.e(Tag, "Data Decoding Success");

            }
            catch (JSONException e)
            {
                Log.e(Tag, "G8 Decoding Failed");
                e.printStackTrace();
            }
        }

        String sParaName = G8_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG8Config - Gauge no " + G8_Data.guageNo + ", param" +G8_Data.parameter + "label " + G8_Data.label);
    }

    public void decodeG9Config() {
        Log.e(Tag, "MEMdecodeG9Config- start");
        if (!G9_Data.config.equals("NA"))
        {

            JSONObject G9Json = null;
            try {
                G9Json = new JSONObject(G9_Data.config);
                G9_Data.guageNo = Integer.parseInt(G9Json.getString(keys.GAUGE_NO));
                G9_Data.parameter = G9Json.getString(keys.PARAMETER);
                G9_Data.label = G9Json.getString(keys.LABEL);
                /**    G7_Data.maxValue = Float.parseFloat(G7Json.getString(keys.CLOCKED_MAX));
                 G7_Data.rangeMin = Float.parseFloat(G7Json.getString(keys.RANGE_MIN));
                 G7_Data.rangeMax = Float.parseFloat(G7Json.getString(keys.RANGE_MAX));
                 G7_Data.alarmMin = Float.parseFloat(G7Json.getString(keys.ALARM_MIN));
                 G7_Data.alarmMax = Float.parseFloat(G7Json.getString(keys.ALARM_MAX));*/
                G9_Data.defaultUnit = G9Json.getString(keys.RESET_UNIT);
                G9_Data.requiredUnit = G9Json.getString(keys.REQUIRED_UNIT);
                G9_Data.additionFactor = Float.parseFloat(G9Json.getString(keys.ADDITION_FACTOR));
                G9_Data.multiplicationFactor = Float.parseFloat(G9Json.getString(keys.MULTIPLICATION_FACTOR));
                G9_Data.value = 0;
                //Log.e(Tag, "Data Decoding Success");

            }
            catch (JSONException e)
            {
                Log.e(Tag, "G8 Decoding Failed");
                e.printStackTrace();
            }
        }

        String sParaName = G9_Data.parameter;
        if(sParaName.equals(Speed))
            ENABLE_MODULE_GPS=1;
        if(sParaName.equals(SoundLevel))
            ENABLE_MODULE_SOUND=1;
        if(sParaName.equals(Acceleration))
            ENABLE_MODULE_ACCELARTION=1;
        if(sParaName.equals(LeanAngle))
            ENABLE_MODULE_LEAN=1;
        if( (sParaName.equals(PDRPM)) || (sParaName.equals(PDAmbTemp))  || (sParaName.equals(PDEngineTemp)) || (sParaName.equals(PDSupplyVolts)) || (sParaName.equals(PDV1)) || (sParaName.equals(PDV2))  || (sParaName.equals(PDV3)) )
            ENABLE_MODULE_BLUETOOTH=1;

        Log.e(Tag, "MEMReadStoreDataFromMemory- decodeG9Config - Gauge no " + G9_Data.guageNo + ", param" +G9_Data.parameter + "label " + G9_Data.label);
    }
//memory stuff end


    //display gauges
    public void displayG1()
    {
        String parameter = G1_Data.parameter;
    //    Log.e(Tag, " displayG1 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge1(fVal_SOUND,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case Acceleration:
            {
                showGauge1(acceleration,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case LeanAngle:
            {
                showGauge1(fVal_LEAN,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case EngineLoad:
            {
                showGauge1(fVal_ENGINELOAD,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge1(fVal_ENGINECOOLANTTEMPERATURE,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case FuelPressure:
            {
                showGauge1(fVal_FUELPRESSURE,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case FuelLevel:
            {
                showGauge1(fVal_FUELTANKLEVELINPUT,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case EngineRPM:
            {
                showGauge1(fVal_ENGINERPM,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case Speed:
            {
                showGauge1(fVal_SPEED_GPS,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge1(fVal_INTAKEAIRTEMPERATURE,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge1(fVal_RUNTIMESINCEENGINESTART,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            case EngineOilTemperature:
            {
                showGauge1(fVal_ENGINEOILTEMPERATURE,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
            }   break;
            
            case AmbientTemperature:
            {
                showGauge1(fVal_AMBIENTAIRTEMPERATURE,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case ThrottlePosition:
            {
                showGauge1(fVal_THROTTLEPOSITION,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge1(fVal_CONTROLMODULEVOLTAGE,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case PDRPM:
            {
                showGauge1(fVal_PDRPM,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge1(fVal_PDfSupplyVolts,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case PDAmbTemp:
            {
                showGauge1(fVal_PDfAmbientTemp,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case PDEngineTemp:
            {
                showGauge1(fVal_PDfEngineTemp,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case PDV1:
            {
                showGauge1(fVal_PDfV1,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case PDV2:
            {
                showGauge1(fVal_PDfV2,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }
            case PDV3:
            {
                showGauge1(fVal_PDfV3,(int)G1_Data.rangeMin,(int)G1_Data.rangeMax);
                break;
            }

        }

    }

    public void displayG2() {
        String parameter = G2_Data.parameter;
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge2(fVal_SOUND,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case Acceleration:
            {
                showGauge2(acceleration,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case LeanAngle:
            {
                showGauge2(fVal_LEAN,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case EngineLoad:
            {
                showGauge2(fVal_ENGINELOAD,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge2(fVal_ENGINECOOLANTTEMPERATURE,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case FuelPressure:
            {
                showGauge2(fVal_FUELPRESSURE,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case FuelLevel:
            {
                showGauge2(fVal_FUELTANKLEVELINPUT,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case EngineRPM:
            {
                showGauge2(fVal_ENGINERPM,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case Speed:
            {
                showGauge2(fVal_SPEED_GPS,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge2(fVal_INTAKEAIRTEMPERATURE,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge2(fVal_RUNTIMESINCEENGINESTART,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;
            case EngineOilTemperature:
            {
                showGauge2(fVal_ENGINEOILTEMPERATURE,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
            }   break;

            case AmbientTemperature:
            {
                showGauge2(fVal_AMBIENTAIRTEMPERATURE,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
                break;
            }
            case ThrottlePosition:
            {
                showGauge2(fVal_THROTTLEPOSITION,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge2(fVal_CONTROLMODULEVOLTAGE,(int)G2_Data.rangeMin,(int)G2_Data.rangeMax);
                break;
            }
        }

    }

    public void displayG3()
    {
        String parameter = G3_Data.parameter;
     //   Log.e(Tag, " displayG3 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge3(fVal_SOUND,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case Acceleration:
            {
                showGauge3(acceleration,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case LeanAngle:
            {
                showGauge3(fVal_LEAN,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case EngineLoad:
            {
                showGauge3(fVal_ENGINELOAD,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge3(fVal_ENGINECOOLANTTEMPERATURE,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case FuelPressure:
            {
                showGauge3(fVal_FUELPRESSURE,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case FuelLevel:
            {
                showGauge3(fVal_FUELTANKLEVELINPUT,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case EngineRPM:
            {
                showGauge3(fVal_ENGINERPM,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case Speed:
            {
                showGauge3(fVal_SPEED_GPS,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge3(fVal_INTAKEAIRTEMPERATURE,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge3(fVal_RUNTIMESINCEENGINESTART,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;
            case EngineOilTemperature:
            {
                showGauge3(fVal_ENGINEOILTEMPERATURE,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
            }   break;

            case AmbientTemperature:
            {
                showGauge3(fVal_AMBIENTAIRTEMPERATURE,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case ThrottlePosition:
            {
                showGauge3(fVal_THROTTLEPOSITION,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge3(fVal_CONTROLMODULEVOLTAGE,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case PDRPM:
            {
                showGauge3(fVal_PDRPM,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge3(fVal_PDfSupplyVolts,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case PDAmbTemp:
            {
                showGauge3(fVal_PDfAmbientTemp,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case PDEngineTemp:
            {
                showGauge3(fVal_PDfEngineTemp,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case PDV1:
            {
                showGauge3(fVal_PDfV1,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case PDV2:
            {
                showGauge3(fVal_PDfV2,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            case PDV3:
            {
                showGauge3(fVal_PDfV3,(int)G3_Data.rangeMin,(int)G3_Data.rangeMax, G3_Data.alarmMin, G3_Data.alarmMax, G3_Data.parameter,G3_Data.label,G3_Data.defaultUnit,G3_Data.requiredUnit,G3_Data.additionFactor,G3_Data.multiplicationFactor);
                break;
            }
            
            

        }
        
    }

    public void displayG4() {
        String parameter = G4_Data.parameter;
     //   Log.e(Tag, " displayG4 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge4(fVal_SOUND,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case Acceleration:
            {
                showGauge4(acceleration,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case LeanAngle:
            {
                showGauge4(fVal_LEAN,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case EngineLoad:
            {
                showGauge4(fVal_ENGINELOAD,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge4(fVal_ENGINECOOLANTTEMPERATURE,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case FuelPressure:
            {
                showGauge4(fVal_FUELPRESSURE,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case FuelLevel:
            {
                showGauge4(fVal_FUELTANKLEVELINPUT,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case EngineRPM:
            {
                showGauge4(fVal_ENGINERPM,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case Speed:
            {
                showGauge4(fVal_SPEED_GPS,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge4(fVal_INTAKEAIRTEMPERATURE,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge4(fVal_RUNTIMESINCEENGINESTART,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;
            case EngineOilTemperature:
            {
                showGauge4(fVal_ENGINEOILTEMPERATURE,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
            }   break;

            case AmbientTemperature:
            {
                showGauge4(fVal_AMBIENTAIRTEMPERATURE,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case ThrottlePosition:
            {
                showGauge4(fVal_THROTTLEPOSITION,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge4(fVal_CONTROLMODULEVOLTAGE,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case PDRPM:
            {
                showGauge4(fVal_PDRPM,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge4(fVal_PDfSupplyVolts,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case PDAmbTemp:
            {
                showGauge4(fVal_PDfAmbientTemp,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case PDEngineTemp:
            {
                showGauge4(fVal_PDfEngineTemp,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case PDV1:
            {
                showGauge4(fVal_PDfV1,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case PDV2:
            {
                showGauge4(fVal_PDfV2,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }
            case PDV3:
            {
                showGauge4(fVal_PDfV3,(int)G4_Data.rangeMin,(int)G4_Data.rangeMax, G4_Data.alarmMin, G4_Data.alarmMax, G4_Data.parameter,G4_Data.label,G4_Data.defaultUnit,G4_Data.requiredUnit,G4_Data.additionFactor,G4_Data.multiplicationFactor);
                break;
            }


        }

    }

    public void displayG5() {
        String parameter = G5_Data.parameter;
      //  Log.e(Tag, " displayG5 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge5(fVal_SOUND,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case Acceleration:
            {
                showGauge5(acceleration,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case LeanAngle:
            {
                showGauge5(fVal_LEAN,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case EngineLoad:
            {
                showGauge5(fVal_ENGINELOAD,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);

            }   break;
            case EngineCoolantTemperature:
            {
                showGauge5(fVal_ENGINECOOLANTTEMPERATURE,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case FuelPressure:
            {
                showGauge5(fVal_FUELPRESSURE,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case FuelLevel:
            {
                showGauge5(fVal_FUELTANKLEVELINPUT,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case EngineRPM:
            {
                showGauge5(fVal_ENGINERPM,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case Speed:
            {
                showGauge5(fVal_SPEED_GPS,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge5(fVal_INTAKEAIRTEMPERATURE,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge5(fVal_RUNTIMESINCEENGINESTART,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;
            case EngineOilTemperature:
            {
                showGauge5(fVal_ENGINEOILTEMPERATURE,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
            }   break;

            case AmbientTemperature:
            {
                showGauge5(fVal_AMBIENTAIRTEMPERATURE,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case ThrottlePosition:
            {
                showGauge5(fVal_THROTTLEPOSITION,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge5(fVal_CONTROLMODULEVOLTAGE,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case PDRPM:
            {
                showGauge5(fVal_PDRPM,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge5(fVal_PDfSupplyVolts,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case PDAmbTemp:
            {
                showGauge5(fVal_PDfAmbientTemp,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case PDEngineTemp:
            {
                showGauge5(fVal_PDfEngineTemp,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case PDV1:
            {
                showGauge5(fVal_PDfV1,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case PDV2:
            {
                showGauge5(fVal_PDfV2,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }
            case PDV3:
            {
                showGauge5(fVal_PDfV3,(int)G5_Data.rangeMin,(int)G5_Data.rangeMax, G5_Data.alarmMin, G5_Data.alarmMax, G5_Data.parameter,G5_Data.label,G5_Data.defaultUnit,G5_Data.requiredUnit,G5_Data.additionFactor,G5_Data.multiplicationFactor);
                break;
            }

        }

    }

    public void displayG6() {
        String parameter = G6_Data.parameter;
     //   Log.e(Tag, " displayG6 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge6(fVal_SOUND,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case Acceleration:
            {
                showGauge6(acceleration,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case LeanAngle:
            {
                showGauge6(fVal_LEAN,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case EngineLoad:
            {
                showGauge6(fVal_ENGINELOAD,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge6(fVal_ENGINECOOLANTTEMPERATURE,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case FuelPressure:
            {
                showGauge6(fVal_FUELPRESSURE,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case FuelLevel:
            {
                showGauge6(fVal_FUELTANKLEVELINPUT,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case EngineRPM:
            {
                showGauge6(fVal_ENGINERPM,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case Speed:
            {
                showGauge6(fVal_SPEED_GPS,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge6(fVal_INTAKEAIRTEMPERATURE,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge6(fVal_RUNTIMESINCEENGINESTART,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;
            case EngineOilTemperature:
            {
                showGauge6(fVal_ENGINEOILTEMPERATURE,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
            }   break;

            case AmbientTemperature:
            {
                showGauge6(fVal_AMBIENTAIRTEMPERATURE,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case ThrottlePosition:
            {
                showGauge6(fVal_THROTTLEPOSITION,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge6(fVal_CONTROLMODULEVOLTAGE,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case PDRPM:
            {
                showGauge6(fVal_PDRPM,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge6(fVal_PDfSupplyVolts,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case PDAmbTemp:
            {
                showGauge6(fVal_PDfAmbientTemp,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case PDEngineTemp:
            {
                showGauge6(fVal_PDfEngineTemp,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case PDV1:
            {
                showGauge6(fVal_PDfV1,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case PDV2:
            {

                showGauge6(fVal_PDfV2,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }
            case PDV3:
            {
                showGauge6(fVal_PDfV3,(int)G6_Data.rangeMin,(int)G6_Data.rangeMax, G6_Data.alarmMin, G6_Data.alarmMax, G6_Data.parameter,G6_Data.label,G6_Data.defaultUnit,G6_Data.requiredUnit,G6_Data.additionFactor,G6_Data.multiplicationFactor);
                break;
            }

        }

    }


    public void displayG7() {
        String parameter = G7_Data.parameter;
     //   Log.e(Tag, " displayG7 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge7(fVal_SOUND,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case Acceleration:
            {
                showGauge7(acceleration,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case LeanAngle:
            {
                showGauge7(fVal_LEAN,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case EngineLoad:
            {
                showGauge7(fVal_ENGINELOAD,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge7(fVal_ENGINECOOLANTTEMPERATURE,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case FuelPressure:
            {
                showGauge7(fVal_FUELPRESSURE,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case FuelLevel:
            {
                showGauge7(fVal_FUELTANKLEVELINPUT,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case EngineRPM:
            {
                showGauge7(fVal_ENGINERPM,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case Speed:
            {
                showGauge7(fVal_SPEED_GPS,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge7(fVal_INTAKEAIRTEMPERATURE,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge7(fVal_RUNTIMESINCEENGINESTART,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;
            case EngineOilTemperature:
            {
                showGauge7(fVal_ENGINEOILTEMPERATURE,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
            }   break;

            case AmbientTemperature:
            {
                showGauge7(fVal_AMBIENTAIRTEMPERATURE,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case ThrottlePosition:
            {
                showGauge7(fVal_THROTTLEPOSITION,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge7(fVal_CONTROLMODULEVOLTAGE,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case PDRPM:
            {
                showGauge7(fVal_PDRPM,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge7(fVal_PDfSupplyVolts,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case PDAmbTemp:
            {
                showGauge7(fVal_PDfAmbientTemp,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case PDEngineTemp:
            {
                showGauge7(fVal_PDfEngineTemp,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case PDV1:
            {
                showGauge7(fVal_PDfV1,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case PDV2:
            {
                showGauge7(fVal_PDfV2,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }
            case PDV3:
            {
                showGauge7(fVal_PDfV3,G7_Data.label,G7_Data.defaultUnit,G7_Data.requiredUnit);
                break;
            }

        }

    }

    public void displayG8() {
        String parameter = G8_Data.parameter;
      //  Log.e(Tag, " displayG8 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge8(fVal_SOUND,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case Acceleration:
            {
                showGauge8(acceleration,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case LeanAngle:
            {
                showGauge8(fVal_LEAN,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case EngineLoad:
            {
                showGauge8(fVal_ENGINELOAD,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge8(fVal_ENGINECOOLANTTEMPERATURE,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case FuelPressure:
            {
                showGauge8(fVal_FUELPRESSURE,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case FuelLevel:
            {
                showGauge8(fVal_FUELTANKLEVELINPUT,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case EngineRPM:
            {
                showGauge8(fVal_ENGINERPM,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case Speed:
            {
                showGauge8(fVal_SPEED_GPS,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge8(fVal_INTAKEAIRTEMPERATURE,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge8(fVal_RUNTIMESINCEENGINESTART,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;
            case EngineOilTemperature:
            {
                showGauge8(fVal_ENGINEOILTEMPERATURE,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
            }   break;

            case AmbientTemperature:
            {
                showGauge8(fVal_AMBIENTAIRTEMPERATURE,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case ThrottlePosition:
            {
                showGauge8(fVal_THROTTLEPOSITION,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge8(fVal_CONTROLMODULEVOLTAGE,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case PDRPM:
            {
                showGauge8(fVal_PDRPM,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge8(fVal_PDfSupplyVolts,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case PDAmbTemp:
            {
                showGauge8(fVal_PDfAmbientTemp,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case PDEngineTemp:
            {
                showGauge8(fVal_PDfEngineTemp,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case PDV1:
            {
                showGauge8(fVal_PDfV1,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case PDV2:
            {
                showGauge8(fVal_PDfV2,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }
            case PDV3:
            {
                showGauge8(fVal_PDfV3,G8_Data.label,G8_Data.defaultUnit,G8_Data.requiredUnit);
                break;
            }

        }

    }

    public void displayG9() {
        String parameter = G9_Data.parameter;
      //  Log.e(Tag, " displayG9 - param : "+parameter);
        switch (parameter)
        {
            case SoundLevel:
            {
                showGauge9(fVal_SOUND,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case Acceleration:
            {
                showGauge9(acceleration,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case LeanAngle:
            {
                showGauge9(fVal_LEAN,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case EngineLoad:
            {
                showGauge9(fVal_ENGINELOAD,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case EngineCoolantTemperature:
            {
                showGauge9(fVal_ENGINECOOLANTTEMPERATURE,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case FuelPressure:
            {
                showGauge9(fVal_FUELPRESSURE,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case FuelLevel:
            {
                showGauge9(fVal_FUELTANKLEVELINPUT,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case EngineRPM:
            {
                showGauge9(fVal_ENGINERPM,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case Speed:
            {
                showGauge9(fVal_SPEED_GPS,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case IntakeAirTemperature:
            {
                showGauge9(fVal_INTAKEAIRTEMPERATURE,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case RunTimesinceEngineStart:
            {
                showGauge9(fVal_RUNTIMESINCEENGINESTART,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;
            case EngineOilTemperature:
            {
                showGauge9(fVal_ENGINEOILTEMPERATURE,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
            }   break;

            case AmbientTemperature:
            {
                showGauge9(fVal_AMBIENTAIRTEMPERATURE,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case ThrottlePosition:
            {
                showGauge9(fVal_THROTTLEPOSITION,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case ControlModuleVoltage:
            {
                showGauge9(fVal_CONTROLMODULEVOLTAGE,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case PDRPM:
            {
                showGauge9(fVal_PDRPM,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case PDSupplyVolts:
            {
                showGauge9(fVal_PDfSupplyVolts,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case PDAmbTemp:
            {
                showGauge9(fVal_PDfAmbientTemp,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case PDEngineTemp:
            {
                showGauge9(fVal_PDfEngineTemp,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case PDV1:
            {
                showGauge9(fVal_PDfV1,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case PDV2:
            {
                showGauge9(fVal_PDfV2,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }
            case PDV3:
            {
                showGauge9(fVal_PDfV3,G9_Data.label,G9_Data.defaultUnit,G9_Data.requiredUnit);
                break;
            }

        }

    }



    public void showGauge1(float value,int min,int max) {

     //   Log.e(G1,"value = "+ String.valueOf(value));
     //   Log.e(G1,"MIN = "+ String.valueOf(min));
     //   Log.e(G1,"MAX = "+ String.valueOf(max));

        if(value>iG1Max)
            iG1Max = (int) value;

        tvG1Max.setText(String.valueOf(iG1Max));
        tvG1Value.setText(String.valueOf(Math.round(value)));

        float step = (max - min) / 30;

        if (value == 0)
            layout1.setBackgroundResource(0);
        else if (value <= (1 * step))
            layout1.setBackgroundResource(R.drawable.la);
        else if (value <= (2 * step))
            layout1.setBackgroundResource(R.drawable.lb);
        else if (value <= (3 * step))
            layout1.setBackgroundResource(R.drawable.lc);
        else if (value <= (4 * step))
            layout1.setBackgroundResource(R.drawable.ld);
        else if (value <= (5 * step))
            layout1.setBackgroundResource(R.drawable.le);
        else if (value <= (6 * step))
            layout1.setBackgroundResource(R.drawable.lf);
        else if (value <= (7 * step))
            layout1.setBackgroundResource(R.drawable.lg);
        else if (value <= (8 * step))
            layout1.setBackgroundResource(R.drawable.lh);
        else if (value <= (9 * step))
            layout1.setBackgroundResource(R.drawable.li);
        else if (value <= (10 * step))
            layout1.setBackgroundResource(R.drawable.lj);
        else if (value <= (11 * step))
            layout1.setBackgroundResource(R.drawable.lk);
        else if (value <= (12 * step))
            layout1.setBackgroundResource(R.drawable.ll);
        else if (value <= (13 * step))
            layout1.setBackgroundResource(R.drawable.lm);
        else if (value <= (14 * step))
            layout1.setBackgroundResource(R.drawable.ln);
        else if (value <= (15 * step))
            layout1.setBackgroundResource(R.drawable.lo);
        else if (value <= (16 * step))
            layout1.setBackgroundResource(R.drawable.lp);
        else if (value <= (17 * step))
            layout1.setBackgroundResource(R.drawable.lq);
        else if (value <= (18 * step))
            layout1.setBackgroundResource(R.drawable.lr);
        else if (value <= (19 * step))
            layout1.setBackgroundResource(R.drawable.ls);
        else if (value <= (20 * step))
            layout1.setBackgroundResource(R.drawable.lt);
        else if (value <= (21 * step))
            layout1.setBackgroundResource(R.drawable.lu);
        else if (value <= (22 * step))
            layout1.setBackgroundResource(R.drawable.lv);
        else if (value <= (23 * step))
            layout1.setBackgroundResource(R.drawable.lw);
        else if (value <= (24 * step))
            layout1.setBackgroundResource(R.drawable.lx);
        else if (value <= (25 * step))
            layout1.setBackgroundResource(R.drawable.ly);
        else if (value <= (26 * step))
            layout1.setBackgroundResource(R.drawable.lz);
        else if (value <= (27 * step))
            layout1.setBackgroundResource(R.drawable.lza);
        else if (value <= (28 * step))
            layout1.setBackgroundResource(R.drawable.lzb);
        else if (value <= (29 * step))
            layout1.setBackgroundResource(R.drawable.lzc);
    }

    public void showGauge2(float value,float min,float max) {


        float step = (max - min) / 30;

        if(value>iG2Max)
            iG2Max = (int) value;

     //   Log.e(Tag,"showGauge2, value = "+ String.valueOf(value) +", Stemp" + step+", min" + min+", max" + max);

        tvG2Max.setText(String.valueOf(iG2Max));
        tvG2Value.setText(String.valueOf(Math.round(value)));

        if (value == 0)
            layout2.setBackgroundResource(0);
        else if (value <= (1 * step))
            layout2.setBackgroundResource(R.drawable.ra);
        else if (value <= (2 * step))
            layout2.setBackgroundResource(R.drawable.rb);
        else if (value <= (3 * step))
            layout2.setBackgroundResource(R.drawable.rc);
        else if (value <= (4 * step))
            layout2.setBackgroundResource(R.drawable.rd);
        else if (value <= (5 * step))
            layout2.setBackgroundResource(R.drawable.re);
        else if (value <= (6 * step))
            layout2.setBackgroundResource(R.drawable.rf);
        else if (value <= (7 * step))
            layout2.setBackgroundResource(R.drawable.rg);
        else if (value <= (8 * step))
            layout2.setBackgroundResource(R.drawable.rh);
        else if (value <= (9 * step))
            layout2.setBackgroundResource(R.drawable.ri);
        else if (value <= (10 * step))
            layout2.setBackgroundResource(R.drawable.rj);
        else if (value <= (11 * step))
            layout2.setBackgroundResource(R.drawable.rk);
        else if (value <= (12 * step))
            layout2.setBackgroundResource(R.drawable.rl);
        else if (value <= (13 * step))
            layout2.setBackgroundResource(R.drawable.rm);
        else if (value <= (14 * step))
            layout2.setBackgroundResource(R.drawable.rn);
        else if (value <= (15 * step))
            layout2.setBackgroundResource(R.drawable.ro);
        else if (value <= (16 * step))
            layout2.setBackgroundResource(R.drawable.rp);
        else if (value <= (17 * step))
            layout2.setBackgroundResource(R.drawable.rq);
        else if (value <= (18 * step))
            layout2.setBackgroundResource(R.drawable.rr);
        else if (value <= (19 * step))
            layout2.setBackgroundResource(R.drawable.rs);
        else if (value <= (20 * step))
            layout2.setBackgroundResource(R.drawable.rt);
        else if (value <= (21 * step))
            layout2.setBackgroundResource(R.drawable.ru);
        else if (value <= (22 * step))
            layout2.setBackgroundResource(R.drawable.rv);
        else if (value <= (23 * step))
            layout2.setBackgroundResource(R.drawable.rw);
        else if (value <= (24 * step))
            layout2.setBackgroundResource(R.drawable.rx);
        else if (value <= (25 * step))
            layout2.setBackgroundResource(R.drawable.ry);
        else if (value <= (26 * step))
            layout2.setBackgroundResource(R.drawable.rz);
        else if (value <= (27 * step))
            layout2.setBackgroundResource(R.drawable.rza);
        else if (value <= (28 * step))
            layout2.setBackgroundResource(R.drawable.rzb);
        else if (value <= (29 * step))
            layout2.setBackgroundResource(R.drawable.rzc);

    }

    public void showGauge3(float iGaugeValue,int minValue,int maxValue, float alarmMin,float alarmMax, String sGaugeLegend,String sLabel,String sDefaultUnit, String sRequiredUnit, float addfactor, float multiplyfactor)
    {
        String stvG3Name;

        iGaugeValue = iGaugeValue + addfactor;
        iGaugeValue = iGaugeValue*multiplyfactor;
        
        G3.setStartValue(minValue);
        G3.setEndValue(maxValue);

        if (iGaugeValue > G3Max)
            G3Max = iGaugeValue;

        if(iGaugeValue>alarmMax || iGaugeValue<alarmMin)
        {
            G3.setPointStartColor(-65536);
            G3.setPointEndColor(-65536);
        }
        else
        {
            G3.setPointStartColor(-16711936);
            G3.setPointEndColor(-16711936);
        }

        G3.setValue(iGaugeValue);

        if(!sLabel.equals(keys.sDEFAULTLABEL))
        {
            sGaugeLegend = sLabel;
        }
        else
        {}

        if(!sRequiredUnit.equals("NULL"))
            stvG3Name = sGaugeLegend+ keys.space + "(" + sRequiredUnit + ")";
        else
            stvG3Name = sGaugeLegend+ keys.space + "(" + sDefaultUnit + ")";



        tvG3Value.setText(String.valueOf(iGaugeValue));
        tvG3Name.setText(stvG3Name);
        tvG3Max.setText(ClassGeneralfuncations.getStringForNumber(G3Max));
    }

    public void showGauge4(float iGaugeValue,int minValue,int maxValue, float alarmMin,float alarmMax, String sGaugeLegend,String sLabel,String sDefaultUnit, String sRequiredUnit, float addfactor, float multiplyfactor)
    {
        String stvG4Name;

        iGaugeValue = iGaugeValue + addfactor;
        iGaugeValue = iGaugeValue*multiplyfactor;

        G4.setStartValue(minValue);
        G4.setEndValue(maxValue);

        if (iGaugeValue > G4Max)
            G4Max = iGaugeValue;

        if(iGaugeValue>alarmMax || iGaugeValue<alarmMin)
        {
            G4.setPointStartColor(-65536);
            G4.setPointEndColor(-65536);
        }
        else
        {
            G4.setPointStartColor(-16711936);
            G4.setPointEndColor(-16711936);
        }

        G4.setValue(iGaugeValue);

        if(!sLabel.equals(keys.sDEFAULTLABEL))
        {
            sGaugeLegend = sLabel;
        }
        else
        {}

        if(!sRequiredUnit.equals("NULL"))
            stvG4Name = sGaugeLegend+ keys.space + "(" + sRequiredUnit + ")";
        else
            stvG4Name = sGaugeLegend+ keys.space + "(" + sDefaultUnit + ")";

        tvG4Value.setText(String.valueOf(iGaugeValue));
        tvG4Name.setText(stvG4Name);
        tvG4Max.setText(ClassGeneralfuncations.getStringForNumber(G4Max));
    }

    public void showGauge5(float iGaugeValue,int minValue,int maxValue, float alarmMin,float alarmMax, String sGaugeLegend,String sLabel,String sDefaultUnit, String sRequiredUnit, float addfactor, float multiplyfactor)
    {
        String stvG5Name;

        iGaugeValue = iGaugeValue + addfactor;
        iGaugeValue = iGaugeValue*multiplyfactor;

        G5.setStartValue(minValue);
        G5.setEndValue(maxValue);

        if (iGaugeValue > G5Max)
            G5Max = iGaugeValue;

        if(iGaugeValue>alarmMax || iGaugeValue<alarmMin)
        {
            G5.setPointStartColor(-65536);
            G5.setPointEndColor(-65536);
        }
        else
        {
            G5.setPointStartColor(-16711936);
            G5.setPointEndColor(-16711936);
        }

        G5.setValue(iGaugeValue);

        if(!sLabel.equals(keys.sDEFAULTLABEL))
        {
            sGaugeLegend = sLabel;
        }
        else
        {}

        if(!sRequiredUnit.equals("NULL"))
            stvG5Name = sGaugeLegend+ keys.space + "(" + sRequiredUnit + ")";
        else
            stvG5Name = sGaugeLegend+ keys.space + "(" + sDefaultUnit + ")";

        tvG5Value.setText(String.valueOf(iGaugeValue));
        tvG5Name.setText(stvG5Name);
        tvG5Max.setText(ClassGeneralfuncations.getStringForNumber(G5Max));
    }

    public void showGauge6(float iGaugeValue,int minValue,int maxValue, float alarmMin,float alarmMax, String sGaugeLegend,String sLabel,String sDefaultUnit, String sRequiredUnit, float addfactor, float multiplyfactor)
    {
        String stvG6Name;

        iGaugeValue = iGaugeValue + addfactor;
        iGaugeValue = iGaugeValue*multiplyfactor;

        G6.setStartValue(minValue);
        G6.setEndValue(maxValue);

        if (iGaugeValue > G6Max)
            G6Max = iGaugeValue;

        if(iGaugeValue>alarmMax || iGaugeValue<alarmMin)
        {
            G6.setPointStartColor(-65536);
            G6.setPointEndColor(-65536);
        }
        else
        {
            G6.setPointStartColor(-16711936);
            G6.setPointEndColor(-16711936);
        }

        G6.setValue(iGaugeValue);

        if(!sLabel.equals(keys.sDEFAULTLABEL))
        {
            sGaugeLegend = sLabel;
        }
        else
        {}

        if(!sRequiredUnit.equals("NULL"))
            stvG6Name = sGaugeLegend+ keys.space + "(" + sRequiredUnit + ")";
        else
            stvG6Name = sGaugeLegend+ keys.space + "(" + sDefaultUnit + ")";

        tvG6Value.setText(String.valueOf(iGaugeValue));
        tvG6Name.setText(stvG6Name);
        tvG6Max.setText(ClassGeneralfuncations.getStringForNumber(G6Max));
    }

    @SuppressLint("SetTextI18n")
    public void showGauge7(float value,String sGaugeLegend,String sDefaultUnit, String sRequiredUnit)
    {
        tvG7Name.setText(sGaugeLegend);

        String sUnit="";
     //   if(!sRequiredUnit.equals("NULL"))
     //       sUnit =  keys.space + "(" + sRequiredUnit + ")";
     //   else
      //      sUnit =  keys.space + "(" + sDefaultUnit + ")";


        tvG7Value.setText(String.valueOf(value) +" "+ sUnit);
    }

    @SuppressLint("SetTextI18n")
    public void showGauge8(float value,String sGaugeLegend,String sDefaultUnit, String sRequiredUnit)
    {
        tvG8Name.setText(sGaugeLegend);
        tvG8Value.setText(String.valueOf(value));
    }

    public void showGauge9(float value,String sGaugeLegend,String sDefaultUnit, String sRequiredUnit)
    {
        tvG9Name.setText(sGaugeLegend);
        tvG9Value.setText(String.valueOf(value));
    }



    public void resetValues()
    {
        iNoOfBluetoothSocket = 0;
        iTotal=0;
        iTrue = 0;
        iFalse = 0;
        iCounter = 0;
        iSuccess = 0;
        iFail = 0;
        fspeedGPS = 0;
        myLatitude = 0;
        myLongitude = 0;
        iSoundInDB = 0;
        acceleration = 0;
        dLeaninDeg = 0;
        dLeaninRad = 0;
        G1Max = 0;
        G2Max = 0;
        G3Max = 0;
        G4Max = 0;
        G5Max = 0;
        G6Max = 0;

        fVal_ENGINELOAD = 0;
        fVal_ENGINECOOLANTTEMPERATURE = 0;
        fVal_FUELPRESSURE = 0;
        fVal_ENGINERPM = 0;
        fVal_SPEED_GPS = 0;
        fVal_SPEED_OBD = 0;
        fVal_INTAKEAIRTEMPERATURE = 0;
        fVal_THROTTLEPOSITION = 0;
        fVal_RUNTIMESINCEENGINESTART = 0;
        fVal_FUELTANKLEVELINPUT = 0;
        fVal_AMBIENTAIRTEMPERATURE = 0;
        fVal_ENGINEOILTEMPERATURE = 0;
        fVal_CONTROLMODULEVOLTAGE = 0;

        ConnectedOnce = false;

    }

    public void resetMaxValues()
    {
        tvG1Max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G1Max = 0;iG1Max=0;
                return;
            }
        });

        tvG2Max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G2Max = 0;iG2Max=0;
                return;
            }
        });

        tvG3Max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G3Max = 0;iG3Max=0;
                return;
            }
        });

        tvG4Max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G4Max = 0;iG4Max=0;
                return;
            }
        });

        tvG5Max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G5Max = 0;iG5Max=0;
                return;
            }
        });

        tvG6Max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G6Max = 0;iG6Max=0;
                return;
            }
        });

        tvCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationService.distance=0;

            }
        });
    }



    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {

            try {

                if (iBTConnectionState == 1) {

                    //Enable this when using ProcessResp() method
                    //sTerminal = "";

/*
                    switch (iSendCommandCntr) {
                        case 10:
                            sendMessage(OBD.getCONTROLMODULEVOLTAGE);
                            break;
                        case 20:
                            sendMessage(OBD.getAMBIENTAIRTEMPERATURE);
                            break;
                        case 30:
                            sendMessage(OBD.getENGINECOOLANTTEMPERATURE);
                            break;
                        case 40:
                            sendMessage(OBD.getINTAKEAIRTEMPERATURE);
                            iSendCommandCntr=0;
                            break;

                        default:
                            sendMessage(OBD.getENGINERPM);
                            Thread.sleep(50);
                            sendMessage(OBD.getTHROTTLEPOSITION);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINELOAD);
                            break;
                    }
*/

                    switch (iSendCommandCntr)
                    {
                        case 0:
                            Log.e(BT,"----------------------------------------------");
                            sendMessage(OBD.getCONTROLMODULEVOLTAGE);
                            Thread.sleep(50);
                            sendMessage(OBD.getAMBIENTAIRTEMPERATURE);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINECOOLANTTEMPERATURE);
                            Thread.sleep(50);
                            sendMessage(OBD.getINTAKEAIRTEMPERATURE);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINERPM);
                            Thread.sleep(50);
                            sendMessage(OBD.getTHROTTLEPOSITION);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINELOAD);
                            iSendCommandCntr++;
                            break;
                        case 75:
                            //Every One Minute
                            Log.e(BT,"----------------------------------------------");
                            iSendCommandCntr = 1;
                            sendMessage(OBD.getCONTROLMODULEVOLTAGE);
                            Thread.sleep(50);
                            sendMessage(OBD.getAMBIENTAIRTEMPERATURE);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINECOOLANTTEMPERATURE);
                            Thread.sleep(50);
                            sendMessage(OBD.getINTAKEAIRTEMPERATURE);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINERPM);
                            Thread.sleep(50);
                            sendMessage(OBD.getTHROTTLEPOSITION);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINELOAD);
                            iSendCommandCntr++;
                            break;
                        default:
                            Log.e(BT,"----------------------------------------------");
                            sendMessage(OBD.getENGINERPM);
                            Thread.sleep(50);
                            sendMessage(OBD.getTHROTTLEPOSITION);
                            Thread.sleep(50);
                            sendMessage(OBD.getENGINELOAD);
                            iSendCommandCntr++;
                            break;

                    }



                }
            } catch (Exception e) {
                Log.e(Tag, "Catch of Runnable");
            }
            mHandler.postDelayed(mRunnable, 200);
        }

    };


    //interrupt routines
    private Runnable interruptRoutineGetSoundValue = new Runnable() {

        @Override
        public void run() {
           fVal_SOUND = SoundGetValue();
            mHandler.postDelayed(interruptRoutineGetSoundValue, 250);
        }

    };


    private Runnable interruptRoutineGetLeanValue = new Runnable() {

        @Override
        public void run() {
            fVal_LEAN= GetLeanFalue();
            mHandler.postDelayed(interruptRoutineGetLeanValue, 250);
        }

    };

    private Runnable interruptRoutineUpdateLocationPara = new Runnable() {

        @Override
        public void run() {
          //  Log.e(Tag, "interruptRoutineUpdateLocationPara, value = " + fVal_SPEED_GPS);
            tvCurrentLocation.setText(sValLocation + ", ODO : " + fValGPSDistance + " KM");
            mHandler.postDelayed(interruptRoutineUpdateLocationPara, 100);
            // mHandler.postDelayed(interruptRoutineGetSoundValue, 250);
        }

    };

    private Runnable interruptRoutineUpdateScreenFromglobalVariables = new Runnable() {

        @Override
        public void run() {
            //updatescreen
           // Log.e(Tag, "int update screen");

            displayG1();
            displayG2();
            displayG3();
            displayG4();
            displayG5();
            displayG6();
            displayG7();
            displayG8();
            displayG9();
         //   Log.e(Tag, "updating locaiton");
         //   tvCurrentLocation.setText(sValLocation);
            mHandler.postDelayed(interruptRoutineUpdateScreenFromglobalVariables, 100);
        }

    };


    private Runnable interruptRoutineDecodePktRecFromProjectDHardware = new Runnable() {

        @Override
        public void run() {

            String sTempPacket = sReceivedPacket;
            sReceivedPacket="";
       //     Log.e(Tag, " Runnable - update ui with packet " + sTempPacket + " Lenght = " + sTempPacket.length());
            if(sTempPacket.length()==58)
            {
                int fTempVariable=0;
                //here

           //     Log.e(Tag, " runnable - update ui, valid packet received " );
                int iCntr=0;
                int iStartIndex =(iCntr*8)+ 1;
                int iEndIndex = (iCntr*8)+ 9;
                String sTemp = sTempPacket.substring(iStartIndex,iEndIndex);
                fVal_PDRPM = convertHexToFloat(sTemp);
                fTempVariable = (int) fVal_PDRPM;
                fVal_PDRPM = fTempVariable;


                iCntr++;
                iStartIndex =(iCntr*8)+ 1;
                iEndIndex = (iCntr*8)+ 9;
                sTemp = sTempPacket.substring(iStartIndex,iEndIndex);
                fVal_PDfSupplyVolts = convertHexToFloat(sTemp);
                fTempVariable = (int) (fVal_PDfSupplyVolts*10);
                fVal_PDfSupplyVolts = ((float)fTempVariable)/10;


                iCntr++;
                iStartIndex =(iCntr*8)+ 1;
                iEndIndex = (iCntr*8)+ 9;
                sTemp = sTempPacket.substring(iStartIndex,iEndIndex);
                fVal_PDfAmbientTemp = convertHexToFloat(sTemp);
                fTempVariable = (int) (fVal_PDfAmbientTemp*10);
                fVal_PDfAmbientTemp = ((float)fTempVariable)/10;


                iCntr++;
                iStartIndex =(iCntr*8)+ 1;
                iEndIndex = (iCntr*8)+ 9;
                sTemp = sTempPacket.substring(iStartIndex,iEndIndex);
                fVal_PDfEngineTemp = convertHexToFloat(sTemp);
                fTempVariable = (int) (fVal_PDfEngineTemp*10);
                fVal_PDfEngineTemp = ((float)fTempVariable)/10;


                iCntr++;
                iStartIndex =(iCntr*8)+ 1;
                iEndIndex = (iCntr*8)+ 9;
                sTemp = sTempPacket.substring(iStartIndex,iEndIndex);
                fVal_PDfV1 = convertHexToFloat(sTemp);
                fTempVariable = (int) (fVal_PDfV1*10);
                fVal_PDfV1 = ((float)fTempVariable)/10;

                iCntr++;
                iStartIndex =(iCntr*8)+ 1;
                iEndIndex = (iCntr*8)+ 9;
                sTemp = sTempPacket.substring(iStartIndex,iEndIndex);
                fVal_PDfV2 = convertHexToFloat(sTemp);
                fTempVariable = (int) (fVal_PDfV2*10);
                fVal_PDfV2 = ((float)fTempVariable)/10;


                iCntr++;
                iStartIndex =(iCntr*8)+ 1;
                iEndIndex = (iCntr*8)+ 9;
                sTemp = sTempPacket.substring(iStartIndex,iEndIndex);
                fVal_PDfV3 = convertHexToFloat(sTemp);
                fTempVariable = (int) (fVal_PDfV3*10);
                fVal_PDfV3 = ((float)fTempVariable)/10;

                Log.e(Tag, "  RPM = " + fVal_PDRPM +"  supply volts = " + fVal_PDfSupplyVolts +"  AmbTemp = " + fVal_PDfAmbientTemp + "  engine temp = " + fVal_PDfEngineTemp +"  fv1 = " + fVal_PDfV1 +"  fv2 = " + fVal_PDfV2 +"  fv3 = " + fVal_PDfV3 );
            }
            mHandler.postDelayed(interruptRoutineDecodePktRecFromProjectDHardware, 100);
        }

    };

    public float convertHexToFloat(String sHexString)
    {
        float fvalueTemp=0;
     //   Log.e(Tag, "in hex  = " + sHexString );
        String myString = sHexString.substring(6,8);
        myString += sHexString.substring(4,6);
        myString += sHexString.substring(2,4);
        myString += sHexString.substring(0,2);
       // Log.e(Tag, "hex  = " + myString + " ");
        Long i = Long.parseLong(myString, 16);
        fvalueTemp = Float.intBitsToFloat(i.intValue());
        return fvalueTemp;
    }

    public void startInterruptRoutines()
    {

     //   mHandler.postDelayed(mRunnable, 200);
        mHandler.postDelayed(interruptRoutineDecodePktRecFromProjectDHardware, 1000);
        mHandler.postDelayed(interruptRoutineUpdateScreenFromglobalVariables, 1000);
        mHandler.postDelayed(interruptRoutineUpdateLocationPara, 1000);



    }

    public void stopRunnables()
    {
        mHandler.removeCallbacks(mRunnable);
        mHandler.removeCallbacks(interruptRoutineDecodePktRecFromProjectDHardware);
        mHandler.removeCallbacks(interruptRoutineUpdateScreenFromglobalVariables);
        mHandler.removeCallbacks(interruptRoutineUpdateLocationPara);
    }

    public int getExpectedLength(String command)
    {
        switch(command)
        {
            case sResENGINERPM: return 8;
            case sResCONTROLMODULEVOLTAGE: return 8;
            default: return 6;
        }
    }


    public void resetDataProcessingComponents()
    {
        iRequestCount = 0;
        iSendCommandCntr = 0;
        iPacketCounter = 0;
        alPackets = null;
    }


    //-------------------------------











}

