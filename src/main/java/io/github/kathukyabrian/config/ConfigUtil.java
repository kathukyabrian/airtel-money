package io.github.kathukyabrian.config;

import io.github.kathukyabrian.constants.ServiceConstants;
import io.github.kathukyabrian.exceptions.ConfigurationException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigUtil {
    public static Properties readConfig() throws Exception {
        String fileName = System.getenv().get(ServiceConstants.AIRTEL_CONFIG_ENV_VARIABLE);

        Properties properties = new Properties();
        if (fileName != null) {
            properties.load(Files.newInputStream(Paths.get(fileName)));
        } else {
            properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream(ServiceConstants.DEFAULT_CONFIG_FILE_NAME));
        }

        return properties;
    }

    public static ApplicationProperties getProperties(Properties properties) {
        ApplicationProperties applicationProperties = new ApplicationProperties();

        applicationProperties.setClientId(properties.getProperty(ServiceConstants.CLIENT_ID_CONFIG_KEY));
        applicationProperties.setClientSecret(properties.getProperty(ServiceConstants.CLIENT_SECRET_CONFIG_KEY));
        applicationProperties.setAuthUrl(properties.getProperty(ServiceConstants.AUTH_URL_CONFIG_KEY));
        applicationProperties.setPaymentUrl(properties.getProperty(ServiceConstants.PAYMENT_URL_CONFIG_KEY));
        applicationProperties.setQueryUrl(properties.getProperty(ServiceConstants.QUERY_URL_CONFIG_KEY));
        applicationProperties.setCountry(properties.getProperty(ServiceConstants.COUNTRY_CONFIG_KEY));
        applicationProperties.setCurrency(properties.getProperty(ServiceConstants.CURRENCY_CONFIG_KEY));

        return applicationProperties;
    }

    public static void validateProperties(ApplicationProperties applicationProperties) throws ConfigurationException {
        String reason = null;

        if (applicationProperties.getClientId() == null || applicationProperties.getClientId().isEmpty()) {
            reason = ServiceConstants.CLIENT_ID_CONFIG_KEY;
        }

        if (applicationProperties.getClientSecret() == null || applicationProperties.getClientSecret().isEmpty()) {
            reason = ServiceConstants.CLIENT_SECRET_CONFIG_KEY;
        }

        if (applicationProperties.getAuthUrl() == null || applicationProperties.getAuthUrl().isEmpty()) {
            reason = ServiceConstants.AUTH_URL_CONFIG_KEY;
        }

        // payment url
        if (applicationProperties.getPaymentUrl() == null || applicationProperties.getPaymentUrl().isEmpty()) {
            reason = ServiceConstants.PAYMENT_URL_CONFIG_KEY;
        }

        if (applicationProperties.getCountry() == null || applicationProperties.getCountry().isEmpty()) {
            reason = ServiceConstants.COUNTRY_CONFIG_KEY;
        }

        if (applicationProperties.getCurrency() == null || applicationProperties.getCurrency().isEmpty()) {
            reason = ServiceConstants.CURRENCY_CONFIG_KEY;
        }

        if (reason != null) {
            throw new ConfigurationException(reason + " cannot be null or empty");
        }
    }
}
