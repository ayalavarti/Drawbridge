<#assign stylesheets>
    <link rel="stylesheet" href="/css/results.css"  type="text/css">
    <link rel="stylesheet" href="/css/my-trips.css"  type="text/css">
</#assign>

<#assign content>
    <div id="container">
        <h1 id="title">
            <img id="trips-picture" src="/images/temp.png" /> My Trips
        </h1>

        <div id="my-trips-wrapper" style="visibility: hidden">
            <div class="sub-heading-left">Hosting</div>
                <img src="/images/divider.png" style="height: 4px; width: auto;" />

                <div class="results-container">
                    <div class="results-content">

                        <div class="trip-group">
                            <div class="result main" id="1" onclick="showGroup([2, 3]);">
                                <div class="result-info">
                                    <div class="sub-heading">Jeff's Carpool</div>
                                    <img src="/images/divider.png" style="height: 3px; width: auto;">
                                    <div>
                                        <i class="fas fa-map-marker-alt icon-label-small"></i>
                                            Providence, RI - New Haven, CT
                                    </div>
                                    <div>
                                        <i class="fas fa-calendar icon-label-small"></i>
                                            March 24, 2019
                                        <i class="fas fa-clock icon-label-small"></i>
                                            2:59 PM
                                    </div>
                                    <div>
                                        <i class="fas fa-users icon-label-small"></i>
                                            3 / 5
                                            <i class="fas fa-dollar-sign icon-label-small"></i>
                                            15
                                    </div>
                                </div>
                            </div>

                            <div class="result sub" id="2">
                                <div class="result-info">
                                    <div class="sub-heading">Jeff's Carpool 2</div>
                                    <img src="/images/divider.png" style="height: 3px; width: auto;">
                                    <div>
                                        <i class="fas fa-map-marker-alt icon-label-small"></i>
                                            Providence, RI - New Haven, CT
                                    </div>
                                    <div>
                                        <i class="fas fa-calendar icon-label-small"></i>
                                            March 24, 2019
                                        <i class="fas fa-clock icon-label-small"></i>
                                            2:59 PM
                                    </div>
                                    <div>
                                        <i class="fas fa-users icon-label-small"></i>
                                            3 / 5
                                            <i class="fas fa-dollar-sign icon-label-small"></i>
                                            15
                                    </div>
                                </div>
                            </div>

                            <div class="result sub" id="3">
                                <div class="result-info">
                                    <div class="sub-heading">Jeff's Carpool 3</div>
                                    <img src="/images/divider.png" style="height: 3px; width: auto;">
                                    <div>
                                        <i class="fas fa-map-marker-alt icon-label-small"></i>
                                            Providence, RI - New Haven, CT
                                    </div>
                                    <div>
                                        <i class="fas fa-calendar icon-label-small"></i>
                                            March 24, 2019
                                        <i class="fas fa-clock icon-label-small"></i>
                                            2:59 PM
                                    </div>
                                    <div>
                                        <i class="fas fa-users icon-label-small"></i>
                                            3 / 5
                                            <i class="fas fa-dollar-sign icon-label-small"></i>
                                            15
                                    </div>
                                    <div style="text-align: right;"><i class="fas fa-angle-double-up icon-label-large" onclick="hideGroup([2,3]);"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="sub-heading-left">Confirmed Membership</div>
                <img src="/images/divider.png" style="height: 4px; width: auto;" />
            </div>
        </div>
    </div>

    <script src="/js/trips.js"></script>
</#assign>
<#include "main.ftl">