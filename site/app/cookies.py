from flask import session


def getFromSession(key, defaultValue=None):
    if key in session:
        return session[key]
    else:
        session[key] = defaultValue
        return defaultValue


def setToSession(key, value):
    session[key] = value
