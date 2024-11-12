package com.ib.imagebord_test.misc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class forTests {
    public static void main(String[] args){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        //date.setTime(2000L);
        DateFormat dateFormat = new SimpleDateFormat("d.M.yyyy H:m:s");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate=dateFormat.format(date);
        System.out.println(formattedDate);
    }
}
