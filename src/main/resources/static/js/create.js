let addressNames = [];
let coordinates = [];

/**
 * When the DOM loads, set the tooltip content and check if cookies are enabled and
 * show the home and info buttons.s
 */
$(document).ready(function () {
    signInTooltip[0].setContent("Sign in with your Google Account to host a trip.");
    /**
     * If the user is not logged in or cookies are disabled, show the signin tooltip
     * prompting the user to sign in to view their trips.
     */
    if (navigator.cookieEnabled) {
        if (getCookie("loggedIn") != "true") {
            signInTooltip[0].show();
        }
    } else {
        signInTooltip[0].show();
    }
    showHomeInfo();
    initDateTime();
    initTooltips();
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
}

/**
 * Handle submit response when the submit button is pressed.
 */
function handleSubmit() {
    let dateInput = $("#date").val();
    let timeInput = $("#time").val();
    let date = new Date(`${dateInput} ${timeInput}`);

    /**
     * If any of the input entries are not filled out, show the form validation tooltip, wait
     * 3 seconds, then hide it, prompting the user to fill out the form completely.
     */
    if (dateInput === "" || timeInput === "" || coordinates[0] === undefined || coordinates[1] === undefined) {
        formValidationTooltip[0].show();
        setTimeout(function () {
            formValidationTooltip[0].hide();
        }, 3000);
    } else {

        if (userProfile != undefined) {
            const postParameters = {
                startName: addressNames[0],
                endName: addressNames[1],
                startCoordinates: coordinates[0].slice(0).reverse(),
                endCoordinates: coordinates[1].slice(0).reverse(),
                date: date.getTime(),
                userID: userProfile.getId()
            };
            console.log(postParameters);
        } else {
            $("html, body").animate({
                    scrollTop: 0
                },
                "slow"
            );
            signInTooltip[0].show();
        }
    }
}