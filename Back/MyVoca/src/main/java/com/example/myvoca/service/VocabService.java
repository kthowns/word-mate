package com.example.myvoca.service;

import com.example.myvoca.dto.CreateVocab;
import com.example.myvoca.dto.VocabDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.exception.ApiException;
import com.example.myvoca.repository.UserRepository;
import com.example.myvoca.repository.VocabRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.myvoca.code.ApiResponseCode.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class VocabService {
    private final VocabRepository vocabRepository;
    private final UserRepository userRepository;

    public List<VocabDto> getVocabs(Integer userId) {
        return vocabRepository.findByUser(getUserById(userId))
                .stream().map(VocabDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreateVocab.Response createVocab(Integer userId, CreateVocab.Request request) {
        validateVocabDuplicate(request.getTitle(), userId);
        Vocab vocab = Vocab.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(getUserById(userId))
                .wordCount(0)
                .build();
        vocab = vocabRepository.save(vocab);

        return CreateVocab.Response.fromEntity(vocab);
    }

    public VocabDto getVocabDtoById(Integer vocabId){
        return VocabDto.fromEntity(getVocabById(vocabId));
    }

    @Transactional
    public VocabDto editVocab(Integer vocabId, CreateVocab.Request request) {
        Vocab vocab = getVocabById(vocabId);
        vocab.setTitle(request.getTitle());
        vocab.setDescription(request.getDescription());

        return VocabDto.fromEntity(vocab);
    }

    @Transactional
    public VocabDto deleteVocab(Integer vocabId) {
        Vocab vocab = getVocabById(vocabId);
        vocabRepository.delete(vocab);
        return VocabDto.fromEntity(vocab);
    }

    private Vocab getVocabById(Integer vocabId){
        return vocabRepository.findById(vocabId)
                .orElseThrow(() -> new ApiException(NO_VOCAB));
    }

    private User getUserById(Integer userId) {
       return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NO_USER));
    }

    private void validateVocabDuplicate(String title, Integer userId){
        vocabRepository.findByTitleAndUser(title, getUserById(userId))
                .ifPresent((e) -> { throw new ApiException(DUPLICATED_TITLE); });
    }
}