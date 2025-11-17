package com.mysite.sbb.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    private int age;

    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;
}
