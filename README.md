# WeightTrackingApp


This Android application seeks to provide a simple approach for users to securly track 
their weigth management goals.  We are seeking to server users who want a simple and 
private method of tracking their weight. To do this the application provides the following
features.

## Features
- A Login / Registration screen to securly manage their weight information.
- A Goal setting page.
- A Local Database for securly storing and controling their data.
- A Grid that allows the users to enter new weight information.
- The ability to edit or delete goal data.

The screens above were designed to make the applicationintuitive and user-friendly.  
This was done by making the screens as simple as possible for the task while following
Android design guidelines.  I think this approach makes for a UI/UX that accomplishes
user requirements.

## Design and Coding Approach
To implement the goals and features an iterative approach was taken to building the app.
I started with analyzing the requirements and attempting to break them down into smaller,
bite-sized bits.  As each feature was implemented refactoring was used to keep the code 
'clean' and as free from technical debt as possible.  This is a general approach that can
and should be used moving forward.

## Functional Testing
To ensure the feature were functional, each one was tested thoutghly by hand in a pseudo QA 
approach.  This process is important because it revals useability and functional isssues 
that may be missed with unit testing alone.

## Callanges
While the overall application desing was fairly straignt forward there were a few places where
some challanges had to be overcome.  In particular, integrating our list view of weights with the
RecyclerView and App bar required digging deeper into Android's MVC apprach and hooking into the 
correct events via Interfaces.

## Specific Success
I think list view was and making it editable the biggest win or success in this project.  This is 
because it required understaning how to implement the Observer Pattern in Android without making the
user take extra steps. For a more seasoned Android developer this is probably childs play but. for someone
who is just implementing an app like this for the first time I think this is fundamental learning and requirment.
