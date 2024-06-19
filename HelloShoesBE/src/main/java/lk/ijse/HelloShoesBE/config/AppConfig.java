package lk.ijse.HelloShoesBE.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    // CORS Configuration
    @Bean
    public CORSConfig corsConfig() { return new CORSConfig(); }

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}