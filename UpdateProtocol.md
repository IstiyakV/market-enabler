# Server -> Android #
We need to specify a updateID so:

**updateID Or hash if needed**

**latestMarketEnabler version ---> For let the people stay up to date with the latest version ;)**

**note --> For eventual communication like "The server will not work for a while"**

**file xml with the values**




# Android -> Server #

**updateID of the xml stored locally**

**Cid e Lac**

**(Eventually) the xml of the local value.**





# Android --> Server : Problem with the app #
**Misc value about android firmware and so on**

# XML of the local value #

```
<configuration>

	<gsmSimOperatorNumeric>[MNCMCC]</gsmSimOperatorNumeric>

	<gsmSimOperatorIso-Country>[COUNTRYCODE]</gsmSimOperatorIso-Country>

	<gsmSimOperatorAlpha>[Alpha Operator Name]</gsmSimOperatorAlpha>

	<gsmOperatorNumeric>[MNCMCC]</gsmSimOperatorNumeric>

	<gsmOperatorIso-Country>[COUNTRYCODE]</gsmSimOperatorIso-Country>

	<gsmOperatorAlpha>[Alpha Operator Name]</gsmSimOperatorAlpha>

	<SettingsHash>[md5(all_values)]</SettingsHash>

</configuration>
```

SettingsHash is used as Index to search if actual configuration alredy exists and if not ask the server if he has this config

# XML OF Android-> Server Request #

```
<updateRequest>

	<lastUpdateId>[UPDATEID OR HASH]</lastUpdateId>

	<location cid="[CID]" lac="[LAC]"/>

	[XML OF THE LOCAL VALUE]

</updateRequest>
```

# XML OF Server -> Android Reply #


```
<updateReply>

	<lastAvailableId>[LastID or Hash]</lastAvailableId>

	<lastMarketEnablerVersion>[Version]</lastMarketEnablerVersion>

	<note>["nope"|some text]</note>

	<configurations>

		<configuration state="[Italy|Us|Switzerland|.......|Unknown]">

			<gsmSimOperatorNumeric>[MNCMCC]</gsmSimOperatorNumeric>

			<gsmSimOperatorIso-Country>[COUNTRYCODE]</gsmSimOperatorIso-Country>

			<gsmSimOperatorAlpha>[Alpha Operator Name]</gsmSimOperatorAlpha>

			<gsmOperatorNumeric>[MNCMCC]</gsmSimOperatorNumeric>

			<gsmOperatorIso-Country>[COUNTRYCODE]</gsmSimOperatorIso-Country>

			<gsmOperatorAlpha>[Alpha Operator Name]</gsmSimOperatorAlpha>

		</configuration>

		<configuration ....>

			...

			...

		</configuration>

		...

		

	</configurations>

</updateReply>

```