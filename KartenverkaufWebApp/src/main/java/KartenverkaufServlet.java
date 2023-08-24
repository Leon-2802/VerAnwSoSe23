import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datenbankanbindung.Kartenverkauf;
import datenbankanbindung.KartenverkaufException;


@WebServlet("/KartenverkaufServlet")
public class KartenverkaufServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Kartenverkauf k = new Kartenverkauf();
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
		String nrParam = request.getParameter("seat-number");
		String name = request.getParameter("name");
		
		
		if(nrParam != null && !nrParam.isEmpty()) {
			int nr = Integer.parseInt(request.getParameter("seat-number"));
			
			try {
				switch(request.getParameter("operation")) {
				case "kaufe":	
					k.kaufe(nr);				
					request.setAttribute("success", "Operation 'kaufe' erfolgreich durchgeführt.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					break;
				case "reserviere":
					k.reserviere(nr, name);
					request.setAttribute("success", "Operation 'reserviere' erfolgreich durchgeführt.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					break;
				case "kaufeReserviert":
					k.kaufeReserviert(nr, name);
					request.setAttribute("success", "Operation 'kaufe reserviert' erfolgreich durchgeführt.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					break;
				case "storniere":
					k.storniere(nr);
					request.setAttribute("success", "Operation 'storniere' erfolgreich durchgeführt.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					break;
				}
			} 
			catch(KartenverkaufException ke) {
				String errorMessage = ke.getMessage();
				errorMessage = "Fehler: " + errorMessage;
				Object error = (Object) errorMessage;
				request.setAttribute("success", error);
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
			catch (SQLException e){
				e.printStackTrace();
			}
		}
		else {
			try {				
				switch(request.getParameter("operation")) {			
				case "hebeReservierungenAuf":
					k.hebeReservierungenAuf();
					request.setAttribute("success", "Operation 'Reservierungen aufheben' erfolgreich durchgeführt.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					break;
				case "initialisiere":
					k.initialisiere();
					request.setAttribute("success", "Operation 'initialisiere' erfolgreich durchgeführt.");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					break;
				default:
					request.setAttribute("success", "Fehler: Falsche Eingabewerte");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
					break;
				}
			}
			catch (SQLException ke) {
				ke.printStackTrace();
			}
		}
}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
