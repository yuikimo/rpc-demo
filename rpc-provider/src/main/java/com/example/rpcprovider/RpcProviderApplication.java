package com.example.rpcprovider;

import com.example.config.EnableProviderRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProviderRpc
public class RpcProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcProviderApplication.class, args);
	}

}
