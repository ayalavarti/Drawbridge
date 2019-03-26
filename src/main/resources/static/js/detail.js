/**
 * Set up global variables for use in all map actions.
 */
let map;

let markers = [];
let addressNames = [];

let curLat = 42.358188;
let curLong = -71.058502;

$(document).ready(function () {
	showHomeInfo();
	initMapbox();
	initMap();
});

function onUserSignedIn() {
	console.log("User signed in.");
}

function onUserSignedOut() {
	console.log("User signed out.");
}

/**
 * Initializes the Map.
 */
function initMap() {
	// Create map object with custom settings and add NavigationControl
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
	drawRoute(coordinates.join(";"));
}

function drawRoute(c) {
	let url =
		"https://api.mapbox.com/directions/v5/mapbox/driving/" +
		c +
		"?geometries=geojson&&access_token=" +
		mapboxgl.accessToken;

	let req = new XMLHttpRequest();
	req.responseType = "json";
	req.open("GET", url, true);
	req.onload = function () {
		let jsonResponse = req.response;
		let coords = jsonResponse.routes[0].geometry;
		addRoute(coords, map);
	};
	req.send();
}