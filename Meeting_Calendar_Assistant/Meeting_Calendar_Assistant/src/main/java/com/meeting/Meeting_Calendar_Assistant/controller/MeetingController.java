package com.meeting.Meeting_Calendar_Assistant.controller;

import com.meeting.Meeting_Calendar_Assistant.model.Meeting;
import com.meeting.Meeting_Calendar_Assistant.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/book")
    public Meeting bookMeeting(@RequestBody Meeting meeting) {
        return meetingService.bookMeeting(meeting);
    }

    @GetMapping("/free-slots/{employeeId}")
    public List<LocalDateTime> findFreeSlots(@PathVariable Long employeeId, @RequestParam int durationMinutes) {
        Duration duration = Duration.ofMinutes(durationMinutes);
        return meetingService.findFreeSlots(employeeId, duration);
    }

    @PostMapping("/conflicts")
    public List<Long> findConflictedParticipants(@RequestBody Meeting meetingRequest, @RequestParam List<Long> participantIds) {
        return meetingService.findConflictedParticipants(meetingRequest, participantIds);
    }


}
