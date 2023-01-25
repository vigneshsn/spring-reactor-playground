package in.vigneshsn.springreactivedemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTest {


    @Test
    void consumerRequestReceiver() {

        Flux request = Flux.range(1, 5)
                .map(i -> "Person "+i )
                //.limitRate(5)
                .log();

//        Subscriber subscriber = new Subscriber() {
//            @Override
//            public void onSubscribe(Subscription subscription) {
//                for (int i = 0; i < 5; i++) {
//                    System.out.println("Requesting the next 10 elements!!!");
//                    subscription.request(5);
//                }
//            }
//
//            @Override
//            public void onNext(Object o) {
//                System.out.println("item received ====" + o);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                System.out.println("error occurred");
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("All 50 items have been successfully processed!!!");
//            }
//        };

//        request.subscribe(
//                (item) -> System.out.println("item received ====" + item),
//                (error) -> System.out.println("error occurred"),
//                () -> System.out.println("All 50 items have been successfully processed!!!")
//        );

        //request.subscribeWith(subscriber);

        StepVerifier.create(request)
                .expectSubscription()
                .thenRequest(5)
                .expectNext("Person 1", "Person 2", "Person 3", "Person 4", "Person 5")
                .verifyComplete();

//        StepVerifier.create(request)
//                .expectSubscription()
//                .thenRequest(10)
//                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
//                .thenRequest(10)
//                .expectNext(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
//                .thenRequest(10)
//                .expectNext(21, 22, 23, 24, 25, 26, 27 , 28, 29 ,30)
//                .thenRequest(10)
//                .expectNext(31, 32, 33, 34, 35, 36, 37 , 38, 39 ,40)
//                .thenRequest(10)
//                .expectNext(41, 42, 43, 44, 45, 46, 47 , 48, 49 ,50)
//                .verifyComplete();

    }
}
