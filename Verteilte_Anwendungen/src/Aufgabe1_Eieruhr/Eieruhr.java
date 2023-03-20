package Aufgabe1_Eieruhr;

public class Eieruhr {
	
	public static void main(String[] args) {
		eieruhr(5000, "test01");
		eieruhr(3500, "test02");
		eieruhr(7600, "test03");
	}
	
	public static void eieruhr(int ms, String output) {
	    MyThread myThread = new MyThread(ms, output);
	    myThread.start();
	}

}
