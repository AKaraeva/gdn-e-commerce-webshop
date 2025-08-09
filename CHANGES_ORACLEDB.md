################################################
test/application.properties
################################################
#spring.datasource.driver-class-name=oracle.jdbc.OracleDriver


################################################
Main/application.properties
################################################
# Database configuration - Oracle - Docker
#spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/gdnDB
#spring.datasource.username=gdn
#spring.datasource.password=oracle
#spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
#app.main.db.port=1521

################################################
V999_9_9_9_InitTEstData.old zu V999_9_9_9_InitTEstData.sql
V1_0_0_0_InitSchema.old zu V1_0_0_0_InitSchema.sql





################################################
GdnApplicationTest.java
################################################
SpringBootTest(classes = GdnTestMain.class)
class GdnApplicationTests{

//	@Test
//	void contextLoads() {
//	}
//}

################################################
GdnTestMain (auskommentiert)
TestcontainerConfiguration (auskommentiert)

################################################





################################################
OrderRepositoryTest.java
################################################
import
//import at.gdn.TestcontainerConfiguration;

Annotation
//@Import(TestcontainerConfiguration.class)