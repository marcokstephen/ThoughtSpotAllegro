#!/usr/bin/env python
#

import webapp2
from index_page import MainPage 

app = webapp2.WSGIApplication([
    ('/', MainPage)
], debug=True)
