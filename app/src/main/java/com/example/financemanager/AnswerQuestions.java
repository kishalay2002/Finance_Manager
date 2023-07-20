package com.example.financemanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnswerQuestions
{

    private static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";
    public boolean keywordCheck(String msg)
    {
        msg=msg.toLowerCase();

        String keywords[]={"debit","credit","debited","credited","inr","spent","received","amount","amount due","account","accounts","charge","charged","collectible"};

        for (String keyword : keywords)
        {
            if (msg.contains(keyword))
                return true;
        }
        return false;
    }

    public String formatPrompt(String prompt)
    {
        char c=' ';
        String new_prompt="";
        for(int i=0;i<prompt.length();i++)
        {
            c=prompt.charAt(i);
            if(c=='\n')
                continue;

            new_prompt+=c;
        }
        return new_prompt;
    }

    /*
    public static void main(String args[])
    {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        //String jsonBody = "{\"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"Who won the world series in 2020?\"}], \"max_tokens\": 50}";

        String jsonBody="[{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},{\"role\":\"user\",\"content\":\"Output must be in minimum tokens separated by ',' do not print questions type NO if answer not determined Q What is transaction date?? Q was money credit or debit?? print word debit or credit Q what amount was credit or debit?? print only the amount and currency Q what is the account number?? Q What is the name of payer/payee??\"}]";

        JSONObject jo1 = new JSONObject();
        JSONObject jo2 = new JSONObject();
        JSONArray ja = new JSONArray();

        try
        {
            jo1.put("role", "system");
            jo1.put("content", "You are an app your response should be consistent");
            ja.put(jo1);
            jo2.put("role", "user");
            jo2.put("content", "Debit INR 80.00 A/c no. XX0154 29-10-22 02:15:04 U  PI/P2M/230271803244/KIIT Hosp/Yes Bank SMS BLOCKUPI Cust ID to 8691000002, if not you - Axis BankOutput must be in minimum tokens separated by ',' do not print questions type NO if answer not determined Q What is transaction date?? Q was money credit or debit?? print word debit or credit Q what amount was credit or debit?? print only the amount and currency Q what is the account number?? Q What is the name of payer/payee??");
            ja.put(jo2);
        }
        catch (JSONException j1)
        {
            j1.printStackTrace();
        }

        //String msg="Debit INR 80.00 A/c no. XX0154 29-10-22 02:15:04 U  PI/P2M/230271803244/KIIT Hosp/Yes Bank SMS BLOCKUPI Cust ID to 8691000002, if not you - Axis Bank";
        //jsonBody=jsonBody.substring(0,89)+msg+jsonBody.substring(89);
        //System.out.println(jsonBody);

        RequestBody body = RequestBody.create(mediaType, String.valueOf(ja));

        Request request = new Request.Builder().url(API_ENDPOINT).post(body).addHeader("Authorization", "Bearer " + API_KEY).build();

        try
        {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            System.out.println(responseBody);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

    public String[] extractData(String message)
    {
        String resp[] = new String[5];
        char c=' ';
        message+=",";
        StringBuilder temp= new StringBuilder(" ");
        int count=0;
        for(int i=0;i<message.length();i++)
        {
            c=message.charAt(i);

            if(c==',' || c=='\n')
            {
                resp[count]= temp.toString();
                count++;
                temp = new StringBuilder();
            }
            else
            {
                temp.append(c);
            }
        }

        return resp;
    }


}
