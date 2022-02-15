package dreamsearcher.crawler.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "runs")
@Data
@AllArgsConstructor
public class Run {
    @Id
    @GeneratedValue
    @Column(name="run_id", nullable=false)
    private String runId;

    @Column(name="date_time")
    private String dateTime;

    @Column(name="shop_name")
    private String shopName;

    @Column(name="is_processed")
    private boolean isProcessed;
}