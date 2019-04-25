<#assign stylesheets>
    <link rel="stylesheet" href="/css/results.css" type="text/css">
</#assign>

<#assign content>
    <div class="trip-info-wrapper">
        <div class="trip-info">
            <div>
                <i class="fas fa-dot-circle icon-label-medium"></i>
                <span id="start" class="trip-info-text"></span>
            </div>
            <img src="/images/divider.png"
                 style="height: 2px; margin: 0px 10px; width: auto;"/>
            <div>
                <i class="fas fa-map-marker-alt icon-label-medium"></i>
                <span id="end" class="trip-info-text"></span>
            </div>
        </div>
        <span class="sub-heading"><span id="date"></span> at <span
                    id="time"></span></span>
    </div>

    <div class="results-container">
        <div class="results-content">
            <div class="thresholds">
                <div>
                    <span class="sub-heading">Walking Time</span>
                    <div>
                        <i class="fas fa-walking icon-label-large"></i>
                        <input class="threshold-input" id="walking-input"
                               value="15" min="0" max="3600" type="number"
                               placeholder="..."/>min
                        <span id="info" class="info-tooltip"
                              data-tippy-content="Walking time is how long you
                              are willing to walk to the departure location,
                              in any one of the carpool trips.">
                            <img style="height: 15px; width: auto"
                                 src="/images/info-icon.png"/>
                        </span>
                    </div>
                </div>
                <div>
                    <span class="sub-heading">Wait Time</span>
                    <div>
                        <i class="far fa-clock icon-label-large"></i>
                        <input class="threshold-input" id="waiting-input"
                               value="30" min="0" max="3600" type="number"
                               placeholder="..."/>min
                        <span id="info" class="info-tooltip"
                              data-tippy-content="Wait time is within how
                              many minutes of your departure time you are
                              willing to leave in any of of the carpool trips.">
                            <img style="height: 15px; width: auto"
                                 src="/images/info-icon.png"/>
                        </span>
                    </div>
                </div>
                <div>
                    <img alt="search-btn" src="/images/search-btn.png"
                         id="search-btn" class="search-btn"
                         onclick="queryTrips();"
                         onmouseover="hover(this);"
                         onmouseout="unhover(this);"/>
                </div>
            </div>
            <span class="trip-info-text">Carpool Search Results</span>
            <div id="walking-distance" style="display: none;">
                We found results, but given your travel time is
                <span id="walk-eta-time"></span> minutes, we strongly feel
                you should just walk.
            </div>
            <div class="loading-gif2" id="loading">
                <img src="/images/loading.gif"
                     style="width: 60px; height: auto;"/>
            </div>
            <div id="carpool-results">
            </div>
        </div>
    </div>
    <script type="text/javascript">
        let data = "${data?js_string}"
        let payload = "${query?js_string}"
    </script>
    <script src="/js/results.js"></script>
</#assign>
<#include "main.ftl">
