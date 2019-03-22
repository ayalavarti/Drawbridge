// Store user profile across JS files
let userProfile;

/**
 * Handles sign in errors.
 *
 * @param {*} error
 */
function onFailure(error) {
	console.log(error);
}

/**
 * Handles successful sign in requests.
 *
 * @param {*} googleUser
 */
function onSignIn(googleUser) {
	// Store userprofile in global variable
	userProfile = googleUser;
	let profile = googleUser.getBasicProfile();

	// Add profile picture
	$("#profile-picture-wrapper").prepend(
		$("<img>", {
			id: "profile-picture",
			src: `${profile.getImageUrl()}`,
			onerror: "this.onerror=null;this.src='images/temp.png';"
		})
	);

	// Set user name
	$("#user-name").text(profile.getGivenName());

	// Hide the sign in button and show the profile info button
	setTimeout(function () {
		$("#profile-info").css({
			visibility: "visible"
		});
		$("#sign-in").hide();
	}, 500);
}

/**
 * Sign out the user.
 */
function signOut() {
	let auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut().then(function () {
		console.log("User signed out.");
		// Reset userProfile variable
		userProfile = undefined;

		// Hide profile info dropdown and show login button
		$("#profile-info").css({
			visibility: "hidden"
		});
		$("#sign-in").show();
	});
}