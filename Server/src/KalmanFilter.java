import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class KalmanFilter {
	public static void main(String[] args) throws IOException, AWTException, InterruptedException { 
		Scanner scan = new Scanner(new File("output"));
		Robot robot = new Robot();
		robot.mousePress(InputEvent.BUTTON1_MASK); 
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
//		while (scan.hasNextLine()) {
//			String line[] = scan.nextLine().split(" ");
//			int x = (int) Float.parseFloat(line[0]); 
//			int y = (int) Float.parseFloat(line[1]); 
//			Thread.sleep(20); 
//			robot.mouseMove(x, y); 
//		}
//		BufferedWriter out = new BufferedWriter(new FileWriter("output-2")); 
//		double x = 0, x2 = 0, x3 = 0;  
//		double p = 0, p2 = 0, p3 = 0; 
//		double q = 0.01;		// process noise 
//		double r = 0.9; 	// sensor noise  
//		while (scan.hasNextLine()) { 
//			int measure_x = Integer.parseInt(scan.nextLine());  
//			p = p + q; 
//			double k = p/(p+r); 
//			x = x + k*(measure_x-x); 
//			p = (1-k) * p; 
////			System.out.println(x); 
////			out.write((int) x + "\n");
////			out.flush(); 
//			
//			p2 = p2 + q;
//			k = p2/(p2+r); 
//			x2 = x2 + k*(x - x2); 
//			p2 = (1-k)*p2;
//			
//			p3 = p3 + q;
//			k = p3/(p3+r); 
//			x3 = x2 + k*(x - x3); 
//			p3 = (1-k)*p3;
//			
//			System.out.println((int) x2); 
//		}
//		out.close(); 
//		scan = new Scanner(new File("output-2"));
//		out = new BufferedWriter(new FileWriter("output-3")); 
//		x = 0; 
//		p = 0; 
//		q = 0.02;		// process noise 
//		r = 0.5; 	// sensor noise
//		Robot robot = new Robot();
//		int y = 200; 
//		while (scan.hasNextLine()) { 
//			int measure_x = Integer.parseInt(scan.nextLine());  
//			p = p + q; 
//			double k = p/(p+r); 
//			x = x + k*(measure_x-x); 
//			p = (1-k) * p; 
//			System.out.println((int) x);
//			if (x != 0) { 
//				robot.mouseMove((int)x, y);
//				Thread.sleep(50);
//			}
//			out.write((int) x + "\n");
//			out.flush(); 
//		}
//		out.close(); 
	}
}
