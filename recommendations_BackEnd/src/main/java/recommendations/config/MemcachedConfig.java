package recommendations.config;

import net.spy.memcached.MemcachedClient;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemcachedConfig {

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new MemcachedClient(
                new InetSocketAddress("localhost", 11211)
        );
    }
}
