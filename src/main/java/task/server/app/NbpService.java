package task.server.app;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NbpService {
    NbpRepository nbpRepository;

    @Autowired
    NbpService(NbpRepository nbpRepository){
        this.nbpRepository = nbpRepository;
    }

    /**
     * Get average exchange rate of given currency on given date
     * @param code code of currency
     * @param date given date
     * @return average exchange rate
     */
    public Optional<Double> getAverage(String code, String date){
        Optional<Double> result = nbpRepository.getAvgExchangeRate(code, date);
        if (result.isEmpty()){
            return Optional.empty();
        }
        return result;
    }   

    /**
     * Gets min and max values for given currency and amount of quotations
     * @param code code of currency
     * @param count amount of quotations
     * @return array with min and max value as 0 and 1 element
     */
    public Optional<Double[]> getMinMax(String code, Long count){
        List<Double> allRates = nbpRepository.getAvgExchangeRates(code, count);
        if(allRates.isEmpty()){
            return Optional.empty();
        }

        Double[] minMax = { allRates.get(0), allRates.get(0)};
        //Sets minimum and maximum values of rates
        allRates.stream().forEach(rate -> {
            minMax[0] = Math.min(minMax[0], rate); 
            minMax[1] = Math.max(minMax[1], rate);
        }); 

        return Optional.of(minMax);
    }

    /**
     * Gets max difference between ask and bid value for given currency and amount of quotations
     * @param code code of currency
     * @param count amount of quotations
     * @return max difference between ask and bid
     */
    public Optional<Double> getMajorDifference(String code, Long count){
        List<Double[]> allRates = nbpRepository.getBidAndAskRates(code, count);
        if(allRates.isEmpty()){
            return Optional.empty();
        }

        double maxDifference = Math.abs(allRates.get(0)[0] - allRates.get(0)[1]);
        //Get Maxiumum difference
        for (Double[] rate : allRates) {
            maxDifference = Math.max(maxDifference, Math.abs(rate[0] - rate[1]));
        }
        return Optional.of(maxDifference);
    }
}
