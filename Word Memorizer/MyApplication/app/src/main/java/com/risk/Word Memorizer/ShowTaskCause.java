package com.risk.dolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ASUS on 8/11/2017.
 */

public class ShowTaskCause extends AppCompatActivity{

    private TextView tvWord,tvMeaning;
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtaskcause);
        count+=1;

        Intent in= getIntent();
        //Bundle b = in.getExtras();

        //if(b!=null){
            //String word = (String)b.get("word");
            //String meaning = (String)b.get("meaning");
        //}
        String word = in.getStringExtra("word");
        String meaning = in.getStringExtra("meaning");
        tvWord = (TextView) findViewById(R.id.tvWord);
        tvMeaning = (TextView) findViewById(R.id.tvMeaning);
        tvWord.setText(word);
        tvMeaning.setText(meaning);
    }
}
