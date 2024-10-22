package com.meeting.Meeting_Calendar_Assistant.exception;

public class MeetingConflictException extends RuntimeException{

    public MeetingConflictException(String message) {
        super(message);
    }
}
