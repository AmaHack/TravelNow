/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function()
    {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id)
    {
        console.log('Received Event: ' + id);
        
        try {
            alert('Device is ready! Make sure you set your app_id below this alert.');
            FB.init({ appId: "642248459160399", nativeInterface: CDV.FB, useCachedDialogs: false });
            document.getElementById('data').innerHTML = "";
        } catch (e) {
            alert(e);
        }
    },
    
    facebookLogin : function ()
    {
        if ((typeof cordova == 'undefined') && (typeof Cordova == 'undefined')) alert('Cordova variable does not exist. Check that you have included cordova.js correctly');
        if (typeof CDV == 'undefined') alert('CDV variable does not exist. Check that you have included cdv-plugin-fb-connect.js correctly');
        if (typeof FB == 'undefined') alert('FB variable does not exist. Check that you have included the Facebook JS SDK file.');

        
        FB.login(
                 function(response) {

//                 getLoginStatus();
                 
                 FB.api('/me', function(response) {
                        
                        if (response.authResponse != null)
                        {
                        
                        var access_token =   FB.getAuthResponse()['accessToken'];
                        
                        AccessTokenByFB = access_token;
                        console.log("AccessTokenByFB " + AccessTokenByFB);
                        }
                        
                        
                        console.log('Good to see you, ' + response.name + '.');
                        alert("Good to see you, " + response.name + ".")
                        
                        facebookCurrentLocation = response.location.name;
                        console.log("facebookCurrentLocation " + facebookCurrentLocation);
                        var re = /\s*, \s*/;
                        facebookCurrentCity = facebookCurrentLocation.split(re);
                        alert("facebookCurrentCity " + facebookCurrentCity[0]);
                        
//                        getFriendsCheckins();
                        
                        });
                 
                 if (response.session)
                 {
                 alert('logged in');
                 }
                 else
                 {
                 alert('not logged in');
                 }
                 },
                 { scope: "user_location, user_events friends_activities, friends_groups, friends_interests, friends_likes, friends_location, friends_events, friends_photos, friends_status, friends_groups, friends_likes, user_friends, user_interests, user_photo_video_tags, friends_photo_video_tags"}
                 );

    }
};
