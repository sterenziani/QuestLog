# QuestLog

This web application aims to help people chose the games they want to play by providing several features described bellow.
Official release: http://pawserver.it.itba.edu.ar/paw-2020a-4/

## What was it built on?

* Maven 3.6.3
* Java 8
* Spring Framework 4.2.5
* JPA 1.0.0
* Logback 1.1.2
* Mockito 2.25.1
* JUnit 4.11
* Jersey 3.0
* React v17.0.1

## Requirements to run

This web application requires having the following dependencies:

* Maven 3.6.3     (https://maven.apache.org/download.cgi)
* Java 8 JDK      (https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
* Tomcat 7.0.76   (https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.76/bin/)
* PostgreSQL 12   (https://www.postgresql.org/download/)

## How to deploy

1. Clone this repository and go into the `QuestLog` directory:
```bash
git clone https://github.com/sterenziani/QuestLog.git
cd QuestLog/
```
2. Run the following command to create a Maven webapp.war for Tomcat:
```bash
mvn clean package
```
3. Copy your newly created webapp.war file into your Tomcat webapps folder:
```bash
cp webapp/target/webapp.war ${CATALINA_HOME}/webapps
```
4. Start Tomcat:
```bash
${CATALINA_HOME}/bin/startup.sh
```
5. Open your browser and go to the following url:
http://localhost:8080/webapp_war/
6. That's it! Enjoy choosing your games to play, reviewing them and interacting with other users!

## Releases

### v1.0.1

#### Features
* See list of games
* See a game's details
* Search for games
* Save games to your backlog
#### Implementation details
* Backlog is stored inside browser cookies
* PostgreSQL script creates database and tables belonging to the Models
  
### v1.0.2

#### Features
* Create accounts to be used anywhere, anytime
* Browse games based on their genres, developers or publishers
* Get game recommendations based on games you have shown interest in
* Rate games
* Log the time it took you to beat a game
* Look at the average score given by users
* Look at the average time it took users to beat the game

### v1.1.3

#### Features
* Be notified when a game becomes available
* Change your password
* Filter your search results
* Visit users' profiles
* See the page you are at while navigating
* No results while searching shows you games you might be interested in
* Backlog does not appear when empty
* Allow special caracters for email addresses
* Show *Adding to Backlog* or *Removing from Backlog* when adding or removing a game from your Backlog respectively
* Remember users who log in with *Remember me* even if they close their browsers
* Show a user friendly screen if an error occurs
* Favicon for the website
* Paginated search results, backlog and lists of games
* Admin users
* Admins can create games
* Admins can edit games
* Admins can delete games
* Admins can make other users admins
* Admins can remove other admins' admin status
#### Implementation details
* Service tests added
* DAO tests added

### v2.0.4

#### Features
* Remove games from Backlog when submiting a time
* Remove games from Backlog when submiting a score
#### Implementation details
* Migrate Models for User, Image, Region and Platform to JPA

### v2.0.5

#### Features
* Searching shows results sorted alphabetically
* Check your account's email in your profile
* See the average of the scores a user gave to the games they rated
* See a users' best rated game
* Access the site via the welcome email
* Be notified when your password reset token has expired
* See the top 5 shortest runs for a game
* Smaller size of games in lists
#### Implementation details
* Migrate Models for Game, Genre, Developer, Publisher, Playstyle, Release Date, Run and Score to JPA
* Use java.time for Date types

### v2.1.6

#### Features
* Write reviews
* Now works on Microsoft Edge
* Default image for games with no cover
* Login in, login out and registering takes you back to the page you were before doing so
* Redirect to a game if it's the only one matching the search
* See how many results were found with your search
* Watch a game's trailer by navigating to its details
* Allows special characters for inputs
* Better UX for user's profile
* Better UX for filtering searches

### v3.1.7

#### Implementation details
* Migrate Backend to REST API made with Jersey
* Migrate Frontend to SPA made with React
* Unconditional cache and file revving
* Frontend unit tests with React Testing Library
