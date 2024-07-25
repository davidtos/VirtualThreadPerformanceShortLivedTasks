package Improv;

import org.openjdk.jmh.annotations.*;

import java.net.http.HttpClient;
import java.time.Duration;

@State(Scope.Benchmark)
public class ExecutionPlan {

  //  @Param({"0", "1", "5", "10", "50"})
  @Param({"0", "1", "5"})
    public int delay;

  @Param({"1", "2"})
   // @Param({ "2"})
    public String numberOfCalls;

  //  HttpClient client;

    @Setup
    public void setup() {

//        client = HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_2)
//                .followRedirects(HttpClient.Redirect.NORMAL)
//                .connectTimeout(Duration.ofSeconds(20))
//                .build();
    }

    @TearDown
    public void tearDown() {
       // client.close();
    }

}
