package com.example.financemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatGptApiTask extends AsyncTask<Context, Void, String> {

    private static final String TAG = "ChatGptApiTask";

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    private ChatGptApiListener listener;

    AnswerQuestions ans = new AnswerQuestions();

    //private final String questionTemplate="[{\"role\": \"system\", \"content\": \"You are an app. example output '29-10-22,debit,80.00 INR,XX0154,KIIT Hosp'\"},{\"role\":\"user\",\"content\":\"Output must be in minimum tokens separated by ',' do not print questions type NO if answer not determined Q What is transaction date?? Q was money credit or debit?? print word debit or credit Q what amount was credit or debit?? print only the amount and currency Q what is the account number?? Q What is the name of payer/payee??\"}]";;


    public ChatGptApiTask(ChatGptApiListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Context... contexts)
    {
        MessageDB buff = new MessageDB(contexts[0]);
        String message,result="";
        try
        {
            boolean loop=true;
            while(loop)
            {
                message=buff.criticalSection("",0,2);
                if(message.length()==0)
                {
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    continue;
                }

                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setDoOutput(true);

                //String prompt;
                String prompt="[{\"role\": \"system\", \"content\": \"You are an app. example output '29-10-22,debit,80.00 INR,XX0154,KIIT Hosp'\"},{\"role\":\"user\",\"content\":\""+message+"Output must be in minimum tokens separated by ',' do not print questions type NO if answer not determined Q What is transaction date?? Q was money credit or debit?? print word debit or credit Q what amount was credit or debit?? print only the amount and currency Q what is the account number?? Q What is the name of payer/payee??\"}]";;
                //prompt = questionTemplate.substring(0, 110) + message + questionTemplate.substring(110);
                String jsonInput = "{\"model\": \"gpt-3.5-turbo\",\"messages\":" + prompt + ",\"temperature\" : 1.0,\"top_p\":1.0,\"n\" : 1,\"stream\": false,\"presence_penalty\":0,\"frequency_penalty\":0}";
                Log.d("Query", jsonInput);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(jsonInput);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                }
                else
                {
                    Log.e(TAG, "HTTP error code: " + responseCode);
                }
                publishProgress(result);
                result="";
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error making API request: " + e.getMessage());
        }
        Log.d("Chat GPT",result);
        return null;
    }

    protected final void publishProgress(String... values)
    {
        Log.d("API", "API response: " + values[0]);

        try
        {
            JSONObject resp = new JSONObject(values[0]);
            JSONArray choices = resp.getJSONArray("choices");
            JSONObject c= choices.getJSONObject(0);
            JSONObject message=c.getJSONObject("message");
            String content=message.getString("content");

            String data[]=new String[5];
            data=ans.extractData(content);

            Log.d("Amount",data[2]);
            Log.d("Payee",data[4]);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onApiResult(result);
        }
    }

    public interface ChatGptApiListener {
        void onApiResult(String response);
    }
}
