package backend.extra;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONFileReader {

	public List<String> readEmailConfigJSON() {
		JSONParser parser = new JSONParser();
		List<String> result = new ArrayList<String>();

		try {
			Path path = Paths.get(getClass().getResource("/files/emailConfiguration.json").toURI());
			Object obj = parser.parse(new FileReader(path.toString()));
			JSONObject jsonObject = (JSONObject) obj;

			String email = (String) jsonObject.get("emailFrom");
			String password = (String) jsonObject.get("password");
			result.add(email);
			result.add(password);
			return result;
		}
		catch(Exception e){
			System.out.println("Error while reading JSON file.");
			return result;
		}
	}

	public String readApiKeyConfigJSON() {
		JSONParser parser = new JSONParser();

		try {
			Path path = Paths.get(getClass().getResource("/files/apiKeyConfiguration.json").toURI());
			Object obj = parser.parse(new FileReader(path.toString()));
			JSONObject jsonObject = (JSONObject) obj;
			String apikey = (String) jsonObject.get("api_key");
			return apikey;
		}
		catch(Exception e){
			System.out.println("Error while reading JSON file.");
			return "ERROR";
		}
	}
}
