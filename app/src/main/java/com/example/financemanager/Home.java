package com.example.financemanager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment
{
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor myEdit;

    private  Button incomeBtn,expenseBtn,balanceBtn;
    private ImageView reload;

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("Expense_Manager_Usr_Data",Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        if(!sharedPreferences.contains("Income"))
            setData("Income",0.0F);

        if(!sharedPreferences.contains("Expense"))
            setData("Expense", 0.0F);

        if(!sharedPreferences.contains("Currency"))
            setData("Currency","USD");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        incomeBtn = (Button) view.findViewById(R.id.income);
        expenseBtn = (Button)view.findViewById(R.id.expense);
        balanceBtn = view.findViewById(R.id.balance);
        reload= view.findViewById(R.id.reloadMsg);

        incomeBtn.setText("Income\n"+getData("Income"));
        expenseBtn.setText("Expense\n"+getData("Expense"));
        balanceBtn.setText("Balance: "+(getData("Income")-getData("Expemse")));

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("Button","Clicked");
                requireActivity().startService(new Intent(getContext(),messageReader.class));
            }
        });
        return view;
    }


    public void setData(String key, float data)
    {
        myEdit.putFloat(key,data);
        myEdit.commit();
    }

    public void setData(String key, String data)
    {
        myEdit.putString(key,data);
        myEdit.commit();
    }

    public float getData(String key)
    {
        return sharedPreferences.getFloat(key,0.0F);
    }
}