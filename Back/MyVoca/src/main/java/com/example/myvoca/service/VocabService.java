package com.example.myvoca.service;

import com.example.myvoca.dto.CreateVocab;
import com.example.myvoca.dto.EditVocab;
import com.example.myvoca.dto.VocabDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.VocabWord;
import com.example.myvoca.entity.Word;
import com.example.myvoca.repository.UserRepository;
import com.example.myvoca.repository.VocabRepository;
import com.example.myvoca.repository.VocabWordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class VocabService {
    private final VocabRepository vocabRepository;
    private final VocabWordRepository vocabWordRepository;
    private final UserRepository userRepository;

    public List<VocabDto> getVocabByUserId(Integer userId) {
        return vocabRepository.findByUser_userId(getUserById(userId).getUserId())
                .stream().map(VocabDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Word> getWordsByVocabId(Integer vocabId) {
        Vocab vocab = getVocabById(vocabId);
        return vocabWordRepository.findWordByVocabId(vocab.getVocabId());
    }

    @Transactional
    public CreateVocab.Response createVocab(CreateVocab.Request request) {
        Vocab vocab = Vocab.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(getUserById(request.getUserId()))
                .build();
        vocabRepository.save(vocab);

        return CreateVocab.Response.fromEntity(vocab);
    }

    public User getUserById(Integer userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(NoSuchElementException::new);
        return user;
    }

    public Vocab getVocabById(Integer vocabId){
        Vocab vocab = vocabRepository.findById(vocabId)
                .orElseThrow(NoSuchElementException::new);
        return vocab;
    }

    @Transactional
    public VocabDto editVocab(Integer vocabId, EditVocab.Request request) {
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
}