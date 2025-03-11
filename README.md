# Backend-Assignment---Pincode-Distance

Need a REST API ( REST Compliant ) - NO UI
Input
From Pincode
To Pincode
Returns
Distance and Duration along with each route
---------------------------------------------------------------------------------------
Integration with Google Maps API
For the route along with Distance and time Duration
1. Wrapper API to fetch duration and route between two pin codes
2. Cache the response so next time we call the API it should not hit the Google Maps API
   
Save Distance and duration Information in DB along with Routes

Extra
If you complete the above, save all Pincode information as well Save Pincode Lat, Long and polygon information in DB

Few things:
1. REST Compliant APIs, NO UI
2. Testable by Postman
3. Will prefer TDD
4. Code should be structured with JUnit

   
--- Optimize API Calls
First Call
From Pincode - 141106
To Pincode: 110060

2nd Call
From Pincode - 141106
To Pincode: 560023

3rd Call
From Pincode - 141106
To Pincode: 110060
