package task.server.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
public class NbpRepository {
    private RestTemplate nbpApi;

    @Autowired
    NbpRepository(){
        nbpApi = new RestTemplateBuilder().rootUri("http://api.nbp.pl/api").build();
    }

    /**
     * Gets Exchange Rate Node for given currency in given day
     * @param table table from Nbp Api
     * @param code currency code
     * @param date date for which to get data
     * @return JsonNode
     */
    public Optional<JsonNode> getNode(String table, String code, String date){
        //Get Json from Nbp Api
        ResponseEntity<String> response;

        try {
            response = nbpApi.getForEntity("/exchangerates/rates/{table}/{code}/{date}", String.class,table,code,date);
        } catch (Exception e) {
            return Optional.empty();
        }

        if(response.getStatusCode() != HttpStatusCode.valueOf(200)){
            return Optional.empty();
        }
        
        JsonNode node = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            node = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Optional.of(node);
    }

    /**
     * Gets Exchange Rate Node for given currency 
     * @param table table from Nbp Api
     * @param code currency code
     * @param count amount of rate quotations to get
     * @return JsonNode from Api
     */
    public Optional<JsonNode> getNodes(String table, String code, Long count){
        //Get Json from Nbp Api
        ResponseEntity<String> response;
        try {
            response = nbpApi.getForEntity("/exchangerates/rates/{table}/{code}/last/{count}", String.class, table, code, count);
        } catch (Exception e) {
            return Optional.empty();
        }

        if(response.getStatusCode() != HttpStatusCode.valueOf(200)){
            return Optional.empty();
        }

        JsonNode node = null; 
        ObjectMapper mapper = new ObjectMapper();
        try {
            node = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Optional.of(node);
    }

    /**
     * Get average rate value.
     * @param code currency code
     * @param date date for which to get data
     * @return Average exchange rate from given day
     */
    public Optional<Double> getAvgExchangeRate(String code, String date){
        Optional<JsonNode> node = getNode("a",code,date);
        if(node.isEmpty())
        {
            return Optional.empty();
        }

        double average = node.get().get("rates").get(0).get("mid").asDouble();
        return Optional.of(average);
    }

    /**
     * Get list of average rate values. 
     * @param code currency code
     * @param count amount of rate quotations to get
     * @return list of average rate values
     */
    public List<Double> getAvgExchangeRates(String code, Long count){
        Optional<JsonNode> nodes = getNodes("a", code, count);
        if(nodes.isEmpty())
        {
            return new ArrayList<>();
        }

        ArrayList<JsonNode> list = new ArrayList<>();
        //Add rate nodes to list 
        nodes.get().get("rates").forEach(node -> list.add(node));

        //Maps all rates nodes to their average values as list
        return list.stream().map(
            node -> node.get("mid").asDouble())
        .collect(Collectors.toList());
    }

    /**
     * Get Bid and Ask values from given amount of quotations
     * @param code currency code
     * @param count amount of rate quotations to get
     * @return list of arrays with ask and bid values as 0 and 1 element respectably.
     */
    public List<Double[]> getBidAndAskRates(String code, Long count){
        Optional<JsonNode> nodes = getNodes("c", code, count);
        
        if(nodes.isEmpty())
        {
            return new ArrayList<>();
        }

        ArrayList<JsonNode> list = new ArrayList<>();
        //Add rate nodes to list 
        nodes.get().get("rates").forEach(node -> list.add(node));
        //Maps all rates nodes to list of arrays with ask and bid values.
        return list.stream().map(
            node -> new Double[] { 
                node.get("ask").asDouble() , 
                node.get("bid").asDouble()})
        .collect(Collectors.toList());

    }
}
