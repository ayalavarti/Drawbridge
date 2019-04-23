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
    let fallback = function (xhr, textStatus, error) {
        geolocateLoc();
    };
    let maxTime = 1500;

    $.ajax({
        type: "GET",
        url: "https://jsonip.com/",
        timeout: maxTime,
        error: fallback,
        dataType: "json",
        success: function (ipData, textStatus, xhr) {
            if (ipData.status === "fail") {
                geolocateLoc();
            } else {
                $.ajax({
                    type: "GET",
                    url: "http://ip-api.com/json/" + ipData.ip,
                    timeout: maxTime,
                    error: fallback,
                    dataType: "json",
                    success: function (locData, textStatus, xhr) {
                        if (locData.status === "fail") {
                            geolocateLoc();
                        } else {
                            let position = {
                                coords: {
                                    latitude: locData.lat,
                                    longitude: locData.lon
                                }
                            };
                            initMap(position);
                            console.log("IP Lookup");
                        }
                    }
                });
            }
        }
    });


}

function geolocateLoc() {
    let defaultMap = () => initMap(
        {coords: {latitude: curLat, longitude: curLong}});
    if (navigator.geolocation) {
        console.log("Geolocating");
        navigator.geolocation.getCurrentPosition(initMap, defaultMap,
            {timeout: 4000});
    } else {
        console.log("Defaulting");
        const defaultPosition = {
            coords: {latitude: curLat, longitude: curLong}
        };
        initMap(defaultPosition);
    }
}

let typingTimerStart;
let typingTimerEnd;
let finishTypingInterval = 1500;

$("#start-input").on('keyup', function () {
    clearTimeout(typingTimerStart);
    typingTimerStart = setTimeout(function () {
            if ($.trim($('#start-input').val()) !== '') {
                $("#start-input").blur();
                $("#end-input").focus();
            }
        },
        finishTypingInterval);
});

$("#start-input").on('keydown', function () {
    clearTimeout(typingTimerStart);
});

$("#start-input").on('blur', function () {
    clearTimeout(typingTimerStart);
});


$("#end-input").on('keyup', function () {
    clearTimeout(typingTimerEnd);
    typingTimerEnd = setTimeout(function () {
            if ($.trim($('#end-input').val()) !== '') {
                $("#end-input").blur();
            }
        },
        finishTypingInterval);
});

$("#end-input").on('keydown', function () {
    clearTimeout(typingTimerEnd);
});

$("#end-input").on('blur', function () {
    clearTimeout(typingTimerEnd);
});

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
        if ($.trim(address) === "") {
            removeMarker(index);
            disableTrip();
            $(`#${id}`).val("");
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
                            $(".clear-btn").css({visibility: "visible"});
                        });
        }, 800);
    }
}

/**
 * Handles onclick events on the map element to set up a popup. Lets the user
 * choose whether to set the pin location as their starting or ending location.
 * @param coord
 */
function handleClick(coord) {
    setTimeout(function () {
        // Send network request for reverse geocoding based on clicked location
        mapboxClient.geocoding
                    .reverseGeocode({
                        query: [coord["lng"], coord["lat"]],
                        limit: 1
                    })
                    .send()
                    .then(function (response) {
                        // If valid response
                        if (response && response.body &&
                            response.body.features &&
                            response.body.features.length) {
                            let feature = response.body.features[0];
                            /**
                             * Drop the pin on the map, parse the resulting
                             * feature address and create a popup with
                             * buttons that let the user select where to use
                             * the new address in their route.
                             */
                            droppedPin = new mapboxgl.Popup()
                            .setLngLat(feature["center"])
                            .setHTML(`
                                ${parseAddressOnClick(feature.place_name)}
                                <button onclick="updateAddress('start-input', 0,
                                    '${feature.place_name}', '${feature.center[1]}',
                                    '${feature.center[0]}');" 
                                    class="setLocation-btn">
                                    Set Starting
                                </button>
                                
                                <button onclick="updateAddress('end-input', 1,
                                    '${feature.place_name}', '${feature.center[1]}',
                                    '${feature.center[0]}');" 
                                    class="setLocation-btn">
                                    Set Ending
                                </button>
                            `)
                            .addTo(map);
                        }
                    });
    }, 800);
}

/**
 * Update the address on when click "Set Starting" or "Set Ending" on a
 * given map popup.
 *
 * @param id
 * @param index
 * @param featureName
 * @param featureLat
 * @param featureLng
 */
function updateAddress(id, index, featureName, featureLat, featureLng) {
    droppedPin.remove();
    $(`#${id}`).val(featureName);
    coordinates[index] = [parseFloat(featureLng), parseFloat(featureLat)];
    /**
     * Add new marker on the map with the returned feature data
     */
    addStreetPoint(featureLat, featureLng,
        id, index, featureName);
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

function hideClearButtons() {
    $(".clear-btn").css({visibility: "hidden"});
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
            window.height < 600) {
            top = 150;
        }
        /**
         * Checks if in half screen vertical mode and adjust map view.
         */
        if (window.height < 600 && window.width >= 767) {
            right = 50;
            bottom = 50;
        }
        /**
         * Checks if the window width is below a threshold. Then the map view
         * is adjusted since the search menu will not cover up the trip.
         */
        if (window.width < 767) {
            top = 100;
            left = 75;
            right = 75;
        }

        if (window.location.pathname === "/new") {
            left = 200;
            right = 50;
            top = 50;
            bottom = 100;
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
