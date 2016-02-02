package such.alex.tpv.config.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;

/**
 * Created by alejandro on 02/02/2016.
 */
@Configuration
public class CustomBeans {
    private final Logger log = LoggerFactory.getLogger(CustomBeans.class);

    @Bean
    Environment env() {
        return Environment
            .initializeIfEmpty()
            .assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(Environment env) {
        return EventBus
            .create(env, Environment.THREAD_POOL);
    }

}
