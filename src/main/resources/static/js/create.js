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
 * Set up tooltips for map page.
 */
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
    getLocation();
    showHomeInfo();
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
    signInTooltip[0].hide();
}

/**
 * Overridden function for user sign out action.
 */
function onUserSignedOut() {
    console.log("User signed out.");
    signInTooltip[0].show();
}

/**
 * Initializes the Map.
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
        zoom: 12,
        interactive: false
    });
    /**
     * Hide loading gif and show all initially hidden objects
     */
    $("#loading").css({
        visibility: "hidden"
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
        theme: "drawbridge-alt2",
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
 * Disable the trip realign button and hide route information modal
 */
function disableTrip() {
    $("#route-info").css({
        visibility: "hidden"
    });
}

/**
 * Enable the trip realign button and show route information modal
 */
function enableTrip() {
    $("#route-info").css({
        visibility: "visible"
    });
}

/**
 * Handle submit response when the submit button is pressed.
 */
function handleSubmit() {
    hideFormTooltips();
    let dateInput = $("#date").val();
    let timeInput = $("#time").val();
    let date = new Date(`${dateInput} ${timeInput}`);

    let nameInput = $("#name").val();
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
    if (dateInput === "" || timeInput === "" || typeInput === "" || nameInput
        === "" || sizeInput === "" || priceInput === "" || phoneInput === "") {
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
                startLat: coordinates[0][1],
                startLon: coordinates[0][0],
                endLat: coordinates[1][1],
                endLon: coordinates[1][0],
                date: date.getTime(),
                size: sizeInput,
                price: priceInput,
                method: typeInput,
                phone: phoneInput,
                name: nameInput,
                comments: commentsInput,
                userID: userProfile.getId(),
                eta: route[1]
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
