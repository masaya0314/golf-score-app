package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_record")
public class ScoreRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "名前を入力してください")
    private String name;
    @NotNull(message = "スコアを入力してください")
    @Min(value = 50, message = "スコアは50以上にしてください")
    @Max(value = 200, message = "スコアは200以下にしてください")
    private Integer score;
    @NotBlank(message = "コースを選択してください")
    private String course;
    private LocalDateTime createdAt;

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Integer getScore(){
        return score;
    }
    public void setScore(Integer score){
        this.score = score;
    }
    public String getCourse(){
        return course;
    }
    public void setCourse(String course){
        this.course = course;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
@PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
}

}
