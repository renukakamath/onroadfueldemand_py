from flask import *
from database import *
admin=Blueprint('admin',__name__)

@admin.route('/admin_home',methods=['get','post'])
def admin_home():
	return render_template('admin_home.html')

@admin.route('/admin_manage_vehicles',methods=['get','post'])
def admin_manage_vehicles():
	data={}
	q="SELECT * FROM `vehicles` inner join fuel_type using(type_id)"
	res=select(q)
	data['vehicles']=res
 
	q1="select * from fuel_type"
	res1=select(q1)
	data['fuel_type']=res1

	

	if 'action' in request.args:
		action=request.args['action']
		vid=request.args['vid']
		# print(vid,"Value...._____________________________________________")
	else:
		action=None
	if action=='update':
		q="SELECT * FROM `vehicles` inner join fuel_type using(type_id) WHERE `vehicle_id`='%s'"%(vid)
		res=select(q)
		data['updatess']=res

	if action=='remove':
		q="DELETE FROM `vehicles` WHERE `vehicle_id`='%s'"%(vid)
		delete(q)
		flash('removed')
		return redirect(url_for('admin.admin_manage_vehicles'))


	if 'submitss' in request.form:
		regnum=request.form['regnum']
		vehicle=request.form['vehicle']
		capacity=request.form['capacity']
		q="UPDATE `vehicles` SET `regnum`='%s',`vehicle`='%s',`capacity`='%s' WHERE `vehicle_id`='%s'"%(regnum,vehicle,capacity,vid)
		update(q)
		flash('successfully updated')
		return redirect(url_for('admin.admin_manage_vehicles'))

	if 'submit' in request.form:
		fuel=request.form['fuel']
		regnum=request.form['regnum']
		vehicle=request.form['vehicle']
		capacity=request.form['capacity']
		
		q="INSERT INTO `vehicles`(`type_id`,`regnum`,`vehicle`,`capacity`)VALUES('%s','%s','%s','%s')"%(fuel,regnum,vehicle,capacity)
		vid=insert(q)
		q="INSERT INTO `stock` VALUES(NULL,'%s','0',NOW())"%(vid)
		insert(q)
		flash('success')
		return redirect(url_for('admin.admin_manage_vehicles'))

	return render_template('admin_manage_vehicles.html',data=data)


@admin.route('/admin_manage_fuel_type',methods=['get','post'])
def admin_manage_fuel_type():
	data={}
	q="SELECT * FROM `fuel_type`"
	res=select(q)
	data['fuel']=res

	

	if 'action' in request.args:
		action=request.args['action']
		tid=request.args['tid']
	
	else:
		action=None
	if action=='update':
		q="SELECT * FROM `fuel_type` WHERE `type_id`='%s'"%(tid)
		res=select(q)
		data['upftype']=res

	if action=='remove':
		q="DELETE FROM `fuel_type` WHERE `type_id`='%s'"%(tid)
		delete(q)
		flash('removed')
		return redirect(url_for('admin.admin_manage_fuel_type'))


	if 'utype' in request.form:
		ftype=request.form['ftype']
		rate=request.form['rate']
  
		q="UPDATE `fuel_type` SET `type_name`='%s',`rate`='%s',`date_time`=NOW() WHERE `type_id`='%s'"%(ftype,rate,tid)
		update(q)
		flash('successfully updated')
		return redirect(url_for('admin.admin_manage_fuel_type'))

	if 'type' in request.form:
		ftype=request.form['ftype']
		rate=request.form['rate']
		
		q="INSERT INTO `fuel_type` VALUES(NULL,'%s','%s',NOW())"%(ftype,rate)
		insert(q)
		flash('success')
		return redirect(url_for('admin.admin_manage_fuel_type'))

	return render_template('admin_manage_fuel_type.html',data=data)




@admin.route('/admin_view_registered_users',methods=['get','post'])
def admin_view_registered_users():
	data={}
	q="SELECT * FROM `users`"
	res=select(q)
	data['users']=res
	return render_template('admin_view_registered_users.html',data=data)

@admin.route('/admin_manage_drivers',methods=['get','post'])
def admin_manage_drivers():
	data={}
	q="SELECT *,CONCAT(`first_name`,' ',`last_name`)AS `d_name` FROM `drivers`"
	res=select(q)
	data['drivers']=res

	

	if 'action' in request.args:
		action=request.args['action']
		did=request.args['did']
		
	else:
		action=None
	if action=='update':
		q="SELECT * FROM `drivers` WHERE `driver_id`='%s'"%(did)
		res=select(q)
		data['updatess']=res

	if action=='remove':
		q="DELETE FROM `drivers` WHERE `driver_id`='%s'"%(did)
		delete(q)
		flash('removed')
		return redirect(url_for('admin.admin_manage_drivers'))


	if 'submitss' in request.form:
		fname=request.form['fname']
		lname=request.form['lname']
		phone=request.form['phone']
		email=request.form['email']
		lnum=request.form['lnum']
		q="UPDATE `drivers` SET `first_name`='%s',`last_name`='%s',`phone`='%s',`email`='%s',`licensenum`='%s' WHERE `driver_id`='%s'"%(fname,lname,phone,email,lnum,did)
		update(q)
		flash('successfully updated')
		return redirect(url_for('admin.admin_manage_drivers'))

	if 'submit' in request.form:
		fname=request.form['fname']
		lname=request.form['lname']
		phone=request.form['phone']
		email=request.form['email']
		lnum=request.form['lnum']
		uname=request.form['uname']
		pwd=request.form['pwd']

		q1="INSERT INTO `login`(`username`,`password`,`user_type`)VALUES('%s','%s','driver')"%(uname,pwd)
		res=insert(q1)
		data['login']=res
		
		
		q="INSERT INTO `drivers`(`login_id`,`first_name`,`last_name`,`phone`,`email`,`licensenum`)VALUES('%s','%s','%s','%s','%s','%s')"%(res,fname,lname,phone,email,lnum)
		insert(q)
		flash('success')
		return redirect(url_for('admin.admin_manage_drivers'))

	return render_template('admin_manage_drivers.html',data=data)

@admin.route('/admin_assign_vehicles',methods=['get','post'])
def admin_assign_vehicles():
	data={}
	did=request.args['did']
	name=request.args['name']
	data['name']=name

	q1="SELECT * FROM `vehicles`"
	res=select(q1)
	data['vehicles']=res

	q="SELECT *,CONCAT(`first_name`,' ',`last_name`)AS `d_name` FROM `drivers`WHERE `driver_id`='%s'"%(did)
	res=select(q)
	data['drivers']=res

	if 'submits' in request.form:
		
		v_id=request.form['vname']
		dname=request.form['dname']
		latitude=request.form['latitude']
		longitude=request.form['longitude']
  
		q="SELECT * FROM `assign_vehicle` WHERE `driver_id`='%s'"%(did)
		rs=select(q)
		if rs:
			flash("Already Alloted")
			return redirect(url_for('admin.admin_manage_drivers'))
		else:
			q="INSERT INTO `assign_vehicle`(`vehicle_id`,`driver_id`,`latitude`,`longitude`)VALUES('%s','%s','%s','%s')"%(v_id,did,latitude,longitude)
			res=insert(q)
			flash('success')
			return redirect(url_for('admin.admin_manage_drivers'))
	

	return render_template('admin_assign_vehicles.html',data=data)
@admin.route('/admin_view_assigned_vehicle')
def admin_view_assigned_vehicle():
	data={}
	did=request.args['did']
	q="SELECT * FROM `assign_vehicle` INNER JOIN `vehicles` USING(`vehicle_id`) INNER JOIN `drivers` USING(`driver_id`)WHERE `driver_id`='%s'"%(did)
	res=select(q)
	data['assign']=res
	return render_template('admin_view_assigned_vehicle.html',data=data)
@admin.route('/admin_view_bookings',methods=['get','post'])
def admin_view_bookings():
	data={}
	q="SELECT *,CONCAT(`drivers`.`first_name`,' ',`drivers`.`last_name`)AS `d_name`,CONCAT(`users`.`first_name`,' ',`users`.`last_name`)AS `u_name` FROM `bookings` INNER JOIN `users` USING(`user_id`) INNER JOIN `drivers` USING(`driver_id`)"
	res=select(q)
	data['bookings']=res
	return render_template('admin_view_bookings.html',data=data)
@admin.route('/admin_view_payments',methods=['get','post'])
def admin_view_payments():
	data={}
	q="SELECT *,CONCAT(users.`first_name`,' ',users.`last_name`)AS `u_name`,CONCAT(drivers.`first_name`,' ',drivers.`last_name`)AS `d_name`  FROM `payments` INNER JOIN `bookings` USING(`booking_id`) INNER JOIN `users` USING(`user_id`) INNER JOIN `drivers` USING(`driver_id`)"
	res=select(q)
	data['payments']=res
	return render_template('admin_view_payments.html',data=data)

@admin.route('/admin_view_rating_and_review',methods=['get','post'])
def admin_view_rating_and_review():
	data={}
	q="SELECT *,CONCAT(`users`.`first_name`,' ',`users`.`last_name`)AS `u_name`,CONCAT(`drivers`.`first_name`,' ',`drivers`.`last_name`)AS `d_name` FROM `rating` INNER JOIN `bookings` USING(`booking_id`) INNER JOIN `users` USING(`user_id`)INNER JOIN `drivers` USING(`driver_id`)"
	res=select(q)
	data['rating']=res
	return render_template('admin_view_rating_and_review.html',data=data)

@admin.route('/admin_view_complaints',methods=['get','post'])
def admin_view_complaints():
	data={}
	q="SELECT *,CONCAT(`first_name`,' ',`last_name`)AS `name` FROM `complaints` INNER JOIN `users` USING(`user_id`)"
	res=select(q)
	data['complaints']=res
	j=0
	for i in range(1,len(res)+1):
		print('submit'+str(i))
		if 'submit'+str(i) in request.form:
			reply=request.form['reply'+str(i)]
			print(reply)
			print(j)
			print(res[j]['complaint_id'])
			q="update complaints set reply='%s' where complaint_id='%s'" %(reply,res[j]['complaint_id'])
			print(q)
			update(q)
			flash("success")
			return redirect(url_for('admin.admin_view_complaints')) 	
		j=j+1
	return  render_template('admin_view_complaints.html',data=data)








	# q="SELECT *,CONCAT(`first_name`,' ',`last_name`)AS `name` FROM `complaints` INNER JOIN `users` USING(`user_id`)"
	# res=select(q)
	# data['complaints']=res
	# j=0
	# for i in range(1,len(res)+1):
	# 	print('submit'+str(i))
	# 	if 'submit'+str(i) in request.form:
	# 		reply=request.form['reply'+str(i)]
	# 		print(reply)
	# 		print(j)
	# 		print(res[j]['complaint_id'])
	# 		q="update complaints set reply='%s' where complaint_id='%s'" %(reply,res[j]['complaint_id'])
	# 		print(q)
	# 		update(q)
	# 		flash("success")
	# 		return redirect(url_for('admin.admin_view_complaints')) 	
	# 	j=j+1
	# return render_template('admin_view_complaints.html',data=data)

# @admin.route('/admin_send_reply',methods=['get','post'])
# def admin_send_reply():
# 	data={}

# 	if 'id' in request.args:
# 		id=request.args['id']

	

# 	if 'submit' in request.form:
# 		reply=request.form['reply']
# 		q="UPDATE `complaints` SET `reply`='%s' WHERE `complaint_id`='%s'"%(reply,id)
# 		res=update(q)
# 		flash('success')
# 		return redirect(url_for('admin.admin_view_complaints'))

# 	return render_template('admin_send_reply.html',data=data)
# @admin.route('/admin_view_vehicle_location',methods=['get','post'])
# def admin_view_vehicle_location():
# 	data={}
# 	q="SELECT * FROM `vehicles`"
# 	res=select(q)
# 	data['vehicles']=res
# 	return render_template('admin_view_vehicle_location.html',data=data)
