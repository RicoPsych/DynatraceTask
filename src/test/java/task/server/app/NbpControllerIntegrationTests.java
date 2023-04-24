package task.server.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NbpControllerIntegrationTests {
    @Value(value="${local.server.port}")
    private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    @Test
    public void getAverageExchangeRate_request_return_NotFound() {
        //Arrange        
        String uri = "http://localhost:" + port + "/exchanges/GBP/2020-11-11";
        //Act
        ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
        //Assert        
        Assertions.assertEquals(response.getStatusCode(),HttpStatusCode.valueOf(404)); //Not Found
    }

    @Test
    public void getAverageExchangeRate_Request_return_Value() {
        //Arrange        
        String uri = "http://localhost:" + port + "/exchanges/GBP/2020-11-10";
        //Act
        ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
        //Assert        
        Assertions.assertEquals(response.getStatusCode(),HttpStatusCode.valueOf(200)); //Ok
    }

    @Test
    public void getMinMaxValues_request_return_BadRequest() {
        //Arrange        
        String uri = "http://localhost:" + port + "/exchanges/GBP/minmax/256";
        //Act
        ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
        //Assert        
        Assertions.assertEquals(response.getStatusCode(),HttpStatusCode.valueOf(400)); //Bad Request
    }

    @Test
    public void getMinMaxValues_Request_return_Value() {
        //Arrange        
        String uri = "http://localhost:" + port + "/exchanges/GBP/minmax/10";
        //Act
        ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
        //Assert        
        Assertions.assertEquals(response.getStatusCode(),HttpStatusCode.valueOf(200)); //Ok
    }

    @Test
    public void getMajorDifferenceValue_request_return_BadRequest() {
        //Arrange        
        String uri = "http://localhost:" + port + "/exchanges/GBP/diff/256";
        //Act
        ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
        //Assert        
        Assertions.assertEquals(response.getStatusCode(),HttpStatusCode.valueOf(400)); //Bad Request
    }

    @Test
    public void getMajorDifferenceValue_request_return_Value() {
        //Arrange        
        String uri = "http://localhost:" + port + "/exchanges/GBP/diff/10";
        //Act
        ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
        //Assert        
        Assertions.assertEquals(response.getStatusCode(),HttpStatusCode.valueOf(200)); //Ok
    }

}
