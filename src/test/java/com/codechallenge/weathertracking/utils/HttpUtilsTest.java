package com.codechallenge.weathertracking.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.codechallenge.weathertracking.utils.HttpUtils.uriOf;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class HttpUtilsTest {
    @BeforeEach
    public void setUp() {
        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void givenValidId_thenCreatUri() {
        var givenId = 6L;

        var actual = uriOf(givenId);

        assertNotNull(actual);
        assertEquals("/6", actual.getPath());
    }

    @Test
    public void givenNullId_thenThrowNullPointerException() {
        final Long givenId = null;

        var actual = assertThrows(NullPointerException.class, () -> uriOf(givenId));

        assertNotNull(actual);
        assertNotNull(actual.getMessage());
    }

}
