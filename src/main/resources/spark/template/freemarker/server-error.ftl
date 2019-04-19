<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,maximum-scale=1">

    <meta property="og:url"
          content=""/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="Drawbridge"/>
    <meta property="og:description" content="Connecting the world. One trip a
            time."/>
    <meta property="og:image"
          content=""/>

    <#--  JQuery and Main.js  -->
    <script src="/js/external/jquery-3.1.1.js"></script>

    <#--  Tooltips  -->
    <script src="https://unpkg.com/popper.js@1/dist/umd/popper.min.js"></script>
    <script src="https://unpkg.com/tippy.js@4"></script>

    <#--  Normalize CSS  -->
    <link rel="stylesheet" href="/css/external/normalize.css" type="text/css">
    <link rel="stylesheet" href="/css/external/html5bp.css" type="text/css">
    <link rel="stylesheet" href="/css/main.css" type="text/css">

    <#--  Font Awesome and Google Fonts  -->
    <link rel="stylesheet"
          href="https://use.fontawesome.com/releases/v5.7.2/css/all.css"
          integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr"
          crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700|Nunito:400,600,700"
          rel="stylesheet">
    <link rel="shortcut icon" href="/images/favicon.png" type="image/x-icon"/>
</head>

<body>
<div class="body">
    <a href="/">
        <div style="top: 2%;" id="home-btn" class="home-btn">
            <img style="height: 15px; width: auto" src="/images/home-icon.png"/>
        </div>
    </a>

    <a href="/help">
        <div style="bottom: 2%; position: fixed;" id="info-btn"
             data-tippy-content="Drawbridge Info"
             class="fixed-controls info-btn">
            <img style="height: 15px; width: auto" src="/images/info-icon.png"/>
        </div>
    </a>

    <a href="/new">
        <div style="display: none; bottom: 2%; right: 90px; position: fixed;"
             id="new-btn" data-tippy-content="Host a Carpool"
             class="fixed-controls new-btn">
            <img style="height: 15px; width: auto" src="/images/host-icon.png"/>
        </div>
    </a>

    <div id="container">
        <img src="/images/server-error.png"/>
    </div>
</div>
</body>
</html>

<script>
    tippy(".fixed-controls", {
        animation: "scale",
        arrow: true,
        arrowType: "round",
        theme: "drawbridge-alt",
        interactive: "true",
        hideOnClick: true,
        inertia: true,
        sticky: true,
        placement: "top",
    });
</script>

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
