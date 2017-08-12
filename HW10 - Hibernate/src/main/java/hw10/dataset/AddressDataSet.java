package hw10.dataset;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity @Table(name = "addresses", indexes = {
        @Index(name = "address_user_id", columnList = "user_id")
})
public class AddressDataSet extends DataSet {
    @Column(nullable = false)
    private String street;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private UserDataSet user;

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }
}
