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
                <p>
                    Search for a carpool on the home screen inputting your desired locations and travel date/time.
                    You can also click on the map to choose your start and end locations.
                    Click submit and we'll find possible carpooling options for you!
                    If you don't find anything that works for you, you can always host your own carpool by clicking host
                    on the results page or by creating a new at the bottom right.
                </p>
            </div>

            <h2>Managing your trip</h2>
            <img alt="" src="/images/divider.png" class="divider"/>
            <div class='info-container'>
                <div><img alt="info" src="/images/info/members.png"
                          style="height: 200px; width: auto;"/></div>
                <p>
                    If you're hosting a trip, you'll be able to see the members in your trip and anyone that has requested to join.
                    You can approve or deny requests to join and view the current members of the trip.
                    Once the trip is full, no new requests to join can be made and you cannot approve any other requests.
                    You can also delete the trip if you no longer can host the carpool.
                </p>
            </div>

            <h2>Privacy policy</h2>
            <img alt="" src="/images/divider.png" class="divider"/>
            <div class='info-container'>
                <p>
                    We only keep your trip data around until 30 minutes after your trip has completed.
                    We do not store your phone number or locations after this point, and we do not share any data with any other parties.
                    We only store your name, email, and profile picture associated with your Google account and we do not share any of this data either.
                    For information about your account, please refer to the Google privacy policy.
                </p>
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
