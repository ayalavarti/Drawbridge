<#assign stylesheets>
    <link rel="stylesheet" href="/css/info.css" type="text/css">
</#assign>

<#assign content>
    <div id="container">
        <div class="title">
            <h1>Frequently Asked Questions</h1>
        </div>
        <div class="section">
            <h2>About Drawbridge</h2>
            <img alt="" src="/images/divider.png" class="divider"/>
            <p>Drawbridge is an application that allows users to efficiently
                carpool to nearby locations.
                Many students may share connections at different universities
                and miss out on the opportunity to carpool with others traveling
                to the same destination.
            </p>

            <h2>Search for carpools</h2>
            <img alt="" src="/images/divider.png" class="divider"/>
            <div class='info-container'>
                <div><img alt="info" src="/images/info/search.png"
                          style="height: 200px; width: auto;"/></div>
                <div><p>Search for a carpool on the home screen by
                        inputting your
                        desired locations and travel date/time.</p></div>
            </div>
        </div>
        <div class="section">
            <h2>The Team</h2>
            <img alt="divider" src="/images/divider.png" class="divider"/>

            <div id="people">
                <div class="person">
                    <img alt="Arvind" src="/images/team/arvind.jpg">
                    <h3>Arvind Yalavarti</h3>
                </div>
                <div class="person">
                    <img alt="Jeff" src="/images/team/jeff.jpg">
                    <h3>Jeffrey Zhu</h3>
                </div>
                <div class="person">
                    <img alt="Sam" src="/images/team/sam.jpg">
                    <h3>Sam Maffa</h3>
                </div>
                <div class="person">
                    <img alt="Mark" src="/images/team/mark.jpg">
                    <h3>Mark Lavrentyev</h3>
                </div>
            </div>
        </div>
    </div>
</#assign>
<#include "main.ftl">

<script type="text/javascript">
    showHomeInfo();

    function onUserSignedIn() {
        console.log("User signed in.");
    }

    function onUserSignedOut() {
        console.log("User signed out.");
    }
</script>
