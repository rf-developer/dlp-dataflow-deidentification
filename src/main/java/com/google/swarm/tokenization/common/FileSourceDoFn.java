/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.swarm.tokenization.common;

import org.apache.beam.sdk.io.FileIO.ReadableFile;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class FileSourceDoFn extends DoFn<ReadableFile, KV<String, ReadableFile>> {
  public static final Logger LOG = LoggerFactory.getLogger(FileSourceDoFn.class);
  private static final String FILE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";

  @ProcessElement
  public void processElement(ProcessContext c) {

    ReadableFile file = c.element();
    String fileName = file.getMetadata().resourceId().toString();
    if (fileName.matches(FILE_PATTERN)) {
      String key = String.format("%s_%s", fileName, Instant.now().getMillis());
      c.output(KV.of(key, file));
    } else {
      LOG.info("Extension Not Supported {}", fileName);
    }
  }
}
