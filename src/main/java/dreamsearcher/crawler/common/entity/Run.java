package dreamsearcher.crawler.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "runs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Run {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name="run_id", nullable=false)
    private String runId;

    @Column(name="date_time")
    private String dateTime;

    @Column(name="shop_name")
    private String shopName;

    @Column(name="is_processed")
    private boolean isProcessed;

    @OneToMany(mappedBy = "run")
    private Set<Item> items;
}