package com.yantramicrosystems.www.projectd.UserClass;

/**
 * Created by yantraPaul on 22-Aug-17.
 */

public class ClassConversion
{
    public String inttoHex(int intvalue)
    {
        String hexvalue;
        hexvalue = String.format("%8s", Integer.toHexString(intvalue)).replace(' ', '0');
        return hexvalue;
    }

    public String floattoHex(float floatvalue)
    {
        String hexvalue;
        hexvalue = inttoHex(Float.floatToRawIntBits(floatvalue));
        return hexvalue;
    }

    public String hextoInt(String hexvalue)
    {
        String sIntValue;
        int intvalue = Integer.parseInt(hexvalue, 16);
        sIntValue = new Integer(intvalue).toString();
        return sIntValue;
    }

    public float hextoFloat(String hexvalue, int iNoOfDecimal)
    {
        String b0,b1,b2,b3, sLittleValue;
        b0 = hexvalue.substring(0,2);
        b1 = hexvalue.substring(2,4);
        b2 = hexvalue.substring(4,6);
        b3 = hexvalue.substring(4);
        sLittleValue = b3+b2+b1+b0;
        Long i = Long.parseLong(sLittleValue, 16);
        Float f = Float.intBitsToFloat(i.intValue());
        //String sFloatValue = new Float(f).toString();

       /* String formattedString = String.format("%.01f", f);
        if(iNoOfDecimal==0)
        {
            int i = Math.round(f);
            formattedString = Integer.toString(i);
        }
        else if(iNoOfDecimal==1)
            formattedString = String.format("%.01f", f);
        else
            formattedString = String.format("%.02f", f);*/


        return f;

    }

    String convertFloatToString(float fNumber, int iNoOfDecimalPlases)
    {
        if(iNoOfDecimalPlases==2)
            return (String.format("%.02f",fNumber));
        else if(iNoOfDecimalPlases==1)
            return (String.format("%.02f",fNumber));
        else
        {
            return Integer.toString(Math.round(fNumber));
        }
    }

    public int stringtoInt(String s)
    {
        int i = Integer.parseInt(s);
        return i;
    }

    public float stringtoFloat(String s)
    {
        float f= Float.parseFloat(s);
        return f;
    }

    public String swapEndian(String s)
    {
        String b0,b1,b2,b3, sLittleValue;
        b0 = s.substring(0,2);
        b1 = s.substring(2,4);
        b2 = s.substring(4,6);
        b3 = s.substring(4);
        sLittleValue = b3+b2+b1+b0;
        return sLittleValue;
    }


}
