package vn.altalab.app.crmvietpack;

import java.text.SimpleDateFormat;

public class DATE_CUSTOM {

    public static String getStringDate(String response){
        try {
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(response);
            response = new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
