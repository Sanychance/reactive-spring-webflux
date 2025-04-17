package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fAm = new FluxAndMonoGeneratorService();
        fAm.fluxNames()
                .subscribe(names -> System.out.println("Names:" + names));
        //using subscribe to access to the Flux - the elements in the flux will return in form of streams
        //no dta will send from Flux until we call subscribe - subscribe is always attach to a publisher
        //here the fluxName is the publisher and subscriber is attached to it

        fAm.fluxNamesMap().subscribe(names -> System.out.println("Names_Map:"+names));

        fAm.monoName("Sanaz").subscribe(name -> System.out.println("Mono Name is:"+name));
    }

    public Flux<String> fluxNames(){

        return Flux.fromIterable(List.of("Sanaz","Soheil"))
                .log(); //it will log each and every event that happens between the subscriber and publisher
    }

    //reactive streams are immutable so the result wont change
    public Flux<String> fluxNames_Immutability(){

      Flux<String> namesFlux =  Flux.fromIterable(List.of("Sanaz","Soheil"));
      namesFlux.map(String::toUpperCase);
      return  namesFlux;

    }

    /***************** MAP ********************
     - use for 1 to 1 transformation
     - Simple Transformation from T to V
     - Simple for synchronous transformation
     - Doesnt support transformations that returns Publisher

     **/
    public Flux<String> fluxNamesMap(){

        return Flux.fromIterable(List.of("Sanaz","Soheil"))
                //.map(name-> name.toUpperCase())
                .map(String::toUpperCase)
                .log(); //it will log each and every event that happens between the subscriber and publisher
    }

    //make the string uppercase and then return the ones that has
    // the lenght > stringLenght and map to be like 4-SANAZ, 6-SOHEIL
    public Flux<String> fluxNamesMap_Filter(int stringLenght){

        //the whole chain of multiple functions below called pipeline iun Functional programming
        return Flux.fromIterable(List.of("Sanaz","Soheil","Mehrdad"))
                //.map(name-> name.toUpperCase())
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLenght)
                .map(s ->s.length() +"-"+s)
                .log(); //it will log each and every event that happens between the subscriber and publisher
    }

    /***************** Flat MAP **********************
     - use for 1 to N transformation
     - Its more than transformations, subscribes to Flux or Mono thats part of the transformations
      and flattens it and sends it downstream
     - Use for asynchronous transformation
     - support transformations that returns Publisher

     **/
    public Flux<String> fluxNames_FlatMap(int stringLenght){

        return Flux.fromIterable(List.of("Sanaz","Soheil","Mehrdad"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLenght)
                //SOHEIL, MEHRDAD -> S,O,H,E,I,L,M,E,H,R,D,A,D
                .flatMap(name -> splitName(name))
                .log(); //it will log each and every event that happens between the subscriber and publisher
    }

    //when we are having a use case where ordering matters then we shouldnt use flatMap as it will call async
    public Flux<String> fluxNames_FlatMapAsync(int stringLenght){

        return Flux.fromIterable(List.of("Sanaz","Soheil","Mehrdad"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLenght)
                //SOHEIL, MEHRDAD -> S,O,H,E,I,L,M,E,H,R,D,A,D
                .flatMap(name -> splitName_WithDelay(name))
                .log(); //it will log each and every event that happens between the subscriber and publisher
    }

    // if we need ordering then we should use concat Map but its much slower than flatMap
    public Flux<String> fluxNames_concatMapAsync(int stringLenght){

        return Flux.fromIterable(List.of("Sanaz","Soheil","Mehrdad"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLenght)
                //SOHEIL, MEHRDAD -> S,O,H,E,I,L,M,E,H,R,D,A,D
                .concatMap(name -> splitName_WithDelay(name))
                .log(); //it will log each and every event that happens between the subscriber and publisher
    }

    //SOHEIL -> Flux(S,O,H,E,I,L)
    public Flux<String> splitName_WithDelay(String name){
        var charArray =  name.split("");
        var delay = 1000; //new Random().nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    //SOHEIL -> Flux(S,O,H,E,I,L)
    public Flux<String> splitName(String name){
       var charArray =  name.split("");
       return Flux.fromArray(charArray);
    }

    public Mono<String> monoName(String name){
        return Mono.just(name)
                .log();
    }

    public Mono<String> monoName_Map_Filter(String name, int stringLenght){
        return Mono.just(name)
                .map(String::toUpperCase)
                .filter(s-> s.length()>stringLenght)
                .log();
    }
    // Use when transformation returns another Mono of some type. flatmap with return Mono<T>
    // when we use RESP API call which requires Async calls
    public Mono<List<String>> monoName_Map_Filter_FlatMap(String name, int stringLenght){
        return Mono.just(name)
                .map(String::toUpperCase)
                .filter(s-> s.length()>stringLenght)
                //SOHEIL, MEHRDAD -> S,O,H,E,I,L,M,E,H,R,D,A,D
                .flatMap(this::splitStringToList)//Mono<List of S,A,N,Z
                .log();
    }

    private Mono<List<String>> splitStringToList(String s) {
        var charArray = s.split("");
       return Mono.just(List.of(charArray));
    }
}
