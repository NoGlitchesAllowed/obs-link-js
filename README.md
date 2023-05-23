# obs-link-js

obs-websocket proxying via browser source. Intended to be used for a portable OBS instance distributed to many people (e.g. runners/commentators of an event).

# Install & use example

On your server:

1. Install:
```
  npm install
  npm run build
```

2. Configure `config/default.json`. Make sure to pick a different secret for each type of connection (client/controller/manager)

3. Start the server with `npm run start`.

3. SSL-tunnel the server to a domain, e.g. with NGINX. **Do not skip this step.** MITM against the websocket would allow anyone to control every connected participant OBS. The implications of that for a live-streamed event do not need to be spelled out here.

OBS Setup:

1. In your to-be-distributed portable OBS, enable the in-built websocket server.

2. Add a 400x80 browser source in OBS targetted at `[[YOURDOMAIN.COM]]/?secret=[[CLIENTSECRET]]`. This browser source displays the target client id.

Accessing the websocket:

1. Obtain a client id either by looking at one of your talents' streams, or via a /manager connection.

2. Access the client OBS websocket connection with `wss://[[YOURDOMAIN.COM]/controller/?secret=[[CONTROLLERSECRET]]&targetId=[[TARGETID]]`, using for example https://obs-web.niek.tv