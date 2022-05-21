package n2k_.nvi.explosions;
import org.bukkit.*;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
public class ExplosiveLine {
    private final Location LOCATION;
    private final int RADIUS;
    private int SURCHARGE_AMOUNT;
    private double STRENGTH_SURCHARGE;
    public ExplosiveLine(Location LOCATION, int RADIUS) {
        this.LOCATION = LOCATION;
        this.RADIUS = RADIUS;
        this.SURCHARGE_AMOUNT = 0;
        this.STRENGTH_SURCHARGE = 0;
    }
    public void start() {
        World WORLD = this.LOCATION.getWorld();
        assert WORLD != null;
        List<Location> POINT_LIST = BlastWave.getSphere(this.LOCATION, this.RADIUS, true);
        this.check(POINT_LIST, WORLD).forEach(POINT -> {
            this.draw(POINT, WORLD, true);
        });
    }
    public List<Location> check(List<Location> POINT_LIST, World WORLD) {
        List<Location> CHECKED_POINT_LIST = new ArrayList<>();
        POINT_LIST.forEach(POINT -> {
            if(this.draw(POINT, WORLD, false)) {
                CHECKED_POINT_LIST.add(POINT);
                this.SURCHARGE_AMOUNT++;
            }
        });
        return CHECKED_POINT_LIST;
    }
    public boolean draw(Location POINT, World WORLD, boolean VISUAL) {
        double DISTANCE = this.LOCATION.distance(POINT);
        if(VISUAL && this.SURCHARGE_AMOUNT != 0 && this.STRENGTH_SURCHARGE != 0) {
            DISTANCE+=(this.STRENGTH_SURCHARGE/this.SURCHARGE_AMOUNT)/20.5;
        }
        Vector POINT1_VECTOR = this.LOCATION.toVector();
        Vector POINT2_VECTOR = POINT.toVector();
        Vector VECTOR = POINT2_VECTOR.clone().subtract(POINT1_VECTOR).normalize().multiply(0.1);
        double LENGHT = 0;
        for(;LENGHT < DISTANCE;POINT1_VECTOR.add(VECTOR)) {
            Location LOCATION = new Location(WORLD, POINT1_VECTOR.getBlockX(), POINT1_VECTOR.getBlockY(), POINT1_VECTOR.getBlockZ());
            if(!LOCATION.getBlock().isEmpty()) {
                if(!VISUAL) this.STRENGTH_SURCHARGE+=DISTANCE-LENGHT;
                return false;
            }
            if(VISUAL) {
                WORLD.spawnParticle(
                        Particle.LAVA, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                        2, 0, 0, 0, 0.005
                );
                WORLD.spawnParticle(
                        Particle.FLAME, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                        1, 0, 0, 0, 0.005
                );
            }
            LENGHT+=0.1;
        }
        return true;
    }
}
//public void draw(Location POINT, World WORLD, boolean CALCULATE) {
//        double DISTANCE = this.LOCATION.distance(POINT);
//        if(this.SURCHARGE_AMOUNT != 0 && this.STRENGTH_SURCHARGE != 0) {
//            DISTANCE+=this.STRENGTH_SURCHARGE/this.SURCHARGE_AMOUNT;
//        }
//        Vector POINT1_VECTOR = this.LOCATION.toVector();
//        Vector POINT2_VECTOR = POINT.toVector();
//        Vector VECTOR = POINT2_VECTOR.clone().subtract(POINT1_VECTOR).normalize().multiply(0.1);
//        double LENGHT = 0;
//        for(;LENGHT < DISTANCE;POINT1_VECTOR.add(VECTOR)) {
//            Location LOCATION = new Location(WORLD, POINT1_VECTOR.getBlockX(), POINT1_VECTOR.getBlockY(), POINT1_VECTOR.getBlockZ());
//            if(!LOCATION.getBlock().isEmpty()) {
//                if(CALCULATE) this.STRENGTH_SURCHARGE+=DISTANCE-LENGHT;
//                return;
//            }
//            LENGHT+=0.1;
//        }
//        this.SURCHARGE_AMOUNT++;
//    }

//WORLD.spawnParticle(
//                    Particle.FLAME, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
//                    1, 0, 0, 0, 0.005
//            );