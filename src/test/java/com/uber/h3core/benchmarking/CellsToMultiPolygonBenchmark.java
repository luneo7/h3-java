/*
 * Copyright 2017-2018, 2022 Uber Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uber.h3core.benchmarking;

import com.google.common.collect.ImmutableSet;
import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/** Benchmarks <code>cellsToMultiPolygon</code>. */
public class CellsToMultiPolygonBenchmark {
  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  public List<List<List<LatLng>>> benchmarkH3SetToMultiPolygon2() {
    return BenchmarkState.h3Core.cellsToMultiPolygon(BenchmarkState.set2, false);
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  public List<List<List<LatLng>>> benchmarkH3SetToMultiPolygon20() {
    return BenchmarkState.h3Core.cellsToMultiPolygon(BenchmarkState.set20, true);
  }

  @State(Scope.Benchmark)
  public static class BenchmarkState {
    static Set<Long> set2 = ImmutableSet.of(0x89283082837ffffL, 0x89283082833ffffL);
    static Set<Long> set20;

    static H3Core h3Core;

    static {
      try {
        h3Core = H3Core.newInstance();
      } catch (IOException ioe) {
        throw new RuntimeException(ioe);
      }

      set20 = new HashSet<>();
      for (int i = 0; i < 20; i++) {
        set20.add(h3Core.latLngToCell(i, 0, 10));
      }
    }
  }

  public static void main(String[] args) throws RunnerException {
    Options opt =
        new OptionsBuilder()
            .include(CellsToMultiPolygonBenchmark.class.getSimpleName())
            .forks(1)
            .build();

    new Runner(opt).run();
  }
}
