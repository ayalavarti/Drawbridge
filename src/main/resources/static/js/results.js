const tempHeading = ["Boston", "Cambridge", "Mar 14 2019", "1:59 PM"];

const tempData = [{
		start: "Providence, RI",
		end: "New Haven, CT",
		date: "1553453944862",
		currentSize: "3",
		maxSize: "5",
		costPerPerson: "23",
		id: "1234",
		name: "Sams's Carpool",
		status: "joined"
	},
	{
		start: "Providence, RI",
		end: "Springfield, MA",
		date: "1553453944862",
		currentSize: "1",
		maxSize: "3",
		costPerPerson: "24",
		id: "2345",
		name: "Going to Airport",
		status: "pending"
	},
	{
		start: "Providence, RI",
		end: "Six Flags",
		date: "1553453944862",
		currentSize: "5",
		maxSize: "5",
		costPerPerson: "20",
		id: "3456",
		name: "Jeff's Carpool",
		status: "join"
	},
	{
		start: "Boston, MA",
		end: "Brown University",
		date: "1553453944862",
		currentSize: "3",
		maxSize: "5",
		costPerPerson: "15",
		id: "4567",
		name: "Arvind's Carpool",
		status: "join"
	},
	{
		start: "Yale University",
		end: "Harvard University",
		date: "1553453944862",
		currentSize: "4",
		maxSize: "5",
		costPerPerson: "14",
		id: "5678",
		name: "Mark's Carpool",
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

function setTripResults(data) {
	data.forEach(element => {
		$(".results-content").append(
			`<div class="result">
				<div class="result-info">
					<div>${element["name"]}</div>
					<img src="/images/divider.png" style="height: 3px; width: auto;" />
					<div style="font-size: 13px; text-align: left;">
						<i class="fas fa-dot-circle icon-label-small"></i>
							${element["start"]}
						<i class="fas fa-map-marker-alt icon-label-small"></i>
							${element["end"]}
					</div>
				</div>
				<img src="../images/${element["status"]}-btn.png" class="${element["status"]}-btn"/>
			</div>`
		);
	});
}