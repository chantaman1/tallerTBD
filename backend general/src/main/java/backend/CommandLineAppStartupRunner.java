package backend;

import backend.seeder.MySqlSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	private MySqlSeeder seeder;

	@Override
    public void run(String... args) throws Exception {
        //seeder.seedArtistsStatistic();
        //seeder.seedGenresStatistic();
        //seeder.createUserNodes();
        //seeder.feedNeo4JNodes();
        //seeder.createRelationsUserArtist();
        //seeder.createRelationsUserGenre();
		//seeder.crearRelacionUserArtist();
		//seeder.crearRelacionUserGenre();
    }

}
