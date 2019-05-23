package enhanced;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class PersistentBase {
       
    protected Long id;
    
    @Id
    @GeneratedValue
    public Long getId() { return id; }

    protected void setId(Long id) { this.id = id; }
    
    
    public PersistentBase() {
    }
    
}