/**
 * Set up global variables for use in all map actions.
 */
let map;

let markers = [];
let addressNames = [];
let route = [];

let curLat = 42.358188;
let curLong = -71.058502;

$(document).ready(function () {
	showHomeInfo();
	initMapbox();
	initMap();
});

/**
 * Initializes the Map.
 */
function initMap() {
	// Create map object with custom settings and add NavigationControl
	console.log(coordinates);
	map = new mapboxgl.Map({
		container: "map",
		keyboard: false,
		maxZoom: 18,
		style: "mapbox://styles/mapbox/streets-v11",
		center: coordinates[0],
		zoom: 12,
		interactive: false,
	});
	setRoute();
	console.log("Map loaded.");
}

function setRoute() {
	map.fitBounds(coordinates, {
		padding: {
			top: 75,
			bottom: 100,
			left: 75,
			right: 75
		},
		linear: false
	});
	addMarker(coordinates[0][1], coordinates[0][0], "start-input",
		0, startName, map);
	addMarker(coordinates[1][1], coordinates[1][0], "end-input",
		1, endName, map);
	calcRoute(coordinates.join(";"));
}

/** Sets up the button click handlers */
function joinClick(uid) {
	const data = {
		action: "join",
		user: uid
	}
}
function leaveClick(uid) {
	const data = {
		action: "leave",
		user: uid
	}
}
function deleteClick(tid) {
	const data = {
		action: "delete",
	}
}
function approveClick(tid, pendUID) {
	const data = {
		action: "approve",
		user: pendUID
	}
}
function denyClick(tid, pendUID) {
	const data = {
		action: "deny",
		user: pendUID
	}
}