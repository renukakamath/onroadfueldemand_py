from flask import *
from database import *
public=Blueprint('public',__name__)

@public.route('/')
def main_home():
	return render_template("main_home.html")

@public.route('/login',methods=['get','post'])
def login():
	if 'Login' in request.form:
		username=request.form['username']
		password=request.form['Password']
		q="select * from login where(username='%s' and password='%s')"%(username,password)
		res=select(q)
		if res:
			session['login_d']=res[0]['login_id']
			if res[0]['user_type']=="admin":
				return redirect(url_for('admin.admin_home'))
	return render_template('login.html')