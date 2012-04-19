package hackathon.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class PingPongActivity extends Activity implements SensorEventListener{
	private static final int DELAY = 15;
	private static final String IP = "192.168.0.13";
	private static final int PORT = 9876;
	private static final int YES = 1; 
	private static final int NO = 0; 
	
	private SensorManager mSensorManager;
    private Sensor mMagnetic;
    private Sensor mAccelerometer;
    
	private TextView xorient;
	private TextView yorient;
	private TextView zorient;
	private TextView text;
	private TextView mouse; 
	private Button button; 
	
	private int x,y,z;
	private int clicked = NO; 
	private float[] mMagneticValues;
	private float[] mAccelerometerValues;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        xorient = (TextView) findViewById(R.id.xvalues);
        yorient = (TextView) findViewById(R.id.yvalues);
        zorient = (TextView) findViewById(R.id.zvalues);
        mouse = (TextView) findViewById(R.id.mouse);
        button = (Button) findViewById(R.id.button);
        button.setOnTouchListener(new ButtonClick()); 
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
        
        try {
			initConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mMagnetic, DELAY);
        mSensorManager.registerListener(this, mAccelerometer, DELAY);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	
    }

	private static final int MATRIX_SIZE = 9;
	private static final float MAGNITUDE = 1000;

    public void onSensorChanged(SensorEvent event) {
    	synchronized (this) {
    		/* 
    		 * Better way to get orientation of android phone
    		 * http://www.damonkohler.com/2010/06/better-orientation-readings-in-android.html
    		 */
    		switch (event.sensor.getType()) {
    	      case Sensor.TYPE_MAGNETIC_FIELD:
    	        mMagneticValues = event.values.clone();
    	        break;
    	      case Sensor.TYPE_ACCELEROMETER:
    	        mAccelerometerValues = event.values.clone();
    	        break;
    	      }

    	      if (mMagneticValues != null && mAccelerometerValues != null) {
    	        float[] R = new float[MATRIX_SIZE];
    	        boolean success = SensorManager.getRotationMatrix(R, null, mAccelerometerValues, mMagneticValues);
    	        if (success) { 
	    	        float[] orientation = new float[3];
	    	        SensorManager.getOrientation(R, orientation);
	    	        x =  (int) (orientation[0] * MAGNITUDE);
	    	        y =  (int) (orientation[1] * MAGNITUDE);
	    	        z =  (int) (orientation[2] * MAGNITUDE);
	    	        xorient.setText("Orientation X: " + x);
	    	        yorient.setText("Orientation Y: " + y); 
	    	        zorient.setText("Orientation Z: " + z); 
    	        }
    	      }
    	}
    }
    
    public void initConnection() throws IOException {
    	String host = IP; //"10.10.66.230";
		int port = PORT;
		
		Socket socket = new Socket(InetAddress.getByName(host), port);
		System.out.println("Connection successful!");
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());

        // start the button thread
//        ButtonPress buttonThread = new ButtonPress(); 
//        buttonThread.start();
        
		SimpleClientThread test = new SimpleClientThread(out, in);
		test.start();
	}
    
    class ButtonClick implements OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) { 
				mouse.setText("YES");
				clicked = YES;
				return true; 
			} else if (event.getAction() == MotionEvent.ACTION_UP) { 
				mouse.setText("NO");
				clicked = NO;
				return true; 
			}
			return false; 
		} 
    }
    
    class SimpleClientThread extends Thread {
		private DataOutputStream out;
		private DataInputStream in;

		public SimpleClientThread(DataOutputStream out, DataInputStream in) {
			this.out = out;
			this.in = in;
		}

		@Override
		public void run() {
			while (true) {
				try {
					synchronized(this) { 
						out.writeInt(x);
						out.writeInt(y);
						out.writeInt(clicked); 
					}
					Thread.sleep(16);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
