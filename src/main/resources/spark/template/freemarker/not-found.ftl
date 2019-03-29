<#assign stylesheets>
</#assign>

<#assign content>
    <div id="container">
        <img src="/images/not-found.jpg" style="width: 100vw;" />
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