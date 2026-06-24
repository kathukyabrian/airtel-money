![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-92%25-green)
![Quality Gate](https://img.shields.io/badge/quality%20gate-passed-brightgreen)
![Security](https://img.shields.io/badge/security-no%20critical%20issues-green)
![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)

# Airtel Money Library

A lightweight Java library for integrating with Airtel Kenya MoMo APIs.

Supports STK Push, transaction status queries, authentication, and callback processing with minimal configuration.

## Features
- Auth 
- STK Push
- Query Transaction Status(coming soon)
- Quick Configuration

## Getting Airtel Kenya Credentials
Create a developer account at [Airtel Kenya Developer Portal](https://developers.airtelkenya.com)

## Installation
For Maven:
```xml
<dependency>
    <groupId>io.github.kathukyabrian</groupId>
    <artifactId>airtel</artifactId>
    <version>1.0.1</version>
</dependency>
```
Latest Version: 1.0.1

## Quick Start
### Configuration
- Create a file called `mpesa.properties`
- Place the file on the application's classpath
```properties
country=KE
currency=KES
client-id=
client-secret=
auth-url=https://openapiuat.airtelkenya.com/auth/oauth2/token
payment-url=https://openapiuat.airtelkenya.com/merchant/v1/payments
query-url=https://api.safaricom.co.ke/mpesa/transactionstatus/v1/query
```

### Properties Description
| Property      | Description                                                                                                                  |
|---------------|------------------------------------------------------------------------------------------------------------------------------|
| country       | Country code i.e KE                                                                                                          |
| currency      | Currency code i.e KES                                                                                                        |
| callback-url  | The URL that will receive the callback upon payment completion.                                                              |
| client-id     | Credentials provided by Airtel to authenticate the application.                                                              |
| client-secret | Credentials provided by Airtel to authenticate the application.                                                              |
| auth-url      | The url provided by Daraja to be used to authenticate the application. Expects the __consumer key__ and __consumer secret__. |
| payment-url   | The url provided by Daraja to  be used to initiate USSD push request                                                         |
| query-url     | The url provided by Daraja to be used to query transaction status                                                            |

## Create Mpesa Config
```java
package x.com.config;
import io.github.kathukyabrian.core.ServiceRepository;
import io.github.kathukyabrian.core.factory.ServiceRepositoryFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AirtelConfig {
    private ServiceRepository serviceRepository;
    
    @Bean
    public ServiceRepository getServiceRepository() {
        if(serviceRepository == null){
            serviceRepository = ServiceRepositoryFactory.getServiceRepository();
        }
        return serviceRepository;
    }
    
    public ServiceRepository getInstance() {
        return this.serviceRepository;
    }
}
```

### Usage
- You are ready to use the library.

#### STK push
- on your service method use the __requestPayment()__ method to request payment.

```java
import io.github.kathukyabrian.core.Airtel;
import io.github.kathukyabrian.dto.AirtelSTKResponse;

AirtelSTKResponse response = Airtel.requestPayment(1,"2547xxxxxxxx", "payment description");
```

#### Callback
- in your callback endpoint, use the __handleResult(MpesaResult mpesaResult)__ method to handle your callback

```java
import io.github.kathukyabrian.core.Airtel;
import io.github.kathukyabrian.dto.AirtelSTKResult;
import io.github.kathukyabrian.dto.STKResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/payments")
@RestController
public class PaymentResource {
    
    @PostMapping("/callback")
    public void handleCallback(@RequestBody AirtelSTKResult airtelSTKResult) {
        STKResult result = Airtel.handleResult(mpesaResult);
        // process callback
    }
}
```

## Contributing
Contributions are welcome.
1. Fork the repository
2. Create a feature branch
3. Submit a pull request

## License
Apache 2.0