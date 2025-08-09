//package at.gdn;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.testcontainers.containers.OracleContainer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.testcontainers.utility.DockerImageName;
//import org.testcontainers.utility.TestcontainersConfiguration;
//
//@TestConfiguration(proxyBeanMethods = false)
//public class TestcontainerConfiguration {
//
//
//    @Value("${app.test.docker.db.image.name}")
//    private String imageName;
//
//    @Bean
//    @ServiceConnection
//    OracleContainer oracleContainer() {
//
//        return new OracleContainer(DockerImageName.parse(imageName))
//                .withExposedPorts(1521);
//    }
//}