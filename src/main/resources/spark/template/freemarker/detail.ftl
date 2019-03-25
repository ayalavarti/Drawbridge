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
                <span><i class="far fa-dot-circle"></i>${startName}</span>
            </div>
            <div class="info-box">
                <span><i class="fas fa-map-marker-alt"></i>${endName}</span>
            </div>
            <div class="info-box half-size">
                <span><i class="far fa-calendar-alt"></i>${(trip.getDepartureTime()*1000)?number_to_date}</span>
            </div>
            <div class="info-box half-size">
                <span><i class="far fa-clock"></i>${(trip.getDepartureTime()*1000)?number_to_time?string("h:mm a")}</span>
            </div>
            <div class="info-box">
                <span><i class="fas fa-phone"></i>Contact host at ${trip.getPhoneNumber()}</span>
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
                    <button id="join-btn" style="display: none">Join<i class="fas fa-sign-in-alt"></i></button>
                    <button id="leave-btn" style="display: none">Leave<i class="fas fa-sign-out-alt"></i></button>
                    <button id="delete-btn" style="display: none">Delete<i class="fas fa-trash-alt"></i></button>
                </div>
            </div>
        </div>
        <div id="comments-container">
            <h2>Description</h2>
            <div id="comments">
                <i class="fas fa-dollar-sign"></i>${trip.getCost()?string("##0.00")}<i class="addendum">/person</i>
                <p>${trip.getComments()}</p>
            </div>
        </div>
    </div>
</#assign>
<#include "main.ftl">