import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.mysql.cj.jdbc.MysqlDataSource;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import datenbankanbindung.KartenverkaufException;
import datenbankanbindung.Sitzplatz;
import datenbankanbindung.Zustand;


@Singleton
@Path("/kartenverkaufrest")
public class KartenverkaufREST implements ExceptionMapper<Throwable> {
	
	public DataSource datasource;
	
	
	public KartenverkaufREST(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public KartenverkaufREST() {
		MysqlDataSource mdatasource = new MysqlDataSource();
        mdatasource.setURL("jdbc:mysql://localhost:3306/Kartenverkauf");
        mdatasource.setUser("admin");
        mdatasource.setPassword("passwort");
        this.datasource = mdatasource;
	}
	
	@Path("/sitzplatz")
	@GET
	@Produces({APPLICATION_JSON})
	public Sitzplatz getSitzplatz(@QueryParam("number")int nr) throws SQLException {
		try(Connection connection = datasource.getConnection()) {
			try {				
				if(nr < 1 || nr > 100) {
					throw new KartenverkaufException("Sitzplatznummer muss zwischen 1 und 100 liegen");
				}
				PreparedStatement ps = connection.prepareStatement(
						"SELECT * FROM `Sitzplätze` WHERE `Sitzplatznummer` = ?");	
				ps.setInt(1, nr);
				ResultSet rs = ps.executeQuery();
				boolean b = rs.next();
				return new Sitzplatz(rs.getInt("Sitzplatznummer"), Zustand.valueOf(rs.getString("Status")), 
						rs.getString("Reservierungsname"));
			}
			catch(KartenverkaufException ke) {
				ke.printStackTrace();
				return null;
			}
		}
	}
	
	@Path("/sitzplaetze")
	@GET
	@Produces({APPLICATION_JSON})
	public Sitzplatz[] getSitzplaetze() throws SQLException {
		try(Connection connection = datasource.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Sitzplätze");
			Sitzplatz[] result = new Sitzplatz[100];
			while(rs.next()) {
				int index = rs.getInt("Sitzplatznummer");
				result[index-1] = new Sitzplatz(index, Zustand.valueOf(rs.getString("Status")), 
						rs.getString("Reservierungsname"));
			}
			return result;
		}
	}
	
	@Path("/reservierungen_annehmen")
	@GET
	@Produces({APPLICATION_JSON})
	public boolean getReservierungenAnnehmen() throws SQLException {
		try(Connection connection = datasource.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Reservierung_möglich");
			rs.next();
			boolean result = rs.getBoolean("Reservierung_möglich");
			return result;
		}
	}
	
	@Override
	public String toString() {
		try {
			String result = "";
			Sitzplatz[] plaetze = this.getSitzplaetze();
			for(int i = 0; i < plaetze.length; i++) {
				result += plaetze[i].toString() + "\n";
			}
			result += "Reservierungen möglich: " + this.getReservierungenAnnehmen();
			return result;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
	
	@Path("/kaufe")
	@GET
	@Produces({APPLICATION_JSON})
	public void kaufe(@QueryParam("number")int nr) throws SQLException, KartenverkaufException {
		try (Connection connection = datasource.getConnection()) {

			Sitzplatz target = this.getSitzplatz(nr);
			if(target == null)
				return;
			
			if(target.getZustand() != Zustand.FREI)
				throw new KartenverkaufException("Der ausgewählte Platz ist bereits reserviert oder verkauft");
			
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE `Sitzplätze` SET `Status` = ? WHERE `Sitzplatznummer` = ? ");
			ps.setString(1, "VERKAUFT");
			ps.setInt(2, nr);
			ps.execute();
			
			System.out.println("Platz nr. " + nr + " erfolgreich gekauft!");

		}
	}
	
	@Path("/reserviere")
	@GET
	@Produces({APPLICATION_JSON})
	public void reserviere(@QueryParam("number")int nr, @QueryParam("name")String name) throws SQLException, KartenverkaufException {
		try (Connection connection = datasource.getConnection()) {				
			Sitzplatz target = this.getSitzplatz(nr);
			if(target == null)
				return;
			
			if(!this.getReservierungenAnnehmen())
				throw new KartenverkaufException("Momentan werden keine Reservierungen mehr angenommen");
			if(target.getZustand() != Zustand.FREI)
				throw new KartenverkaufException("Der ausgewählte Platz ist bereits reserviert oder verkauft");
			
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE `Sitzplätze` SET `Status` = ?, `Reservierungsname` = ? WHERE `Sitzplatznummer` = ? ");
			ps.setString(1, "RESERVIERT");
			ps.setString(2, name);
			ps.setInt(3, nr);
			ps.execute();
			
			System.out.println("Platz nr. " + nr + " erfolgreich reserviert!");
		} 
	}
	
	@Path("/kaufe-reserviert")
	@GET
	@Produces({APPLICATION_JSON})
	public void kaufeReserviert(@QueryParam("number")int nr, @QueryParam("name")String name) throws SQLException, KartenverkaufException {
		try (Connection connection = datasource.getConnection()) {
			Sitzplatz target = this.getSitzplatz(nr);
			if(target == null)
				return;
			
			if(target.getZustand() != Zustand.RESERVIERT)
				throw new KartenverkaufException("Der ausgewählte Platz ist noch nicht reserviert");
			if(!target.getReservierungsname().equals(name))
				throw new KartenverkaufException("Der ausgewählte Platz ist nicht auf diesen Namen reserviert");
			
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE `Sitzplätze` SET `Status` = ?, `Reservierungsname` = ? WHERE `Sitzplatznummer` = ? ");
			ps.setString(1, "VERKAUFT");
			ps.setString(2, "");
			ps.setInt(3, nr);
			ps.execute();
			
			System.out.println("Platz nr. " + nr + " erfolgreich gekauft!");
		}
	}
	
	@Path("/storniere")
	@GET
	@Produces({APPLICATION_JSON})
	public void storniere(@QueryParam("number")int nr) throws SQLException, KartenverkaufException {
		try (Connection connection = datasource.getConnection()) {			
			Sitzplatz target = this.getSitzplatz(nr);
			if(target == null)
				return;
			
			if(target.getZustand() == Zustand.FREI)
				throw new KartenverkaufException("Der ausgewählte Platz ist bereits frei");
			
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE `Sitzplätze` SET `Status` = ?, `Reservierungsname` = ? WHERE `Sitzplatznummer` = ? ");
			ps.setString(1, "FREI");
			ps.setString(2, "");
			ps.setInt(3, nr);
			ps.execute();
			
			System.out.println("Platz nr. " + nr + " erfolgreich storniert!");
		}
	}

	@Path("/hebe-reservierungen-auf")
	@GET
	@Produces({APPLICATION_JSON})
	public void hebeReservierungenAuf() throws SQLException {
		try(Connection connection = datasource.getConnection()) {
			PreparedStatement ps0 = connection.prepareStatement(
					"UPDATE `Sitzplätze` SET `Status` = ?, `Reservierungsname` = ? WHERE `Status` = ?");
			ps0.setString(1, "FREI");
			ps0.setString(2, "");
			ps0.setString(3, "RESERVIERT");
			ps0.execute();
			
			PreparedStatement ps1 = connection.prepareStatement(
					"UPDATE `Reservierung_möglich` SET `Reservierung_möglich` = ?");
			ps1.setBoolean(1, false);
			ps1.execute();
		}
	}
	
	@Path("/initialisiere")
	@GET
	@Produces({APPLICATION_JSON})
	public void initialisiere() throws SQLException {
		try(Connection connection = datasource.getConnection()) {
			PreparedStatement ps0 = connection.prepareStatement(
					"UPDATE `Sitzplätze` SET `Status` = ?, `Reservierungsname` = ? WHERE `Status` = ? OR `Status` = ?");
			ps0.setString(1, "FREI");
			ps0.setString(2, "");
			ps0.setString(3, "RESERVIERT");
			ps0.setString(4, "VERKAUFT");
			ps0.execute();
			
			PreparedStatement ps1 = connection.prepareStatement(
					"UPDATE `Reservierung_möglich` SET `Reservierung_möglich` = ?");
			ps1.setBoolean(1, true);
			ps1.execute();
		}
		
	}
	
	@Override
	public Response toResponse(Throwable exception) {
		exception.printStackTrace();
		System.out.println("REST-Exception: " + exception.getMessage());
		return Response.status(500).entity(exception.getMessage())
				.type("text/plain").build();
	}

	
}


