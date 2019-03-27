const tempHeading = ["Boston", "Cambridge", "Mar 14 2019", "1:59 PM"];
const months = [
	"January", "February", "March", "April", "May", "June",
	"July", "August", "September", "October", "November", "December"
];

const tempData = [{
		trips: {
			trip1: {
				start: "Providence, RI",
				end: "New Haven, CT",
				date: "1553453944862",
				currentSize: "3",
				maxSize: "5",
				costPerPerson: "15",
				id: "1",
				name: "Jeff's Carpool",
			},
			trip2: {
				start: "New Haven, CT",
				end: "New York, NY",
				date: "1553453944862",
				currentSize: "3",
				maxSize: "5",
				costPerPerson: "20",
				id: "2",
				name: "Sam's Carpool",
			},
			trip3: {
				start: "New York, NY",
				end: "Marlboro, NJ",
				date: "1553453944862",
				currentSize: "3",
				maxSize: "5",
				costPerPerson: "10",
				id: "3",
				name: "Arvind's Carpool",
			}
		},
		totalPrice: "24",
		totalTime: "260",
		status: "joined"
	},
	{
		trips: {
			trip1: {
				start: "Providence, RI",
				end: "New Haven, CT",
				date: "1553453944862",
				currentSize: "3",
				maxSize: "5",
				costPerPerson: "15",
				id: "1",
				name: "Jeff's Carpool",
			},
			trip2: {
				start: "New Haven, CT",
				end: "Marlboro, NJ",
				date: "1553453944862",
				currentSize: "3",
				maxSize: "5",
				costPerPerson: "20",
				id: "4",
				name: "Mark's Carpool",
			}
		},
		totalPrice: "2",
		totalTime: "220",
		status: "pending"
	},
	{
		trips: {
			trip1: {
				start: "Providence, RI",
				end: "Springfield, MA",
				date: "1553453944862",
				currentSize: "1",
				maxSize: "3",
				costPerPerson: "24",
				id: "5",
				name: "Going to Airport",
			}
		},
		totalPrice: "24",
		totalTime: "60",
		status: "pending"
	},
	{
		trips: {
			trip1: {
				start: "Providence, RI",
				end: "Six Flags",
				date: "1553453944862",
				currentSize: "4",
				maxSize: "5",
				costPerPerson: "24",
				id: "6",
				name: "Six Flags Trip",
			}
		},
		totalPrice: "24",
		totalTime: "180",
		status: "join"
	},
	{
		trips: {
			trip1: {
				start: "Boston, MA",
				end: "Brown University",
				date: "1553453944862",
				currentSize: "3",
				maxSize: "5",
				costPerPerson: "15",
				id: "7",
				name: "Boston to Brown",
			}
		},
		totalPrice: "15",
		totalTime: "140",
		status: "join"
	},
	{
		trips: {
			trip1: {
				start: "Yale University",
				end: "Harvard University",
				date: "1553453944862",
				currentSize: "3",
				maxSize: "5",
				costPerPerson: "14",
				id: "8",
				name: "Yale to Harvard",
			}
		},
		totalPrice: "14",
		totalTime: "200",
		status: "join"
	}
];

$(document).ready(function () {
	showHomeInfo();
	queryResults();
	initTooltips();
});

function onUserSignedIn() {
	console.log("User signed in.");
	signInTooltip[0].hide();
}

function onUserSignedOut() {
	console.log("User signed out.");
}


function initTooltips() {
	tippy("#info", {
		animation: "scale",
		arrow: true,
		maxWidth: "250px",
		arrowType: "round",
		theme: "drawbridge",
		hideOnClick: false,
		inertia: true,
		placement: "bottom",
	});
}

function queryResults() {
	$("#start").text(tempHeading[0]);
	$("#end").text(tempHeading[1]);
	$("#date").text(tempHeading[2]);
	$("#time").text(tempHeading[3]);

	setTripResults(tempData);
}

function setTripResults(data) {
	data.forEach(element => {
		let result = `<div class="result"><div class="result-trips">`;
		let e = element["trips"];
		let ids = [];

		for (let trip in e) {
			result = result + generateTrip(
				e[trip]["name"], e[trip]["start"],
				e[trip]["end"], e[trip]["date"],
				e[trip]["currentSize"], e[trip]["maxSize"],
				e[trip]["costPerPerson"]);
			ids.push(e[trip]["id"]);
		}
		let imgAtt = "";
		if (element["status"] === "join") {
			imgAtt = `onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleJoin(${ids	});"`;
		}

		result = result + `</div><img src="../images/${element["status"]}-btn.png" class="${element["status"]}-btn" ${imgAtt}/>
							</div>`
		$(".results-content").append(result);
	});
	$(".results-content").append(`<input name="host" alt="Host" type="image" src="/images/host-btn.png"
		class="host-btn" onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleHost();"/>`);
}

function handleJoin(ids) {
	if (userProfile == undefined) {
		$("html, body").animate({
			scrollTop: 0
		}, "slow");
		signInTooltip[0].setContent("Sign in with your Google Account to join an existing trip.");
		signInTooltip[0].show();
	} else {
		console.log(ids);
	}
}

function handleHost() {
	if (userProfile == undefined) {
		$("html, body").animate({
			scrollTop: 0
		}, "slow");
		signInTooltip[0].setContent("Sign in with your Google Account to host your own trip.");
		signInTooltip[0].show();
	} else {
		console.log("HOST");
	}
}