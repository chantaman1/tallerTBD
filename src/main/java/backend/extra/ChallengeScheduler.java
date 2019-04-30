package backend.extra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import backend.controller.UsersController;

@Component
public class ChallengeScheduler implements Runnable {
	@Autowired
	private UsersController usersController;

	@Scheduled(fixedRate = 60000)
	public void run() {
		//AQUI SE puede poner a correr funciones con un tiempo de 60000 ms.
	}
}
