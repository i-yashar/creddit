package bg.softuni.creddit.model.entity;

import javax.persistence.*;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "galleries")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer picture_count;

    private LocalDateTime createdOn;

    @OneToOne
    private User owner;

    @OneToMany(mappedBy = "gallery")
    private Set<Picture> pictures;

    public Gallery() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPicture_count() {
        return picture_count;
    }

    public void setPicture_count(Integer picture_count) {
        this.picture_count = picture_count;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }
}
