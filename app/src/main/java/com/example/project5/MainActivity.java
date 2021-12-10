package com.example.project5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends myconstAndFun implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView nav;

    SQLiteDatabase db= null;

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

            case R.id.display_records:
            {
                displayF();
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
        Log.w ("MainActivity", "onSupportNavigateUp is calll");
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
        Log.w ("MainActivity", "this onbackpress is calll");

        AlertDialog.Builder   x= new AlertDialog.Builder ( this );
        x.setMessage ( "DO YOU WANT TO EXIT?" ).setTitle ( "exit application" )

                .setPositiveButton ( "YES_EXIT", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w ("MainActivity", "end");
                        Toast.makeText(getApplicationContext(), "GOODBYE...", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w ("MainActivity", "start");
        Toast.makeText(getApplicationContext(), "WELCOME...", Toast.LENGTH_SHORT).show();

        ActionBar bar = getSupportActionBar ();
        bar.setHomeButtonEnabled ( true );
        bar.setDisplayHomeAsUpEnabled ( true );
        bar.setHomeAsUpIndicator ( R.drawable.ex);


        nav = (BottomNavigationView)findViewById ( R.id.nb );
        nav.setItemIconTintList ( null );
        nav.setOnNavigationItemSelectedListener (this);
        clearSelection(nav);


        requestPermissions ( new String[]
        {
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
        ,perSD );


    db=openOrCreateDatabase (DbName,MODE_PRIVATE,null );
    db.execSQL ( "create table if not exists employees(ID number(10) not null,NAME char(255)not null,sex char(1)not null,Base_Salary float not null,Total_Sales float not null,Commission_Rate float not null,primary key(ID));" );


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

            case R.id.display_records:
            {
                displayF();
                break;
            }
            default:{}
        }

        Log.w ("onNavigationItemSelected", "down");
        return false;
    }
}