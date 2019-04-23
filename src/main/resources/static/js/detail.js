/**
 * Set up global variables for use in all map actions.
 */
let map;

let markers = [];
let addressNames = [];
let copyTooltip;

/**
 * When the DOM is ready, show home and info buttons, initialize the
 * mapbox client, and initialize the map.
 */
$(document).ready(function () {
    showHomeInfo();
    initMapbox();
    initMap();
    initTooltips();
    if ((navigator.cookieEnabled && getCookie("loggedIn") === "true") ||
        tripFull) {
        $("#join-btn").hide();
    } else {
        $("#join-btn").show();
    }
    console.log("DOM ready.");
});


/**
 * Overridden function for user sign in action.
 */
function onUserSignedIn() {
    /**
     * Hide the sign in tooltip if it is visible.
     */
    console.log("User signed in.");
    signInTooltip[0].hide();
    let uid = userProfile.getId();

    if (uid === host) {
        $("#delete-btn").show();
        $("#join-btn").hide();
        $("#leave-btn").hide();
        $("#pending").show();
        $("#pending-label").hide();
        $("#action-btn").show();
        $("#edit-btn").show();
    } else if (containsUser(members, uid)) {
        $("#delete-btn").hide();
        $("#join-btn").hide();
        $("#leave-btn").show();
        $("#pending").hide();
        $("#pending-label").hide();
        $("#action-btn").hide();
    } else if (containsUser(pending, uid)) {
        $("#delete-btn").hide();
        $("#join-btn").hide();
        $("#leave-btn").show();
        $("#pending").hide();
        $("#action-btn").hide();
        $("#pending-label").show();
    } else {
        $("#join-btn").show();
    }
    if (tripFull) {
        $("#join-btn").hide();
        $("#full-label").show();
        $("[id=approve-btn]").hide();
    }
    console.log(uid);
    $(`#${uid}`).css({visibility: "visible"});
    $(`.${uid}`).attr("src", `${userProfile.getImageUrl()}`);
}

/**
 * Checks if a given user is in the given list of users.
 *
 * @param list
 * @param uid
 * @returns {boolean}
 */
function containsUser(list, uid) {
    for (let m in list) {
        if (uid === list[m]) {
            return true;
        }
    }
    return false;
}

/**
 * Overridden function for user sign out action.
 */
function onUserSignedOut() {
    console.log("User signed out.");
    $("#delete-btn").hide();
    $("#leave-btn").hide();
    $("#pending").hide();
    $("#pending-label").hide();
    $("#edit-btn").show();

    if (!tripFull) {
        $("#join-btn").show();
    }
    $("[class=verified]").css({visibility: "hidden"});
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
        center: coordinates[0],
        zoom: 12,
        interactive: false
    });
    map.on('load', function () {
        setRoute();
    });

    console.log("Map loaded.");
}

function initTooltips() {
    copyTooltip = tippy("#clipboardTooltip", {
        animation: "scale",
        arrow: true,
        arrowType: "round",
        theme: "drawbridge-alt2",
        interactive: false,
        trigger: "manual",
        hideOnClick: false,
        maxWidth: 250,
        inertia: true,
        sticky: true,
        placement: "bottom",
    });
}

let tripID;
let editing = false;

function editComment(tid) {
    tripID = tid;
    if (!editing) {
        $("#edit-img").attr("src", "/images/arrow-label.png");
        $("#cancel-btn").show();
        $("#true-comment").hide();
        $("#comment-input").show();
        $(".comments-textarea").focus();
        editing = true;
    } else {
        const data = {
            action: "editComment",
            newComment: $(".comments-textarea").val(),
            user: userProfile.getId()
        };
        console.log(data);
        //sendRequest(data, "/trip/" + tid);
    }
}

function cancelEditing() {
    if (editing) {
        $("#edit-img").attr("src", "/images/pencil-icon.png");
        $("#cancel-btn").hide();
        $("#true-comment").show();
        $("#comment-input").hide();
        $(".comments-textarea").blur();
        editing = false;
    }
}

/**
 * Sets the route on the map.
 */
function setRoute() {
    // Fit the map to the coordinates of the trip starting and ending
    // coordinates
    map.fitBounds(coordinates, {
        padding: {
            top: 40,
            bottom: 50,
            left: 40,
            right: 40
        },
        linear: false
    });

    // Add the two markers and draw the route
    addMarker(coordinates[0][1], coordinates[0][0], "start-input", 0, startName,
        map);
    addMarker(coordinates[1][1], coordinates[1][0], "end-input", 1, endName,
        map);
    drawRoute(coordinates.join(";"));
}

/**
 * Button click handler for a join request.
 *
 * @param {*} tid
 */
function joinClick(tid) {
    if (userProfile === undefined) {
        $("html, body").animate({scrollTop: 0}, "slow");
        signInTooltip[0].setContent(
            "Sign in with your Google Account to join this trip.");
        signInTooltip[0].show();

    } else {
        const data = {
            action: "join",
            user: userProfile.getId()
        };
        sendRequest(data, "/trip/" + tid);
    }
}

/**
 * Button click handler for a leave request.
 *
 * @param {*} tid
 */
function leaveClick(tid) {
    const data = {
        action: "leave",
        user: userProfile.getId()
    };
    sendRequest(data, "/trip/" + tid);
}

/**
 * Button click handler for a delete request.
 *
 * @param {*} tid
 */
function deleteClick(tid) {
    const data = {
        action: "delete",
        user: userProfile.getId()
    };
    sendRequest(data, "/trip/" + tid);
}

/**
 *  Button click handler for an approve request.
 *
 * @param {*} tid
 * @param {*} pendUID
 */
function approveClick(tid, pendUID) {
    const data = {
        action: "approve",
        user: userProfile.getId(),
        pendingUser: pendUID
    };
    sendRequest(data, "/trip/" + tid);
}

/**
 *  Button click handler for a deny request.
 *
 * @param {*} tid
 * @param {*} pendUID
 */
function denyClick(tid, pendUID) {
    const data = {
        action: "deny",
        user: userProfile.getId(),
        pendingUser: pendUID
    };
    sendRequest(data, "/trip/" + tid);
}

/**
 *  Sends a POST request to the server.
 *
 * @param {*} data
 * @param {*} url
 */
function sendRequest(data, url) {
    $.post(url, data, response => {
        if (response.success) {
            if (response.redirect) {
                window.location.replace(response.redirect);
            } else {
                window.location.reload(true);
            }
        } else {
            window.location.replace("/error");
        }
    }, "json");
}

/**
 * Draws the route on the map
 * @param {*} c
 */
function drawRoute(c) {
    let url =
            "https://api.mapbox.com/directions/v5/mapbox/driving/" +
            c +
            "?geometries=geojson&&access_token=" +
            mapboxgl.accessToken;

    $.get(url, responseJSON => {
        let coords = responseJSON.routes[0].geometry;
        addRoute(coords, map);
    }, "json");
}

function copyToClipboard() {
    //let text = `localhost:8000/trip/${tid}`;
    navigator.clipboard.writeText(window.location).then(function () {
        showHideTooltip(copyTooltip[0], 1500);
    }, function (err) {
        console.error('Async: Could not copy text: ', err);
    });
}
