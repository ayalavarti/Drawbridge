<#assign stylesheets>
    <link rel="stylesheet" href="/css/detail.css" type="text/css">
    <script src="/js/map.js"></script>
    <script src="/js/detail.js"></script>
</#assign>

<#assign content>
    <div id="container">
        <h1 id="title">${trip.getName()}</h1>
        <div id="map-inset">
            <div id="map-container">
                <img alt="Trip map" src="https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/-71,42,7/500x300?access_token=pk.eyJ1IjoiYXJ2Mzk1IiwiYSI6ImNqdGpodWcwdDB6dXEzeXBrOHJyeGVpNm8ifQ.bAwH-KG_5A5kwIxCf6xCSQ">
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
        <div id="member-list-container">
            <h2>Carpool Members</h2>
            <div id="member-list">
                <div class="list-person">
                    <span class="user-name">Mary B.</span>
                    <div class="status host">
                        <span><i class="fas fa-car"></i>Host</span>
                    </div>
                </div>
                <div class="list-person">
                    <span class="user-name">John T.</span>
                    <div class="status member">
                        <span><i class="fas fa-user"></i>Member</span>
                    </div>
                </div>
                <div class="list-person">
                    <span class="user-name">Taylor S.</span>
                    <div class="status member">
                        <span><i class="fas fa-user"></i>Member</span>
                    </div>
                </div>
                <div class="list-person">
                    <span class="user-name">Sylvester S. <i class="addendum">(pending)</i></span>
                    <div class="pending">
                        <button class="approve"><i class="fas fa-check"></i></button><button class="deny"><i class="fas fa-times"></i></button>
                    </div>
                </div>
                <div id="button-container">
                    <button>Join<i class="fas fa-sign-in-alt"></i></button>
                </div>
            </div>
        </div>
        <div id="comments-container">
            <h2>Description</h2>
            <div id="comments">
                <i class="fas fa-dollar-sign"></i>24<i class="addendum">/person</i>
                <p>We'll be meeting inside the Ratty at 4pm, though a little later is okay too.</p>
            </div>
        </div>
    </div>
</#assign>
<#include "main.ftl">