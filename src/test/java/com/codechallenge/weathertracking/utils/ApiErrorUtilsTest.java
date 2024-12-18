package com.codechallenge.weathertracking.utils;

import com.codechallenge.weathertracking.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class ApiErrorUtilsTest {
    @Test
    void givenNoErrors_thenValidationPasses() {
        var givenBindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(givenBindingResult.hasErrors()).thenReturn(false);
        assertDoesNotThrow(() -> ApiErrorUtils.shouldBeValid(givenBindingResult));
    }

    @Test
    void givenValidationErrors_thenThrowsException() {
        var givenBindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(givenBindingResult.hasErrors()).thenReturn(true);
        Mockito.when(givenBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("object name", "error message")));

        var actual = assertThrows(ValidationException.class, () -> ApiErrorUtils.shouldBeValid(givenBindingResult));

        assertNotNull(actual);
        assertNotNull(actual.getMessage());
        assertFalse(actual.getDetails().isEmpty());
    }
}
