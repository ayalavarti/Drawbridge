// Store user profile across JS files
let userProfile;

/**
 * When the DOM loads, check for the logged in cookie.
 */
$(document).ready(function () {
	if (navigator.cookieEnabled) {
		if (getCookie("loggedIn") != "true") {
			$("#sign-in").css({
				visibility: "visible"
			});
		}
	} else {
		$("#sign-in").css({
			visibility: "visible"
		});
	}
});

function getCookie(cname) {
	let name = cname + "=";
	let decodedCookie = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(';');
	for (let i = 0; i < ca.length; i++) {
		let c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

function toHome() {
	window.open("/", "_self");
}

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

	document.cookie = "loggedIn=true; path=/";

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
	$("#profile-info").css({
		visibility: "visible"
	});
	$("#sign-in").css({
		visibility: "hidden"
	});
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
		document.cookie = "loggedIn=false; path=/";

		// Hide profile info dropdown and show login button
		$("#profile-info").css({
			visibility: "hidden"
		});
		$("#sign-in").css({
			visibility: "visible"
		});
	});
}

function showHomeInfo() {
	$("#home-btn").css({
		visibility: "visible"
	});
	$("#info-btn").css({
		visibility: "visible"
	});
}