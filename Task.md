# Code assignment for senior backend engineer

## Purpose
Main purpose of this assignment is to test practical skills of designing, coding, testing and building
java applications. It should take you no more than 8h to implement it.

## Prerequisites
Basic java knowledge along with experience in OOP, API design, testing frameworks, and
understanding of basic algorithms and data structures is a necessary prerequisite for taking this
test.

## Assignment
We ask you to implement an application that stores and processes traffic events from our cameras.
Each event entity represents a unique event captured by our cameras and sensors that can be a
Speed or Red Light event. New types of events might be created in the future which might imply
new processors for each event type.
Every event needs to be processed, some of them are Violations and some are not. A violation
means that that vehicle was indeed in violation of the law, therefore a fine should be created.

### The Event entity should look like:
```
{
"id": "d9bb7458-d5d9-4de7-87f7-7f39edd51d18",
"eventDate": "2022-02-09T00:25:20.529",
"eventType": "SPEED",
"licensePlate": "ABC-123",
"speed": 87,
"limit": 50,
"unity": "km/h",
"processed": "false"
}
```

### The Violation entity should look like:
```
{
"eventId": "d9bb7458-d5d9-4de7-87f7-7f39edd51d18",
"fine": 50,
"paid": "false"
}
```

## Requirements
- Implementation should be done in Java and Spring Boot, feel free to use any libraries you
  might want
- Application is thread safe
- Application is using in-memory data structures (not to be confused with in-memory
  databases)
- Upper bound complexity of O(log(n)) for all operations related to the data structure that
  holds traffic events
- Speed events will be fined 50 and redlight events will be fined 100
- A rest endpoint that should return a summary of the total fines paid and still to be paid