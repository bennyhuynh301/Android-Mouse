import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	public static final int MAX = 100; // maximum connections that this server can handle
	private static final String IP = "192.168.0.13";
	private static final int PORT = 9876; 
	private static final int YES = 1; 
	private static final int NO = 0; 
	
	public static void main(String[] args) throws UnknownHostException, IOException { 
		String host = IP; // args[0];
		int port = PORT; // Integer.parseInt(args[1]);
		
		ServerSocket socket = new ServerSocket(port, MAX, InetAddress.getByName(host)); 
		
		System.out.println("Waiting for connection...");
		Socket client = socket.accept();
		System.out.println("Connection etablished.");
		
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		DataInputStream in = new DataInputStream(client.getInputStream()); 
		
		RevcieveThread thread = new RevcieveThread(out, in); 
		thread.start();
	}
	
	static class RevcieveThread extends Thread {
		private DataOutputStream out;
		private DataInputStream in;
		// Preparing for Kalman filter 
		private double x1 = 0, x2 = 0, x3 = 0; 
		private double y1 = 0, y2 = 0, y3 = 0; 
		private double px1 = 0, px2 = 0, px3 = 0;
		private double py1 = 0, py2 = 0, py3 = 0; 
		private double q = 0.001; // noise process
		private double r = 0.5; // noise measurement
		private double k = 0; 
		
		public RevcieveThread(DataOutputStream out, DataInputStream in) {
			this.out = out;
			this.in = in;
		}
		
		public void kalmanFilter(int x, int y) {
			// apply first kalman filter for x
			px1 = px1 + q; 
			k = px1/(px1+r); 
			x1 = x1 + k*(x-x1); 
			px1 = (1-k) * px1; 
			
			// apply second kalman filter for x 
			px2 = px2 + q;
			k = px2/(px2+r); 
			x2 = x2 + k*(x1 - x2); 
			px2 = (1-k)*px2;
			
			
			// apply first kalman filter or y
			py1 = py1 + q; 
			k = py1/(py1+r); 
			y1 = y1 + k*(y-y1); 
			py1 = (1-k) * py1; 
			
			// apply second kalman filter for y
			py2 = py2 + q;
			k = py2/(py2+r); 
			y2 = y2 + k*(y1 - y2); 
			py2 = (1-k)*py2;
		}

		public void run() {
			BufferedWriter out = null; 
			try {
				out = new BufferedWriter(new FileWriter("output"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			Robot robot = null;
			try {
				robot = new Robot();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			while (true) {
				try {
					int x = in.readInt();
					int y = 1000 - in.readInt() * (-1); 
					int clicked = in.readInt(); 
					if (clicked == YES) { 
						robot.mousePress(InputEvent.BUTTON1_MASK);
						System.out.println("YES");
					}
					else {  
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
						System.out.println("NO");
					}
					// apply kalman filter here
					kalmanFilter(x, y); // this would change the values of x2 and y2
					robot.mouseMove((int) x2, (int) y2);
					out.write(x2 + " " + y2 + "\n"); 
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}