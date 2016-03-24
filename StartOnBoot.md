# Manifest modifications #
Not tested but found with google...
```
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<receiver android:name=".StartupTest.TesteStartup">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" /> 
        <category android:name="android.intent.category.LAUNCHER" /
    </intent-filter>
</receiver>
<service android:name=".ServiceClass"/> 
```