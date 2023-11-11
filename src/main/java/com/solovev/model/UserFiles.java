package com.solovev.model;

import com.solovev.dto.DTO;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "user_files",
        uniqueConstraints = @UniqueConstraint(columnNames = {"file_name", "user_id"})
)
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class UserFiles implements DTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_name")
    @NonNull
    private String fileName;

    @Column(name = "server_file_name")
    private String serverFileName;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserFiles(@NonNull String fileName, String serverFileName, User user) {
        this.fileName = fileName;
        this.serverFileName = serverFileName;
        this.user = user;
    }
}
