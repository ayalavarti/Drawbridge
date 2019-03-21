let userProfile;

function onFailure(error) {
	console.log(error);
}

function onSignIn(googleUser) {
	userProfile = googleUser;
	let profile = googleUser.getBasicProfile();

	$("#profile-picture-wrapper").prepend(
		$("<img>", {
			id: "profile-picture",
			src: `${profile.getImageUrl()}`,
			onerror: "this.onerror=null;this.src='images/temp.png';"
		})
	);
	$("#user-name").text(profile.getGivenName());
	setTimeout(function () {
		$("#profile-info").css({
			visibility: "visible"
		});
		$("#sign-in").hide();
	}, 500);
}

function signOut() {
	let auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut().then(function () {
		console.log("User signed out.");
		userProfile = undefined;
		$("#profile-info").css({
			visibility: "hidden"
		});
		$("#sign-in").show();
	});
}