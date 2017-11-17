package vn.altalab.app.crmvietpack;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Preferences {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Shared_Preferences(Context context, String key){
        sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putString(String key, String dulieu){
        editor.putString(key,"" + dulieu);
        editor.commit();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }

}
