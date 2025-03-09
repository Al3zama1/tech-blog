package com.selflearntech.techblogbackend.config;

import io.imagekit.sdk.ImageKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageKitConfig {
    @Value("${IMAGEKIT_ENDPOINT}")
    private String IKUrlEndpoint;
    @Value("${IMAGEKIT_PRIVATE}")
    private String IKPrivateKey;
    @Value("${IMAGEKIT_PUBLIC}")
    private String IKPublicKey;


    @Bean
    public ImageKit imageKit() {
        ImageKit imageKit = ImageKit.getInstance();
        io.imagekit.sdk.config.Configuration config = new io.imagekit.sdk.config.Configuration(
                IKPublicKey, IKPrivateKey, IKUrlEndpoint
        );
        imageKit.setConfig(config);
        return imageKit;
    }
}
