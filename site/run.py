#!flask/bin/python
from app import app
app.secret_key = '0dQpnislbp'
app.run(host='0.0.0.0', threaded=True, debug=True)
