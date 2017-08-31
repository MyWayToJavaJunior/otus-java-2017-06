package hw10.dataset;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity @Table(name = "users")
public class UserDataSet extends DataSet {
    public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private String name;

    private String password;

    @Column(nullable = false)
    private int age;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AddressDataSet address;

    private String role = "USER";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PhoneDataSet> phones = new ArrayList<>();

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public UserDataSet() {
    }

    public UserDataSet(String name, String password, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        this.name = name;
        this.password = PASSWORD_ENCODER.encode(password);
        this.age = age;
        this.address = address;
        this.address.setUser(this);
        this.phones = phones;
        this.phones.forEach(phone -> phone.setUser(this));
    }
}

