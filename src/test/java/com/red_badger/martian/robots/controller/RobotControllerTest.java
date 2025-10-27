package com.red_badger.martian.robots.controller;

import com.red_badger.martian.robots.model.Orientation;
import com.red_badger.martian.robots.model.Position;
import com.red_badger.martian.robots.model.RobotInput;
import com.red_badger.martian.robots.service.RobotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RobotController.class)
class RobotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RobotService robotService;

    @Test
    void shouldShowErrorOnInvalidInput() throws Exception {
        mockMvc.perform(post("/process")
                        .param("gridMaxX", "0")
                        .param("gridMaxY", "3")
                        .param("startX", "1")
                        .param("startY", "1")
                        .param("startOrientation", "X")
                        .param("instructions", "INVALID"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("robotInput", new RobotInput(0, 3, 1, 1, "X", "INVALID")));
    }

    @Test
    void shouldReturnJsonViaApi() throws Exception {
        var result = new Position(1, 1, Orientation.E);
        when(robotService.processMultipleRobots(any())).thenReturn(List.of(result));

        mockMvc.perform(get("/api/process")
                        .param("gridMaxX", "5")
                        .param("gridMaxY", "3")
                        .param("startX", "1")
                        .param("startY", "1")
                        .param("startOrientation", "E")
                        .param("instructions", "RFRFRFRF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].x").value(1))
                .andExpect(jsonPath("$[0].y").value(1))
                .andExpect(jsonPath("$[0].orientation").value("E"))
                .andExpect(jsonPath("$[0].lost").value(false));
    }

    @Test
    void shouldReturn400OnInvalidApiInput() throws Exception {
        mockMvc.perform(get("/api/process")
                        .param("gridMaxX", "0")
                        .param("startOrientation", "Z"))
                .andExpect(status().isBadRequest());
    }
}