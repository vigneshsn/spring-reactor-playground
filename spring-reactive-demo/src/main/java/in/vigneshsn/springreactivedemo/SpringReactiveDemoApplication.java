package in.vigneshsn.springreactivedemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

@SpringBootApplication
@Configuration
public class SpringReactiveDemoApplication{

	@Bean
	CommandLineRunner appInit() {
		return (args) -> System.out.print("command runner with lambda");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveDemoApplication.class, args);
	}

}


@RestController
@RequestMapping("/players")
class PlayerController {

	private final PlayerRepository playerRepository;

	PlayerController(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	@GetMapping
	public Flux<Player> all() {
		return playerRepository.findAll();
	}

	@GetMapping(value = "/{id}/score", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Score> getScoreById(@PathVariable String id) {
		Random random = new Random();
		return Flux.<Score>generate(scoreSynchronousSink ->
				scoreSynchronousSink.next(new Score(id, random.nextInt())))
				.delayElements(Duration.ofSeconds(1));
	}
}

@Component
class DataAppIntializer{
	private final PlayerRepository playerRepository;

	@org.springframework.context.event.EventListener(ApplicationReadyEvent.class)
	public void run() {
		playerRepository
				.deleteAll()
				.thenMany(Flux.just("dhoni", "virat")
				.flatMap(name -> playerRepository.save(new Player(name))))
				.subscribe(System.out::print);
	}

	DataAppIntializer(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
}

interface PlayerRepository extends ReactiveMongoRepository<Player, String> {
}

@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
class Player{
	private String id;
	@NonNull
	private String name;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Score{
	private String playerId;
	private int runs;
}
