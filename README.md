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

# 📃 API DOCUMENTATION
## 🔐 Authentication Endpoints 
   
### 1. **LOGIN** 

**POST** `/auth/login` 

Authenticate a user and return access/refresh tokens as cookies. 
Include the following properties:

- `username` - String - Required  
- `password` - String - Required  

#### Request Body
```json
{
    "username": "user_123",
    "password": "Pass@1234"
}
```
#### Response(200 OK)
**- COOKIES:**
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
**Login Response**
![Login Response](./screenshots/Logged%20in%20with%20Dummy%20User%20Credentials.png)

**Tokens at Cookies after login**
![Tokens at Cookies](./screenshots/Tokens%20at%20Cookies%20after%20login.png)

#### Response(401 UNAUTHORIZED)
**- Wrong credentials**
```json
{
    "code": 401,
    "status": "Unauthorized",
    "message": "Bad Credentials. Authentication failed.",
    "Validation Error": "Invalid username or password"
}
```
**Wrong Credentials**
![Wrong Credentials](./screenshots/Wrong%20Credentials.png)

### 2. **REGISTER** 

**POST**	`/auth/signup`

Register a new user. 
Include the following properties:

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
**User Registration**
![User Registration](./screenshots/User%20Registration.png)

#### Response(200 OK)
**- Registered successfully**
```json
{
    "code": 201,
    "status": "Created",
    "message": "User registered successfully. Please verify your email.",
    "Details": "<firstName>"
}
```
#### Response(409 CONFLICT)
**- Username | Email ID | Phone Number**
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

**Username Conflict**
![Username Conflict](./screenshots/Duplicate%20Username.png)

**Email ID Conflict**
![Email ID Conflict](./screenshots/Duplicate%20email%20ID.png)

**Phone Number Conflict**
![Phone Number Conflict](./screenshots/Duplicate%20phone%20number.png)

#### Response(400 BAD REQUEST)
**- Password - Confirm password mismatch**
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
Include the following properties:

- `firstName` - String - Required  
- `lastName` - String - Optional  
- `gender` - String - Required  
- `dateOfBirth` - LocalDate - Required  

#### Request Body JSON
```json
{
    "firstName": "Alice",
    "lastName": "Smith",
    "gender": "Female",
    "dateOfBirth": "1990-05-20"
}
```
#### Response(200 OK)
**- Updated successfully**
```json
{
    "code": 200,
    "status": "OK",
    "message": "User Profile updated successfully",
    "Details": "<firstName>"
}
```

### 4. **CHANGE PASSWORD** 

**POST**	`/auth/changePassword`

Change user password. Authentication required.
Include the following properties:

- `oldPassword` - String - Required  
- `password` - String - Required  
- `confirmPassword` - String - Required  

#### Request Body JSON
```json
{
    "oldPassword": "Old@Pass1",
    "password": "New@Pass2",
    "confirmPassword": "New@Pass2"
}

```
#### Response(200 OK)
**- Password change successful**
```json
{
    "code": 200,
    "status": "OK",
    "message": "Change Password request processed successfully.",
    "Details": "<firstName>"
}
```
#### Response(403 FORBIDDEN)
**- Old password incorrect**
```json
{
    "code": 403,
    "status": "Forbidden",
    "message": "Old Password - Incorrect",
    "Recovery": "Enter correct Old Password"
}
```
#### Response(400 BAD REQUEST)
**- Password - Confirm Password mismatch**
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
Include the following properties:

#### Response(200 OK)
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
Include the following properties:

#### Response(200 OK)
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
Include the following properties:

- `refreshToken` - String - Required  

#### Request Body JSON
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
#### Response(200 OK)
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
Include the following properties:

- `email` - String - Required

#### Request Body JSON
```json
{
  "email": "john.doe@example.com"
}
```
#### Response(200 OK)
**- Email already verified**
```json
{
    "code": 200,
    "status": "OK",
    "message": "Email Id already verified.",
    "Action": "Continue looking for properties."
}
```
#### Response(200 OK)
**- Email sent successfully**
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
Include the following properties:

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
**- Email verified successfully**
```json
{
    "code": 200,
    "status": "OK",
    "message": "Email verified successfully.",
    "Details": "<firstName>"
}
```
#### Response(400 BAD REQUEST)
**- OTP Expired**
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

## 🔗 Postman Collection


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


