package hackathon.phone;
import java.util.Arrays;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class HackathonActivity extends Activity implements SensorEventListener{
    /** Called when the activity is first created. */
	SensorManager sm = null;
	Sensor ma = null;
	TextView text; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        text = (TextView) findViewById(R.id.values);
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        text.setText("nguyen tan do");
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        ma = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		synchronized (this) {
			text.setText(Arrays.toString(arg0.values));
		}
	}
	
	@Override
	protected void onStop() {
		super.onPause(); 
		sm.unregisterListener(this); 
	}
	
	@Override
	protected void onResume() { 
		super.onResume();
		sm.registerListener(this, ma, SensorManager.SENSOR_DELAY_FASTEST);
	}
}