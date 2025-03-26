/*
 * Copyright (c) 2024. glywk
 * https://github.com/glywk
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.glywk;

import org.glywk.builder.Criterias;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class ComplexQueryTest {
    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp() {
        assertTrue(true);
    }

    private static String parse(Criterias criterias) throws ParseException {
        return ((RequestStringConverter) criterias).getCriteria();
    }

    private static Query makeQueryParser(String cmd) {
        Query ltm = new Query(new StringReader(cmd));
        ltm.setRequestFactory(new RequestTestFactory());
        return ltm;
    }

    @Test
    public void parseCriterias() throws ParseException {
        String search = """
                strawberry.family  =  'Rosaceae';
                strawberry.genius  =  'Fragaria';
                strawberry.species <> "F. virginiana";
                strawberry.energy  >  136;
                strawberry.sugars  =  [4,5];
                strawberry.sale    >= time-point(date, 'Europe/Paris', 25d 2m 2025y)
                """;
        Query ltm = makeQueryParser(search);
        var query = parse(ltm.criterias());
        assertEquals("strawberry.family='Rosaceae';strawberry.genius='Fragaria';strawberry.species<>\"F. virginiana\";strawberry.energy>136;strawberry.sugars=[4,5];strawberry.sale>=time-point(date,'Europe/Paris',25d2m2025y)", query);
    }
}
