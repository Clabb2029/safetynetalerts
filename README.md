# Safety Net Alerts

API that allows sending information to emergency service systems to give them the means to better prepare and understand all situations.

-----
## DataMapper

The application reads a JSON data file containing information about inhabitants and stations and stores them in an object that allows to output a JSON file from the queries made.

-----
## Endpoints


### http://localhost:8080/person

It performs the following actions:
* add new person
* update an existing person
* delete a person.

### http://localhost:8080/firestation

It performs the following actions:
* add a caserne/address mapping
* update the fire station number of an address
* remove the mapping of a barracks or address.


### http://localhost:8080/medicalRecord

It performs the following actions:
* add medical record
* update an existing medical record
* delete a medical record.

-----
## URLs

### http://localhost:8080/firestation?stationNumber=<station_number>

This url returns a list of people covered by the corresponding fire station. The list includes people’s first names, last names, addresses, and phone numbers, as well as a count of the number of adults and children (any individual 18 years or younger).

### http://localhost:8080/childAlert?address=<address>

This url returns a list of children (any individual aged 18 or under) living at this address. The list includes the first name, last name and age of each child, and a list of other household members.

### http://localhost:8080/phoneAlert?firestation=<firestation_number>

This url returns a list of telephone numbers of residents served by the fire station.


### http://localhost:8080/fire?address=<address>

This url returns the list of inhabitants living at the given addresse and the number of the fire station serving it. The list includes each person’s last name, first name, phone number, age and medical history.


### http://localhost:8080/flood/stations?stations=<a_list_of_station_numbers>

This url returns a list of all households served by the barracks. This list groups people by address. It also includes the name, first name, telephone number, age and background of each resident.


### http://localhost:8080/personInfofirstName=<firstName>&lastName=<lastName>

This url returns the name, first name, age, email address and medical history of each inhabitant.


### http://localhost:8080/communityEmail?city=<city>

This url returns the email addresses of all the inhabitants of the city.