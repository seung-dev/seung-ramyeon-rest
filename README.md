# seung-ramyeon-rest

Rest API Spring Boot Application

### Gradle

    Install Gradle [바로가기](https://gradle.org/install/)
    
    ```
    $ gradle -v
    ```
    
    Gradle Wrapper
    
    ```
    $ gradle wrapper
    ```
    
    Change Gradle Version with the Gradle Wrapper
    
    ```
    $ gradlew wrapper --gradle-version 8.3
    ```
    
    Gradlew Tasks List
    
    ```
    $ gradlew tasks
    ```
    
    Gradlew Build
    
    ```
    $ gradlew build -Pspring.profiles.active=loc
    $ gradlew build -Pspring.profiles.active=dev
    $ gradlew build -Pspring.profiles.active=ops
    ```

### JVM Options

```
-Xms256m
-Xmx256m
-Dapp.path=d:/apps/seung-ramyeon-rest
```
