package task.server.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class NbpServiceTests {

	@Mock
	private NbpRepository nbpRepository;
	@InjectMocks
	private NbpService nbpService;

	//getAverage
	@Test
	void getAverage_should_return_average(){
		//Arrange
		Optional<Double> returnValue = Optional.of(34d);
		when(nbpRepository.getAvgExchangeRate("GBP", "2019-11-12")).thenReturn(returnValue);

		//Act
		Optional<Double> average = nbpService.getAverage("GBP", "2019-11-12");

		//Assert
		Optional<Double> expected = Optional.of(34d);
		assertEquals(average.isPresent(), expected.isPresent());
		assertEquals(average, expected);
	}

	@Test
	void getAverage_should_return_empty(){
		//Arrange
		Optional<Double> returnValue = Optional.empty();
		when(nbpRepository.getAvgExchangeRate("GBP", "2019-11-11")).thenReturn(returnValue);

		//Act
		Optional<Double> average = nbpService.getAverage("GBP", "2019-11-11");

		//Assert
		Optional<Double> expected = Optional.empty();
		assertEquals(average.isPresent(), expected.isPresent());
		assertEquals(average, expected);
	}
	


	//getMajorDifference
	@Test
	void getMajorDifference_should_return_biggest_difference(){
		//Arrange
		List<Double[]> returnList = new ArrayList<>();
		returnList.add(new Double[]{ 3d, 4d});
		returnList.add(new Double[]{ 2d, 5d}); //biggest
		returnList.add(new Double[]{ 1d, 3d});

		when(nbpRepository.getBidAndAskRates("GBP", 3l)).thenReturn(returnList);
		//Act
		Optional<Double> majorDifference = nbpService.getMajorDifference("GBP", 3l);
		
		//Assert
		Optional<Double> expected = Optional.of(3d);
		assertEquals(majorDifference.isPresent(), expected.isPresent());
		assertEquals(majorDifference, expected);
	}
	@Test
	void getMajorDifference_should_return_empty(){
		//Arrange
		List<Double[]> returnList = new ArrayList<>();

		when(nbpRepository.getBidAndAskRates("GBP", 3l)).thenReturn(returnList);
		//Act
		Optional<Double> majorDifference = nbpService.getMajorDifference("GBP", 3l);
		
		//Assert
		Optional<Double> expected = Optional.empty();
		assertEquals(majorDifference.isPresent(), expected.isPresent());
		assertEquals(majorDifference, expected);
	}

	//getMinMax
	@Test
	void getMinMax_should_return_min_and_max_value(){
		//Arrange
		List<Double> returnList = new ArrayList<>();
		returnList.add(3d);
		returnList.add(2d); //smallest
		returnList.add(4d); //biggest

		when(nbpRepository.getAvgExchangeRates("GBP", 3l)).thenReturn(returnList);
		//Act
		Optional<Double[]> minMax = nbpService.getMinMax("GBP", 3l);
		
		//Assert
		Optional<Double[]> expected = Optional.of(new Double[]{2d,4d});
		assertEquals(minMax.isPresent(), expected.isPresent());
		assertEquals(minMax.get()[0], expected.get()[0]);
		assertEquals(minMax.get()[1], expected.get()[1]);
	}

	@Test
	void getMinMax_should_return_empty(){
		//Arrange
		List<Double> returnList = new ArrayList<>();

		when(nbpRepository.getAvgExchangeRates("GBP", 3l)).thenReturn(returnList);
		//Act
		Optional<Double[]> minMax = nbpService.getMinMax("GBP", 3l);
		
		//Assert
		Optional<Double[]> expected = Optional.empty();
		assertEquals(minMax.isPresent(), expected.isPresent());
		assertEquals(minMax, expected);
	}
}
