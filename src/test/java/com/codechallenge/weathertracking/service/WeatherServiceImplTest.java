package com.codechallenge.weathertracking.service;

import com.codechallenge.weathertracking.dto.WeatherConditionDto;
import com.codechallenge.weathertracking.dto.WeatherRequestDto;
import com.codechallenge.weathertracking.model.DescriptionEntity;
import com.codechallenge.weathertracking.model.UserEntity;
import com.codechallenge.weathertracking.model.WeatherConditionEntity;
import com.codechallenge.weathertracking.repository.UserRepository;
import com.codechallenge.weathertracking.repository.WeatherConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class WeatherServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Mock
    private WeatherConditionRepository weatherConditionRepository;

    @Autowired
    private WeatherService systemUnderTest;

    @Nested
    class SaveTests {
        @Test
        void save_givenValidDto_thenReturnWeatherConditionDto() {
            final var expectedId = 1L;
            var givenDto = new WeatherRequestDto("Samira", "94041");
            when(weatherConditionRepository.save(any(WeatherConditionEntity.class)))
                    .thenReturn(new WeatherConditionEntity());

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertEquals(expectedId, actual.get().id());
        }

    }

    @Nested
    class FindTests {

        @BeforeEach
        void setUp() {
            UserEntity userEntity = createTestUserEntity();
            userRepository.saveAndFlush(userEntity);
        }

        @Test
        void getByPostalCode_whenNoUserFound_thenReturnEmptyOptional() {

            String postalCode = "94042";

            Optional<List<WeatherConditionDto>> actual = systemUnderTest.getByPostalCode(postalCode);

            assertTrue(actual.isEmpty());
        }


        @Test
        void getByPostalCode_whenUserHasWeathers_thenReturnWeatherConditionList() {
            String postalCode = "94041";

            Optional<List<WeatherConditionDto>> actual = systemUnderTest.getByPostalCode(postalCode);

            assertTrue(actual.isPresent());
            assertEquals(1, actual.get().size());
        }


        @Test
        void getByUser_whenNoUserFound_thenReturnEmptyOptional() {
            String username = "Joe";

            Optional<List<WeatherConditionDto>> actual = systemUnderTest.getByUser(username);

            assertTrue(actual.isEmpty());
        }

       @Test
        void getByUser_whenUserHasWeathers_thenReturnWeatherConditionList() {
            String username = "Samira";

            Optional<List<WeatherConditionDto>> result = systemUnderTest.getByUser(username);

            assertTrue(result.isPresent());
            assertEquals(1, result.get().size());
        }

     private static UserEntity createTestUserEntity() {
         DescriptionEntity description = new DescriptionEntity();
         description.setId(1L);
         description.setWeatherId(101);
         description.setMain("Clear");
         description.setDescription("Clear sky");
         description.setIcon("01d");

         WeatherConditionEntity weather = new WeatherConditionEntity();
         weather.setId(1L);
         weather.setTemperature(25.5);
         weather.setFeelsLike(24.0);
         weather.setTempMin(22.0);
         weather.setTempMax(28.0);
         weather.setHumidity(60);
         weather.setPressure(1013);
         weather.setWindSpeed(5.5);
         weather.setWindDeg(180);
         weather.setCloudCoverage(10);
         weather.setCityName("florida");
         weather.setCountry("US");
         weather.setTimestamp(LocalDateTime.now());
         weather.setDescriptions(List.of(description));

         description.setWeather(weather);

         UserEntity user = new UserEntity();
         user.setId(1L);
         user.setUsername("Samira");
         user.setPostalCode("94041");
         user.setRequestTime(LocalDateTime.now());
         user.setWeathers(new ArrayList<>(List.of(weather)));

         weather.setUser(user);

         return user;
     }

    }
}
