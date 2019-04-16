/**
 * Set up global variables for use in all map actions.
 */
let map;

let found = [];
let coordinates = [];
let markers = [];

let addressNames = [];
let route = [];

let formValidationTooltip;
let formTooltips = [];

/**
 * When the DOM loads, set the tooltip content and check if cookies are enabled
 * and show the home and info buttons.s
 */
$(document).ready(function () {
    signInTooltip[0].setContent(
        "Sign in with your Google Account to host a trip.");
    initMapbox();
    initMap();
    showHomeInfo();
    initDateTime();
    initTooltips();
    console.log("DOM ready.");
});

/**
 * Overriden function for user sign in action.
 */
function onUserSignedIn() {
    console.log("User signed in.");
    signInTooltip[0].hide();
}

/**
 * Overriden function for user sign out action.
 */
function onUserSignedOut() {
    console.log("User signed out.");
    signInTooltip[0].show();
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
                               center: [-71.058502, 42.358188],
                               zoom: 12,
                               interactive: false
                           });
    map.on('load', function () {
    });

    console.log("Map loaded.");
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
    formTooltips = tippy(".form-tooltip", {
        animation: "scale",
        arrow: true,
        arrowType: "round",
        theme: "drawbridge-alt",
        interactive: false,
        trigger: "manual",
        hideOnClick: false,
        inertia: true,
        placement: "bottom",
    });

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
    let address = $(`#${id}`).val();
    if (address === "" || address === addressNames[index]) {
        removeMarker(index);
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
                            response.body.features.length)
                        {
                            /**
                             * Get the first element of the suggestions, set
                             * the input box to that value, then update the
                             * addressNames and coordinates arrays with the
                             * feature data.
                             * */
                            let feature = response.body.features[0];
                            $(`#${id}`).val(feature.place_name);
                            coordinates[index] = feature.center;
                            addressNames[index] = feature.place_name;
                            // Add new marker on the map with the returned
                            // feature data
                            addStreetPoint(feature.center[1], feature.center[0],
                                           id, index, feature.place_name);
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
    } else {
        moveToLocation(lat, long);
    }
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

        let coords = responseJSON.routes[0].geometry;
        addRoute(coords, map);
    }, "json");
}

function clearTrip(id, index) {
    if (markers[index]) {
        $(`#${id}`).val("");
        removeMarker(index);
    }
}

/**
 * Handle submit response when the submit button is pressed.
 */
function handleSubmit() {
    hideFormTooltips();
    let dateInput = $("#date").val();
    let timeInput = $("#time").val();
    let date = new Date(`${dateInput} ${timeInput}`);

    let sizeInput = $("#carpool-size").val();
    let typeInput = $("#transport-type").val();
    let priceInput = $("#expected-price").val();
    let phoneInput = $("#contact-number").val();
    let commentsInput = $("#comments").val();
    let phoneRegex = new RegExp("[0-9]{3}-[0-9]{3}-[0-9]{4}");

    /**
     * If any of the input entries are not filled out, show the form validation
     * tooltip, wait
     * 3 seconds, then hide it, prompting the user to fill out the form
     * completely.
     */
    if (dateInput === "" || timeInput === "" || typeInput === "" ||
        sizeInput === "" || priceInput === "" || phoneInput === "")
    {
        showHideTooltip(formValidationTooltip[0]);
    } else {
        if (sizeInput < 1) {
            showHideTooltip(formTooltips[0]);
        } else if (priceInput < 0) {
            showHideTooltip(formTooltips[1]);
        } else if (!phoneRegex.test(phoneInput)) {
            showHideTooltip(formTooltips[2]);
        } else if (userProfile === undefined) {
            $("html, body").animate({
                                        scrollTop: 0
                                    },
                                    "slow"
            );
            signInTooltip[0].show();
        } else {
            const postParameters = {
                startName: addressNames[0],
                endName: addressNames[1],
                startLat: coordinates[0].slice(0).reverse()[0],
                startLon: coordinates[0].slice(0).reverse()[1],
                endLat: coordinates[1].slice(0).reverse()[0],
                endLon: coordinates[1].slice(0).reverse()[1],
                date: date.getTime(),
                size: sizeInput,
                price: priceInput,
                type: typeInput,
                phone: phoneInput,
                comments: commentsInput,
                userID: userProfile.getId()
            };
            console.log(postParameters);

            $.post("/new", postParameters, responseJSON => {
                console.log("Response");
            }, "json");
        }
    }
}

/**
 * Hide all the form tooltips.
 */
function hideFormTooltips() {
    for (let i = 0; i < 3; i++) {
        formTooltips[i].hide();
    }
}
