#!/usr/bin/python
import os
import urllib
import urllib2
import cookielib
import ConfigParser
config = ConfigParser.ConfigParser()
config.read('appengine.conf')
adminEmail = config.get('admin details', 'ADMIN_EMAIL', 0)[1:-1]
adminPassword = config.get('admin details', 'ADMIN_PASSWORD', 0)[1:-1]
routerName = config.get('app data', 'ROUTER_NAME', 0)[1:-1]
targetAuthURL = 'https://homework-notify.appspot.com/notify/1/'
appName = 'homework-notify'
cookiejar = cookielib.LWPCookieJar()
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookiejar))
urllib2.install_opener(opener)

authURL = 'https://www.google.com/accounts/ClientLogin'
authRequestData = urllib.urlencode({"Email": adminEmail, "Passwd": adminPassword, "service":"ah", "source":appName, "accountType": "HOSTED_OR_GOOGLE"})
authRequest = urllib2.Request(authURL, data=authRequestData)
authResponse = urllib2.urlopen(authRequest)
authResponseBody = authResponse.read()
authResponseDict = dict(x.split("=") for x in authResponseBody.split("\n") if x)
authToken = authResponseDict["Auth"]
requestArgs = {}
requestArgs['continue'] = targetAuthURL
requestArgs['auth'] = authToken
loginURL = "https://homework-notify.appspot.com/_ah/login?%s" % (urllib.urlencode(requestArgs))
request = urllib2.Request(loginURL)
response = urllib2.urlopen(request)
responseBody = response.read()
f = open('/etc/homework/notification.conf', 'w')
f.write("router_id = %s" % (responseBody))
f.close()
addURL = "https://homework-notify.appspot.com/notify/1/%s/add/" % (responseBody)
addRequestData = urllib.urlencode({"name": routerName})
addRequest = urllib2.Request(addURL, data=addRequestData)
addResponse = urllib2.urlopen(addRequest)
addResponseBody = addResponse.read()
print addResponseBody
