package com.hits.liid.forumx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

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

    @ManyToOne
    @JoinColumn(name="parent_category_id")
    private CategoryEntity parentCategory;

    @ManyToMany
    @JoinTable(
            name = "user_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> moderators = new HashSet<>();

    public static CategoryEntity of (UUID id, UserEntity userCreator, LocalDateTime creationDate, LocalDateTime editingDate, String name, CategoryEntity parentCategory){
        return new CategoryEntity(id, userCreator, creationDate, editingDate, name, parentCategory, null);
    }

}
