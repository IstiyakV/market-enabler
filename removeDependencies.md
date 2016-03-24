#ideas to get rid of dependencies


# Clearing the cache trough API (one step in removing su app dependencies) #
Maybe following permission could help...

```
Allows an application to delete cache files.
Constant Value: "android.permission.DELETE_CACHE_FILES"
```

or

```
Allows an application to clear the caches of all installed applications on the device.
Constant Value: "android.permission.CLEAR_APP_CACHE"
```

Have to test and see if this permissions can be used without the need of the system certificate.

# Using the setprop commands trough API (one step in removing su app dependencies) #
Using reflection it would work but just if the app has been compiled with the system certificate

# Killing the Vending process (removing busybox dependencies) #
Can easily be done with the restart application permission
```
<uses-permission android:name="android.permission.GET_TASKS"/>
<uses-permission android:name="android.permission.RESTART_PACKAGES"/>
```
[Example of how to get the task list and restart (kill) an application](http://code.google.com/p/andrac/source/browse/trunk/racTasks/src/ch/racic/android/ractasks/ractasks.java)