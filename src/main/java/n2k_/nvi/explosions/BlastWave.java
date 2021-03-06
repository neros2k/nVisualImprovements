package n2k_.nvi.explosions;
import n2k_.nvi.base.APlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
public class BlastWave {
    private final Location LOCATION;
    private final int RADIUS;
    private final APlugin PLUGIN;
    public BlastWave(Location LOCATION, int RADIUS, APlugin PLUGIN) {
        this.LOCATION = LOCATION;
        this.RADIUS = RADIUS;
        this.PLUGIN = PLUGIN;
    }
    public void start() {
        Bukkit.getScheduler().runTaskAsynchronously(this.PLUGIN, () -> {
            for(int I = 1;I<=this.RADIUS;I++) {
                BlastWave.getSphere(this.LOCATION, I, true).forEach(LOCATION -> {
                    World WORLD = LOCATION.getWorld();
                    assert WORLD != null;
                    if(!LOCATION.getBlock().isEmpty()) {
                        WORLD.spawnParticle(
                                Particle.ASH, LOCATION,
                                10, 0, 1, 0, 0.005
                        );
                        WORLD.spawnParticle(
                                Particle.CAMPFIRE_COSY_SMOKE, LOCATION,
                                1, 0, 1, 0, 0.005
                        );
                    }
                });
                try {
                    Thread.sleep(10L);
                } catch(InterruptedException EXCEPTION) {
                    EXCEPTION.printStackTrace();
                }
            }
        });
    }
    @NotNull
    public static List<Location> getSphere(@NotNull Location CENTER, int RADIUS, boolean HOLLOW) {
        List<Location> LOCATION_LIST = new ArrayList<>();
        int BX = CENTER.getBlockX();
        int BY = CENTER.getBlockY();
        int BZ = CENTER.getBlockZ();
        for(int FX = BX-RADIUS; FX<=BX+RADIUS; FX++) {
            for(int FY = BY-RADIUS;FY<=BY+RADIUS;FY++) {
                for(int FZ = BZ-RADIUS;FZ<=BZ+RADIUS;FZ++) {
                    double distance = ((BX -FX)*(BX-FX)+((BZ-FZ)*(BZ-FZ))+((BY-FY)*(BY-FY)));
                    if(distance < RADIUS*RADIUS &&
                       !(HOLLOW && distance < ((RADIUS-1)*(RADIUS-1)))) {
                        Location LOCATION = new Location(CENTER.getWorld(), FX, FY, FZ);
                        LOCATION_LIST.add(LOCATION);
                    }
                }
            }
        }
        return LOCATION_LIST;
    }
}
