package ee.bigbank.dragons.api;

import ee.bigbank.dragons.client.DragonsApiClient;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
@Tag(name = "Game", description = "Dragons of Mugloar game controller API")
public class APIController {

    @Autowired
    protected DragonsApiClient dragonsApiClient;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

}
