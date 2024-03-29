package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class fluxAndMonoTransformTest {

  @Test
  public void testFlux1() {

    List<String> names = Arrays.asList("Rakesh", "Muraly");

    Flux<String> namesFlux = Flux.fromIterable(names)
      .map( s -> s.toUpperCase()).log();

    StepVerifier.create(namesFlux)
      .expectNext("RAKESH")
      .expectNext("MURALY")
      .verifyComplete();

  }

  @Test
  public void testFlux2() {
    List<String> names = Arrays.asList("Rakesh", "Muralydharan");

    Flux<Integer> namesFlux = Flux.fromIterable(names)
      .map( s -> s.length()).log();

    StepVerifier.create(namesFlux)
      .expectNext(6)
      .expectNext(12)
      .verifyComplete();

  }

  @Test
  public void testFlux3() {
    List<String> names = Arrays.asList("Rakesh", "Muralydharan");

    Flux<Integer> namesFlux = Flux.fromIterable(names)
      .map( s -> s.length())
      .repeat(1)
      .log();

    StepVerifier.create(namesFlux)
      .expectNext(6)
      .expectNext(12)
      .expectNext(6)
      .expectNext(12)
      .verifyComplete();

  }

  @Test
  public void testFlux4() {

    List<String> names = Arrays.asList("Rakesh", "Muralydharan");

    Flux<String> namesFlux = Flux.fromIterable(names)
      .filter(s -> s.length() > 6)
      .map( s -> s.toUpperCase()).log();

    StepVerifier.create(namesFlux)
      .expectNext("MURALYDHARAN")
      .verifyComplete();

  }

  @Test
  public void testFlux5() {
    List<String> names = Arrays.asList("A", "B", "C", "D", "E");

    Flux<String> namesFlux = Flux.fromIterable(names)
      .flatMap(s -> {
        // this is where you do your remote web service calls
        return Flux.fromIterable(convert(s));
      })
      .log();

    StepVerifier.create(namesFlux)
      .expectNextCount(5)
      .verifyComplete();
  }

  @Test
  public void testFlux6() {
    List<Integer> itemIds = Arrays.asList(1,2,3,4,5);

    Flux<List<String>> namesFlux = Flux.fromIterable(itemIds)
      .window(3) //Flux<Flux<Integer>> {1,2} {3,4} {5}
      .flatMap((s) -> s.map(integer -> this.getDataFromRemoteWebService(integer)).subscribeOn(Schedulers.parallel())) //Flux<String>
      .log();

    Flux<String> outputFlux = namesFlux.flatMap(strings -> {
      return Flux.fromIterable(strings);
    });
    StepVerifier.create(outputFlux)
      .expectNextCount(5)
      .verifyComplete();
  }

  @Test
  public void testFlux7() {
    List<Integer> itemIds = Arrays.asList(1,2,3,4,5);

    Flux<String> namesFlux = Flux.fromIterable(itemIds)
      .window(2) //Flux<Flux<String>> {1,2} {3,4} {5}
      .flatMapSequential((s) ->
        s.map(this::getDataFromRemoteWebService).subscribeOn(Schedulers.parallel()) //Flux<List<String>>
          .flatMap(t -> Flux.fromIterable(t))) //Flux<String>
      .log();

    StepVerifier.create(namesFlux)
      .expectNextCount(5)
      .verifyComplete();
  }


  private List<String> getDataFromRemoteWebService(Integer itemId) {
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Arrays.asList("NEW STR" + itemId);
  }

  private List<String> convert(String s) {
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Arrays.asList("NEW STR");

  }
}
