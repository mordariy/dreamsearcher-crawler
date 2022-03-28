package dreamsearcher.crawler.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "item_id", nullable = false)
    private String itemId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="run_id", referencedColumnName="run_id", nullable=false)
    private Run run;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "link")
    private String link;

    @Column(name = "price")
    private double price;
}