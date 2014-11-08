import webapp2

class API (webapp2.RequestHandler):
   # implementing the API for the hackathon
   def post(self,method):
	self.response.headers['Content-Type'] = 'application/json'

	if (os.getenv('SERVER_SOFTWARE') and os.getenv('SERVER_SOFTWARE').startswith('Google App Engine/')):
         db = MySQLdb.connect(unix_socket='/cloudsql/<project-name>:<instance-name>', db='<db-name>', user='root')
        else:
         db = MySQLdb.connect(host='localhost', user='root', passwd="htndjango",db="<db-name>")

	cursor = db.cursor()

	if(method == ""):


	elif(method == ""):


	elif(method == ""):


	elif(method == ""):

	

