package com.example.dimitriy.zzapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class addword extends AppCompatActivity {
    Toolbar tb;
    DatabaseOpenHelper dbh;

    EditText word,trans,descr;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addword);
        tb=(Toolbar) findViewById(R.id.mytoolbar);
        word=(EditText) findViewById(R.id.editWord) ;
        trans=(EditText) findViewById(R.id.editTrans);
        descr=(EditText) findViewById(R.id.editDescr);
        btn=(Button) findViewById(R.id.btnAdd);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wrd="",tslt="",dcrpt="",upwrd="";
                wrd=word.getText().toString();
                tslt=trans.getText().toString();
                if((wrd.equals(""))||(tslt.equals(""))){
                    Toast.makeText(addword.this, "Вы не заполнили обязательные поля", Toast.LENGTH_SHORT).show();
                }
                else{
                    wrd=word.getText().toString();
                    tslt=trans.getText().toString();
                    if (descr.getText()==null){
                        dcrpt="";
                    } else{ dcrpt=descr.getText().toString();}
                    upwrd=wrd.toUpperCase();
                    Cursor c= MainActivity.data.query("enRu",new String[]{Database.COLUMN_UPPER,Database.COLUMN_DESCR,Database.COLUMN_WORD,Database.COLUMN_TRANSLATE},Database.COLUMN_UPPER+" = '"+upwrd+"'",null,null,null,null);
                    if (c.getCount()==0){
                        ContentValues encv=new ContentValues();
                        ContentValues rucv=new ContentValues();
                        encv.put(Database.COLUMN_WORD,wrd+"");
                        encv.put(Database.COLUMN_TRANSLATE,tslt+"");
                        encv.put(Database.COLUMN_DESCR,dcrpt+"");
                        encv.put(Database.COLUMN_UPPER,wrd.toUpperCase()+"");
                        MainActivity.data.insert("enRu",null,encv);
                        rucv.put(Database.COLUMN_WORD,tslt+"");
                        rucv.put(Database.COLUMN_TRANSLATE,wrd+"");
                        rucv.put(Database.COLUMN_DESCR,"");
                        rucv.put(Database.COLUMN_UPPER,wrd.toUpperCase()+"");
                        MainActivity.data.insert("ruEn",null,rucv);}
                    else{
                        Toast.makeText(addword.this, "Слово уже есть в базе данных", Toast.LENGTH_SHORT).show();
                    }
                    word.setText("");
                    trans.setText("");
                    descr.setText("");
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(addword.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
