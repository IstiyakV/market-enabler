<wiki:gadget url="http://buncha-toolz.googlecode.com/svn/trunk/google\_gadgets/custom\_adsense/custom\_adsense.xml" g\_client="pub-1304947992104673" g\_slot="3752098995" g\_format="336x280" width="600" height="280" up\_g\_client="pub-1304947992104673" up\_g\_slot="3752098995" up\_g\_format="336x280"/>
# Problem #
There exist certain ways how to drop a shell command from an application.
Some of them end in force close on some circumstances.

# Existing solution #
```
Process process = Runtime.getRuntime().exec("su");
DataOutputStream os = new DataOutputStream(process.getOutputStream());
DataInputStream osRes = new DataInputStream(process.getInputStream());
for (String single : commands) {
   os.writeBytes(single + "\n");
   os.flush();
   res.add(osRes.readLine());
}
os.writeBytes("exit\n");
os.flush();
process.waitFor();
```

# prior solution from rac #
found this [here](http://gimite.net/en/index.php?Run%20native%20executable%20in%20Android%20App)
```
Class<?> execClass = Class.forName("android.os.Exec");
Method createSubprocess = execClass.getMethod("createSubprocess",
	String.class, String.class, String.class, int[].class);
Method waitFor = execClass.getMethod("waitFor", int.class);
// Executes the command.
// NOTE: createSubprocess() is asynchronous.
int[] pid = new int[1];
FileDescriptor fd = (FileDescriptor)createSubprocess.invoke(
        		null, arg0, arg1, arg2, pid);
// Waits for the command to finish.
waitFor.invoke(null, pid[0]);
```
# ideas #
## Execute trough a shell script ##
The application ships with a generic shell script and a normal shell command will be dropped.
```
#!/system/bin/sh
# MarketEnabler example set script
# Usage: has to be called with all the values as arguments
setprop gsm.sim.operator.numeric $1
setprop gsm.operator.numeric $2
setprop gsm.sim.operator.iso-country $3
setprop gsm.operator.iso-country $4
setprop gsm.operator.alpha $5
setprop gsm.sim.operator.alpha $6
kill $(ps | grep vending | tr -s ' ' | cut -d ' ' -f2)
rm -rf /data/data/com.android.vending/cache/* 
```
As I played on my phone with different ways to execute this commands, this was the only way that executed the commands all times and very fast.
## Using API for getting values and setting of some values ##
Setting operator values (untested):
```
android.telephony.ServiceState.setOperatorName(String longName, String shortName, String numeric)
```
Getting values (working):
```
TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
tm.getNetworkCountryIso()
tm.getNetworkOperator()
tm.getNetworkOperatorName()
tm.getSimCountryIso()
tm.getSimOperator()
tm.getSimOperatorName()
```

Set values can maybe done trough `android.os.SystemProperties.set(String key, String val)` <== not anymore in 1.5