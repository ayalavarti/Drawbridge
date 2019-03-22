<#assign content>

<div style="visibility: hidden;" class="search-menu-wrapper">
    <div class="search-menu">
        <h2 class="section-heading">Search for a Carpool</h2>
        <div class="search-inputs">
            <div><i class="fas fa-dot-circle icon-label"></i>
                <input class="address-input" id="start-input" oninput="handleInput()" type="text" placeholder="Starting point...">
            </div>
            <i class="fab fa-nintendo-switch icon-switch" onclick="switchAddresses()"></i>
            <div><i class="fas fa-map-marker-alt icon-label"></i>
                <input class="address-input" id="end-input" oninput="handleInput()" type="text" placeholder="Ending destination...">
            </div>
        </div>
    </div>
</div>

<div class="current-location" onclick="centerMap()">
    <i class="fas fa-map-pin icon-current-location"></i>
</div>


<div id="map"></div>

<script src="/js/jquery-3.1.1.js"></script>
<script src="/js/map.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDUkO8cyOm9dCtWhDkAwdZyfjwsBegrXd4&callback=initMap" async defer></script>

</#assign>
<#include "main.ftl">
