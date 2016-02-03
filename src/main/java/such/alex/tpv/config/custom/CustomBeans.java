package such.alex.tpv.config.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.spring.context.config.EnableReactor;

/**
 * Created by alejandro on 02/02/2016.
 */
@Configuration
@EnableReactor
@EnableAsync
public class CustomBeans {
    private final Logger log = LoggerFactory.getLogger(CustomBeans.class);

    @Bean(name = "eventBus")
    EventBus getEventBus(Environment env) {
        return EventBus.create(env, Environment.THREAD_POOL);
    }
}
