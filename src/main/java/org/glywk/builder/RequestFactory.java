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

package org.glywk.builder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestFactory {

    // criteria
    Criterias makeCriterias(List<Criteria> criterias);

    Criteria makeCriteria(Attributes attributes, Operation operation);

    // logical
    Request and(Request lhs, Request rhs);

    Request or(Request lhs, Request rhs);

    // equality
    Operation equals(Request lhs, Request rhs);

    Operation equals(Value value);

    Operation equals(CollectionValue values);

    Operation equals(RangeValue range);

    Operation differs(Request lhs, Request rhs);

    Operation differs(Value value);

    Operation differs(CollectionValue values);

    // relational
    Operation less(Request lhs, Request rhs);

    Operation less(Value value);

    Operation lessOrEquals(Request lhs, Request rhs);

    Operation lessOrEquals(Value value);

    Operation greater(Request lhs, Request rhs);

    Operation greater(Value value);

    Operation greaterOrEquals(Request lhs, Request rhs);

    Operation greaterOrEquals(Value value);

    // properties
    Operation isNull();

    Operation isNotNull();

    // attribute

    Attributes attributes(List<Attribute> attributes);

    Attribute attribute(String attribute);

    // terminal

    Request identifier(String value);

    DateValue makeDate(String value);

    TimePointValue makeTimePoint(TimePointCategory timePointCategory, TimePointOffsetValue timePointOffsetValue, Optional<TimeZoneValue> timeZoneValue);

    RangeValue range(Value lhs, Value rhs);

    TimePointOffsetValue makeTimePointOffsetValue(Collection<TimePointSpanModifier> modifiers);

    TimePointSpanModifier makeTimePointModifier(TimePointSpanOperation operation, int offset, TimePointSpanUnit unit);

    CollectionValue values(List<Value> values);

    // QSL String
    StringValue makeQuotedString(String value);

    StringValue makeDoubleQuotedString(String value);

    // QSL Timestamp
    TimeZoneValue makeUtcTimeZoneString();

    TimeZoneValue makeQuotedTimeZoneString(String value);

    TimeZoneValue makeDoubleQuotedTimeZoneString(String value);

    // QSL Number
    LongValue makeLong(String value);

    DoubleValue makeDouble(String value);

    // QSL Boolean
    BooleanValue makeTrue();

    BooleanValue makeFalse();
}
