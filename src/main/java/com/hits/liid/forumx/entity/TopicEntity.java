package com.hits.liid.forumx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "topic")
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @Column(name = "user_creator_id")
//    @NotNull
//    private UUID userCreatorId;

    @ManyToOne
    @JoinColumn(name="user_creator_id")
    private UserEntity userCreator;

    @Column(name = "creation_date")
    @NotNull
    private LocalDateTime creationDate;

    @Column(name = "editing_date")
    private LocalDateTime editingDate;

    @Column(name = "name")
    @NotBlank
    private String name;

//    @Column(name = "category_id")
//    @NotNull
//    private UUID categoryId;

    @ManyToOne
    @JoinColumn(name="category_id")
    private CategoryEntity parentCategory;
}
