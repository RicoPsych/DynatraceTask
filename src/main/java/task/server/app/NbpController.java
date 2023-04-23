package task.server.app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("exchanges/")
public class NbpController {
    private NbpService nbpService;

    @Autowired
    NbpController(NbpService nbpService){
        this.nbpService = nbpService;
    }

    @GetMapping("{currency}/{date}")
    public  ResponseEntity<Double> getAvgExchangeRate(@PathVariable("currency") String currencyCode, @PathVariable("date") String dateString){
        //Check if given date format is valid
        try {
            LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e){
            return ResponseEntity.badRequest().header("Reason", "Wrong Date type").build();
        }

        Optional<Double> result = nbpService.getAverage(currencyCode, dateString);
        if (result.isEmpty()){
            return ResponseEntity.notFound().header("Reason", "Not found").build(); 
        }

        return ResponseEntity.ok(result.get()); 
    }

    @GetMapping("{currency}/minmax/{amount}")
    public  ResponseEntity<Double[]> getMinMaxOfAvgRate(@PathVariable("currency") String currencyCode, @PathVariable("amount") Long amount){
        //Check if amount is too high
        if (amount > 255){
            return ResponseEntity.badRequest().header("Reason", "Amount too high").build(); 
        }

        Optional<Double[]> result = nbpService.getMinMax(currencyCode, amount);
        if(result.isEmpty()){
            return ResponseEntity.badRequest().header("Reason", "Could not fetch values").build(); 
        }

        return ResponseEntity.ok(result.get()); 
    }

    @GetMapping("{currency}/diff/{amount}")
    public  ResponseEntity<Double> getMajorDifference(@PathVariable("currency") String currencyCode, @PathVariable("amount") Long amount){
        //Check if amount is too high
        if (amount > 255){
            return ResponseEntity.badRequest().header("Reason", "Amount too high").build(); 
        }

        Optional<Double> result = nbpService.getMajorDifference(currencyCode, amount);
        if (result.isEmpty()){
            return ResponseEntity.notFound().header("Reason", "Could not fetch values").build(); 
        }

        return ResponseEntity.ok(result.get()); 
    }
}
