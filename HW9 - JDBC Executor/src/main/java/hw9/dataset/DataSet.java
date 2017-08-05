package hw9.dataset;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public abstract class DataSet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
}
