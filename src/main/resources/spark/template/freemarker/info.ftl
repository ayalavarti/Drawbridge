<#assign stylesheets>
</#assign>

<#assign content>
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