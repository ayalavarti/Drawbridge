<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">

    <#--  Google Sign In  -->
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="682909194982-g9daf2o99e5jtijv18i92r1pdmrjk0ec.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>

    <#--  Mapbox Scripts and Styling  -->
    <script src='/js/external/es6-promise.auto.min.js'></script>
    <script src="/js/external/mapbox-sdk.min.js"></script>
    <script src='https://api.mapbox.com/mapbox-gl-js/v0.53.0/mapbox-gl.js'></script>
    <link href='https://api.mapbox.com/mapbox-gl-js/v0.53.0/mapbox-gl.css' rel='stylesheet' />

    <#--  JQuery and Main.js  -->
    <script src="/js/external/jquery-3.1.1.js"></script>
    <script src="/js/main.js"></script>

    <#--  Datetime Picker  -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

    <script src="https://unpkg.com/popper.js@1/dist/umd/popper.min.js"></script>
	  <script src="https://unpkg.com/tippy.js@4"></script>

    <#--  Normalize CSS  -->
    <link rel="stylesheet" href="/css/external/normalize.css"  type="text/css">
    <link rel="stylesheet" href="/css/external/html5bp.css"  type="text/css">
    <link rel="stylesheet" href="/css/main.css"  type="text/css">
    ${stylesheets}

    <#--  Font Awesome and Google Fonts  -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700|Nunito:400,600,700" rel="stylesheet">
    <link rel="shortcut icon" href="/images/favicon.png" type="image/x-icon" />
  </head>

  <body>
    <div class="header">
      <div style="visibility: hidden;" id="profile-info" class="profile-btn dropdown">
        <div class="user-info" id = "user-name"></div>
        <div id = "profile-picture-wrapper">
          <div class="dropdown-content">
            <a class="dropdown-filler"></a>
            <a class="dropdown-top" href="#">My Trips</a>
            <a class="dropdown-bottom" onclick="signOut();">Sign Out</a>
          </div>
        </div>
      </div>
      <div style="visibility: hidden;" id="sign-in" class="profile-btn">
        <div class="g-signin2" data-onsuccess="onSignIn" data-onfailure="onFailure" data-longtitle="true"></div>
      </div>
    </div>

    <div style="visibility: hidden; top: 2%;" id="home-btn" class="home-btn" onclick="toHome();">
      <img style="height: 15px; width: auto" src = "/images/home-icon.png"/>
    </div>

    <div style="visibility: hidden; bottom: 2%; position: fixed;" id="info-btn" class="info-btn" onclick="toInfo();">
      <img style="height: 15px; width: auto" src = "/images/info-icon.png" />
    </div>

    <div class = "body">
      ${content}
    </div>
  </body>
</html>