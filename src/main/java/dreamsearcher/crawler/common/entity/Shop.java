package dreamsearcher.crawler.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "shops")
@Data
@AllArgsConstructor
public class Shop {

    @Column(name = "name")
    private String name;

}