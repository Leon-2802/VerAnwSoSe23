<%@page import="datenbankanbindung.Sitzplatz"%>
<%@page import="datenbankanbindung.Kartenverkauf"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Kartenverkauf</title>
<link rel="stylesheet" href="/style.css">
<%
Kartenverkauf k = new Kartenverkauf();
%>
</head>
<body>
	<h1>Kartenverkauf</h1>
	<div id="wrap-content">
		<div id=wrap-output>
			<table>
				<%
				int nummer = 0;
				for(int i = 0; i < 10; i++) {
				%>
				<tr>
					<%
					for(int j = 0; j < 10; j++) {
						nummer++;
						String s = String.format("%03d", nummer);
						Sitzplatz sitzplatz = k.getSitzplatz(nummer);
						String zustand = sitzplatz.getZustand().toString();
						request.setAttribute("zustand", zustand.toLowerCase());
					%>
					<td class="${zustand}"><%= "" + s %></td>
					<%
					}
					%>
				</tr>
				<%
				}
				%>
			</table>
			<div id="wrap-info">
				<div class="info-element frei">frei</div>
				<div class="info-element reserviert">reserviert</div>
				<div class="info-element verkauft">verkauft</div>
			</div>
			<%
			boolean openToReservations = k.getReservierungenAnnehmen();
			String reservationInfo = "";
			if(openToReservations) {
				reservationInfo = "Reservierungen können noch angenommen werden.";
			} else {
				reservationInfo = "Es werden keine Reservierungen mehr angenommen.";
			}
			%>
			<p><%= "" + reservationInfo %></p>
		</div>
		<div id=wrap-form>
			<div>
				<form class=form-row action="KartenverkaufServlet" method="post">
					<input class="hidden" name="operation" value="kaufe">
					<div class="kaufen">
						<label for="seat-number">nr:</label>
						<input name="seat-number" type="number">
						<button type="submit">kaufe</button>
					</div>
				</form>
				<form class=form-row action="KartenverkaufServlet" method="post">
					<input class="hidden" name="operation" value="reserviere">
					<div class="reservieren">
						<label for="seat-number">nr:</label>
						<input name="seat-number" type="number">
						<label for="name">name</label>
						<input name="name" type="text">
						<button type="submit">reserviere</button>
					</div>
				</form>
				<form class=form-row action="KartenverkaufServlet" method="post">
					<input class="hidden" name="operation" value="kaufeReserviert">
					<div class="kaufeReserviert">
						<label for="seat-number">nr:</label>
						<input name="seat-number" type="number">
						<label for="name">name</label>
						<input name="name" type="text">
						<button type="submit">kaufe reserviert</button>
					</div>
				</form>
				<form class=form-row action="KartenverkaufServlet" method="post">
					<input class="hidden" name="operation" value="storniere">
					<div class="storniere">
						<label for="seat-number">nr:</label>
						<input name="seat-number" type="number">
						<button type="submit">storniere</button>
					</div>
				</form>
				<form action="KartenverkaufServlet" method="post">
					<input class="hidden" name="operation" value="hebeReservierungenAuf">
					<button class="form-btn" type="submit">hebe Reservierungen auf</button>
				</form>
				<form action="KartenverkaufServlet" method="post">
					<input class="hidden" name="operation" value="initialisiere">
					<button class="form-btn" type="submit">initialisiere</button>
				</form>
			</div>
			<%
				String feedback = (String) request.getAttribute("success");
			%>
			<p id="session-info"><%= "" + feedback %></p>
		</div>
	</div>
	
	<%request.removeAttribute("success"); %>
</body>
</html>