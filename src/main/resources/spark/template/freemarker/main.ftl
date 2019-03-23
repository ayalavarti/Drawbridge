<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="682909194982-g9daf2o99e5jtijv18i92r1pdmrjk0ec.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script src='/js/mapbox-gl.js'></script>

    <link rel='stylesheet' href='/css/mapbox-gl.css' />
    <link rel="stylesheet" href="/css/normalize.css"  type="text/css">
    <link rel="stylesheet" href="/css/html5bp.css"  type="text/css">
    <link rel="stylesheet" href="/css/main.css"  type="text/css">
    <link rel="stylesheet" href="/css/map.css"  type="text/css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/google-fonts.css">
    <link rel="shortcut icon" href="/images/favicon.png" type="image/x-icon" />

    ${stylesheets}
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

      <div id="sign-in" class="profile-btn">
        <div class="g-signin2" data-onsuccess="onSignIn" data-onfailure="onFailure" data-longtitle="true"></div>
      </div>
    </div>

    <div class = "body">
      ${content}
    </div>

    <script src="/js/main.js"></script>
  </body>
</html>