package Util;

import android.app.Application;

public class iClaimAPI extends Application {
    private String username;
    private String userId;

    private static iClaimAPI instance;

    public static iClaimAPI getInstance()
    {
        if(instance==null)
            instance=new iClaimAPI();
        return instance;
    }
    public iClaimAPI(){}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
