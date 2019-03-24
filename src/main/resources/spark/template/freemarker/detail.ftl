<#assign stylesheets>
    <link rel="stylesheet" href="/css/detail.css" type="text/css">
    <script src="/js/map.js"></script>
    <script src="/js/detail.js"></script>
</#assign>

<#assign content>
    <div id="map-inset">
        <h1 id="title">${trip.getName()}</h1>
        <div id="map-container">
            <img alt="Trip map" src="https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/-71,42,7/400x300?access_token=pk.eyJ1IjoiYXJ2Mzk1IiwiYSI6ImNqdGpodWcwdDB6dXEzeXBrOHJyeGVpNm8ifQ.bAwH-KG_5A5kwIxCf6xCSQ">
        </div>
    </div>
    <div id="basic-info">
        <div class="info-box">
            <span><i class="far fa-dot-circle"></i>40 Hillside Rd., Cromwell, CT</span>
        </div>
        <div class="info-box">
            <span><i class="fas fa-map-marker-alt"></i>Keeney Quadrangle, Brown University, Providence, RI</span>
        </div>
        <div class="info-box half-size">
            <span><i class="far fa-calendar-alt"></i>April 5, 2019</span>
        </div>
        <div class="info-box half-size">
            <span><i class="far fa-clock"></i>11:59 am</span>
        </div>
        <div class="info-box">
            <span><i class="fas fa-phone"></i>Contact host at (555) 555-5555</span>
        </div>
    </div>
    <div id="member-list"></div>
    <div id="comments"></div>

    <script>

    </script>
</#assign>
<#include "main.ftl">