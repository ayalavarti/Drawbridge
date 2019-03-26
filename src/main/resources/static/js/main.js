// Store user profile across JS files
let userProfile = undefined;

let signInTooltip;

/**
 * When the DOM loads, check for the logged in cookie.
 */

$(document).ready(function () {
	initSignInTooltip();
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

function initSignInTooltip() {
	signInTooltip = tippy("#profile-picture-wrapper", {
		animation: "scale",
		arrow: true,
		arrowType: "round",
		theme: "drawbridge",
		interactive: false,
		trigger: "manual",
		hideOnClick: false,
		offset: "0, 40",
		maxWidth: 170,
		inertia: true,
		sticky: true,
		placement: "bottom",
	});
}

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

function toInfo() {
	window.open("/help", "_self");
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
	userProfile = googleUser.getBasicProfile();
	document.cookie = "loggedIn=true; path=/";

	onUserSignedIn();

	// Add profile picture
	$("#profile-picture-wrapper").prepend(
		$("<img>", {
			id: "profile-picture",
			src: `${userProfile.getImageUrl()}`,
			onerror: "this.onerror=null;this.src='/images/temp.png';"
		})
	);

	// Set user name
	$("#user-name").text(userProfile.getGivenName());

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
		// Reset userProfile variable
		userProfile = undefined;
		document.cookie = "loggedIn=false; path=/";

		onUserSignedOut();

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

function hover(e) {
	e.setAttribute('src', `../images/${e.className}-hover.png`);
}

function unhover(e) {
	e.setAttribute('src', `../images/${e.className}.png`);
}