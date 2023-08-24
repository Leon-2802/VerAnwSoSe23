package datenbankanbindung;


public class Sitzplatz {
	
	private int sitzplatznummer;
	private Zustand zustand;
	private String reservierungsname;
	
	
	public Sitzplatz(int sitzplatznummer, Zustand zustand, String reservierungsname) {
		this.sitzplatznummer = sitzplatznummer;
		this.zustand = zustand;
		this.reservierungsname = reservierungsname;
	}
	
	
	public int getSitzplatznummer() {
		return this.sitzplatznummer;
	}
	public void setSitzplatznummer(int nr) {
		this.sitzplatznummer = nr;
	}
	
	public Zustand getZustand() {
		return this.zustand;
	}
	public void setZustand(Zustand zustand) {
		this.zustand = zustand;
	}
	
	public String getReservierungsname() {
		return this.reservierungsname;
	}
	public void setReservierungsname(String name) {
		this.reservierungsname = name;
	}
	
	@Override
	public String toString() {
		return sitzplatznummer + " " + zustand + " " + reservierungsname;
	}
	
}
