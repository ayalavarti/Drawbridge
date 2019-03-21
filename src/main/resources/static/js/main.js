let userProfile;

function onSignIn(googleUser) {
    userProfile = googleUser;
    let profile = googleUser.getBasicProfile();

    $("#profileInfo").append($('<img>', {
        id: 'profile-picture',
        src: `${profile.getImageUrl()}`,
        onerror: "this.onerror=null;this.src='images/temp.png';"
    }));
    setTimeout(function() {
        $("#profileInfo").css({ "visibility": "visible" });
        $(".g-signin2").hide();
    }, 500);
}