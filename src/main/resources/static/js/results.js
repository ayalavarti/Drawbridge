const months = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

/**
 * When the DOM loads, show the home and info buttons, query for the
 * results of the search, and initialize tooltips.
 */
$(document).ready(function () {
    showHomeInfo();
    queryResults();
    initTooltips();
});

/**
 * Overridden function for user sign in action.
 */
function onUserSignedIn() {
    console.log("User signed in.");
    // Hide the sign in tooltip if visible
    signInTooltip[0].hide();
}

/**
 * Overridden function for user sign out action.
 */
function onUserSignedOut() {
    console.log("User signed out.");
}

/**
 * Initialize the info tooltips
 */
function initTooltips() {
    tippy("#info", {
        animation: "scale",
        arrow: true,
        maxWidth: "250px",
        arrowType: "round",
        theme: "drawbridge",
        hideOnClick: false,
        inertia: true,
        placement: "bottom",
    });
}

/**
 * Query the server for the results of the trip search
 */
function queryResults() {
    const queryMap = JSON.parse(payload);
    $("#start").text(queryMap["startName"]);
    $("#end").text(queryMap["endName"]);

    let tripDate = new Date(queryMap["date"] * 1000);
    let hourLabel = "AM";
    let minutes;
    let hour = tripDate.getHours();
    if (hour >= 12) {
        hour = hour - 12;
        hourLabel = "PM";
    }
    if (hour === 0) {
        hour = 12;
    }
    if (tripDate.getMinutes() < 10) {
        minutes = `${tripDate.getMinutes()}0`;
    } else {
        minutes = tripDate.getMinutes();
    }

    $("#date").text(
        `${months[tripDate.getMonth()]} ${tripDate.getDate()},
        ${tripDate.getFullYear()}`);
    $("#time").text(`${hour}:${minutes} ${hourLabel}`);

    // Set the trip results on the page with the resulting data
    setTripResults(JSON.parse(data));
}

/**
 * Sets the trip results on the page with the data from the server
 * @param {*} data
 */
function setTripResults(data) {

    if (data.length > 0) {
        // Iterate through each trip group
        data.forEach(element => {
            let result = `<div class="result"><div class="result-trips">`;
            //Iterate through each connecting trip in a trip group
            for (let trip in element) {
                result += generateTrip(element[trip]);
            }
            result += `</div></div>`
            $(".results-content").append(result);
        });
    } else {
        $(".results-content").append(`<div class="empty-results">
			<img src="/images/no-trips-icon.png" /></div>`);
    }
    /**
     * Append a host trip button wiht hover effects and an onclick handler
     * to the end of the results-content div
     */
    $(".results-content").append(`<div><input name="host" alt="Host" type="image" src="/images/host-btn.png"
		class="host-btn" onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleHost();"/></div>`);
}

/**
 * Handles a join button press.
 * @param {*} id
 */
function handleJoin(ids) {
    /**
     * If the user is not logged in, scroll to the top of the screen and
     * display the sign in tooltip prompting the user to sign in.
     *
     * Otherwise, perform a join trip request if the user is not already a
     * member/pending/host
     */
    if (userProfile === undefined) {
        $("html, body").animate({
            scrollTop: 0
        }, "slow");
        signInTooltip[0].setContent(
            "Sign in with your Google Account to join an existing trip.");
        signInTooltip[0].show();
    } else {
        console.log(ids);
    }
}

/**
 * Handles a host button press.
 */
function handleHost() {
    /**
     * If the user is not logged in, scroll to the top of the screen and
     * display the sign in tooltip prompting the user to sign in.
     *
     * Otherwise, perform a host trip request
     */
    if (userProfile === undefined) {
        $("html, body").animate({
            scrollTop: 0
        }, "slow");
        signInTooltip[0].setContent(
            "Sign in with your Google Account to host your own trip.");
        signInTooltip[0].show();
    } else {
        window.open("/new", "_self");
    }
}
