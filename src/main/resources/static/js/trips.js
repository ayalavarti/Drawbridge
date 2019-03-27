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

$(document).ready(function () {
    signInTooltip[0].setContent("Sign in with your Google Account to view your trips.");
    if (navigator.cookieEnabled) {
        if (getCookie("loggedIn") != "true") {
            signInTooltip[0].show();
        }
    } else {
        signInTooltip[0].show();
    }
    showHomeInfo();
});

function onUserSignedIn() {
    $("#my-trips-wrapper").css({
        visibility: "visible"
    });
    $("#trips-picture").attr({
        src: `${userProfile.getImageUrl()}`,
        onerror: "this.onerror=null;this.src='/images/temp.png';"
    });
    signInTooltip[0].hide();
    queryUserTrips();
}

function onUserSignedOut() {
    $("#my-trips-wrapper").css({
        visibility: "hidden"
    });
    $("#trips-picture").attr({
        src: "/images/temp.png"
    });
    signInTooltip[0].show();
}

function queryUserTrips() {
    generateTripCards(tempData);
}

function generateTripCards(data) {
    let hosting = [];
    let member = [];
    let pending = [];

    data.forEach(tripGroup => {
        let result = `<div class="trip-group">`;
        let ids = [];
        let linkedTrips = tripGroup["trips"];
        for (let trip in linkedTrips) {
            ids.push(linkedTrips[trip]["id"]);
        }
        for (let i = 0; i < ids.length; i++) {
            if (i === 0) {
                result = result + `<div class="result main" id="${ids[i]}">`;
            } else {
                result = result + `<div class="result sub" style="z-index: -${i}; bottom: ${360 - ((i-1)*10)}px" id="${ids[i]}">`;
            }
            let trip = Object.keys(linkedTrips)[i];
            result += generateTrip(linkedTrips[trip]["name"], linkedTrips[trip]["start"],
                linkedTrips[trip]["end"], linkedTrips[trip]["date"],
                linkedTrips[trip]["currentSize"], linkedTrips[trip]["maxSize"],
                linkedTrips[trip]["costPerPerson"]) + "</div>";
        }
        result += `</div>`
        if (tripGroup["status"] = "host") {
            hosting.push(result);
        } else if (tripGroup["status"] = "member") {
            member.push(result);
        } else {
            pending.push(result);
        }
    });
    renderTripCards(hosting, member, pending);
}

function renderTripCards(hosting, member, pending) {
    if (hosting.length > 0) {
        $("#hosting").append(hosting);
    }
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