package com.blekione.fsb.exception;

import com.blekione.fsb.FixedClockConfiguration;
import com.blekione.fsb.configuration.ApplicationContextProvider;
import com.blekione.fsb.model.dto.ApiErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ApplicationContextProvider.class, FixedClockConfiguration.class})
class RestExceptionHandlerTest {

    @Test
    void shouldReturnExpectedErrorResponse() throws Exception {
        // given
        var ex = new ApplicationException("some api error");

        // when
        ResponseEntity<ApiErrorDto> actualErrorResponse = new RestExceptionHandler().handleApplicationException(ex);

        // then
        assertThat(actualErrorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        var expectedApiError = ApiErrorDto.create(HttpStatus.CONFLICT, ex);
        assertThat(actualErrorResponse.getBody()).isEqualTo(expectedApiError);
    }
}