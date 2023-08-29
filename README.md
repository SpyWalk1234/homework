<h1>Wandoo homework Automation Tests</h1>

* There is the E2E scenario which checks basic functionality.
* There is not valid user registration scenario.
* There is user with existing email registration scenario.


<h3>System requirements</h3>

* JDK 20, Gradle 8.2.1.



<h3>Local run</h3>
1) For running all tests by IntelliJ IDEA: Launch `.runConfiguration/RunCucumberTest.xml` run configuration.</br>
2) For running tests by Gradle: gradlew :test --tests "com.wandoo.homework.RunCucumberTest" --stacktrace (Windows).</br>
3) Test reports are available after test execution via RunCucumberTest.run.xml in the Run tab below test scenario results.