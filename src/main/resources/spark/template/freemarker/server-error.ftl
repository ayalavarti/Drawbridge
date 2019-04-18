<#assign stylesheets>
    <style>
        #container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            flex-wrap: wrap;
        }

        img {
            width: 350px;
        }
    </style>
</#assign>

<#assign content>
    <div id="container">
        <img src="/images/server-error.png"/>
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
