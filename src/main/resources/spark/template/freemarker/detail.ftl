<#assign stylesheets>
    <link rel="stylesheet" href="/css/detail.css" type="text/css">
</#assign>

<#assign content>
    <div id="map-inset">
        <h1>${trip.getName()}</h1>
        <div id="map"></div>
    </div>
    <div id="basic-info"></div>
    <div id="member-list"></div>
    <div id="comments"></div>
</#assign>
<#include "main.ftl">