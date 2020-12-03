package com.example.sunatlanticsrider.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class LatLongValidationTest {

    protected static final String LATITUDE_PATTERN = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    protected static final String LONGITUDE_PATTERN = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";


    public void latitudeTest() {
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.UP);
        double step = 0.01;
        Double latitudeToTest = -90.0;

        while (latitudeToTest <= 90.0) {
            boolean result = df.format(latitudeToTest).matches(LATITUDE_PATTERN);
            //log.info("Latitude: tested {}. Result (matches regex): {}", df.format(latitudeToTest), result);
            //assertThat(result, is(true));
            latitudeToTest += step;
        }

        latitudeToTest = -90.1;

        while (latitudeToTest >= -200.0) {
            boolean result = df.format(latitudeToTest).matches(LATITUDE_PATTERN);
            //log.info("Latitude: tested {}. Result (matches regex): {}", df.format(latitudeToTest), result);
            //assertThat(result, is(false));
            latitudeToTest -= step;
        }

        latitudeToTest = 90.01;

        while (latitudeToTest <= 200.0) {
            boolean result = df.format(latitudeToTest).matches(LATITUDE_PATTERN);
            //log.info("Latitude: tested {}. Result (matches regex): {}", df.format(latitudeToTest), result);
            //assertThat(result, is(false));
            latitudeToTest += step;
        }
    }

    public void longitudeTest() {
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.UP);
        double step = 0.01;
        Double longitudeToTest = -180.0;

        while (longitudeToTest <= 180.0) {
            boolean result = df.format(longitudeToTest).matches(LONGITUDE_PATTERN);
            //log.info("Longitude: tested {}. Result (matches regex): {}", df.format(longitudeToTest), result);
            //assertThat(result, is(true));
            longitudeToTest += step;
        }

        longitudeToTest = -180.01;

        while (longitudeToTest >= -300.0) {
            boolean result = df.format(longitudeToTest).matches(LONGITUDE_PATTERN);
            //log.info("Longitude: tested {}. Result (matches regex): {}", df.format(longitudeToTest), result);
            //assertThat(result, is(false));
            longitudeToTest -= step;
        }

        longitudeToTest = 180.01;

        while (longitudeToTest <= 300.0) {
            boolean result = df.format(longitudeToTest).matches(LONGITUDE_PATTERN);
            //log.info("Longitude: tested {}. Result (matches regex): {}", df.format(longitudeToTest), result);
            //assertThat(result, is(false));
            longitudeToTest += step;
        }
    }

}
