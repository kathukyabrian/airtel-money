package io.github.kathukyabrian.config;

import org.junit.jupiter.api.Test;
import io.github.kathukyabrian.constants.ServiceConstants;

import java.util.Properties;

public class ConfigUtilTest {
    @Test
    void testReadConfig() throws Exception {
        Properties properties = ConfigUtil.readConfig();
        assert properties.containsKey(ServiceConstants.CLIENT_ID_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.CLIENT_SECRET_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.AUTH_URL_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.PAYMENT_URL_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.COUNTRY_CONFIG_KEY);
        assert properties.containsKey(ServiceConstants.CURRENCY_CONFIG_KEY);
    }

    @Test
    void testReadProperties() throws Exception {
        Properties properties = ConfigUtil.readConfig();
        ApplicationProperties applicationProperties = ConfigUtil.getProperties(properties);
        assert applicationProperties.getClientId() != null;
        assert applicationProperties.getClientSecret() != null;
        assert applicationProperties.getAuthUrl() != null;
        assert applicationProperties.getPaymentUrl() != null;
        assert applicationProperties.getCountry() != null;
        assert applicationProperties.getCurrency() != null;
    }
}
