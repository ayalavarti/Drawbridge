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
    tutorialElements = [
        $(".header"), $(".search-menu-wrapper"), $(".team-setting"),
        $(".info-setting")
    ];

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
    $("#loading").hide();
    $("[id=pre-load]").fadeIn(FADE_SPEED);
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
        showHideTooltip(formValidationTooltip[0], 1500);
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
            startLat: coordinates[0][1],
            startLon: coordinates[0][0],
            endLat: coordinates[1][1],
            endLon: coordinates[1][0],
            date: date.getTime() / 1000,
            userID: userID,
            eta: route[1]
        };
        let urlStr = jQuery.param(postParameters);
        window.open(`/results?${urlStr}`, "_self");
    }
}
