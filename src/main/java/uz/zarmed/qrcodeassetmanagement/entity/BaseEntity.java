package uz.zarmed.qrcodeassetmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updateAt;
    @Column(nullable = false)
    private Boolean isDeleted=false;

    private LocalDateTime deletedAt;

    private String deletedBy;

    @PrePersist
    protected void onCreate(){
        this.createdAt=LocalDateTime.now();
        this.updateAt=LocalDateTime.now();
        if(this.isDeleted==null){
            this.isDeleted=false;
        }
    }

    @PreUpdate

    protected void onUpdate(){
        this.updateAt=LocalDateTime.now();
    }
}
