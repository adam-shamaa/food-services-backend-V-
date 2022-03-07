package com.foodservicesapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RestaurantUtilsTest {

    RestaurantUtils restaurantUtils;

    @BeforeEach
    void setup(){
        restaurantUtils = new RestaurantUtils();
    }

    @ParameterizedTest
    @CsvSource({
            "tes21t, tes21t",
            "T123EST, T123EST",
            "hey there a b c :), hey there a b c :)",
            "hey  there! We got a      few extra SPACES  HEREEE      , hey there! We got a few extra SPACES HEREEE"
    })
    public void convertWhitespaceToSingleSpace(String input, String expected){
        assertThat(restaurantUtils.convertWhitespaceToSingleSpace(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "tes21t, tes21t",
            "T123EST, T123EST",
            "tes#12312312 21t, tes 21t",
            "Burger King #17885 , Burger King",
            "Burger King #17885 asdasdasd #2123123@ASda213asdad @31 TEST!!, Burger King  asdasdasd  @31 TEST!!",
            "tes #testwhitespace, tes",
    })
    public void removeHashtagIdentifiers(String input, String expected){
        assertThat(restaurantUtils.removeHashtagIdentifiers(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "tes21t, tes21t",
            "T123EST, T123EST",
            "asdfas(1'23123), asdfas(123123)",
            "asdf ''''' (aslkdnasldnasd) & A*&Daksjdn(ASDK) ' (21)12'3, asdf (aslkdnasldnasd) & A*&Daksjdn(ASDK) (21)123",
            "Skip's Dishes, Skips Dishes",
    })
    public void removeApostrophes(String input, String expected){
        assertThat(restaurantUtils.removeApostrophes(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "test, test",
            "TEST, TEST",
            "asdfas(123123), asdfas",
            "asdf   (aslkdnasldnasd) & A*&Daksjdn(ASDK) (21)123, asdf    & A*&Daksjdn 123",
            "Restaurant Name (123 Address), Restaurant Name",
            "Restaurant Name       (123 Address), Restaurant Name"
    })
    public void removeTextInParenthesisTest(String input, String expected){
        assertThat(restaurantUtils.removeTextInParenthesis(input)).isEqualTo(expected);
    }
}
