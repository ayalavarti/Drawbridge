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
 * Overriden function for user sign in action.
 */
function onUserSignedIn() {
    console.log("User signed in.");
    // Hide the sign in tooltip if visible
    signInTooltip[0].hide();
}

/**
 * Overriden function for user sign out action.
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
    // Set the trip results on the page with the resulting data
    console.log(data);
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
 * @param {*} id
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
        console.log("HOST");
    }
}
