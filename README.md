# Asssignment-and-Exam-deadline
| Student Name | Student ID |
| :--- | :---: |
| Patrick Simukonda | 2421020 |
| Gabriel Sakala | 2421031 |
| Blessings Kalumba | 2421022 |
| Thomas Phiri | 2420995 |
| Annabel Mbuye | 2410487 |

The Problem

University students often struggle to keep track of assignment deadlines, tests, and examinations from different courses. This can lead to missed deadlines, poor preparation, and difficulty deciding which academic task to prioritise.
Our Assignment and Exam Deadline Planner helps students organise their academic tasks, monitor upcoming deadlines, and identify the most urgent tasks through a priority scoring system.

 How to Run the Project

 Follow these steps to run the application:
1. Install Java Development Kit (JDK).
2. Install Apache Maven.
3. Clone or download this GitHub repository.
4. Open the project in Visual Studio Code or another Java IDE.
5. Allow Maven to download the required dependencies.
6. Run the Maven build to check that the project compiles.
7. Run Main.java located in com.deadlineplanner.
8. Once the Javalin server starts, open your browser.
9. Visit http://localhost:7070 to access the Deadline Planner.

Our Java Classes

1. Task.java: Abstract parent class containing common properties and behaviours of academic tasks.
2. Assignment.java: Represents assignments and calculates priority using remaining days and assignment weight.
3. Test.java: Represents tests and calculates priority based on approaching test dates.
4. Exam.java: Represents examinations and gives them higher priority as their dates approach.
5. Planner.java: Manages tasks, including adding, deleting, completing and organising tasks.
6. FileStorage.java: Saves and loads task information using file storage.
7. Main.java: Starts the Javalin server and provides API endpoints connecting the backend to the frontend.
8. PastDeadlineException.java: Handles errors when a task has a deadline that has already passed.
9. StorageException.java: Handles errors related to saving and loading task information.
10. TaskNotFoundException.java: Handles situations where a requested task cannot be found
