package com.example.project5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
public class display extends myconstAndFun implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView nav;

    SQLiteDatabase db= null;

    RecyclerView recyclerView;
    ArrayList<data> as;
    CustomAdapter ca;

    ImageView ref;
    ImageView del;

    //
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inf=getMenuInflater ();
        inf.inflate (R.menu.menu,menu);
        if (menu!=null && menu instanceof MenuBuilder)
            ((MenuBuilder)menu).setOptionalIconsVisible ( true );
        return super.onCreateOptionsMenu ( menu );
    }
    //
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) { return super.onPrepareOptionsMenu ( menu ); }
    //
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) { return super.onMenuOpened ( featureId, menu ); }
    //
    @Override
    public void onOptionsMenuClosed(Menu menu) { super.onOptionsMenuClosed ( menu ); }
    //
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        //getSupportActionBar ().setTitle ( item.getTitle ()+ "  is pressed" );
        switch(item.getItemId())
        {
            case R.id.insert_records:
            {
                insertF();
                break;
            }

            case R.id.delete_records:
            {
                deleteF();
                break;
            }
            case R.id.search_records:
            {
                searchF();
                break;
            }

            case R.id.modify_records:
            {
                modifyF();
                break;
            }

            default:{}
        }
        return super.onOptionsItemSelected ( item );
    }
    //
    @Override
    public boolean onSupportNavigateUp()
    {
        Log.w ("DisplayActivity", "onSupportNavigateUp is calll");
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }
    //
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return super.onKeyDown(keyCode, event);
    }
    //
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed ();
        Log.w ("DisplayActivity", "this onbackpress is calll");

        AlertDialog.Builder   x= new AlertDialog.Builder ( this );
        x.setMessage ( "DO YOU WANT TO EXIT?" ).setTitle ( "Exit DisplayActivity" )

                .setPositiveButton ( "YES_EXIT", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w ("DisplayActivity", "end");
                        Toast.makeText(getApplicationContext(), "Back...", Toast.LENGTH_SHORT).show();
                        db.close();
                        finish();
                    }
                } )

                .setNegativeButton ( "CANCEL", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })

                .setIcon(R.drawable.qus)
                .setPositiveButtonIcon (getDrawable ( R.drawable.yes))
                .setNegativeButtonIcon(getDrawable ( R.drawable.no))
                .show ();
        return;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Log.w ("DisplayActivity", "start");
        Toast.makeText(getApplicationContext(), "DisplayActivity...", Toast.LENGTH_SHORT).show();

        ActionBar bar = getSupportActionBar ();
        bar.setHomeButtonEnabled ( true );
        bar.setDisplayHomeAsUpEnabled ( true );
        bar.setHomeAsUpIndicator ( R.drawable.ex);
        bar.setTitle("display records.");

        nav = (BottomNavigationView)findViewById ( R.id.nb );
        nav.setItemIconTintList ( null );
        nav.setOnNavigationItemSelectedListener (this);
        clearSelection(nav);

        clearSelection(nav);
        final BottomNavigationMenuView menuView;
        menuView = (BottomNavigationMenuView) nav.getChildAt(0);
        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(4);
        item.setChecked(true);

        db=openOrCreateDatabase (DbName,MODE_PRIVATE,null );
        if(db==null)
        {
            AlertDialog.Builder x = new AlertDialog.Builder(this);
            x.setMessage("No DataBase.").setTitle("Error")

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })

                    .setIcon(R.drawable.goo)
                    .setPositiveButtonIcon(getDrawable(R.drawable.yes))

                    .show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);

        ref = findViewById(R.id.refDisplay);
        ref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "ٌٌRefresh Display...", Toast.LENGTH_SHORT).show();
                print();
            }
        });

        del = findViewById(R.id.delDisplay);
        del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "ٌٌClear Display...", Toast.LENGTH_SHORT).show();
                clc();
            }
        });

        print();
    }


    public void setData()
    {
        try {
            Cursor cursor = db.rawQuery("select * from employees;", null);
            if (cursor.getCount() == 0) {
                AlertDialog.Builder x = new AlertDialog.Builder(this);
                x.setMessage("Empty DataBase, please fill DataBase.").setTitle("Empty")

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })

                        .setIcon(R.drawable.goo)
                        .setPositiveButtonIcon(getDrawable(R.drawable.yes))

                        .show();
            } else {
                while (cursor.moveToNext()) {
                    data d = new data();
                    d.setD_id(cursor.getLong(0));
                    d.setD_name(cursor.getString(1));
                    d.setD_sex(cursor.getString(2));
                    d.setD_salary(cursor.getFloat(3));
                    d.setD_sales(cursor.getFloat(4));
                    d.setD_rate(cursor.getFloat(5));
                    as.add(d);
                }
            }
        }
        catch (Exception e)
        {
            AlertDialog.Builder x = new AlertDialog.Builder(this);
            x.setMessage("Unexpected error.").setTitle("Error")

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })

                    .setIcon(R.drawable.goo)
                    .setPositiveButtonIcon(getDrawable(R.drawable.yes))

                    .show();
            return;
        }

    }

    void print(){
        try {
            as = new ArrayList<>();
            setData();
            ca = new CustomAdapter(display.this, this, as);
            recyclerView.setAdapter(ca);
            recyclerView.setLayoutManager(new LinearLayoutManager(display.this));
        }
        catch (Exception e)
        {
            AlertDialog.Builder x = new AlertDialog.Builder(this);
            x.setMessage("Unexpected error.").setTitle("Error")

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })

                    .setIcon(R.drawable.goo)
                    .setPositiveButtonIcon(getDrawable(R.drawable.yes))

                    .show();
            return;
        }

    }

    void clc()
    {
        try {

            if (as == null) {
                as = new ArrayList<>();
                setData();
            }// first run and empty and first click ,catch error
            if (ca == null) {
                ca = new CustomAdapter(display.this, this, as);
            }// first run and empty and first click ,catch error
            ca.clearRV(); ca.clearRV(); ca.clearRV();
            recyclerView.setAdapter(ca);
            recyclerView.setLayoutManager(new LinearLayoutManager(display.this));

        }
        catch (Exception e)
        {
            AlertDialog.Builder x = new AlertDialog.Builder(this);
            x.setMessage("Unexpected error.").setTitle("Error")

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })

                    .setIcon(R.drawable.goo)
                    .setPositiveButtonIcon(getDrawable(R.drawable.yes))

                    .show();
            return;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.w ("onNavigationItemSelected", "up");

        switch(item.getItemId())
        {
            case R.id.insert_records:
            {
                insertF();
                break;
            }

            case R.id.delete_records:
            {
                deleteF();
                break;
            }
            case R.id.search_records:
            {
                searchF();
                break;
            }

            case R.id.modify_records:
            {
                modifyF();
                break;
            }

            default:{}
        }

        Log.w ("onNavigationItemSelected", "down");
        return false;
    }
}