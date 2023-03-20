package Aufgabe2_Dispatcher;

public class Dispatcher {
	
	public static void main(String[] args) {
		int[] test = execute(new F() {

			@Override
			public int f(int x) {
				return x+1;
			}
			
		}, 10);
		
		for(int i = 0; i < test.length; i++) {
			System.out.println(test[i]);
		}
	}
	
	
	public static int[] execute(F f, int n) {
		Result resultStorage = new Result(n);
		
		for(int i = 0; i < n; i++) {
			DispatchThread newThread = new DispatchThread(f, i, resultStorage);
			newThread.start();
		}
		
		return resultStorage.getFinalResult();
	}

}