# 🚀 Backend Assignment - Pincode Distance API

## 📌 Overview
This project is a **REST API** that calculates the **distance and duration** between two pin codes using the **Google Maps API**. The API is designed to be **REST-compliant**, supports **caching**, and stores the distance and duration data in a **database**.

---

## 🛠 Features
- ✅ Fetches **route, distance, and duration** between two pin codes.  
- ✅ **Caches responses** to reduce unnecessary API calls to Google Maps.  
- ✅ Saves **distance, duration, and route information** in a database.  
- ✅ Uses **TDD (Test-Driven Development)** with JUnit.  
- ✅ Optimized API calls for better efficiency.  

---

## 🔗 API Integration
The API integrates with **geocoding api and routes api** to calculate travel duration and distance between pin codes.

### 🛠 API Workflow
1. **First Call**: API fetches data from Google Maps and stores it in the cache & database.  
2. **Subsequent Calls**: If the same request is made, the data is retrieved from the cache/database instead of hitting Google Maps API.  

---

## 📄 API Endpoints
### 1. **Fetch Distance & Duration**
```
POST
```
#### 🔹 Request Example:
```sh
##post
http://localhost:8080/api/calculate-distance
## json body 
{
  "originPincode": "141106",
  "destinationPincode": "110060"
}

```
#### 🔹 Response Example:
```json
{
  "from": "141106",
  "to": "110060",
  "distance": "250 km",
  "duration": "4 hours 30 minutes",
  "route": ["Route details..."]
}
```

---

## 🗂 Database Structure
| Table Name | Columns |
|------------|---------|
| **distance_cache** | id, from_pincode, to_pincode, distance, duration, route, created_at |
| **pincode_info** | pincode, latitude, longitude, polygon_info |

---

## 📈 Optimization Strategy
### ✅ Optimized API Calls Example
| Call # | From Pincode | To Pincode | API Hits |
|--------|-------------|------------|----------|
| **1st** | 141106 | 110060 | ✅ Google Maps API |
| **2nd** | 141106 | 560023 | ✅ Google Maps API |
| **3rd** | 141106 | 110060 | ✅ Cached Response |

---

## ⚙️ Tech Stack
- **Java 21**
- **Spring Boot** (REST API)
- **Google Maps API** (Geocoding and Routes api)
- **H2 Database** (Testing) / MySQL (Production)
- **JUnit** (Testing)
- **Postman** (API Testing)

---

## ✅ Setup & Installation
1. **Clone the repository:**
```sh
git clone https://github.com/your-username/Backend-Assignment.git
cd Backend-Assignment
```
2. **Configure Google Maps API Key:**
   - Go to `src/main/resources/application.properties`
   - Add your API key:
```properties
google.maps.api.key=YOUR_API_KEY
```
3. **Run the application:**
```sh
mvn spring-boot:run
```
4. **Test the API using Postman**
   - Use the endpoint: `GET /distance?fromPincode=141106&toPincode=110060`

---

## 🔬 Testing
Run unit tests using:
```sh
mvn test
```

---

## 📌 Notes
- 🔹 **No UI** - The API is meant for backend services and is testable via **Postman**.  
- 🔹 **Follows RESTful principles** for clean and structured endpoints.  
- 🔹 **TDD approach** - Uses JUnit to ensure robust testing.  

---

## ✨ Contributors
👤 **Shekhar**  
📧 Contact: shekharsuman0397@gmail.com

