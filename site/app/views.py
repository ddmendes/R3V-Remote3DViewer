from flask import render_template
from app import app


@app.route('/')
@app.route('/index')
def index():
    # user = {'nickname': 'Miguel'}  # fake user
    # posts = [  # fake array of posts
    #     {
    #         'author': {'nickname': 'John'},
    #         'body': 'Beautiful day in Portland!'
    #     },
    #     {
    #         'author': {'nickname': 'Susan'},
    #         'body': 'The Avengers movie was so cool!'
    #     }
    # ]
    return render_template("index.html")


@app.route('/about')
def about():
    # user = {'nickname': 'Miguel'}  # fake user
    # posts = [  # fake array of posts
    #     {
    #         'author': {'nickname': 'John'},
    #         'body': 'Beautiful day in Portland!'
    #     },
    #     {
    #         'author': {'nickname': 'Susan'},
    #         'body': 'The Avengers movie was so cool!'
    #     }
    # ]
    return render_template("about.html")

@app.route('/camerastream')
def camera_stream():
    # user = {'nickname': 'Miguel'}  # fake user
    # posts = [  # fake array of posts
    #     {
    #         'author': {'nickname': 'John'},
    #         'body': 'Beautiful day in Portland!'
    #     },
    #     {
    #         'author': {'nickname': 'Susan'},
    #         'body': 'The Avengers movie was so cool!'
    #     }
    # ]
    return render_template("camerastream.html")


