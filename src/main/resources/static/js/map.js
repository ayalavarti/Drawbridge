let map;
let mapboxClient;

let curLat = 42.358188;
let curLong = -71.058502;

let found = [];
let addressNames = [];
let coordinates = [];
let markers = [];

$(document).ready(function () {
    initMapbox();
    initMap();
    console.log("DOM ready.");
});

function initMapbox() {
    mapboxgl.accessToken = 'pk.eyJ1IjoiYXJ2Mzk1IiwiYSI6ImNqdGpodWcwdDB6dXEzeXBrOHJyeGVpNm8ifQ.bAwH-KG_5A5kwIxCf6xCSQ';
    mapboxClient = mapboxSdk({
        accessToken: mapboxgl.accessToken
    });
}

function initMap() {
    try {
        map = new mapboxgl.Map({
            container: 'map',
            keyboard: false,
            maxZoom: 18,
            style: 'mapbox://styles/mapbox/streets-v11',
            center: [curLong, curLat],
            zoom: 12
        });
        map.addControl(new mapboxgl.NavigationControl());

        $("#loading").css({
            visibility: "hidden"
        });
        $("[id=pre-load]").css({
            visibility: "visible"
        });
        console.log("Map loaded.");

    } catch (err) {
        console.log("Map load error.");
    }
}

function handleInput(id, index) {
    let address = $(`#${id}`).val()
    if (address != "" && addressNames[index] != address) {
        mapboxClient.geocoding.forwardGeocode({
                query: address,
                proximity: [curLong, curLat],
                autocomplete: true,
                limit: 1
            })
            .send()
            .then(function (response) {
                if (response && response.body && response.body.features &&
                    response.body.features.length) {

                    let feature = response.body.features[0];
                    $(`#${id}`).val(feature.place_name);
                    addressNames[index] = feature.place_name;
                    coordinates[index] = feature.center;
                    addStreetPoint(feature.center[1], feature.center[0], id, index);
                }
            });
    }
}

function addStreetPoint(lat, long, id, index) {
    addMarker2(lat, long, id, index);
    found[index] = true;

    if (found[0] && found[1]) {
        map.fitBounds(coordinates, {
            padding: {
                top: 150,
                bottom: 100,
                left: 500,
                right: 150
            },
            linear: false
        });

    } else {
        moveToLocation(lat, long);
    }
}

function addMarker2(lat, long, id, index) {
    let el = document.createElement('div');
    el.className = `marker ${id}`;
    if (id == "start-input") {
        el.className = el.className + " pulse";
    }
    if (markers[index]) {
        console.log("Existing marker exists.");
        markers[index].remove();
    }
    markers[index] = new mapboxgl.Marker(el)
        .setLngLat([long, lat]);
    markers[index].addTo(map);
}

function centerMap() {
    map.flyTo({
        center: [
            curLong, curLat
        ],
        pitch: 0,
        bearing: 0,
        zoom: 12
    });
}

function alignMap() {
    map.flyTo({
        pitch: 0,
        bearing: 0,
        zoom: 12
    });
}


function moveToLocation(lat, lng) {
    map.flyTo({
        center: [
            lng, lat,
        ],
        zoom: 12
    });
}