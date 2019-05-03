const months = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];
const walkSpeed = 0.05167;

let formValidationTooltip;

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
    queryTrips();
    // Hide the sign in tooltip if visible
    signInTooltip[0].hide();
}

function queryTrips() {
    let uid;
    if (userProfile) {
        uid = userProfile.getId();
    } else {
        uid = null;
    }
    let walkTime = $("#walking-input").val();
    let waitTime = $("#waiting-input").val();
    let postParameters = (payload);

    if (parseFloat(walkTime) > 0 && parseFloat(waitTime) > 0) {
        postParameters["walkTime"] = parseFloat(walkTime);
        postParameters["waitTime"] = parseFloat(waitTime);
    } else {
        showHideTooltip(formValidationTooltip[0], 2000);
        return;
    }
    postParameters["userID"] = uid;
    $("#carpool-results").html("");
    $("#loading").show();
    $("#walking-distance").hide();
    $.post("/results", postParameters, response => {
        // Set the trip results on the page with the resulting data
        payload = response.payload;
        data = response.data;
        setTripResults((response.data));
    }, "json");
}

/**
 * Overridden function for user sign out action.
 */
function onUserSignedOut() {
    queryTrips();
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
    formValidationTooltip = tippy("#search-btn", {
        animation: "scale",
        arrow: true,
        arrowType: "round",
        theme: "drawbridge-alt",
        interactive: false,
        trigger: "manual",
        hideOnClick: false,
        inertia: true,
        sticky: true,
        content: "Inputs should be positive numbers less than 3600.",
        placement: "top",
    });

}

/**
 * Query the server for the results of the trip search
 */
function queryResults() {
    const queryMap = (payload);
    $("#start").text(queryMap["startName"]);
    $("#end").text(queryMap["endName"]);

    let tripDate = new Date(queryMap["date"] * 1000);
    $("#date").text(
        `${tripDate.toDateString()}`);
    $("#time").text(`${toJSTime(tripDate)}`);
    if ((navigator.cookieEnabled && getCookie("loggedIn") === "true")) {
    } else {
        // Set the trip results on the page with the resulting data
        setTripResults(data);
    }
}

/**
 * Sets the trip results on the page with the data from the server
 * @param {*} data
 */
function setTripResults(data) {
    const queryMap = (payload);
    if (queryMap.tripDist < queryMap.walkTime * walkSpeed) {
        $("#walk-eta-time")
        .text(parseFloat(queryMap.tripDist / walkSpeed).toFixed(0));
        $("#walking-distance").show();
    }

    if (data.length > 0) {
        // Iterate through each trip group
        data.forEach(element => {
            let result = `<div class="result"><div class="result-trips">`;
            //Iterate through each connecting trip in a trip group
            for (let trip in element) {
                result += generateTrip(element[trip]);
            }
            result += `</div></div>`
            $("#carpool-results").append(result);
        });
    } else {
        $("#carpool-results").append(`<div class="empty-results">
			<img src="/images/no-trips-icon.png" /></div>`);
    }
    /**
     * Append a host trip button with hover effects and an onclick handler
     * to the end of the results-content div
     */
    $("#carpool-results").append(`<div><input name="host" alt="Host" type="image" src="/images/host-btn.png"
		class="host-btn" onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleHost();"/></div>`);
    $("#loading").hide();
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
