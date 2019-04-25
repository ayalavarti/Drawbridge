<#assign stylesheets>
    <link rel="stylesheet" href="/css/results.css" type="text/css">
    <link rel="stylesheet" href="/css/my-trips.css" type="text/css">
</#assign>

<#assign content>
    <div id="container">
        <h1 id="title">
            <img id="trips-picture" src="/images/temp.png"/> My Trips
        </h1>

        <div id="my-trips-wrapper" style="display: none">
            <div class="sub-heading-left">Hosting</div>
            <img src="/images/divider.png" style="height: 4px; width: auto;"/>
            <div class="loading-gif2" id="loading" style="display: none;">
                <img src="/images/loading.gif"
                     style="width: 60px; height: auto;"/>
            </div>
            <div id="hosting" class="results-content"></div>

            <div class="sub-heading-left">Confirmed Membership</div>
            <img src="/images/divider.png" style="height: 4px; width: auto;"/>
            <div id="member" class="results-content"></div>

            <div class="sub-heading-left">Pending</div>
            <img src="/images/divider.png" style="height: 4px; width: auto;"/>
            <div id="pending" class="results-content"></div>
        </div>
    </div>
    <script src="/js/my-trips.js"></script>
</#assign>
<#include "main.ftl">
