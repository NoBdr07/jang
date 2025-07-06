package com.bdr.jang.service;

import com.bdr.jang.entities.payload.AttemptLight;

import java.util.List;

public interface AttemptService {

    void recordSeries(Long userId, List<AttemptLight> attempts);
}
