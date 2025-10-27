package com.red_badger.martian.robots.controller;


import com.red_badger.martian.robots.model.Position;
import com.red_badger.martian.robots.model.RobotInput;
import com.red_badger.martian.robots.service.RobotService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
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
@OpenAPIDefinition(
        info = @Info(title = "Martian Robots API", version = "1.0", description = "Control robots on Mars")
)
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

    @Operation(summary = "Process robot instructions via API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Position.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
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