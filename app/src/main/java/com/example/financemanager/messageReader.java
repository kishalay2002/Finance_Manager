package com.example.financemanager;

import android.app.Service;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class messageReader extends Service
{


    private Handler handler = new Handler();
    private boolean isServiceRunning = false;
    private static final int READ_SMS_PERMISSION_CODE = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("Service","Started");
        if (!isServiceRunning)
        {

            isServiceRunning = true;
            readMessagesInBackground();
        }
        return START_STICKY;
    }

    private void readMessagesInBackground()
    {


        MessageDB buff = new MessageDB(this);
        ChatGptApiTask chatApiTask = new ChatGptApiTask(new ChatGptApiTask.ChatGptApiListener() {
            @Override
            public void onApiResult(String response) {
                // Handle the API response here
                Log.d("API", "API response: " + response);
                // Process the response as needed
            }
        });

        chatApiTask.execute(this);

        handler.post(new Runnable()
        {
            @Override

            public void run()
            {
                Log.d("Entered","Run");
                Uri smsUri = Uri.parse("content://sms/inbox");
                Cursor cursor = getContentResolver().query(smsUri, null, null, null, null);
                String prompt = "";

                AnswerQuestions ans = new AnswerQuestions();


                int count=0;


                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            long unixTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
                            String message = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));


                            if (ans.keywordCheck(message)) {
                                //
                                //
                                message=ans.formatPrompt(message);
                                buff.criticalSection(message,unixTimestamp,1);
                                Log.d("Message",message);
                                count++;
                            }
                            // Process the sender and message as per your requirements
                        } while (count<1 && cursor.moveToNext());
                    }
                    cursor.close();
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        isServiceRunning = false;
        handler.removeCallbacksAndMessages(null);
    }
}
