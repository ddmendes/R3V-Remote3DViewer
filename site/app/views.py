import requests
from flask import render_template, request
from app import app
import pdb


ipcamProtocol = 'http://'
ipcamAddress = '143.107.235.49:8085'
ipcamWebctl = '/decoder_control.cgi'
ipcamUser = 'sel630'
ipcamPasswd = 'sel630'
ipcamCommands = {'up': 2, 'down': 0, 'left': 6, 'right': 4}


@app.route('/')
@app.route('/index/')
def index():
    return render_template("index.html")


@app.route('/cam/', methods=['GET', 'POST'])
def cam():
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


@app.route('/about/')
def about():
    return render_template("about.html")


@app.route('/camerastream/')
def camera_stream():
    return render_template("camerastream.html")
