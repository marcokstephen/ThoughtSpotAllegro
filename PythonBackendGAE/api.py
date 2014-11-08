import webapp2
import cgi 
import math

class API (webapp2.RequestHandler):
   # implementing the API for the hackathon
   def post(self,method):
	self.response.headers['Content-Type'] = 'application/json'

	if (os.getenv('SERVER_SOFTWARE') and os.getenv('SERVER_SOFTWARE').startswith('Google App Engine/')):
         db = MySQLdb.connect(unix_socket='/cloudsql/<project-name>:<instance-name>', db='<db-name>', user='root')
        else:
         db = MySQLdb.connect(host='localhost', user='root', passwd="htndjango",db="<db-name>")

	cursor = db.cursor()

	if(method == "AddUpVote"):
	#Write a method for adding up vote 
	#This acts on the Comment Table 
	elif(method == "AddNewLocation"):
	#Write a method for adding a new location
	#This acts on the Location Table 

	elif(method == "GetCategoryLocation"):
	#Write a method for retrieving the location in a specific category 
	#This method will cal the calculate_location(), add a dictionary for [distance] -> [location ID]
	#Put distances into an array and sort the distance array. Use the distance to map back to location ID 

	elif(method == "AddComment"):
	#Write a method for adding a comment to a location 
	#This acts on the Comment Table


   def calculate_location(lg1,la1,lg2,la2):
	#Implement a method for calculate the sum of difference between Latitude and Longitude 

	sum = math.abs(lg1-lg2) + math.abs(la1-la2)
	return sum  

