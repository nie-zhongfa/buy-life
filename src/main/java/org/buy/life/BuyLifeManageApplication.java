package org.buy.life;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.buy.life")
public class BuyLifeManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyLifeManageApplication.class, args);
	}


}
