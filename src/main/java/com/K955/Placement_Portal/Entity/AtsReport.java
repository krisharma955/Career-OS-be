package com.K955.Placement_Portal.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "ats_reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AtsReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    Resume resume;

    Integer overallScore;

    Integer skillScore;

    Integer experienceScore;

    Integer educationScore;

    Integer formattingScore;

    @Builder.Default
    List<String> missingKeywords = new ArrayList<>();

    @Builder.Default
    List<String> suggestions = new ArrayList<>();

    @Builder.Default
    List<String> strengths = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    Instant createdAt;

}
