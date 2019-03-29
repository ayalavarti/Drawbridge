const months = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

const tempData = [{
        trips: {
            trip1: {
                start: "Providence, RI",
                end: "New Haven, CT",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "15",
                id: "1",
                name: "Jeff's Carpool 1",
            },
            trip2: {
                start: "New Haven, CT",
                end: "New York, NY",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "20",
                id: "2",
                name: "Sam's Carpool 1",
            },
            trip3: {
                start: "New York, NY",
                end: "Marlboro, NJ",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "10",
                id: "3",
                name: "Arvind's Carpool 1",
            }
        },
        totalPrice: "24",
        totalTime: "260",
        status: "host"
    },
    {
        trips: {
            trip1: {
                start: "Providence, RI",
                end: "New Haven, CT",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "15",
                id: "4",
                name: "Jeff's Carpool 2",
            },
            trip2: {
                start: "New Haven, CT",
                end: "Marlboro, NJ",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "20",
                id: "5",
                name: "Mark's Carpool 2",
            }
        },
        totalPrice: "2",
        totalTime: "220",
        status: "host"
    },
    {
        trips: {
            trip1: {
                start: "Providence, RI",
                end: "New Haven, CT",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "15",
                id: "6",
                name: "Jeff's Carpool 3",
            },
            trip2: {
                start: "New Haven, CT",
                end: "Marlboro, NJ",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "20",
                id: "7",
                name: "Mark's Carpool 3",
            }
        },
        totalPrice: "2",
        totalTime: "220",
        status: "member"
    },
    {
        trips: {
            trip1: {
                start: "Providence, RI",
                end: "New Haven, CT",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "15",
                id: "8",
                name: "Jeff's Carpool 4",
            },
            trip2: {
                start: "New Haven, CT",
                end: "Marlboro, NJ",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "20",
                id: "7",
                name: "Mark's Carpool 3",
            }
        },
        totalPrice: "2",
        totalTime: "220",
        status: "pending"
    },
    {
        trips: {
            trip1: {
                start: "Providence, RI",
                end: "New Haven, CT",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "15",
                id: "10",
                name: "Jeff's Carpool 5",
            }
        },
        totalPrice: "2",
        totalTime: "220",
        status: "pending"
    }
];

/**
 * When the DOM loads, set the tooltip content and check if cookies are enabled and
 * show the home and info buttons.s
 */
$(document).ready(function () {
    signInTooltip[0].setContent("Sign in with your Google Account to view your trips.");
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
});

/**
 * Overriden function for user sign in action.
 */
function onUserSignedIn() {
    /**
     * Show the my-trips div, set the profile picture to the user's profile
     * picture, hide the sign in tooltip, and query for the user's trips
     * from the server.
     */
    $("#my-trips-wrapper").css({
        display: "block"
    });
    $("#trips-picture").attr({
        src: `${userProfile.getImageUrl()}`,
        onerror: "this.onerror=null;this.src='/images/temp.png';"
    });
    $("#hosting").empty();
    $("#member").empty();
    $("#pending").empty();
    allIDs = [];

    signInTooltip[0].hide();
    queryUserTrips();
}

/**
 * Overriden function for user sign out action.
 */
function onUserSignedOut() {
    /**
     * Hide the my-trips section, reset profile picture,
     * and show the sign in tooltip
     */

    $("#my-trips-wrapper").css({
        display: "none"
    });
    $("#trips-picture").attr({
        src: "/images/temp.png"
    });

    signInTooltip[0].show();
}

/**
 * Query the server for the user's trips and generate
 * trip cards for each group.
 */
function queryUserTrips() {
    generateTripCards(tempData);
}

/**
 * Generate trip cards for the user's trips.
 * @param {*} data
 */
function generateTripCards(data) {
    let hosting = [];
    let member = [];
    let pending = [];

    // Iterate over each trip group
    data.forEach(tripGroup => {
        let result = `<div class="trip-group">`;
        let ids = [];
        let linkedTrips = tripGroup["trips"];

        // Get a list of the trip ids
        for (let trip in linkedTrips) {
            ids.push(linkedTrips[trip]["id"]);
        }

        if (ids.length > 1) {
            // Iterate through each trip in the group
            for (let i = 0; i < ids.length; i++) {
                // Apply different classes if first trip in group or subsequent trips
                if (i === 0) {
                    result += `<div class="result main" id="${ids[i]}" onclick="showGroup(${ids[0]},[${ids.slice(1)}]);">`;
                } else {
                    /**
                     * 130*i refers to scaling the bottom position of the relatively
                     * positioned card to create the "stacked-card" effect
                     */
                    result += `<div class="result sub" style="z-index: -${i};
                        bottom: ${130*i}px" id="${ids[0]+"-"+ids[i]}">`;
                }
                // Generate the trip content for each card
                let trip = Object.keys(linkedTrips)[i];
                result += generateTrip(linkedTrips[trip]["name"], linkedTrips[trip]["start"],
                    linkedTrips[trip]["end"], linkedTrips[trip]["date"],
                    linkedTrips[trip]["currentSize"], linkedTrips[trip]["maxSize"],
                    linkedTrips[trip]["costPerPerson"]);

                if (i === ids.length - 1) {
                    result += `<div id="hide-${ids[0]}" style="width: 100%; text-align: right; display: none;"><i onclick="hideGroup(${ids[0]},[${ids.slice(1)}])" class="fas fa-chevron-up icon-label-large"></i></div>`;
                }
                result += "</div>";
            }
        } else {
            let trip = Object.keys(linkedTrips)[0];
            result += `<div class="result" id="${ids[0]}">` + generateTrip(linkedTrips[trip]["name"], linkedTrips[trip]["start"],
                linkedTrips[trip]["end"], linkedTrips[trip]["date"],
                linkedTrips[trip]["currentSize"], linkedTrips[trip]["maxSize"],
                linkedTrips[trip]["costPerPerson"]) + "</div>";
        }
        result += `</div>`

        // Add the trip to its respective list
        if (tripGroup["status"] === "host") {
            hosting.push(result);
        } else if (tripGroup["status"] === "member") {
            member.push(result);
        } else {
            pending.push(result);
        }
    });
    // Render the cards on the screen.s
    renderTripCards(hosting, member, pending);
}

function renderTripCards(hosting, member, pending) {
    $("#hosting").append(hosting);
    $("#member").append(member);
    $("#pending").append(pending);
}

function showGroup(startID, ids) {
    ids.reverse().forEach(element => {
        $(`#${startID}-${element}`).animate({
                bottom: "0px"
            },
            "slow"
        );
        $(`#${startID}-${element}`).css({
            "z-index": "10"
        });
    });

    $("#hide-" + startID).show();
}

function hideGroup(startID, ids) {
    let bottom = 130;

    ids.forEach(element => {
        $(`#${startID}-${element}`).animate({
                bottom: `${bottom}px`
            },
            "slow"
        );
        $(`#${startID}-${element}`).css({
            "z-index": `-${bottom/130}`
        });
        bottom *= 2;
    });

    $("#hide-" + startID).hide();
}