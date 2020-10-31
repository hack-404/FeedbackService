package com.oyo.feedback.model.entity;

import com.oyo.feedback.model.enums.Label;
import lombok.*;

import javax.persistence.*;

import com.oyo.feedback.model.enums.SourceType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Id;

import java.io.Serializable;

@Entity
@Table(name = "feedback_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    private String bookingId;

    private String hotelId;

    private Integer source;

    private String roomId;

    private String description;

    @Enumerated(EnumType.STRING)
    private Label label;

    private Integer rating;

    private String email;

}
