package Improv;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class ExecutionPlan {

  @Param({"0", "1", "2", "3", "4", "5"})
  public int delay;

  @Param({"1", "2"})
  public String numberOfCalls;

}
