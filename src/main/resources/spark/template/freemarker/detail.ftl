<#assign stylesheets>
    <link rel="stylesheet" href="/css/detail.css" type="text/css">
</#assign>

<#assign content>
    <div id="map-inset">
        <h1>${trip.getName()}</h1>
        <div id="map"></div>
    </div>
    <div id="basic-info">
        <div class="info-box">
            <i class="far fa-dot-circle"></i>
        </div>
        <div class="info-box">
            <i class="fas fa-map-marker-alt"></i>
        </div>
        <div class="info-box">
            <i class="far fa-calendar-alt"></i>
        </div>
        <div class="info-box">
            <i class="far fa-clock"></i>
        </div>
        <div class="info-box">
            <i class="fas fa-phone"></i>
        </div>
    </div>
    <div id="member-list"></div>
    <div id="comments"></div>
</#assign>
<#include "main.ftl">