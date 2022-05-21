package n2k_.nvi.explosions;
import org.bukkit.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.List;
public class ExplosiveFlow {
    private final Location LOCATION;
    private final int RADIUS;
    public ExplosiveFlow(Location LOCATION, int RADIUS) {
        this.LOCATION = LOCATION;
        this.RADIUS = RADIUS;
    }
    public void start() {
        List<Location> POINT_LIST = BlastWave.getSphere(this.LOCATION, this.RADIUS, true);
        POINT_LIST.forEach(LOCATION -> {
            World WORLD = LOCATION.getWorld();
            assert WORLD != null;
            WORLD.spawnParticle(
                    Particle.CLOUD, LOCATION,
                    1, 0, 1, 0, 0.005
            );
            ExplosiveFlow.drawFlow(this.LOCATION.clone(), LOCATION.clone(), 0.1);
        });
    }
    public static void drawFlow(@NotNull Location POINT1, Location POINT2, double SPACE) {
        World WORLD = POINT1.getWorld();
        assert WORLD != null;
        double DISTANCE = POINT1.distance(POINT2);
        Vector POINT1_VECTOR = POINT1.toVector();
        Vector POINT2_VECTOR = POINT2.toVector();
        Vector VECTOR = POINT2_VECTOR.clone().subtract(POINT1_VECTOR).normalize().multiply(SPACE);
        double LENGHT = 0;
        for(;LENGHT < DISTANCE;POINT1_VECTOR.add(VECTOR)) {
            if(!WORLD.getBlockAt(new Location(WORLD, POINT1_VECTOR.getBlockX(), POINT1_VECTOR.getBlockY(), POINT1_VECTOR.getBlockZ())).isEmpty()) {
                return;
            }
            WORLD.spawnParticle(
                    Particle.FLAME, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                    1, 0, 0, 0, 0.005
            );
            LENGHT += SPACE;
        }
    }
}
