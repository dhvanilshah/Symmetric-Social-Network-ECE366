### Symmetric Social Network: DISC
- by Ariana Freitag, Andy Jeong, Leart Krasniqi, and Dhvanil Shah

#### Project Description
- Symmetric Social Network Service Platform designed to provide a medium through which songs could be shared. 
- Song access(fetching) will be possible through open-source APIs (i.e. Spotify API)
- Language: Java, JavaScript
- Tools: Maven

#### Tech Stack
- ReactJS, Redux, Axios
- Nginx, Apollo-Spotify, MongoDB
********
##### Explanation of backend
In general, the flow of the process is as such:
App -> Handler routes -> Controller implementation. Upon start, HttpService.boot(), followed by init(), is called, where path routes from the Handlers are registed: ```java registerRoutes( Handler.routes() )```. Then each path route is asynchronously linked to the specified uri and its method type (i.e. GET, POST). 

To check what parameters are needed for a user, post, etc., check under ```/model``` directory

User currently has the following path routes (subject to change):
  1) /addUser : adds a user with the input payload. 
  
  2) /getUser/${name} : returns a user with the specified ${name}

Post currently has the following path routes (subject to change): 
  1) /getFeed?name=${name}
  when name equals the {first+last name concatenated} of an existing user, it returns all posts written by the identified user and his/her friends. This uses methods ```java getPosts``` on each friend of the user (```java getFriends ```) and the user as well, and returns a list of Posts.
  
  2) /addPost
  given writer, targeted user and message, it adds a post entry to the collection in the database. The input payload will be passed in as a JSON object (```java payload()```). 
  
  3) /getAllPosts
  retrieves all posts in the post collection.


