package com.daangndaangn.chatserver.controller.participant;

import com.daangndaangn.chatserver.service.ParticipantService;
import com.daangndaangn.common.chat.document.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
