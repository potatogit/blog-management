package com.potato.spring.boot.blog.service;

import com.potato.spring.boot.blog.domain.Vote;

public interface VoteService {
    Vote getVoteById(Long id);

    void removeVote(Long id);
}
