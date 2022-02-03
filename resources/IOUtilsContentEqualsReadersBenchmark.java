/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = {"-server"})
public class IOUtilsContentEqualsReadersBenchmark {
    private static final String TEST_PATH_16K_A = "/org/apache/commons/io/abitmorethan16k.txt";

    private static final String content = getString();

    public static String getString() {
        try {
            FileInputStream fileInputStream = new FileInputStream(IOUtilsContentEqualsReadersBenchmark.class.getResource(TEST_PATH_16K_A).toURI().getPath());
            return IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static FileOutputStream getOutputStream() throws URISyntaxException, FileNotFoundException {
        return new FileOutputStream(new File("out.txt"));
    }

    @Benchmark
    public static void read1(Blackhole blackhole) throws Exception {
        Writer writer = new OutputStreamWriter(getOutputStream());
        for (char c : content.toCharArray()) {
            writer.write(c);
        }
        writer.flush();
    }

    @Benchmark
    public static void read2(Blackhole blackhole) throws Exception {
        Writer writer = new OutputStreamWriter(new BufferedOutputStream(getOutputStream()));
        for (char c : content.toCharArray()) {
            writer.write(c);
        }
        writer.flush();
    }

    @Benchmark
    public static void read3(Blackhole blackhole) throws Exception {
        Writer writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()));
        for (char c : content.toCharArray()) {
            writer.write(c);
        }
        writer.flush();
    }
}
