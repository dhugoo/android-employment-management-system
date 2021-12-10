package com.example.project5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class insert extends myconstAndFun implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnFocusChangeListener {

    private BottomNavigationView nav;

    SQLiteDatabase db= null;
    SQLiteStatement prep=null;

    private EditText id;
    private EditText name;
    private EditText base_salary;
    private EditText total_sales;
    private EditText com_rate;

    private RadioGroup rg_;
    private RadioButton rm;
    private RadioButton rf;

    private Button clear;
    private Button insert_do;

    char sex_='d';

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
        Log.w ("InsertActivity", "onSupportNavigateUp is calll");
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }
    //
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        id.clearFocus();
        name.clearFocus();
        base_salary.clearFocus();
        total_sales.clearFocus();
        com_rate.clearFocus();

        return super.onKeyDown(keyCode, event);
    }
    //
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed ();
        Log.w ("InsertActivity", "this onbackpress is calll");

        AlertDialog.Builder   x= new AlertDialog.Builder ( this );
        x.setMessage ( "DO YOU WANT TO EXIT?" ).setTitle ( "Exit InsertActivity" )

                .setPositiveButton ( "YES_EXIT", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w ("InsertActivity", "end");
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
        setContentView(R.layout.activity_insert);

        Log.w ("InsertActivity", "start");
        Toast.makeText(getApplicationContext(), "InsertActivity...", Toast.LENGTH_SHORT).show();

        ActionBar bar = getSupportActionBar ();
        bar.setHomeButtonEnabled ( true );
        bar.setDisplayHomeAsUpEnabled ( true );
        bar.setHomeAsUpIndicator ( R.drawable.ex);
        bar.setTitle("insert new records.");

        nav = (BottomNavigationView)findViewById ( R.id.nb );
        nav.setItemIconTintList ( null );
        nav.setOnNavigationItemSelectedListener (this);
        clearSelection(nav);

        clearSelection(nav);
        final BottomNavigationMenuView menuView;
        menuView = (BottomNavigationMenuView) nav.getChildAt(0);
        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(0);
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
        prep=db.compileStatement ( "insert into employees values (?,?,?,?,?,?);" );

        id = findViewById (R.id.id_insert);
        name = findViewById (R.id.name_insert);
        base_salary = findViewById (R.id.bs_insert);
        total_sales = findViewById (R.id.ts_insert);
        com_rate = findViewById (R.id.cr_insert);

        id.setOnFocusChangeListener(this); id.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        name.setOnFocusChangeListener(this);
        base_salary.setOnFocusChangeListener(this); com_rate.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        total_sales.setOnFocusChangeListener(this); com_rate.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        com_rate.setOnFocusChangeListener(this); com_rate.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));

        rg_ = (RadioGroup) findViewById (R.id.rg);
        rm = (RadioButton) findViewById (R.id.bMale);
        rf = (RadioButton) findViewById (R.id.bFemale);

        rm.setOnClickListener ( this );
        rf.setOnClickListener ( this );
        rm.setOnCheckedChangeListener ( this );
        rf.setOnCheckedChangeListener ( this );

        insert_do = (Button) findViewById (R.id.insertInsert);
        clear = (Button) findViewById (R.id.clearInsert);

        clear.setOnClickListener (this);
        insert_do.setOnClickListener (this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.clearInsert) { cleaR(); return; }

        if (v.getId() == R.id.insertInsert) { do_(); return; }

        if (v.getId() == R.id.bMale) { rm.setSelected(true); return;}

        if (v.getId() == R.id.bFemale) { rf.setSelected(true); return;}

    }

    void cleaR()
    {
        sex_='d';

        rm.setSelected(false); rf.setSelected(false);
        /* rg_.clearFocus();*/ rg_.clearCheck();
        id.setText("");    name.setText("");   base_salary.setText("");   total_sales.setText("");   com_rate.setText("");
    }

    void do_()
    {
        try {
            String str1 = id.getText().toString();
            String str2 = name.getText().toString();
            String str3 = base_salary.getText().toString();
            String str4 = total_sales.getText().toString();
            String str5 = com_rate.getText().toString();

            if (str1.isEmpty()
                    || str2.isEmpty()
                    || str3.isEmpty()
                    || str4.isEmpty()
                    || str5.isEmpty()
                    || sex_ == 'd') {
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

            if(str1.length() >10)
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

            Cursor rs= db.rawQuery("select * from employees where ID="+str1+";",null);
            if(rs.getCount()>=1)
            {
                AlertDialog.Builder x = new AlertDialog.Builder(this);
                x.setMessage("This ID is use, The ID must be Unique.").setTitle("ERROR")

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

            prep.bindLong(1, Long.parseLong(str1));
            prep.bindString(2, str2);
            prep.bindString(3, (sex_ + ""));
            prep.bindDouble(4, Double.parseDouble(str3));
            prep.bindDouble(5, Double.parseDouble(str4));
            prep.bindDouble(6, Double.parseDouble(str5));

            AlertDialog.Builder   x= new AlertDialog.Builder ( this );
            x.setMessage ( "Do you really want to add new row?" ).setTitle ( "add row" )

                    .setPositiveButton ( "YES_ADD", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prep.execute();
                            Toast.makeText ( getApplicationContext(), "Done Successful new row add.",Toast.LENGTH_SHORT ).show ();
                            prep.clearBindings();
                        }
                    } )

                    .setNegativeButton ( "CANCEL", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText ( getApplicationContext(), "row add canceled.",Toast.LENGTH_SHORT ).show ();
                            prep.clearBindings();
                        }
                    })

                    .setIcon(R.drawable.qus)
                    .setPositiveButtonIcon (getDrawable ( R.drawable.yes))
                    .setNegativeButtonIcon(getDrawable ( R.drawable.no))
                    .show ();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.w ("onNavigationItemSelected", "up");

        switch(item.getItemId())
        {
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


    @Override
    public void onCheckedChanged(CompoundButton View, boolean isChecked) {

        if (View==rm) {
            if (isChecked == true) {
                sex_='M';
            }
            return;
        }

        if (View==rf) {
            if (isChecked == true) {
                sex_='F';
            }
            return;
        }

    }

   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

        if(v==base_salary)
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

        if(v==total_sales)
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

        if(v==com_rate)
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