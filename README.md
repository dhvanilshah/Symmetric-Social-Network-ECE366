# Nginx

In our project, we are using Nginx both as a server for our static HTML files, as well as a reverse proxy for our Apollo server.  Nginx is configured to listen on port 8000 and acts as a reverse proxy to `localhost:8080` which is where the Apollo server is listening.  

The static files can be found in */var/www/disc.cool* and are referred to by the `root` directive in the actual Nginx code.  The Nginx code can be found in */etc/nginx/sites-enabled/disc.cool* and the configuration file, which includes all code in the */etc/nginx/sites-enabled/* directory can be found in */etc/nginx/nginx.conf*.  

In order to run Nginx, run the following command: `sudo nginx` and in order to stop Nginx, run: `sudo nginx -s stop`.  A very helpful guide for running Nginx on an Ubuntu VM (which is what we are doing) can be found [here](https://medium.com/@jgefroh/a-guide-to-using-nginx-for-static-websites-d96a9d034940?fbclid=IwAR2HYBfjMCbsoSDHM9SHxzrMWqOVn5nwLl1OegxakSP9Sp2OR5fa6gj9msw).

Below is our actual code in the file */etc/nginx/sites-enabled/disc.cool*:

```
server {
  listen 8000 default_server;
  listen [::]:8000 default_server;
  root /var/www/disc.cool/;
  index index.html;
  server_name disc.cool www.disc.cool;
  location ~ ^/api/(.*)$  {
	# insert Apollo IP address and server port number below
    	proxy_pass http://199.98.27.115:8080/$1;
  }
}
```

The `location` directive tells Nginx to reverse proxy whenever a URI contains */api/* and pass whatever argument follows the */api/* segment to the Apollo server listening on port 8080.


