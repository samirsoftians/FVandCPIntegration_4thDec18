package com.example.currentpositionapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.currentpositionapp.fleetviewandroid.MainForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class VehicleInfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    MyDBHelper dbHelper;
    SQLiteDatabase db;
    ListView infoList;
    String[] arr = null;
    ArrayList<String> vehicles;
    ArrayList<String> date;
    ArrayList<String> time;
    ArrayList<String> location;
    ArrayList<String> lat;
    ArrayList<String> lng;
    ArrayList<String> speed;
    ArrayList<Integer> statusArray;
    String vehDetails;
    final Handler myHandler = new Handler();
    int i=0;
    SharedPreferences pref,preferences;
    int count;
    CardView cardView;
    String s;
    Date currentdate;
    String uname,upswd;
    static ArrayList<String> arrayList=new ArrayList<>();
    String url="http://twtech.in:8080/FleetViewProject/MyServlet";
    private String[] vno;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean isRefreshing=false;
    FloatingActionButton fab;
    TextView usrNm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("VehicleInfoActivity", "In the vehicleinfoactivity");

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_search2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("vehicle", MODE_PRIVATE);
        String dt2 = preferences.getString("nameVehicle", null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleInfoActivity.this,
                        SearchActivity.class));
                finish();
            }
        });

       // mSwipeRefreshLayout.setColorSchemeResources(R.color.black);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setSoundEffectsEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(VehicleInfoActivity.this, "Refreshing data..", Toast.LENGTH_SHORT).show();
                stopService(new Intent(VehicleInfoActivity.this,MyService.class));
                startService(new Intent(VehicleInfoActivity.this,MyService.class));
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);

            }
        });

       // mSwipeRefreshLayout.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        preferences = getSharedPreferences("login", MODE_PRIVATE);
        uname=preferences.getString("name",null);
        upswd=preferences.getString("password",null);

        // sasmple();
        currentdate=new Date();

      /*  final SharedPreferences prefs = getSharedPreferences("vehdetails", MODE_PRIVATE);
        s = prefs.getString("veh",null);
        //String stoppedsince=datetime-s;
        Log.e("prefer",s);*/


        startService(new Intent(VehicleInfoActivity.this,MyService.class));
        Log.e("VehicleInfoActivity", "In the init of vehicleactivity");
        infoList = (ListView) findViewById(R.id.infoList);
        initArray();
        pref = getSharedPreferences("vList", Activity.MODE_PRIVATE);
        count = pref.getInt("count", -1);
        Log.e("VehicleInfoActivity", String.valueOf(count));
        // mt(count+"");
        if (count <= 0) {
            mt("Please Select Vehicles...");
            startActivity(new Intent(VehicleInfoActivity.this, SearchActivity.class));
        }

        start();
        //stoppedSince();
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vehDetails", vehDetails);
        editor.commit();
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
                Log.e("VehicleInfoActivity", "Timer executed");
            }
        }, 30*1000, 30*1000);

    }



    private void initArray() {

        Log.e("VehicleInfoActivity", "In the initarray");

        vehicles = new ArrayList<String>();
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        location = new ArrayList<String>();
        lat = new ArrayList<String>();
        lng = new ArrayList<String>();
        speed=new ArrayList<String>();
        statusArray = new ArrayList<Integer>();
    }

    @SuppressLint("ResourceAsColor")
    private void start() {
        Log.i("VehicleInfoActivity", "In the start");
        for (int i = 0; i < count; i++) {

            vehicles.add(pref.getString("vehicle" + i, null));
            Log.e("VehicleInfoActivity", String.valueOf(vehicles));
            // mt(pref.getString("vehicle"+i, null));
        }
        dbHelper = new MyDBHelper(VehicleInfoActivity.this);
        db = dbHelper.getReadableDatabase();
        for (int i = 0; i < vehicles.size(); i++) {
            String[] selectionArgs = {vehicles.get(i)

            };
            Log.e("VehicleInfoActivity","selection args"+ String.valueOf(vehicles.get(i)));

            Cursor cursor = db.query("onlineInfo", null, "vNum=?", selectionArgs,
                    null, null, null);
            // mt("tushar");
            // mt("name" +cursor.getString(1));
            cursor.move(0);
            while (cursor.moveToNext()) {
                date.add(cursor.getString(2));
                time.add(cursor.getString(3));
                location.add(cursor.getString(6));
                lat.add(cursor.getString(4));
                lng.add(cursor.getString(5));
                speed.add(cursor.getString(8));
                statusArray.add(cursor.getInt(7));
                cursor.moveToFirst();
                // mt(cursor.getString(7) + " " +cursor.getString(8));
            }
            //cursor.close();
        }

		/*code begins from here*/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(VehicleInfoActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(vehicles);
        editor.putString("key", json);
        editor.apply();

		/*ends here*/
        db.close();
        // db.close();
        // mt(date.get(2));
        VehicleInfoAdapter adapter = new VehicleInfoAdapter(this, vehicles,
                date, time, location, lat, lng, speed,statusArray);
        Log.e("VehicleInfoActivity", String.valueOf(adapter));
        infoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    private void mt(String text) {
        Toast.makeText(VehicleInfoActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    private boolean UpdateGUI() {
        //i++;
        //tv.setText(String.valueOf(i));
        myHandler.post(myRunnable);

        return true;
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            //tv.setText(String.valueOf(i));
            //  Intent intent=new Intent(VehicleInfoActivity.this,MyService.class);
            //  startService(intent);
            //Toast.makeText(VehicleInfoActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
            initArray();
            start();
        }
    };


	@Override
	public void onBackPressed()
	{

						/*startActivity(new Intent(VehicleInfoActivity.this,SearchActivity.class));
						finish();*/

        AlertDialog.Builder builder1 = new AlertDialog.Builder(VehicleInfoActivity.this);
        builder1.setTitle("Fleetview APP");
        builder1.setMessage("Are you sure want to exit");
        builder1.setIcon(R.drawable.fleetlogo);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                });

        builder1.setNegativeButton
                (
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert11 = builder1.create();
        alert11.show();


	}


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i=new Intent(VehicleInfoActivity.this,MainForm.class);
            startActivity(i);
            finish();

        }else if(id==R.id.current_position){
            Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(intent);
            finish();
        }


        else if (id==R.id.nav_logout){
            SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.clear();
            e.commit();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sure to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(VehicleInfoActivity.this, LoginAct.class));
                            stopService(new Intent(VehicleInfoActivity.this,MyService.class));
                            dialog.dismiss();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.dismiss();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}


