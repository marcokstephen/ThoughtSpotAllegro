#!/usr/bin/env python
#

import webapp2
from index_page import MainPage 
from api import API

app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/api/(.+)',API)
], debug=True)
