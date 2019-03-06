package wikdd.kasapp.helper;

import java.util.Calendar;

/**
 * Created by ubuntu on 7/4/2018.
 */

public class CurrentDate {

    public static Calendar calendar = Calendar.getInstance();
    public  static  int year = calendar.get(Calendar.YEAR);
    public  static  int month = calendar.get(Calendar.MONTH);
    public  static  int day = calendar.get(Calendar.DAY_OF_MONTH);
}
