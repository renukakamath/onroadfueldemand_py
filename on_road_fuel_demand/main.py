from flask import *
from database import *
from admin import admin
from public import public
from api import api

app=Flask(__name__)
app.secret_key="abacd"
app.register_blueprint(public)
app.register_blueprint(admin,url_prefix='/admin')
app.register_blueprint(api,url_prefix='/api')


app.run(debug=True,port=5034,host="")