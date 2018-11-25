package com.demo.common.id;

public interface EntityIdGenerator {
    public long generateLongId() throws InvalidSystemClockException, GetHardwareIdFailedException;

    public String generateLongIdString() throws InvalidSystemClockException, GetHardwareIdFailedException;
}