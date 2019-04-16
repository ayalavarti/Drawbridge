<#assign stylesheets>
    <link rel="stylesheet" href="/css/detail.css" type="text/css">
    <script src="/js/mapUtil.js"></script>
    <script src="/js/detail.js"></script>
</#assign>

<#assign content>
    <div id="container">
        <h1 id="title">${trip.getName()}</h1>
        <div id="map-inset">
            <div id="map"></div>
        </div>
        <div id="basic-info">
            <div class="info-box">
                <span><i class="far fa-dot-circle"></i>${trip.getStartingAddress()}</span>
            </div>
            <div class="info-box">
                <span><i class="fas fa-map-marker-alt"></i>${trip.getEndingAddress()}</span>
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
                    <span class="user-name">${host.getName()}</span>
                    <img alt="host-label" src="/images/hosting-btn.png"
                         class="host-label"/>
                </div>
                <#list members as member>
                    <div class="list-person">
                        <span class="user-name">${member.getName()}</span>
                        <img alt="member-label" src="/images/member-label.png"
                             class="member-label"/>
                    </div>
                </#list>
                <#list pending as pend>
                    <div class="list-person">
                        <span class="user-name">${pend.getName()}<i
                                    class="addendum">(pending)</i></span>
                        <div class="pending">
                            <img alt="approve-btn" src="/images/approve-btn.png"
                                 class="approve-btn"
                                 onclick="approveClick(${trip.getId()}, ${pend.getId()});"
                                 onmouseover="hover(this);"
                                 onmouseout="unhover(this);"/>
                            <img alt="deny-btn" src="/images/deny-btn.png"
                                 class="deny-btn"
                                 onclick="denyClick(${trip.getId()}, ${pend.getId()});"
                                 onmouseover="hover(this);"
                                 onmouseout="unhover(this);"/>
                        </div>
                    </div>
                </#list>
                <div id="button-container">
                    <img alt="join-btn" src="/images/join-btn.png" id="join-btn"
                         style="display: none" class="join-btn"
                         onclick="joinClick(${trip.getId()});"
                         onmouseover="hover(this);"
                         onmouseout="unhover(this);"/>
                    <img alt="leave-btn" src="/images/leave-btn.png"
                         id="leave-btn"
                         style="display: none" class="leave-btn"
                         onclick="leaveClick(${trip.getId()});"
                         onmouseover="hover(this);"
                         onmouseout="unhover(this);"/>
                    <img alt="delete-btn" src="/images/delete-btn.png"
                         id="delete-btn"
                         style="display: none" class="delete-btn"
                         onclick="deleteClick(${trip.getId()});"
                         onmouseover="hover(this);"
                         onmouseout="unhover(this);"/>
                </div>
            </div>
        </div>
        <div id="comments-container">
            <h2>Description</h2>
            <div id="comments">
                <i class="fas fa-dollar-sign"></i>${trip.getCost()?string("##0.00")}
                <i class="addendum">/person</i>
                <p>${trip.getComments()}</p>
            </div>
        </div>
    </div>
</#assign>
<#include "main.ftl">

<script type="text/javascript">
    mapboxgl.accessToken = "${mapboxKey?js_string}";
    let coordinates = [
        [
            ${trip.getStartingLongitude()},
            ${trip.getStartingLatitude()}
        ],
        [
            ${trip.getEndingLongitude()},
            ${trip.getEndingLatitude()}
        ]
    ];
    let startName = "${trip.getStartingAddress()?js_string}";
    let endName = "${trip.getEndingAddress()?js_string}";

    let uid = userProfile == null ? null : userProfile.getId();
    let host = "${host.getId()?js_string}";
    let members = [<#list members as mem>"${mem.getId()?js_string}"</#list>];
    let pending = [<#list pending as pen>"${pen.getId()?js_string}"</#list>];

    if (uid === host) {
        $("#delete-btn").show();
        $("#join-btn").hide();
        $("#leave-btn").hide();
    } else if (uid in members || uid in pending) {
        $("#delete-btn").hide();
        $("#join-btn").show();
        $("#leave-btn").hide();
    } else {
        $("#delete-btn").hide();
        $("#join-btn").show();
        $("#leave-btn").hide();
    }
</script>
