package hw10.dataset;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity @Table(name = "phones", indexes = {
        @Index(name = "phone_user_id", columnList = "user_id")
})
public class PhoneDataSet extends DataSet{
    @Column(nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private UserDataSet user;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {
        this.number = number;
    }
}
