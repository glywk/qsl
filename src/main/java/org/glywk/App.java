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

import static org.glywk.QueryConstants.EOF;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {
        java.io.Reader r = new java.io.FileReader(args[0]);
        SimpleCharStream scs = new SimpleCharStream(r);
        QueryTokenManager mgr = new QueryTokenManager(scs);
        for (Token t = mgr.getNextToken(); t.kind != EOF;
             t = mgr.getNextToken()) {
            System.out.println("Found a " + QueryConstants.tokenImage[t.kind] + ": " + t.image);
        }
    }
}
