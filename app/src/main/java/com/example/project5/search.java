package com.example.project5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class search extends myconstAndFun implements View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnFocusChangeListener{

    private BottomNavigationView nav;

    SQLiteDatabase db= null;

    RecyclerView recyclerView;
    ArrayList<data> as;
    CustomAdapter ca;

    private EditText id;
    private EditText name;

    private Button bs1;
    private Button bs2;

    ImageView ref;
    ImageView del;

    int mood=0;

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
        Log.w ("SearchActivity", "onSupportNavigateUp is calll");
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }
    //
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        id.clearFocus();
        name.clearFocus();
        return super.onKeyDown(keyCode, event);
    }
    //
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed ();
        Log.w ("SearchActivity", "this onbackpress is calll");

        AlertDialog.Builder   x= new AlertDialog.Builder ( this );
        x.setMessage ( "DO YOU WANT TO EXIT?" ).setTitle ( "Exit SearchActivity" )

                .setPositiveButton ( "YES_EXIT", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w ("SearchActivity", "end");
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
        setContentView(R.layout.activity_search);

        Log.w ("SearchActivity", "start");
        Toast.makeText(getApplicationContext(), "SearchActivity...", Toast.LENGTH_SHORT).show();

        ActionBar bar = getSupportActionBar ();
        bar.setHomeButtonEnabled ( true );
        bar.setDisplayHomeAsUpEnabled ( true );
        bar.setHomeAsUpIndicator ( R.drawable.ex);
        bar.setTitle("search for a particular record.");

        nav = (BottomNavigationView)findViewById ( R.id.nb );
        nav.setItemIconTintList ( null );
        nav.setOnNavigationItemSelectedListener (this);
        clearSelection(nav);

        clearSelection(nav);
        final BottomNavigationMenuView menuView;
        menuView = (BottomNavigationMenuView) nav.getChildAt(0);
        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(2);
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

        id = findViewById (R.id.idSearch);
        name = findViewById (R.id.nameSearch);

        id.setOnFocusChangeListener(this); id.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        name.setOnFocusChangeListener(this);

        bs1 = (Button) findViewById (R.id.searchID);
        bs2 = (Button) findViewById (R.id.searchName);

        bs1.setOnClickListener (this);
        bs2.setOnClickListener (this);

        recyclerView = findViewById(R.id.recyclerViewSearch);

        ref = findViewById(R.id.refSearch);
        ref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "ٌٌRefresh Last Search...", Toast.LENGTH_SHORT).show();
                 do_();
            }
        });

        del = findViewById(R.id.delSearch);
        del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "ٌٌClear Search...", Toast.LENGTH_SHORT).show();
                clc();
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.searchID) {
            mood=1;
            do_();
            return;
        }

        if (v.getId() == R.id.searchName) {
            mood=2;
            do_();
            return;
        }
    }

    public void setData()
    {
        try {
            String sql="";
            if(mood==1){
                if(id.getText().toString().length()>10)
                {
                    AlertDialog.Builder x = new AlertDialog.Builder(this);
                    x.setMessage("ID must be from 1 to 10 digits.").setTitle("ERROR")

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
                sql=("select * from employees where ID="+id.getText().toString()+";");
            }
            else
            {
                if(mood==2){
                    System.out.println("hh"+name.getText().toString());
                    sql=("select * from employees where NAME='"+name.getText().toString()+"';");
                }
                else  {sql=("select * from employees;");}// clc ,catch error
            }

            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() == 0) {
                AlertDialog.Builder x = new AlertDialog.Builder(this);
                x.setMessage("does not exist item!!!.").setTitle("Error")

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
            x.setMessage("Unexpected error, please check input.").setTitle("Error")

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
        id.setText(""); name.setText(""); mood=0;
        try {

            if (as == null) {
                as = new ArrayList<>();
                setData();
            }// first run and empty and first click ,catch error
            if (ca == null) {
                ca = new CustomAdapter(search.this, this, as);
            }// first run and empty and first click ,catch error
            ca.clearRV();  ca.clearRV();  ca.clearRV();
            recyclerView.setAdapter(ca);
            recyclerView.setLayoutManager(new LinearLayoutManager(search.this));
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

    public void do_()
    {
        if(mood == 0)return;
        if(((mood==1)&&id.getText().toString().isEmpty())||((mood==2)&&name.getText().toString().isEmpty()))
        {
            AlertDialog.Builder x = new AlertDialog.Builder(this);
            x.setMessage("Please complete fill the form data.").setTitle("incomplete data")

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

        try {
            as = new ArrayList<>();
            setData();
            ca = new CustomAdapter(search.this, this, as);
            recyclerView.setAdapter(ca);
            recyclerView.setLayoutManager(new LinearLayoutManager(search.this));
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(v==id)
        {
            if (!hasFocus) {
                Log.d("focus", "focus lost");
                // Do whatever you want here
            } else {
                Toast.makeText(getApplicationContext(), " Tap outside edittext to lose focus ", Toast.LENGTH_SHORT).show();
                Log.d("focus", "focused");
            }

            return;
        }

        if(v==name)
        {
            if (!hasFocus) {
                Log.d("focus", "focus lost");
                // Do whatever you want here
            } else {
                Toast.makeText(getApplicationContext(), " Tap outside edittext to lose focus ", Toast.LENGTH_SHORT).show();
                Log.d("focus", "focused");
            }

            return;
        }
    }
    //
    // <!-- Clear focus on touch outside for all EditText inputs. "Clear focus input"
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
// "Clear focus input" -->

}