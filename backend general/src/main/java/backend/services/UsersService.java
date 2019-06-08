// package backend.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// import backend.models.Users;
// import backend.repositories.UsersRepository;

// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;
// import java.util.List;

// @Component
// public class UsersService{
//   @Autowired
//   private UsersRepository userRepository;

//   public Users create(String firstName, String lastName, String email, String password, String role, String birthDate) {
// 	  LocalTime localTime = LocalTime.now();
// 	  LocalDate localDate = LocalDate.now();
// 	  String date = localDate.format(DateTimeFormatter.ofPattern("EEEE dd, MM, yyyy"));
// 	  String time = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
// 	  return userRepository.save(new Users(firstName, lastName, email, password, role, birthDate, date, time, false, true));
//   }

//   public List<Users> getAll(){
// 	  return userRepository.findAll();
//   }

//   public Users getByEmail(String email) {
// 	  return userRepository.findByEmail(email);
//   }

//   public List<Users> getById(String _id) {
// 	  return userRepository.findBy_id(_id);
//   }

//   public Users updatePassword(String email, String password) {
// 	  Users user = userRepository.findByEmail(email);
// 	  user.setPassword(password);
// 	  return userRepository.save(user);
//   }

//   public Users updateSession(String email) {
// 	  Users user = userRepository.findByEmail(email);
// 	  boolean status = user.getLoggedUser();
// 	  if(status == true) {
// 		  user.setLoggedUser(false);
// 	  }
// 	  else {
// 		  user.setLoggedUser(true);
// 	  }
// 	  return userRepository.save(user);
//   }

//   public Users updateAccountStatus(String email) {
// 	  Users user = userRepository.findByEmail(email);
// 	  boolean status = user.getActivatedAccount();
// 	  if(status == true) {
// 		  user.setActivatedAccount(false);
// 	  }
// 	  else {
// 		  user.setActivatedAccount(true);
// 	  }
// 	  return userRepository.save(user);
//   }
// }
