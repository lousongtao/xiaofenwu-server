package com.shuishou.retailer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


@Component
public class ServerProperties implements InitializingBean{
	private final static Logger logger = LoggerFactory.getLogger(ServerProperties.class);
	
	public static Properties prop = new Properties();
	public static final String MEMBERLOCATION_LOCAL = "LOCAL";
	public static final String MEMBERLOCATION_CLOUD = "CLOUD";
	public static String MEMBERLOCATION;
	public static String MEMBERCLOUDLOCATION;
	public static String MEMBERCUSTOMERNAME;
	
	private String configFile = "/server_config.properties";
	
	@Override
	public void afterPropertiesSet() throws Exception {
		InputStream input = null;
		try {
			input = this.getClass().getClassLoader().getResourceAsStream(configFile);
			prop.load(input);
			MEMBERLOCATION = prop.getProperty("MemberLocation");
			MEMBERCLOUDLOCATION = prop.getProperty("MemberCloudLocation");
			MEMBERCUSTOMERNAME = prop.getProperty("MemberCustomerName");
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
