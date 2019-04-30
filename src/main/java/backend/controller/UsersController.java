package backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.extra.ApiKey;
import backend.extra.EmailValidator;
import backend.extra.EmailSender;
import backend.models.Users;
import backend.services.UsersService;

@RestController
public class UsersController {

	private ApiKey apikey = new ApiKey();
	private EmailValidator eValidator = new EmailValidator();
	private String p_api_key = apikey.getApi_key();

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	    return bCryptPasswordEncoder;
	}
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UsersService usersServices;

	@RequestMapping(value = "/users/create", method = RequestMethod.POST)
	public List<HashMap<String, String>> create(@RequestBody Map<String, Object> jsonData, @RequestParam String api_key) {
		HashMap<String, String> map = new HashMap<>();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		if(api_key.equals(p_api_key)) {
			boolean isEmailValid = eValidator.isValidEmailAddress(jsonData.get("email").toString());
			if(isEmailValid == true) {
				Users user = usersServices.getByEmail(jsonData.get("email").toString());
				if(user == null) {
					usersServices.create(jsonData.get("firstName").toString(), jsonData.get("lastName").toString(), jsonData.get("email").toString(), bCryptPasswordEncoder.encode(jsonData.get("password").toString()), jsonData.get("role").toString(), jsonData.get("birthDate").toString());
					map.put("status", "201");
					map.put("message", "OK");
					result.add(map);
					Thread mail = new Thread(new EmailSender(jsonData.get("email").toString()));
					mail.start();
					return result;
				}
				else {
					map.put("status", "401");
					map.put("message", "USER ALREADY EXIST!");
					result.add(map);
					return result;
				}

			}
			else {
				map.put("status", "401");
				map.put("message", "EMAIL IS NOT VALID.");
				result.add(map);
				return result;
			}

		}
		else {
			map.put("status", "403");
			map.put("message", "WRONG API KEY.");
			result.add(map);
			return result;
		}
	}
	@RequestMapping(value = "/users/getUser", method = RequestMethod.GET)
	public List<Users> getUser(@RequestParam String email, @RequestParam String api_key) {
		List<Users> result = new ArrayList<Users>();
		if(api_key.equals(p_api_key)) {
			result.add(usersServices.getByEmail(email));
			return result;
		}
		else {
			return result;
		}
	}
	@RequestMapping(value = "/users/login", method = RequestMethod.POST)
	public List<HashMap<String, String>> login(@RequestBody Map<String, Object> jsonData, @RequestParam String api_key){
		HashMap<String, String> map = new HashMap<>();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		if(api_key.equals(p_api_key)) {
			Users user = usersServices.getByEmail(jsonData.get("email").toString());
			if(user != null) {
				if(user.getActivatedAccount() == true) {
					if(user.getLoggedUser() == false) {
						boolean passwordMatch = bCryptPasswordEncoder.matches(jsonData.get("password").toString(), user.getPassword());
						if(passwordMatch == true) {
							usersServices.updateSession(jsonData.get("email").toString());
							map.put("status", "200");
							map.put("message", "OK");
							map.put("login", "true");
							map.put("id", user.get_id());
							map.put("firstName", user.getFirstName());
							map.put("lastName", user.getLastName());
							map.put("email", user.getEmail());
							map.put("role", user.getRole());
							result.add(map);
							return result;
						}
						else {
							map.put("status", "200");
							map.put("message", "PASSWORD DOES NOT MATCH.");
							map.put("login", "false");
							result.add(map);
							return result;
						}
					}
					else {
						map.put("status", "403");
						map.put("message", "USER IS ALREADY LOGGED.");
						map.put("login", "false");
						result.add(map);
						return result;
					}
				}
				else {
					map.put("status", "200");
					map.put("message", "ACCOUNT IS DISABLED.");
					map.put("login", "false");
					result.add(map);
					return result;
				}
			}
			else {
				map.put("status", "404");
				map.put("message", "USER DOES NOT EXIST.");
				map.put("login", "false");
				result.add(map);
				return result;
			}
		}
		else {
			map.put("status", "400");
			map.put("message", "WRONG API KEY.");
			map.put("login", "false");
			result.add(map);
			return result;
		}
	}
	@RequestMapping(value = "/users/getAll", method = RequestMethod.GET)
	public List<Users> getAll() {
		return usersServices.getAll();
	}
	@RequestMapping(value = "/users/updateUserSession", method = RequestMethod.POST)
	public List<HashMap<String, String>> updateSession(@RequestParam String email, @RequestParam String api_key) {
		HashMap<String, String> map = new HashMap<>();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		if(api_key.equals(p_api_key)) {
			if(email == null) {
				map.put("status", "404");
				map.put("message", "EMAIL NOT FOUND.");
				result.add(map);
				return result;
			}else {
				usersServices.updateSession(email);
				map.put("status", "200");
				map.put("message", "OK");
				result.add(map);
				return result;
			}
		}
		else {
			map.put("status", "400");
			map.put("message", "WRONG API KEY.");
			result.add(map);
			return result;
		}
	}
	@RequestMapping(value = "/users/updateUserStatus", method = RequestMethod.POST)
	public List<HashMap<String, String>> updateAccountStatus(@RequestParam String email, @RequestParam String api_key) {
		HashMap<String, String> map = new HashMap<>();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		if(api_key.equals(p_api_key)) {
			usersServices.updateAccountStatus(email);
			map.put("status", "200");
			map.put("message", "OK");
			result.add(map);
			return result;
		}
		else {
			map.put("status", "400");
			map.put("message", "WRONG API KEY.");
			result.add(map);
			return result;
		}
	}
	@RequestMapping(value = "/users/updateUserPassword", method = RequestMethod.POST)
	public List<HashMap<String, String>> updatePassword(@RequestBody Map<String, Object> jsonData, @RequestParam String email, @RequestParam String api_key) {
		HashMap<String, String> map = new HashMap<>();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		if(api_key.equals(p_api_key)) {
			usersServices.updatePassword(email, bCryptPasswordEncoder.encode(jsonData.get("password").toString()));
			map.put("status", "200");
			map.put("message", "OK");
			result.add(map);
			return result;
		}
		else {
			map.put("status", "400");
			map.put("message", "WRONG API KEY.");
			result.add(map);
			return result;
		}
	}
}
