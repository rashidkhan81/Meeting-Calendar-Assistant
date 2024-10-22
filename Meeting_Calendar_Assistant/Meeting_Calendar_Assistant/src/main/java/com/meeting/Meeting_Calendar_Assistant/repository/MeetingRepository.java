package com.meeting.Meeting_Calendar_Assistant.repository;

import com.meeting.Meeting_Calendar_Assistant.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    List<Meeting> findByOwnerId(Long ownerId);
}
