package Aufgabe2_Dispatcher;
import java.util.*;

public class Result {
	
	public List<Integer> results = new LinkedList<Integer>();
	private int calculations;
	
	public Result(int calculations) {
		this.calculations = calculations;
	}

	public synchronized void addResult(int r) {
		results.add(r);
		if(results.size() == calculations) {			
			notify();
		}
	}
	
	public synchronized int[] getFinalResult() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//source: https://www.techiedelight.com/convert-list-integer-array-int/
		int[] resultsInt = results.stream()
                .mapToInt(Integer::intValue)
                .toArray();
		return resultsInt;
	}
}
