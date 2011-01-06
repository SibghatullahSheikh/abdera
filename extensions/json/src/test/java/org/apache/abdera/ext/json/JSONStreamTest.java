/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera.ext.json;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Entry;
import org.apache.abdera.writer.Writer;
import org.apache.abdera.writer.WriterOptions;
import org.junit.Test;

public class JSONStreamTest {

    @Test
    public void testJSONStreamContent() throws Exception {
        Abdera abdera = new Abdera();
        Entry entry = abdera.newEntry();

        entry.setContent(new IRI("http://example.org/xml"), "text/xml");

        Writer json = abdera.getWriterFactory().getWriter("json");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WriterOptions opts = entry.getDefaultWriterOptions();
        opts.setAutoClose(true);
        entry.writeTo(json, outputStream, opts);
        String output = outputStream.toString();
        assertTrue(output.contains("\"type\":\"text/xml\""));
        assertTrue(output.contains("\"src\":\"http://example.org/xml\""));
        assertTrue(output.contains("\"content\":"));
    }
}
