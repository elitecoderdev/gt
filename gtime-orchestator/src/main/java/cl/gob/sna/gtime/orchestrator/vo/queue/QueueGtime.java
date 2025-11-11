package cl.gob.sna.gtime.orchestrator.vo.queue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QueueGtime {
	private String user;
	private String xml;
	private Integer idQueue; 
}
