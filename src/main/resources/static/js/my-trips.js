const months = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];
const tempData = [
    [{
            start: "Providence, RI",
            end: "New Haven, CT",
            date: "1553453944862",
            currentSize: "3",
            maxSize: "5",
            costPerPerson: "15",
            id: "1",
            name: "Jeff's Carpool",
            status: "joined"
        },
        {
            start: "New Haven, CT",
            end: "New York, NY",
            date: "1553453944862",
            currentSize: "3",
            maxSize: "5",
            costPerPerson: "20",
            id: "2",
            name: "Sam's Carpool",
            status: "joined"
        },
        {
            start: "New York, NY",
            end: "Marlboro, NJ",
            date: "1553453944862",
            currentSize: "3",
            maxSize: "5",
            costPerPerson: "10",
            id: "3",
            name: "Arvind's Carpool",
            status: "pending"
        }
    ],
    [{
            start: "Providence, RI",
            end: "New Haven, CT",
            date: "1553453944862",
            currentSize: "3",
            maxSize: "5",
            costPerPerson: "15",
            id: "1",
            name: "Jeff's Carpool",
            status: "joined"
        },
        {
            start: "New Haven, CT",
            end: "Marlboro, NJ",
            date: "1553453944862",
            currentSize: "3",
            maxSize: "5",
            costPerPerson: "20",
            id: "4",
            name: "Mark's Carpool",
            status: "pending"
        }
    ],
    [{
        start: "Providence, RI",
        end: "Springfield, MA",
        date: "1553453944862",
        currentSize: "1",
        maxSize: "3",
        costPerPerson: "24",
        id: "5",
        name: "Going to Airport",
        status: "pending"
    }],
    [{
        start: "Providence, RI",
        end: "Six Flags",
        date: "1553453944862",
        currentSize: "4",
        maxSize: "5",
        costPerPerson: "24",
        id: "6",
        name: "Six Flags Trip",
        status: "hosting"
    }],
    [{
        start: "Boston, MA",
        end: "Brown University",
        date: "1553453944862",
        currentSize: "3",
        maxSize: "5",
        costPerPerson: "15",
        id: "7",
        name: "Boston to Brown",
        status: "hosting"
    }],
    [{
        start: "Yale University",
        end: "Harvard University",
        date: "1553453944862",
        currentSize: "3",
        maxSize: "5",
        costPerPerson: "14",
        id: "8",
        name: "Yale to Harvard",
        status: "hosting"
    }]
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
    console.log("DOM ready.");
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
    console.log("User signed in.");

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
    console.log("User signed out.");

    signInTooltip[0].show();
}

/**
 * Query the server for the user's trips and generate
 * trip cards for each group.
 */
function queryUserTrips() {
    const data = {
        userID: userProfile.getId()
    };
    $.post("/my-trips", data, response => {
        generateTripCards(response);
    }, "json");

    //generateTripCards(data);
}

/**
 * Generate trip cards for the user's trips.
 * @param {*} data
 */
function generateTripCards(data) {
    let hosting = [];
    let member = [];
    let pending = [];

    data.forEach(element => {
        for (let trip in element) {
            // Add the trip to its respective list
            if (element[trip]["status"] === "hosting") {
                hosting.push(generateTrip(element[trip]));
            } else if (element[trip]["status"] === "joined") {
                member.push(generateTrip(element[trip]));
            } else {
                pending.push(generateTrip(element[trip]));
            }
        }
    });
    // Render the cards on the screen.
    renderTripCards(hosting, member, pending);
}

/**
 * Render the trips in each category on the screen.
 * @param {*} hosting
 * @param {*} member
 * @param {*} pending
 */
function renderTripCards(hosting, member, pending) {
    $("#hosting").append(hosting);
    $("#member").append(member);
    $("#pending").append(pending);
}