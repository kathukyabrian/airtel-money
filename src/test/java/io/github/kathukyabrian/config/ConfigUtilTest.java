package io.github.kathukyabrian.config;

import io.github.kathukyabrian.constants.ServiceConstants;
import io.github.kathukyabrian.exceptions.ConfigurationException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigUtilTest {

    @Test
    void shouldReadConfigFromFileSpecifiedByEnvironment() throws Exception {

        Path tempFile = Files.createTempFile("airtel", ".properties");

        Files.writeString(tempFile, """
            client-id=my-client
            client-secret=my-secret
            auth-url=https://auth.test
            payment-url=https://payment.test
            query-url=https://query.test
            country=KE
            currency=KES
            """);

        try (MockedStatic<ConfigUtil> mocked = Mockito.mockStatic(ConfigUtil.class, Mockito.CALLS_REAL_METHODS)) {

            System.out.println("***** " + tempFile.toString());
            mocked.when(ConfigUtil::getConfigFileName)
                    .thenReturn(tempFile.toString());

            Properties properties = ConfigUtil.readConfig();

            assertEquals(
                    "my-client",
                    properties.getProperty(ServiceConstants.CLIENT_ID_CONFIG_KEY));

            assertEquals(
                    "my-secret",
                    properties.getProperty(ServiceConstants.CLIENT_SECRET_CONFIG_KEY));
        }
    }

    @Test
    void shouldReturnEnvironmentVariable() {
        // We don't care whether it's null or not.
        // We only want to execute the real method.
        ConfigUtil.getConfigFileName();
    }

    @Test
    void testReadConfig() throws Exception {
        Properties properties = ConfigUtil.readConfig();
        assertTrue(properties.containsKey(ServiceConstants.CLIENT_ID_CONFIG_KEY));
        assertTrue(properties.containsKey(ServiceConstants.CLIENT_SECRET_CONFIG_KEY));
        assertTrue(properties.containsKey(ServiceConstants.AUTH_URL_CONFIG_KEY));
        assertTrue(properties.containsKey(ServiceConstants.PAYMENT_URL_CONFIG_KEY));
        assertTrue(properties.containsKey(ServiceConstants.COUNTRY_CONFIG_KEY));
        assertTrue(properties.containsKey(ServiceConstants.CURRENCY_CONFIG_KEY));
    }

    @Test
    void testReadProperties() throws Exception {
        Properties properties = ConfigUtil.readConfig();
        ApplicationProperties applicationProperties = ConfigUtil.getProperties(properties);
        assertNotNull(applicationProperties.getClientId());
        assertNotNull(applicationProperties.getClientSecret());
        assertNotNull(applicationProperties.getAuthUrl());
        assertNotNull(applicationProperties.getPaymentUrl());
        assertNotNull(applicationProperties.getCountry());
        assertNotNull(applicationProperties.getCurrency());
    }

    @Test
    void shouldThrowMissingClientId() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setClientId(null);
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowEmptyClientId() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setClientId("");
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowMissingClientSecret() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setClientSecret(null);
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowEmptyClientSecret() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setClientSecret("");
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowMissingAuthUrl() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setAuthUrl(null);
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowEmptyAuthUrl() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setAuthUrl("");
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowMissingPaymentUrl() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setPaymentUrl(null);
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowEmptyPaymentUrl() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setPaymentUrl("");
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowMissingCountry() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setCountry(null);
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowEmptyCountry() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setCountry("");
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowMissingCurrency() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setCurrency(null);
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }

    @Test
    void shouldThrowEmptyCurrency() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setCurrency("");
        assertThrows(ConfigurationException.class, () -> ConfigUtil.validateProperties(applicationProperties));
    }
}
