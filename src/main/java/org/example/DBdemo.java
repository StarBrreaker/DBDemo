package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBdemo {

    private static final String BASE_URL = "http://localhost:8080"; // Adjust port if necessary

    private static final HttpClient client = HttpClient.newHttpClient();



    private static void addNewUser() throws Exception {
        String formParams = "username=testUser&password=testPass&userType=STAFF";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Add New User Response Status Code: " + postResponse.statusCode());
        System.out.println("Add New User Response Body: " + postResponse.body());
    }


    private static void getAllUsers() throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/all"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("Get All Users Response Status Code: " + getResponse.statusCode());
        System.out.println("Get All Users Response Body: " + getResponse.body());
    }

    private static void removeUserByUsername(String username) throws Exception {
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/remove?username=" + username))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        System.out.println("Remove User Response Status Code: " + deleteResponse.statusCode());
        System.out.println("Remove User Response Body: " + deleteResponse.body());
    }
    private static void registerProfessional(String preferredUsername, String password, String firstName, String lastName,
                                             String dob, String email, String phoneNumber, String mailAddress,
                                             String degree, String institution, String dateOfAward
                                             ) throws Exception {


        // New code to create a professional account request with qualifications included
        JSONObject accountRequestJson = new JSONObject();
        accountRequestJson.put("preferredUsername", preferredUsername);
        accountRequestJson.put("password", password);
        accountRequestJson.put("firstName", firstName);
        accountRequestJson.put("lastName", lastName);
        accountRequestJson.put("dob", dob);
        accountRequestJson.put("email", email);
        accountRequestJson.put("phone", phoneNumber);
        accountRequestJson.put("mailingAddress", mailAddress);
        accountRequestJson.put("degree", degree);
        accountRequestJson.put("institution", institution);
        accountRequestJson.put("dateOfAward", dateOfAward);


        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalAccountRequests/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(accountRequestJson.toString()))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Create Professional Account Request Response Status Code: " + postResponse.statusCode());
        System.out.println("Create Professional Account Request Response Body: " + postResponse.body());
    }

    private static void getAllProfessionalRequests() throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalAccountRequests/all"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("All Professional Account Requests: " + response.body());
    }
    private static String retrieveProfessionalRequest(Long requestId) throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalAccountRequests/" + requestId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Selected Professional Account Request Details: " + response.body());
        return response.body(); // Return the response body for further processing
    }
    private static void createProfessional(String requestDetails) throws Exception {
        JSONObject obj = new JSONObject(requestDetails);

        // Extract data from JSON
        String username = obj.getString("preferredUsername");
        String password = obj.getString("password");
        String firstName = obj.getString("firstName");
        String lastName = obj.getString("lastName");
        String dob = obj.getString("dob");
        String email = obj.getString("email");
        String phoneNumber = obj.getString("phone");
        String mailAddress = obj.getString("mailingAddress");
        String degree = obj.getString("degree");
        String institution = obj.getString("institution");
        String dateOfAward = obj.getString("dateOfAward");

        // Now use the extracted data to create a professional account
        // Assuming you have a method to add a professional user, similar to the commented-out registerProfessional method
        //registerProfessional(username, password, firstName, lastName, dob, email, phoneNumber, mailAddress, degree, institution, dateOfAward);
        String formParams = "username=" + username +"&password="+ password +"&userType=PROFESSIONAL";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Add New User Response Status Code: " + postResponse.statusCode());
        System.out.println("Add New User Response Body: " + postResponse.body());

        String formParams1 = "username=" + username +"&firstName=" + firstName + "&lastName=" + lastName
                +"&dob=" + dob + "&email=" + email + "&phoneNumber=" + phoneNumber + "&mailAddress="
                + mailAddress + "&degree=" + degree + "&institution=" + institution + "&dateOfAward=" + dateOfAward;
        HttpRequest postRequest1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professional/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams1))
                .build();

        HttpResponse<String> postResponse1 = client.send(postRequest1, BodyHandlers.ofString());
        System.out.println("Add New Professional Response Status Code: " + postResponse.statusCode());
        System.out.println("Add New Professional Response Body: " + postResponse1.body());

        // Add payment for the professional
        LocalDate previousBillingDate = LocalDate.now(); // Today's date
        LocalDate nextBillingDate = previousBillingDate.plusDays(30); // 30 days from today

        // Format dates to a string in the format "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedPreviousBillingDate = previousBillingDate.format(formatter);
        String formattedNextBillingDate = nextBillingDate.format(formatter);

        // Now include these formatted dates in JSON
        String paymentJson = String.format("{\"paymentID\":\"%s\", \"subscriptionSituation\": true, \"paymentBalance\": 50.00, \"nextBillingDate\": \"%s\", " +
                "\"previousBillingDate\": \"%s\", \"paymentStatus\": \"Active\"}", username, formattedNextBillingDate, formattedPreviousBillingDate);

        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalPayments/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(paymentJson))
                .build();

        HttpResponse<String> postResponse2 = client.send(postRequest2, BodyHandlers.ofString());
        System.out.println("Add New Professional Payment Response Status Code: " + postResponse2.statusCode());
        System.out.println("Add New Professional Payment Response Body: " + postResponse2.body());
    }
    private static void createNewStaffAccount(String username, String password, String firstName, String lastName, String dob, String email, String phoneNumber) throws Exception {
        // Create a new user with userType STAFF
        String userParams = String.format("username=%s&password=%s&userType=%s", username, password, "STAFF");
        HttpRequest addUserRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(userParams))
                .build();

        HttpResponse<String> addUserResponse = client.send(addUserRequest, BodyHandlers.ofString());
        System.out.println("Add New Staff User Response: " + addUserResponse.body());

        // Add staff-specific details
        String staffParams = String.format("username=%s&firstName=%s&lastName=%s&dob=%s&email=%s&phoneNumber=%s", username, firstName, lastName, dob, email, phoneNumber);
        HttpRequest addStaffRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/staff/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(staffParams))
                .build();

        HttpResponse<String> addStaffResponse = client.send(addStaffRequest, BodyHandlers.ofString());
        System.out.println("Add Staff Details Response: " + addStaffResponse.body());
    }



    /*
    private static void registerProfessional (String username, String password, String firstName, String lastName,
                                                String dob, String email, String phoneNumber, String mailAddress,
                                                String degree, String institution, String dateOfAward)throws Exception {
        String formParams = "username=" + username +"&password="+ password +"&userType=PROFESSIONAL";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Add New User Response Status Code: " + postResponse.statusCode());
        System.out.println("Add New User Response Body: " + postResponse.body());

        String formParams1 = "username=" + username +"&firstName=" + firstName + "&lastName=" + lastName
                +"&dob=" + dob + "&email=" + email + "&phoneNumber=" + phoneNumber + "&mailAddress="
                + mailAddress + "&degree=" + degree + "&institution=" + institution + "&dateOfAward=" + dateOfAward;
        HttpRequest postRequest1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professional/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams1))
                .build();

        HttpResponse<String> postResponse1 = client.send(postRequest1, BodyHandlers.ofString());
        System.out.println("Add New Professional Response Status Code: " + postResponse.statusCode());
        System.out.println("Add New Professional Response Body: " + postResponse1.body());

        // Add payment for the professional
        LocalDate previousBillingDate = LocalDate.now(); // Today's date
        LocalDate nextBillingDate = previousBillingDate.plusDays(30); // 30 days from today

        // Format dates to a string in the format "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedPreviousBillingDate = previousBillingDate.format(formatter);
        String formattedNextBillingDate = nextBillingDate.format(formatter);

        // Now include these formatted dates in JSON
        String paymentJson = String.format("{\"paymentID\":\"%s\", \"subscriptionSituation\": true, \"paymentBalance\": 50.00, \"nextBillingDate\": \"%s\", " +
                "\"previousBillingDate\": \"%s\", \"paymentStatus\": \"Active\"}", username, formattedNextBillingDate, formattedPreviousBillingDate);

        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalPayments/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(paymentJson))
                .build();

        HttpResponse<String> postResponse2 = client.send(postRequest2, BodyHandlers.ofString());
        System.out.println("Add New Professional Payment Response Status Code: " + postResponse2.statusCode());
        System.out.println("Add New Professional Payment Response Body: " + postResponse2.body());
    }
    */
    private static void registerEmployer(String preferredUsername, String password, String companyName, String registrationNumber,
                                         String industry, String size, String primaryContactFirstName, String primaryContactLastName,
                                         String primaryContactEmail, String primaryContactPhoneNumber, String primaryContactMailAddress,
                                         String websiteLink) throws Exception {
        // New code to create an employer account request
        String accountRequestJson = String.format("{\"preferredUsername\":\"%s\", \"password\":\"%s\", \"companyName\":\"%s\", " +
                        "\"registrationNumber\":\"%s\", \"industry\":\"%s\", \"size\":\"%s\", \"primaryContactFirstName\":\"%s\", " +
                        "\"primaryContactLastName\":\"%s\", \"primaryContactEmail\":\"%s\", \"primaryContactPhoneNumber\":\"%s\", " +
                        "\"primaryContactMailAddress\":\"%s\", \"websiteLink\":\"%s\"}",
                preferredUsername, password, companyName, registrationNumber, industry, size, primaryContactFirstName,
                primaryContactLastName, primaryContactEmail, primaryContactPhoneNumber, primaryContactMailAddress, websiteLink);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerAccountRequests/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(accountRequestJson))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Create Employer Account Request Response Status Code: " + postResponse.statusCode());
        System.out.println("Create Employer Account Request Response Body: " + postResponse.body());
    }
    private static void getAllEmployerRequests() throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerAccountRequests/all"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("All Employer Account Requests: " + response.body());
    }
    private static String retrieveEmployerRequest(Long requestId) throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerAccountRequests/" + requestId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Selected Employer Account Request Details: " + response.body());
        return response.body(); // Return the response body for further processing
    }
    private static void createEmployer(String requestDetails) throws Exception {
        JSONObject obj = new JSONObject(requestDetails);

        // Extract data from JSON
        String username = obj.getString("preferredUsername");
        String password = obj.getString("password");
        String companyName = obj.getString("companyName");
        String registrationNumber = obj.getString("registrationNumber");
        String industry = obj.getString("industry");
        String size = obj.getString("size");
        String primaryContactFirstName = obj.getString("primaryContactFirstName");
        String primaryContactLastName = obj.getString("primaryContactLastName");
        String primaryContactEmail = obj.getString("primaryContactEmail");
        String primaryContactPhoneNumber = obj.getString("primaryContactPhoneNumber");
        String primaryContactMailAddress = obj.getString("primaryContactMailAddress");
        String websiteLink = obj.getString("websiteLink");


        String formParams = "username=" + username +"&password="+ password +"&userType=EMPLOYER";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Add New User Response Status Code: " + postResponse.statusCode());
        System.out.println("Add New User Response Body: " + postResponse.body());

        String formParams1 = "username=" + username +"&companyName=" + companyName + "&registrationNumber=" + registrationNumber
                +"&industry=" + industry + "&size=" + size + "&primaryContactFirstName=" + primaryContactFirstName + "&primaryContactLastName="
                + primaryContactLastName + "&primaryContactEmail=" + primaryContactEmail + "&primaryContactPhoneNumber=" + primaryContactPhoneNumber
                + "&primaryContactMailAddress=" + primaryContactMailAddress + "&websiteLink=" + websiteLink;
        System.out.println(formParams1);
        HttpRequest postRequest1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employer/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams1))
                .build();

        HttpResponse<String> postResponse1 = client.send(postRequest1, BodyHandlers.ofString());
        System.out.println("Add New Employer Response Status Code: " + postResponse1.statusCode());
        System.out.println("Add New Employer Response Body: " + postResponse1.body());

        // Add payment for the employer
        LocalDate previousBillingDate = LocalDate.now(); // Today's date
        LocalDate nextBillingDate = previousBillingDate.plusDays(30); // 30 days from today

        // Format dates to a string in the format "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedPreviousBillingDate = previousBillingDate.format(formatter);
        String formattedNextBillingDate = nextBillingDate.format(formatter);

        // Now include these formatted dates in JSON
        String paymentJson = String.format("{\"paymentID\":\"%s\", \"subscriptionSituation\": true, \"paymentBalance\": 50.00, \"nextBillingDate\": \"%s\", " +
                "\"previousBillingDate\": \"%s\", \"paymentStatus\": \"Active\"}", username, formattedNextBillingDate, formattedPreviousBillingDate);

        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerPayments/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(paymentJson))
                .build();

        HttpResponse<String> postResponse2 = client.send(postRequest2, BodyHandlers.ofString());
        System.out.println("Add New Employer Payment Response Status Code: " + postResponse2.statusCode());
        System.out.println("Add New Employer Payment Response Body: " + postResponse2.body());
    }


    /*
    private static void registerEmployer (String username, String password, String companyName, String registrationNumber,
                                              String industry, String size, String primaryContactFirstName, String primaryContactLastName,
                                              String primaryContactEmail, String primaryContactPhoneNumber, String primaryContactMailAddress,
                                              String websiteLink )throws Exception {
        String formParams = "username=" + username +"&password="+ password +"&userType=EMPLOYER";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Add New User Response Status Code: " + postResponse.statusCode());
        System.out.println("Add New User Response Body: " + postResponse.body());

        String formParams1 = "username=" + username +"&companyName=" + companyName + "&registrationNumber=" + registrationNumber
                +"&industry=" + industry + "&size=" + size + "&primaryContactFirstName=" + primaryContactFirstName + "&primaryContactLastName="
                + primaryContactLastName + "&primaryContactEmail=" + primaryContactEmail + "&primaryContactPhoneNumber=" + primaryContactPhoneNumber
                + "&primaryContactMailAddress=" + primaryContactMailAddress + "&websiteLink=" + websiteLink;
        System.out.println(formParams1);
        HttpRequest postRequest1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employer/add"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams1))
                .build();

        HttpResponse<String> postResponse1 = client.send(postRequest1, BodyHandlers.ofString());
        System.out.println("Add New Employer Response Status Code: " + postResponse1.statusCode());
        System.out.println("Add New Employer Response Body: " + postResponse1.body());

        // Add payment for the employer
        LocalDate previousBillingDate = LocalDate.now(); // Today's date
        LocalDate nextBillingDate = previousBillingDate.plusDays(30); // 30 days from today

        // Format dates to a string in the format "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedPreviousBillingDate = previousBillingDate.format(formatter);
        String formattedNextBillingDate = nextBillingDate.format(formatter);

        // Now include these formatted dates in JSON
        String paymentJson = String.format("{\"paymentID\":\"%s\", \"subscriptionSituation\": true, \"paymentBalance\": 50.00, \"nextBillingDate\": \"%s\", " +
                "\"previousBillingDate\": \"%s\", \"paymentStatus\": \"Active\"}", username, formattedNextBillingDate, formattedPreviousBillingDate);

        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerPayments/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(paymentJson))
                .build();

        HttpResponse<String> postResponse2 = client.send(postRequest2, BodyHandlers.ofString());
        System.out.println("Add New Employer Payment Response Status Code: " + postResponse2.statusCode());
        System.out.println("Add New Employer Payment Response Body: " + postResponse2.body());
    }
    */
    private static void updateProfessional(String professionalID, String firstName, String lastName,
                                           String dob, String email, String phoneNumber, String mailAddress,
                                           String degree, String institution, String dateOfAward) throws Exception {
        // Prepare form parameters
        String formParams = "firstName=" + firstName +
                "&lastName=" + lastName +
                "&dob=" + dob +
                "&email=" + email +
                "&phoneNumber=" + phoneNumber +
                "&mailAddress=" + mailAddress +
                "&degree=" + degree +
                "&institution=" + institution +
                "&dateOfAward=" + dateOfAward;

        // Encode the form parameters as needed to ensure they are correctly interpreted by the server
        // Note: You might need to URL encode the parameters if they include spaces or special characters

        // Create HTTP PUT request
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professional/" + professionalID)) // Use the professionalID to specify the update endpoint
                .header("Content-Type", "application/x-www-form-urlencoded")
                .PUT(HttpRequest.BodyPublishers.ofString(formParams))
                .build();

        // Send the request and get the response
        HttpResponse<String> putResponse = client.send(putRequest, HttpResponse.BodyHandlers.ofString());

        // Print response details
        System.out.println("Update Professional Response Status Code: " + putResponse.statusCode());
        System.out.println("Update Professional Response Body: " + putResponse.body());
    }


    private static void updateEmployer(String employerID, String companyName, String registrationNumber,
                                       String industry, String size, String primaryContactFirstName, String primaryContactLastName,
                                       String primaryContactEmail, String primaryContactPhoneNumber, String primaryContactMailAddress,
                                       String websiteLink) throws Exception {
        // Prepare form parameters
        String formParams = "companyName=" + companyName +
                "&registrationNumber=" + registrationNumber +
                "&industry=" + industry +
                "&size=" + size +
                "&primaryContactFirstName=" + primaryContactFirstName +
                "&primaryContactLastName=" + primaryContactLastName +
                "&primaryContactEmail=" + primaryContactEmail +
                "&primaryContactPhoneNumber=" + primaryContactPhoneNumber +
                "&primaryContactMailAddress=" + primaryContactMailAddress +
                "&websiteLink=" + websiteLink;

        // Create HTTP PUT request
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employer/" +  employerID )) // Ensure the URL is correct
                .header("Content-Type", "application/x-www-form-urlencoded")
                .PUT(HttpRequest.BodyPublishers.ofString(formParams))
                .build();

        // Send the request and get the response
        HttpResponse<String> putResponse = client.send(putRequest, HttpResponse.BodyHandlers.ofString());

        // Print response details
        System.out.println("Update Employer Response Status Code: " + putResponse.statusCode());
        System.out.println("Update Employer Response Body: " + putResponse.body());
    }

    private static void processProfessionalPayment(String paymentID, double amountPaid) throws Exception {
        // Convert the amount paid to JSON
        String json = "{\"amountPaid\":" + amountPaid + "}";

        HttpRequest patchRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalPayments/" + paymentID + "/processPayment"))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> patchResponse = client.send(patchRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Process Payment Response Status Code: " + patchResponse.statusCode());
        System.out.println("Process Payment Response Body: " + patchResponse.body());
    }

    private static void processEmployerPayment(String paymentID, double amountPaid) throws Exception {
        // Convert the amount paid to JSON
        String json = "{\"amountPaid\":" + amountPaid + "}";

        HttpRequest patchRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerPayments/" + paymentID + "/processPayment"))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> patchResponse = client.send(patchRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Process Payment Response Status Code: " + patchResponse.statusCode());
        System.out.println("Process Payment Response Body: " + patchResponse.body());
    }

    private static void updatePassword(String username, String oldPassword, String newPassword) throws Exception {
        String formParams = "username=" + username + "&oldPassword=" + oldPassword + "&newPassword=" + newPassword;

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/updatePassword"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Update Password Response: " + postResponse.body());
    }

    private static void login(String username, String password) throws Exception {
        String formParams = "username=" + username + "&password=" + password;

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(formParams))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Login Response: " + postResponse.body());
    }

    private static void requestProfessionalAccountDeletion(String username, String status) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTimestamp = now.format(formatter);

        String requestJson = String.format("{\"username\":\"%s\", \"status\":\"%s\", \"timestamp\":\"%s\"}",
                username, status, formattedTimestamp);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalAccountDeletionRequests/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Request Professional Account Deletion Response Status Code: " + postResponse.statusCode());
        System.out.println("Request Professional Account Deletion Response Body: " + postResponse.body());
    }

    private static void requestEmployerAccountDeletion(String username, String status) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTimestamp = now.format(formatter);

        String requestJson = String.format("{\"username\":\"%s\", \"status\":\"%s\", \"timestamp\":\"%s\"}",
                username, status, formattedTimestamp);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerAccountDeletionRequests/add"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("Request Employer Account Deletion Response Status Code: " + postResponse.statusCode());
        System.out.println("Request Employer Account Deletion Response Body: " + postResponse.body());
    }

    private static void getAllProfessionalDeletionRequests() throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalAccountDeletionRequests/all"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("Get All Deletion Requests Response Status Code: " + getResponse.statusCode());
        System.out.println("Get All Deletion Requests Response Body: " + getResponse.body());
    }
    private static void retrieveProfessionalDeletionRequest(Long requestId) throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professionalAccountDeletionRequests/" + requestId))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("Retrieve Deletion Request Response Status Code: " + getResponse.statusCode());
        System.out.println("Retrieve Deletion Request Response Body: " + getResponse.body());
    }
    private static void deleteProfessionalByUsername(String username) throws Exception {
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/professional/" + username)) // Use path variable for deletion
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Delete Professional Response Status Code: " + deleteResponse.statusCode());
        System.out.println("Delete Professional Response Body: " + deleteResponse.body());
    }
    private static void deleteEmployerByUsername(String username) throws Exception {
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employer/" + username)) // Use path variable for deletion
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Delete Professional Response Status Code: " + deleteResponse.statusCode());
        System.out.println("Delete Professional Response Body: " + deleteResponse.body());
    }

    private static void deleteUserByUsername(String username) throws Exception {
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/remove?username=" + username))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        System.out.println("Delete User Response Status Code: " + deleteResponse.statusCode());
        System.out.println("Delete User Response Body: " + deleteResponse.body());
    }
    private static void getAllEmployerDeletionRequests() throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerAccountDeletionRequests/all"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("Get All Employer Deletion Requests Response Status Code: " + getResponse.statusCode());
        System.out.println("Get All Employer Deletion Requests Response Body: " + getResponse.body());
    }

    private static void retrieveEmployerDeletionRequest(Long requestId) throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/employerAccountDeletionRequests/" + requestId))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("Retrieve Employer Deletion Request Response Status Code: " + getResponse.statusCode());
        System.out.println("Retrieve Employer Deletion Request Response Body: " + getResponse.body());
    }

    public static void main(String[] args) throws Exception {
        //addNewUser();
        //getAllUsers();
        //removeUserByUsername("testUser"); // Adjust username as necessary

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the function number (0 to exit): ");
            int function = sc.nextInt();
            sc.nextLine();
            switch (function) {
                case 1: {
                    System.out.println("Registering a new professional");
                    System.out.println("Enter preferred username: ");
                    String preferredUsername = sc.nextLine();
                    System.out.println("Enter password: ");
                    String password = sc.nextLine();
                    System.out.println("Enter First Name: ");
                    String firstName = sc.nextLine();
                    System.out.println("Enter Last Name: ");
                    String lastName = sc.nextLine();
                    //Example DOB input: "1997-11-17"
                    System.out.println("Enter Date of Birth: (e.g., 1997-11-17)");
                    String dob = sc.nextLine();
                    System.out.println("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.println("Enter Phone Number: ");
                    String phoneNumber = sc.nextLine();
                    System.out.println("Enter Mailing Address: ");
                    String mailAddress = sc.nextLine();
                    System.out.println("Enter Degree: ");
                    String degree = sc.nextLine();
                    System.out.println("Enter Institution: ");
                    String institution = sc.nextLine();
                    //Example Date of Award input: 2025-05-01
                    System.out.println("Enter Date of Award: ");
                    String dateOfAward = sc.nextLine();


                    registerProfessional(preferredUsername, password, firstName, lastName, dob, email, phoneNumber, mailAddress, degree, institution, dateOfAward);
                    break;
                }
                case 2: {
                    System.out.println("Register a new employer");
                    System.out.println("Enter username: ");
                    String preferredUsername  = sc.nextLine();
                    System.out.println("Enter password: ");
                    String password = sc.nextLine();
                    System.out.println("Enter Company Name: ");
                    String companyName = sc.nextLine();
                    System.out.println("Enter Registration Number: ");
                    String registrationNumber = sc.nextLine();
                    System.out.println("Enter Industry: ");
                    String industry = sc.nextLine();
                    System.out.println("Enter Size: ");
                    String size = sc.nextLine();
                    System.out.println("Enter Primary Contact First Name: ");
                    String primaryContactFirstName = sc.nextLine();
                    System.out.println("Enter Primary Contact Last Name: ");
                    String primaryContactLastName = sc.nextLine();
                    System.out.println("Enter Primary Contact Email: ");
                    String primaryContactEmail = sc.nextLine();
                    System.out.println("Enter Primary Contact Phone Number: ");
                    String primaryContactPhoneNumber = sc.nextLine();
                    //Example Date of Award input: 2025-05-01
                    System.out.println("Enter Primary Contact Mailing Address:  ");
                    String primaryContactMailAddress = sc.nextLine();
                    System.out.println("Enter Website Link: ");
                    String websiteLink = sc.nextLine();
                    registerEmployer(preferredUsername , password, companyName, registrationNumber, industry,
                            size, primaryContactFirstName, primaryContactLastName, primaryContactEmail, primaryContactPhoneNumber,
                            primaryContactMailAddress, websiteLink);
                    break;
                }
                case 3: {
                    System.out.println("Update professional information:");
                    System.out.println("Enter professional ID: ");
                    String professionalID = sc.nextLine();
                    System.out.println("Enter First Name: ");
                    String firstName = sc.nextLine();
                    System.out.println("Enter Last Name: ");
                    String lastName = sc.nextLine();
                    System.out.println("Enter Date of Birth (e.g., 1997-11-17): ");
                    String dob = sc.nextLine();
                    System.out.println("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.println("Enter Phone Number: ");
                    String phoneNumber = sc.nextLine();
                    System.out.println("Enter Mailing Address: ");
                    String mailAddress = sc.nextLine();
                    System.out.println("Enter Degree: ");
                    String degree = sc.nextLine();
                    System.out.println("Enter Institution: ");
                    String institution = sc.nextLine();
                    System.out.println("Enter Date of Award (e.g., 2025-05-01): ");
                    String dateOfAward = sc.nextLine();

                    updateProfessional(professionalID, firstName, lastName, dob, email, phoneNumber, mailAddress, degree, institution, dateOfAward);
                    break;
                }
                case 4: {
                    System.out.println("Modify a employer information: ");
                    System.out.println("Enter employerID: ");
                    String employerID = sc.nextLine();
                    System.out.println("Enter Company Name: ");
                    String companyName = sc.nextLine();
                    System.out.println("Enter Registration Number: ");
                    String registrationNumber = sc.nextLine();
                    System.out.println("Enter Industry: ");
                    String industry = sc.nextLine();
                    System.out.println("Enter Size: ");
                    String size = sc.nextLine();
                    System.out.println("Enter Primary Contact First Name: ");
                    String primaryContactFirstName = sc.nextLine();
                    System.out.println("Enter Primary Contact Last Name: ");
                    String primaryContactLastName = sc.nextLine();
                    System.out.println("Enter Primary Contact Email: ");
                    String primaryContactEmail = sc.nextLine();
                    System.out.println("Enter Primary Contact Phone Number: ");
                    String primaryContactPhoneNumber = sc.nextLine();
                    //Example Date of Award input: 2025-05-01
                    System.out.println("Enter Primary Contact Mailing Address:  ");
                    String primaryContactMailAddress = sc.nextLine();
                    System.out.println("Enter Website Link: ");
                    String websiteLink = sc.nextLine();
                    updateEmployer(employerID, companyName, registrationNumber, industry,
                            size, primaryContactFirstName, primaryContactLastName, primaryContactEmail, primaryContactPhoneNumber,
                            primaryContactMailAddress, websiteLink);
                    break;
                }
                case 7: {
                    System.out.println("Processing a payment for a professional:");
                    System.out.println("Enter Professional Payment ID: ");
                    String paymentID = sc.nextLine();
                    System.out.println("Enter Amount Paid: ");
                    double amountPaid = sc.nextDouble();
                    sc.nextLine(); // Consume the newline left-over

                    processProfessionalPayment(paymentID, amountPaid);
                    break;
                }
                case 8: {
                    System.out.println("Processing a payment for a employer:");
                    System.out.println("Enter Employer Payment ID: ");
                    String paymentID = sc.nextLine();
                    System.out.println("Enter Amount Paid: ");
                    double amountPaid = sc.nextDouble();
                    sc.nextLine(); // Consume the newline left-over

                    processEmployerPayment(paymentID, amountPaid);
                    break;
                }
                case 9: {
                    System.out.println("Updating professional's password with login verification");
                    System.out.println("Enter username: ");
                    String username = sc.nextLine();
                    System.out.println("Enter current password: ");
                    String currentPassword = sc.nextLine();

                    // Attempt to log in with the current password
                    System.out.println("Attempting to log in with current password...");
                    login(username, currentPassword); // This will print the login attempt result

                    System.out.println("Enter new password: ");
                    String newPassword = sc.nextLine();

                    // Attempt to update the professional's password
                    updatePassword(username, currentPassword, newPassword); // This will print the update attempt result

                    // Ask for username and new password again for verification
                    System.out.println("Password update successful. Please enter your username and new password again to verify login:");
                    System.out.println("Enter username: ");
                    String verifyUsername = sc.nextLine();
                    System.out.println("Enter new password: ");
                    String verifyNewPassword = sc.nextLine();

                    // Attempt to log in with the new password
                    System.out.println("Attempting to log in with new password...");
                    login(verifyUsername, verifyNewPassword); // This will print the new login attempt result
                    break;
                }
                case 10: {
                    System.out.println("Updating employer's password with login verification");
                    System.out.println("Enter username: ");
                    String username = sc.nextLine();
                    System.out.println("Enter current password: ");
                    String currentPassword = sc.nextLine();

                    // Attempt to log in with the current password
                    System.out.println("Attempting to log in with current password...");
                    login(username, currentPassword); // This will print the login attempt result

                    System.out.println("Enter new password: ");
                    String newPassword = sc.nextLine();

                    // Attempt to update the professional's password
                    updatePassword(username, currentPassword, newPassword); // This will print the update attempt result

                    // Ask for username and new password again for verification
                    System.out.println("Password update successful. Please enter your username and new password again to verify login:");
                    System.out.println("Enter username: ");
                    String verifyUsername = sc.nextLine();
                    System.out.println("Enter new password: ");
                    String verifyNewPassword = sc.nextLine();

                    // Attempt to log in with the new password
                    System.out.println("Attempting to log in with new password...");
                    login(verifyUsername, verifyNewPassword); // This will print the new login attempt result
                    break;
                }
                case 11: {
                    System.out.println("Requesting professional account deletion");
                    System.out.println("Enter username: ");
                    String username = sc.nextLine();
                    String status = "PENDING"; // Assuming the initial status is always "PENDING"

                    requestProfessionalAccountDeletion(username, status);
                    break;
                }
                case 12: { // Assuming 12 is the next available number
                    System.out.println("Requesting employer account deletion");
                    System.out.println("Enter username: ");
                    String username = sc.nextLine();
                    String status = "PENDING"; // Assuming the initial status is always "PENDING"

                    requestEmployerAccountDeletion(username, status);
                    break;
                }
                case 13: {
                    System.out.println("Updating staff's password with login verification");
                    System.out.println("Enter username: ");
                    String username = sc.nextLine();
                    System.out.println("Enter current password: ");
                    String currentPassword = sc.nextLine();

                    // Attempt to log in with the current password
                    System.out.println("Attempting to log in with current password...");
                    login(username, currentPassword); // This will print the login attempt result

                    System.out.println("Enter new password: ");
                    String newPassword = sc.nextLine();

                    // Attempt to update the professional's password
                    updatePassword(username, currentPassword, newPassword); // This will print the update attempt result

                    // Ask for username and new password again for verification
                    System.out.println("Password update successful. Please enter your username and new password again to verify login:");
                    System.out.println("Enter username: ");
                    String verifyUsername = sc.nextLine();
                    System.out.println("Enter new password: ");
                    String verifyNewPassword = sc.nextLine();

                    // Attempt to log in with the new password
                    System.out.println("Attempting to log in with new password...");
                    login(verifyUsername, verifyNewPassword); // This will print the new login attempt result
                    break;
                }
                case 14: {
                    System.out.println("Processing a new professional account request...");

                    // Step 1: Retrieve all professional account requests
                    System.out.println("Retrieving all professional account requests...");
                    getAllProfessionalRequests();

                    // Prompt the staff member to enter the ID of the request they want to process
                    System.out.println("Enter the request ID of the professional account request you want to process: ");
                    Long requestId = sc.nextLong();
                    sc.nextLine(); // Consume the newline left-over

                    // Step 2: Retrieve the specific professional account request
                    System.out.println("Retrieving professional account request details for ID: " + requestId);
                    String requestDetails = retrieveProfessionalRequest(requestId);

                    // Step 3: Create a professional account based on the retrieved request
                    System.out.println("Creating a professional account based on the request...");
                    createProfessional(requestDetails);

                    // Optionally, retrieve and display the updated Users table to show the new account
                    System.out.println("Retrieving updated Users table...");
                    getAllUsers();
                    break;
                }
                case 15: {
                    System.out.println("Processing a new employer account request...");

                    // Step 1: Retrieve all employer account requests
                    System.out.println("Retrieving all employer account requests...");
                    getAllEmployerRequests(); // This method needs to be implemented

                    // Prompt the staff member to enter the ID of the request they want to process
                    System.out.println("Enter the request ID of the employer account request you want to process: ");
                    Long requestId = sc.nextLong();
                    sc.nextLine(); // Consume the newline left-over

                    // Step 2: Retrieve the specific employer account request
                    System.out.println("Retrieving employer account request details for ID: " + requestId);
                    String requestDetails = retrieveEmployerRequest(requestId); // This method needs to be implemented

                    // Step 3: Create an employer account based on the retrieved request
                    System.out.println("Creating an employer account based on the request...");
                    createEmployer(requestDetails); // This method needs to be implemented

                    // Optionally, retrieve and display the updated Users table to show the new account
                    System.out.println("Retrieving updated Users table...");
                    getAllUsers();
                    break;
                }



                case 16: {
                    System.out.println("Retrieving all professional account deletion requests...");
                    getAllProfessionalDeletionRequests();

                    // Assuming the response body includes request IDs and usernames
                    System.out.println("Enter the request ID of the deletion request you want to process: ");
                    Long requestId = sc.nextLong();
                    sc.nextLine(); // Consume the newline

                    // Retrieve the specific deletion request for more details
                    retrieveProfessionalDeletionRequest(requestId);

                    // Ask if the staff member wants to delete the user associated with the request
                    System.out.println("Do you want to proceed with deleting the user associated with this request? (yes/no): ");
                    String decision = sc.nextLine();

                    if ("yes".equalsIgnoreCase(decision)) {
                        System.out.println("Enter the username associated with this deletion request to delete: ");
                        String username = sc.nextLine();
                        deleteProfessionalByUsername(username);
                        deleteUserByUsername(username);

                        // Optionally, show the updated Users table by retrieving all users
                        getAllUsers();
                    } else {
                        System.out.println("Deletion request processing aborted.");
                    }
                    break;
                }
                case 17: {
                    System.out.println("Retrieving all employer account deletion requests...");
                    getAllEmployerDeletionRequests();

                    System.out.println("Enter the request ID of the deletion request you want to process: ");
                    Long requestId = sc.nextLong();
                    sc.nextLine(); // Consume the newline

                    retrieveEmployerDeletionRequest(requestId);

                    System.out.println("Do you want to proceed with deleting the employer account associated with this request? (yes/no): ");
                    String decision = sc.nextLine();

                    if ("yes".equalsIgnoreCase(decision)) {
                        System.out.println("Enter the username associated with this deletion request to delete: ");
                        String username = sc.nextLine();
                        deleteEmployerByUsername(username); // Assuming a similar method exists for employers
                        deleteUserByUsername(username);

                        // Optionally, show the updated Users table by retrieving all users
                        getAllUsers();
                    } else {
                        System.out.println("Deletion request processing aborted.");
                    }
                    break;
                }

                case 19: {
                    System.out.println("Creating a new staff account...");
                    // Prompt for staff details
                    System.out.println("Enter username: ");
                    String username = sc.nextLine();
                    System.out.println("Enter password: ");
                    String password = sc.nextLine();
                    System.out.println("Enter First Name: ");
                    String firstName = sc.nextLine();
                    System.out.println("Enter Last Name: ");
                    String lastName = sc.nextLine();
                    System.out.println("Enter Date of Birth (YYYY-MM-DD): ");
                    String dob = sc.nextLine();
                    System.out.println("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.println("Enter Phone Number: ");
                    String phoneNumber = sc.nextLine();

                    // Create the staff account
                    createNewStaffAccount(username, password, firstName, lastName, dob, email, phoneNumber);

                    // Display the updated Users table
                    System.out.println("Displaying updated Users table...");
                    getAllUsers();
                    break;
                }



                default:
                    System.out.println("Exiting!");
                    System.exit(0);
                    break;
            }
        }


    }
}