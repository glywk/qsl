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

import org.glywk.builder.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RequestTestFactory implements RequestFactory {
    @Override
    public Criterias makeCriterias(List<Criteria> criterias) {
        var sb = new StringBuilder();
        String separator = "";
        for (var criteria : criterias) {
            sb.append(separator);
            sb.append(getCriteria(criteria));
            separator = ";";
        }
        return new RequestTest(sb.toString());
    }

    @Override
    public Criteria makeCriteria(Attributes attributes, Operation operation) {
        var sb = new StringBuilder();
        sb.append(getAttributes(attributes));
        sb.append(getOperation(operation));
        return new RequestTest(sb.toString());
    }

    @Override
    public Request and(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append(" and ");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Request or(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append(" or ");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation equals(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append("=");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation equals(Value value) {
        var sb = new StringBuilder();
        sb.append("=");
        sb.append(getCriteria(value));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation equals(CollectionValue values) {
        var sb = new StringBuilder();
        sb.append("=");
        sb.append(getValues(values));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation equals(RangeValue range) {
        var sb = new StringBuilder();
        sb.append("=");
        sb.append(getRange(range));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation differs(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append("<>");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation differs(CollectionValue values) {
        var sb = new StringBuilder();
        sb.append("<>");
        sb.append(getValues(values));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation differs(Value value) {
        var sb = new StringBuilder();
        sb.append("<>");
        sb.append(getCriteria(value));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation less(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append("<");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation less(Value value) {
        var sb = new StringBuilder();
        sb.append("<");
        sb.append(getCriteria(value));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation lessOrEquals(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append("<=");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation lessOrEquals(Value value) {
        var sb = new StringBuilder();
        sb.append("<=");
        sb.append(getCriteria(value));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation greater(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append(">");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation greater(Value value) {
        var sb = new StringBuilder();
        sb.append(">");
        sb.append(getCriteria(value));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation greaterOrEquals(Request lhs, Request rhs) {
        var sb = new StringBuilder();
        sb.append(getCriteria(lhs));
        sb.append(">=");
        sb.append(getCriteria(rhs));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation greaterOrEquals(Value value) {
        var sb = new StringBuilder();
        sb.append(">=");
        sb.append(getCriteria(value));
        return new RequestTest(sb.toString());
    }

    @Override
    public Operation isNull() {
        return new RequestTest(" is null");
    }

    @Override
    public Operation isNotNull() {
        return new RequestTest(" is not null");
    }

    @Override
    public Attributes attributes(List<Attribute> attributes) {
        var sb = new StringBuilder();
        String separator = "";
        for (var attribute : attributes) {
            sb.append(separator);
            sb.append(getAttribute(attribute));
            separator = ",";
        }
        return new RequestTest(sb.toString());
    }

    @Override
    public Attribute attribute(String value) {
        return new RequestTest(value);
    }

    @Override
    public Request identifier(String value) {
        return new RequestTest(value);
    }

    @Override
    public DateValue makeDate(String value) {
        return new RequestTest(value);
    }

    @Override
    public TimePointValue makeTimePoint(TimePointCategory timePointCategory, TimePointOffsetValue timePointOffsetValue, Optional<TimeZoneValue> timeZoneValue) {
        return switch (timePointCategory) {
            case DATE_TIME ->
                    new RequestTest("time-point(" + serializeTimeZoneValue(timeZoneValue, "") + serializeTimePointOffsetDateTimeValue(timePointOffsetValue) + ")");

            case DATE ->
                    new RequestTest("time-point(date" + serializeTimeZoneValue(timeZoneValue, ",") + serializeTimePointOffsetValue(timePointOffsetValue) + ")");

            case TIME ->
                    new RequestTest("time-point(time" + serializeTimeZoneValue(timeZoneValue, ",") + serializeTimePointOffsetValue(timePointOffsetValue) + ")");
        };
    }

    private String serializeTimePointOffsetDateTimeValue(TimePointOffsetValue timePointOffsetValue) {
        return serializeTimePointOffsetValue(timePointOffsetValue, "");
    }

    private String serializeTimePointOffsetValue(TimePointOffsetValue timePointOffsetValue) {
        return serializeTimePointOffsetValue(timePointOffsetValue, ",");
    }

    private String serializeTimePointOffsetValue(TimePointOffsetValue timePointOffsetValue, String separator) {
        String offset = ((RequestStringConverter) timePointOffsetValue).getCriteria();
        return offset.isEmpty() ? "" : separator + offset;
    }

    private String serializeTimeZoneValue(Optional<TimeZoneValue> timeZoneValue, String separator) {
        return timeZoneValue.map(zoneValue -> separator + ((RequestStringConverter) zoneValue).getCriteria()).orElse("");
    }

    @Override
    public TimePointOffsetValue makeTimePointOffsetValue(Collection<TimePointSpanModifier> modifiers) {
        StringBuilder s = new StringBuilder();
        for (TimePointSpanModifier modifier : modifiers) {
            s.append(((RequestStringConverter) modifier).getCriteria());
        }
        return new RequestTest(s.toString());
    }

    @Override
    public TimePointSpanModifier makeTimePointModifier(TimePointSpanOperation operation, int offset, TimePointSpanUnit unit) {
        String o = switch (operation) {
            case ABSOLUTE -> "";
            case ADDITIVE -> "+";
            case SUBSTRACT -> "-";
        };
        String u = switch (unit) {
            case DAY -> "d";
            case MONTH -> "m";
            case YEAR -> "y";
            case HOUR -> "H";
            case MINUTE -> "M";
            case SECOND -> "S";
        };
        return new RequestTest(o + Integer.toString(offset) + u);
    }

    @Override
    public CollectionValue values(List<Value> values) {
        var sb = new StringBuilder();
        sb.append("(");
        String separator = "";
        for (var value : values) {
            sb.append(separator);
            sb.append(getCriteria(value));
            separator = ",";
        }
        sb.append(")");
        return new RequestTest(sb.toString());
    }

    @Override
    public RangeValue range(Value lhs, Value rhs) {
        var sb = new StringBuilder();
        sb.append("[");
        sb.append(getCriteria(lhs));
        sb.append(",");
        sb.append(getCriteria(rhs));
        sb.append("]");
        return new RequestTest(sb.toString());
    }

    @Override
    public StringValue makeQuotedString(String value) {
        return new RequestTest("'" + value + "'");
    }

    @Override
    public TimeZoneValue makeDoubleQuotedTimeZoneString(String value) {
        return new RequestTest("\"" + value + "\"");
    }

    @Override
    public TimeZoneValue makeQuotedTimeZoneString(String value) {
        return new RequestTest("'" + value + "'");
    }

    @Override
    public TimeZoneValue makeUtcTimeZoneString() {
        return new RequestTest("UTC");
    }

    @Override
    public StringValue makeDoubleQuotedString(String value) {
        return new RequestTest("\"" + value + "\"");
    }

    @Override
    public LongValue makeLong(String value) {
        return new RequestTest(value);
    }

    @Override
    public DoubleValue makeDouble(String value) {
        return new RequestTest(value);
    }

    @Override
    public BooleanValue makeTrue() {
        return new RequestTest("true");
    }

    @Override
    public BooleanValue makeFalse() {
        return new RequestTest("false");
    }

    private String getCriteria(Criteria criteria) {
        return ((RequestStringConverter) criteria).getCriteria();
    }

    private String getAttributes(Attributes attributes) {
        return ((RequestStringConverter) attributes).getCriteria();
    }

    private String getAttribute(Attribute attribute) {
        return ((RequestStringConverter) attribute).getCriteria();
    }

    private String getOperation(Operation operation) {
        return ((RequestStringConverter) operation).getCriteria();
    }

    private String getCriteria(Request criteria) {
        return ((RequestStringConverter) criteria).getCriteria();
    }

    private String getValues(CollectionValue values) {
        return ((RequestStringConverter) values).getCriteria();
    }

    private String getRange(RangeValue range) {
        return ((RequestStringConverter) range).getCriteria();
    }
}
