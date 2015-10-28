from flask import render_template, request, Response
from app import app
import requests
import urllib
import pdb


ipcamProtocol = 'http://'
ipcamAddress = '143.107.235.49:8085'
ipcamWebctl = '/decoder_control.cgi'
ipcamVideostream = '/videostream.cgi'
ipcamUser = 'sel630'
ipcamPasswd = 'sel630'
ipcamResolution = '32'
ipcamRate = '0'
ipcamCommands = {'up': 2, 'down': 0, 'left': 6, 'right': 4}


@app.route('/')
@app.route('/index/')
def index():
    return render_template("index.html")


@app.route('/camposition/', methods=['GET', 'POST'])
def camposition():
    # pdb.set_trace()
    cmd = request.args['move']
    degree = request.args['degree']

    print cmd, degree
    r = requests.get(ipcamProtocol + ipcamAddress + ipcamWebctl,
                     {'user': ipcamUser,
                      'pwd': ipcamPasswd,
                      'command': cmd,
                      'onestep': 1,
                      'degree': degree})
    return r.text


@app.route('/camstream/')
def camstream():
    url = "%(protocol)s%(address)s%(cgi)s?user=%(user)s&pwd=%(pwd)s&resolution\
=%(resolution)s&rate=%(rate)s" % {'protocol': ipcamProtocol,
                                  'address': ipcamAddress,
                                  'cgi': ipcamVideostream,
                                  'user': ipcamUser,
                                  'pwd': ipcamPasswd,
                                  'resolution': ipcamResolution,
                                  'rate': ipcamRate}
    print url
    ws = urllib.urlopen(url)

    def stream():
        while(True):
            res = ""

            s = ws.readline()
            res = res + s

            s = ws.readline()
            res = res + s

            s = ws.readline()
            res = res + s

            length = int(s.split(':')[1].strip())

            s = ws.readline()
            res = res + s

            s = ws.read(length)
            res = res + s

            s = ws.readline()
            res = res + s

            yield res

    return Response(stream(), mimetype="multipart/x-mixed-replace; boundary=ipcamera")


@app.route('/about/')
def about():
    return render_template("about.html")


@app.route('/camerastream/')
def camera_stream():
    return render_template("camerastream.html")
