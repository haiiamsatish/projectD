package com.yantramicrosystems.www.projectd.UserClass;

/**
 * Created by yantraPaul on 30-Oct-17.
 */

public class OBD {

    public static final String BT = "bluetooth";

    public static final String getATZ = "ATZ\r";
    public static final String getATE0 = "ATE0\r";
    public static final String getENGINELOAD = "0104\r";
    public static final String getENGINECOOLANTTEMPERATURE = "0105\r";
    public static final String getFUELPRESSURE = "010A\r";
    public static final String getENGINERPM = "010C\r";
    public static final String getSPEED = "010D\r";
    public static final String getINTAKEAIRTEMPERATURE = "010F\r";
    public static final String getTHROTTLEPOSITION = "0111\r";
    public static final String getRUNTIMESINCEENGINESTART = "011F\r";
    public static final String getFUELTANKLEVELINPUT = "012F\r";
    public static final String getAMBIENTAIRTEMPERATURE = "0146\r";
    public static final String getENGINEOILTEMPERATURE = "015C\r";
    public static final String getCONTROLMODULEVOLTAGE = "0142\r";

    public static float calcEngineLoad(String hexString)
    {
        int value = hex2decimal(hexString);
        value = (int) (value/(2.55));        //Calculating Actual Engine RPM from response
        return (float) value;

    }

    public static float calcEngineCoolantTemperature(String hexString)
    {
        int value = hex2decimal(hexString);
        value = value-40;
        return (float) value;
    }

    public static float calcFuelPressure(String hexString)
    {
        int value = hex2decimal(hexString);
        value = value*3;
        return (float) value;
    }

    public static float calcEngineRPM(String hexString)
    {
        /*
        int value = hex2decimal(hexString);
        value = value/4;        //Calculating Actual Engine RPM from response
        return (float) value;
        */
        String A = hexString.substring(0,2);
        //Log.e(BT,"A = " + A);
        String B="00";

        if(hexString.length()==4)
            B = hexString.substring(2);

       // Log.e(BT,"B = " + B);
        int iA = hex2decimal(A);
        int iB = hex2decimal(B);
        int value = (256*iA + iB)/4;
        return (float) value;

    }

    public static float calcSpeed(String hexString)
    {
        
        int value = hex2decimal(hexString);
        return (float) value;
    }

    public static float calcIntakeAirTemperature(String hexString)
    {
        
        int value = hex2decimal(hexString);
        value = value-40;
        return (float) value;
    }

    public static float calcThrottlePosition(String hexString)
    {
        
        int value = hex2decimal(hexString);
        value = (int) (value/2.55);
        return (float) value;
    }

    public static float calcRunTimeSinceEngineStart(String hexString)
    {
        String A = hexString.substring(0,2);
       // Log.e(BT,"A = " + A);
        String B="00";

        if(hexString.length()==4)
            B = hexString.substring(2);


        //Log.e(BT,"B = " + B);
        int iA = hex2decimal(A);
        int iB = hex2decimal(B);
        int value = 256*iA + iB;
        return (float) value;
    }

    public static float calcFuelTankLevelInput(String hexString)
    {
        int value = hex2decimal(hexString);
        value = (int) (value/2.55);
        return (float) value;
    }

    public static float calcAmbientAirTemperature(String hexString)
    {
        
        int value = hex2decimal(hexString);
        value = value-40;
        return (float) value;
    }

    public static float calcEngineOilTemperature(String hexString)
    {
        
        int value = hex2decimal(hexString);
        value = value-40;
        return (float) value;
    }

    public static float calcControlModuleVoltage(String hexString)
    {
        
        String A = hexString.substring(0,2);
        //Log.e(BT,"A = " + A);
        String B="00";

        if(hexString.length()==4)
            B = hexString.substring(2);

        //Log.e(BT,"B = " + B);
        int iA = hex2decimal(A);
        int iB = hex2decimal(B);
        int value = (256*iA + iB)/1000;
        return (float) value;
    }

    /*
    public static int hex2decimal(String s)
    {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
    */

    public static int hex2decimal(String hexvalue)
    {
        int intvalue = Integer.parseInt(hexvalue, 16);
        return intvalue;
    }




}
