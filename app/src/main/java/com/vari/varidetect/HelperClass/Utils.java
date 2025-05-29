package com.vari.varidetect.HelperClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getCurrentFormattedDate(){
        // Get current date
        Date currentDate = new Date();

        // Define formate: "MMM dd, yyyy" => Apr 24, 2025
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

        //Return formatted date
        return sdf.format(currentDate);
    }
}
