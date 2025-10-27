package com.red_badger.Martian.Robots.controller;


import com.red_badger.Martian.Robots.model.Position;
import com.red_badger.Martian.Robots.model.RobotInput;
import com.red_badger.Martian.Robots.service.RobotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("robotInput", new RobotInput(5, 3, 1, 1, "E", "RFRFRFRF"));
        model.addAttribute("sampleInputs", sampleInputs());
        return "index";
    }

    @PostMapping("/process")
    public String process(@Valid @ModelAttribute RobotInput input, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Invalid input. Please check values.");
            model.addAttribute("sampleInputs", sampleInputs());
            return "index";
        }
        try {
            var results = robotService.processMultipleRobots(input);
            model.addAttribute("results", results).addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("robotInput", input).addAttribute("sampleInputs", sampleInputs());
        return "index";
    }

    @GetMapping("/api/process")
    @ResponseBody
    public List<Position> apiProcess(@Valid RobotInput input) {
        return robotService.processMultipleRobots(input);
    }

    private List<RobotInput> sampleInputs() {
        return List.of(
                new RobotInput(5, 3, 1, 1, "E", "RFRFRFRF"),
                new RobotInput(5, 3, 3, 2, "N", "FRRFLLFFRRFLL"),
                new RobotInput(5, 3, 0, 3, "W", "LLFFFLFLFL")
        );
    }
}