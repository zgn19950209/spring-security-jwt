package com.security.springsecurityjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.lang.model.element.VariableElement;

@SpringBootTest
class SpringSecurityJwtApplicationTests {

   @Test
    public void test(){
       BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
       String user = encoder.encode("user");
       System.out.println("admin = " + user);
   }

}
