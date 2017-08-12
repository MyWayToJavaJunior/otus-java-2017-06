package hw10.dataset;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class DataSet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
}
