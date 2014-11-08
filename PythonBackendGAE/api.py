import webapp2
import cgi 
import math
import os
import MySQLdb
import json

class API (webapp2.RequestHandler):
   # implementing the API for the hackathon
   def post(self,method):
	self.response.headers['Content-Type'] = 'application/json'

	if (os.getenv('SERVER_SOFTWARE') and os.getenv('SERVER_SOFTWARE').startswith('Google App Engine/')):
         db = MySQLdb.connect(unix_socket='/cloudsql/hackathought:hackathought', db='hackathought', user='root')
        else:
         db = MySQLdb.connect(host='localhost', user='root', passwd="htndjango",db="hackathought")
	
	#The object that contains the result from Database
	cursor = db.cursor()
	#The object that contains the json return
	JsonReturn = {}

	if(method == "AddUpVote"):
		#Write a method for adding up vote 
		#This acts on the Comment Table, increment the upvote by 1
		comment_id = cgi.escape(self.request.get('comment_id')) 
		cursor.execute('SELECT comment_upvotes FROM comments WHERE comment_id = %i', comment_id)
		#Get current upvotes and increment by 1
		row = cursor.fetchall()		
		upvote = cursor[0]
		upvote = upvote + 1
		#Update their database 
		cursor.execute('UPDATE comments SET comment_upvotes = %i WHERE comment_id = %i',(upvote,comment_id))
		
		if (cursor.rowcount > 0):
		  db.commit()
		  JsonReturn['status'] = 200
		  JsonReturn['message'] = "Update Successfully"

		 else:
		  JsonReturn['status'] = 500
		  JsonReturn['message'] = "Error Holy Shiiiiiii" 

		self.reponse.write(json.dumps(JsonReturn,sort_keys=True, indent=4, separators=(',',': '))


	elif(method == "AddNewLocation"):
		#Write a method for adding a new location
		#This acts on the Location Table 
		location_id = cgi.escape(self.request.get('location_id'))
		location_name = cgi.escape(self.request.get('locaiton_name'))
		location_lat = cgi.escape(self.request.get('location_lat'))
		location_lon = cgi.escape(self.request.get('location_lon'))
		location_desc = cgi.escape(self.request.get('location_desc'))
		location_category = cgi.escape(self.request.get('location_category'))
		location_address = cgi.escape(self.request.get('location_address'))
		location_city = cgi.escape(self.request.get('location_city'))

		cursor.execute('INSERT INTO locations VALUES(%d, %s, %f, %f, %s, %s, %s, %s)',(location_id, location_name, location_lat, location_lon, location_desc, location_category,location_address, location_city))
		db.commit() 

		#Check if that pass through

	elif(method == "GetCategoryLocation"):
		#Write a method for retrieving the location in a specific category 
		#This method will cal the calculate_location(), add a dictionary for [distance] -> [location ID]
		#Put distances into an array and sort the distance array. Use the distance to map back to location ID 
		category = cgi.escape(self.request.get('location_category'))
		# Select All and return that Json Object
		query = 'SELECT * FROM locations WHERE location_category like' + '"%' + category + '%"'
		cursor.execute(query) 

		#This is the list that contains all the location, each element is an dictionary that maps  
		location_list = []

		for record in cursor:
			#populate the dictionary that stores data of each location
			location_element = {}
			location_element['location_id'] = record[0]
			location_element['location_name'] = record[1]
			location_element['location_lat'] = record[2]
			location_element['location_lon'] = record[3]
			location_element['location_des'] = record[4]
			location_element['location_category'] = record[5]
			location_element['location_address'] = record[6]
			location_element['location_city'] = record[7]
			#add this location to the location list


			location_list.append(location_element)

		JsonReturn['data'] = location_list
		JsonReturn['status'] = 200 
		self.response.write(json.dumps(JsonReturn, sort_keys=True, indent=4, separators=(',',': ')))

		#Check if it pass through 

	elif(method == "AddComment"):
		#Write a method for adding a comment to a location 
		#This acts on the Comment Table
		comment_text = cgi.escape(self.request.get('comment_text'))
		comment_location_id = cgi.escape(self.request.get('comment_location_id'))
		#Comment_upvotes is 0 by default 	

		cursor.execute('INSERT INTO comments VALUES(NULL,%s,%s,%s)',(comment_location_id,comment_text,0))
		db.commit()

		#Check that if it pass through 
		if (cursor.rowcount > 0):
			JsonReturn['status'] = 200
			JsonReturn['message'] = "Sucessfully Insertted"
		else:
			JsonReturn['status'] = 500
			JsonReturn['message'] = "Insert Unsuccessfully"

		self.response.write(json.dump(JsonReturn, sort_keys=True, indent=4, separators=(',',': ')))



	elif(method == "GetComment"):
		#Write a method that retrieve the comments of a specific location 
		# Get the location ID
		comment_location_id = cgi.escape(self.request.get('comment_location_id'))
		# Run the query
		cursor.execute('SELECT * FROM comments WHERE comment_location_id = %s',(comment_location_id))
		#Comment_list that gets input into the JsonReturn
		comment_list = []
		for record in cursor:
			comment_element = {}
			comment_element['comment_text'] = record[2]
			comment_element['comment_upvotes'] = record[3]
			comment_list.append(comment_element)

		#Create json object
		JsonReturn['data'] = comment_list
		JsonReturn['status'] = 200 
		self.response.write(json.dumps(JsonReturn, sort_keys=True, indent=4, separators=(',',': ')))


   def calculate_location(lg1,la1,lg2,la2):
	#Implement a method for calculate the sum of difference between Latitude and Longitude 

	sum = math.abs(lg1-lg2) + math.abs(la1-la2)
	return sum  

