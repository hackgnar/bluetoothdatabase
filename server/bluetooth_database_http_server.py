from twisted.internet import reactor
from twisted.web import static, server
from twisted.web.resource import Resource
from twisted.python import log

import json

log.startLogging(open('./http_access.log', 'w'))


class Hello(Resource):

    def getChild(self, name, request):
        return self

    def render_GET(self, request):
        request.setResponseCode(404)
        return "<html><body>Nada....</body></html>"

    def render_POST(self, request):
        response = "<html><body>Nada....</body></html>"
        if request.uri == "/bluetoothdata":
            data = request.content.read()
            print data
            json_data = json.loads(data)
            f = open(str(json_data['timestamp']) + '.' + str(json_data['bluetoothAddress']).replace(':','.') + '.json', 'wb')
            f.write(data)
            f.close()
            request.setResponseCode(200)
            response = "<html><body>thx....</body></html>"
        else:
            request.setResponseCode(404)
        return response

site = server.Site(Hello())
reactor.listenTCP(8163, site)
reactor.run()
