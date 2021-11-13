# noga-obs-link

This is an application made for
[No Glitches Allowed](https://noglitchesallowed.org) (NoGA) events.
It links our portable OBS, running "obs-websocket", to our services.
It also sends some additional system info & stats.

NOTE: This project is highly customized to NoGA's setup.
You may find use cases for it yourself, but you will likely need
to make major modifications.

## Security Notice for Switcher Server

**THE SWITCHER SERVER MUST BE BEHIND A SECURE REVERSE PROXY.**

Do NOT run the switcher server without an SSL reverse proxy!   
MITM attacks to the switcher server (possible without SSL) would allow **anyone**
to control **every** connected participant OBS.

The implications of that for a live-streamed marathon event
do not need to be spelled out here.


## Participant service
The "Participant" service, which is targeted as the main class,
runs on participant's machines and links their OBS to the Switcher service.

launchargs: *\[local OBS URI\]* *\[switcher server URI\]*

If empty, defaults to ws://127.0.0.1:4444 & wss://obslink.noglitchesallowed.org

## Switcher server
The Switcher service handles 3 different types of connections:
- Participant -> connections from "Local" services
- obs-websocket-js -> The switcher also acts as am OBS websocket itself,
**with the password acting as the switch.**
By supplying it with a password of "*\[server secret\]*:*\[tunnel-id\]*",
the switcher connects that instance of obs-websocket-js
to the participant-side OBS, removing the need for port-tunneling.
- Manager -> A bare-bones connection mode meant to be used
as a standalone connection from NodeCG.

launchargs: *\[hostname\]* *\[port\]* *\[secret\]*

## System Info & Stats injection

On connection, the participant service sends base level
system information to be used for debugging during
tech checking & live production. The data sent is defined by
`org.noglitchesallowed.obslink.system.info.SystemInfoSerializer`.

The Participant service also injects a custom field into all
"stats" responses (GetStats & Heartbeat) from obs-websocket-js.
See package `org.noglitchesallowed.obslink.system.stats`
for more details.



## Protocol spec

TODO (see code)

## Build instructions

TODO

Contact [fgeorjje@noglitchesallowed.org](mailto:fgeorjje@noglitchesallowed.org)
if you need help deploying this for your own event.

## License

AGPLv3, see COPYING for details.

TL;DR (provided for your own convenience, **not binding**):  
If you modify and host this yourself, publish your modifications
and make sure your users are prominently linked to your modified source.
Sharing is caring.

If you need to host a custom modification
without publishing source, email 
[fgeorjje@noglitchesallowed.org](mailto:fgeorjje@noglitchesallowed.org)
to discuss terms.

File exemptions:
- `org.noglitchesallowed.obslink.utils.OBSAuth` is licensed under GPLv2
as it contains pseudocode from obs-websocket.