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

/**
 * When the DOM loads, show the home and info buttons, query for the
 * results of the search, and initialize tooltips.
 */
$(document).ready(function () {
	showHomeInfo();
	queryResults();
	initTooltips();
});

/**
 * Overriden function for user sign in action.
 */
function onUserSignedIn() {
	console.log("User signed in.");
	// Hide the sign in tooltip if visible
	signInTooltip[0].hide();
}

/**
 * Overriden function for user sign out action.
 */
function onUserSignedOut() {
	console.log("User signed out.");
}

/**
 * Initialize the info tooltips
 */
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

/**
 * Query the server for the results of the trip search
 */
function queryResults() {
	$("#start").text(tempHeading[0]);
	$("#end").text(tempHeading[1]);
	$("#date").text(tempHeading[2]);
	$("#time").text(tempHeading[3]);

	// Set the trip results on the page with the resulting data
	setTripResults(tempData);
}

/**
 * Sets the trip results on the page with the data from the server
 * @param {*} data
 */
function setTripResults(data) {
	// Iterate through each trip group
	data.forEach(element => {
		let result = `<div class="result"><div class="result-trips">`;
		let e = element["trips"];
		let ids = [];

		//Iterate through each connecting trip in a trip group
		for (let trip in e) {
			result += generateTrip(
				e[trip]["name"], e[trip]["start"],
				e[trip]["end"], e[trip]["date"],
				e[trip]["currentSize"], e[trip]["maxSize"],
				e[trip]["costPerPerson"]);
			ids.push(e[trip]["id"]);
		}
		// If the status is "join" add hover effects and an onclick handler
		let imgAtt = "";
		if (element["status"] === "join") {
			imgAtt = `onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleJoin(${ids	});"`;
		}

		// Add the corresponding button and append to the results-content div
		result += `</div><img src="../images/${element["status"]}-btn.png" class="${element["status"]}-btn" ${imgAtt}/>
							</div>`
		$(".results-content").append(result);
	});
	/**
	 * Append a host trip button wiht hover effects and an onclick handler
	 * to the end of the results-content div
	 */
	$(".results-content").append(`<input name="host" alt="Host" type="image" src="/images/host-btn.png"
		class="host-btn" onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleHost();"/>`);
}

/**
 * Handles a join button press.
 * @param {*} id
 */
function handleJoin(ids) {
	/**
	 * If the user is not logged in, scroll to the top of the screen and display the
	 * sign in tooltip prompting the user to sign in.
	 *
	 * Otherwise, perform a join trip request if the user is not already a member/pending/host
	 */
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

/**
 * Handles a host button press.
 * @param {*} id
 */
function handleHost() {
	/**
	 * If the user is not logged in, scroll to the top of the screen and display the
	 * sign in tooltip prompting the user to sign in.
	 *
	 * Otherwise, perform a host trip request
	 */
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