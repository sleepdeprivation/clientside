# clientside


////////////////////////////////////////////////////////////////////////

So, we need an API key for google maps.

Following these instructions:

https://developers.google.com/maps/documentation/android/start#getting_the_google_maps_android_api_v2

, I have gotten myself an API key (using my debug keystore) and succesfully displayed a map on my android device.
There is a problem, however, with all of us getting our own API keys. Since we will all be pushing to the same repo, we will often need to change to our own API key after pulling. This is a pain.

There is a solution, however. I refer you to the user mente's answer (scroll down, it is not the accepted one) to this question:

http://stackoverflow.com/questions/4361942/one-google-maps-key-for-multiple-developers-android-eclipse-custom-keystore?rq=1

We may provide multiple SHA1 hashes to the google API key generator and it will provide us a single API key, which we can all use.

You can find your debug keystore (I think you must compile first) in


~/.android (linux)
C:\Users\your_user_name\.android\ (windows)

After which you should run

linux:
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

windows:
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android

Please copy the SHA1 hash and paste it above this message or bring it to the group meeting.

Thanks,

(cburke)

03/10/15[14:16:03]

////////////////////////////////////////////////////////////////////////




Alright... I think it's up. I've got google play services listed as a dependency too... Let's test us some maps, shall we?