package com.blekione.fsb.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GameStatusTest {

    static Stream<Arguments> valueMappings() {
        return Stream.of(arguments("ACTIVE", GameStatus.ACTIVE),
                arguments("INACTIVE", GameStatus.INACTIVE));
    }


    @ParameterizedTest
    @MethodSource("valueMappings")
    void shouldReturnValueFromTheGivenString(String input, GameStatus expectedValue) throws Exception {
        // when
        var actualvalue = GameStatus.from(input);

        // then
        assertThat(actualvalue).isEqualTo(expectedValue);
    }



     @Test
     void shouldReturnUnrecognisedWhenTheGivenStringDoesntMatchAnyValue() throws Exception {
         // given
         var illValue = "BLAH";

         //
         var actualValue = GameStatus.from(illValue);

         // then
         assertThat(actualValue).isEqualTo(GameStatus.UNRECOGNIZED);

    }
}
