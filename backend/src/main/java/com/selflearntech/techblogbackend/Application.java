package com.selflearntech.techblogbackend;

import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.RoleType;
import com.selflearntech.techblogbackend.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
//        return args -> {
//            Role user = Role.builder().authority(RoleType.USER).build();
//            Role admin = Role.builder().authority(RoleType.ADMIN).build();
//            Role editor = Role.builder().authority(RoleType.EDITOR).build();
//
//            roleRepository.saveAll(Set.of(user, admin, editor));
//
//        };
//    }

}
