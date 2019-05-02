<#assign stylesheets>
    <link rel="stylesheet" href="/css/info.css" type="text/css">
</#assign>

<#assign content>
    <div id="container">
        <div class="title">
            <h1>About Drawbridge</h1>
        </div>
        <div class="section">
            <p>Drawbridge is an application that allows users to efficiently
                carpool to nearby locations.
                Many students may share connections at different universities
                and miss out on the opportunity to carpool with others traveling
                to the same destination.
            </p>

            <h2>Search for Carpools</h2>
            <img alt="" src="/images/divider.png" class="divider"/>
            <div class='info-container img'>
                <div><img alt="info" src="/images/info/search.png"
                          style="height: 200px; width: auto;"/></div>
                <p>
                    Search for a carpool on the home screen inputting your
                    desired locations and travel date/time.
                    You can also click on the map to choose your start and end
                    locations.
                    Click submit and we'll find possible carpooling options for
                    you!
                    If you don't find anything that works for you, you can
                    always host your own carpool by clicking host
                    on the results page or by creating a new at the bottom
                    right.
                </p>
            </div>

            <h2>Managing your Trip</h2>
            <img alt="" src="/images/divider.png" class="divider"/>
            <div class='info-container img'>
                <p>
                    If you're hosting a trip, you'll be able to see the members
                    in your trip and anyone that has requested to join.
                    You can approve or deny requests to join and view the
                    current members of the trip.
                    Once the trip is full, no new requests to join can be made
                    and you cannot approve any other requests.
                    You can also delete the trip if you no longer can host the
                    carpool.
                </p>
                <div><img alt="info" src="/images/info/members.png"
                          style="height: 200px; width: auto;"/></div>
            </div>

            <h2>Privacy Policy</h2>
            <img alt="" src="/images/divider.png" class="divider"/>
            <div class='info-container' style="justify-content: left;">
                <h3 style="font-weight: 700; margin-top: 0;">Information
                    Collection and
                    Use</h3>
                <p>
                    We collect several different types of information for
                    various purposes to provide our application to you.
                </p>
                <h4>Personal Data</h4>
                <p>
                    While using Drawbridge, we ask you to provide us with
                    personally identifiable information. This
                    consists of:
                </p>
                <ul>
                    <li>Email Address</li>
                    <li>First name and last name</li>
                    <li>Google profile picture</li>
                </ul>
                <p>
                    We only use your Personal Data for internal identification
                    purposes. We do not share any data with any other parties.
                    For information about your account, please refer to the
                    Google Privacy Policy.
                </p>

                <h4>Location Data</h4>
                <p>
                    We use your current location to find geolocated points
                    closer to you when searching for locations. At any time,
                    you may opt to turn location services off.
                </p>

                <h4>Cookies Data</h4>
                <p>
                    We use cookies to track the activity on Drawbridge and we
                    hold limited information about session usage. Cookies are
                    files with a small amount of data that are sent to your
                    browser from a website and stored on your device. <br/>

                    You can instruct your browser to refuse to store all
                    cookies, but doing so may prevent you from using the full
                    features of Drawbridge.
                </p>

                <h4>Retention of Data</h4>
                <p>
                    Trip data is only stored in our databases until around 30
                    minutes after the trip has been completed. We do not
                    store your phone number or previously travelled locations
                    after this point, and data is not shared with any other
                    parties.
                </p>


                <h3 style="font-weight: 700;">Application Security</h3>
                <p>
                    Note that trip data can be viewed publicly, as well as
                    starting location and time. We, Drawbridge, are not held
                    liable for disclosing this information, and potential
                    users should be aware of this before using our
                    application. Starting locations and times should be
                    determined under the discretion of the user.
                </p>
            </div>
        </div>
        <div class="section">
            <h2>The Team</h2>
            <img alt="divider" src="/images/divider.png" class="divider"/>

            <div id="people">
                <div class="person">
                    <img alt="Arvind" src="/images/team/arvind.png">
                    <h3>Arvind Yalavarti</h3>
                </div>
                <div class="person">
                    <img alt="Jeff" src="/images/team/jeff.png">
                    <h3>Jeffrey Zhu</h3>
                </div>
                <div class="person">
                    <img alt="Sam" src="/images/team/sam.png">
                    <h3>Sam Maffa</h3>
                </div>
                <div class="person">
                    <img alt="Mark" src="/images/team/mark.png">
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
