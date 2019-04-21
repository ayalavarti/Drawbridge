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
            <div>
                <h3 style="margin-top: 30px;">Share this Trip</h3>
                <div style="display: flex; flex-wrap: wrap; justify-content:
                center; align-items: center;">
                    <a href="https://twitter.com/intent/tweet?url=http%3A%2F%2Flocalhost:8000/trip/${trip.getId()}&text=Join%20My%20Carpool!">
                        <div class="share-tooltip">
                            <img style="height: 15px; width: auto"
                                 src="/images/twitter-icon.png"/>
                        </div>
                    </a>
                    <div class="share-tooltip" id="clipboardTooltip"
                         data-tippy-content="Trip URL copied to clipboard!"
                         onclick="copyToClipboard()">
                        <img style="height: 20px; width: auto"
                             src="/images/copy-icon.png"/>
                    </div>
                    <a href="mailto:?Subject=Join%20My%20Carpool!"
                       target="_top">
                        <div class="share-tooltip">
                            <img style="height: 15px; width: auto"
                                 src="/images/mail-icon.png"/>
                        </div>
                    </a>

                    <script async src="https://platform.twitter.com/widgets.js"
                            charset="utf-8"></script>
                </div>
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
                <div id="pending" style="display: none;">
                    <#list pending as pend>
                        <div class="list-person">
                        <span class="user-name">${pend.getName()}<i
                                    class="addendum">(pending)</i></span>
                            <div class="pending">
                                <img alt="approve-btn" id="approve-btn"
                                     src="/images/approve-btn.png"
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
                </div>
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
                    <img alt="pending-label" src="/images/pending-btn.png"
                         id="pending-label"
                         style="display: none;"
                         class="pending-btn"
                    />
                    <img alt="full-label" src="/images/full-label.png"
                         id="full-label"
                         style="display: none;"
                         class="full-label"
                    />
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
    let tid = "${trip.getId()?js_string}";

    let host = "${host.getId()?js_string}";
    let members = [
        <#list members as mem>"${mem.getId()?js_string}"<#if mem_has_next>,
        </#if></#list>
    ];

    let tripFull =
    ${trip.getMaxUsers()} ===
    ${trip.getCurrentSize()};

    if (tripFull) {
        $("#full-label").show();
    }

    let pending = [
        <#list pending as pen>"${pen.getId()?js_string}"<#if pen_has_next>,
        </#if></#list>
    ];
</script>
