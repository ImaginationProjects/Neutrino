<?
/*include('global.php');
if(!isset($_SESSION['username']))
{
	header('location: index.php');
}*/
//$data = $core->GetFetch("SELECT * FROM users WHERE id = ".$_SESSION["id"]);
$ip = $_SERVER["REMOTE_ADDR"];
/*$core->SendMUSData("UPDATEIP ".$data['id']." ".$ip);
if($core->ExecuteQuery("UPDATE `users` SET `authenticator` = '".$ip."' WHERE `id` = ".$data['id'].";"))
{
// good!
}*/
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" xmlns:og="http://opengraphprotocol.org/schema/" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Habbo Hotel -  </title>

<script type="text/javascript">
var andSoItBegins = (new Date()).getTime();
</script>
<link rel="shortcut icon" href="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/v2/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="alternate" type="application/rss+xml" title="Habbo Hotel - RSS" href="http://www.habbo.com/articles/rss.xml" />
<meta name="csrf-token" content="0972d50f22"/>
<link rel="stylesheet" href="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/styles/common.css" type="text/css" />

<script src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/js/libs2.js" type="text/javascript"></script>
<script src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/js/visual.js" type="text/javascript"></script>
<script src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/js/libs.js" type="text/javascript"></script>
<script src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/js/common.js" type="text/javascript"></script>



<script type="text/javascript">

var ad_keywords = "lol,yay,awesome,right,troll,gender%3Am,age%3A23";

var ad_key_value = "kvage=23;kvgender=m;kvtags=yay:awesome:right:lol:troll";

</script>

<script type="text/javascript">
document.habboLoggedIn = true;
var habboName = "";
var habboId = 1;
var facebookUser = true;
var habboReqPath = "";
var habboStaticFilePath = "http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery";
var habboImagerUrl = "http://www.habbo.com/habbo-imaging/";
var habboPartner = "FBC";
var habboDefaultClientPopupUrl = "http://www.habbo.com/client";
window.name = "da5c2d578e1e4d7450ea9cee0664bd8426794a42";
if (typeof HabboClient != "undefined") {
    HabboClient.windowName = "da5c2d578e1e4d7450ea9cee0664bd8426794a42";
    HabboClient.maximizeWindow = true;
}


</script>

<meta property="fb:app_id" content="183096284873" />

<meta property="og:site_name" content="Habbo Hotel" />
<meta property="og:title" content="Habbo Hotel - " />
<meta property="og:url" content="http://www.habbo.com" />
<meta property="og:image" content="http://www.habbo.com/v2/images/facebook/app_habbo_hotel_image.gif" />
<meta property="og:locale" content="en_US" />

<noscript>
    <meta http-equiv="refresh" content="0;url=/client/nojs" />
</noscript>
<meta http-equiv="Pragma" content="no-cache, no-store" />
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Cache-Control" content="no-cache, no-store" />


<link rel="stylesheet" href="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/styles/habboflashclient.css" type="text/css" />
<script src="http://habbo.com/habboflashclient.js" type="text/javascript"></script>
<script src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/js/identity.js" type="text/javascript"></script>

<script type="text/javascript">
    FlashExternalInterface.loginLogEnabled = true;
    
    FlashExternalInterface.logLoginStep("web.view.start");
    
    if (top == self) {
        FlashHabboClient.cacheCheck();
    }
    var flashvars = {
            "client.allow.cross.domain" : "1", 
            "client.notify.cross.domain" : "1", 
            "connection.info.host" : "game-es2.habbo.com", 
            "connection.info.port" : "8542", 
            "site.url" : "http://www.habbo.com", 
            "url.prefix" : "http://www.habbo.com", 
            "client.reload.url" : "http://www.habbo.com/client", 
            "client.fatal.error.url" : "http://www.habbo.com/flash_client_error", 
            "client.connection.failed.url" : "http://www.habbo.com/client_connection_failed", 
            "external.variables.txt" : "http://private.habbo.ht/gamedata/external_variables.txt", 
            "external.texts.txt" : "http://private.habbo.ht/gamedata/external_flash_texts.txt", 
            "external.override.texts.txt" : "http://private.habbo.ht/gamedata/external_flash_override_texts.txt", 
            "external.override.variables.txt" : "http://www.private.habbo.ht/gamedata/external_override_variables.txt", 
            "productdata.load.url" : "http://private.habbo.ht/gamedata/productdata.txt", 
            "furnidata.load.url" : "http://private.habbo.ht/gamedata/furnidata.txt", 
            "sso.ticket" : "c872b1cf-875f-42b8-9e00-c247300f5db1", 
            "processlog.enabled" : "1", 
            "account_id" : "29269672", 
            "client.starting" : "Por favor espera, Habbo está cargando.", 
            "client.hotel_view.image.url" : "http://private.habbo.ht/c_images/hotel_view_images_hq/hotelview_xmas10_000.png", 
            "flash.client.url" : "habbo.com", 
            "user.hash" : "2926967293829f3ccb1a20a7ca4209439e67245d", 
            "facebook.user" : "1", 
            "has.identity" : "1", 
            "flash.client.origin" : "popup" 
    };
    var params = {
        "base" : "RELEASE63-201201190922-633225503 url",
        "allowScriptAccess" : "always",
        "menu" : "false"                
    };

        if (!(HabbletLoader.needsFlashKbWorkaround())) {
            params["wmode"] = "opaque";
        }

    FlashExternalInterface.signoutUrl = "https://www.habbo.com/account/logout?token=0972d50f22";

    var clientUrl = "RELEASE63-201201190922-633225503 url swf";
    swfobject.embedSWF(clientUrl, "flash-container", "100%", "100%", "10.0.0", "http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/flash/expressInstall.swf", flashvars, params, null, FlashExternalInterface.embedSwfCallback);

    window.onbeforeunload = unloading;
    function unloading() {
        var clientObject;
        if (navigator.appName.indexOf("Microsoft") != -1) {
            clientObject = window["flash-container"];
        } else {
            clientObject = document["flash-container"];
        }
        try {
            clientObject.unloading();
        } catch (e) {}
    }
    window.onresize = function() {
        HabboClient.storeWindowSize();
    }.debounce(0.5);
</script>

<meta name="description" content="Check into the world�s largest virtual hotel for FREE! Meet and make friends, play games, chat with others, create your avatar, design rooms and more�" />
<meta name="keywords" content="habbo hotel, virtual, world, social network, free, community, avatar, chat, online, teen, roleplaying, join, social, groups, forums, safe, play, games, online, friends, teens, rares, rare furni, collecting, create, collect, connect, furni, furniture, pets, room design, sharing, expression, badges, hangout, music, celebrity, celebrity visits, celebrities, mmo, mmorpg, massively multiplayer" />

<script src="//cdn.optimizely.com/js/13389159.js"></script>

<!--[if IE 8]>
<link rel="stylesheet" href="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/styles/ie8.css" type="text/css" />
<![endif]-->
<!--[if lt IE 8]>
<link rel="stylesheet" href="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/styles/ie.css" type="text/css" />
<![endif]-->
<!--[if lt IE 7]>
<link rel="stylesheet" href="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/styles/ie6.css" type="text/css" />
<script src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/static/js/pngfix.js" type="text/javascript"></script>
<script type="text/javascript">
try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {}
</script>

<style type="text/css">
body { behavior: url(/js/csshover.htc); }
</style>
<![endif]-->
<meta name="build" content="63-BUILD1023 - 29.12.2011 23:04 - com" />
</head>

<body id="client" class="flashclient">
<div id="overlay"></div>
<img src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/v2/images/page_loader.gif" style="position:absolute; margin: -1500px;" />

<div id="overlay"></div>
<div id="client-ui" >
    <div id="flash-wrapper">
    <div id="flash-container">
        <div id="content" style="width: 400px; margin: 20px auto 0 auto; display: none">
<div class="cbb clearfix">
    <h2 class="title">Please update your Flash Player to the latest version.</h2>
    <div class="box-content">
            <p>You can install and download Adobe Flash Player here: <a href="http://get.adobe.com/flashplayer/">Install flash player</a>. More instructions for installation can be found here: <a href="http://www.adobe.com/products/flashplayer/productinfo/instructions/">More information</a></p>
            <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://images.habbo.com/habboweb/63_1dc60c6d6ea6e089c6893ab4e0541ee0/846/web-gallery/v2/images/client/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
    </div>
</div>
        </div>
        <script type="text/javascript">
            $('content').show();
        </script>
        <noscript>
            <div style="width: 400px; margin: 20px auto 0 auto; text-align: center">
                <p>If you are not automatically redirected, please <a href="/client/nojs">click here</a></p>
            </div>
        </noscript>
    </div>
    </div>
	<div id="content" class="client-content"></div>            
</div>
    <script type="text/javascript">
        RightClick.init("flash-wrapper", "flash-container");
        if (window.opener && window.opener != window && window.opener.location.href == "/") {
            window.opener.location.replace("/me");
        }
        $(document.body).addClassName("js");
       	HabboClient.startPingListener();
        Pinger.start(true);
        HabboClient.resizeToFitScreenIfNeeded();
    </script>
<div id="fb-root"></div>

<script type="text/javascript">

    window.fbAsyncInit = function() {

        Cookie.erase("fbsr_183096284873");

        FB.init({appId: '183096284873', status: true, cookie: true, xfbml: true, oauth: true});

        $(document).fire("fbevents:scriptLoaded");



    };

    window.assistedLogin = function(FBobject, optresponse) {

        

        Cookie.erase("fbsr_183096284873");

        FB.init({appId: '183096284873', status: true, cookie: true, xfbml: true, oauth: true});



        permissions = 'user_birthday,email';

        defaultAction = function(response) {



            if (response.authResponse) {

                fbConnectUrl = "/facebook?connect=true";

                Cookie.erase("fbhb_val_183096284873");

                Cookie.set("fbhb_val_183096284873", response.authResponse.accessToken);

                Cookie.erase("fbhb_expr_183096284873");

                Cookie.set("fbhb_expr_183096284873", response.authResponse.expiresIn);

                window.location.replace(fbConnectUrl);

            }

        };



        if (typeof optresponse == 'undefined')

            FB.login(defaultAction, {scope:permissions});

        else

            FB.login(optresponse, {scope:permissions});



    };



    (function() {

        var e = document.createElement('script');

        e.async = true;

        e.src = 'https://connect.facebook.net/en_US/all.js';

        document.getElementById('fb-root').appendChild(e);

    }());

</script>

    <div id="FB_HiddenIFrameContainer" style="display:none; position:absolute; left:-100px; top:-100px; width:0px; height: 0px;"></div>
    <script type="text/javascript">
        FacebookIntegration.apiKey = "65d5e60e738877cb53bb5004edf6a8fc";
        FacebookIntegration.applicationId = "183096284873";
        FacebookIntegration.applicationName = "habbous";
        FacebookIntegration.badgeImagePath = "http://www.habbo.com/habbo-imaging/decorate/001";
        FacebookIntegration.viralPresentImagePath = "http://www.habbo.com/habbo-imaging/decorate/005";
        FacebookIntegration.viralPartnerCode = "FBVIR";
        FacebookIntegration.fbAppRequestUserFilter = "all";
        L10N.put("facebook.story.actionlink.text", "Get the Reward");
        L10N.put("facebook.story.name", "Breaking News From Habbo Hotel!");
        L10N.put("facebook.story.registration.name", "Welcome to Habbo Hotel");
        L10N.put("facebook.story.registration.description", "Starting a new life as a Habbo in Habbo Hotel.");
        L10N.put("facebook.story.registration.prompt", "You are a Habbo now. Whaddya think?");
        L10N.put("facebook.story.achievement.prompt", "Add a message to your friends");
        L10N.put("facebook.story.registration.caption", "{0} just checked into the Hotel!");
        L10N.put("facebook.story.achievement.caption", "{0} needs your help to get an achievement reward! Click on Get the Reward, and the both of you will get the reward points.");
        L10N.put("facebook.story.xmasviral.actionlink.text", "Open the package");
        L10N.put("facebook.story.xmasviral.prompt", "Tell your friends you need their help!");
        L10N.put("facebook.request.content.text", "I found a mystery package in Habbo! Please help me open it!");
        L10N.put("title.fb_app_request", "Send Ribbit Request");
        FacebookIntegration.initUI();
    </script>



        <iframe name="logframe" src="/bc/logframe?" width="0" height="0" frameborder="0" scrolling="no" marginwidth="0" marginheight="0" style="position: absolute; top:0; left:0"></iframe>


<iframe name="conversion-tracking" src="/conversion_tracking_frame" width="0" height="0" frameborder="0" scrolling="no" marginwidth="0" marginheight="0" style="position: absolute; top:0; left:0"></iframe>



<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-448325-2']);
  _gaq.push(['_trackPageview']);
  window.setTimeout("_gaq.push(['_setCustomVar', 5, 'facebook', 'client open', 2])", 2000);
  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>
<script type="text/javascript">
    HabboView.run();
</script>

</body>
</html>