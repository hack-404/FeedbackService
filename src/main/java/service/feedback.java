package com.oyo.feedback.services;

import com.oyo.feedback.constants.GenericConstants;
import com.oyo.feedback.model.dto.FeedbackResponse;
import com.oyo.feedback.model.entity.ExtensionEntity;
import com.oyo.feedback.model.entity.FeedbackEntity;
import com.oyo.feedback.model.entity.FeedbackTrackerEntity;
import com.oyo.feedback.model.enums.Label;
import com.oyo.feedback.model.enums.Status;
import com.oyo.feedback.repositories.ExtensionRepository;
import com.oyo.feedback.repositories.FeedbackRepository;
import com.oyo.feedback.repositories.FeedbackTrackerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.java2d.loops.GeneralRenderer;

import javax.mail.MessagingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackTrackerRepository feedbackTrackerRepository;

    @Autowired
    private ExtensionRepository extensionRepository;

    @Autowired
    private JavaEmail javaEmailService;

    public Map<String, List<FeedbackResponse>> getFeedbackData(String hotelId, Integer rating) {
        List<FeedbackEntity> feedbackEntityList = feedbackRepository.findByHotelId(hotelId);
        Map<String, List<FeedbackResponse>> response = new HashMap<>();

        List<Integer> feedbackIds =
                feedbackEntityList.stream().filter(feedbackEntity -> feedbackEntity.getRating() <= rating).
                        map(FeedbackEntity::getId).collect(
                        Collectors.toList());

        List<FeedbackTrackerEntity> feedbackTrackerEntityList =
                feedbackTrackerRepository.findByFeedbackIdIn(feedbackIds);

        List<Integer> trackerIds =
                feedbackTrackerEntityList.stream().map(FeedbackTrackerEntity::getId).collect(Collectors.toList());

        List<ExtensionEntity> extensionEntitiesList = new ArrayList<>();


        for (FeedbackEntity feedbackEntity: feedbackEntityList) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setRating(feedbackEntity.getRating());
            feedbackResponse.setComment(feedbackEntity.getDescription());
            feedbackResponse.setFeedbackId(feedbackEntity.getId());
            feedbackResponse.setLabel(feedbackEntity.getLabel());

            for (FeedbackTrackerEntity feedbackTrackerEntity : feedbackTrackerEntityList ){
                if(feedbackTrackerEntity.getFeedbackId().equals(feedbackEntity.getId())){
                    feedbackResponse.setStatus(feedbackTrackerEntity.getStatus());
                    feedbackResponse.setStartTime(feedbackTrackerEntity.getStartTime());
                    feedbackResponse.setDuration(feedbackTrackerEntity.getDuration());
                    feedbackResponse.setPenalty(feedbackTrackerEntity.getPenalty());
                    feedbackResponse.setPenaltyDuration(Label.getPenaltyTimeMap(feedbackEntity.getLabel()));
                    feedbackResponse.setExtension(feedbackTrackerEntity.getExtensionAvailable());

                    ExtensionEntity extensionEntity = extensionRepository.findByTrackerId(feedbackTrackerEntity.getId());
                    if(extensionEntity!=null){
                        feedbackResponse.setExtensionPeriod(extensionEntity.getExtensionPeriod());
                        feedbackResponse.setExtensionStartTime(extensionEntity.getExtensionStartTime());
                    }
                }
            }
            if(response.containsKey(feedbackEntity.getRoomId())){
                response.get(feedbackEntity.getRoomId()).add(feedbackResponse);
            }else {
                List<FeedbackResponse> list = new ArrayList<>();
                list.add(feedbackResponse);
                response.put(feedbackEntity.getRoomId(), list);
            }
        }
        return response;
    }

    @Transactional
    public void changeStatus(Integer feedbackId, Status status) throws Exception {
        try {
            Optional<FeedbackEntity> feedbackEntity = feedbackRepository.findById(feedbackId);
            if (feedbackEntity.isPresent()) {
                FeedbackTrackerEntity feedbackTrackerEntity = feedbackTrackerRepository.findByFeedbackId(feedbackId);
                if(feedbackTrackerEntity!=null) {

                    feedbackTrackerEntity.setStatus(status);
                    feedbackTrackerRepository.save(feedbackTrackerEntity);
                    if(status.equals(Status.EXPIRED)){
                        javaEmailService.sendMail("hasan.alirajpurwala@oyorooms.com", GenericConstants.mailBody2,
                                                  GenericConstants.mailSubject2);
                    }
                    javaEmailService.sendMail(feedbackEntity.get().getEmail(), GenericConstants.mailBody1,
                                              GenericConstants.mailSubject1);

                }
                else {
                    log.error("No job tracker associated with feedbackId:{} ",feedbackId);
                    throw new Exception();
                }
            }
            else {
                log.error("No such feedbackId exists {}",feedbackId);
            }
        }catch (MessagingException e) {
            log.error("Status change Operation got failed for id: {}",feedbackId);
            throw new Exception();
        }catch (Exception e){
            log.error("Status Success\\\"");
        }
    }
}
