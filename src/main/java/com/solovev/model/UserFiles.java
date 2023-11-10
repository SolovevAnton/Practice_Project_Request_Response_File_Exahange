package com.solovev.model;

import com.solovev.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name ="user_files")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class UserFiles implements DTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String fileName;
    private String serverFileName;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public UserFiles(@NonNull String fileName, String serverFileName, User user) {
        this.fileName = fileName;
        this.serverFileName = serverFileName;
        this.user = user;
    }
}
