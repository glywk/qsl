package org.glywk.builder;

import java.util.Optional;

public record TimeCategory(TimePointCategory category, Optional<TimeZoneValue> timeZone) {
}
