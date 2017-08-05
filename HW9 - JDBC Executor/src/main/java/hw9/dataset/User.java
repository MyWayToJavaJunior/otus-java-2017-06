package hw9.dataset;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity @Table(name = "users")
public class User extends DataSet {
    @Column
    private String name;

    @Column(nullable = false, columnDefinition="default 0")
    private int age;


    public User() {
    }

    @Builder
    public User(long id, String name, int age) {
        super.setId(id);
        this.name = name;
        this.age = age;
    }
}
