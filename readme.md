# How to deploy

First, define the following application's properties files.

### jwt.properties
Contains keys that are used to sign JWT tokens

#### Sample file content.
```
jwt.key.merchant = <SOME_UNIQUE_STRONG_KEY>
jwt.key.customer = <SOME_UNIQUE_STRONG_KEY>
jwt.key.admin = <SOME_UNIQUE_STRONG_KEY>
```
Ensure that the keys provided are unique for each user type.

### db.properties
Contains the credential and configs for MySQL Database

#### Sample file content

```
spring.jpa.hibernate.ddl-auto = update
spring.datasource.url = jdbc:mysql://localhost:<PORT_NUMBER>/<DB_NAME>
spring.datasource.username = <DB_USERNAME>
spring.datasource.password = <DB_PASSWORD>
```

### africas-talking-credentials.properties
Has credentials used to access the Africas talking API.

#### Sample file content
```
africastalking.username = <AFRICAS_TALKING_APP_USERNAME>
africastalking.api-key = <AFRICAS_TALKING_API_KEY>
africastalking.sender-id = <YOUR_SMS_SENDER_ID>
```

### mpesa-config.properties
This file contains configurations and credentials for the M-PESA API. [Read the docs here.](https://developer.safaricom.co.ke/Documentation)

#### Sample file content
```
mpesa.c2b.short-code = <C2B_SHORT_CODE>
mpesa.stk.push.consumer-key = <STK_PUSH_CONSUMER_KEY>
mpesa.stk.push.consumer-secret = <STK_PUSH_CONSUMER_SECRET>
mpesa.stk.push.pass-key = <STK_PUSH_PASS_KEY>
mpesa.stk.push.customer-pay-bill-online-name = CustomerPayBillOnline
mpesa.stk.push.callback-url = <SKT_PUSH_PAYMENT_CALLBACK>

mpesa.c2b.validation-consumer-key = <MPESA_C2B_API_CONSUMER_KEY>
mpesa.c2b.validation-consumer-secret = <MPESA_C2B_API_CONSUMER_SECRET>

mpesa.c2b.validation-initiator-name = <M_PESA_C2B_API_USERNAME>
mpesa.c2b.validation-security-password = <M_PESA_C2B_API_PASSWORD>

mpesa.c2b.validation-identifier-type = 4
mpesa.c2b.validation-command-id = TransactionStatusQuery
mpesa.c2b.validation-callback-url = <M_PESA_C2B_TRANSACTION_VALIDATION_CALLBACK_URL>
```

# Configuring the properties files.
Finally, let the Spring application know where to read the external properties from .

In your ``application.properties`` file, import the external config files.
#### Sample configuration

```
spring.config.import=file:/PATH_TO/jwt.properties,\
    file:/PATH_TO/db.properties,\
    file:/PATH_TO/africas-talking-credentials.properties,\
    file:/PATH_TO/mpesa-config.properties
```

Run the code