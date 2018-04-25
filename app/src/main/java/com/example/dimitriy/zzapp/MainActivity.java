package com.example.dimitriy.zzapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Words> words = new ArrayList<Words>();
    static WordAdapter wordAdapter;
    private Toolbar toolbar;
    MenuItem item1,item2,item3;
    SearchView search=null;
    ListView lv;
    String LOCALE="enRu";
    DatabaseOpenHelper dtbh;
    static SQLiteDatabase data;
    SimpleCursorAdapter sc;
    boolean t=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        dtbh=new DatabaseOpenHelper(this);
        data = dtbh.getReadWritableDatabase();
        wordAdapter=new WordAdapter(this,words);
        Cursor c = data.query(LOCALE,null,null,null,null,null,Database.COLUMN_WORD);
        lv = (ListView) findViewById(R.id.ListWords);
        lv.setAdapter(wordAdapter);
        wordAdapter.notifyDataSetChanged();
       list();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar,menu);
        item1=menu.findItem(R.id.action_search);
        item2=menu.findItem(R.id.switcher);
        item3=menu.findItem(R.id.adder);
        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this,addword.class);
                startActivity(intent);
                return false;
            }
        });
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(t){
                    LOCALE="ruEn";t=false;item2.setTitle("Англо-русский");
                list();
                }else{
                    LOCALE="enRu";t=true;item2.setTitle("Русско-английский");
                    list();
                }

                return false;
            }
        });
        search=(SearchView) item1.getActionView();
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {


                String Upquery=query.toUpperCase();
                    Cursor z=data.query(LOCALE,
                            new String[] {Database.COLUMN_WORD,Database.COLUMN_DESCR,Database.COLUMN_TRANSLATE},
                            "UPPER ("+Database.COLUMN_WORD + ") LIKE ?",
                            new String[] { "%" + Upquery + "%" },
                            null, null, Database.COLUMN_WORD, null);

                    if (z.getCount()>0) {
                        words.clear();
                        wordAdapter.notifyDataSetChanged();
                        z.moveToFirst();
                        do {

                            int idWord = z.getColumnIndex(Database.COLUMN_WORD);
                            int idTrans = z.getColumnIndex(Database.COLUMN_TRANSLATE);
                            int idDescr = z.getColumnIndex(Database.COLUMN_DESCR);
                            words.add(new Words(z.getString(idWord), z.getString(idTrans), z.getString(idDescr)));
                            wordAdapter.notifyDataSetChanged();
                        } while (z.moveToNext());}
                        else {
                        words.clear();
                        wordAdapter.notifyDataSetChanged();
                    }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String Upquery=newText.toUpperCase();
                Cursor z=data.query(LOCALE,
                        new String[] {Database.COLUMN_WORD,Database.COLUMN_DESCR,Database.COLUMN_TRANSLATE,Database.COLUMN_UPPER},
                        Database.COLUMN_UPPER + " LIKE ?",
                        new String[] { "%" + Upquery + "%" },
                        null, null, Database.COLUMN_WORD);

                if (z.getCount()>0){
                    words.clear();
                    wordAdapter.notifyDataSetChanged();
                    z.moveToFirst();
                    do {

                    int idWord = z.getColumnIndex(Database.COLUMN_WORD);
                    int idTrans=z.getColumnIndex(Database.COLUMN_TRANSLATE);
                    int idDescr=z.getColumnIndex(Database.COLUMN_DESCR);
                    words.add(new Words(z.getString(idWord),z.getString(idTrans),z.getString(idDescr)));
                    wordAdapter.notifyDataSetChanged();
                    } while (z.moveToNext());}
                else {
                    words.clear();
                    wordAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        return true;
    }
     void list(){
        words.clear();
        Cursor c = data.query(LOCALE,null,null,null,null,null,Database.COLUMN_WORD);
        lv = (ListView) findViewById(R.id.ListWords);
        lv.setAdapter(wordAdapter);
        c.moveToFirst();
        if (c.moveToFirst()) {
            do {
                int idWord = c.getColumnIndex(Database.COLUMN_WORD);
                int idTrans=c.getColumnIndex(Database.COLUMN_TRANSLATE);
                int idDescr=c.getColumnIndex(Database.COLUMN_DESCR);
                words.add(new Words(c.getString(idWord),c.getString(idTrans),c.getString(idDescr)));
            } while (c.moveToNext());
        } else
            Log.d("mLog","0 rows");
        wordAdapter.notifyDataSetChanged();
        c.close();
    }

}
