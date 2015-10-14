from flask import render_template, Flask
from app import app


ipcamUser = 'sel630'
ipcamPasswd = 'sel630'


@app.route('/')
@app.route('/index/')
def index():
    return render_template("index.html")


@app.route('/cam/', methods=['POST'])
def cam():
    if not request.json:
        return "No json received"

    params = request.get_json()
    action = []

    if 'move' in params:
        for x in params['move']:
            if x in ['up', 'down', 'left', 'right', 'stop']:
                action.append(('cmd', x))
