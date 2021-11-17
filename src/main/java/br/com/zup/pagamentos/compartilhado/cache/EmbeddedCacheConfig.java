package br.com.zup.pagamentos.compartilhado.cache;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.NearCacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
class EmbeddedCacheConfig {

    @Bean
    ClientConfig config() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.addNearCacheConfig(nearCacheConfig());
        return clientConfig;
    }

    private NearCacheConfig nearCacheConfig() {
        NearCacheConfig nearCacheConfig = new NearCacheConfig();
        nearCacheConfig.setName("pagamentos");
        nearCacheConfig.setTimeToLiveSeconds(300);
        return nearCacheConfig;
    }
}
