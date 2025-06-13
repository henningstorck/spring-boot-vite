package com.henningstorck.springbootvite.frontend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frontend")
@Getter
@Setter
public class FrontendProperties {
	private boolean developmentMode;
}
