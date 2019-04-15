### Build with Maven
```bash
mvn clean compile   // compiles the project
mvn package         // compile, run tests, and produce deployable artifact (fat .jar)
java -jar ${path for fat Jar file} cool.disc.server.App -Dhttp.server.port=${port} // specify path and port address
// by default, the bankend server will run on localhost with above command
```
