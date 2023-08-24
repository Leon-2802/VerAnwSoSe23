const seatTable = document.getElementById("seats");
const reservationInfo = document.getElementById("reservation-info");
const sessionInfo = document.getElementById("session-info");
let timeout = null;


function refresh() {
	fetch("/rest/kartenverkaufrest/sitzplaetze")
	  .then(response => {
		return response.json();
	}).then(sitzplaetze => {
		refreshTable(sitzplaetze);
	});
	
	fetch("/rest/kartenverkaufrest/reservierungen_annehmen")
		.then(response => {
			return response.json();
		}).then(reservierungen_annehmen => {
			refreshReservationInfo(reservierungen_annehmen);
		});
}

function refreshTable(seats) {
	var seatIndex = 0;
	seatTable.innerHTML = "";
	for(let r = 0; r < 100; r+=10) {
		var row = document.createElement("tr");
		seatTable.appendChild(row);
		for(let i = 0; i < 10; i++) {
			var currentSeat = document.createElement("td");
			var formatSeatNumber = ('000' + seats[seatIndex].sitzplatznummer).substr(-3);
			currentSeat.innerHTML = formatSeatNumber;
			switch (seats[seatIndex].zustand) {
				case "FREI":
					currentSeat.className = "frei";
					break;
				case "VERKAUFT":
					currentSeat.className = "verkauft";
					break;
				case "RESERVIERT":
					currentSeat.className = "reserviert";
					break;
			}
			row.appendChild(currentSeat)
			seatIndex++;
		}
	}
}

function refreshReservationInfo(value) {
	if (value) {
		reservationInfo.innerHTML = "Reservierungen werden noch angenommen";
	}
	else {
		reservationInfo.innerHTML = "Es werden keine Reservierungen mehr angenommen.";
	}
}

function restOperation(operation, formId) {
	operation += "?";
	
	let form = document.getElementById(formId);
	let formData = new FormData(form);
	
	for(var pair of formData.entries()) {
		operation += pair[0] + "=" + pair[1] + "&";
	}
	
	form.reset();
	
	fetch(operation).then(response => {
		if (response.ok) {
			refresh();
			refreshSessionInfo("Operation erfolgreich durchgeführt");
		} else {
			response.text().then(tx => {
				refreshSessionInfo(tx);
			});
		}
	});
}

function refreshSessionInfo(message) {
	if(timeout) {		
		clearTimeout(timeout);
		timeout = null;
	}
	
	sessionInfo.innerHTML = message;
	
	timeout = setTimeout(() => {
		sessionInfo.innerHTML = "";
	}, 5000);
}


window.addEventListener("load", ()=> {
	refresh();
});