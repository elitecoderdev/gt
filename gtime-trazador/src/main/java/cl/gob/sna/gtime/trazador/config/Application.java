/**
 *  Copyright 2005-2018 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package cl.gob.sna.gtime.trazador.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.component.restlet.RestletComponent;
import org.restlet.Component;
import org.restlet.ext.spring.SpringServerServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * A spring-boot application that includes a Camel route builder to setup the
 * Camel routes
 */
@SpringBootApplication
@ImportResource({ "classpath:spring/camel-context.xml" })

public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	// must have a main method spring-boot can run
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
	}

	/*@Bean
	public ServletRegistrationBean servletRegistrationBean(ApplicationContext context) {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new SpringServerServlet(),
				"/rest/*");

		Map<String, String> params = new HashMap<String, String>();
		params.put("org.restlet.component", "restletComponent");

		servletRegistrationBean.setInitParameters(params);
		return servletRegistrationBean;
	}
	

	
	@Bean
	public Component restletComponent() {
		return new Component();
	}

	@Bean
	public RestletComponent restletComponentService() {
		return new RestletComponent(restletComponent());
	}*/

}
