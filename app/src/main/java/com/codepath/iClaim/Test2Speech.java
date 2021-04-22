package com.codepath.iClaim;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import Util.iClaimAPI;

public class Test2Speech implements TextToSpeech.OnInitListener {

    private final TextToSpeech tts;

    public Test2Speech(Context context) {
        tts = new TextToSpeech(context, this);
    }

    public void speakBalance(Context context) {

        String toSpeak = "Hi Demo user, your balance is " + 0.0 + " dollars";

        iClaimAPI i = iClaimAPI.getInstance();

        if (i.getUsername() != null) {
            toSpeak = "Hi " + i.getUsername() + ", your balance is " + i.getBalance() + " dollars";
        }

        Toast.makeText(context, toSpeak, Toast.LENGTH_SHORT).show();
        int ret = tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "");
        Log.i("TTS", "speakBalance: " + ret);

    }

    public void speakBill(Context context, String bill, String amount) {

        String toSpeak = "Hi User, let's add you bill";

        iClaimAPI i = iClaimAPI.getInstance();

        if (i.getUsername() != null && bill != null && amount != null ) {
            bill = bill.isEmpty() ? "new" : bill;
            amount = amount.isEmpty() ? "0" : amount;
            toSpeak = "Hi " + i.getUsername() + ", let's add your " + bill + " bill of " + amount + " dollars";
        }

        Toast.makeText(context, toSpeak, Toast.LENGTH_SHORT).show();
        int ret = tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "");
        Log.i("TTS", "speakBill: " + ret);

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            //Setting speech Language
            tts.setLanguage(Locale.ENGLISH);
            tts.setPitch(1);
        } else {
            Log.e("TTS", "onInit: " + status);
        }
    }

    public void done() {
        tts.shutdown();
    }
}
