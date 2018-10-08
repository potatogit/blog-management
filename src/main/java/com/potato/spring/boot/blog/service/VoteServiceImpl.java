package com.potato.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.potato.spring.boot.blog.domain.Vote;
import com.potato.spring.boot.blog.repository.VoteRepository;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Override public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
