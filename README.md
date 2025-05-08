# 🏠 RentEase | Rental Management - Backend

A Spring Boot-based backend for managing products, users, and warranties. Features include:

## 🔧 Tech Stack

- **Language**: Java 23
- **Framework**: Spring Boot
- **Security**: Spring Security, JWT (Access + Refresh Tokens)
- **Database**: MySQL
- **ORM**: Spring Data JPA, Hibernate
- **Caching**: Redis (for token/session management)
- **Mailing**: Java Mail Sender 
- **Validation**: Hibernate Validator (JSR-380)
- **Mapper**: MapStruct
- **Build Tool**: Gradle

## 🚀 Features

- ®️ User Registration, triggers Email OTP to activate account.
- 🔐 Login & JWT Authentication
- 🔁 Access and Refresh Token Mechanism
- 🧂 Password Hashing with BCrypt
- 🧠 Tracks Last Login for Credential and Account Expiry
- 🍪 Cookie-based token storage
- 🗂️ CRUD for Property and Users
- 🔍 Search by Locality / sublocality, Filter property through multiple fields (JPA Criteria API)
- ⏰ Seller / buyer's details gets exchanged on buyer's interets through Email
- ✅ Custom Validations (e.g., Age Validation, Floor checker, Area validator)
- 🩷 Like / Unlike properties. Get count of likes for each property.
- 📃 Paginated API responses
  
---
## 🔗 Postman Collection


---

# 📃 API DOCUMENTATION
## 🔐 Authentication Endpoints 
   
### 1. **LOGIN** 

**POST** `/auth/login` 

Authenticate a user and return access and refresh tokens as cookies. 
Include the following properties as *body*:

- `username` - String - Required  
- `password` - String - Required  

#### Request Body
```json
{
    "username": "john_doe123",
    "password": "Abc@1234"
}
```
#### Response(200 OK)
*Tokens generated and returned as cookies.*
 - `accessToken` - Short lived token
 - `refreshToken` - Long lived token
```json
{
    "status": "Success",
    "message": "Tokens generated",
    "payload": {
        "sub": <Token subject>,
        "iat": <Issued at time>,
        "exp": <Expiry time>
    }
}
```

**Tokens at Cookies after login**
![Tokens at Cookies](./screenshots/Tokens%20at%20Cookies%20after%20login.png)

#### Response(401 UNAUTHORIZED)
*Authentication failed due to invalid username or password.*
```json
{
    "code": 401,
    "status": "Unauthorized",
    "message": "Bad Credentials. Authentication failed.",
    "Validation Error": "Invalid username or password"
}
```

#### Response(403 FORBIDDEN)
*The account is locked by default. Email verification is required to activate the account.*
```
{
    "code": 403,
    "status": "Forbidden",
    "message": "Account is locked.",
    "Recovery": "Verify Email OTP to unlock account."
}
```

### 2. **REGISTER** 

**POST**	`/auth/signup`

Register a new user. 
Include the following properties as *body*:

- `firstName` - String - Required  
- `lastName` - String - Optional  
- `dateOfBirth` - LocalDate - Required  
- `gender` - String - Required  
- `username` - String - Required  
- `password` - String - Required  
- `confirmPassword` - String - Required  
- `email` - String - Required  
- `phoneNumber` - String - Required  

#### Request Body JSON
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1995-08-15",
    "gender": "Male",
    "username": "john_doe123",
    "password": "Abc@1234",
    "confirmPassword": "Abc@1234",
    "email": "john.doe@example.com",
    "phoneNumber": "+911234567890"
}
```

#### Response(200 OK)
*User registration completed successfully.*
```json
{
    "code": 201,
    "status": "Created",
    "message": "User registered successfully. Please verify your email.",
    "Details": "John"
}
```

#### Response(409 CONFLICT)
*Occurs when the Username, Email ID, or Phone Number already exists in the system.*
```json
{
    "code": 409,
    "status": "Conflict",
    "message": "Username already in use.",
    "Recovery": "Retry with different username."
}

{
    "code": 409,
    "status": "Conflict",
    "message": "Email ID already in use.",
    "Recovery": "Try login with existing account."
}

{
    "code": 409,
    "status": "Conflict",
    "message": "Phone Number already in use.",
    "Recovery": "Try login with existing account."
}
```

#### Response(400 BAD REQUEST)
*Registartion failed due to password - confirm password mismatch.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Password - Confirm Password Mismatch",
    "Recovery": "Password and Confirm Password should be same."
}
```

### 3. **UPDATE** 

**POST**	`/auth/update`

Update user profile details. Authentication required.
Include the following properties as *body*:

- `firstName` - String - Required  
- `lastName` - String - Optional  
- `gender` - String - Required  
- `dateOfBirth` - LocalDate - Required  

#### Request Body JSON
```json
{
    "firstName": "Johnny",
    "lastName": "Doe",
    "gender": "Male",
    "dateOfBirth": "1995-09-15"
}
```

#### Response(200 OK)
*User details updated successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "User Profile updated successfully",
    "Details": "Johnny"
}
```

### 4. **CHANGE PASSWORD** 

**POST**	`/auth/changePassword`

Change user password. Authentication required.
Include the following properties as *body*:

- `oldPassword` - String - Required  
- `password` - String - Required  
- `confirmPassword` - String - Required  

#### Request Body JSON
```json
{
    "oldPassword": "Abc@1234",
    "password": "New@Pass2",
    "confirmPassword": "New@Pass2"
}

```
#### Response(200 OK)
*User password updated successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Change Password request processed successfully.",
    "Details": "Johnny"
}
```

#### Response(403 FORBIDDEN)
*The provided old password is incorrect.*
```json
{
    "code": 403,
    "status": "Forbidden",
    "message": "Old Password - Incorrect",
    "Recovery": "Enter correct Old Password"
}
```

#### Response(400 BAD REQUEST)
*Updation failed due to password - confirm password mismatch.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Password - Confirm Password Mismatch",
    "Recovery": "Password and Confirm Password should be same."
}
```

### 5. **PROFILE** 

**GET**	`/auth/profile`

Returns the logged-in user's profile. Authentication required. 

#### Response(200 OK)
*User profile retrieved successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "User profile fetch successful.",
    "Details": {
      "firstName": "John",
      "lastName": "Doe",
      "dateOfBirth": "1995-08-15",
      "gender": "Male",
      "username": "john_doe123",
      "email": "john.doe@example.com",
      "isEmailVerified": true,
      "phoneNumber": "+911234567890",
      "isPhoneNumberVerified": true
}
```

### 6. **DELETE** 

**DELETE**	`/auth/delete`

Deletes the logged-in user's account. Authentication required.

#### Response(200 OK)
*User profile deleted successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Profile Deleted SuccessFully.",
    "Home": "/app/properties"
}
```

### 7. **GENERATE NEW TOKEN** 

**POST**	`/auth/refreshToken`	

Refresh JWT token and return access/refresh tokens. 
Include the following properties as *body*:

- `refreshToken` - String - Required  

#### Request Body JSON
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
#### Response(200 OK)
*New access and refresh tokens generated successfully and returned as cookies upon valid refresh token submission.*
```json
{
    "status": "Success",
    "message": "Tokens generated",
    "payload": {
        "sub": "<Token Subject>",
        "iat": <Issued at time>,
        "exp": <Expiry time>
    }
}
```

### 8. **TRIGGER OTP** 

**POST**	`/auth/sendOTP`

Send OTP to email for verification.
Include the following properties as *body*:

- `email` - String - Required

#### Request Body JSON
```json
{
  "email": "john.doe@example.com"
}
```
#### Response(200 OK)
*User email already verified.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Email Id already verified.",
    "Action": "Continue looking for properties."
}
```
#### Response(200 OK)
*Email OTP sent successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "OTP sent to email successfully.",
    "Action": "Verify the OTP"
}
```

### 9. **VERIFY OTP** 

**POST**	`/auth/verifyEmailOTP`

Verify OTP received on email. 
Include the following properties as *body*:

- `email` - String - Required  
- `otp` - String - Required  

#### Request Body JSON
```json
{
  "email": "john.doe@example.com",
  "otp": "123456"
}
```
#### Response(200 OK)
*Email OTP verified successfully, and the account has been activated.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Email verified successfully.",
    "Details": "<firstName>"
}
```
#### Response(400 BAD REQUEST)
*Invalid or Expired OTP entered.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Invalid or Expired OTP",
    "Recovery": "Try hitting /auth/sendOTP"
}
```

## Common Validation Error Response

```json
{
    "code": <HttpStatus Code>,
    "status": <HttpStatus Status>,
    "message": "Validation check failed.",
    "Validation Errors": [
        <Error Messages>
    ]
}
```

**User Registration Validation**
![Validations at User Registration](./screenshots/User%20Validation%20Errors.png)

**Custom Age Validation**
![Age Validation](./screenshots/Validation%20of%20data.png)

**Other Logical Validation**
![Logical Validation](./screenshots/Custom%20Logical%20Validations.png)

---

## 🔐 Property Management Endpoints 
   
### 1. **ADD PROPERTY** 

**POST** `/app/add`

Registers a new property with validated details provided. Authentication required.
Include the following properties as *body*:

- `title` - String - Required  
- `description` - String - Required  
- `address` - Object - Required  
  - `fullAddress` - String - Required  
  - `subLocality` - String - Required  
  - `locality` - String - Required  
  - `city` - String - Required  
  - `state` - String - Required  
  - `pinCode` - Integer - Required  
  - `latitude` - Double - Required  
  - `longitude` - Double - Required  
- `price` - Object - Required  
  - `expectedPrice` - Double - Required  
  - `isPriceNegotiable` - Boolean - Required  
  - `securityDeposit` - Double - Required  
  - `callForPrice` - Boolean - Required  
  - `bookingAmount` - Double - Optional  
  - `maintenanceFee` - Double - Optional  
  - `maintenanceFeeCycle` - Integer - Optional  
- `bedrooms` - Integer - Required  
- `bathrooms` - Integer - Required  
- `isAttachedBathroom` - Boolean - Required  
- `balconies` - Integer - Required  
- `isAttachedBalcony` - Boolean - Required  
- `propertyFloor` - Integer - Required  
- `totalFloor` - Integer - Required  
- `facing` - String - Required  
- `isMainRoadFacing` - Boolean - Required  
- `buildUpArea` - Integer - Required  
- `carpetArea` - Integer - Required  
- `propertyAge` - Integer - Required  
- `availableFrom` - String (yyyy-MM-dd) - Required  
- `noticePeriodInMonths` - Integer - Required  
- `gatedSecurity` - Boolean - Required  
- `gym` - Boolean - Required  
- `onlyVeg` - Boolean - Required  
- `amenities` - Set<Integer> - Optional (Refer to amenity ID-name map below)  
- `preferredTenants` - Set<PreferredTenants> - Required  
  - PreferredTenants values: `ANYONE`, `FAMILY`, `BACHELOR_MALE`, `BACHELOR_FEMALE`, `COMPANY` 
- `propertyCategory` - PropertyCategory  - Required  
  - PropertyCategory values: `COMMERCIAL`, `RESIDENTIAL`  
- `waterSupply` - WaterSupply  - Required  
  - WaterSupply values: `CORPORATION`, `BOREWELL`, `BOTH`  
- `listingCategory` - ListingCategory - Required  
  - ListingCategory values: `RENT_OR_LEASE`, `SALE`, `PG_OR_HOSTEL`  
- `propertyType` - PropertyType - Required  
  - PropertyType values: `FLAT_OR_APARTMENT`, `RESIDENTIAL_HOUSE`, `VILLA`, `STUDIO_APARTMENT`, `OFFICE_SPACE`, `SHOP`, `SHOWROOM`, `WAREHOUSE_OR_GODOWN`, `INDUSTRY`, `AGRICULTURAL`  
- `furnishedStatus` - FurnishedStatus - Required  
  - FurnishedStatus values: `FURNISHED`, `UNFURNISHED`, `SEMI_FURNISHED`  
- `possessionStatus` - PossessionStatus - Required  
  - PossessionStatus values: `UNDER_CONSTRUCTION`, `READY_TO_MOVE`  
- `parking` - Parking - Required  
  - Parking values: `CAR`, `BIKE`, `BOTH`, `NONE`

#### Request Body
```json
{
  "title": "Spacious 2BHK near Palace",
  "description": "A beautiful and spacious 2BHK apartment with all modern amenities, close to metro and schools.",
  "address": {
    "fullAddress": "No. 24, Sunrise Residency, MG Road, Near Metro Station",
    "subLocality": "Indira Nagar",
    "locality": "MG Road",
    "city": "Bangalore",
    "state": "Karnataka",
    "pinCode": 560001,
    "latitude": 12.9716,
    "longitude": 77.5946
  },
  "price": {
    "expectedPrice": 25000.0,
    "isPriceNegotiable": true,
    "securityDeposit": 50000.0,
    "callForPrice": false,
    "bookingAmount": 10000.0,
    "maintenanceFee": 1500.0,
    "maintenanceFeeCycle": 1
  },
  "bedrooms": 2,
  "bathrooms": 2,
  "isAttachedBathroom": true,
  "balconies": 1,
  "isAttachedBalcony": true,
  "propertyFloor": 3,
  "totalFloor": 5,
  "facing": "East",
  "isMainRoadFacing": false,
  "buildUpArea": 1200,
  "carpetArea": 1000,
  "propertyAge": 5,
  "availableFrom": "2025-06-01",
  "noticePeriodInMonths": 2,
  "gatedSecurity": true,
  "gym": true,
  "onlyVeg": false,
  "amenities": [1, 2, 3],
  "preferredTenants": ["FAMILY", "BACHELOR_FEMALE"],
  "propertyCategory": "RESIDENTIAL",
  "waterSupply": "BOTH",
  "listingCategory": "RENT_OR_LEASE",
  "propertyType": "FLAT_OR_APARTMENT",
  "furnishedStatus": "SEMI_FURNISHED",
  "possessionStatus": "READY_TO_MOVE",
  "parking": "BOTH"
}

```
#### Response(200 OK)
*Property created successfully.*
```json
{
    "code": 201,
    "status": "Created",
    "message": "Property created successfully.",
    "Details": "Spacious 2BHK near Palace"
}
```

#### Response(400 BAD REQUEST)
*Invalid Amenity ID given.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Amenity not found.",
    "Recovery": "Try providing the correct amenity Id."
}
```

#### Response(400 BAD REQUEST)
*Duplicate property cannot be created.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Duplicate Entry found.",
    "Recovery": "Try adding different entry. Not the same one 🙃."
}
```

### 2. **EDIT PROPERTY** 

**POST** `/app/edit`

Updates an existing and own property's information. Authentication required.
Include the following properties as *body*:

- `id` - Integer - Required  
- `title` - String - Required  
- `description` - String - Required  
- `address` - Object - Required  
  - `fullAddress` - String - Required  
  - `subLocality` - String - Required  
  - `locality` - String - Required  
  - `city` - String - Required  
  - `state` - String - Required  
  - `pinCode` - Integer - Required  
  - `latitude` - Double - Required  
  - `longitude` - Double - Required  
- `price` - Object - Required  
  - `expectedPrice` - Double - Required  
  - `isPriceNegotiable` - Boolean - Required  
  - `securityDeposit` - Double - Required  
  - `callForPrice` - Boolean - Required  
  - `bookingAmount` - Double - Optional  
  - `maintenanceFee` - Double - Optional  
  - `maintenanceFeeCycle` - Integer - Optional  
- `bedrooms` - Integer - Required  
- `bathrooms` - Integer - Required  
- `isAttachedBathroom` - Boolean - Required  
- `balconies` - Integer - Required  
- `isAttachedBalcony` - Boolean - Required  
- `propertyFloor` - Integer - Required  
- `totalFloor` - Integer - Required  
- `facing` - String - Required  
- `isMainRoadFacing` - Boolean - Required  
- `buildUpArea` - Integer - Required  
- `carpetArea` - Integer - Required  
- `propertyAge` - Integer - Required  
- `availableFrom` - String (yyyy-MM-dd) - Required  
- `noticePeriodInMonths` - Integer - Required  
- `gatedSecurity` - Boolean - Required  
- `gym` - Boolean - Required  
- `onlyVeg` - Boolean - Required  
- `amenities` - Set<Integer> - Optional (Refer to amenity ID-name map below)  
- `preferredTenants` - Set<PreferredTenants> - Required  
  - PreferredTenants values: `ANYONE`, `FAMILY`, `BACHELOR_MALE`, `BACHELOR_FEMALE`, `COMPANY` 
- `propertyCategory` - PropertyCategory  - Required  
  - PropertyCategory values: `COMMERCIAL`, `RESIDENTIAL`  
- `waterSupply` - WaterSupply  - Required  
  - WaterSupply values: `CORPORATION`, `BOREWELL`, `BOTH`  
- `listingCategory` - ListingCategory - Required  
  - ListingCategory values: `RENT_OR_LEASE`, `SALE`, `PG_OR_HOSTEL`  
- `propertyType` - PropertyType - Required  
  - PropertyType values: `FLAT_OR_APARTMENT`, `RESIDENTIAL_HOUSE`, `VILLA`, `STUDIO_APARTMENT`, `OFFICE_SPACE`, `SHOP`, `SHOWROOM`, `WAREHOUSE_OR_GODOWN`, `INDUSTRY`, `AGRICULTURAL`  
- `furnishedStatus` - FurnishedStatus - Required  
  - FurnishedStatus values: `FURNISHED`, `UNFURNISHED`, `SEMI_FURNISHED`  
- `possessionStatus` - PossessionStatus - Required  
  - PossessionStatus values: `UNDER_CONSTRUCTION`, `READY_TO_MOVE`  
- `parking` - Parking - Required  
  - Parking values: `CAR`, `BIKE`, `BOTH`, `NONE`

#### Request Body
```json
{
  "id": 100,
  "title": "Spacious 2BHK near Mall",
  "description": "A beautiful and spacious 2BHK apartment with all modern amenities, close to metro and schools.",
  "address": {
    "fullAddress": "No. 24, Sunrise Residency, MG Road, Near Metro Station",
    "subLocality": "Indira Nagar",
    "locality": "MG Road",
    "city": "Bangalore",
    "state": "Karnataka",
    "pinCode": 560001,
    "latitude": 12.9716,
    "longitude": 77.5946
  },
  "price": {
    "expectedPrice": 50000.0,
    "isPriceNegotiable": true,
    "securityDeposit": 100000.0,
    "callForPrice": false,
    "bookingAmount": 15000.0,
    "maintenanceFee": 3000.0,
    "maintenanceFeeCycle": 2
  },
  "bedrooms": 2,
  "bathrooms": 2,
  "isAttachedBathroom": true,
  "balconies": 1,
  "isAttachedBalcony": true,
  "propertyFloor": 3,
  "totalFloor": 6,
  "facing": "East",
  "isMainRoadFacing": false,
  "buildUpArea": 1200,
  "carpetArea": 1000,
  "propertyAge": 5,
  "availableFrom": "2025-06-01",
  "noticePeriodInMonths": 2,
  "gatedSecurity": true,
  "gym": true,
  "onlyVeg": false,
  "amenities": [1, 2, 3],
  "preferredTenants": ["FAMILY", "BACHELOR_FEMALE"],
  "propertyCategory": "RESIDENTIAL",
  "waterSupply": "BOTH",
  "listingCategory": "RENT_OR_LEASE",
  "propertyType": "FLAT_OR_APARTMENT",
  "furnishedStatus": "SEMI_FURNISHED",
  "possessionStatus": "READY_TO_MOVE",
  "parking": "BOTH"
}

```

#### Response(200 OK)
*Property updated successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Property updated successfully.",
    "Details": "Spacious 2BHK near Mall"
}
```

#### Response(400 BAD REQUEST)
*Invalid Amenity ID given.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Amenity not found.",
    "Recovery": "Try providing the correct amenity Id."
}
```

#### Response(400 BAD REQUEST)
*Try editing others or non-existing property.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Property with that Id doesn't exists.",
    "Recovery": "Confirm the property Id."
}
```

### 3. **VIEW PROPERTY DETAILS** 

**POST** `/app/view`

Retrieves detailed information about a specific property using its ID. Authentication required.
Include the following properties as *body*:

- `id` - Integer - Required  

#### Request Body
```json
{
  "id": 100
}
```

#### Response(200 OK)
*Property details fetched successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Property fetched successfully.",
    "Details": {
        "id": 100,
        "title": "Spacious 2BHK near Mall",
        "description": "A beautiful and spacious 2BHK apartment with all modern amenities, close to metro and schools.",
        "address": {
            "fullAddress": "No. 24, Sunrise Residency, MG Road, Near Metro Station",
            "subLocality": "Indira Nagar",
            "locality": "MG Road",
            "city": "Bangalore",
            "state": "Karnataka",
            "pinCode": 560001,
            "latitude": 12.9716,
            "longitude": 77.5946
        },
        "price": {
            "expectedPrice": 50000.0,
            "isPriceNegotiable": true,
            "securityDeposit": 100000.0,
            "callForPrice": false,
            "bookingAmount": 15000.0,
            "maintenanceFee": 3000.0,
            "maintenanceFeeCycle": 2
        },
        "bedrooms": 2,
        "bathrooms": 2,
        "isAttachedBathroom": true,
        "balconies": 1,
        "isAttachedBalcony": true,
        "propertyFloor": 3,
        "totalFloor": 6,
        "facing": "East",
        "isMainRoadFacing": false,
        "buildUpArea": 1200,
        "carpetArea": 1000,
        "propertyAge": 5,
        "availableFrom": "2025-06-01",
        "noticePeriodInMonths": 2,
        "gatedSecurity": true,
        "gym": true,
        "onlyVeg": false,
        "amenities": [
            "Air Conditioner",
            "Internet Services",
            "Lift"
        ],
        "preferredTenants": [
            "BACHELOR_FEMALE",
            "FAMILY"
        ],
        "propertyCategory": "RESIDENTIAL",
        "waterSupply": "BOTH",
        "listingCategory": "RENT_OR_LEASE",
        "propertyType": "FLAT_OR_APARTMENT",
        "furnishedStatus": "SEMI_FURNISHED",
        "possessionStatus": "READY_TO_MOVE",
        "parking": "BOTH"
    }
}
```

#### Response(400 BAD REQUEST)
*Try fetching non-existing property.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Property with that Id doesn't exists.",
    "Recovery": "Confirm the property Id."
}
```

### 4. **VIEW MY PROPERTIES** 

**GET** `/app/myProperties`

Returns a paginated list of all properties added by that user. Authentication required.
Include the following properties as *query parameters*:

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied

#### Request URL
```json
{
    GET /app/myProperties?page=0&size=6&sort=price.expectedPrice,desc&sort=bedrooms,asc
}
```

#### Response(200 OK)
*Fetched all properties of the user with pageable support.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Your Properties fetched successfully.",
    "Details": {
        "content": [
            {
                "id": 100,
                "title": "Spacious 2BHK near Mall",
                "description": "A beautiful and spacious 2BHK apartment with all modern amenities, close to metro and schools.",
                "address": {
                    "fullAddress": "No. 24, Sunrise Residency, MG Road, Near Metro Station",
                    "subLocality": "Indira Nagar",
                    "locality": "MG Road",
                    "city": "Bangalore",
                    "state": "Karnataka",
                    "pinCode": 560001,
                    "latitude": 12.9716,
                    "longitude": 77.5946
                },
                "price": {
                    "expectedPrice": 50000.0,
                    "isPriceNegotiable": true,
                    "securityDeposit": 100000.0,
                    "callForPrice": false,
                    "bookingAmount": 15000.0,
                    "maintenanceFee": 3000.0,
                    "maintenanceFeeCycle": 2
                },
                "bedrooms": 2,
                "bathrooms": 2,
                "isAttachedBathroom": true,
                "balconies": 1,
                "isAttachedBalcony": true,
                "propertyFloor": 3,
                "totalFloor": 6,
                "facing": "East",
                "isMainRoadFacing": false,
                "buildUpArea": 1200,
                "carpetArea": 1000,
                "propertyAge": 5,
                "availableFrom": "2025-06-01",
                "noticePeriodInMonths": 2,
                "gatedSecurity": true,
                "gym": true,
                "onlyVeg": false,
                "amenities": [
                    "Air Conditioner",
                    "Internet Services",
                    "Lift"
                ],
                "preferredTenants": [
                    "BACHELOR_FEMALE",
                    "FAMILY"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "BOTH",
                "listingCategory": "RENT_OR_LEASE",
                "propertyType": "FLAT_OR_APARTMENT",
                "furnishedStatus": "SEMI_FURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "BOTH"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 6,
            "sort": {
                "empty": false,
                "unsorted": false,
                "sorted": true
            },
            "offset": 0,
            "unpaged": false,
            "paged": true
        },
        "last": true,
        "totalElements": 1,
        "totalPages": 1,
        "first": true,
        "size": 6,
        "number": 0,
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "numberOfElements": 1,
        "empty": false
    }
}
```

#### Response(400 BAD REQUEST)
*Occurs when the user not listed any property.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "You haven't listed any property.",
    "Recovery": "Add your first property for free by clicking '+' button."
}
```

### 5. **VIEW ALL PROPERTIES** 

**GET** `/app/properties`

Returns a paginated list of all active properties listed on the platform.
Include the following properties as *query parameters*:

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied

#### Request URL
```json
{
    GET /app/properties?page=0&size=6&sort=price.expectedPrice,desc&sort=bedrooms,asc
}
```

#### Response(200 OK)
*Fetched all properties available on the platform with pageable support.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Properties fetched successfully.",
    "Details": {
        "content": [
            {
                "id": 47,
                "title": "3BHK Villa",
                "description": "Spacious and well-ventilated property located near Guindy with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 862, Garden Street",
                    "subLocality": "Arcot Road",
                    "locality": "Ambattur",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600009,
                    "latitude": 12.885935,
                    "longitude": 80.207968
                },
                "price": {
                    "expectedPrice": 199262.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 484186.0,
                    "callForPrice": false,
                    "bookingAmount": 71869.0,
                    "maintenanceFee": 9048.0,
                    "maintenanceFeeCycle": 4
                },
                "bedrooms": 2,
                "bathrooms": 3,
                "isAttachedBathroom": false,
                "balconies": 2,
                "isAttachedBalcony": false,
                "propertyFloor": 1,
                "totalFloor": 2,
                "facing": "West",
                "isMainRoadFacing": true,
                "buildUpArea": 3246,
                "carpetArea": 627,
                "propertyAge": 3,
                "availableFrom": "2025-07-01",
                "noticePeriodInMonths": 2,
                "gatedSecurity": false,
                "gym": true,
                "onlyVeg": false,
                "amenities": [
                    "Fire Safety",
                    "Visitor Parking",
                    "Rain Water Harvesting",
                    "Intercom"
                ],
                "preferredTenants": [
                    "COMPANY",
                    "FAMILY"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "BOTH",
                "listingCategory": "SALE",
                "propertyType": "VILLA",
                "furnishedStatus": "SEMI_FURNISHED",
                "possessionStatus": "UNDER_CONSTRUCTION",
                "parking": "NONE"
            },
            {
                "id": 34,
                "title": "2BHK Apartment",
                "description": "Spacious and well-ventilated property located near T. Nagar with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 946, Sunset Street",
                    "subLocality": "ECR",
                    "locality": "Kodambakkam",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600059,
                    "latitude": 12.910196,
                    "longitude": 80.20698
                },
                "price": {
                    "expectedPrice": 198586.0,
                    "isPriceNegotiable": true,
                    "securityDeposit": 607815.0,
                    "callForPrice": true,
                    "bookingAmount": 58664.0,
                    "maintenanceFee": 9682.0,
                    "maintenanceFeeCycle": 3
                },
                "bedrooms": 4,
                "bathrooms": 1,
                "isAttachedBathroom": false,
                "balconies": 0,
                "isAttachedBalcony": false,
                "propertyFloor": 5,
                "totalFloor": 12,
                "facing": "West",
                "isMainRoadFacing": false,
                "buildUpArea": 6282,
                "carpetArea": 2167,
                "propertyAge": 29,
                "availableFrom": "2025-06-02",
                "noticePeriodInMonths": 2,
                "gatedSecurity": false,
                "gym": false,
                "onlyVeg": false,
                "amenities": [
                    "Servant Room",
                    "Fire Safety",
                    "House Keeping"
                ],
                "preferredTenants": [
                    "BACHELOR_MALE"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "CORPORATION",
                "listingCategory": "PG_OR_HOSTEL",
                "propertyType": "WAREHOUSE_OR_GODOWN",
                "furnishedStatus": "SEMI_FURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "BIKE"
            },
            {
                "id": 44,
                "title": "1BHK Villa",
                "description": "Beautifully constructed property located near T. Nagar with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 564, Lake View Street",
                    "subLocality": "Poonamallee High Road",
                    "locality": "Mylapore",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600009,
                    "latitude": 12.880334,
                    "longitude": 80.20258
                },
                "price": {
                    "expectedPrice": 197701.0,
                    "isPriceNegotiable": true,
                    "securityDeposit": 449998.0,
                    "callForPrice": false,
                    "bookingAmount": 38482.0,
                    "maintenanceFee": 14128.0,
                    "maintenanceFeeCycle": 4
                },
                "bedrooms": 1,
                "bathrooms": 2,
                "isAttachedBathroom": true,
                "balconies": 2,
                "isAttachedBalcony": true,
                "propertyFloor": 0,
                "totalFloor": 3,
                "facing": "East",
                "isMainRoadFacing": false,
                "buildUpArea": 7356,
                "carpetArea": 565,
                "propertyAge": 19,
                "availableFrom": "2025-07-02",
                "noticePeriodInMonths": 3,
                "gatedSecurity": true,
                "gym": true,
                "onlyVeg": true,
                "amenities": [
                    "House Keeping",
                    "Visitor Parking",
                    "Internet Services",
                    "Rain Water Harvesting",
                    "Gas Pipeline",
                    "Children Play Area",
                    "Shopping Center"
                ],
                "preferredTenants": [
                    "BACHELOR_MALE"
                ],
                "propertyCategory": "COMMERCIAL",
                "waterSupply": "BOTH",
                "listingCategory": "SALE",
                "propertyType": "RESIDENTIAL_HOUSE",
                "furnishedStatus": "UNFURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "CAR"
            },
            {
                "id": 25,
                "title": "Studio House",
                "description": "Spacious and well-ventilated property located near Guindy with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 968, Garden Street",
                    "subLocality": "Mount Road",
                    "locality": "Nungambakkam",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600093,
                    "latitude": 12.84518,
                    "longitude": 80.147389
                },
                "price": {
                    "expectedPrice": 196562.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 72496.0,
                    "callForPrice": false,
                    "bookingAmount": 36511.0,
                    "maintenanceFee": 7605.0,
                    "maintenanceFeeCycle": 4
                },
                "bedrooms": 4,
                "bathrooms": 1,
                "isAttachedBathroom": true,
                "balconies": 2,
                "isAttachedBalcony": true,
                "propertyFloor": 5,
                "totalFloor": 8,
                "facing": "South",
                "isMainRoadFacing": false,
                "buildUpArea": 3900,
                "carpetArea": 2483,
                "propertyAge": 4,
                "availableFrom": "2025-06-18",
                "noticePeriodInMonths": 1,
                "gatedSecurity": false,
                "gym": false,
                "onlyVeg": true,
                "amenities": [
                    "Rain Water Harvesting",
                    "Gas Pipeline",
                    "Power Backup",
                    "Lift"
                ],
                "preferredTenants": [
                    "COMPANY",
                    "BACHELOR_FEMALE"
                ],
                "propertyCategory": "COMMERCIAL",
                "waterSupply": "BOREWELL",
                "listingCategory": "SALE",
                "propertyType": "WAREHOUSE_OR_GODOWN",
                "furnishedStatus": "UNFURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "BOTH"
            },
            {
                "id": 14,
                "title": "2BHK House",
                "description": "Recently renovated property located near Velachery with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 810, Garden Street",
                    "subLocality": "Arcot Road",
                    "locality": "Porur",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600059,
                    "latitude": 12.83318,
                    "longitude": 80.242914
                },
                "price": {
                    "expectedPrice": 196429.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 977946.0,
                    "callForPrice": true,
                    "bookingAmount": 89255.0,
                    "maintenanceFee": 5822.0,
                    "maintenanceFeeCycle": 2
                },
                "bedrooms": 2,
                "bathrooms": 1,
                "isAttachedBathroom": true,
                "balconies": 1,
                "isAttachedBalcony": true,
                "propertyFloor": 0,
                "totalFloor": 3,
                "facing": "West",
                "isMainRoadFacing": false,
                "buildUpArea": 6017,
                "carpetArea": 1782,
                "propertyAge": 10,
                "availableFrom": "2025-06-27",
                "noticePeriodInMonths": 3,
                "gatedSecurity": true,
                "gym": false,
                "onlyVeg": false,
                "amenities": [
                    "Air Conditioner",
                    "House Keeping",
                    "Power Backup",
                    "Park"
                ],
                "preferredTenants": [
                    "BACHELOR_MALE",
                    "ANYONE",
                    "FAMILY"
                ],
                "propertyCategory": "COMMERCIAL",
                "waterSupply": "BOREWELL",
                "listingCategory": "PG_OR_HOSTEL",
                "propertyType": "INDUSTRY",
                "furnishedStatus": "UNFURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "NONE"
            },
            {
                "id": 19,
                "title": "2BHK House",
                "description": "Spacious and well-ventilated property located near Mogappair with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 915, Garden Street",
                    "subLocality": "OMR",
                    "locality": "Adyar",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600080,
                    "latitude": 13.195095,
                    "longitude": 80.249122
                },
                "price": {
                    "expectedPrice": 186523.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 307257.0,
                    "callForPrice": false,
                    "bookingAmount": 82476.0,
                    "maintenanceFee": 13226.0,
                    "maintenanceFeeCycle": 2
                },
                "bedrooms": 1,
                "bathrooms": 2,
                "isAttachedBathroom": false,
                "balconies": 2,
                "isAttachedBalcony": false,
                "propertyFloor": 6,
                "totalFloor": 9,
                "facing": "West",
                "isMainRoadFacing": false,
                "buildUpArea": 6031,
                "carpetArea": 1442,
                "propertyAge": 3,
                "availableFrom": "2025-05-21",
                "noticePeriodInMonths": 1,
                "gatedSecurity": false,
                "gym": false,
                "onlyVeg": false,
                "amenities": [
                    "Internet Services",
                    "Rain Water Harvesting",
                    "Gas Pipeline",
                    "Children Play Area",
                    "Shopping Center",
                    "Club House",
                    "Lift",
                    "Intercom"
                ],
                "preferredTenants": [
                    "BACHELOR_FEMALE"
                ],
                "propertyCategory": "COMMERCIAL",
                "waterSupply": "BOREWELL",
                "listingCategory": "PG_OR_HOSTEL",
                "propertyType": "AGRICULTURAL",
                "furnishedStatus": "FURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "CAR"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 6,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": false,
        "totalElements": 51,
        "totalPages": 9,
        "first": true,
        "size": 6,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 6,
        "empty": false
    }
}
```

#### Response(404 NOT FOUND)
*Occurs when there are no properties available in the platform.*
```json
{
    "code": 404,
    "status": "Not Found",
    "message": "No properties available.",
    "Recovery": "Please try again after some time."
}
```

### 6. **DELETE PROPERTY** 

**POST** `/app/delete`

Deletes a specific property listed by that user, identified via property ID. Authentication required.
Include the following properties as *body*:

- `id` - Integer - Required  

#### Request Body
```json
{
    "id": 100
}
```

### Response(200 OK)
*Deleted the property of the user.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Property deleted successfully.",
    "Home": "/app/properties"
}
```

#### Response(404 NOT FOUND)
*Try deleting others or non-existing property.*
```json
{
    "code": 404,
    "status": "Not Found",
    "message": "Property not found.",
    "Home": "/app/properties"
}
```

### 7. **SEARCH BY LOCALITY** 

**GET** `/app/localitySearch/{locality}`

Searches for properties in the specified locality with pagination support.
Include the following properties as *query parameter*:

- `locality` - String - Required 

#### Request URL
```json
{
    GET /app/localitySearch/Poonamallee High Road?page=0&size=6&sort=price.expectedPrice,asc&sort=bedrooms,asc
}
```

#### Response(200 OK)
*Fetched all properties available on the given locality with pageable support.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Properties at Poonamallee High Road fetched successfully.",
    "Details": {
        "content": [
            {
                "id": 41,
                "title": "3BHK House",
                "description": "Spacious and well-ventilated property located near Ambattur with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 973, Garden Street",
                    "subLocality": "Poonamallee High Road",
                    "locality": "Ambattur",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600038,
                    "latitude": 13.110826,
                    "longitude": 80.185496
                },
                "price": {
                    "expectedPrice": 53963.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 269405.0,
                    "callForPrice": true,
                    "bookingAmount": 94641.0,
                    "maintenanceFee": 14588.0,
                    "maintenanceFeeCycle": 3
                },
                "bedrooms": 4,
                "bathrooms": 1,
                "isAttachedBathroom": false,
                "balconies": 1,
                "isAttachedBalcony": false,
                "propertyFloor": 3,
                "totalFloor": 3,
                "facing": "West",
                "isMainRoadFacing": false,
                "buildUpArea": 706,
                "carpetArea": 606,
                "propertyAge": 28,
                "availableFrom": "2025-06-07",
                "noticePeriodInMonths": 2,
                "gatedSecurity": true,
                "gym": false,
                "onlyVeg": false,
                "amenities": [
                    "Visitor Parking",
                    "Internet Services",
                    "Rain Water Harvesting",
                    "Gas Pipeline",
                    "Sewage Treatment Plant",
                    "Club House",
                    "Lift"
                ],
                "preferredTenants": [
                    "ANYONE"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "BOTH",
                "listingCategory": "PG_OR_HOSTEL",
                "propertyType": "WAREHOUSE_OR_GODOWN",
                "furnishedStatus": "UNFURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "CAR"
            },
            {
                "id": 17,
                "title": "2BHK Villa",
                "description": "Spacious and well-ventilated property located near Guindy with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 782, Lake View Street",
                    "subLocality": "Poonamallee High Road",
                    "locality": "Kodambakkam",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600038,
                    "latitude": 12.890807,
                    "longitude": 80.188726
                },
                "price": {
                    "expectedPrice": 71794.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 668402.0,
                    "callForPrice": false,
                    "bookingAmount": 58340.0,
                    "maintenanceFee": 3059.0,
                    "maintenanceFeeCycle": 3
                },
                "bedrooms": 4,
                "bathrooms": 3,
                "isAttachedBathroom": true,
                "balconies": 2,
                "isAttachedBalcony": false,
                "propertyFloor": 3,
                "totalFloor": 10,
                "facing": "West",
                "isMainRoadFacing": true,
                "buildUpArea": 7469,
                "carpetArea": 2131,
                "propertyAge": 1,
                "availableFrom": "2025-06-15",
                "noticePeriodInMonths": 6,
                "gatedSecurity": false,
                "gym": true,
                "onlyVeg": false,
                "amenities": [
                    "Gas Pipeline",
                    "Sewage Treatment Plant",
                    "Shopping Center",
                    "Club House",
                    "Park",
                    "Intercom"
                ],
                "preferredTenants": [
                    "ANYONE",
                    "FAMILY"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "BOREWELL",
                "listingCategory": "SALE",
                "propertyType": "AGRICULTURAL",
                "furnishedStatus": "SEMI_FURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "BOTH"
            },
            {
                "id": 5,
                "title": "3BHK Villa",
                "description": "Spacious and well-ventilated property located near Thiruvanmiyur with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 145, Sunset Street",
                    "subLocality": "Poonamallee High Road",
                    "locality": "Anna Nagar",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600082,
                    "latitude": 12.813217,
                    "longitude": 80.299913
                },
                "price": {
                    "expectedPrice": 99944.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 147959.0,
                    "callForPrice": false,
                    "bookingAmount": 47571.0,
                    "maintenanceFee": 11926.0,
                    "maintenanceFeeCycle": 4
                },
                "bedrooms": 2,
                "bathrooms": 2,
                "isAttachedBathroom": true,
                "balconies": 1,
                "isAttachedBalcony": true,
                "propertyFloor": 12,
                "totalFloor": 12,
                "facing": "North",
                "isMainRoadFacing": true,
                "buildUpArea": 3667,
                "carpetArea": 1973,
                "propertyAge": 0,
                "availableFrom": "2025-05-15",
                "noticePeriodInMonths": 6,
                "gatedSecurity": false,
                "gym": false,
                "onlyVeg": true,
                "amenities": [
                    "Air Conditioner",
                    "House Keeping",
                    "Visitor Parking",
                    "Internet Services",
                    "Gas Pipeline",
                    "Sewage Treatment Plant",
                    "Park",
                    "Intercom"
                ],
                "preferredTenants": [
                    "ANYONE",
                    "FAMILY"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "BOTH",
                "listingCategory": "PG_OR_HOSTEL",
                "propertyType": "STUDIO_APARTMENT",
                "furnishedStatus": "FURNISHED",
                "possessionStatus": "UNDER_CONSTRUCTION",
                "parking": "BIKE"
            },
            {
                "id": 30,
                "title": "1BHK House",
                "description": "Spacious and well-ventilated property located near Porur with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 235, Sunset Street",
                    "subLocality": "Poonamallee High Road",
                    "locality": "Thiruvanmiyur",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600086,
                    "latitude": 13.061118,
                    "longitude": 80.228444
                },
                "price": {
                    "expectedPrice": 150596.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 512122.0,
                    "callForPrice": true,
                    "bookingAmount": 68748.0,
                    "maintenanceFee": 2412.0,
                    "maintenanceFeeCycle": 4
                },
                "bedrooms": 2,
                "bathrooms": 3,
                "isAttachedBathroom": false,
                "balconies": 1,
                "isAttachedBalcony": true,
                "propertyFloor": 4,
                "totalFloor": 9,
                "facing": "East",
                "isMainRoadFacing": true,
                "buildUpArea": 939,
                "carpetArea": 528,
                "propertyAge": 5,
                "availableFrom": "2025-06-27",
                "noticePeriodInMonths": 1,
                "gatedSecurity": true,
                "gym": false,
                "onlyVeg": false,
                "amenities": [
                    "Gas Pipeline",
                    "Sewage Treatment Plant",
                    "Power Backup",
                    "Lift",
                    "Intercom"
                ],
                "preferredTenants": [
                    "BACHELOR_MALE",
                    "BACHELOR_FEMALE"
                ],
                "propertyCategory": "COMMERCIAL",
                "waterSupply": "BOTH",
                "listingCategory": "SALE",
                "propertyType": "RESIDENTIAL_HOUSE",
                "furnishedStatus": "UNFURNISHED",
                "possessionStatus": "UNDER_CONSTRUCTION",
                "parking": "BOTH"
            },
            {
                "id": 42,
                "title": "2BHK Villa",
                "description": "Recently renovated property located near Perambur with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 191, Sunset Street",
                    "subLocality": "Poonamallee High Road",
                    "locality": "Guindy",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600035,
                    "latitude": 12.984366,
                    "longitude": 80.159396
                },
                "price": {
                    "expectedPrice": 170306.0,
                    "isPriceNegotiable": true,
                    "securityDeposit": 780366.0,
                    "callForPrice": true,
                    "bookingAmount": 94610.0,
                    "maintenanceFee": 7655.0,
                    "maintenanceFeeCycle": 1
                },
                "bedrooms": 4,
                "bathrooms": 1,
                "isAttachedBathroom": false,
                "balconies": 2,
                "isAttachedBalcony": false,
                "propertyFloor": 0,
                "totalFloor": 2,
                "facing": "East",
                "isMainRoadFacing": false,
                "buildUpArea": 2008,
                "carpetArea": 1434,
                "propertyAge": 20,
                "availableFrom": "2025-06-21",
                "noticePeriodInMonths": 2,
                "gatedSecurity": true,
                "gym": false,
                "onlyVeg": true,
                "amenities": [
                    "Internet Services",
                    "Gas Pipeline",
                    "Children Play Area",
                    "Park",
                    "Intercom"
                ],
                "preferredTenants": [
                    "BACHELOR_MALE",
                    "BACHELOR_FEMALE",
                    "FAMILY"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "BOREWELL",
                "listingCategory": "PG_OR_HOSTEL",
                "propertyType": "FLAT_OR_APARTMENT",
                "furnishedStatus": "FURNISHED",
                "possessionStatus": "READY_TO_MOVE",
                "parking": "BIKE"
            },
            {
                "id": 4,
                "title": "Studio Villa",
                "description": "Spacious and well-ventilated property located near Porur with easy access to all essentials.",
                "address": {
                    "fullAddress": "No. 387, Garden Street",
                    "subLocality": "Poonamallee High Road",
                    "locality": "Porur",
                    "city": "Chennai",
                    "state": "Tamilnadu",
                    "pinCode": 600095,
                    "latitude": 12.849376,
                    "longitude": 80.163826
                },
                "price": {
                    "expectedPrice": 178366.0,
                    "isPriceNegotiable": false,
                    "securityDeposit": 993769.0,
                    "callForPrice": false,
                    "bookingAmount": 51189.0,
                    "maintenanceFee": 12824.0,
                    "maintenanceFeeCycle": 2
                },
                "bedrooms": 2,
                "bathrooms": 2,
                "isAttachedBathroom": true,
                "balconies": 2,
                "isAttachedBalcony": true,
                "propertyFloor": 2,
                "totalFloor": 2,
                "facing": "West",
                "isMainRoadFacing": true,
                "buildUpArea": 3991,
                "carpetArea": 3924,
                "propertyAge": 26,
                "availableFrom": "2025-05-17",
                "noticePeriodInMonths": 3,
                "gatedSecurity": true,
                "gym": false,
                "onlyVeg": true,
                "amenities": [
                    "Fire Safety",
                    "Visitor Parking",
                    "Power Backup",
                    "Shopping Center",
                    "Park",
                    "Intercom"
                ],
                "preferredTenants": [
                    "BACHELOR_FEMALE"
                ],
                "propertyCategory": "RESIDENTIAL",
                "waterSupply": "CORPORATION",
                "listingCategory": "PG_OR_HOSTEL",
                "propertyType": "SHOWROOM",
                "furnishedStatus": "UNFURNISHED",
                "possessionStatus": "UNDER_CONSTRUCTION",
                "parking": "NONE"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 6,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": false,
        "totalElements": 8,
        "totalPages": 2,
        "first": true,
        "size": 6,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 6,
        "empty": false
    }
}
```

#### Response(400 NOT FOUND)
*Occurs when there are no properties available in the given locality*
```json
{
    "code": 404,
    "status": "Not Found",
    "message": "No properties available in DLF Street locality.",
    "Recovery": "Please try by expanding your search a bit."
}
```

### 8. **FILTER PROPERTIES**

**GET** `/app/filter`

Applies multiple dynamic filters (e.g. type, price, BHK, area, amenities) to search properties with pagination.
Include the following properties:

- `username` - String - Required  
- `password` - String - Required  

#### Request Body
```json
{
    "username": "john_doe123",
    "password": "Abc@1234"
}
```

### 9. **GET SELLER DETAILS**

**POST** `/app/sellerDetails`

Fetches seller information for a specific property and triggers an automated contact exchange feature, which emails both buyer and seller their respective details when the buyer expresses interest. Authentication required.
Include the following properties as *body*:

- `id` - Integer - Required  

#### Request Body
```json
{
    "id": 38
}
```

#### Response(200 OK)
*Seller details fetched and email along with required details of the buyer and seller gets exchanged.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Property Seller details fetched successfully.",
    "Details": {
        "firstName": "Meera",
        "lastName": "Sharma",
        "gender": "Male",
        "email": "meera.sharma34@example.net",
        "phoneNumber": "+916905661623",
        "isEmailVerified": true,
        "isPhoneNumberVerified": true
    }
}
```

#### Response(400 BAD REQUEST)
*Try fetching non-existing property.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Property with that Id doesn't exists.",
    "Recovery": "Confirm the property Id."
}
```

#### Response(400 BAD REQUEST)
*Can't be interested on own property.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Buyer and Seller cant be the same person.",
    "Recovery": "Try entering different property id."
}
```

**Buyer Email**
![Buyer Email](./screenshots/Buyer%20Received%20Email.png)

**Seller Email**
![Seller Email](./screenshots/Buyer%20Received%20Email.png)

### 10. **LIKE / UNLIKE PROPERTY** 

**POST** `/app/like`

Toggles the like status for a property (like if not liked, unlike if already liked) by the user. Authentication required.
Include the following properties as *body*:

- `id` - Integer - Required  

#### Request Body
```json
{
    "id": 38
}
```

#### Response(200 OK)
*Liked a property.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Property liked successfully.",
    "Details": "2BHK Apartment liked."
}
```

#### Response(200 OK)
*Unliked a property.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Property unliked successfully.",
    "Details": "2BHK Apartment unliked."
}
```

#### Response(400 BAD REQUEST)
*Try liking non-existing property.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Property with that Id doesn't exists.",
    "Recovery": "Confirm the property Id."
}
```

### 11. **GET LIVE LIKE COUNT** 

**POST** `/app/likeCount`

Returns the total number of likes received by a specific property.
Include the following properties as *body*:

- `id` - Integer - Required  

#### Request Body
```json
{
    "id": 38
}
```

#### Response(400 BAD REQUEST)
*Live like count of a property fetched successfully.*
```json
{
    "code": 200,
    "status": "OK",
    "message": "Property like count fetched successfully.",
    "Likes Count": 1
}
```

#### Response(400 BAD REQUEST)
*Try fetching like count of non-existing property.*
```json
{
    "code": 400,
    "status": "Bad Request",
    "message": "Property with that Id doesn't exists.",
    "Recovery": "Confirm the property Id."
}
```


---

## 📁 Project Structure

```
src
└── main
    └── java
        └── com
            └── rentease
                └── rental_management
                    ├── auth
                    │   ├── config
                    │   │   ├── GeneralConfig.java
                    │   │   └── SecurityConfig.java
                    │   ├── controller
                    │   │   └── UsersController.java
                    │   ├── dto
                    │   │   ├── OTPRequest.java
                    │   │   ├── TokenRequest.java
                    │   │   ├── UserAsParty.java
                    │   │   ├── UsersLogin.java
                    │   │   ├── UsersPasswordChange.java
                    │   │   ├── UsersProfile.java
                    │   │   ├── UsersRegistration.java
                    │   │   ├── UsersUpdate.java
                    │   │   ├── VerifyEmailOTPRequest.java
                    │   │   └── VerifyPhoneOTPRequest.java
                    │   ├── entity
                    │   │   ├── UserPrincipal.java
                    │   │   └── Users.java
                    │   ├── filter
                    │   │   ├── JWTExceptionHandlerFilter.java
                    │   │   └── JWTFilter.java
                    │   ├── repository
                    │   |   └── UsersRepository.java
                    |   └── service
                    │       ├── impl
                    │       │   ├── JwtServiceImpl.java
                    │       |   ├── UserDetailsServiceHelper.java
                    │       |   ├── UsersServiceImpl.java
                    │       ├── JwtService.java
                    │       ├── RedisService.java
                    │       └── UsersService.java
                    │
                    ├── rent
                    │   ├── controller
                    │   │   └── PropertyController.java
                    │   ├── dto
                    │   │   ├── AddressDTO.java
                    │   │   ├── PriceDTO.java
                    │   │   ├── PropertyFilterDTO.java
                    │   │   ├── PropertyHashDTO.java
                    │   │   ├── PropertyInfo.java
                    │   │   ├── PropertyProjection.java
                    │   │   ├── PropertyRegistration.java
                    │   │   └── PropertyUpdate.java
                    │   ├── entity
                    │   │   ├── Address.java
                    │   │   ├── Amenity.java
                    │   │   ├── Likes.java
                    │   │   ├── Price.java
                    │   │   └── Property.java
                    │   └── repository
                    │       ├── AmenityRepository.java
                    │       ├── LikesRepository.java
                    │       └── PropertyRepository.java
                    │
                    ├── service
                    │   ├── impl
                    │   │   ├── PropertyLikeServiceImpl.java
                    │   │   └── PropertyServiceImpl.java
                    │   ├── PropertyLikeService.java
                    │   └── PropertyService.java
                    │
                    ├── util
                    │   ├── annotations
                    │   │   ├── validators
                    │   │   |   ├── CarpetAreaValidator.java
                    │   │   |   ├── MinAgeValidator.java
                    │   │   |   └── ValidPropertyFloorValidator.java
                    │   │   ├── MinAge.java
                    │   │   ├── ValidCarpetArea.java
                    │   │   └── ValidPropertyFloor.java
                    │   ├── exception
                    │   │   ├── AuthenticationExceptionHandler.java
                    │   │   ├── CustomAuthEntryPoint.java
                    │   │   ├── GenericExceptionHandler.java
                    │   │   └── TooManyRequestsException.java
                    │   ├── hash
                    │   │   └── PropertyHash.java
                    │   ├── mail
                    │   │   └── EmailNotifier.java
                    │   ├── mappers
                    │   │   ├── PropertyMappers.java
                    │   │   └── UsersMapper.java
                    │   └── response
                    │       ├── CustomResponseCookieHandler.java
                    │       └── ResponseEntityHandler.java
                    │
                    └── RentalManagementApplication.java
```


