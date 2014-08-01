package com.vteba.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.vteba.io.DefaultResourceLoader;
import com.vteba.io.Resource;
import com.vteba.io.ResourceLoader;
import com.vteba.utils.common.PropertiesLoader;

public class TestRes {
    public static void main(String[] args) {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("config.properties");
        try {
            InputStream inputStream = resource.getInputStream();
            System.out.println(inputStream);
            
            Properties properties = new Properties();
            properties.load(inputStream);
            
            String url = properties.getProperty("jdbc.url");
            System.out.println(url);
        } catch (IOException e) {
            
        }
        
        PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
        String jdbcUrl = propertiesLoader.getProperty("jdbc.url");
        System.out.println(jdbcUrl);
    }
}
