package at.gdn;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;



/*####################
PORT IN USE: SHELL
>> lsof -i: 8080
>> kill -9 <PID>
########################*/


//@SpringBootApplication
@Configuration
@TestConfiguration
public class GdnTestMain {

//    @Value("${app.test.db.url}")
//    private String dbUrl;
    @Value("${app.test.db.user}")
    private String dbUser;
    @Value("${app.test.db.password}")
    private String dbPassword;

    @Value("${app.test.docker.db.image.name}")
    private String imageName;
    @Value("gdn_e_commerce_webshop")
    private String containerName;
    @Value("${app.test.db.user}")
    private String dbuser;
    @Value("${app.test.db.password}")
    private String dbUserPassword;
    @Value("${app.test.db.name}")
    private String dbName;
    @Value("${app.test.db.port}")
    private Integer dbport;

    @Bean
    @ServiceConnection
    OracleContainer oracleContainer() {
            //final Integer localPort = dbport; ausgeblendet da unten direkt angebunden

            final Integer localPort = dbport;
            final Integer containerPort = 1521;

            PortBinding portBinding = new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort));
            return new OracleContainer(DockerImageName.parse(imageName))
                    .withCreateContainerCmdModifier(cmd -> {
                        cmd.withName(containerName); //Container name
                        cmd.withHostConfig(new HostConfig().withPortBindings(portBinding));
                    })
                    .withDatabaseName(dbName)
                    .withUsername(dbuser)
                    .withPassword(dbUserPassword)
                    .withReuse(true)
                    .withExposedPorts(containerPort);
    }


    public static void main(String[] args) {
        SpringApplication.from(GdnApplication::main)
                         .with(GdnTestMain.class)
                         .run(args);

//        braucht man für Schema-Erstellung ( den oberen Teil auskommentieren)
//        SpringApplication.run(GdnApplication.class, args);
    }
}
