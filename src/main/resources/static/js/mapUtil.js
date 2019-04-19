let mapboxClient;

/**
 * Hardcoded starting location - will replace later.
 */
let curLat = 42.358188;
let curLong = -71.058502;

/**
 * Initializes the Mapbox using the accessToken and sets up the
 * mapboxClient for use in Geolocating.
 */
function initMapbox() {
    mapboxClient = mapboxSdk({
        accessToken: mapboxgl.accessToken
    });
}

/**
 * Gets the current location if geolocation is enabled.
 */
function getLocation() {

    // Default to using ip lookup (faster + no popups)
    $.get("https://jsonip.com/", function (ipData) {
        if (ipData.status === "fail") {
            // Fallback to using browser geolocation
            geolocateLoc();
        } else {
            $.get("http://ip-api.com/json/" + ipData.ip, function (locData) {
                if (locData.status === "fail") {
                    geolocateLoc();
                }
                let position = {
                    coords: {latitude: locData.lat, longitude: locData.lon}
                };
                initMap(position);
            });
        }
    });


}

function geolocateLoc() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(initMap, defaultMap);
    } else {
        const defaultPosition = {
            coords: {latitude: curLat, longitude: curLong}
        };
        initMap(defaultPosition);
    }
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
    if (map) {
        // Get the address value from the correct input box
        let address = $(`#${id}`).val();
        if (address === "") {
            removeMarker(index);
            disableTrip();
            return;
        }

        $(`#loading-${id}`).css({
            visibility: "visible"
        });

        setTimeout(function () {
            // Send network request for geocoding based on address box value
            mapboxClient.geocoding
                        .forwardGeocode({
                            query: address,
                            proximity: [curLong, curLat],
                            autocomplete: true,
                            limit: 1
                        })
                        .send()
                        .then(function (response) {
                            // If valid response
                            if (response && response.body &&
                                response.body.features &&
                                response.body.features.length) {
                                /**
                                 * Get the first element of the suggestions, set
                                 * the input box to that value, then update the
                                 * addressNames and coordinates arrays with the
                                 * feature data.
                                 * */
                                let feature = response.body.features[0];
                                $(`#${id}`).val(feature.place_name);
                                console.log(feature);
                                coordinates[index] = feature.center;
                                // Add new marker on the map with the returned
                                // feature data
                                addStreetPoint(feature.center[1],
                                    feature.center[0],
                                    id, index, feature.place_name);
                            }
                            $(`#loading-${id}`).css({
                                visibility: "hidden"
                            });
                        });
        }, 800);
    }
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
 * @param {*} name
 *            The name of the location
 */
function addStreetPoint(lat, long, id, index, name) {
    /**
     * Add a new marker on the map then mark the associated input type as found
     */
    addMarker(lat, long, id, index, name, map);
    found[index] = true;

    /**
     * If both are found, align the map view based on the trip coordinates.
     * Otherwise, move to the location of the given address.
     */
    if (found[0] && found[1]) {
        alignTrip();
        updateRoute();
        enableTrip();
    } else {
        moveToLocation(lat, long);
    }
}

/**
 * Disable the trip realign button and hide route information modal
 */
function disableTrip() {
    $(".trip-setting").css({
        background: "#a5a5a5",
        cursor: "auto"
    });
    $("#car-icon").attr("src", "/images/car-disabled.png");
    $("#route-info").fadeOut(FADE_SPEED);
}

/**
 * Enable the trip realign button and show route information modal
 */
function enableTrip() {
    $(".trip-setting").css({
        background: "#fff",
        cursor: "pointer"
    });
    $("#car-icon").attr("src", "/images/car-icon.png");
    $("#route-info").fadeIn(FADE_SPEED);
}

/**
 * Clear the given trip from the input box.
 * @param id
 * @param index
 */
function clearTrip(id, index) {
    if (markers[index]) {
        $(`#${id}`).val("");
        removeMarker(index);
        disableTrip();
    }
}

/**
 * Centers the map view.
 */
function centerMap() {
    map.flyTo({
        center: [curLong, curLat],
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
        center: [lng, lat],
        zoom: 12
    });
}

/**
 * Realigns the map view based on the current trip coordinates.
 */
function alignTrip() {
    if (found[0] && found[1]) {
        let top = 400;
        let left = 275;
        let right = 150;
        let bottom = 150;
        /**
         * Checks if the trip positions are routed diagonally upwards or if the
         * window height is below a threshold. Then the map view is adjusted
         * since the search menu will not cover up the trip.
         */
        if (
            (coordinates[0][0] < coordinates[1][0] && coordinates[0][1] <
                coordinates[1][1]) ||
            (coordinates[0][0] > coordinates[1][0] && coordinates[0][1] >
                coordinates[1][1]) ||
            $("#map").height() < 600) {
            top = 150;
        }
        /**
         * Checks if in half screen vertical mode and adjust map view.
         */
        if ($("#map").height() < 600 && $("#map").width() >= 767) {
            right = 50;
            bottom = 50;
        }
        /**
         * Checks if the window width is below a threshold. Then the map view
         * is adjusted since the search menu will not cover up the trip.
         */
        if ($("#map").width() < 767) {
            top = 100;
            left = 75;
            right = 75;
        }
        // Fits the bounds of the map to the given padding sizes.
        map.fitBounds(coordinates, {
            padding: {
                top: top,
                bottom: bottom,
                left: left,
                right: right
            },
            linear: false
        });
    }
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
        route = [];
        removeRoute();
        found[index] = false;
    }
}

/**
 * Updates the route visualization on the map.
 */
function updateRoute() {
    removeRoute();
    calcRoute(coordinates.join(";"));
}

/**
 * Calculates the route direction coordinates based on starting and ending
 * locations using the Mapbox directions API.
 *
 * @param {*} c
 */
function calcRoute(c) {
    let url =
            "https://api.mapbox.com/directions/v5/mapbox/driving/" +
            c +
            "?geometries=geojson&&access_token=" +
            mapboxgl.accessToken;
    $.get(url, responseJSON => {
        route = [
            responseJSON.routes[0].distance * 0.001,
            responseJSON.routes[0].duration / 60
        ];
        setTripInfo();

        let coords = responseJSON.routes[0].geometry;
        addRoute(coords, map);
    }, "json");
}

/**
 * Sets the trip info test boxes to the appropriate distance and duration
 */
function setTripInfo() {
    $("#distance").text(`${((route[0]) * 0.621371).toFixed(2)} mi`);
    $("#duration").text(`${route[1].toFixed(0)} min`);
}

/**
 * Removes the route visualization from the map.
 */
function removeRoute() {
    if (map.getSource("route")) {
        map.removeLayer("route");
        map.removeSource("route");
    } else {
        return;
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
 * @param {*} name
 *            The name of the location
 * @param {*} map
 *            The mapbox object to manipulate
 */
function addMarker(lat, long, id, index, name, map) {
    let el = document.createElement("div");
    el.className = `marker ${id}`;
    if (id === "start-input") {
        el.className = el.className + " pulse";
    }
    if (markers[index]) {
        markers[index].remove();
    }
    let popup = new mapboxgl.Popup({
        offset: 25
    }).setHTML(parseAddress(name, index));

    markers[index] = new mapboxgl.Marker(el).setLngLat([long, lat]).setPopup(
        popup);
    markers[index].addTo(map);
}

/**
 * Parse an address and return formatted HTML code.
 *
 * @param {*} raw
 * @param {*} index
 */
function parseAddress(raw, index) {
    if (raw.indexOf(",") > -1) {
        let title = raw.substr(0, raw.indexOf(","));
        addressNames[index] = title;
        return `<div class="popup-title">${title}</div>
                <img src="/images/divider.png" style="height: 2px; width: auto;" />
                <div class="popup-content">${raw.substr(
            raw.indexOf(",") + 1)}</div>`;
    } else {
        addressNames[index] = raw;
        return `<div class="popup-title">${raw}</div>`;
    }
}

function parseAddressOnClick(raw) {
    if (raw.indexOf(",") > -1) {
        let title = raw.substr(0, raw.indexOf(","));
        return `<div class="popup-title">${title}</div>
                <img src="/images/divider.png" style="height: 2px; width: auto;" />
                <div class="popup-content">
                    ${raw.substr(raw.indexOf(",") + 1)}
                </div>`;
    } else {
        return `<div class="popup-title">${raw}</div>`;
    }
}

/**
 * Adds the route visualization to the map based on the given set of
 * coordinates.
 *
 * @param {*} coords
 * @param {*} map
 */
function addRoute(coords, map) {
    if (map.getSource("route")) {
        map.removeLayer("route");
        map.removeSource("route");
    } else {
        map.addLayer({
            id: "route",
            type: "line",
            source: {
                type: "geojson",
                data: {
                    type: "Feature",
                    properties: {},
                    geometry: coords
                }
            },
            layout: {
                "line-join": "round",
                "line-cap": "round"
            },
            paint: {
                "line-color": "#47A5FF",
                "line-width": 7,
                "line-opacity": 0.7
            }
        });
    }
}
