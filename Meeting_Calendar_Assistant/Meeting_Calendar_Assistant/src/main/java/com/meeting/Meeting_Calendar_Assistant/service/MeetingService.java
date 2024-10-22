package com.meeting.Meeting_Calendar_Assistant.service;

import com.meeting.Meeting_Calendar_Assistant.model.Meeting;
import com.meeting.Meeting_Calendar_Assistant.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public Meeting bookMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public List<LocalDateTime> findFreeSlots(Long employeeId, Duration duration) {
        List<Meeting> meetings = meetingRepository.findByOwnerId(employeeId);
        // Assuming we check against a 24-hour time frame from now
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.plusDays(1);
        List<LocalDateTime> freeSlots = new ArrayList<>();

        for (LocalDateTime time = now; time.plus(duration).isBefore(endOfDay); time = time.plusMinutes(30)) {
            boolean isFree = true;
            for (Meeting meeting : meetings) {
                if (time.isBefore(meeting.getEndTime()) && time.plus(duration).isAfter(meeting.getStartTime())) {
                    isFree = false;
                    break;
                }
            }
            if (isFree) {
                freeSlots.add(time);
            }
        }
        return freeSlots;
    }

    public List<Long> findConflictedParticipants(Meeting meetingRequest, List<Long> participantIds) {
        List<Long> conflictedParticipants = new ArrayList<>();
        for (Long participantId : participantIds) {
            List<Meeting> meetings = meetingRepository.findByOwnerId(participantId);
            for (Meeting meeting : meetings) {
                if (meeting.getStartTime().isBefore(meetingRequest.getEndTime()) &&
                        meeting.getEndTime().isAfter(meetingRequest.getStartTime())) {
                    conflictedParticipants.add(participantId);
                    break;
                }
            }
        }
        return conflictedParticipants;
    }

}
