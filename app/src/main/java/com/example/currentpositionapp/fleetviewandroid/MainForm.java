package com.example.currentpositionapp.fleetviewandroid;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.currentpositionapp.R;
import com.example.currentpositionapp.VehicleInfoActivity;
import com.example.currentpositionapp.VehicleInfoAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainForm extends AppCompatActivity implements OnClickListener
{
	//String url="http://192.168.2.239:8181/FleetViewProjectTest/MyServlet";
	String url="http://twtech.in:8080/FleetViewProject/MyServlet";

	String user,passwd;
	ListView listView;
	int activity=1;
	String[] list={"Add Request","Add Odometer","View Violation Report","L1 Generate Report"};
	CardView Request,Odometer,viewreport,l1generate,currentpos;
	String name = "";
	String name2 = "";
	String status = "";
	SQLiteDatabase db;
	SQLiteDatabase db3;
	SQLiteDatabase db1;
	SQLiteDatabase db5;
	SharedPreferences sharedPreferences;
	String userName;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainform_new);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

		Request=(CardView) findViewById(R.id.cv_one);
		Odometer=(CardView) findViewById(R.id.cv_two);
		viewreport=(CardView) findViewById(R.id.cv_three);
		l1generate=(CardView) findViewById(R.id.cv_four);

		sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
		userName=sharedPreferences.getString("name",null);
		//currentpos=(TextView) findViewById(R.id.img6);
	    
		  Typeface face = Typeface.createFromAsset(getAssets(),
		             "arial.ttf");
		 /*Request.setTypeface(face);
		 Odometer.setTypeface(face);
		 viewreport.setTypeface(face);
		 l1generate.setTypeface(face);*/
		// fuel.setTypeface(face);
		 
		Intent ins=getIntent();
		user=ins.getStringExtra("Username");
		passwd=ins.getStringExtra("password");
         try {
			 TextView textView = (TextView) findViewById(R.id.txtUname);
			 textView.setText("Hi," + userName);
		 }catch (Exception e){
         	Log.e("exception e",e.getMessage());
		 }

//		array=ins.getExtras().getStringArrayList("VehicleNo");
/*
		//Insert the data into  shared prefernces
		SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("key",array.size());
		for(int i=0;i<=array.size();i++)
		{

				editor.remove("keyvalue"+i);
				editor.putString("keyvalue",array.get(1));

		}
		editor.commit(); //commit changes

		//Getting the data from the sharedprefernces
		array.clear();
		int size = sharedPreferences.getInt("key", 0);
		for(int i=0;i<size;i++)
		{
			varray.add(sharedPreferences.getString("keyvalue" + i, null));

		}*/
		Request.setOnClickListener(this);
		Odometer.setOnClickListener(this);
		viewreport.setOnClickListener(this);
		l1generate.setOnClickListener(this);
		//currentpos.setOnClickListener(this);
//		System.out.println("edtUsername"+user+" "+"edtPassword"+passwd+array.size());
        MyDBHelper helper = new MyDBHelper(MainForm.this);
        db5 = helper.getWritableDatabase();
        db1 = helper.getWritableDatabase();
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.cv_one:
			Intent i=new Intent(getApplicationContext(),AddRequest.class);
			i.putExtra("name",userName);
			finish();
//			i.putExtra("vehlist",LoginForm.arrayList);
			startActivity(i);
			break;
		case R.id.cv_two:
			Intent in=new Intent(getApplicationContext(),OdoMeter.class);
			in.putExtra("name",userName);
			finish();
//			in.putExtra("vehlist", array);
			startActivity(in);
			break;
		case R.id.cv_three:
			Intent ins=new Intent(getApplicationContext(),Alerts.class);
//			ins.putExtra("name",LoginForm.name);
			ins.putExtra("Alerts", "Alerts");
			startActivity(ins);
			break;
		case R.id.cv_four:
			//Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(getApplicationContext(),Violation.class);
//			intent.putExtra("name",LoginForm.name);
			intent.putExtra("violations", "Violations");
			startActivity(intent);
			break;
	 	/*case R.id.img5:
			Intent intent1=new Intent(getApplicationContext(),FuelEntry.class);
			intent1.putExtra("name", LoginForm.name);
			intent1.putExtra("Fuel", "fuel");
			intent1.putExtra("vehlist",LoginForm.arrayList);
			System.out.println("--===in fuelintent");
			startActivity(intent1);
			break;			
		*/
	/*case R.id.img6:
		
		//Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
		Intent intent2=new Intent(getApplicationContext(),LoginAct.class);
		intent2.putExtra("name", user);
		intent2.putExtra("passwd", passwd);
		System.out.println("--===in pos");
		startActivity(intent2);
		break;	
	*/
	}

	}


	@SuppressLint("NewApi")
	@Override
	public void onBackPressed()
	{

		Intent i=new Intent(MainForm.this, VehicleInfoActivity.class);
		startActivity(i);
		finish();
		
	}
@Override
   public void onResume(){
       super.onResume();
	}

}