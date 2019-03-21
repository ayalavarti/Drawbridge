<#assign content>

<div style="visibility: hidden;" class="search-menu-wrapper">
    <div class="search-menu">
        <i class="far fa-compass icon" onclick="centerMap()"></i>
        <div id="pac-container">
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

<div id="map"></div>

<script src="/js/jquery-3.1.1.js"></script>
<script src="/js/map.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDUkO8cyOm9dCtWhDkAwdZyfjwsBegrXd4&callback=initMap" async defer></script>

</#assign>
<#include "main.ftl">
