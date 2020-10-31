package com.oyo.feedback.controllers;


import com.oyo.feedback.model.entity.FeedbackEntity;
import com.oyo.feedback.model.enums.Label;
import com.oyo.feedback.services.CreateFeedbackService;
import com.oyo.feedback.services.FeedBackTrackerService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class CreateFeedbackController {

    @Autowired
    CreateFeedbackService createFeedbackService;

    @Autowired
    FeedBackTrackerService feedBackTrackerService;
    @CrossOrigin
    @PostMapping(value = "/create_feedback_entry")
    public ResponseEntity<String> createFeedbackEntry(@RequestBody HashMap<String , String> map){
        FeedbackEntity feedbackEntity = populateEntity(map);
        FeedbackEntity saved =  createFeedbackService.userFeedbackEntry(feedbackEntity);
        feedBackTrackerService.createTracker(feedbackEntity);
        return ResponseEntity.ok().body("");
    }

    private FeedbackEntity populateEntity(HashMap<String , String> map){
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setRating(Integer.parseInt(map.get("rating")));
        feedbackEntity.setBookingId("ABC123");
        feedbackEntity.setDescription(map.get("description"));
        feedbackEntity.setRoomId(map.get("roomId"));
        feedbackEntity.setLabel(Label.valueOf(map.get("label")));
        feedbackEntity.setEmail(map.get("email"));
        feedbackEntity.setHotelId(map.get("hotelId"));
        return feedbackEntity;
    }

}
