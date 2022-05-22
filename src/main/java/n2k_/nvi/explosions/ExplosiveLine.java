package n2k_.nvi.explosions;
import n2k_.nvi.base.APlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class ExplosiveLine {
    private final Location LOCATION;
    private final int RADIUS;
    private final APlugin PLUGIN;
    private final List<Block> EXPLODE_LIST;
    private int SURCHARGE_AMOUNT;
    private double STRENGTH_SURCHARGE;
    public ExplosiveLine(Location LOCATION, int RADIUS, APlugin PLUGIN, List<Block> EXPLODE_LIST) {
        this.LOCATION = LOCATION;
        this.RADIUS = RADIUS;
        this.PLUGIN = PLUGIN;
        this.EXPLODE_LIST = EXPLODE_LIST;
        this.SURCHARGE_AMOUNT = 0;
        this.STRENGTH_SURCHARGE = 0;
    }
    public void start() {
        World WORLD = this.LOCATION.getWorld();
        assert WORLD != null;
        List<Location> POINT_LIST = BlastWave.getSphere(this.LOCATION, this.RADIUS, true);
        this.check(POINT_LIST, WORLD).forEach(POINT ->
            Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
                try {
                    this.draw(POINT, WORLD, true);
                } catch(InterruptedException EXCEPTION) {
                    EXCEPTION.printStackTrace();
                }
        }));
        Bukkit.getScheduler().runTaskLater(PLUGIN, () ->
                this.EXPLODE_LIST.forEach(Block::breakNaturally),
                6L);
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
        Vector LOCATION_VECTOR = this.LOCATION.toVector();
        Vector POINT_VECTOR = POINT.toVector();
        Vector VECTOR = POINT_VECTOR.clone().subtract(LOCATION_VECTOR).normalize().multiply(0.1);
        double LENGTH = 0;
        boolean SHRAPNEL = false;
        boolean SMOKE = false;
        if(VISUAL) {
            if(RANDOM.nextInt(100) > 70) {
                SMOKE = true;
                DISTANCE/=1.5+RANDOM.nextInt(2);
                if(RANDOM.nextBoolean()) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                            WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 10, 1),
                            RANDOM.nextInt(3));
                } else {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                            WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 10, 1),
                            RANDOM.nextInt(3));
                }
            } else if(RANDOM.nextInt(100) > 80) {
                SHRAPNEL = true;
                DISTANCE*=2-RANDOM.nextInt(5);
                if(RANDOM.nextInt(100) > 90) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                            WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, RANDOM.nextInt(5), 1),
                            RANDOM.nextInt(10));
                }
            }
        }
        for(;LENGTH < DISTANCE;LOCATION_VECTOR.add(VECTOR)) {
            Location LOCATION = new Location(WORLD, LOCATION_VECTOR.getBlockX(), LOCATION_VECTOR.getBlockY(), LOCATION_VECTOR.getBlockZ());
            if(!LOCATION.getBlock().isEmpty()) {
                if(VISUAL){
                    WORLD.spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE, LOCATION,
                            1, 0, 0, 0, 0.05
                    );
                    WORLD.spawnParticle(
                            Particle.LAVA, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );

                } else {
                    this.STRENGTH_SURCHARGE+=DISTANCE-LENGTH;
                }
                if(this.EXPLODE_LIST.contains(LOCATION.getBlock())) {
                    if(VISUAL) {
                        Bukkit.getScheduler().runTask(PLUGIN, () -> {
                            World SYNC_WORLD = Bukkit.getWorld(WORLD.getName());
                            assert SYNC_WORLD != null;
                            Block BLOCK = SYNC_WORLD.getBlockAt(LOCATION);
                            if(BLOCK.getType() == Material.TNT) {
                                BLOCK.setType(Material.AIR);
                                int FUSE;
                                int TNT_DISTANCE = (int) this.LOCATION.distance(BLOCK.getLocation());
                                if(TNT_DISTANCE<=1) {
                                    FUSE = 0;
                                } else {
                                    FUSE = TNT_DISTANCE*RANDOM.nextInt(5);
                                }
                                ((TNTPrimed) WORLD.spawnEntity(BLOCK.getLocation(), EntityType.PRIMED_TNT)).setFuseTicks(FUSE);
                                return;
                            }
                            BLOCK.breakNaturally();
                            this.EXPLODE_LIST.remove(BLOCK);
                        });
                        if(RANDOM.nextInt(100) > 90) {
                            if(RANDOM.nextInt(100) > 95) {
                                if(RANDOM.nextBoolean()) {
                                    Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                                                    WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1),
                                            RANDOM.nextInt(10));
                                } else {
                                    Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                                                    WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1),
                                            RANDOM.nextInt(10));
                                }
                            } else {
                                Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                                                WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_BLAST, RANDOM.nextInt(1), 1),
                                        RANDOM.nextInt(5));
                            }
                        }
                    } else {
                        return RANDOM.nextInt(100) < 90;
                    }
                } else {
                    if(VISUAL && RANDOM.nextInt(100) > 60) {
                        if(RANDOM.nextBoolean()) {
                            Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                                            WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1),
                                    RANDOM.nextInt(10));
                        } else {
                            Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () ->
                                            WORLD.playSound(LOCATION, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1),
                                    RANDOM.nextInt(10));
                        }
                    }
                    return false;
                }
            }
            if(VISUAL) {
                if(SHRAPNEL) {
                    WORLD.spawnParticle(
                            Particle.FLAME, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                }
                if(SMOKE) {
                    WORLD.spawnParticle(
                            Particle.SMOKE_LARGE, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                    if(RANDOM.nextInt(140) > 130) {
                        WORLD.spawnParticle(
                                Particle.CAMPFIRE_COSY_SMOKE, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                                1, 0, 0, 0, 0.005
                        );
                    }
                }
                if(RANDOM.nextInt(100) > 70) {
                    WORLD.spawnParticle(
                            Particle.FLAME, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                }
                if(RANDOM.nextInt(150) > 130) {
                    WORLD.spawnParticle(
                            Particle.SMOKE_NORMAL, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                }
                if(DISTANCE < DISTANCE/((40-RANDOM.nextDouble(10))/10) && RANDOM.nextInt(100) > 40) {
                    WORLD.spawnParticle(
                            Particle.FLAME, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.005
                    );
                    WORLD.spawnParticle(
                            Particle.LAVA, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                    if(RANDOM.nextInt(140) > 130) {
                        WORLD.spawnParticle(
                                Particle.CAMPFIRE_COSY_SMOKE, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                                1, 0, 0, 0, 0.5
                        );
                    }
                }
                if(RANDOM.nextInt(300) > 270) {
                    WORLD.spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                }
                if(RANDOM.nextInt(300) > 298) {
                    WORLD.spawnParticle(
                            Particle.CLOUD, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                }
                if(RANDOM.nextInt(170) > 150) {
                    WORLD.spawnParticle(
                            Particle.LAVA, LOCATION_VECTOR.getX(), LOCATION_VECTOR.getY(), LOCATION_VECTOR.getZ(),
                            1, 0, 0, 0, 0.05
                    );
                }
            }
            LENGTH+=0.1;
            if(VISUAL) {
                if(SHRAPNEL) {
                    Thread.sleep(4L);
                } else {
                    Thread.sleep(3L);
                }
            }
        }
        return true;
    }
}