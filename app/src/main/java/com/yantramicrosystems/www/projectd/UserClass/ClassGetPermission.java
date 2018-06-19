package com.yantramicrosystems.www.projectd.UserClass;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


public class ClassGetPermission
{
    public static final int PERMISSION_ALL = 1;
    public int PERMISSIN_DENIED_BY_USER = 0;


    public void GetUserPermissions(Activity activity)
    {
        String[] PERMISSIONS =  {
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.BLUETOOTH,
                                    Manifest.permission.BLUETOOTH_ADMIN,
                                    Manifest.permission.BLUETOOTH_PRIVILEGED,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION


                                };

        ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);

    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_ALL:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                }
                else
                {
                    PERMISSIN_DENIED_BY_USER = 1;
                }
                return;
            }
        }
    }


}
