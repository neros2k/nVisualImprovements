package n2k_.nvi.explosions;
import n2k_.nvi.base.APlugin;
import org.bukkit.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class ExplosiveLine {
    private final Location LOCATION;
    private final int RADIUS;
    private final APlugin PLUGIN;
    private int SURCHARGE_AMOUNT;
    private double STRENGTH_SURCHARGE;
    public ExplosiveLine(Location LOCATION, int RADIUS, APlugin PLUGIN) {
        this.LOCATION = LOCATION;
        this.RADIUS = RADIUS;
        this.PLUGIN = PLUGIN;
        this.SURCHARGE_AMOUNT = 0;
        this.STRENGTH_SURCHARGE = 0;
    }
    public void start() {
        World WORLD = this.LOCATION.getWorld();
        assert WORLD != null;
        List<Location> POINT_LIST = BlastWave.getSphere(this.LOCATION, this.RADIUS, true);
        this.check(POINT_LIST, WORLD).forEach(POINT ->
        Bukkit.getScheduler().runTaskAsynchronously(this.PLUGIN, () -> {
            try {
                this.draw(POINT, WORLD, true);
            } catch(InterruptedException EXCEPTION) {
                EXCEPTION.printStackTrace();
            }
        }));
    }
    public List<Location> check(@NotNull List<Location> POINT_LIST, World WORLD) {
        List<Location> CHECKED_POINT_LIST = new ArrayList<>();
        POINT_LIST.forEach(POINT -> {
            try {
                if(this.draw(POINT, WORLD, false)) {
                    CHECKED_POINT_LIST.add(POINT);
                    this.SURCHARGE_AMOUNT++;
                }
            } catch(InterruptedException EXCEPTION) {
                EXCEPTION.printStackTrace();
            }
        });
        return CHECKED_POINT_LIST;
    }
    public boolean draw(Location POINT, World WORLD, boolean VISUAL) throws InterruptedException {
        Random RANDOM = new Random();
        double DISTANCE = this.LOCATION.distance(POINT);
        if(VISUAL && this.SURCHARGE_AMOUNT != 0 && this.STRENGTH_SURCHARGE != 0) {
            DISTANCE+=((this.STRENGTH_SURCHARGE/this.SURCHARGE_AMOUNT)/20.5)+RANDOM.nextInt(5)
                                                                            -RANDOM.nextInt(5);
        }
        Vector POINT1_VECTOR = this.LOCATION.toVector();
        Vector POINT2_VECTOR = POINT.toVector();
        Vector VECTOR = POINT2_VECTOR.clone().subtract(POINT1_VECTOR).normalize().multiply(0.1);
        double LENGHT = 0;
        boolean SHRAPNEL = false;
        boolean SMOKE = false;
        if(VISUAL) {
            if(RANDOM.nextInt(100) > 70) {
                SMOKE = true;
                DISTANCE/=1.5+RANDOM.nextInt(2);
            } else if(RANDOM.nextInt(100) > 80) {
                SHRAPNEL = true;
                DISTANCE*=2-RANDOM.nextInt(5);
            }
        }
        for(;LENGHT < DISTANCE;POINT1_VECTOR.add(VECTOR)) {
            Location LOCATION = new Location(WORLD, POINT1_VECTOR.getBlockX(), POINT1_VECTOR.getBlockY(), POINT1_VECTOR.getBlockZ());
            if(!LOCATION.getBlock().isEmpty()) {
                if(VISUAL){
                    WORLD.spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE, LOCATION,
                            1, 0, 0, 0, 0.05
                    );
                    WORLD.spawnParticle(
                            Particle.LAVA, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                } else {
                    this.STRENGTH_SURCHARGE+=DISTANCE-LENGHT;
                }
                return false;
            }
            if(VISUAL) {
                if(SHRAPNEL) {
                    WORLD.spawnParticle(
                            Particle.FLAME, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                }
                if(SMOKE) {
                    WORLD.spawnParticle(
                            Particle.SMOKE_LARGE, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                    if(RANDOM.nextInt(140) > 130) {
                        WORLD.spawnParticle(
                                Particle.CAMPFIRE_COSY_SMOKE, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                                1, 0, 0, 0, 0.005
                        );
                    }
                }
                if(RANDOM.nextInt(100) > 70) {
                    WORLD.spawnParticle(
                            Particle.FLAME, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                }
                if(RANDOM.nextInt(150) > 130) {
                    WORLD.spawnParticle(
                            Particle.SMOKE_NORMAL, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                }
                if(DISTANCE < DISTANCE/((40-RANDOM.nextDouble(10))/10) && RANDOM.nextInt(100) > 40) {
                    WORLD.spawnParticle(
                            Particle.FLAME, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                    WORLD.spawnParticle(
                            Particle.LAVA, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                    if(RANDOM.nextInt(140) > 130) {
                        WORLD.spawnParticle(
                                Particle.CAMPFIRE_COSY_SMOKE, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                                1, 0, 0, 0, 0.5
                        );
                    }
                }
                if(RANDOM.nextInt(300) > 270) {
                    WORLD.spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                }
                if(RANDOM.nextInt(300) > 298) {
                    WORLD.spawnParticle(
                            Particle.CLOUD, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                }
                if(RANDOM.nextInt(170) > 150) {
                    WORLD.spawnParticle(
                            Particle.LAVA, POINT1_VECTOR.getX(), POINT1_VECTOR.getY(), POINT1_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                }
            }
            LENGHT+=0.1;
            if(VISUAL) Thread.sleep(2L);
        }
        return true;
    }
}