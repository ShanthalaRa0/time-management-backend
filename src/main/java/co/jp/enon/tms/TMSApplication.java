package co.jp.enon.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class TMSApplication {
	 
	public static void main(String[] args) {
		SpringApplication.run(TMSApplication.class, args);
	}

	// If you delete the following addCorsMappings() process, the following error will occur when you try to get after logging in.
	// 		Access to XMLHttpRequest at 'http://localhost:8080/api/test/users'
	//			from origin 'null' has been blocked by CORS policy:
	//				Response to preflight request doesn't pass access control check:
	//					No 'Access-Control-Allow-Origin' header is present on the requested resource.
	// 		GET http://localhost:8080/api/test/users net::ERR_FAILED
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			// Defining global CORS configuration
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					//Enable CORS for the entire application (to enable CORS for the entire controller, set it in Controller)
					.addMapping("/**")
					//registry.addMapping("/actor")

					// A required, comma-separated list of origins that are allowed to access the API deployment.
					// If you want to send cookies in an XMLHttpRequest across origins, you need to explicitly specify the origins for which CORS is allowed.
					.allowedOrigins("*")
					//.allowedOrigins(CrossOrigin.DEFAULT_ORIGINS)

					// An optional comma-separated list of HTTP headers that are allowed in the actual request to the API deployment
					.allowedHeaders("Content-Type", "X-Requested-With", "Accept", "Origin", "Authorization"
									, "Access-Control-Request-Method"
									, "Access-Control-Request-Headers")
									//, "Accept-Encoding", "Accept-Language", "Connection"
									//, "Host", "Sec-Fetch-Dest", "Sec-Fetch-Mode", "Sec-Fetch-Site", "User-Agent")
					//.allowedHeaders("*")
					//.allowedHeaders(CrossOrigin.DEFAULT_ALLOWED_HEADERS)

					// An optional comma-separated list of HTTP headers that clients can access in the API deployment's response to the actual request.
					//.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
					//.exposedHeaders("*")	-> Error!

					// An optional comma-separated list of HTTP methods that are allowed in the actual request to the API deployment
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
					//.allowedMethods("*")	-> Error!

					// Whether browsers should send authentication information, such as cookies, with cross-domain requests to the annotated endpoint
					.allowCredentials(false)

					// Sets the amount of time in seconds that a client can cache the response from a preflight request.
					.maxAge(3600L);
			}
		};
	}
}
