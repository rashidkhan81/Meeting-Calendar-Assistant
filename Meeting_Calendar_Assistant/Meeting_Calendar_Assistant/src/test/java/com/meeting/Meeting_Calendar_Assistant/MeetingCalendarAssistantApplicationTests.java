package com.meeting.Meeting_Calendar_Assistant;


import com.meeting.Meeting_Calendar_Assistant.model.Meeting;
import com.meeting.Meeting_Calendar_Assistant.repository.MeetingRepository;
import com.meeting.Meeting_Calendar_Assistant.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MeetingCalendarAssistantApplicationTests {

	@InjectMocks
	private MeetingService meetingService;

	@Mock
	private MeetingRepository meetingRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void contextLoads() {
		// This test simply checks if the application context loads successfully
	}

	@Test
	public void testBookMeeting() {
		Meeting meeting = new Meeting();
		meeting.setOwnerId(1L);
		meeting.setStartTime(LocalDateTime.of(2024, 10, 22, 10, 0));
		meeting.setEndTime(LocalDateTime.of(2024, 10, 22, 11, 0));

		when(meetingRepository.save(meeting)).thenReturn(meeting);

		Meeting bookedMeeting = meetingService.bookMeeting(meeting);
		assertNotNull(bookedMeeting);
		assertEquals(meeting.getOwnerId(), bookedMeeting.getOwnerId());
		assertEquals(meeting.getStartTime(), bookedMeeting.getStartTime());
		assertEquals(meeting.getEndTime(), bookedMeeting.getEndTime());
	}

	@Test
	public void testFindFreeSlots() {
		List<Meeting> meetings = new ArrayList<>();
		meetings.add(new Meeting(1L, 1L, LocalDateTime.of(2024, 10, 22, 10, 0), LocalDateTime.of(2024, 10, 22, 11, 0)));
		meetings.add(new Meeting(2L, 1L, LocalDateTime.of(2024, 10, 22, 12, 0), LocalDateTime.of(2024, 10, 22, 13, 0)));

		when(meetingRepository.findByOwnerId(1L)).thenReturn(meetings);

		List<LocalDateTime> freeSlots = meetingService.findFreeSlots(1L, Duration.ofMinutes(30));

		assertNotNull(freeSlots);
		assertEquals(3, freeSlots.size()); // Expecting 3 free slots
		assertEquals(LocalDateTime.of(2024, 10, 22, 11, 0), freeSlots.get(0));
		assertEquals(LocalDateTime.of(2024, 10, 22, 11, 30), freeSlots.get(1));
		assertEquals(LocalDateTime.of(2024, 10, 22, 13, 0), freeSlots.get(2));
	}

	@Test
	public void testFindConflictedParticipants() {
		List<Meeting> meetings = new ArrayList<>();
		meetings.add(new Meeting(1L, 1L, LocalDateTime.of(2024, 10, 22, 10, 0), LocalDateTime.of(2024, 10, 22, 11, 0)));
		meetings.add(new Meeting(2L, 2L, LocalDateTime.of(2024, 10, 22, 11, 30), LocalDateTime.of(2024, 10, 22, 12, 30)));

		when(meetingRepository.findByOwnerId(1L)).thenReturn(meetings);
		when(meetingRepository.findByOwnerId(2L)).thenReturn(meetings);

		Meeting meetingRequest = new Meeting();
		meetingRequest.setStartTime(LocalDateTime.of(2024, 10, 22, 10, 30));
		meetingRequest.setEndTime(LocalDateTime.of(2024, 10, 22, 11, 30));

		List<Long> participantIds = List.of(1L, 2L);
		List<Long> conflictedParticipants = meetingService.findConflictedParticipants(meetingRequest, participantIds);

		assertNotNull(conflictedParticipants);
		assertEquals(1, conflictedParticipants.size());
		assertEquals(1L, conflictedParticipants.get(0));
	}
}
