package phone.client.hkdm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ClientActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView text = (TextView) findViewById(R.id.text_id);
        text.setText("Hello Tami!");
        
        try {
			initConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void initConnection() throws IOException {
    	System.out.println("Start connecting to my laptop!");
    	
    	String host = "localhost";
		int port = 12345;

		Socket socket = new Socket(InetAddress.getByName(host), port);
		System.out.println("Connection successful!");
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());

		TextView text = (TextView) findViewById(R.id.text_id);
		SimpleClientThread test = new SimpleClientThread(out, in, text);
		test.start();
	}
    
    static class SimpleClientThread extends Thread {
		private DataOutputStream out;
		private DataInputStream in;
		private TextView text;
		
		public SimpleClientThread(DataOutputStream out, DataInputStream in, TextView text) {
			this.out = out;
			this.in = in;
			this.text = text;
		}

		public void run() {
			try {
//				Scanner scan = new Scanner(System.in);
				while (true) {
					text.setText(in.readUTF());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}