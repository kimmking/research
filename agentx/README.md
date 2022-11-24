# research-agentx

## Invoke java agent with Cglib
Modify `<Premain-Class>cn.kimmking.research.agentx.XAgent</Premain-Class>` in pom.xml.
And then execute command:
```shell
mvn package
javac Demo.java
java -javaagent:target/research-agentx-0.0.1-SNAPSHOT.jar=testagent Demo
```

## Invoke java agent with ByteBuddy
Modify `<Premain-Class>cn.kimmking.research.agentx.XAgentByteBuddy</Premain-Class>` in pom.xml.
And then execute command:
```shell
mvn package
javac Demo.java
java -javaagent:target/research-agentx-0.0.1-SNAPSHOT.jar=testagent Demo
```
