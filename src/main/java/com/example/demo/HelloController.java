package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class HelloController {
    private final ScoreRecordRepository scoreRecordRepository;
    public HelloController(ScoreRecordRepository scoreRecordRepository){
        this.scoreRecordRepository = scoreRecordRepository;
    }
    @GetMapping("/")
    public String hello(
            @RequestParam(defaultValue = "0") int page,
            Model model){
        Page<ScoreRecord> scorePage = scoreRecordRepository.findAll(
                PageRequest.of(page, 5)
        );
        model.addAttribute("scores",
                scorePage.getContent());
        model.addAttribute("count",
                scorePage.getTotalElements());
        model.addAttribute("name","");
        model.addAttribute("course", "");
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", scorePage.hasNext());
        model.addAttribute("hasPrevious", scorePage.hasPrevious());
        model.addAttribute("totalPages", scorePage.getTotalPages());
        model.addAttribute(
                "pages",
                java.util.stream.IntStream.range(0, scorePage.getTotalPages()).boxed().toList()
        );
        model.addAttribute("scoreRecord",new ScoreRecord());
        return "hello";
    }
    @GetMapping("/search")
    public String search(
            @RequestParam("name") String name,
            @RequestParam("course") String course,
            Model model){
        List<ScoreRecord> scores = scoreRecordRepository.findByNameContainingAndCourseContaining(
                name,
                course);
        model.addAttribute("scores", scores);
        model.addAttribute("count", scores.size());
        model.addAttribute("name", name);
        model.addAttribute("course", course);
        model.addAttribute("scoreRecord", new ScoreRecord());
        return "hello";
    }
    @PostMapping("/result")
    public String result(
            @Valid ScoreRecord record,
            BindingResult bindingResult,
            Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("scoreRecord", record);
            return "hello";
        }
        model.addAttribute("name", record.getName());
        model.addAttribute("score", record.getScore());
        model.addAttribute("course", record.getCourse());
            scoreRecordRepository.save(record);
            List<String> recommendations;
            System.out.println("score=" + record.getScore());
            if (record.getScore() <= 100) {
                recommendations = List.of(
                        "石川遼コース",
                        "やばいコース",
                        "すごいコース"
                );
            } else {
                recommendations = List.of(
                        "簡単コース",
                        "初心者コース"
                );
            }
            model.addAttribute("recommendations", recommendations);
        return "result";
    }
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id){
        scoreRecordRepository.deleteById(id);
        return "redirect:/";
    }
    @PostMapping("/update")
    public String update(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("score") String score,
            @RequestParam("course") String course,
            Model model){
        if(name.isEmpty()){
            model.addAttribute("message", "名前を入力してください");
            return edit(id, model);
        }
        if(score.trim().isEmpty()){
            model.addAttribute("message", "スコアを入力してください");
            return edit(id, model);
        }
        if(Integer.parseInt(score) > 200 || Integer.parseInt(score) < 0){
            model.addAttribute("message", "スコアは０～２００で入力してください");
            return edit(id, model);
        }
        ScoreRecord record = scoreRecordRepository.findById(id).get();
        record.setName(name);
        record.setScore(Integer.parseInt(score));
        record.setCourse(course);
        scoreRecordRepository.save(record);
        return "redirect:/";
    }
    @GetMapping("/edit")
    public String edit(@RequestParam("id") Integer id, Model model){
        model.addAttribute("score", scoreRecordRepository.findById(id).get());
        return "edit";
    }
    @GetMapping("/searchName")
    public String searchName(
            @RequestParam("name") String name,
            Model model){
        List<ScoreRecord> scores =
                scoreRecordRepository.findByNameContaining(name);
        model.addAttribute("scores",scores);
        model.addAttribute("count", scores.size());
        return "hello";
    }
    @GetMapping("/sortDesc")
    public String sortDesc(Model model){
        model.addAttribute(
                "scores",
                scoreRecordRepository.findAllByOrderByScoreDesc()
        );
        model.addAttribute("scoreRecord", new ScoreRecord());
        return "hello";
    }
    @GetMapping("/sortAsc")
    public String sortAsc(Model model){
        model.addAttribute(
                "scores",
                scoreRecordRepository.findAllByOrderByScoreAsc()
        );
        model.addAttribute("scoreRecord", new ScoreRecord());
        return "hello";
    }
}


