package Aufgabe2_Dispatcher;

public class DispatchThread extends Thread {
	
	private F f;
	private int x;
	private Result result;

	public DispatchThread(F f, int x, Result result) {
		this.f = f;
		this.x = x;
		this.result = result;
	}
	
	public void run() {
		result.addResult(f.f(x));
	}
}
