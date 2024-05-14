package com.hits.liid.forumx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MessageEntity {
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

    @Column(name = "text")
    @NotBlank
    private String text;

//    @Column(name = "topic_id")
//    @NotNull
//    private UUID topicId;

    @ManyToOne
    @JoinColumn(name="topic_id")
    private TopicEntity parentTopic;
}
