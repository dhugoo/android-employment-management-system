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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class modify extends myconstAndFun implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnFocusChangeListener {

    private BottomNavigationView nav;

    SQLiteDatabase db= null;
    SQLiteStatement prep=null;

    private EditText id_;
    private EditText id;
    private EditText name;
    private EditText base_salary;
    private EditText total_sales;
    private EditText com_rate;

    private RadioGroup rg_;
    private RadioButton rm;
    private RadioButton rf;

    private TextView l1;
    private TextView l2;
    private TextView l3;
    private TextView l4;
    private TextView l5;
    private TextView l6;

    ImageView ser;
    ImageView del;

    private Button clear;
    private Button update;

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
        Log.w ("ModifyActivity", "onSupportNavigateUp is calll");
        onBackPressed ();
        return super.onSupportNavigateUp ();
    }
    //
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        id_.clearFocus();
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
        Log.w ("ModifyActivity", "this onbackpress is calll");

        AlertDialog.Builder   x= new AlertDialog.Builder ( this );
        x.setMessage ( "DO YOU WANT TO EXIT?" ).setTitle ( "Exit ModifyActivity" )

                .setPositiveButton ( "YES_EXIT", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w ("ModifyActivity", "end");
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
        setContentView(R.layout.activity_modify);

        Log.w ("ModifyActivity", "start");
        Toast.makeText(getApplicationContext(), "ModifyActivity...", Toast.LENGTH_SHORT).show();

        ActionBar bar = getSupportActionBar ();
        bar.setHomeButtonEnabled ( true );
        bar.setDisplayHomeAsUpEnabled ( true );
        bar.setHomeAsUpIndicator ( R.drawable.ex);
        bar.setTitle("modify certain record.");

        nav = (BottomNavigationView)findViewById ( R.id.nb );
        nav.setItemIconTintList ( null );
        nav.setOnNavigationItemSelectedListener (this);
        clearSelection(nav);

        clearSelection(nav);
        final BottomNavigationMenuView menuView;
        menuView = (BottomNavigationMenuView) nav.getChildAt(0);
        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(3);
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
       prep = db.compileStatement("update employees set ID=?, NAME=?,sex=?,Base_Salary=?,Total_Sales=?,Commission_Rate=? where ID=?;");

        l1= findViewById (R.id.modtxt2);
        l2= findViewById (R.id.modtxt3);
        l3= findViewById (R.id.modtxt4);
        l4= findViewById (R.id.modtxt5);
        l5= findViewById (R.id.mod6);
        l6= findViewById (R.id.mod7);

        id_= findViewById (R.id.idMOD);
        id = findViewById (R.id.UPidMOD);
        name = findViewById (R.id.nameUP);
        base_salary = findViewById (R.id.bsUP);
        total_sales = findViewById (R.id.tsUP);
        com_rate = findViewById (R.id.crUP);

        id_.setOnFocusChangeListener(this); id_.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        id.setOnFocusChangeListener(this); id.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        name.setOnFocusChangeListener(this);
        base_salary.setOnFocusChangeListener(this); base_salary.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        total_sales.setOnFocusChangeListener(this); total_sales.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        com_rate.setOnFocusChangeListener(this); com_rate.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));

        rg_ = (RadioGroup) findViewById (R.id.rgUP);
        rm = (RadioButton) findViewById (R.id.rMU);
        rf = (RadioButton) findViewById (R.id.rFU);

        rm.setOnClickListener ( this );
        rf.setOnClickListener ( this );
        rm.setOnCheckedChangeListener ( this );
        rf.setOnCheckedChangeListener ( this );

        update = (Button) findViewById (R.id.updateUP);
        clear = (Button) findViewById (R.id.clearMOD);

        clear.setOnClickListener (this);
        update.setOnClickListener (this);

        ser = findViewById(R.id.serMOD);
        del = findViewById(R.id.delMOD);

        ser.setOnClickListener (this);
        del.setOnClickListener (this);

        cleaR();

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.serMOD) { do_(); return; }

        if (v.getId() == R.id.delMOD) { cleaR(); return; }

        if (v.getId() == R.id.clearMOD) { cleaR_(); return; }

        if (v.getId() == R.id.updateUP) { do_a(); return; }

        if (v.getId() == R.id.bMale) {  rg_.clearCheck(); rf.setSelected(false); rm.setSelected(true); rm.setChecked(true); return;}

        if (v.getId() == R.id.bFemale) { rg_.clearCheck(); rm.setSelected(false); rf.setSelected(true); rf.setChecked(true); return;}

    }

    void do_a()
    {
        try {
            String str= id_.getText().toString();
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
                if(!str1.equals(id_.getText().toString())) {
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
            }

            prep.bindLong(1, Long.parseLong(str1));
            prep.bindString(2, str2);
            prep.bindString(3, (sex_ + ""));
            prep.bindDouble(4, Double.parseDouble(str3));
            prep.bindDouble(5, Double.parseDouble(str4));
            prep.bindDouble(6, Double.parseDouble(str5));
            prep.bindDouble(7, Long.parseLong(str));

            /*String sqq=String.format("update employees set ID=%ld,NAME='%s',sex='%s',Base_Salary=%f,Total_Sales=%f,Commission_Rate=%f where ID=%ld;"
                    ,Long.parseLong(str1),str2,(sex_ + ""),Float.parseFloat(str3),Float.parseFloat(str4),Float.parseFloat(str5),Long.parseLong(str1));*/

            //System.out.println(sqq);
            //String sql="update employees set NAME='"+str2+"' , sex='"+sex_ +"' Base_Salary="+Double.parseDouble(str3)+" where ID="+Long.parseLong(str1)+";";

           // db.execSQL(sqq);

            /*String sql ="UPDATE employees" +
                    "SET column1 = "+str1+" , column2 = '"+str2+ "' , column3 = '"+sex_+"' , column4 = "+str3+", column5 = "+str4+" , column6 = "+str5 +
                    "WHERE ID="+str1+";";*/

            /*String sql ="UPDATE employees" +
                    "SET column1 = "+str1+" , column2 = '"+str2+ "' , column3 = '"+sex_+"' , column4 = "+str3+", column5 = "+str4+" , column6 = "+str5 +
                    "WHERE ID="+str1+";";*/
            // db.execSQL(sql);
            // db.execSQL("update employees set ID="+Long.parseLong(str1)+",NAME='"+str2+"' where ID="+Long.parseLong(str1)+";");
            // db.execSQL("update employees set sex='"+sex_+"', Base_Salary="+Double.parseDouble(str3)+"where ID="+Long.parseLong(str1)+";");
            //   db.execSQL("update employees set Total_Sales="+Double.parseDouble(str4)+",Commission_Rate="+Double.parseDouble(str5)+" where ID="+Long.parseLong(str1)+";");
            // db.execSQL("update employees set NAME='aaaa',Base_Salary=100000 where ID="+str1+";");


            AlertDialog.Builder   x= new AlertDialog.Builder ( this );
            x.setMessage ( "Do you really want to Update this row?" ).setTitle ( "Update row" )

                    .setPositiveButton ( "YES_UPDATE", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prep.execute();
                            id_.setText(str1);
                            Toast.makeText ( getApplicationContext(), "Done Successful Update row.",Toast.LENGTH_SHORT ).show ();
                            prep.clearBindings();
                        }
                    } )

                    .setNegativeButton ( "CANCEL", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {   Toast.makeText ( getApplicationContext(), "Update row canceled.",Toast.LENGTH_SHORT ).show (); prep.clearBindings(); }
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

    void do_()
    {

        try {
            String str1_ = id_.getText().toString();
            if (str1_.isEmpty())
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

            if(str1_.length() >10)
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

            Cursor rs= db.rawQuery("select * from employees where ID="+str1_+";",null);
            if(rs.getCount()<1)
            {
                AlertDialog.Builder x = new AlertDialog.Builder(this);
                x.setMessage("This ID does not exist, The ID must be exist.").setTitle("ERROR")

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

            id.setEnabled(true);
            name.setEnabled(true);
            base_salary .setEnabled(true);
            total_sales.setEnabled(true);
            com_rate.setEnabled(true);
            rg_.setEnabled(true);
            rm.setEnabled(true);
            rf.setEnabled(true);
            clear.setEnabled(true);
            update.setEnabled(true);

            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l4.setVisibility(View.VISIBLE);
            l5.setVisibility(View.VISIBLE);
            l6.setVisibility(View.VISIBLE);

            id.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            base_salary.setVisibility(View.VISIBLE);
            total_sales.setVisibility(View.VISIBLE);
            com_rate.setVisibility(View.VISIBLE);
            rg_.setVisibility(View.VISIBLE);
            rm.setVisibility(View.VISIBLE);
            rf.setVisibility(View.VISIBLE);
            clear.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);

            rs.moveToNext();

            id.setText(""+rs.getLong(0));
            name.setText(rs.getString(1));
            String fr=rs.getString(2);
            if(fr.equals("M")){ rg_.clearCheck(); rf.setSelected(false); rm.setSelected(true); rm.setChecked(true);  rm.callOnClick(); sex_='M';}
            else if(fr.equals("F")){rg_.clearCheck(); rm.setSelected(false); rf.setSelected(true); rf.setChecked(true);  rf.callOnClick(); sex_='F';}
            base_salary.setText(""+rs.getFloat(3));
            total_sales.setText(""+rs.getFloat(4));
            com_rate.setText(""+rs.getFloat(5));


            Toast.makeText ( this, "Enabled True.",Toast.LENGTH_SHORT ).show ();
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

    void cleaR_()
    {
        sex_='d';

        rm.setSelected(false); rf.setSelected(false);
        /* rg_.clearFocus();*/ rg_.clearCheck();
        id.setText("");    name.setText("");   base_salary.setText("");   total_sales.setText("");   com_rate.setText("");
    }

    void cleaR()
    {
        id_.setText("");
        cleaR_();

        l1.setVisibility(View.INVISIBLE);
        l2.setVisibility(View.INVISIBLE);
        l3.setVisibility(View.INVISIBLE);
        l4.setVisibility(View.INVISIBLE);
        l5.setVisibility(View.INVISIBLE);
        l6.setVisibility(View.INVISIBLE);

        id.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        base_salary.setVisibility(View.INVISIBLE);
        total_sales.setVisibility(View.INVISIBLE);
        com_rate.setVisibility(View.INVISIBLE);
        rg_.setVisibility(View.INVISIBLE);
        rm.setVisibility(View.INVISIBLE);
        rf.setVisibility(View.INVISIBLE);
        clear.setVisibility(View.INVISIBLE);
        update.setVisibility(View.INVISIBLE);

        id.setEnabled(false);
        name.setEnabled(false);
        base_salary .setEnabled(false);
        total_sales.setEnabled(false);
        com_rate.setEnabled(false);
        rg_.setEnabled(false);
        rm.setEnabled(false);
        rf.setEnabled(false);
        clear.setEnabled(false);
        update.setEnabled(false);

        Toast.makeText ( this, "Enabled False.",Toast.LENGTH_SHORT ).show ();
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(v==id_)
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