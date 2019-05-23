package enhanced;

import javax.persistence.Entity;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.CollectionOfElements;

@Entity
public class RequestData extends PersistentBase{
    protected String name;
    protected String rarity;
    protected String dps;
    protected String damage;
    protected String env;
    protected String rate;
    protected String mag;
    protected String reload;

    public RequestData() {
    }
    
    public void setName (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getRarity() {
        return rarity;
    }
    
    public void setDps (String dps) {
        this.dps = dps;
    }

    public String getDps() {
        return dps;
    }
    
    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getDamage() {
        return damage;
    }
    
    public void setEnv(String env) {
        this.env = env;
    }

    public String getEnv() {
        return env;
    }
    
    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }
    
    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getMag() {
        return mag;
    }
    
    public void setReload(String reload) {
        this.reload = reload;
    }

    public String getReload() {
        return reload;
    }
}