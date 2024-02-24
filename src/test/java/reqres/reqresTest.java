package reqres;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.JsonSchemaValidator;

public class reqresTest {
    HttpClient clientHttp;
 
    @BeforeEach
    public void setup(){
        System.out.println("Inicio de la Configuracion ");
 
        clientHttp = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
   
    }
     @Test // Consultar los usuarios de la página 2
    public void UsersPage2() throws URISyntaxException, IOException, InterruptedException{
 
        URIBuilder uri = new URIBuilder()
            .setScheme("https")
            .setHost("reqres.in")
            .setPath("/api/users")
            .setParameter("page", "2");
 
        System.out.println("Endpoint: " + uri.build());
 
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(uri.build())
            .build();
 
        HttpResponse<String> response = clientHttp.send(request, HttpResponse.BodyHandlers.ofString());
 
        System.out.println("Response: " + response.body());
 
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"page\":2"));
        String validationSchema = JsonSchemaValidator.validateJsonAgainstSchema(response.body(), "ReqresUsersPageSchema.json");
         System.out.println("Resultado de la validacion del Schema: " + validationSchema);


    }
        @Test // Consultar los datos del usuario 12
        public void Users12Data() throws URISyntaxException, IOException, InterruptedException{
     
            URIBuilder uri = new URIBuilder()
                .setScheme("https")
                .setHost("reqres.in")
                .setPath("/api/users")
                .setParameter("page", "2")
                .addParameter("id", "12");
     
            System.out.println("Endpoint: " + uri.build());
     
            HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri.build())
                .build();
     
            HttpResponse<String> response = clientHttp.send(request, HttpResponse.BodyHandlers.ofString());
     
            System.out.println("Response: " + response.body());
     
            assertTrue(response.body().contains("\"email\":\"rachel.howell@reqres.in\""));
            String validationSchema = JsonSchemaValidator.validateJsonAgainstSchema(response.body(), "ReqresUserSchema.json");
            System.out.println("Resultado de la validacion del Schema: " + validationSchema);
        }

        @Test // Consultar los datos de un usuario inexistente
        public void UserNotFound() throws URISyntaxException, IOException, InterruptedException{
     
            URIBuilder uri = new URIBuilder()
                .setScheme("https")
                .setHost("reqres.in")
                .setPath("/api/users/23");
                
     
            System.out.println("Endpoint: " + uri.build());
     
            HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri.build())
                .build();
     
            HttpResponse<String> response = clientHttp.send(request, HttpResponse.BodyHandlers.ofString());
     
            System.out.println("Response: " + response.body());
     
            assertEquals(404, response.statusCode());
            

        }
        @Test // Consultar el endpoint reqres.in/api/unknown
        public void UnknowPage() throws URISyntaxException, IOException, InterruptedException{
     
            URIBuilder uri = new URIBuilder()
                .setScheme("https")
                .setHost("reqres.in")
                .setPath("/api/unknown");
                
     
            System.out.println("Endpoint: " + uri.build());
     
            HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri.build())
                .build();
     
            HttpResponse<String> response = clientHttp.send(request, HttpResponse.BodyHandlers.ofString());
     
            System.out.println("Response: " + response.body());
     
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("\"page\":1"));
            String validationSchema = JsonSchemaValidator.validateJsonAgainstSchema(response.body(), "ReqresUnknownSchema.json");
            System.out.println("Resultado de la validacion del Schema: " + validationSchema);
        }
        @Test // Consumir el servicio "Delayed Response"
        public void DelayedResponse() throws URISyntaxException, IOException, InterruptedException{
     
            URIBuilder uri = new URIBuilder()
                .setScheme("https")
                .setHost("reqres.in")
                .setPath("/api/users")
                .setParameter("delay", "3");
                
     
            System.out.println("Endpoint: " + uri.build());
     
            HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri.build())
                .build();
     
            HttpResponse<String> response = clientHttp.send(request, HttpResponse.BodyHandlers.ofString());
     
            System.out.println("Response: " + response.body());
     
            assertEquals(HttpStatus.SC_OK, response.statusCode());
            String validationSchema = JsonSchemaValidator.validateJsonAgainstSchema(response.body(), "ReqresDelayedResponseSchema.json");
            System.out.println("Resultado de la validacion del Schema: " + validationSchema);
            
        }
    
}
