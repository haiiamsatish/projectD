package com.yantramicrosystems.www.projectd.UserClass;

/**
 * Created by satish on 23-09-2016.
 */
public class ClassGeneralfuncations {


    public static String getStringForNumber(double dNumber)
    {
        int iNum = (int) dNumber;
        double dNum = dNumber - iNum;

        if(dNum==0)
        {
            return (String.format("%01d",(int)dNumber) );
        }
        else
        {
            return (String.format("%.1f", dNumber));
        }

    }

}
