package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void fluxNamesTest() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames();

        //Then
        StepVerifier.create(fluxNames)
                //.expectNext("Sanaz", "Soheil")
                .expectNextCount(2)
                .verifyComplete();
    }


    @Test
    void fluxNamesTest_Mixed_NextCount() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames();

        //Then
        StepVerifier.create(fluxNames) // create function will invoke the Subscribe internally which automatically triggers the publisher to send events
                .expectNext("Sanaz")//check the next element if its sanaz
                .expectNextCount(1)//check to see if the remaining item is 1 item
                .verifyComplete();
    }

    @Test
    void fluxNamesMapTest() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNamesMap();

        //Then
        StepVerifier.create(fluxNames)
                .expectNext("SANAZ", "SOHEIL")
                .verifyComplete();
    }

    @Test
    void fluxNames_Immutability() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames_Immutability();
        //Then
        StepVerifier.create(fluxNames)
                .expectNext("Sanaz", "Soheil")
                .verifyComplete();
    }

    @Test
    void fluxNamesMap_Filter() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNamesMap_Filter(5);

        //Then
        StepVerifier.create(fluxNames)
                .expectNext("6-SOHEIL")
                .verifyComplete();

    }

    @Test
    void fluxNames_FlatMap() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames_FlatMap(5);

        //Then
        StepVerifier.create(fluxNames)
                .expectNext("S","O","H","E","I","L","M","E","H","R","D","A","D")
                .verifyComplete();
    }
    @Test
    void fluxNames_transform() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames_transform(5);

        //Then
        StepVerifier.create(fluxNames)
                .expectNext("S","O","H","E","I","L","M","E","H","R","D","A","D")
                .verifyComplete();
    }

    @Test
    void fluxNames_transform_DefaultIfEmpty() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames_transform_DefaultIfEmpty(8);

        //Then
        StepVerifier.create(fluxNames)
                //.expectNext("S","O","H","E","I","L","M","E","H","R","D","A","D")
                .expectNext("Sanaz")
                .verifyComplete();
    }

    @Test
    void fluxNames_transform_SwitchIfEmpty() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames_transform_SwitchIfEmpty(8);

        //Then
        StepVerifier.create(fluxNames)
                .expectNext("S", "A", "N", "A", "Z")
                //.expectNext("S","A","N","A","Z")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty() {

        //given
        int stringLength = 6;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength).log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                //.expectNextCount(5)
                .verifyComplete();

    }
    @Test
    void fluxNames_FlatMapAsync() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames_FlatMapAsync(5);

        //Then
        StepVerifier.create(fluxNames)
                //expectNext("S","O","H","E","I","L","M","E","H","R","D","A","D")
                .expectNextCount(13)
                .verifyComplete();
    }

    @Test
    void fluxNames_concatMapAsync() {
        //when
        var fluxNames = fluxAndMonoGeneratorService.fluxNames_concatMapAsync(5);

        //Then
        StepVerifier.create(fluxNames)
                .expectNext("S","O","H","E","I","L","M","E","H","R","D","A","D")
                .verifyComplete();
    }

    @Test
    void flux_concat() {
        var fluxConcat = fluxAndMonoGeneratorService.flux_concat();

        StepVerifier.create(fluxConcat)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void flux_concatWith() {
        var flux_concatWith = fluxAndMonoGeneratorService.flux_concatWith();

        StepVerifier.create(flux_concatWith)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void flux_merge() {
        var fluxConcat = fluxAndMonoGeneratorService.flux_merge();

        StepVerifier.create(fluxConcat)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void flux_mergeWith() {
        var flux_mergeWith = fluxAndMonoGeneratorService.flux_mergeWith();

        StepVerifier.create(flux_mergeWith)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void mono_mergeWith() {
        var mono_mergeWith = fluxAndMonoGeneratorService.mono_mergeWith();

        StepVerifier.create(mono_mergeWith)
                .expectNext("A","D")
                .verifyComplete();
    }

    @Test
    void mono_concatWith() {
        var mono_concatWith = fluxAndMonoGeneratorService.mono_concatWith();

        StepVerifier.create(mono_concatWith)
                .expectNext("A","D")
                .verifyComplete();
    }

    @Test
    void monoName_Map_Filter_FlatMap() {

        //when
        var monoName = fluxAndMonoGeneratorService.monoName_Map_Filter_FlatMap("Sanaz",4);
        //Then
        StepVerifier.create(monoName)
                .expectNext(List.of("S","A","N","A","Z"))
                .verifyComplete();
    }

    @Test
    void monoName_Map_Filter_FlatMapMany() {
        //when
        var monoName = fluxAndMonoGeneratorService.monoName_Map_Filter_FlatMapMany("Sanaz",4);
        //Then
        StepVerifier.create(monoName)
                .expectNext("S","A","N","A","Z")
                .verifyComplete();
    }


}