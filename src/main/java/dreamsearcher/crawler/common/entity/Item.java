package dreamsearcher.crawler.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "itemId")
    private UUID itemId;

    @ManyToOne
    @JoinColumn(name="run_id", nullable=false)
    private Run run;

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "link")
    private String link;

    @Column(name = "price")
    private double price;
}