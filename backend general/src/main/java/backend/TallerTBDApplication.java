package backend;
 import backend.models.ListaPalabra;
 import backend.services.ListaPalabraService;
 import backend.seeder.MySqlSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
// import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class TallerTBDApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerTBDApplication.class, args);

		 Elastic e = new Elastic();
		 e.indexCreate();
		 System.out.println("Index creado, saliendo...");
     System.exit();
		//MySqlSeeder seeder = new MySqlSeeder();
		//seeder.seedArtistsStatistic();
		//seeder.seedGenresStatistic();
	}

}
