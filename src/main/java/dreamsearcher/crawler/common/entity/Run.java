package dreamsearcher.crawler.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "runs")
@Data
@AllArgsConstructor
public class Run {
    @Id
    @GeneratedValue
    @Column(name="runId")
    private UUID runId;

    @Column(name="dateTime")
    private String dateTime;

    @Column(name="shopName")
    private String shopName;

    @Column(name="isProcessed")
    private boolean isProcessed;

    @OneToMany(mappedBy = "runs")
    private List<Item> items;
}