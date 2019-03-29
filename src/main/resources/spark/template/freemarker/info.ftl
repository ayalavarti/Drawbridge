<#assign stylesheets>
    <link rel="stylesheet" href="/css/info.css" type="text/css">
</#assign>

<#assign content>
    <div id="container">
        <div class="title">
            <h1>Frequently Asked Questions</h1>
        </div>
        <div class="section">
            <h2>FAQ</h2>
            <img src="/images/divider.png" style="height: 3px; width: auto;" />
            <p><i>What does this app do?</i></p>
        </div>
        <div class="section">
            <h2>The Team</h2>
            <img src="/images/divider.png" style="height: 3px; width: auto;" />

            <div id="people">
                <div class="person">
                    <img src="/images/team/arvind.jpg">
                    <h3>Arvind Yalavarti</h3>
                </div>
                <div class="person">
                    <img src="/images/team/jeff.jpg">
                    <h3>Jeffrey Zhu</h3>
                </div>
                <div class="person">
                    <img src="/images/team/sam.jpg">
                    <h3>Sam Maffa</h3>
                </div>
                <div class="person">
                    <img src="/images/team/mark.jpg">
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