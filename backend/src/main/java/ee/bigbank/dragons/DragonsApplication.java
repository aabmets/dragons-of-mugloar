package ee.bigbank.dragons;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Dragons API",
        version = "v1"
    )
)
public class DragonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DragonsApplication.class, args);
	}

}
