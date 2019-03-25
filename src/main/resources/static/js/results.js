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
});

function queryResults() {
	$("#start").text(tempHeading[0]);
	$("#end").text(tempHeading[1]);
	$("#date").text(tempHeading[2]);
	$("#time").text(tempHeading[3]);

	setTripResults(tempData);
}

function generateTrip(name, start, end, rawDate, size, maxSize, price) {
	let date = new Date(parseFloat(rawDate));
	let hourLabel = "AM";
	let hour = date.getHours();
	if (hour > 12) {
		hour = hour - 12;
		hourLabel = "PM";
	}

	return (`<div class="result-info">
				<div class="sub-heading">${name}</div>
				<img src="/images/divider.png" style="height: 3px; width: auto;" />
				<div>
					<i class="fas fa-map-marker-alt icon-label-small"></i>
						${start} - ${end}
				</div>
				<div>
					<i class="fas fa-calendar icon-label-small"></i>
						${months[date.getMonth()]} ${date.getDate()}, ${date.getFullYear()}
					<i class="fas fa-clock icon-label-small"></i>
						${hour}:${date.getMinutes()} ${hourLabel}
				</div>
				<div>
					<i class="fas fa-users icon-label-small"></i>
						${size} / ${maxSize}
						<i class="fas fa-dollar-sign icon-label-small"></i>
						${price}
				</div>
			</div>`)
}

function setTripResults(data) {
	data.forEach(element => {
		let result = `<div class="result"><div class="result-trips">`;
		let e = element["trips"];

		for (let trip in e) {
			result = result + generateTrip(
				e[trip]["name"], e[trip]["start"],
				e[trip]["end"], e[trip]["date"],
				e[trip]["currentSize"], e[trip]["maxSize"],
				e[trip]["costPerPerson"]);
		}

		result = result + `</div><img src="../images/${element["status"]}-btn.png" class="${element["status"]}-btn"/>
							</div>`
		$(".results-content").append(result);
	});
}