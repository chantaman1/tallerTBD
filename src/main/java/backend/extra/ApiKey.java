package backend.extra;

public class ApiKey {

	private JSONFileReader file = new JSONFileReader();
	private String api_key = file.readApiKeyConfigJSON();

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
}
