package com.gregayer.smoker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gregayer.smoker.data.SmokerSQLiteHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SmokerActivity extends Activity {
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	private int smokeCount;
	private Date lastSmokeTime = null;
	private SmokerSQLiteHelper dbHelper;
	private SQLiteDatabase db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoker);
        
        dbHelper = new SmokerSQLiteHelper(this);
        db = dbHelper.getWritableDatabase();

        final TextView txtCount = (TextView) findViewById(R.id.txtSmokeCount);
        final TextView txtTime = (TextView) findViewById(R.id.txtSmokeTime);
        
        Button button = (Button) findViewById(R.id.btnSmoke);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordSmoke();
				txtCount.setText(Integer.toString(smokeCount));
				txtTime.setText(DateFormat.getTimeInstance().format(lastSmokeTime));
			}
		});
    }

	@Override
	public void onResume() {
		super.onResume();
		
		db = dbHelper.getWritableDatabase();
        retrieveSmoke();
        
        final TextView txtCount = (TextView) findViewById(R.id.txtSmokeCount);
        txtCount.setText(Integer.toString(smokeCount));
        
        final TextView txtTime = (TextView) findViewById(R.id.txtSmokeTime);
        if (lastSmokeTime != null)
        {
        	txtTime.setText(DateFormat.getTimeInstance().format(lastSmokeTime));
        }
	}
    
    @Override
	protected void onPause() {
		super.onPause();
		db.close();
	}

	private void recordSmoke() {
    	smokeCount++;
    	lastSmokeTime = new Date();
    	
    	// save the information
    	SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_FORMAT);
    	ContentValues values = new ContentValues();
    	values.put(SmokerSQLiteHelper.COLUMN_DATE, dateFormatter.format(lastSmokeTime));
		long id = db.insert(SmokerSQLiteHelper.TABLE_SMOKER, null, values);
    	// TODO: Check that the value was actually inserted
    }
    
    private void retrieveSmoke() {
    	Cursor cursor = db.query(SmokerSQLiteHelper.TABLE_SMOKER, new String[] {SmokerSQLiteHelper.COLUMN_DATE}, null, null, null, null, SmokerSQLiteHelper.COLUMN_ID + " DESC");
    	if(cursor.moveToFirst())
    	{
	    	String lastSmokeString = cursor.getString(0);
	    	if (lastSmokeString != "")
	        {
	        	try {
					lastSmokeTime = new SimpleDateFormat(TIME_FORMAT).parse(lastSmokeString);
				} catch (ParseException e) {
					
				}
	        }
    	}
    	smokeCount = cursor.getCount();
    	cursor.close();
    }
}
