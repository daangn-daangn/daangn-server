package com.daangndaangn.apiserver.controller.participant;

import com.daangndaangn.apiserver.service.participant.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chat/participants")
@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

//    @GetMapping("/{userId}")
//    public ParticipantResponse.GetResponse func(Long userId, Pageable pageable) {
//        List<Participant> participants = participantService.getParticipants(userId, pageable);
//
//    }

}
