package com.taskFlow.demo.Model.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.EnableMBeanExport;

@Entity
@Getter
@Setter
@Table(name = "Tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "le_nom")
    private  String le_nom;
    @Column(name = "description")
    private  String description;
    @Column(name = "image")
    private String image;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
