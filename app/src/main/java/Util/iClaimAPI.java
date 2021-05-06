package Util;

import android.app.Application;

public class iClaimAPI extends Application {
    private String username;
    private String userId;
    private Double balance;

    private String ref;

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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}
