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
                id: "1",
                name: "Jeff's Carpool 2",
            },
            trip2: {
                start: "New Haven, CT",
                end: "Marlboro, NJ",
                date: "1553453944862",
                currentSize: "3",
                maxSize: "5",
                costPerPerson: "20",
                id: "4",
                name: "Mark's Carpool 2",
            }
        },
        totalPrice: "2",
        totalTime: "220",
        status: "host"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Providence, RI",
        //             end: "New Haven, CT",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "15",
        //             id: "1",
        //             name: "Jeff's Carpool",
        //         },
        //         trip2: {
        //             start: "New Haven, CT",
        //             end: "New York, NY",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "20",
        //             id: "2",
        //             name: "Sam's Carpool",
        //         },
        //         trip3: {
        //             start: "New York, NY",
        //             end: "Marlboro, NJ",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "10",
        //             id: "3",
        //             name: "Arvind's Carpool",
        //         }
        //     },
        //     totalPrice: "24",
        //     totalTime: "260",
        //     status: "member"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Providence, RI",
        //             end: "New Haven, CT",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "15",
        //             id: "1",
        //             name: "Jeff's Carpool",
        //         },
        //         trip2: {
        //             start: "New Haven, CT",
        //             end: "Marlboro, NJ",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "20",
        //             id: "4",
        //             name: "Mark's Carpool",
        //         }
        //     },
        //     totalPrice: "2",
        //     totalTime: "220",
        //     status: "member"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Providence, RI",
        //             end: "Springfield, MA",
        //             date: "1553453944862",
        //             currentSize: "1",
        //             maxSize: "3",
        //             costPerPerson: "24",
        //             id: "5",
        //             name: "Going to Airport",
        //         }
        //     },
        //     totalPrice: "24",
        //     totalTime: "60",
        //     status: "member"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Providence, RI",
        //             end: "Six Flags",
        //             date: "1553453944862",
        //             currentSize: "4",
        //             maxSize: "5",
        //             costPerPerson: "24",
        //             id: "6",
        //             name: "Six Flags Trip",
        //         }
        //     },
        //     totalPrice: "24",
        //     totalTime: "180",
        //     status: "pending"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Boston, MA",
        //             end: "Brown University",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "15",
        //             id: "7",
        //             name: "Boston to Brown",
        //         }
        //     },
        //     totalPrice: "15",
        //     totalTime: "140",
        //     status: "pending"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Yale University",
        //             end: "Harvard University",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "14",
        //             id: "8",
        //             name: "Yale to Harvard",
        //         }
        //     },
        //     totalPrice: "14",
        //     totalTime: "200",
        //     status: "pending"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Providence, RI",
        //             end: "New Haven, CT",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "15",
        //             id: "1",
        //             name: "Jeff's Carpool",
        //         },
        //         trip2: {
        //             start: "New Haven, CT",
        //             end: "New York, NY",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "20",
        //             id: "2",
        //             name: "Sam's Carpool",
        //         },
        //         trip3: {
        //             start: "New York, NY",
        //             end: "Marlboro, NJ",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "10",
        //             id: "3",
        //             name: "Arvind's Carpool",
        //         }
        //     },
        //     totalPrice: "24",
        //     totalTime: "260",
        //     status: "pending"
        // },
        // {
        //     trips: {
        //         trip1: {
        //             start: "Providence, RI",
        //             end: "New Haven, CT",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "15",
        //             id: "1",
        //             name: "Jeff's Carpool",
        //         },
        //         trip2: {
        //             start: "New Haven, CT",
        //             end: "Marlboro, NJ",
        //             date: "1553453944862",
        //             currentSize: "3",
        //             maxSize: "5",
        //             costPerPerson: "20",
        //             id: "4",
        //             name: "Mark's Carpool",
        //         }
        //     },
        //     totalPrice: "2",
        //     totalTime: "220",
        //     status: "pending"
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

        // Iterate through each trip in the group
        for (let i = 0; i < ids.length; i++) {
            // Apply different classes if first trip in group or subsequent trips
            if (i === 0) {
                result = result + `<div class="result main" id="${ids[i]}">`;
            } else {
                /**
                 * 130*i refers to scaling the bottom position of the relatively
                 * positioned card to create the "stacked-card" effect
                 */
                result = result + `<div class="result sub" style="z-index: -${i}; bottom: ${130*i}px" id="${ids[i]}">`;
            }
            // Generate the trip content for each card
            let trip = Object.keys(linkedTrips)[i];
            result += generateTrip(linkedTrips[trip]["name"], linkedTrips[trip]["start"],
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

function showGroup(ids) {
    let start = 0;
    console.log(ids);
    [ids].reverse().forEach(element => {
        $(`#${element}`).animate({
                bottom: `${start}px`
            },
            "slow"
        );
        start = start + 150;
    });
}

function hideGroup(ids) {
    let bottom = 150 * ids.length;

    ids.forEach(element => {
        $(`#${element}`).animate({
                bottom: `${bottom}px`
            },
            "slow"
        );
    });
}