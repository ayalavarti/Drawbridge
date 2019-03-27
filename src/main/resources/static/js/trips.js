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
    })
    $("#trips-picture").attr({
        src: `${userProfile.getImageUrl()}`,
        onerror: "this.onerror=null;this.src='/images/temp.png';"
    });

    signInTooltip[0].hide();
}

function onUserSignedOut() {
    $("#my-trips-wrapper").css({
        visibility: "hidden"
    })
    $("#trips-picture").attr({
        src: "/images/temp.png",
    });
    signInTooltip[0].show();
}