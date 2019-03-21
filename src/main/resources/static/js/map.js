let map;

let curLat = 42.358188;
let curLong = -71.058502;

function centerMap() {
    moveToLocation(curLat, curLong);
}

function moveToLocation(lat, lng) {
    let center = new google.maps.LatLng(lat, lng);
    map.panTo(center);
}

function handleInput() {
    console.log("handled!");
}

function switchAddresses() {
    let start = $("#start-input").val();
    $("#start-input").val($("#end-input").val());
    $("#end-input").val(start);
}

function initMap() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            curLat = position.coords.latitude;
            curLong = position.coords.longitude;
            renderMap(curLat, curLong);
        });
    } else {
        console.log("Geolocation is not supported by this browser.");
        renderMap(curLat, curLong);
    }
}

function renderMap(lat, long) {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {
            lat: lat,
            lng: long,
        },
        zoom: 15,
        disableDefaultUI: true
    });
    $(".search-menu-wrapper").css({
        visibility: "visible"
    });
}