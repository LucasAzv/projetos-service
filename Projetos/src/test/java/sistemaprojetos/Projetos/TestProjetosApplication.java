package sistemaprojetos.Projetos;

import org.springframework.boot.SpringApplication;

public class TestProjetosApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProjetosApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
