package backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TallerTBDApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerTBDApplication.class, args);
		/*Elastic e = new Elastic();
		 e.indexCreate();
		 System.out.println("Index creado, saliendo...");
		 System.exit(0);*/
		//MySqlSeeder seeder = new MySqlSeeder();
		//seeder.seedArtistsStatistic();
		//seeder.seedGenresStatistic();
	}

}
