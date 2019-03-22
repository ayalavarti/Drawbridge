let map;
let geocoder;

let curLat = 42.358188;
let curLong = -71.058502;

$(document).ready(function () {
    initMap();
    console.log("DOM ready.");
});


function centerMap() {
    moveToLocation(curLat, curLong);
}

function addMarkerTest() {
    addMarker(42.358188, -71.058502, "test");
    moveToLocation(42.358188, -71.058502);
}

function addMarker(lat, long, name) {
    let mapLayer = map.getLayer(name);
    if (typeof mapLayer === 'undefined') {
        /* Image: An image is loaded and added to the map. */
        map.loadImage("https://i.imgur.com/MK4NUzI.png", function (error, image) {
            if (error) throw error;
            map.addImage("custom-marker", image);
            /* Style layer: A style layer ties together the source and image and specifies how they are displayed on the map. */
            map.addLayer({
                id: name,
                type: "symbol",
                /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
                source: {
                    type: "geojson",
                    data: {
                        type: 'FeatureCollection',
                        features: [{
                            type: 'Feature',
                            properties: {},
                            geometry: {
                                type: "Point",
                                coordinates: [long, lat]
                            }
                        }]
                    }
                },
                layout: {
                    "icon-image": "custom-marker",
                }
            });
        });
    } else {
        console.log("Marker already exists.");
    }
}

function moveToLocation(lat, lng) {
    map.flyTo({
        center: [
            lng, lat
        ]
    });
}

function handleInput() {}

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
    mapboxgl.accessToken = 'pk.eyJ1IjoiYXJ2Mzk1IiwiYSI6ImNqdGpodWcwdDB6dXEzeXBrOHJyeGVpNm8ifQ.bAwH-KG_5A5kwIxCf6xCSQ';
    map = new mapboxgl.Map({
        container: 'map',
        keyboard: false,
        style: 'mapbox://styles/mapbox/streets-v11',
        center: [long, lat], // starting position [lng, lat]
        zoom: 12
    });
    $(".loading").css({
        visibility: "hidden"
    });

    $(".search-menu-wrapper").css({
        visibility: "visible"
    });
    console.log("Map loaded.");
}