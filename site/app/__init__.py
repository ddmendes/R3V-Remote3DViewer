from flask import Flask

app = Flask(__name__)
from app import views

views.horPosition = 0.0
views.vertPosition = 0.0
