/**
 * Set up global variables for use in all map actions.
 */
let map;

let found = [];
let coordinates = [];
let markers = [];

let addressNames = [];
let route = [];

/**
 * Set up tooltips for map page, both form validation and map control
 * tooltips
 */
let formValidationTooltip;
let mapControlTooltips;

// Set up reference to the dropped pin on map press
let droppedPin;

/**
 * When the DOM loads, initialize Mapbox and the Map object.
 */
$(document).ready(function () {
    initMapbox();
    getLocation();
    initDateTime();
    initTooltips();
    disableTrip();
    console.log("DOM ready.");
});

/**
 * Overridden function for user sign in action.
 */
function onUserSignedIn() {
    console.log("User signed in.");
}

/**
 * Overridden function for user sign out action.
 */
function onUserSignedOut() {
    console.log("User signed out.");
}

/**
 * Gets the current location if geolocation is enabled.
 */
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(initMap, defaultMap);
    } else {
        defaultMap("Browser doesn't support geolocation");
    }
}

function defaultMap(error) {
    $.get("https://jsonip.com/", function(ipData) {
        if (ipData.status === "fail") {
            initMap(undefined);
        } else {
            $.get("http://ip-api.com/json/" + ipData.ip, function (locData) {
                let position = {coords: {latitude: locData.lat, longitude: locData.lon}};
                initMap(position);
            });
        }
    });
}

/**
 * Initializes the Map on the DOM.
 */
function initMap(position) {
    /**
     * If the position is valid from the navigator, set the current latitude
     * and longitude to the positions latitude and longitude.
     */
    if (position !== undefined) {
        curLat = position.coords.latitude;
        curLong = position.coords.longitude;
    }
    // Create map object with custom settings and add NavigationControl
    map = new mapboxgl.Map({
        container: "map",
        keyboard: false,
        maxZoom: 18,
        style: "mapbox://styles/mapbox/streets-v11",
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

    /**
     * Set up binding for map onclick event
     */
    map.on("click", function (event) {
        handleClick(event.lngLat);
    });
}

/**
 * Initialize date and time pickers.
 */
function initDateTime() {
    flatpickr("#date", {
        minDate: "today",
        altInput: true,
        dateFormat: "m/d/Y"
    });
    flatpickr("#time", {
        enableTime: true,
        noCalendar: true,
        altInput: true,
        dateFormat: "H:i"
    });
}

/**
 * Initialize the form validation tooltip
 */
function initTooltips() {
    formValidationTooltip = tippy("#requiredTooltip", {
        animation: "scale",
        arrow: true,
        arrowType: "round",
        theme: "drawbridge-alt",
        interactive: false,
        trigger: "manual",
        hideOnClick: false,
        inertia: true,
        sticky: true,
        placement: "top",
    });
    mapControlTooltips = tippy(".map-settings", {
        animation: "scale",
        arrow: true,
        arrowType: "round",
        theme: "drawbridge-alt",
        interactive: "true",
        hideOnClick: true,
        inertia: true,
        sticky: true,
        placement: "top",
    });
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
    coordinates[index] = [featureLng, featureLat];
    /**
     * Add new marker on the map with the returned feature data
     */
    addStreetPoint(featureLat, featureLng,
        id, index, featureName);
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
    $("#route-info").css({
        visibility: "hidden"
    });
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
    $("#route-info").css({
        visibility: "visible"
    });
}

/**
 * Handle submit response when the submit button is pressed.
 */
function validateSubmit() {
    let dateInput = $("#date").val();
    let timeInput = $("#time").val();
    let date = new Date(`${dateInput} ${timeInput}`);

    /**
     * If any of the input entries are not filled out, show the form validation
     * tooltip, wait
     * 3 seconds, then hide it, prompting the user to fill out the form
     * completely.
     */
    if (dateInput === "" || timeInput === "" || coordinates[0] === undefined ||
        coordinates[1] === undefined) {
        showHideTooltip(formValidationTooltip[0]);
    } else {
        /**
         * If the user is logged in, send a GET request with the UID.
         * Otherwise, send null as the UID, returning generic results.
         */
        let userID;
        if (userProfile === undefined) {
            userID = null;
        } else {
            userID = userProfile.getId();
        }

        const postParameters = {
            startName: addressNames[0],
            endName: addressNames[1],
            startLat: coordinates[0].slice(0).reverse()[0],
            startLon: coordinates[0].slice(0).reverse()[1],
            endLat: coordinates[1].slice(0).reverse()[0],
            endLon: coordinates[1].slice(0).reverse()[1],
            date: date.getTime(),
            userID: userID,
            eta: route[1]
        };
        let urlStr = jQuery.param(postParameters);
        window.open(`/results?${urlStr}`, "_self");
    }
}
