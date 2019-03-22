/**
 * Set up global variables for use in all map actions.
 */
let map;
let mapboxClient;

/**
 * Hardcoded starting location - will replace later.
 */
let curLat = 42.358188;
let curLong = -71.058502;

let found = [];
let coordinates = [];
let markers = [];

/**
 * When the DOM loads, initialize Mapbox and the Map object.
 */
$(document).ready(function () {
    initMapbox();
    initMap();
    console.log("DOM ready.");
});

/**
 * Initializes the Mapbox using the accessToken and sets up the
 * mapboxClient for use in Geolocating.
 */
function initMapbox() {
    mapboxgl.accessToken = 'pk.eyJ1IjoiYXJ2Mzk1IiwiYSI6ImNqdGpodWcwdDB6dXEzeXBrOHJyeGVpNm8ifQ.bAwH-KG_5A5kwIxCf6xCSQ';
    mapboxClient = mapboxSdk({
        accessToken: mapboxgl.accessToken
    });
}

/**
 * Initializes the Map.
 */
function initMap() {
    // Create map object with custom settings and add NavigationControl
    map = new mapboxgl.Map({
        container: 'map',
        keyboard: false,
        maxZoom: 18,
        style: 'mapbox://styles/mapbox/streets-v11',
        center: [curLong, curLat],
        zoom: 12
    });
    map.addControl(new mapboxgl.NavigationControl());

    /**
     * Hide loading gif and show all initially hidden objects
     */
    $("#loading").css({
        visibility: "hidden"
    });
    $("[id=pre-load]").css({
        visibility: "visible"
    });
    console.log("Map loaded.");
}

/**
 * Handle changes to the address input boxes whenever the input box loses focus.
 *
 * @param {*} id
 *            The name of the input box (either 'start-input' or 'end-input')
 * @param {*} index
 *            The index to use for identification (either 0 or 1)
 */
function handleInput(id, index) {
    // Get the address value from the correct input box
    let address = $(`#${id}`).val()
    if (address === "") {
        removeMarker(index);
        return;
    }

    $(`#loading-${id}`).css({
        visibility: "visible"
    });

    setTimeout(function () {
        // Send network request for geocoding based on address box value
        mapboxClient.geocoding.forwardGeocode({
                query: address,
                proximity: [curLong, curLat],
                autocomplete: true,
                limit: 1
            })
            .send()
            .then(function (response) {
                // If valid response
                if (response && response.body && response.body.features &&
                    response.body.features.length) {
                    /**
                     * Get the first element of the suggestions, set the input box to that
                     * value, then update the addressNames and coordinates arrays with the
                     * feature data.
                     * */
                    let feature = response.body.features[0];
                    $(`#${id}`).val(feature.place_name);
                    coordinates[index] = feature.center;
                    // Add new marker on the map with the returned feature data
                    addStreetPoint(feature.center[1], feature.center[0], id, index);
                }
                $(`#loading-${id}`).css({
                    visibility: "hidden"
                });
            });
    }, 800);
}

/**
 * Add a new street point on the map after a new address is inputted.
 *
 * @param {*} lat
 *            The marker center latitude.
 * @param {*} long
 *            The marker center longitude.
 * @param {*} id
 *            The name of the input box (either 'start-input' or 'end-input')
 * @param {*} index
 *            The index to use for identification (either 0 or 1)
 */
function addStreetPoint(lat, long, id, index) {
    /**
     * Add a new marker on the map then mark the associated input type as found
     */
    addMarker(lat, long, id, index);
    found[index] = true;

    /**
     * If both
     */
    if (found[0] && found[1]) {
        map.fitBounds(coordinates, {
            padding: {
                top: 150,
                bottom: 100,
                left: 500,
                right: 150
            },
            linear: false
        });

    } else {
        moveToLocation(lat, long);
    }
}

/**
 * Add marker on the map with the given parameters.
 *
 * @param {*} lat
 *            The marker center latitude.
 * @param {*} long
 *            The marker center longitude.
 * @param {*} id
 *            The name of the input box (either 'start-input' or 'end-input')
 * @param {*} index
 *            The index to use for identification (either 0 or 1)
 */
function addMarker(lat, long, id, index) {
    let el = document.createElement('div');
    el.className = `marker ${id}`;
    if (id == "start-input") {
        el.className = el.className + " pulse";
    }
    if (markers[index]) {
        markers[index].remove();
    }
    markers[index] = new mapboxgl.Marker(el)
        .setLngLat([long, lat]);
    markers[index].addTo(map);
}

/**
 * Removes a marker from the map given index to remove.
 *
 * @param {*} index
 *            Index to remove
 */
function removeMarker(index) {
    if (markers[index]) {
        markers[index].remove();
        delete markers[index];
        delete coordinates[index];
        found[index] = false;
    }
}

/**
 * Centers the map view.
 */
function centerMap() {
    map.flyTo({
        center: [
            curLong, curLat
        ],
        pitch: 0,
        bearing: 0,
        zoom: 12
    });
}

/**
 * Realigns the map view to north and 0 tilt.
 */
function alignMap() {
    map.flyTo({
        pitch: 0,
        bearing: 0
    });
}

/**
 * Moves the map view to a new center location with the
 * provided arguments.
 *
 * @param {*} lat
 * @param {*} lng
 */
function moveToLocation(lat, lng) {
    map.flyTo({
        center: [
            lng, lat,
        ],
        zoom: 12
    });
}