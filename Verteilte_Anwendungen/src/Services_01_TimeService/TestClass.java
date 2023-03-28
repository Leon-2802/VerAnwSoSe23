package Services_01_TimeService;

public class TestClass {
	
	public static void main(String[] args) {
		System.out.println(TimeServiceClient.dateFromServer("127.0.0.1"));
		System.out.println(TimeServiceClient.timeFromServer("127.0.0.1"));
	}

}
