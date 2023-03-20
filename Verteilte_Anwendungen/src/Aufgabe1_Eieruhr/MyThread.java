package Aufgabe1_Eieruhr;

public class MyThread extends Thread {
	
	private int waitTime;
	private String output;
	
	public MyThread(int waitTime, String output) {
		this.waitTime = waitTime;
		this.output = output;
	}
	
	public void run() {
		for(int i = waitTime; i > 0; i -= 1000) {
			if(i >= 1000) {
				schlafen(1000);
			}
			else {
				schlafen(i);
			}
			System.out.println(Integer.toString(i) + "ms : " + output);
		}
		System.out.println("done: " + output);
	}
	
	private void schlafen(int ms) {
		try {
		Thread.sleep(ms);
		} catch (InterruptedException t) {
		}
	}
}
