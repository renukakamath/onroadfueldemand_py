from flask import *
from database import *

import demjson
import uuid


api=Blueprint('api',__name__)

@api.route('/login',methods=['get','post'])
def login():
	data={}
	
	username = request.args['username']
	password = request.args['password']
	q="SELECT * from login where username='%s' and password='%s'" % (username,password)
	res = select(q)
	if res :
		data['status']  = 'success'
		data['data'] = res
		data['method']='login'
	else:
		data['status']	= 'failed'
		data['method']='login'
	return  demjson.encode(data)

@api.route('/userregister',methods=['get','post'])
def userregister():

	data = {}

	fname=request.args['fname']
	lname=request.args['lname']
	phone=request.args['phone']
	licence=request.args['licence']
	email=request.args['email']
	uname=request.args['uname']
	passw=request.args['pass']
	lati=request.args['lati']
	longi=request.args['longi']

	q1="SELECT * FROM login WHERE username='%s'" %(uname)
	print(q1)
	r=select(q1)
	print(r)
	if r:
		data['status']='duplicate'
		data['method']='userregister'
	else:
		q= "INSERT INTO `login` VALUES(NULL,'%s','%s','user')"%(uname,passw)
		lid = insert(q)
		qr="INSERT INTO `users` VALUES(NULL,'%s','%s','%s','%s','%s','%s','%s','%s')"%(lid,fname,lname,phone,email,licence,lati,longi)
		id=insert(qr)
		if id>0:
			data['status'] = 'success'
		else:
			data['status'] = 'failed'
		data['method']='userregister'
		return demjson.encode(data)



@api.route('/updatepasslocation',methods=['get','post'])
def updatepasslocation():
	data={}

	latti=request.args['latti']
	longi=request.args['longi']
	logid=request.args['log_id']
	
	q="update `users` set `latitude`='%s',`longitude`='%s' where `login_id`='%s'"%(latti,longi,logid)
	id=update(q)
	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'updatepasslocation'
	return demjson.encode(data)


@api.route('/user_view_nearest_vehicle',methods=['get','post'])
def user_view_nearest_vehicle():
	data={}
	data['method']='user_view_nearest_vehicle'
	lati=request.args['lati']
	logi=request.args['logi']
	# q = "SELECT *,(3959 * ACOS ( COS ( RADIANS('%s') ) * COS( RADIANS( latitude) ) * COS( RADIANS( longitude ) - RADIANS('%s') ) + SIN ( RADIANS('%s') ) * SIN( RADIANS(latitude ) ))) AS user_distance  FROM colleges inner join ranking using(college_id)  HAVING user_distance<31.068 ORDER BY rank desc" % (lati,logi,lati)
	q="SELECT *,(3959 * ACOS ( COS ( RADIANS('%s') ) * COS( RADIANS( latitude) ) * COS( RADIANS( longitude ) - RADIANS('%s') ) + SIN ( RADIANS('%s') ) * SIN( RADIANS(latitude ) ))) AS user_distance,CONCAT(`drivers`.`first_name`,' ',`drivers`.`last_name`) AS dname  FROM `vehicles` INNER JOIN `assign_vehicle` USING(`vehicle_id`) INNER JOIN `drivers` USING(`driver_id`) INNER JOIN `fuel_type` USING(`type_id`)  HAVING user_distance<31.068 " % (lati,logi,lati)
	print(q)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']='failed'
	return demjson.encode(data)



@api.route('/user_send_fuel_request',methods=['get','post'])
def user_send_fuel_request():
	data={}

	type_id=request.args['type_id']
	driver=request.args['driver']
	login_id=request.args['login_id']
	no_titter=request.args['no_titter']
	tot_rate=request.args['tot_rate']
	
	q="INSERT INTO `bookings` VALUES(NULL,(SELECT `user_id` FROM `users` WHERE `login_id`='%s'),'%s','%s','%s','%s',NOW(),'Pending')"%(login_id,driver,type_id,no_titter,tot_rate)
	id=insert(q)
	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'user_send_fuel_request'
	return demjson.encode(data)



@api.route('/user_view_request',methods=['get','post'])
def user_view_request():
	data={}
	data['method']='user_view_request'
	login_id=request.args['login_id']
	q="SELECT *,CONCAT(`drivers`.`first_name`,' ',`drivers`.`last_name`) AS driver,`bookings`.`date_time` AS date_time FROM `bookings` INNER JOIN `drivers` ON `drivers`.`driver_id`=`bookings`.`driver_id` INNER JOIN `fuel_type` ON `fuel_type`.`type_id`=`bookings`.`type_id` INNER JOIN `vehicles` ON `vehicles`.`type_id`=`fuel_type`.`type_id` WHERE `bookings`.`user_id`=(SELECT `user_id` FROM `users` WHERE `login_id`='%s')"%(login_id)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']='failed'
	return demjson.encode(data)


@api.route('/user_payment',methods=['get','post'])
def user_payment():
	data={}

	booking_id=request.args['booking_id']
	q1="UPDATE `bookings` SET `status`='Paid' WHERE `booking_id`='%s'"%(booking_id)
	update(q1)
	q="INSERT INTO `payments` VALUES(NULL,'%s',NOW())"%(booking_id)
	id=insert(q)
	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'user_payment'
	return demjson.encode(data)



@api.route('/user_rate_fuel',methods=['get','post'])
def user_rate_fuel():

	data={}

	booking_id=request.args['booking_id']
	ratings=request.args['rating']
	review=request.args['review']

	q="SELECT * FROM `rating` WHERE `booking_id`='%s'"%(booking_id)
	res=select(q)
	if res:

		q="UPDATE `rating` SET `rating`='%s',`review`='%s',`date_time`=CURDATE() WHERE `booking_id`='%s'"%(ratings,review,booking_id)
		update(q)
		data['method'] = 'user_rate_fuel'
	else:
		q="INSERT INTO `rating` VALUES(NULL,'%s','%s','%s',CURDATE())"%(booking_id,ratings,review)
		id=insert(q)
		if id>0:
			data['status'] = 'success'
			
		else:
			data['status'] = 'failed'
		data['method'] = 'user_rate_fuel'
	return demjson.encode(data)



@api.route('/user_view_rated',methods=['get','post'])
def user_view_rated():
	data = {}

	booking_id=request.args['booking_id']
	
	q=" SELECT * FROM `rating` WHERE `booking_id`='%s'"%(booking_id)
	print(q)
	result=select(q)
	if result:
		data['status'] = 'success'
		data['data'] = result[0]['rating']
		data['data1'] = result[0]['review']
		
	else:
		data['status'] = 'failed'
	data['method'] = 'user_view_rated'
	return demjson.encode(data)



@api.route('/user_send_complaints',methods=['get','post'])
def user_send_complaints():

	data = {}

	loginid=request.args['loginid']
	complaints=request.args['complaints']


	qr="INSERT INTO `complaints` VALUES(NULL,(SELECT `user_id` FROM `users` WHERE `login_id`='%s'),'%s','pending',NOW())"%(loginid,complaints)
	print(qr)
	id=insert(qr)
	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method']='user_send_complaints'
	return demjson.encode(data)

@api.route('/user_view_complaints',methods=['get','post'])
def user_view_complaints():
	data={}

	loginid=request.args['loginid']
	
	q="SELECT * FROM `complaints` WHERE `user_id`=(SELECT `user_id` FROM `users` WHERE `login_id`='%s')"%(loginid)
	res = select(q)
	if res :
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	data['method']='user_view_complaints'
	return  demjson.encode(data)



###########################################################################################
############################ DRIVER #######################################################
###########################################################################################




@api.route('/driver_view_assigned_vehicle',methods=['get','post'])
def driver_view_assigned_vehicle():
	data = {}
	login_id=request.args['login_id']

	
	q="SELECT * FROM `assign_vehicle` INNER JOIN `vehicles` USING(`vehicle_id`) INNER JOIN `stock` USING(`vehicle_id`) WHERE `driver_id`=(SELECT `driver_id` FROM `drivers` WHERE `login_id`='%s')"%(login_id)
	result=select(q)
	if result:
		data['status'] = 'success'
		data['data'] = result
		
	else:
		data['status'] = 'failed'
	data['method'] = 'driver_view_assigned_vehicle'
	return demjson.encode(data)



@api.route('/driver_view_stock',methods=['get','post'])
def driver_view_stock():
	data = {}
	vehicle_id=request.args['vehicle_id']

	q="SELECT * FROM `stock` WHERE `vehicle_id`='%s'"%(vehicle_id)
	# q="SELECT * FROM `drivers` INNER JOIN `assign_vehicle` USING(`driver_id`) INNER JOIN `stock` USING(`vehicle_id`) WHERE `driver_id`=(SELECT `driver_id` FROM `drivers` WHERE `login_id`='%s')"%(login_id)
	result=select(q)
	if result:
		data['status'] = 'success'
		data['stock_id'] = result[0]['stock_id']
		data['stock'] = result[0]['stock']
		
	else:
		data['status'] = 'failed'
	data['method'] = 'driver_view_stock'
	return demjson.encode(data)



@api.route('/driver_update_stock',methods=['get','post'])
def driver_update_stock():
	data={}

	stock=request.args['stock']
	vehicle_id=request.args['vehicle_id']
	q1="UPDATE `stock` SET `stock`='%s' WHERE `vehicle_id`='%s'"%(stock,vehicle_id)
	id=update(q1)

	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'driver_update_stock'
	return demjson.encode(data)


@api.route('/driver_view_request',methods=['get','post'])
def driver_view_request():
	data = {}
	login_id=request.args['login_id']
 
	q="SELECT *,CONCAT(`users`.`first_name`,' ',`users`.`last_name`) AS username,`bookings`.`date_time` AS date_time FROM `bookings` INNER JOIN `users` USING(`user_id`) INNER JOIN `fuel_type` USING(`type_id`) INNER JOIN `assign_vehicle` USING(`driver_id`) WHERE `driver_id`=(SELECT `driver_id` FROM `drivers` WHERE `login_id`='%s')"%(login_id)
	result=select(q)
	if result:
		data['status'] = 'success'
		data['data'] = result
		
	else:
		data['status'] = 'failed'
	data['method'] = 'driver_view_request'
	return demjson.encode(data)



@api.route('/driver_accept_request',methods=['get','post'])
def driver_accept_request():
	data={}

	booking_ids=request.args['booking_ids']
 
	q1=" UPDATE `bookings` SET `status`='Accept' WHERE `booking_id`='%s'"%(booking_ids) 
	id=update(q1)

	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'driver_accept_request'
	return demjson.encode(data)



@api.route('/driver_accept_payment',methods=['get','post'])
def driver_accept_payment():
	data={}

	booking_ids=request.args['booking_ids']
	vehicle_ids=request.args['vehicle_ids']
	nooflitters=request.args['nooflitters']
	
	q2="UPDATE `stock` SET `stock`=`stock`-'%s' WHERE `vehicle_id`='%s'"%(nooflitters,vehicle_ids)
	update(q2)
	q1="UPDATE `bookings` SET `status`='Payment Received' WHERE `booking_id`='%s'"%(booking_ids) 
	id=update(q1)

	if id>0:
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'driver_accept_payment'
	return demjson.encode(data)




@api.route('/driver_view_ratings',methods=['get','post'])
def driver_view_ratings():
	data = {}

	booking_id=request.args['booking_id']
	
	q=" SELECT * FROM `rating` WHERE `booking_id`='%s'"%(booking_id)
	print(q)
	result=select(q)
	if result:
		data['status'] = 'success'
		data['data'] = result[0]['rating']
		data['data1'] = result[0]['review']
		
	else:
		data['status'] = 'failed'
	data['method'] = 'driver_view_ratings'
	return demjson.encode(data)
