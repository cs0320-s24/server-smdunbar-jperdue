# Project Details
This server contains two major funcionalities, one which allows users to upload, view, and search CSV files, the other allowing them to query Census Data for Broadband connectivity given a county and state. 

The CSV endpoints are designed to allow one CSV to be uploaded at a time, which is parsed upon upload. Once the CSV, as well as the headers boolean and filepath are saved in the server state, users can utilize the viewcsv and searchcsv, which will send serialized json responses. If no CSV has been loaded via the loadcsv endpoint, searchcsv and viewcsv will fail. Both responses will contain a result field, and if a success response is return it will include a data field with the 2D list containing the result data. If this is a search it will be the search result, and view will return the entire CSV data. Failed responses will contain a message explaining the error. 

The broadband endpoint

# Design Choices
For the CSV endpoints, we developed the handler methods to pull from a given server state that each handler is constructed with. By having all handlers interact with the same state, we can ensure endpoints that rely on others, like search and view requiring a csv be loaded, can throw the necessary errors if certain pre cursor steps have not been taken by the user. Addittionally, we used different classes for success and failure responses for each handler, to accomodate the different kinds of results a success or failure should yield (a data result for success, while a error message for failure). Then, we serialized with a serialize method within each response, allowing each handle method to return a response object that was a standard serialized json. When designing the server state which all csv handlers rely upon, we had the constructor do nothing, allowing for the assignment of values in the load handler. This choice means that the state can only be modified in the load handler, ensuring data is only loaded when the loadcsv endpoint is called, and not in the creation of the server or at any other moment. 

For broadband, 

# Errors/Bugs

# Tests
For testing the CSV endpoints, we largely relied on integration testing to ensure the functionality of our server. There are unit tests which test the parse and search classes to check edge cases and functionality of the search and parse which our csv endpoints are built on, but the majority of our testing is integration testing to check possible interactions with the server. This includes interactions between different handlers, and ensures all errors return a failure response. There are also unit tests examining functionality of the serialize method, ensuring the map provided is returned as a string serialized with the json adapter.

For broadband testing, because the handler requires data from the ACS api, we utilized mock data to avoid api calls when testing. 

# How to
First, run the server and navigate to the host location provided given your port number. 

For CSV endpoints, first use loadcsv to input data into the server state. This can be done by following the input /loadcsv?filepath=desiredpath&headers=boolean, with desired path being your chosen path and headers being true if the csv contains headers and false otherwise. Once data is successfully loaded, one can view the CSV data by following the viewcsv endpoint where the data will be contained in the data field of the response map. Similarly, one can us the search endpoint with the following input /searchcsv?query=query&column=column with query being the item to search for in the rows of the csv and the column field being either an index or column name to search within. Note that the index is an option field, but if provided the search will only yield results where the query is found within that column for every given row. 

For broadband, 
