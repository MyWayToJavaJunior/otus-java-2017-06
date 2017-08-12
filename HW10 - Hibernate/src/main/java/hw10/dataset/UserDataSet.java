package hw10.dataset;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity @Table(name = "users")
public class UserDataSet extends DataSet {
    @Column
    private String name;

    @Column(nullable = false)
    private int age;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PhoneDataSet> phones = new ArrayList<>();


    public UserDataSet() {
    }

    public UserDataSet(String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.address.setUser(this);
        this.phones = phones;
        this.phones.forEach(phone -> phone.setUser(this));
    }
}

