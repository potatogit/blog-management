package com.waylau.spring.boot.blog.service;

import com.waylau.spring.boot.blog.domain.Vote;

public interface VoteService {
    Vote getVoteById(Long id);

    void removeVote(Long id);
}
