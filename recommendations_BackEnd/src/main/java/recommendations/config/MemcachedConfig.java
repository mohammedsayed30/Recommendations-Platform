package recommendations.config;

import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

public class MemcachedConfig {

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new MemcachedClient(
                new InetSocketAddress("localhost", 11211)
        );
    }
}
