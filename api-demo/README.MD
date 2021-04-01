We need a tomcat server to deploy the app.
You can use any server that is compatible with jakarta ee 8
But I would like to suggest  
tomee        8-jre-8.0.6-plume
to pull the image from docker hub just run
docker pull tomee:8-jre-8.0.6-plume



then we need to make container to run the server. 
In order to do that run the following command.





<Context antiResourceLocking="false" privileged="true" >
  <!--<Valve className="org.apache.catalina.valves.RemoteAddrValve"
         allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" />-->
</Context>