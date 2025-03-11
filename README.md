# Turf Booking Bookings Papi
Bookings Process API for the Turf Booking Application

## Architecture
![Turf booking Architecture v2](https://github.com/user-attachments/assets/1be80a85-d7bf-4f2f-a335-d4eec4e6a6f2)

## End Points

### GET /live

To Check if the app is up

### GET /ready

To Check if the dependent apps are running

### GET /turf/all

Returns a list of Turfs along with their Booked slots

### GET /turf

| **Query Parameters** | **Description** |
| --- | --- |
| Parameter | The parameter to filter Turfs by *Name*, *City*, *Area*, S*ports* |
| Value | Value to filter the Turfs by |

Returns a list of Turfs filtered by the specified parameter

### GET /turf/{turfId}

Returns the List of Slots Booked for the Turf with the specified Turf ID

### POST /booking

Creates a booking 

### GET /booking

| **Query Parameters** | **Description** |
| --- | --- |
| Parameter | The parameter to filter Bookings by *Turf,* *User* |
| Value | Value to filter the Bookings by |

Returns a list of Bookings filtered by the specified parameter

### GET /booking/{bookingId}

Returns a Booking based on the Booking ID

### DELETE /booking/{bookingId}

Cancels the Booking based on the Booking ID

### GET /checkout

| **Query Parameters** | **Description** |
| --- | --- |
| username | Username of the User |
| turfId | ID of the Turf |
| slotIds | List of Slot IDs to be booked |

Returns a Checkout for the parameter specified

## Object Example

### ApiResponse

```json
{
    "statusCode": 200,
    "statusMessage": "OK",
    "payload": {
    
    },
    "apiError": null
}
```

### ApiError

```json
{
	"errorMessage": "No value present",
	"errorDescription": "java.util.NoSuchElementException: No value present",
	"customError": "No Turf with the Turf ID: 5, Present in the DB"
}
```

### Booking

```json
{
    "turfId": 4,
    "slotIds": [1,2],
    "userId": 3
}
```

### Booking DTO

```json
{
    "turfId": 2,
    "slotList": [
        {
            "day": "26",
            "month": "09",
            "year": "2024",
            "slotTime": "10am-11am"
        }
    ],
    "userId": 1
}
```

### BookingWrapper

```json
{
    "statusCode": 200,
    "statusMessage": "OK",
    "payload": {
        "bookingId": 1,
        "turfName": "FC Chennai",
        "turfAddress": "No 10, Velachery, Chennai, Tamil Nadu",
        "bookedSlots": [
            {
                "slotId": "01-03-1999$5am-6am",
                "day": "01",
                "month": "03",
                "year": "1999",
                "slotTime": "5am-6am"
            }
        ],
        "userName": "Gautham Vinod",
        "userUserName": "gauthi",
        "price": null
    },
    "apiError": null
}
```

### Checkout

```json
{
    "statusCode": 200,
    "statusMessage": "OK",
    "payload": {
        "username": "ouji",
        "turfName": "Marina",
        "turfAddress": "No 5, Velachery, Chennai, Tamil Nadu",
        "slots": [
            {
                "slotId": "22-09-2024$5am-6am",
                "day": "22",
                "month": "09",
                "year": "2024",
                "slotTime": "5am-6am"
            }
        ],
        "price": 2000
    },
    "apiError": null
}
```

### Slot

```json
{
	"slotId": "22-09-2024$5am-6am",
	"day": "22",
	"month": "09",
	"year": "2024",
	"slotTime": "5am-6am"
}
```

### Turf

```json
{
	"turfId": 3,
	"name": "Marina",
	"city": "Chennai",
	"area": "Velachery",
	"address": "No 5, Velachery, Chennai, Tamil Nadu",
	"sports": "Football",
	"bookedSlots": ["21-09-2024$5am-6am"],
	"pricePerHour": 2000
}
```

### TurfWrapper

```json
{
	"turfId": 3,
	"name": "Marina",
	"city": "Chennai",
	"area": "Velachery",
	"address": "No 5, Velachery, Chennai, Tamil Nadu",
	"sports": "Football",
	"bookedSlots": [
		{
			"slotId": "21-09-2024$5am-6am",
			"day": "21",
			"month": "09",
			"year": "2024",
			"slotTime": "5am-6am"
		}
	],
	"pricePerHour": 2000
}
```

### UserDetails

```json
{
        "userId": 1,
        "username": "gauthi",
        "password": "gauthikutti1",
        "firstName": "Gautham",
        "lastName": "Vinod",
        "phoneNumber": "90909090",
        "emailId": "something4@email.com"
    }
```
