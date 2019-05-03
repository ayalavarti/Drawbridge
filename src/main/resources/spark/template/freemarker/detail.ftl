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
                <i class="far fa-clock"></i><span
                        id="timeDetail"></span>
            </div>
            <div id='privacy-hidden' style="display:none;" class="info-box">
                <span><i class="fas fa-phone"></i>Contact host at ${trip.getPhoneNumber()}</span>
            </div>
            <div id='privacy-hidden' class="share-section" style="display:
            none">
                <h3 style="margin-top: 30px;">Share this Trip</h3>
                <div style="display: flex; flex-wrap: wrap; justify-content:
                center; align-items: center;">
                    <a href="https://twitter.com/intent/tweet?url=https%3A%2F%2Fdrawbridges.herokuapp.com/trip/${trip.getId()}&text=Join%20My%20Carpool!&hashtags=drawbridge">
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
            <h2>Carpool Members <i style="font-size: 15px">${trip
                    .getCurrentSize()}/${trip.getMaxUsers()}
                </i></h2>
            <div id="member-list">
                <div class="list-person">
                    <span class="user-name">
                        <img class="${host.getId()}" id="member-img"
                             src="${host.getProfilePic()}"/>
                        ${host.getName()}
                        <img id="${host.getId()}" class="verified"
                             style="visibility: hidden;"
                             src="/images/current-user.png"/>
                    </span>
                    <img alt="host-label" src="/images/hosting-btn.png"
                         class="host-label"/>
                </div>
                <#list members as member>
                    <div class="list-person">
                        <span class="user-name">
                            <img class="${member.getId()}" id="member-img"
                                 src="${member.getProfilePic()}"/>
                            ${member.getName()}
                            <img id="${member.getId()}" class="verified"
                                 style="visibility: hidden;"
                                 src="/images/current-user.png"/>
                        </span>
                        <img alt="member-label" src="/images/member-label.png"
                             class="member-label"/>
                    </div>
                </#list>
                <div id="pending" style="display: none;">
                    <#list pending as pend>
                        <div class="list-person">
                        <span id="${pend.getId()}" class="user-name">
                            <img class="${pend.getId()}" id="member-img"
                                 src="${pend.getProfilePic()}"/>
                            ${pend.getName()}<i
                                    style="padding-left: 5px;"
                                    class="addendum">(pending)</i>
                            <img id=`"${pend.getId()}"` class="verified"
                                 style="visibility: hidden;"
                                 src="/images/current-user.png"/>
                        </span>
                            <div class="pending">
                                <img alt="approve-btn" id="approve-btn"
                                     src="/images/approve-btn.png"
                                     class="approve-btn"
                                     onclick="approveClick(${trip.getId()?c}, '${pend.getId()?js_string}');"
                                     onmouseover="hover(this);"
                                     onmouseout="unhover(this);"/>
                                <img alt="deny-btn" src="/images/deny-btn.png"
                                     class="deny-btn"
                                     onclick="denyClick(${trip.getId()?c}, '${pend.getId()?js_string}');"
                                     onmouseover="hover(this);"
                                     onmouseout="unhover(this);"/>
                            </div>
                        </div>
                    </#list>
                </div>
                <div id="button-container">
                    <img alt="join-btn" src="/images/join-btn.png" id="join-btn"
                         style="display: none" class="join-btn"
                         onclick="joinClick(${trip.getId()?c});"
                         onmouseover="hover(this);"
                         onmouseout="unhover(this);"/>
                    <img alt="leave-btn" src="/images/leave-btn.png"
                         id="leave-btn"
                         style="display: none" class="leave-btn"
                         onclick="leaveClick(${trip.getId()?c});"
                         onmouseover="hover(this);"
                         onmouseout="unhover(this);"/>
                    <img alt="delete-btn" src="/images/delete-btn.png"
                         id="delete-btn"
                         style="display: none" class="delete-btn"
                         onclick="deleteClick(${trip.getId()?c});"
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
                <p id="comment-input" style="display: none;">
                    <textarea
                            class="comments-textarea">${trip.getComments()}</textarea>
                </p>
                <p id="true-comment">
                    ${trip.getComments()}
                </p>
                <div style="display: flex;">
                    <div class="share-tooltip" id="cancel-btn" style="display:
                    none;"
                         onclick="cancelEditing();">
                        <img style="height: 17px; width: auto; margin: 0;"
                             id="cancel-img"
                             src="/images/times-icon.png"/>
                    </div>
                    <div class="share-tooltip" id="edit-btn"
                         style="display: none;"
                         onclick="editComment(${trip.getId()?c})">
                        <img style="height: 20px; width: auto" id="edit-img"
                             src="/images/pencil-icon.png"/>
                    </div>
                </div>
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
    let time = ${(trip.getDepartureTime()*1000)?c};
    let date = new Date(time);
    console.log(toJSTime(date));
    $("#timeDetail").text(toJSTime(date));

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
