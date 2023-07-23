# Data Transformation Application


**Description**

The Data Transformation Application is a Java-based application that performs data transformations on CSV files. It takes input CSV files, applies various data transformations based on configurable rules, and generates output JSON data that can be sent to a hypothetical in-house REST API.


**Data Transformation**


The data transformation process is divided into three main components:

1. Data Transformation Service: This service is responsible for reading the input CSV files, applying the data transformation rules, and generating the output JSON data.

3. Data Transformation Strategies: These are individual classes that handle specific data transformation rules as described in the document and performing the data validations and formatting given in dynamic_configuration file.
   For example, the EmployeeCodeTransformer class generates employee codes for new hires, and the RegularTransformer class handles updates to existing employee data.

4. Dynamic Configuration: Configurations related to date formatting and file paths are added to application properties files. These properties allow for easy modification and customization of the data transformation process without changing the code.

**API Endpoint**

The output JSON data is sent to a hypothetical in-house REST API endpoint. Modify the endpoint URL in the AssignmentApplication class to match the actual API endpoint.

**How to Run**

1. Clone the repository..

2. Set up any required dependencies and environment configurations.

3. Run the application using the AssignmentApplication class's main method.
