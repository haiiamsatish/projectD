package com.yantramicrosystems.www.projectd.UserClass;

/**
 * Created by yantraPaul on 26-Sep-17.
 */

public class Class_GaugeData
{
    public String config;
    public int guageNo = 0;
    public String parameter = "";
    public String label = "";
    public float rangeMax = 0;
    public float rangeMin = 0;
    public float alarmMax = 0;
    public float alarmMin = 0;
    public String defaultUnit = "";
    public String requiredUnit = "";
    public float additionFactor = 0;
    public float multiplicationFactor = 0;
    public float maxValue = 0;
    public float value = 0;
    public int guageType = 0;

    public int checkforNullConfig()
    {

        int i = 0;
        if(config.equals("NA"))
            i = 1;
        return i;
    }
}
