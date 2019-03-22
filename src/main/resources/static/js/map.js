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
                if (response && response.body && response.body.features && response.body.features.length) {
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
    addMarker(lat, long, id);
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

function addMarker(lat, long, name) {
    let mapLayer = map.getLayer(name);
    if (typeof mapLayer != 'undefined') {
        map.removeLayer(name).removeSource(name);
        map.removeImage(name);
    }
    /* Image: An image is loaded and added to the map. */
    let imageUrl;
    if (name === "start-input") {
        imageUrl = "/images/marker-start.png";
    } else {
        imageUrl = "/images/marker-end.png";
    }

    map.loadImage(imageUrl, function (error, image) {
        if (error) throw error;
        map.addImage(name, image);
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
                "icon-image": name,
            }
        });
    });
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