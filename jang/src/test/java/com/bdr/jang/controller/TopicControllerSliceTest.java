package com.bdr.jang.controller;

import com.bdr.jang.entities.dto.TopicDTO;
import com.bdr.jang.security.AuthTokenFilter;
import com.bdr.jang.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TopicController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class TopicControllerSliceTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TopicService topicService;

    @Test
    void getAllTopics_shouldReturnListOfTopics() throws Exception {
        List<TopicDTO> topics = List.of(
                new TopicDTO(1L, "Sujet A"),
                new TopicDTO(2L, "Sujet B")
        );
        given(topicService.getAllTopics()).willReturn(topics);

        mockMvc.perform(get("/topics")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(topics.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sujet A"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Sujet B"));
    }
}
