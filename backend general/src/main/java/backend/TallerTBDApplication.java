package backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.mongodb.*;

@SpringBootApplication
@EnableScheduling
public class TallerTBDApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallerTBDApplication.class, args);
		/*MongoConnection mongo = MongoConnection.getMongo();
		mongo.OpenMongoClient();
		DBCursor cursor = mongo.getTweets();
		System.out.println(cursor);
		while (cursor.hasNext()) {
			DBObject cur = cursor.next();
			//String texto = cur.get("text");
			System.out.println(cur.get("text"));
		}
		cursor.close();*/
	}

}
