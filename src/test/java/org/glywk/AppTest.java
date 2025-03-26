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
public class AppTest
{
    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp()
    {
        assertTrue( true );
    }

    @Test
    public void tokenizeMoveCommand() {
        String cmd = "STEP = 10";
        SimpleCharStream cs = new SimpleCharStream(new StringReader(cmd));
        QueryTokenManager ltm = new QueryTokenManager(cs);

        Token t = ltm.getNextToken();
        assertEquals(QueryTokenManager.ATTRIBUT, t.kind);
        t = ltm.getNextToken();
        assertEquals(QueryTokenManager.EQ, t.kind);
        t = ltm.getNextToken();
        assertEquals(QueryTokenManager.LONG, t.kind);
    }

    @Test
    public void tokenizeEqualComparator() {
        String cmd = "STEP=10";
        SimpleCharStream cs = new SimpleCharStream(new StringReader(cmd));
        QueryTokenManager ltm = new QueryTokenManager(cs);
        Token t = ltm.getNextToken();
        assertEquals(QueryTokenManager.ATTRIBUT, t.kind);
        t = ltm.getNextToken();
        assertEquals("=", t.image);
        t = ltm.getNextToken();
        assertEquals(QueryTokenManager.LONG, t.kind);
        assertEquals("10", t.image);
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
        String cmd = "a=1;b='2'";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("a=1;b='2'", query);
    }

    @Test
    public void parseCriteria() throws ParseException {
        String cmd = "count=10";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("count=10", query);
    }

    @Test
    public void parseAttribute() throws ParseException {
        String cmd = "attribute=1";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("attribute=1", query);
    }

    @Test
    public void parseAttributes() throws ParseException {
        String cmd = "step1, step2=0";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("step1,step2=0", query);
    }

    @Test
    public void parseBadTokenComparator() {
        String cmd = "food.fruit += 10";
        Query ltm = makeQueryParser(cmd);
        Assertions.assertThrows(TokenMgrError.class, () -> {
            ltm.criterias();
        });
    }

    @Test
    public void parseBadParserComparator() {
        String cmd = "food.expiration <= \"2025023')";
        Query ltm = makeQueryParser(cmd);
        Assertions.assertThrows(ParseException.class, () -> {
            ltm.criterias();
        });
    }

    @Test
    public void tokenizeLessOrEqualComparator() {
        String cmd = "STEP<=true";
        SimpleCharStream cs = new SimpleCharStream(new StringReader(cmd));
        QueryTokenManager ltm = new QueryTokenManager(cs);
        Token t = ltm.getNextToken();
        assertEquals(QueryTokenManager.ATTRIBUT, t.kind);
        t = ltm.getNextToken();
        assertEquals("<=", t.image);
        t = ltm.getNextToken();
        assertEquals(QueryTokenManager.TRUE, t.kind);
        assertEquals("true", t.image);
    }

    @Test
    public void parseLogicalDifference() throws ParseException {
        String cmd = "STEP<>''";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<>''", query);
    }

    @Test
    public void parseRelationalLess() throws ParseException {
        String cmd = "STEP<''";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<''", query);
    }

    @Test
    public void parseOneValues() throws ParseException {
        String cmd = "set=(1)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("set=(1)", query);
    }

    @Test
    public void parseManyValues() throws ParseException {
        String cmd = "set=(1, 10, 100, 1000)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("set=(1,10,100,1000)", query);
    }

    @Test
    public void parseIdenticalValues() throws ParseException {
        String cmd = "set=(1,10)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("set=(1,10)", query);
    }

    @Test
    public void parseIdenticalStringValues() throws ParseException {
        String cmd = "set=('1','10')";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("set=('1','10')", query);
    }

    @Test
    public void parseIdenticalMixingStringStyleValues() throws ParseException {
        String cmd = "set=('1',\"10\")";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("set=('1',\"10\")", query);
    }

    @Test
    public void parseDifferentStringValues() throws ParseException {
        String cmd = "set<>('1','10')";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("set<>('1','10')", query);
    }

    @Test
    public void parseDifferentMixingStringStyleValues() throws ParseException {
        String cmd = "set<>(\"1\",'10')";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("set<>(\"1\",'10')", query);
    }

    @Test
    public void parseIdenticalRangeLongValues() throws ParseException {
        String cmd = "range=[1,10]";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("range=[1,10]", query);
    }

    @Test
    public void parseIdenticalRangeStringValues() throws ParseException {
        String cmd = "range=['1','10']";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("range=['1','10']", query);
    }

    @Test
    public void parseBasicAttribute() throws ParseException {
        String cmd = "attribute=1";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("attribute=1", query);
    }

    @Test
    public void parseChainAttribute() throws ParseException {
        String cmd = "inner.attribute.1=2";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("inner.attribute.1=2", query);
    }

    @Test
    public void parseAttributeIsNullProperty() throws ParseException {
        String cmd = "attribute is null";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("attribute is null", query);
    }

    @Test
    public void parseAttributeIsNullPropertyIgnoreCase() throws ParseException {
        String cmd = "attribute IS NULL";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("attribute is null", query);
    }

    @Test
    public void parseAttributeIsNotNullProperty() throws ParseException {
        String cmd = "attribute is not null";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("attribute is not null", query);
    }

    @Test
    public void parseAttributeIsNotNullPropertyIgnoreCase() throws ParseException {
        String cmd = "attribute IS NOT NULL";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("attribute is not null", query);
    }

    @Test
    public void parseEmptyQuotedString() throws ParseException {
        String cmd = "s=''";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s=''", query);
    }

    @Test
    public void parseQuotedString() throws ParseException {
        String cmd = "s='a_quoted_string'";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s='a_quoted_string'", query);
    }

    @Test
    public void parseQuotedString_shouldKeepSpace() throws ParseException {
        String cmd = "s='a quoted string'";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s='a quoted string'", query);
    }

    @Test
    public void parseQuotedString_shouldAcceptInnerDoubleQuote() throws ParseException {
        String cmd = "s='\"double_quoted\"'";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s='\"double_quoted\"'", query);
    }

    @Test
    public void parseEmptyDoubleQuotedString() throws ParseException {
        String cmd = "s=\"\"";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s=\"\"", query);
    }

    @Test
    public void parseDoubleQuotedString() throws ParseException {
        String cmd = "s=\"a_quoted_string\"";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s=\"a_quoted_string\"", query);
    }

    @Test
    public void parseDoubleQuotedString_shouldKeepSpace() throws ParseException {
        String cmd = "s=\"a quoted string\"";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s=\"a quoted string\"", query);
    }

    @Test
    public void parseDoubleQuotedString_shouldAcceptInnerSingleQuote() throws ParseException {
        String cmd = "s=\"'single_quoted'\"";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("s=\"'single_quoted'\"", query);
    }

    @Test
    public void parseLong() throws ParseException {
        String cmd = "l=1234567890";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("l=1234567890", query);
    }

    @Test
    public void parseLongWithPositiveSign() throws ParseException {
        String cmd = "l=+1234567890";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("l=+1234567890", query);
    }

    @Test
    public void parseLongWithNegativeSign() throws ParseException {
        String cmd = "l=-1234567890";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("l=-1234567890", query);
    }

    @Test
    public void parseDecimalDouble() throws ParseException {
        String cmd = "d=.123456";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=.123456", query);
    }

    @Test
    public void parseDecimalDoubleWithPositiveSign() throws ParseException {
        String cmd = "d=+.123456";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=+.123456", query);
    }

    @Test
    public void parseDecimalDoubleWithNegativeSign() throws ParseException {
        String cmd = "d=-.123456";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=-.123456", query);
    }

    @Test
    public void parseDoubleExponential() throws ParseException {
        String cmd = "d=123e456";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=123e456", query);
    }

    @Test
    public void parseDoubleExponentialWithPositiveSign() throws ParseException {
        String cmd = "d=+123e+456";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=+123e+456", query);
    }

    @Test
    public void parseDoubleExponentialWithNegativeSign() throws ParseException {
        String cmd = "d=-123e-456";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=-123e-456", query);
    }

    @Test
    public void parseFullDoubleWithExponential() throws ParseException {
        String cmd = "d=123.456e789";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=123.456e789", query);
    }

    @Test
    public void parseFullDoubleWithExponentialWithPositiveSign() throws ParseException {
        String cmd = "d=+123.456e+789";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=+123.456e+789", query);
    }

    @Test
    public void parseFullDoubleWithExponentialWithNegativeSign() throws ParseException {
        String cmd = "d=-123.456e-789";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("d=-123.456e-789", query);
    }

    @Test
    public void parseDate() throws ParseException {
        String cmd = "date=date(\"20251228\")";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("date=20251228", query);
    }

    @Test
    public void parseDateIgnoreCase() throws ParseException {
        String cmd = "date=DaTe(\"20251228\")";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("date=20251228", query);
    }

    @Test
    public void parseTrue() throws ParseException {
        String cmd = "STEP <= true";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<=true", query);
    }

    @Test
    public void parseTrueUpperCase() throws ParseException {
        String cmd = "STEP <= TRUE";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<=true", query);
    }

    @Test
    public void parseTrueIgnoreCase() throws ParseException {
        String cmd = "STEP <= TrUe";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<=true", query);
    }

    @Test
    public void parseFalse() throws ParseException {
        String cmd = "STEP <= false";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<=false", query);
    }

    @Test
    public void parseFalseUpperCase() throws ParseException {
        String cmd = "STEP <= FALSE";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<=false", query);
    }

    @Test
    public void parseFalseIgnoreCase() throws ParseException {
        String cmd = "STEP <= FaLsE";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("STEP<=false", query);
    }

    @Test
    public void parseTimePointOfDateTimeNow() throws ParseException {
        String cmd = "creation = time-point( )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point()", query);
    }

    @Test
    public void parseTimePointWithFullFixedDateTime() throws ParseException {
        String cmd = "creation = time-point(25d2m2025y23H56M45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(25d2m2025y23H56M45S)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetDateTime() throws ParseException {
        String cmd = "creation = time-point(-25d+2m-2025y+23H-56M+45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(-25d+2m-2025y+23H-56M+45S)", query);
    }

    @Test
    public void parseTimePointWithMixedPartialDateTime() throws ParseException {
        String cmd = "creation = time-point(25d 2025y +23H -45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(25d2025y+23H-45S)", query);
    }

    @Test
    public void parseTimePointWithDoubleQuotedZoneDateTime() throws ParseException {
        String cmd = "creation = time-point(\"Europe/Paris\")";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(\"Europe/Paris\")", query);
    }

    @Test
    public void parseTimePointWithQuotedZoneDateTime() throws ParseException {
        String cmd = "creation = time-point('Europe/Paris')";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point('Europe/Paris')", query);
    }

    @Test
    public void parseTimePointWithUtcZoneDateTime() throws ParseException {
        String cmd = "creation = time-point(utc)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(UTC)", query);
    }

    @Test
    public void parseTimePointOfDateNow() throws ParseException {
        String cmd = "creation = time-point( date )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date)", query);
    }

    @Test
    public void parseTimePointOfUtcZoneDateNow() throws ParseException {
        String cmd = "creation = time-point( date, UTC )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,UTC)", query);
    }

    @Test
    public void parseTimePointOfDoubleQuotedZoneDateNow() throws ParseException {
        String cmd = "creation = time-point( date, \"Europe/Paris\" )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,\"Europe/Paris\")", query);
    }

    @Test
    public void parseTimePointOfQuotedZoneDateNow() throws ParseException {
        String cmd = "creation = time-point( date, 'Europe/Paris' )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,'Europe/Paris')", query);
    }

    @Test
    public void parseTimePointWithFullFixedDate() throws ParseException {
        String cmd = "creation = time-point(date,25d 2m 2025y)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,25d2m2025y)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetDate() throws ParseException {
        String cmd = "creation = time-point(date, -25d +2m -2025y)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,-25d+2m-2025y)", query);
    }

    @Test
    public void parseTimePointWithMixedPartialDate() throws ParseException {
        String cmd = "creation = time-point(date, +2m -2025y)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,+2m-2025y)", query);
    }


    @Test
    public void parseTimePointWithFullFixedUtcZoneDate() throws ParseException {
        String cmd = "creation = time-point(date, utc, 23H 56M 45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,UTC,23H56M45S)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetUtcZoneDate() throws ParseException {
        String cmd = "creation = time-point(date, utc , +23H -56M +45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,UTC,+23H-56M+45S)", query);
    }

    @Test
    public void parseTimePointWithFullFixedQuotedZoneDate() throws ParseException {
        String cmd = "creation = time-point(date, 'Europe/Paris', 25d 2m 2025y)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,'Europe/Paris',25d2m2025y)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetQuotedZoneDate() throws ParseException {
        String cmd = "creation = time-point(date, 'Europe/Paris' , +25d -2m +2025y)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,'Europe/Paris',+25d-2m+2025y)", query);
    }

    @Test
    public void parseTimePointWithFullFixedDoubleQuotedZoneDate() throws ParseException {
        String cmd = "creation = time-point(date, \"Europe/Paris\", 25d 2m 2025y)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,\"Europe/Paris\",25d2m2025y)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetDoubleQuotedZoneDate() throws ParseException {
        String cmd = "creation = time-point(date, \"Europe/Paris\" , -25d+2m-2025y)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(date,\"Europe/Paris\",-25d+2m-2025y)", query);
    }

    @Test
    public void parseTimePointOfTimeNow() throws ParseException {
        String cmd = "creation = time-point( time )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time)", query);
    }

    @Test
    public void parseTimePointOfUtcZoneTimeNow() throws ParseException {
        String cmd = "creation = time-point( time, UTC )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,UTC)", query);
    }

    @Test
    public void parseTimePointOfDoubleQuotedTimeDateNow() throws ParseException {
        String cmd = "creation = time-point( time, \"Europe/Paris\" )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,\"Europe/Paris\")", query);
    }

    @Test
    public void parseTimePointOfQuotedZoneTimeNow() throws ParseException {
        String cmd = "creation = time-point( time, 'Europe/Paris' )";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,'Europe/Paris')", query);
    }

    @Test
    public void parseTimePointWithFullFixedTime() throws ParseException {
        String cmd = "creation = time-point(time, 23H 56M 45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,23H56M45S)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetTime() throws ParseException {
        String cmd = "creation = time-point(time, +23H -56M +45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,+23H-56M+45S)", query);
    }

    @Test
    public void parseTimePointWithMixedPartialTime() throws ParseException {
        String cmd = "creation = time-point(time, +23H +45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,+23H+45S)", query);
    }

    @Test
    public void parseTimePointWithFullFixedUtcZoneTime() throws ParseException {
        String cmd = "creation = time-point(time, utc, 23H 56M 45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,UTC,23H56M45S)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetUtcZoneTime() throws ParseException {
        String cmd = "creation = time-point(time, utc , +23H -56M +45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,UTC,+23H-56M+45S)", query);
    }

    @Test
    public void parseTimePointWithFullFixedQuotedZoneTime() throws ParseException {
        String cmd = "creation = time-point(time, 'Europe/Paris', 23H 56M 45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,'Europe/Paris',23H56M45S)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetQuotedZoneTime() throws ParseException {
        String cmd = "creation = time-point(time, 'Europe/Paris' , +23H -56M +45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,'Europe/Paris',+23H-56M+45S)", query);
    }

    @Test
    public void parseTimePointWithFullFixedDoubleQuotedZoneTime() throws ParseException {
        String cmd = "creation = time-point(time, \"Europe/Paris\", 23H 56M 45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,\"Europe/Paris\",23H56M45S)", query);
    }

    @Test
    public void parseTimePointWithFullOffsetDoubleQuotedZoneTime() throws ParseException {
        String cmd = "creation = time-point(time, \"Europe/Paris\" , +23H -56M +45S)";
        Query ltm = makeQueryParser(cmd);
        var query = parse(ltm.criterias());
        assertEquals("creation=time-point(time,\"Europe/Paris\",+23H-56M+45S)", query);
    }

    @Test
    @Disabled
    public void tokenizeFailure() {
        String cmd = "STOP 10";
        SimpleCharStream cs = new SimpleCharStream(new StringReader(cmd));
        QueryTokenManager ltm = new QueryTokenManager(cs);
        var thrown = Assertions.assertThrows(TokenMgrError.class, () -> {
            ltm.getNextToken();
        });
    }
}
